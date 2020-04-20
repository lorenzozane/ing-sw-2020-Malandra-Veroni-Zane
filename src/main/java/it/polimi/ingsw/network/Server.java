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
    public synchronized void lobby(Connection c, String nickname) throws IOException, ParseException, IllegalAccessException, InterruptedException {
        waitingConnection.put(nickname, c);
        if (waitingConnection.size() == 1) {
            Connection c1 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c1.getSocket().getInputStream());
            Game.getInstance();


            Player p1 = new Player(nickname);
            Game.getInstance().addPlayer(p1);


            c1.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c1.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(0).setGui(read.toLowerCase());


            c1.asyncSend(Message.chooseNoPlayer);
            read = in.nextLine();
            while (!noPlayerChecker(read)) {
                c1.asyncSend(Message.chooseNoPlayerAgain);
                read = in.nextLine();
            }
            Game.getInstance().setPlayerNumber(Integer.parseInt(read));


            c1.asyncSend(Message.chooseColor + Game.getInstance().getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c1.asyncSend(Message.chooseColorAgain + Game.getInstance().getAvailableColor());
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(0).setPlayerColor(read.toLowerCase());


            c1.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (!dateChecker(read)) {
                c1.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(0).setBirthday(dateFormat.parse(read));

            c1.asyncSend(Message.wait);

        } else if (waitingConnection.size() == 2) {
            Connection c2 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c2.getSocket().getInputStream());

            Player p2 = new Player(nickname);
            Game.getInstance().addPlayer(p2);


            c2.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c2.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(1).setGui(read.toLowerCase());


            c2.asyncSend(Message.chooseColor + Game.getInstance().getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c2.asyncSend(Message.chooseColorAgain + Game.getInstance().getAvailableColor());
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(1).setPlayerColor(read.toLowerCase());


            c2.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (!dateChecker(read)) {
                c2.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(1).setBirthday(dateFormat.parse(read));

            if (Game.getInstance().getPlayerNumber() == 2) {
                Connection c1 = waitingConnection.get(nicknameDatabase.get(0));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);

                //TODO: fare challenge e far partire il gioco
                //Game.getInstance().challenge();
                //playingConnection.put(c1, c2);
                //playingConnection.put(c2, c1);
                //waitingConnection.clear();

            } else
                c2.asyncSend(Message.wait);

        } else if (waitingConnection.size() == 3) {
            Connection c3 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c3.getSocket().getInputStream());

            Player p3 = new Player(nickname);
            try{
                Game.getInstance().addPlayer(p3);
            }
            catch (IllegalAccessException e){
                c3.asyncSend(Message.lobbyFull);
                c3.wait(500);
                c3.closeConnection();
            }


            c3.asyncSend(Message.chooseCLIorGUI);
            String read = in.nextLine();
            while (!cliOrGuiChecker(read.toLowerCase())) {
                c3.asyncSend(Message.chooseCLIorGUIAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(2).setGui(read.toLowerCase());


            c3.asyncSend(Message.chooseColor + Game.getInstance().getAvailableColor());
            read = in.nextLine();
            while (!colorChecker(read.toLowerCase())) {
                c3.asyncSend(Message.chooseColorAgain + Game.getInstance().getAvailableColor());
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(2).setPlayerColor(read.toLowerCase());


            c3.asyncSend(Message.birthday);
            read = in.nextLine();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (!dateChecker(read)) {
                c3.asyncSend(Message.birthdayAgain);
                read = in.nextLine();
            }
            Game.getInstance().getPlayerList().get(2).setBirthday(dateFormat.parse(read));


            if (Game.getInstance().getPlayerNumber() == 3) {
                Connection c1 = waitingConnection.get(nicknameDatabase.get(0));
                Connection c2 = waitingConnection.get(nicknameDatabase.get(1));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);
                c3.asyncSend(Message.gameLoading);

                //TODO: fare challenge e far partire il gioco
                //Game.getInstance().challenge();
                //playingConnection.put(c1, c2);
                //playingConnection.put(c2, c1);
                //waitingConnection.clear();

            } else
                throw new IllegalArgumentException();


        } else if(waitingConnection.size() > Game.getInstance().getPlayerNumber()) {
            c.asyncSend(Message.lobbyFull);
            throw new IllegalArgumentException();
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
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(s);
            return s.equals(dateFormat.format(date));
        }
        catch (ParseException e){
            return false;
        }

    }

}
