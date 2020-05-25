package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameInitializationManager;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, SocketConnection> waitingConnection = new LinkedHashMap<>();
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
    public synchronized void deregisterConnection(String nick, SocketConnection c) throws IOException, IllegalAccessException, ParseException {
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
                SocketConnection socketConnection = new SocketConnection(newSocket, this);
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
    public boolean noPlayerChecker(String s) {
        return s.equals("2") || s.equals("3");
    }


    public void lobby(String nickname, Date playerBirthday, SocketConnection c) throws IOException, IllegalAccessException, ParseException {
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
        Game gameInstance = new Game();
        gamesStarted.add(gameInstance);

        gameInstance.setPlayerNumber(nPlayer);

        for (int i = 0; i < nPlayer; i++) {
            SocketConnection socketConnection = waitingConnection.get(usersReady.get(i).getNickname());
            socketConnection.asyncSend(Message.gameLoading);

            GameManager gameManager = new GameManager(gameInstance);
            GameInitializationManager gameInitializationManager = new GameInitializationManager(gameInstance);
            RemoteView remoteView = new RemoteView(usersReady.get(i).getNickname(), socketConnection);

            gameInstance.getTurn().addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());
            remoteView.addUpdateTurnMessageObserver(socketConnection.getUpdateTurnMessageReceiver());
            socketConnection.addPlayerMoveObserver(remoteView.getPlayerMoveReceiver());
            socketConnection.addPlayerMoveStartupObserver(remoteView.getPlayerMoveStartupReceiver());
            remoteView.addPlayerMoveObserver(gameManager.getPlayerMoveReceiver());
            remoteView.addPlayerMoveStartupObserver(gameInitializationManager.getPlayerMoveStartupReceiver());

            gameInstance.addPlayer(usersReady.get(i));

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


    private void creatorSetup(SocketConnection c) throws IOException, IllegalAccessException, ParseException {
        c.asyncSend(Message.chooseNoPlayer);
        ObjectInputStream in = null;

        try {
            in = c.getIn();
            Object inputObject = in.readObject();
            System.out.println("ricevuto 173 :"  + inputObject.toString());
            while (true) {
                if (inputObject instanceof String) {
                    if (noPlayerChecker((String) inputObject)) {
                        synchronized (lock) {
                            nPlayer = Integer.parseInt((String) inputObject);
                        }
                        break;
                    }
                }
                c.asyncSend(Message.chooseNoPlayerAgain);
                inputObject = in.readObject();
                System.out.println("ricevuto 185 :"  + inputObject.toString());
                in.reset();
            }
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        c.asyncSend(Message.wait);

        if (nPlayer <= waitingConnection.size() && nPlayer > 0)
            gameLobby();

    }



    //da mettere lato client
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
