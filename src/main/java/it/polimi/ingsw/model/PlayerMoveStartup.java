package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.view.RemoteView;

import java.io.Serializable;

public class PlayerMoveStartup implements Serializable {

    //private final Turn turn; //TODO: Da togliere (?)
    private RemoteView remoteView;
    private final Player playerOwner;
    private final StartupActions action;
    private PlayerColor chosenColor;
    private String chosenCard;
    private Position workerPosition;

    public PlayerMoveStartup(Player playerOwner, StartupActions action/*, Turn turn*/) {
        this.playerOwner = playerOwner;
        this.action = action;
        //this.turn = turn;
    }

    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public Player getPlayerOwner() {
        return playerOwner;
    }

    public StartupActions getAction() {
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
