package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.view.RemoteView;

import java.io.Serializable;

public class PlayerMoveStartup implements Serializable {

    private static final long serialVersionUID = -7581105397668977776L;
    //private final Turn turn; //TODO: Da togliere (?)
    private RemoteView remoteView;
    private String playerOwnerNickname; //TODO: Volendo si può settare direttamente nella View (facendo un controllo successivo nella remoteView)
    private Player playerOwner;
    private final StartupActions action;
    private PlayerColor chosenColor;
    private String chosenCard;
    private Position workerPosition;

    public PlayerMoveStartup(StartupActions action) {
//        this.playerOwner = playerOwner;
        this.action = action;
    }

    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
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
