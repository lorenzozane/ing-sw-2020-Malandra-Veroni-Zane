package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Color;
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
    private ArrayList<String> nicknameReady = new ArrayList<>();


    //Deregister connection
    public synchronized void deregisterConnection(String nick, Connection c) {
        nicknameDatabase.remove(nick);
        if(playingConnection.isEmpty()) {
            waitingConnection.remove(nick, c);
            nicknameReady.remove(nick);
        }
        else {
            //verificare se la partita puo andare avanti
            playingConnection.remove(c);
        }
    }

    //Wait for another player
    public synchronized void lobby(Connection c, String nickname) throws IOException, ParseException, IllegalAccessException, InterruptedException {
        Game game = Game.getInstance();
        waitingConnection.put(nickname, c);
        if (waitingConnection.size() == 1 && playingConnection.isEmpty()) {
            Connection c1 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c1.getSocket().getInputStream());


            Player p1 = new Player(nickname);
            game.addPlayer(p1);


            c1.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c1.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(0).setGui(read.toLowerCase());


            c1.asyncSend(Message.chooseNoPlayer);
            read = in.nextLine();
            while (!noPlayerChecker(read)) {
                c1.asyncSend(Message.chooseNoPlayerAgain);
                read = in.nextLine();
            }
            game.setPlayerNumber(Integer.parseInt(read));


            c1.asyncSend(Message.chooseColor + game.getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c1.asyncSend(Message.chooseColorAgain + game.getAvailableColor());
                read = in.nextLine();
            }
            game.getPlayerList().get(0).setPlayerColor(read.toLowerCase());


            c1.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            while (!dateChecker(read)) {
                c1.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(0).setBirthday(dateFormat.parse(read));

            nicknameReady.add(nickname);
            c1.asyncSend(Message.wait);


        } else if (waitingConnection.size() == 2 && playingConnection.isEmpty()) {
            Connection c2 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c2.getSocket().getInputStream());

            Player p2 = new Player(nickname);
            game.addPlayer(p2);


            c2.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c2.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(1).setGui(read.toLowerCase());


            c2.asyncSend(Message.chooseColor + game.getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c2.asyncSend(Message.chooseColorAgain + game.getAvailableColor());
                read = in.nextLine();
            }
            game.getPlayerList().get(1).setPlayerColor(read.toLowerCase());


            c2.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            while (!dateChecker(read)) {
                c2.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(1).setBirthday(dateFormat.parse(read));

            nicknameReady.add(nickname);


            if (game.getPlayerNumber() == 2) {
                Connection c1 = waitingConnection.get(nicknameReady.get(0));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);

                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
                waitingConnection.clear();

                game.challenge();
                }

            else
                c2.asyncSend(Message.wait);

        }

        else if (waitingConnection.size() == 3 && playingConnection.isEmpty()) {
            Connection c3 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c3.getSocket().getInputStream());

            Player p3 = new Player(nickname);
            game.addPlayer(p3);


            c3.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c3.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(2).setGui(read.toLowerCase());


            c3.asyncSend(Message.chooseColor + game.getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c3.asyncSend(Message.chooseColorAgain + game.getAvailableColor());
                read = in.nextLine();
            }
            game.getPlayerList().get(2).setPlayerColor(read.toLowerCase());


            c3.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            while (!dateChecker(read)) {
                c3.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            game.getPlayerList().get(2).setBirthday(dateFormat.parse(read));

            nicknameReady.add(nickname);


            if (game.getPlayerNumber() == 3) {
                Connection c1 = waitingConnection.get(nicknameReady.get(0));
                Connection c2 = waitingConnection.get(nicknameReady.get(1));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);
                c3.asyncSend(Message.gameLoading);

                //da capire se per noi va bene così
                playingConnection.put(c1, c2);
                playingConnection.put(c1, c3);
                playingConnection.put(c2, c1);
                playingConnection.put(c2, c3);
                playingConnection.put(c3, c1);
                playingConnection.put(c3, c2);
                waitingConnection.clear();

                game.challenge();

            } else
                throw new IllegalArgumentException();
        }
        else {
            c.asyncSend(Message.lobbyFull);
            deregisterConnection(nickname, c);
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

    public ArrayList<String> getNicknameDatabase() {
        return nicknameDatabase;
    }

    public void addNickname(String nickname) {
        this.nicknameDatabase.add(nickname);
    }

    public boolean cliOrGuiChecker(String s) {
        return s.equals("cli") || s.equals("gui");
    }

    public boolean noPlayerChecker(String s) {
        return s.equals("2") || s.equals("3");
    }

    public boolean colorChecker(String s) {
        for (Color color : Game.getInstance().getColorList()) {
            if (s.equals(color.getColorAsString(color).toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean dateChecker(String s){
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = dateFormat.parse(s);
            return s.equals(dateFormat.format(date));
        }
        catch (ParseException e){
            return false;
        }

    }

}
