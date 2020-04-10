package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class Turn {

    protected static Player currentPlayer;
    protected static ArrayList<Player> playerOrder;
    public static HashMap<Player, ArrayList<TurnEvents>> moveSequence;

    public void setPlayerOrder(Player... players) throws IllegalArgumentException {
        if (playerOrder.isEmpty()) {
            if (players.length != Game.getInstance().getPlayerNumber())
                throw new IllegalArgumentException();

            playerOrder.addAll(Arrays.asList(players));

            playerOrder.sort(Comparator.comparing(Player::getBirthday));
        }
    }

    public void updateTurn() {
        if (currentPlayer == null)
            playerOrder.get(0);
        else
            currentPlayer = getNextPlayer();

        updateMoveSequence();
    }

    //TODO: Test
    protected Player getNextPlayer() {
        int index = playerOrder.indexOf(currentPlayer);

        return playerOrder.get((index + 1) % playerOrder.size());
    }

    protected void updateMoveSequence(){
        //TODO
    }
}
