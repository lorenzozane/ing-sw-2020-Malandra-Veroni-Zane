package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private Map<Connection, Connection> playingConnection = new HashMap<>();


    //Deregister connection
    public synchronized void deregisterConnection(Connection c) {
        Connection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        waitingConnection.keySet().removeIf(s -> waitingConnection.get(s) == c);
    }

    //Wait for another player
    public synchronized void lobby(Connection c, String name) throws IOException {
        waitingConnection.put(name, c);
        if(waitingConnection.size()==1){
            //devo far scegliere il numero dei giocatori della partita
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            Connection c1 = waitingConnection.get(keys.get(0));
            c1.asyncSend(Message.chooseNoPlayer);
            Scanner in = new Scanner(c1.getSocket().getInputStream());
            String read = in.nextLine();
            int numberP = Integer.parseInt(read);
            Game game = Game.getInstance();
            Game.getInstance().setPlayerNumber(numberP);


        }

    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketConnection socketConnection = new SocketConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    public Map<String, Connection> getWaitingConnection() {
        return waitingConnection;
    }
}
