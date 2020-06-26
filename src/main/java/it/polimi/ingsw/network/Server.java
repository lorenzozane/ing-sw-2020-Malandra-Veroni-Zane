package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameInitializationManager;
import it.polimi.ingsw.controller.GameManager;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, SocketConnection> waitingConnection = new LinkedHashMap<>();
    private final Map<String, SocketConnection> playingConnection = new LinkedHashMap<>();
    private final ArrayList<String> nicknameDatabase = new ArrayList<>();
    private String currentCreator = "";
    private final ArrayList<Player> usersReady = new ArrayList<>();
    private boolean isSomeoneCreatingAGame = false;
    private int nPlayer = 0;
    protected final Object lock = new Object();
    protected final ArrayList<Game> gamesStarted = new ArrayList<>();


    /**
     * Deregister a client when is no longer reachable
     *
     * @param nick Client unique name
     */
    public synchronized void deregisterConnection(String nick) {
        waitingConnection.remove(nick);
        usersReady.removeIf(x -> x.getNickname().equals(nick));

        AtomicBoolean deregisterOther = new AtomicBoolean(false);

        for (Game game : gamesStarted) {          //caso in cui il giocatore sia dentro una partita
            game.getPlayerList().forEach(player -> {
                if (player.getNickname().equals(nick)) {
                    deregisterOther.set(true);
                }
            });
            if (deregisterOther.get()) {
                game.removePlayerByName(nick);
                for (Player otherPlayer : game.getPlayerList()) {
                    playingConnection.get(otherPlayer.getNickname()).asyncSend(nick + "left the game\n" + Message.gameOver);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        System.out.println("Exception thrown from Server.deregisterConnection");
                        e.printStackTrace();
                    }
                    playingConnection.get(otherPlayer.getNickname()).closeConnection(); //da fare?
                    playingConnection.remove(otherPlayer.getNickname());
                    nicknameDatabase.remove(otherPlayer.getNickname());
                }
                game.getPlayerList().clear();
                deregisterOther.set(false);
            }

        }

        gamesStarted.removeIf(game -> game.getPlayerList().size() == 0);

        playingConnection.remove(nick);
        nicknameDatabase.remove(nick);

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
                System.out.println("Connection Error! Exception thrown from Server.run");
            }
        }
    }

    public ArrayList<String> getNicknameDatabase() {
        return nicknameDatabase;
    }

    public void addNickname(String nickname) {
        this.nicknameDatabase.add(nickname);
    }


    public Map<String, SocketConnection> getWaitingConnection() {
        return waitingConnection;
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

    public void lobby(String nickname, Date playerBirthday, SocketConnection c) {
        waitingConnection.put(nickname, c);

        Player p = new Player(nickname);
        p.setBirthday(playerBirthday);
        usersReady.add(p);

        if (!isSomeoneCreatingAGame) {
            isSomeoneCreatingAGame = true;
            currentCreator = nickname;

            creatorSetup(c);
        }
        else {
            if (nPlayer <= usersReady.size() && nPlayer > 0)
                new Thread(this::gameLobby).start();
        }
    }

    public void gameLobby() {
        Game gameInstance = new Game();
        gamesStarted.add(gameInstance);

        gameInstance.setPlayerNumber(nPlayer);

        ArrayList<Player> userReadyCopy = new ArrayList<>(usersReady);
        Map<String, SocketConnection> waitingConnectionCopy = new LinkedHashMap<>(waitingConnection);

        new Thread(() -> gameSettings(gameInstance, userReadyCopy, waitingConnectionCopy)).start();

        for (int i = 0; i < nPlayer; i++) {
            waitingConnection.remove(usersReady.get(i).getNickname());
        }
        usersReady.subList(0, nPlayer).clear();
        this.nPlayer = 0;

        checkNewCreator();
    }

    private synchronized void checkNewCreator() {
        if (waitingConnection.isEmpty()) {
            isSomeoneCreatingAGame = false;
            currentCreator = "";
            nPlayer = 0;
        } else {
            nPlayer = 0;
            currentCreator = usersReady.get(0).getNickname();
            creatorSetup(waitingConnection.get(currentCreator));
        }
    }

    private void creatorSetup(SocketConnection c) {
        c.asyncSend(Message.chooseNoPlayer);
        ObjectInputStream in;

        try {
            in = c.getIn();
            Object inputObject = in.readObject();
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
            }
        }
        catch (ClassNotFoundException | IOException e){
            System.out.println("Exception thrown from Server.creatorSetup");
            e.printStackTrace();
        }

        c.asyncSend(Message.wait);

        if (nPlayer <= waitingConnection.size() && nPlayer > 0)
            new Thread(this::gameLobby).start();

    }

    private void gameSettings(Game gameInstance, ArrayList<Player> usersReadyCopy, Map<String, SocketConnection> waitingConnectionCopy){
        GameManager gameManager = new GameManager(gameInstance);
        GameInitializationManager gameInitializationManager = new GameInitializationManager(gameInstance);

        for (int i = 0; i < gameInstance.getPlayerNumber(); i++) {
            SocketConnection socketConnection = waitingConnectionCopy.get(usersReadyCopy.get(i).getNickname());
            playingConnection.put(usersReadyCopy.get(i).getNickname(), socketConnection);
            RemoteView remoteView = new RemoteView(usersReadyCopy.get(i).getNickname(), socketConnection);

            gameInstance.getTurn().addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());
            remoteView.addUpdateTurnMessageObserver(socketConnection.getUpdateTurnMessageReceiver());
            socketConnection.addPlayerMoveObserver(remoteView.getPlayerMoveReceiver());
            socketConnection.addPlayerMoveStartupObserver(remoteView.getPlayerMoveStartupReceiver());
            remoteView.addPlayerMoveObserver(gameManager.getPlayerMoveReceiver());
            remoteView.addPlayerMoveStartupObserver(gameInitializationManager.getPlayerMoveStartupReceiver());

            try {
                gameInstance.addPlayer(usersReadyCopy.get(i));
            } catch (IllegalAccessException e) {
                System.out.println("Exception thrown from Server.gameSettings");
                e.printStackTrace();
            }
        }
    }

    public void deregisterOnePlayer(String nickname) {
        AtomicBoolean toDelete = new AtomicBoolean(false);
        for (Game game : gamesStarted) {
            game.getPlayerList().forEach(player -> {
                if (player.getNickname().equals(nickname)) {
                    toDelete.set(true);
                }
            });
            if(toDelete.get()){
                game.removePlayerByName(nickname);
            }
        }
        nicknameDatabase.removeIf(nick -> nick.equals(nickname));
        waitingConnection.remove(nickname);
        playingConnection.remove(nickname);
        gamesStarted.removeIf(game -> game.getPlayerList().size() == 0);
    }

}
