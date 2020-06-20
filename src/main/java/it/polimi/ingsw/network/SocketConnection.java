package it.polimi.ingsw.network;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.UpdateTurnMessage;
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
     * Constructor of SocketConnection
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
     * Send an object by socket
     *
     * @param message The message we want to send out
     */
    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }


    /**
     * Close the socketConnection
     */
    public synchronized void closeConnection() {
        //send("Connection closed! socketConnection.closeconnection");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }


    /**
     * Send an object by socket with thread
     */
    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
    }

    public ObjectInputStream getIn() {
        return in;
    }


    /**
     * It set the unique name for the client and keeps alive the socket
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
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
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

            while (isActive()) {
                inputObject = in.readObject();

                if (inputObject instanceof PlayerMoveStartup) {
                    handlePlayerMoveStartup((PlayerMoveStartup) inputObject);

                } else if (inputObject instanceof PlayerMove) {
                    handlePlayerMove((PlayerMove) inputObject);
                } else
                    asyncSend(Message.error + " Exception thrown from SocketConnection.run");

            }
        } catch (IOException | NoSuchElementException | ParseException | ClassNotFoundException e) {
            //e.printStackTrace();
            System.err.println("Exception thrown from SocketConnection.run " + e.getMessage());
            try {
                server.deregisterConnection(nickname);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } finally {
            closeConnection();
        }
    }


    /**
     * Check if the nickname chosen by client has not already been taken
     *
     * @return true if it is not legal otherwise false
     */
    private boolean illegalNicknameChecker(String nickname) {
        return server.getNicknameDatabase().contains(nickname) || nickname.equals("");
    }


    /**
     * Check if the input string about the birthday date is legal
     *
     * @param s String from user input that must be checked
     * @return true if the string is correctly formatted otherwise false
     */
    public static boolean dateChecker(String s) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = dateFormat.parse(s);
            return s.equals(dateFormat.format(date));
        } catch (ParseException e) {
            return false;
        }

    }

    @Override
    protected void handleUpdateTurnMessage(UpdateTurnMessage message) { // arriva dalla remoteview e va mandato al client
        if (message.getNextMove() == TurnEvents.Actions.QUIT &&
            message.getCurrentPlayer().getNickname().equalsIgnoreCase(playerOwnerNickname)) {
            server.deregisterOnePlayer(this.playerOwnerNickname);
        } else {
            asyncSend(message);
        }
    }

    protected void handlePlayerMove(PlayerMove message) {  // arriva dal socket del client e va mandato alla remoteview
        playerMoveSender.notifyAll(message);
    }

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
