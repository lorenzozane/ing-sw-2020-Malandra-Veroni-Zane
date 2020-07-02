package it.polimi.ingsw.network;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;

public class SocketConnection extends MessageForwarder implements Runnable {

    //private final Game gameInstance;
    private String playerOwnerNickname;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Server server;
    private boolean active = true;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();


    /**
     * Constructor of SocketConnection.
     *
     * @param socket is the socket accepted by the server.
     * @param server is the server that accept the connection.
     */
    public SocketConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isActive() {
        return active;
    }

    /**
     * Send an object by socket.
     *
     * @param message The message we want to send out.
     */
    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.out.println("Error during " + this.playerOwnerNickname + " connection on sending Object");
        }

    }

    /**
     * Close the socketConnection.
     */
    public synchronized void closeConnection() {
        //send("Connection closed! socketConnection.closeConnection");
        try {
            socket.close();
            System.out.println("Closing " + this.playerOwnerNickname + " connection");
        } catch (IOException e) {
            System.out.println("Error during close " + this.playerOwnerNickname + " connection");
        }
        active = false;
    }

    /**
     * Send an object by socket with thread.
     */
    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
    }

    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * It set the unique name, birthday for the client on register phase. Than wait until the game is started and keeps alive the socket.
     */
    @Override
    public void run() {
        String nickname = "";
        try {
            in = new ObjectInputStream(socket.getInputStream());

            asyncSend(Message.chooseNickname);
            Object inputObject = in.readObject();
            while (true) {
                if (inputObject instanceof String) {
                    if (!(illegalNicknameChecker(((String) inputObject)))) {
                        nickname = (String) inputObject;
                        break;
                    }
                }
                send(Message.chooseNicknameAgain);
                inputObject = in.readObject();
            }
            asyncSend("Nickname: " + nickname);
            server.addNickname(nickname);
            playerOwnerNickname = nickname;

            asyncSend(Message.birthday);
            inputObject = in.readObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date playerBirthday;
            while (true) {
                if (inputObject instanceof String) {
                    if (dateChecker((String) inputObject)) {
                        playerBirthday = dateFormat.parse((String) inputObject);
                        break;
                    }
                }
                send(Message.birthdayAgain);
                inputObject = in.readObject();
            }

            asyncSend(Message.lobby);
            server.lobby(nickname, playerBirthday, this);


            while (server.getWaitingConnection().containsKey(nickname)) ;

            asyncSend(Message.gameLoading);

            Thread.sleep(1000);

            while (isActive()) {
                inputObject = in.readObject();

                if (inputObject instanceof PlayerMoveStartup) {
                    handlePlayerMoveStartup((PlayerMoveStartup) inputObject);

                } else if (inputObject instanceof PlayerMove) {
                    handlePlayerMove((PlayerMove) inputObject);
                } else
                    asyncSend(Message.error + " Exception thrown from SocketConnection.run");

            }
        } catch (IOException | NoSuchElementException | ParseException | ClassNotFoundException | InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Error: client " + this.playerOwnerNickname + " is not longer reachable");
            server.deregisterConnection(nickname);

        } finally {
            closeConnection();
        }
    }

    /**
     * Check if the nickname chosen by client has not already been taken.
     *
     * @return true if it is not legal otherwise false.
     */
    private boolean illegalNicknameChecker(String nickname) {
        return server.getNicknameDatabase().contains(nickname) || nickname.equals("");
    }

    /**
     * Check if the input string about the birthday date is legal.
     *
     * @param s String from user input that must be checked.
     * @return true if the string is correctly formatted otherwise false.
     */
    public static boolean dateChecker(String s) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(s);
            return s.equals(dateFormat.format(date));
        } catch (ParseException e) {
            return false;
        }

    }

    /**
     * Method to override to handle message received by the UpdateTurnMessageReceiver.
     * Handle QUIT and GAME_END action.
     *
     * @param message The message to handle.
     */
    @Override
    protected void handleUpdateTurnMessage(UpdateTurnMessage message) { // arriva dalla remoteview e va mandato al client
        try {
            if ((message.getNextMove() == TurnEvents.Actions.QUIT &&
                    message.getCurrentPlayer().getNickname().equalsIgnoreCase(playerOwnerNickname)) ||
                    (message.getNextMove() == TurnEvents.Actions.GAME_END &&
                            message.getCurrentPlayer().getNickname().equalsIgnoreCase(playerOwnerNickname))) {
                asyncSend(message);
                Thread.sleep(100);
                server.deregisterOnePlayer(this.playerOwnerNickname);
                this.closeConnection();
            } else {
                asyncSend(message);
            }
        } catch (InterruptedException ex) {
            System.out.println("Exception thrown from SocketConnection.handleUpdateTurnMessage: " + ex.getMessage());
        }
    }

    /**
     * Method to override to handle message received by the PlayerMoveReceiver.
     *
     * @param message The message to handle.
     */
    protected void handlePlayerMove(PlayerMove message) {  // arriva dal socket del client e va mandato alla remoteview
        playerMoveSender.notifyAll(message);
    }

    /**
     * Method to override to handle message received by the PlayerMoveStartupReceiver.
     *
     * @param message The message to handle.
     */
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        playerMoveStartupSender.notifyAll(message);
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }
}
