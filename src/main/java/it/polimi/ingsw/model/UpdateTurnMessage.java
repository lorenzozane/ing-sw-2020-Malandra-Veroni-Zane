package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * UpdateTurnMessage message, require to communicate the information regarding the next move to be performed by the player.
 */
public class UpdateTurnMessage implements Serializable {

    private static final long serialVersionUID = 4116568860427433236L;
    private final String lastMovePerformedBy;
    private final TurnEvents.StartupActions nextStartupMove;
    private final TurnEvents.Actions nextMove;
    private final boolean startupPhase;
    private final Player currentPlayer;
    private final Worker currentWorker;
    private Board boardCopy;
    private boolean stuck = false;
    private boolean gameFinish = false;
    private final ArrayList<PlayerColor> availableColor = new ArrayList<>();
    private final ArrayList<GodsCard> availableCards = new ArrayList<>();

    /**
     * Constructor of the message UpdateTurnMessage, require to communicate the information regarding the next move to be performed by the player.
     *
     * @param nextStartupMove The next StartupActions to be performed by the player during the startup phase of the game.
     * @param currentPlayer   The current player who must perform the specified move.
     * @param playerColor     ArrayList of PlayerColor available to be chosen.
     */
    public UpdateTurnMessage(TurnEvents.StartupActions nextStartupMove, Player currentPlayer, ArrayList<PlayerColor> playerColor) {
        this.startupPhase = true;
        this.nextStartupMove = nextStartupMove;
        this.currentPlayer = currentPlayer;
        this.availableColor.addAll(playerColor);
        this.currentWorker = null;
        this.boardCopy = null;
        this.lastMovePerformedBy = null;
        this.nextMove = null;

    }

    /**
     * Constructor of the message UpdateTurnMessage, require to communicate the information regarding the next move to be performed by the player.
     *
     * @param boardCopy           A copy of the board to be shown at the players every turn.
     * @param lastMovePerformedBy Nickname of the player who performed the last move.
     * @param nextMove            The next Actions to be performed by the player during the game.
     * @param currentPlayer       The current player who must perform the specified move.
     * @param currentWorker       The worker chose by the player to play with this turn.
     */
    public UpdateTurnMessage(Board boardCopy, String lastMovePerformedBy, TurnEvents.Actions nextMove, Player currentPlayer, Worker currentWorker) {
        this.startupPhase = false;
        this.nextMove = nextMove;
        this.currentPlayer = currentPlayer;
        this.currentWorker = currentWorker;
        this.boardCopy = boardCopy;
        this.lastMovePerformedBy = lastMovePerformedBy;
        this.nextStartupMove = null;
    }

    /**
     * Allows to know if it is in the startup phase of the game.
     *
     * @return Boolean value describing if it is in the startup phase of the game.
     */
    public boolean isStartupPhase() {
        return startupPhase;
    }

    public void setStuck(boolean isStuck) {
        this.stuck = isStuck;
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setGameFinish(boolean gameFinish) {
        this.gameFinish = gameFinish;
    }

    public boolean isGameFinish() {
        return gameFinish;
    }

    public TurnEvents.StartupActions getNextStartupMove() {
        return nextStartupMove;
    }

    public TurnEvents.Actions getNextMove() {
        return nextMove;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

//    public String getLastMovePerformedBy() {
//        return lastMovePerformedBy;
//    }

    public Board getBoardCopy() {
        return boardCopy;
    }

    public ArrayList<PlayerColor> getAvailableColor() {
        return availableColor;
    }

    public void setAvailableCards(ArrayList<GodsCard> godsCard) {
        this.availableCards.addAll(godsCard);
    }

    public void setBoardCopy(Board boardCopy) {
        this.boardCopy = boardCopy;
    }

    public ArrayList<GodsCard> getAvailableCards() {
        return availableCards;
    }
}
