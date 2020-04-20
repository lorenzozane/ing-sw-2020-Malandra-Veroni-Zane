package it.polimi.ingsw.network;

import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketConnection extends Observable<String> implements Connection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private boolean active = true;

    public SocketConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

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

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    @Override
    public void run() {
        Scanner in;
        String nickname;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
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
            server.lobby(this, nickname);
            while(isActive()){
                read = in.nextLine();
                notifyAll(read);
            }
        } catch (IOException | NoSuchElementException | ParseException | IllegalAccessException | InterruptedException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
    }

    private boolean nicknameChecker(String nickname){
        return !server.getNicknameDatabase().contains(nickname);
    }

    @Override
    public Socket getSocket(){
        return this.socket;
    }

}
