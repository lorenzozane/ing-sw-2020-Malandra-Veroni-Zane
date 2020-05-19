package it.polimi.ingsw.model;

import it.polimi.ingsw.view.RemoteView;

public class PlayerMoveStartup {

    private final Turn turn;
    private final RemoteView remoteView;
    private final Player playerOwner;
    private final TurnEvents.SetUpActions action;

    public PlayerMoveStartup(Player playerOwner, TurnEvents.SetUpActions action, Turn turn, RemoteView remoteView) {
        this.playerOwner = playerOwner;
        this.action = action;
        this.turn = turn;
        this.remoteView = remoteView;
    }

    public TurnEvents.SetUpActions getAction() {
        return action;
    }
}
