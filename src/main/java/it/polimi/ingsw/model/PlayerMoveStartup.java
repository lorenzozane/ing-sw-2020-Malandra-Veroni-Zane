package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.view.RemoteView;

import java.io.Serializable;

/**
 * PlayerMoveStartup message, require to send the information regarding the move performed by the player during startup phase of the game.
 */
public class PlayerMoveStartup implements Serializable {

    private static final long serialVersionUID = -7581105397668977776L;
    private RemoteView remoteView;
    private String playerOwnerNickname;
    private Player playerOwner;
    private final StartupActions action;
    private PlayerColor chosenColor;
    private String chosenCard;
    private Position workerPosition;

    /**
     * Constructor of the message PlayerMove, required to send the information regarding the move performed by the player during the startup phase of the game.
     *
     * @param action The StartupActions performed by the player.
     */
    public PlayerMoveStartup(StartupActions action) {
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
