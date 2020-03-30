package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {

    private ArrayList playerList;
    private int playerNumber;
    private Board board;
    private Deck godsDeck;


    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
