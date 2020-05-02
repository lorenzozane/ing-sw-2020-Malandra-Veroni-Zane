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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Game getInstance = new Game();
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, Connection> waitingConnection = new HashMap<>();
    private final Map<Connection, Connection> playingConnection = new HashMap<>();
    private final ArrayList<String> nicknameDatabase = new ArrayList<>();
    private final ArrayList<String> usersReady = new ArrayList<>();


    /**
     * Deregister a client when is no longer reachable
     *
     * @param nick Client unique name
     * @param c Client socket connection
     */
    public synchronized void deregisterConnection(String nick, Connection c) {
        nicknameDatabase.remove(nick);
        if(playingConnection.isEmpty()) {
            waitingConnection.remove(nick, c);
            usersReady.remove(nick);
        }
        else {
            //verificare se la partita puo andare avanti
            playingConnection.remove(c);
        }
    }


    //lobby vecchia
    /*public synchronized void lobby1(Connection c, String nickname) throws IOException, ParseException, IllegalAccessException {
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

            usersReady.add(nickname);
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

            usersReady.add(nickname);


            if (game.getPlayerNumber() == 2 && usersReady.size() == 2) {
                Connection c1 = waitingConnection.get(usersReady.get(0));
                c1.asyncSend(Message.gameLoading);
                c2.asyncSend(Message.gameLoading);

                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
                waitingConnection.clear();
                usersReady.clear();

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

            usersReady.add(nickname);


            if (game.getPlayerNumber() == 3 && usersReady.size()==3) {
                Connection c1 = waitingConnection.get(usersReady.get(0));
                Connection c2 = waitingConnection.get(usersReady.get(1));
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
                usersReady.clear();

                game.challenge();

            } else
                c3.asyncSend(Message.wait);
        }
        else {
            c.asyncSend(Message.lobbyFull);
            deregisterConnection(nickname, c);
        }

    }

     */


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
                SocketConnection socketConnection = new SocketConnection(newSocket, this, getInstance);
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
     * Check if the input string about the cli/gui choice is legal
     *
     * @param s String from user input that must be checked
     * @return true if it is equals to "cli" or "gui" otherwise false
     */
    public boolean cliOrGuiChecker(String s) {
        return s.equals("cli") || s.equals("gui");
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


    /**
     * Check if the input string about the color choice is legal
     *
     * @param s String from user input that must be checked
     * @return true if the color is available in the game otherwise false
     */
    public boolean colorChecker(String s) {
        for (Color color : getInstance.getColorList()) {
            if (s.equals(color.getColorAsString(color).toLowerCase()))
                return true;
        }
        return false;
    }


    /**
     * Check if the input string about the birthday date is legal
     *
     * @param s String from user input that must be checked
     * @return true if the string is correctly formatted otherwise false
     */
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


    public void lobby(String nickname, Connection c) throws IOException, IllegalAccessException {
        waitingConnection.put(nickname, c);
        if(waitingConnection.size() == 1){
            usersReady.add(nickname);
            Connection c1 = waitingConnection.get(nickname);
            Scanner in = new Scanner(c1.getSocket().getInputStream());

            c1.asyncSend(Message.chooseNoPlayer);
            String read = in.nextLine();
            while (!noPlayerChecker(read)) {
                c1.asyncSend(Message.chooseNoPlayerAgain);
                read = in.nextLine();
            }
            getInstance.setPlayerNumber(Integer.parseInt(read));

            c1.asyncSend(Message.wait);

            if(getInstance.getPlayerNumber() == usersReady.size())
                gameLobby();

        }
        else{
            usersReady.add(nickname);
            c.asyncSend(Message.wait);
            if(getInstance.getPlayerNumber() == usersReady.size())
                gameLobby();
        }



    }

    //PROBLEMA se facciamo partite multiple: quando tiro via le connsioni di chi è entrato in partita quello che
    //era in 2a/3a posizione va nella 0 quindi dovrebbe decidere da quanti è la partita però non puo
    // perchè già entrato in lobby :/


    public synchronized void gameLobby() throws IllegalAccessException {
        for(int i=0; i<getInstance.getPlayerNumber(); i++){
            Player p = new Player(getInstance, usersReady.get(i));
            p.setConnection(waitingConnection.get(usersReady.get(i)));
            getInstance.addPlayer(p);

        }



        getInstance.setup();
        /*
        for(int i=0; i<getInstance.getPlayerNumber(); i++){
            waitingConnection.remove(usersReady.get(i));
        }

        if (getInstance.getPlayerNumber() > 0) {
            usersReady.subList(0, getInstance.getPlayerNumber()).clear();
        }

         */
    }

}
