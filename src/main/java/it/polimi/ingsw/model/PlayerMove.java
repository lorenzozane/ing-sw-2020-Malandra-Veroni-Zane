package it.polimi.ingsw.model;

public class PlayerMove {

    private final Player player;
    private final TurnEvents.Actions move;
    private final Slot targetSlot;

    public PlayerMove(Player player, TurnEvents.Actions move, Slot targetSlot){
        this.player = player;
        this.move = move;
        this.targetSlot = targetSlot;
    }

    public Player getPlayer(){
        return player;
    }

    public TurnEvents.Actions getMove(){
        return move;
    }

    public Slot getTargetSlot(){
        return targetSlot;
    }
}
