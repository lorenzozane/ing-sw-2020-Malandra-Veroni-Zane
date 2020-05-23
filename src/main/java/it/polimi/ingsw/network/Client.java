package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.cli.Cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends MessageForwarder {

    public enum UserInterface {
        CLI,
        GUI;
    }

    private final String ip;
    private final int port;
    private volatile boolean active = true;
    private UserInterface chosenUserInterface;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final UpdateTurnMessageSender updateTurnMessageSender = new UpdateTurnMessageSender();
    private final PlayerMoveReceiver playerMoveReceiver = new PlayerMoveReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();

    //    private boolean gui;

    /**
     * Constructor of Client
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public synchronized boolean isActive() {
        return active;
    }


    public synchronized void setActive(boolean active) {
        this.active = active;
    }

//    public void setGui(boolean gui){
//        this.gui = true;
//    }


    /**
     * Creation of thread that handle the read from socket
     *
     * @param socketIn Client socket where the client receives information
     * @return The that thread
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    //TODO: da cambiare appena sappiamo cosa inviamo esattamente
                    Object inputObject = socketIn.readObject();
                    if (inputObject instanceof String) {
                        System.out.println((String) inputObject);
                    } else if (inputObject instanceof Board) {
                        ((Cli) inputObject).printGameBoard();
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }


    /**
     * Creation of thread that handle the write to socket
     *
     * @param socketOut Client socket where the client send information
     * @param stdin     Scanner for read the input of client
     * @return The that thread
     */
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdin.nextLine();
                    if(inputLine.equalsIgnoreCase("exit")) {
                        System.out.println("Closing connection...");
                        throw new Exception();
                    }
                    socketOut.println(inputLine);
                    socketOut.flush();
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }


    /**
     * Connection to the server and creation of threads to handle input/output
     *
     * @throws IOException Is thrown if an I/O error occurs while reading stream header
     */
    public void run() throws IOException {
        Socket socket = new Socket(ip, port);

        try (
                socket;
                ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
                Scanner stdin = new Scanner(System.in)) {

            asyncReadFromSocket(socketIn);
            asyncWriteToSocket(stdin, socketOut);

            while (isActive()) ;

        } catch (Exception e) {
            System.out.println("Connection closed by Exception");
        } finally {
            System.out.println("Connection closed");
        }
    }





    @Override
    protected void handlePlayerMove(PlayerMove message) {
        System.out.println("Received player move");
    }

    @Override
    protected void handleUpdateTurn(UpdateTurnMessage message) {
        System.out.println("Received update turn message");
    }

    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        System.out.println("Received player move startup");
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public PlayerMoveReceiver getPlayerMoveReceiver() {
        return playerMoveReceiver;
    }

    public PlayerMoveStartupReceiver getPlayerMoveStartupReceiver() {
        return playerMoveStartupReceiver;
    }

    public void addUpdateTurnMessageObserver(Observer<UpdateTurnMessage> observer) {
        updateTurnMessageSender.addObserver(observer);
    }

    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }
}
