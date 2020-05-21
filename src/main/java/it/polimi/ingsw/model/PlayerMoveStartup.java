package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.SetUpActions;
import it.polimi.ingsw.view.RemoteView;

public class PlayerMoveStartup {

    private final Turn turn;
    private final RemoteView remoteView;
    private final Player playerOwner;
    private final SetUpActions action;
    private PlayerColor chosenColor;
    private String chosenCard;
    private Position workerPosition;

    public PlayerMoveStartup(Player playerOwner, SetUpActions action, Turn turn, RemoteView remoteView) {
        this.playerOwner = playerOwner;
        this.action = action;
        this.turn = turn;
        this.remoteView = remoteView;
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }

    public SetUpActions getAction() {
        return action;
    }

    public void setChosenColor(PlayerColor chosenColor) {
        this.chosenColor = chosenColor;
    }

    public PlayerColor getChosenColor() {
        return chosenColor;
    }

    public void setChosenCard(String chosenCard) {
        this.chosenCard = chosenCard;
    }

    public String getChosenCard() {
        return chosenCard;
    }

    public void setWorkerPosition(Position workerPosition) {
        this.workerPosition = workerPosition;
    }

    public Position getWorkerPosition() {
        return workerPosition;
    }
}
