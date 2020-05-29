package it.polimi.ingsw.network;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.View;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends MessageForwarder {

    public enum UserInterface {
        CLI,
        GUI
    }

    private final String ip;
    private final int port;
    private final Socket socket;
    protected ObjectOutputStream out;
    private View clientView;
    private volatile boolean active = true;
    private UserInterface chosenUserInterface = null;
    private final UpdateTurnMessageSender updateTurnMessageSender = new UpdateTurnMessageSender();
    private final PlayerMoveReceiver playerMoveReceiver = new PlayerMoveReceiver();
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();
    //private final StringReceiver stringReceiver = new StringReceiver();

    /**
     * Constructor of Client
     */
    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public void setChosenUserInterface(View clientView, UserInterface chosenUserInterface) {
        if (this.chosenUserInterface == null) {
            this.chosenUserInterface = chosenUserInterface;
            this.clientView = clientView;
        }
    }

    /**
     * Creation of thread that handle the read from socket
     *
     * @param socketIn Client socket where the client receives information
     * @return The that thread
     */
    public Thread asyncReadFromSocket(ObjectInputStream socketIn) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();


                    if(inputObject instanceof String){
                        if (((String) inputObject).contains("Nickname: "))
                            clientView.setPlayerOwnerNickname(((String) inputObject).replace("Nickname: ", ""));
                        else
                            clientView.showMessage((String) inputObject);
                        //notify()

                    }
                    else if(inputObject instanceof UpdateTurnMessage){
                        handleUpdateTurn((UpdateTurnMessage) inputObject);
                    }


                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

/*
    public Thread asyncSend(Object message) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    socketOut = new ObjectOutputStream()
                    if(((String) message).equalsIgnoreCase("quit")) { // si potrebbe spostare anche un po piu a monte
                        System.out.println("Closing connection...");
                        throw new Exception();
                    }
                    socketOut.writeObject(message);
                    socketOut.flush();
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }
    */

    /**
     * Send an object by socket with thread
     */
    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
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
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Connection to the server and creation of threads to handle input/output
     *
     * //@throws IOException Is thrown if an I/O error occurs while reading stream header
     */
    public void run() {
        ObjectInputStream socketIn;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            socketIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            asyncReadFromSocket(socketIn);

            while (isActive());
        } catch (Exception e) {
            System.out.println("Connection closed by Exception");
        } finally {
            System.out.println("Connection closed");
            closeConnection();
        }
    }

    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }


    protected void handleUpdateTurn(UpdateTurnMessage message) {
        updateTurnMessageSender.notifyAll(message);
    }

    @Override
    protected void handlePlayerMove(PlayerMove message) {
        asyncSend(message);
    }

    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        asyncSend(message);
    }

//    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
//        return updateTurnMessageReceiver;
//    }

    /*@Override
    protected void handleString(String message){ asyncSend(message);}

    public StringReceiver getStringReceiver(){ return stringReceiver; }

     */

    public PlayerMoveReceiver getPlayerMoveReceiver() {
        return playerMoveReceiver;
    }

    public PlayerMoveStartupReceiver getPlayerMoveStartupReceiver() {
        return playerMoveStartupReceiver;
    }

    public void addUpdateTurnMessageObserver(Observer<UpdateTurnMessage> observer) {
        updateTurnMessageSender.addObserver(observer);
    }
}
