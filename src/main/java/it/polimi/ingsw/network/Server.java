package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private Map<Connection, Connection> playingConnection = new HashMap<>();
    private ArrayList<String> nicknameDatabase = new ArrayList<>();


    //Deregister connection
    public synchronized void deregisterConnection(Connection c) {
        Connection opponent = playingConnection.get(c);
        if (opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        waitingConnection.keySet().removeIf(s -> waitingConnection.get(s) == c);
    }

    //Wait for another player
    public synchronized void lobby(Connection c, String nickname) throws IOException, ParseException {
        waitingConnection.put(nickname, c);
        nicknameDatabase.add(nickname);
        if (waitingConnection.size() == 1) {
            Connection c1 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c1.getSocket().getInputStream());
            Game game = Game.getInstance();

            Player p1 = new Player(nickname);
            Game.getInstance().addPlayer(p1);

            //TODO: controlli sull'input

            c1.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            Game.getInstance().getPlayerList().get(0).setGui(read);

            c1.asyncSend(Message.chooseNoPlayer);
            Game.getInstance().setPlayerNumber(Integer.parseInt(in.nextLine()));

            c1.asyncSend(Message.chooseColor + Game.getInstance().getAvailableColor());
            Game.getInstance().getPlayerList().get(0).setPlayerColor(in.nextLine());

            c1.asyncSend(Message.birthday);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //Game.getInstance().getPlayerList().get(0).setBirthday(dateFormat.parse(in.nextLine()));

            c1.asyncSend(Message.wait);
        }
        else if (waitingConnection.size() == 2){
            Connection c2 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c2.getSocket().getInputStream());

            Player p2 = new Player(nickname);
            Game.getInstance().addPlayer(p2);

            //TODO: controlli sull'input

            c2.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            Game.getInstance().getPlayerList().get(1).setGui(read);

            c2.asyncSend(Message.chooseColor + Game.getInstance().getAvailableColor());
            Game.getInstance().getPlayerList().get(1).setPlayerColor(in.nextLine());

            c2.asyncSend(Message.birthday);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Game.getInstance().getPlayerList().get(1).setBirthday(dateFormat.parse(in.nextLine()));

            if(Game.getInstance().getPlayerNumber() == 2){
                Connection c1 = waitingConnection.get(nicknameDatabase.get(0));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);

                //TODO: fare challenge e far partire il gioco
                //Game.getInstance().challenge();
                //playingConnection.put(c1, c2);
                //playingConnection.put(c2, c1);
                //waitingConnection.clear();

            }


        }

    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run() {
        while (true) {
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
