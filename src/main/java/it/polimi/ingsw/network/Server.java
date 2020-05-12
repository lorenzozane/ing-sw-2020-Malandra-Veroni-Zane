package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameInitializationManager;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Observable<AbstractMap.SimpleEntry> {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, Connection> waitingConnection = new LinkedHashMap<>();
    // ci serve? private final Map<Connection, Connection> playingConnection = new HashMap<>();
    private final ArrayList<String> nicknameDatabase = new ArrayList<>();
    private String currentCreator = "";
    private final ArrayList<Player> usersReady = new ArrayList<>();
    private boolean isSomeoneCreatingAGame = false;
    private int nPlayer = 0;
    private final Object lock = new Object();
    private final ArrayList<Game> gamesStarted = new ArrayList<>();


    /**
     * Deregister a client when is no longer reachable
     *
     * @param nick Client unique name
     * @param c    Client socket connection
     */
    public synchronized void deregisterConnection(String nick, Connection c) throws IOException, IllegalAccessException, ParseException {
        waitingConnection.remove(nick);
        usersReady.removeIf(x -> x.getNickname().equals(nick));

        for (Game game : gamesStarted){          //caso in cui il giocatore sia dentro una partita
            for (Player p : game.getPlayerList()){
                if(p.getNickname().equals(nick)){
                    game.removePlayerByName(nick);
                }
            }
        }

        gamesStarted.removeIf(game -> game.getPlayerList().size() == 0); //caso in cui il game ha 0 player == finito == lo elimino

        nicknameDatabase.remove(nick);
        c.closeConnection();
        if (nick.equals(currentCreator)) {
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


    public void lobby(String nickname, Date playerBirthday, Connection c) throws IOException, IllegalAccessException, ParseException {
        waitingConnection.put(nickname, c);

        Player p = new Player(nickname);
        p.setBirthday(playerBirthday);
        usersReady.add(p);

        if (!isSomeoneCreatingAGame) {
            isSomeoneCreatingAGame = true;
            currentCreator = nickname;

            creatorSetup(c);
        } else {
            if (nPlayer <= waitingConnection.size() && nPlayer > 0)
                gameLobby();
        }
    }


    public synchronized void gameLobby() throws IllegalAccessException, IOException, ParseException {
        Game game = new Game();
        GameInitializationManager gameInitializationManager = new GameInitializationManager(game);

        gamesStarted.add(game);

        game.setPlayerNumber(nPlayer);

        for (int i = 0; i < nPlayer; i++) {
            Connection c = waitingConnection.get(usersReady.get(i).getNickname());
            c.asyncSend(Message.gameLoading);

            game.addPlayer(usersReady.get(i));

            //settare view (?)
        }

        //add.observer vari e partenza gioco per salezionare il colore

        for (int i = 0; i < nPlayer; i++) {
            waitingConnection.remove(usersReady.get(i).getNickname());
        }

        usersReady.subList(0, nPlayer).clear();
        checkNewCreator();
    }


    private synchronized void checkNewCreator() throws IOException, IllegalAccessException, ParseException {
        if (waitingConnection.isEmpty()) {
            isSomeoneCreatingAGame = false;
            nPlayer = 0;
            currentCreator = "";
        } else {
            currentCreator = usersReady.get(0).getNickname();
            creatorSetup(waitingConnection.get(currentCreator));

        }
    }


    private void creatorSetup(Connection c) throws IOException, IllegalAccessException, ParseException {
        Scanner in = new Scanner(c.getSocket().getInputStream());
        c.asyncSend(Message.chooseNoPlayer);

        int read = in.nextInt();
        while (!noPlayerChecker(read)) {
            c.asyncSend(Message.chooseNoPlayerAgain);
            read = in.nextInt();
        }

        synchronized (lock) {
            nPlayer = read;
        }

        c.asyncSend(Message.wait);

        if (nPlayer <= waitingConnection.size() && nPlayer > 0)
            gameLobby();

    }


    private void ping() {

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

    /**
     * Check if the input string about the color choice is legal
     *
     * @param s String from user input that must be checked
     * @return true if the color is available in the game otherwise false
     */
    public static boolean colorChecker(String s) {
        //TODO: Check del toString()
        return Arrays.toString(Color.PlayerColor.values()).contains(s);
    }

}
