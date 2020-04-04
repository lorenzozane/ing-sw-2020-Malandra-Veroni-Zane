package it.polimi.ingsw.model;

import java.util.ArrayList;
import javax.swing.DefaultListSelectionModel;

public class Game {

    private ArrayList playerList;
    private int playerNumber;
    private Board board;
    private Deck godsDeck;
    private Player firstPlayer;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public boolean addPlayer(Player newPlayer) {
        if(checkPlayer(newPlayer)) {
            playerList.add(newPlayer);
            return true;
        }
        else{
            return false;
        }

    }

    private boolean checkPlayer(Player newPlayer){
        for (int counter = 0; counter < playerList.size(); counter++) {
            if (playerList.contains(newPlayer) )
                return false;
        }
        return true;
    }

    public void removePlayer(final Player playerToDelete){
        playerList.remove(playerToDelete);
        throw new IllegalArgumentException();
    }

    public Player getChallengerPlayer(){
        return this.firstPlayer;
    }

    public void challenge(){

    }



}
