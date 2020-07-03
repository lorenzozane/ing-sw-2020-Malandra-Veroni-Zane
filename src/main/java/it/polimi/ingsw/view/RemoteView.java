package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;

/**
 * Allow to the game instance (server side) to communicate with a specific player.
 */
public class RemoteView extends MessageForwarder {

    private final String playerOwner;
    private final SocketConnection clientConnection;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final UpdateTurnMessageSender updateTurnMessageSender = new UpdateTurnMessageSender();
    private final PlayerMoveReceiver playerMoveReceiver = new PlayerMoveReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();

    public RemoteView(String playerOwner, SocketConnection c) {
        this.playerOwner = playerOwner;
        this.clientConnection = c;
    }

    protected void sendMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    public void errorMessage(String message) {
        sendMessage(message);
    }

    public void resetBoard(Object message) {
        sendMessage(message);
    }

    @Override
    protected void handleUpdateTurnMessage(UpdateTurnMessage message) {
        updateTurnMessageSender.notifyAll(message);
    }

    @Override
    protected void handlePlayerMove(PlayerMove message) {
        message.setRemoteView(this);
        message.setPlayerOwnerNickname(playerOwner);
        playerMoveSender.notifyAll(message);
    }

    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        message.setRemoteView(this);
        message.setPlayerOwnerNickname(playerOwner);
        playerMoveStartupSender.notifyAll(message);
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public PlayerMoveReceiver getPlayerMoveReceiver() {
        return playerMoveReceiver;
    }

    public PlayerMoveStartupReceiver getPlayerMoveStartupReceiver() {
        return playerMoveStartupReceiver;
    }

    public void addUpdateTurnMessageObserver(Observer<UpdateTurnMessage> observer) {
        updateTurnMessageSender.addObserver(observer);
    }

    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }
}
