package it.polimi.ingsw.network;

import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketConnection extends Observable<String> implements Connection, Runnable {

    //private final Game gameInstance;
    private Socket socket;
    private ObjectOutputStream out;
    private Server server;
    private boolean active = true;


    /**
     * Constructor of SocketConnection
     *
     */
    public SocketConnection(Socket socket, Server server/*, Game gameInstance*/) {
        //this.gameInstance = gameInstance;
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
    private void close(String nickname) throws IOException, IllegalAccessException, ParseException {
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
//            nickname = read;
            while(!nicknameChecker(read)){
                send(Message.chooseNicknameAgain);
                read = in.nextLine();
//                nickname = read;
                }
            nickname = read;
            server.addNickname(nickname);

            asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat();
            while (!Server.dateChecker(read)) {
                asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            Date playerBirthday = dateFormat.parse(read);

            this.asyncSend(Message.lobby);
            server.lobby(nickname, playerBirthday, this);


            read = in.nextLine();
            while (!Server.colorChecker(read)) {
                asyncSend(Message.chooseColorAgain);
                read = in.nextLine();
                //notifica al controller
            }



            while(isActive()){
                read = in.nextLine();
                notifyAll(read);
            }
        } catch (IOException | NoSuchElementException | IllegalAccessException | ParseException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            try {
                server.deregisterConnection(nickname, this);
                close(nickname);
            } catch (IOException | IllegalAccessException | ParseException e) {
                e.printStackTrace();
            }
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
