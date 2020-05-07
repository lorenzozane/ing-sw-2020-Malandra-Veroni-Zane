package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public class RemoteView /*extends Observable<PlayerMove>*/ implements Observer<String> {

    private final Connection clientConnection;

    public RemoteView(Player player, String opponent, Connection c) {
        this.clientConnection = c;
//        c.addObserver(new MessageReceiver()); //??
//        c.asyncSend("Your opponent is: " + opponent);
    }

    private void handleString(String message) {

    }

    protected void sendMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    public void errorMessage(String message) {
        sendMessage(message);
    }

    @Override
    public void update(String message) {
        handleString(message);
    }
}
