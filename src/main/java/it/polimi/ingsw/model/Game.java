package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {

    private static Game instance;   //Singleton pattern
    private ArrayList<Player> playerList;
    private int playerNumber;
    private Board board;
    private Deck godsDeck;
    private Player firstPlayer;

    private Game(){

    }

    public static Game getInstance(){
        if (instance == null)
            instance = new Game();

        return instance;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        if (this.playerNumber == 0) //se il valore è diverso da zero, vuol dire che è già stato settato
            this.playerNumber = playerNumber;
    }

    public boolean addPlayer(Player newPlayer) {
        if (checkPlayer(newPlayer)) {
            playerList.add(newPlayer);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPlayer(Player newPlayer) {
        for (int counter = 0; counter < playerList.size(); counter++) {
            if (playerList.contains(newPlayer))
                return false;
        }
        return true;
    }

    public void removePlayer(final Player playerToDelete) {
        playerList.remove(playerToDelete);
        throw new IllegalArgumentException(); //TODO: Check: ??
    }

    public Player getChallengerPlayer() {
        return this.firstPlayer;
    }

    public void challenge() {}


}

