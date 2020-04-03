package it.polimi.ingsw.model;

import java.util.ArrayList;
import javax.swing.DefaultListSelectionModel;

public class Game {

    private ArrayList playerList;
    private int playerNumber;
    private Board board;
    private Deck godsDeck;
    private Player firstPlayer;

    public Game() {

    }


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
        ArrayList<GodsCard> challengeCard = new ArrayList<>();
        if(playerNumber == 3){
            while(challengeCard.size()!=3){
                //chiedere al firstPlayer di selezionare una carta -- comunicazione server-client mancante
                //challengeCard.add( carta selezionata dal challenger );
            }
            /* far scegliere una carta al
            new card = playerList.get(1).choosecard
            playerList.get(1).setGodsCard( card );
            poi rimuoverla dalla lista
            challengeCard.remove( card );
            ripetere
            card = playerList.get(2).choosecard
            playerList.get(2).setGodsCard( card );
            poi rimuoverla dalla lista
            challengeCard.remove( card );
            playerList.get(0).setGodsCard( challengeCard.get(0) );
            */
        }
        else if (playerNumber == 2){
            /* far scegliere una carta all'altro giocatore
            new card = playerList.get(1).choosecard
            playerList.get(1).setGodsCard( card );
            poi rimuoverla dalla lista
            challengeCard.remove( card );
            playerList.get(0).setGodsCard( challengeCard.get(0) );
            */
        }
        else {
            throw new IllegalArgumentException();
        }
    }



}
