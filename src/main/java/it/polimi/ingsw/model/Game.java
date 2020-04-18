package it.polimi.ingsw.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Game {

    private static Game instance;   //Singleton pattern
    private final Turn turn;
    private ArrayList<Player> playerList = new ArrayList<>();
    private int playerNumber;
    private Board board;
    private Deck godsDeck;
    private Player firstPlayer;
    private Player challengerPlayer;

    private Game() {
        this.turn = new Turn();
    }

    public Turn getTurn(){
        return turn;
    }

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void addPlayer(Player newPlayer) {
        if (checkPlayer(newPlayer)) {
            playerList.add(newPlayer);
            playerList.sort(Comparator.comparing(Player::getBirthday).reversed());   //mette già in ordine di età
            challengerPlayer = playerList.get(0);
        }
    }

    private boolean checkPlayer(Player newPlayer) {
        if (!playerList.isEmpty())
            if (playerList.contains(newPlayer))
                return false;
        return true;
    }

    public void removePlayer(final Player playerToDelete) {
        playerList.remove(playerToDelete);
        throw new IllegalArgumentException(); //TODO: Check: ??
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public Player getChallengerPlayer() {
        return playerList.get(0);
    }

    public void challenge() throws IOException {
        //mostrare alla view del challenger tutti gli dei
        godsDeck = new Deck();
        challengerPlayer = getChallengerPlayer();
        godsDeck.printAllDeck();

        System.out.println("Choose gods");
        //Scanner in = new Scanner(System.in);
        String godsChooses = "apollo, pan";
        String[] split = godsChooses.split("\\s*,\\s*");
        godsDeck.chooseCards(split[0], split[1]);


    }


}


