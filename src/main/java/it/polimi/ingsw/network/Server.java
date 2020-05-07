package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, Connection> waitingConnection = new LinkedHashMap<>();
    // ci serve? private final Map<Connection, Connection> playingConnection = new HashMap<>();
    private final ArrayList<String> nicknameDatabase = new ArrayList<>();
    private String currentCreator = "";
    private final ArrayList<String> usersReady = new ArrayList<>();
    private boolean isSomeoneCreatingAGame = false;
    private int nPlayer = 0;
    private final Object lock = new Object();


    /**
     * Deregister a client when is no longer reachable
     *
     * @param nick Client unique name
     * @param c Client socket connection
     */
    public synchronized void deregisterConnection(String nick, Connection c) throws IOException, IllegalAccessException {
        waitingConnection.remove(nick);
        usersReady.remove(nick);
        nicknameDatabase.remove(nick);
        c.closeConnection();
        if(nick.equals(currentCreator)){
            checkNewCreator();
        }
    }

    /**
     * Constructor of Server
     *
     * @throws IOException Is thrown if an I/O error occurs when opening the socket
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }


    /**
     * It keeps the server listening on PORT and it accepts new client
     *
     */
    public void run() {
        while (true) {
            try {
                Socket newSocket = serverSocket.accept();
                SocketConnection socketConnection = new SocketConnection(newSocket, this/*, this, getInstance*/);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }


    public ArrayList<String> getNicknameDatabase() {
        return nicknameDatabase;
    }


    public void addNickname(String nickname) {
        this.nicknameDatabase.add(nickname);
    }


    /**
     * Check if the input string about number of player is legal
     *
     * @param s String from user input that must be checked
     * @return true if it is 2 or 3 otherwise false
     */
    public boolean noPlayerChecker(int s) {
        return s == 2 || s == 3;
    }


    public void lobby(String nickname, Connection c) throws IOException, IllegalAccessException {
        waitingConnection.put(nickname, c);
        if (!isSomeoneCreatingAGame){
            isSomeoneCreatingAGame = true;
            usersReady.add(nickname);
            currentCreator = nickname;

            creatorSetup(c);
        }
        else{
            usersReady.add(nickname);

            if(nPlayer <= waitingConnection.size() && nPlayer > 0)
                gameLobby();

        }
    }



    public synchronized void gameLobby() throws IllegalAccessException, IOException {
        Game game = new Game();
        game.setPlayerNumber(nPlayer);
        for(int i=0; i<nPlayer; i++){
            Player p = new Player(usersReady.get(i));
            p.setConnection(waitingConnection.get(usersReady.get(i)));
            game.addPlayer(p);
        }

        game.setup();

        //Si chiede la data di nascita
        //Si notifica string + tipo (data)
        //Si chiede il colore
        //Si notifica string + color

        for(int i=0; i<nPlayer; i++){
            waitingConnection.remove(usersReady.get(i));
        }

        usersReady.subList(0, nPlayer).clear();

        checkNewCreator();
    }



    private synchronized void checkNewCreator() throws IOException, IllegalAccessException {
        if(waitingConnection.isEmpty()){
            isSomeoneCreatingAGame = false;
            nPlayer = 0;
            currentCreator="";
        }
        else{
            currentCreator = usersReady.get(0);
            creatorSetup(waitingConnection.get(currentCreator));

        }
    }



    private synchronized void creatorSetup(Connection c) throws IOException, IllegalAccessException {
        Scanner in = new Scanner(c.getSocket().getInputStream());
        c.asyncSend(Message.chooseNoPlayerAgain);

        int read = in.nextInt();
        while (!noPlayerChecker(read)) {
            c.asyncSend(Message.chooseNoPlayerAgain);
            read = in.nextInt();
        }

        c.asyncSend(Message.wait);

        synchronized (lock) {
            nPlayer = read;
        }

        if (nPlayer <= waitingConnection.size() && nPlayer > 0)
            gameLobby();

    }

}
