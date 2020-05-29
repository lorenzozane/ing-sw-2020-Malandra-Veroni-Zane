package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.RemoteView;

public class PlayerMove {

    private final String currentPlayer;
    private RemoteView remoteView;
    private final Player playerOwner;
    private final Worker movedWorker;
    private final Actions move;
    private final Position startingPosition;
    private final Position targetPosition;
    private Slot startingSlot;
    private Slot targetSlot;
    private boolean forcedMove = false;

    public PlayerMove(Worker worker, Actions move, Position targetPosition, String currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.movedWorker = worker;
        this.playerOwner = worker.getPlayerOwner();
        this.move = move;
        this.startingPosition = worker.getWorkerPosition();
        this.targetPosition = targetPosition;
    }

    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public Worker getMovedWorker() {
        return movedWorker;
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }

    public Actions getMove() {
        return move;
    }

    public void setStartingSlot(Slot startingSlot) {
        if (this.startingSlot == null)
            this.startingSlot = startingSlot;
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

    //TODO: Check se deve essere necessariamente public
    public void setForcedMove(Player player) {
        if (player.getNickname().equals(currentPlayer))
            forcedMove = true;
    }

    public boolean getForcedMove() {
        return forcedMove;
    }
}
