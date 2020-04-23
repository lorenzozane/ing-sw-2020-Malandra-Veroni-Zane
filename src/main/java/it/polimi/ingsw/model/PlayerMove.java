package it.polimi.ingsw.model;

public class PlayerMove {

    private final Player player;
    private final Worker movedWorker;
    private final TurnEvents.Actions move;
    private final Slot startingSlot;
    private final Slot targetSlot;
    private boolean forcedMove = false;

    public PlayerMove(Worker worker, TurnEvents.Actions move, Slot targetSlot) {
        this.movedWorker = worker;
        this.player = worker.getPlayerOwner();
        this.move = move;
        this.startingSlot = worker.getWorkerSlot();
        this.targetSlot = targetSlot;
    }

    public Worker getMovedWorker() {
        return movedWorker;
    }

    public Player getPlayer() {
        return player;
    }

    public TurnEvents.Actions getMove() {
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
        if (player == Game.getInstance().getTurn().getCurrentWorker().getPlayerOwner())
            forcedMove = true;
    }

    public boolean getForcedMove() {
        return forcedMove;
    }
}
