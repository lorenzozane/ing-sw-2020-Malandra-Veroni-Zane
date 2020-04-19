package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        if (waitingConnection.size() == 1) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            Connection c1 = waitingConnection.get(keys.get(0));
            Scanner in = new Scanner(c1.getSocket().getInputStream());
            Game game = Game.getInstance();

            Player p1 = new Player(nickname);
            Game.getInstance().addPlayer(p1);

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
