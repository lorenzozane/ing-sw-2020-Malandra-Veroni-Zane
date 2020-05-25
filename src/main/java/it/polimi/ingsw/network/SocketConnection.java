package it.polimi.ingsw.network;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
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

public class SocketConnection extends MessageForwarder implements /*Connection,*/ Runnable {

    //private final Game gameInstance;
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
    public SocketConnection(Socket socket, Server server/*, Game gameInstance*/) {
        //this.gameInstance = gameInstance;
        this.socket = socket;
        this.server = server;
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
            /*ObjectMapper objectMapper = new ObjectMapper();
            String serialized = null;
            serialized = objectMapper.writeValueAsString(message);
             */
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }


    /**
     * Close the socketConnection
     */
    /*@Override*/
    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }


    /**
     * Close the nickname's socketConnection
     *
     * @param nickname The client unique name
     */
    private void closeSocket(String nickname) throws IOException, IllegalAccessException, ParseException {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(nickname, this);
        System.out.println("Done!");
    }


    /**
     * Send an object by socket with thread
     */
    /*@Override*/
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
            out = new ObjectOutputStream(socket.getOutputStream());
            //asyncSend(Message.santorini);

            asyncSend(Message.chooseNickname);
            Object inputObject = in.readObject();
            while(true) {
                if (inputObject instanceof String) {
                    if(!(illegalNicknameChecker(((String) inputObject)))){
                        nickname = (String) inputObject;
                        break;
                    }
                }
                send(Message.chooseNicknameAgain);
                inputObject = in.readObject();
            }
            asyncSend("Nickname: " + nickname);
            server.addNickname(nickname);

            asyncSend(Message.birthday);
            inputObject = in.readObject();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date playerBirthday;
            while(true) {
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


            asyncReadFromSocket(in);
            while (isActive());

        } catch (IOException | NoSuchElementException | IllegalAccessException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error!" + e.getMessage());
        } finally {
            try {
                server.deregisterConnection(nickname, this);
            } catch (IOException | IllegalAccessException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creation of thread that handle the read from socket
     *
     * @param socketIn Server socket where the client receives information
     * @return The that thread
     */
    public Thread asyncReadFromSocket(ObjectInputStream socketIn) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();


                    if(inputObject instanceof PlayerMoveStartup){
                        handlePlayerMoveStartup((PlayerMoveStartup) inputObject);

                    }
                    else if(inputObject instanceof PlayerMove){
                        handlePlayerMove((PlayerMove) inputObject);
                    }


                }
            } catch (Exception e) {
                this.active = false;
            }
        });
        t.start();
        return t;
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

    /*@Override*/
    public Socket getSocket() {
        return this.socket;
    }




    @Override
    protected void handleUpdateTurn(UpdateTurnMessage message) { // arriva dalla remoteview e va mandato al client
        asyncSend(message);
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
