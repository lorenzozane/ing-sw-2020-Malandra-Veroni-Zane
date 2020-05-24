package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;

public class RemoteView extends MessageForwarder {

    private final Connection clientConnection;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final UpdateTurnMessageSender updateTurnMessageSender = new UpdateTurnMessageSender();
    private final PlayerMoveReceiver playerMoveReceiver = new PlayerMoveReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();

    public RemoteView(Player player, String opponent, Connection c) {
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
    protected void handleUpdateTurn(UpdateTurnMessage message) {
        updateTurnMessageSender.notifyAll(message);
    }

    @Override
    protected void handlePlayerMove(PlayerMove message) {
        message.setRemoteView(this);
        playerMoveSender.notifyAll(message);
    }

    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        message.setRemoteView(this);
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
