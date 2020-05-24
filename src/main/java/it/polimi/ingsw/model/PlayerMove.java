package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.RemoteView;

public class PlayerMove {

    private final Turn turn;
    private RemoteView remoteView;
    private final Player player;
    private final Worker movedWorker;
    private final Actions move;
    private final Slot startingSlot;
    private final Slot targetSlot;
    private boolean forcedMove = false;

    public PlayerMove(Worker worker, Actions move, Slot targetSlot, Turn turn) {
        this.turn = turn;
        this.movedWorker = worker;
        this.player = worker.getPlayerOwner();
        this.move = move;
        this.startingSlot = worker.getWorkerSlot();
        this.targetSlot = targetSlot;
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

    public Player getPlayer() {
        return player;
    }

    public Actions getMove() {
        return move;
    }

    public Slot getTargetSlot() {
        return targetSlot;
    }

    public Slot getStartingSlot() {
        return startingSlot;
    }

    //TODO: Check se deve essere necessariamente public
    public void setForcedMove(Player player) {
        if (player == turn.getCurrentPlayer())
            forcedMove = true;
    }

    public boolean getForcedMove() {
        return forcedMove;
    }
}
