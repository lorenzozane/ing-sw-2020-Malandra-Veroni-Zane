package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.*;

import java.util.ArrayList;

public class TurnSequence {

    private final Player playerOwner;
    private final ArrayList<Actions> moveSequence;
    private final ArrayList<WinConditions> winConditions;
    private boolean canMoveUp;

    /**
     * Constructor of the Turn Sequence
     *
     * @param playerOwner Player owner of the Turn Sequence
     * @param moveSequence Sequence of actions that describe the player's standard turn
     * @param winConditions Win conditions of the player
     */
    public TurnSequence(Player playerOwner, ArrayList<Actions> moveSequence, ArrayList<WinConditions> winConditions) {
        this.playerOwner = playerOwner;
        this.moveSequence = moveSequence;
        this.winConditions = winConditions;
        setCanMoveUp();
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
     * Disables the ability to move up to the player during his turn
     */
    public void setCanNotMoveUp() {
        canMoveUp = false;
    }

    /**
     * Enables the ability to move up to the player during his turn
     */
    public void setCanMoveUp() {
        canMoveUp = true;
    }

    public boolean isCanMoveUp() {
        return canMoveUp;
    }
}
