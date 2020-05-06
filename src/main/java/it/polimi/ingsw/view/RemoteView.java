package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.observer.Observer;

public class RemoteView /*extends View*/ {
    /*private final Connection clientConnection;

    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String message) {
        }

    }


    public RemoteView(Player player, String opponent, Connection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new MessageReceiver()); //??
        c.asyncSend("Your opponent is: " + opponent);

    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(MoveMessage message) {}
     */


}
