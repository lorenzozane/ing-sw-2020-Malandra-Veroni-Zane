package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.*;

import java.util.ArrayList;

public class TurnSequence {

    private final Player playerOwner;
    private final ArrayList<Actions> moveSequence;
    private final ArrayList<WinConditions> winConditions;
    private boolean canMoveUp = true;

    /**
     * Constructor of the Turn Sequence
     *
     * @param playerOwner   Player owner of the Turn Sequence
     * @param moveSequence  Sequence of actions that describe the player's standard turn
     * @param winConditions Win conditions of the player
     */
    public TurnSequence(Player playerOwner, ArrayList<Actions> moveSequence, ArrayList<WinConditions> winConditions) {
        this.playerOwner = playerOwner;
        this.moveSequence = moveSequence;
        this.winConditions = winConditions;
        setCanMoveUp(true);
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }

    public ArrayList<Actions> getMoveSequence() {
        return moveSequence; //Essendo "final" non serve crearne una copia
    }

    public ArrayList<WinConditions> getWinConditions() {
        return winConditions;
    }

    /**
     * Enables or Disables the ability to move up to the player during his turn.
     *
     * @param canMoveUpValue The boolean describing if set the ability to move up or not.
     */
    public void setCanMoveUp(Boolean canMoveUpValue) {
        canMoveUp = canMoveUpValue;
    }

    /**
     * Allows to know if a player has the permission to move up in this turn
     *
     * @return The boolean describing if the player has the permission to move up in this turn
     */
    public boolean isCanMoveUp() {
        return canMoveUp;
    }
}
