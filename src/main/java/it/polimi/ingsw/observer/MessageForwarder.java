package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;

public abstract class MessageForwarder {

    //UpdateTurn
    public class UpdateTurnMessageReceiver implements Observer<UpdateTurnMessage> {

        @Override
        public void update(UpdateTurnMessage message) {
            handleUpdateTurnFromSocket(message);
        }
    }

    protected void handleUpdateTurnFromSocket(UpdateTurnMessage message) {

    };

    public class UpdateTurnMessageSender extends Observable<UpdateTurnMessage> {
        @Override
        public void addObserver(Observer<UpdateTurnMessage> observer) {
            super.addObserver(observer);
        }

        @Override
        public void removeObserver(Observer<UpdateTurnMessage> observer) {
            super.removeObserver(observer);
        }

        @Override
        public void notifyAll(UpdateTurnMessage message) {
            super.notifyAll(message);
        }
    }


    //PlayerMove
    public class PlayerMoveReceiver implements Observer<PlayerMove> {

        @Override
        public void update(PlayerMove message)  {
            handlePlayerMove(message);
        }
    }

    protected void handlePlayerMove(PlayerMove message) {

    };

    public class PlayerMoveSender extends Observable<PlayerMove> {
        @Override
        public void addObserver(Observer<PlayerMove> observer) {
            super.addObserver(observer);
        }

        @Override
        public void removeObserver(Observer<PlayerMove> observer) {
            super.removeObserver(observer);
        }

        @Override
        public void notifyAll(PlayerMove message) {
            super.notifyAll(message);
        }
    }


    //PlayerMoveStartup
    public class PlayerMoveStartupReceiver implements Observer<PlayerMoveStartup> {

        @Override
        public void update(PlayerMoveStartup message) {
            handlePlayerMoveStartup(message);
        }
    }

    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {

    };

    public class PlayerMoveStartupSender extends Observable<PlayerMoveStartup> {
        @Override
        public void addObserver(Observer<PlayerMoveStartup> observer) {
            super.addObserver(observer);
        }

        @Override
        public void removeObserver(Observer<PlayerMoveStartup> observer) {
            super.removeObserver(observer);
        }

        @Override
        public void notifyAll(PlayerMoveStartup message) {
            super.notifyAll(message);
        }
    }




    //String
    public class StringReceiver implements Observer<String> {

        @Override
        public void update(String message) {
            handleString(message);
        }
    }

    protected void handleString(String message) {

    };


    public class StringSender extends Observable<String> {
        @Override
        public void addObserver(Observer<String> observer) {
            super.addObserver(observer);
        }

        @Override
        public void removeObserver(Observer<String> observer) {
            super.removeObserver(observer);
        }

        @Override
        public void notifyAll(String message) {
            super.notifyAll(message);
        }
    }


}
