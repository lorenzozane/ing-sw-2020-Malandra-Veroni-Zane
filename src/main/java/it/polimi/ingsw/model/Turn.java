package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Turn {

    protected static Player currentPlayer;
    protected static ArrayList<Player> playerOrder;

    public void setPlayerOrder(Player ... players) throws IllegalArgumentException {
        if (playerOrder.isEmpty()){
            if (players.length != Game.getPlayerNumber())
                throw new IllegalArgumentException();

            for (Player player : players)
                playerOrder.add(player);

            playerOrder.sort(Comparator.comparing(Player::getBirthday));
        }
    }

    public void updateTurn(){
        if (currentPlayer == null)
            playerOrder.get(0);
        else
            currentPlayer = getNextPlayer();
    }

    protected Player getNextPlayer(){
        //TODO

        return null;
    }
}
