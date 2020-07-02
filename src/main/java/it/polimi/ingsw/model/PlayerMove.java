package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.RemoteView;

import java.io.Serializable;

/**
 * PlayerMove message, require to send the information regarding the move performed by the player during the game.
 */
public class PlayerMove implements Serializable {

    private static final long serialVersionUID = -7736062440222355391L;
    private final String currentPlayer;
    private RemoteView remoteView;
    private final Actions move;
    private Position startingPosition;
    private final Position targetPosition;
    private Slot startingSlot;
    private Slot targetSlot;
    private final String movedWorkerId;
    private Worker movedWorker;
    private String playerOwnerNickname;
    private Player playerOwner;
    private boolean forcedMove = false;

    /**
     * Constructor of the message PlayerMove, required to send the information regarding the move performed by the player during the game.
     *
     * @param workerId The ID of the worker chosen by the player to play with this turn.
     * @param move The Actions performed by the player.
     * @param targetPosition The position of the target slot chose by the player for the current move.
     * @param currentPlayerNickname The nickname of the current player.
     */
    public PlayerMove(String workerId, Actions move, Position targetPosition, String currentPlayerNickname) {
        this.currentPlayer = currentPlayerNickname;
        this.movedWorkerId = workerId;
//        this.movedWorker = worker;
//        this.playerOwner = worker.getPlayerOwner();
        this.move = move;
//        this.startingPosition = worker.getWorkerPosition();
        this.targetPosition = targetPosition;
    }

    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public void setMovedWorker(Worker movedWorker) {
        if (this.movedWorker == null) {
            this.movedWorker = movedWorker;
            setStartingSlot(movedWorker.getWorkerSlot());
        }
    }

    public Worker getMovedWorker() {
        return movedWorker;
    }

    public String getMovedWorkerId() {
        return movedWorkerId;
    }

    public void setPlayerOwnerNickname(String playerOwnerNickname) {
        if (this.playerOwnerNickname == null)
            this.playerOwnerNickname = playerOwnerNickname;
    }

    public String getPlayerOwnerNickname() {
        return playerOwnerNickname;
    }

    public void setPlayerOwner(Player playerOwner) {
        if (this.playerOwner == null)
            this.playerOwner = playerOwner;
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }

    public Actions getMove() {
        return move;
    }

    public void setStartingSlot(Slot startingSlot) {
        if (this.startingSlot == null) {
            this.startingSlot = startingSlot;
            this.startingPosition = this.startingSlot.getSlotPosition();
        }
    }

    public void setTargetSlot(Slot targetSlot) {
        if (this.targetSlot == null)
            this.targetSlot = targetSlot;
    }

    public Slot getStartingSlot() {
        return startingSlot;
    }

    public Slot getTargetSlot() {
        return targetSlot;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setForcedMove(Player player) {
        if (player.getNickname().equals(currentPlayer))
            forcedMove = true;
    }

    public boolean getForcedMove() {
        return forcedMove;
    }
}
