package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketConnection extends Observable<String> implements Connection, Runnable {

    private final Game gameInstance;
    private Socket socket;
    private ObjectOutputStream out;
    private Server server;
    private boolean active = true;


    /**
     * Constructor of SocketConnection
     *
     */
    public SocketConnection(Socket socket, Server server, Game gameInstance) {
        this.gameInstance = gameInstance;
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
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
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }


    /**
     * Close the socketConnection
     *
     */
    @Override
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
    private void close(String nickname){
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(nickname, this);
        System.out.println("Done!");
    }


    /**
     * Send an object by socket with thread
     *
     */
    @Override
    public void asyncSend(final Object message){
        new Thread(() -> send(message)).start();
    }


    /**
     * It set the unique name for the client and keeps alive the socket
     *
     */
    @Override
    public void run() {
        Scanner in;
        String nickname = "";
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(Message.santorini);
            send(Message.chooseNickname);
            String read = in.nextLine();
            nickname = read;
            while(!nicknameChecker(nickname)){
                send(Message.chooseNicknameAgain);
                read = in.nextLine();
                nickname = read;
                }
            server.addNickname(nickname);
            this.asyncSend(Message.lobby);
            server.lobby(nickname, this);
            while(isActive()){
                read = in.nextLine();
                notifyAll(read);
            }
        } catch (IOException | NoSuchElementException | IllegalAccessException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            server.deregisterConnection(nickname, this);
            gameInstance.removePlayerByName(nickname);
            close(nickname);
        }
    }

    /**
     * Check if the name chosen by client has not already been taken
     *
     * @return true if it is legal otherwise false
     */
    private boolean nicknameChecker(String nickname){
        return !server.getNicknameDatabase().contains(nickname);
    }

    @Override
    public Socket getSocket(){
        return this.socket;
    }

}
