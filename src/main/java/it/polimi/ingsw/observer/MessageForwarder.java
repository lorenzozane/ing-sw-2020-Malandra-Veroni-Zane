package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;

public abstract class MessageForwarder {

    //UPDATE_TURN
    public class UpdateTurnMessageReceiver implements Observer<UpdateTurnMessage> {

        /**
         * This method is called whenever the observed object is sent through notifyAll.
         *
         * @param message The observable object to handle with the update.
         */
        @Override
        public void update(UpdateTurnMessage message) {
            handleUpdateTurnFromSocket(message);
        }
    }

    protected void handleUpdateTurnFromSocket(UpdateTurnMessage message) {

    }

    public class UpdateTurnMessageSender extends Observable<UpdateTurnMessage> {

        /**
         * Adds an observer of a specific type to the set of observers for this object, provided that it is not the same as some observer already in the set.
         *
         * @param observer An observer to be added (consistently with the generics type declaration).
         */
        @Override
        public void addObserver(Observer<UpdateTurnMessage> observer) {
            super.addObserver(observer);
        }

        /**
         * Removes an observer from the set of observers of this object.
         *
         * @param observer An observer to be removed.
         */
        @Override
        public void removeObserver(Observer<UpdateTurnMessage> observer) {
            super.removeObserver(observer);
        }

        /**
         * Notify all the listening observer the specified message.
         *
         * @param message Any object you wish to notify (consistently with the generics type declaration).
         */
        @Override
        public void notifyAll(UpdateTurnMessage message) {
            super.notifyAll(message);
        }
    }


    //PLAYER_MOVE
    public class PlayerMoveReceiver implements Observer<PlayerMove> {

        /**
         * This method is called whenever the observed object is sent through notifyAll.
         *
         * @param message The observable object to handle with the update.
         */
        @Override
        public void update(PlayerMove message)  {
            handlePlayerMove(message);
        }
    }

    protected void handlePlayerMove(PlayerMove message) {

    }

    public class PlayerMoveSender extends Observable<PlayerMove> {

        /**
         * Adds an observer of a specific type to the set of observers for this object, provided that it is not the same as some observer already in the set.
         *
         * @param observer An observer to be added (consistently with the generics type declaration).
         */
        @Override
        public void addObserver(Observer<PlayerMove> observer) {
            super.addObserver(observer);
        }

        /**
         * Removes an observer from the set of observers of this object.
         *
         * @param observer An observer to be removed.
         */
        @Override
        public void removeObserver(Observer<PlayerMove> observer) {
            super.removeObserver(observer);
        }

        /**
         * Notify all the listening observer the specified message.
         *
         * @param message Any object you wish to notify (consistently with the generics type declaration).
         */
        @Override
        public void notifyAll(PlayerMove message) {
            super.notifyAll(message);
        }
    }


    //PLAYER_MOVE_STARTUP
    public class PlayerMoveStartupReceiver implements Observer<PlayerMoveStartup> {

        /**
         * This method is called whenever the observed object is sent through notifyAll.
         *
         * @param message The observable object to handle with the update.
         */
        @Override
        public void update(PlayerMoveStartup message) {
            handlePlayerMoveStartup(message);
        }
    }

    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {

    }

    public class PlayerMoveStartupSender extends Observable<PlayerMoveStartup> {

        /**
         * Adds an observer of a specific type to the set of observers for this object, provided that it is not the same as some observer already in the set.
         *
         * @param observer An observer to be added (consistently with the generics type declaration).
         */
        @Override
        public void addObserver(Observer<PlayerMoveStartup> observer) {
            super.addObserver(observer);
        }

        /**
         * Removes an observer from the set of observers of this object.
         *
         * @param observer An observer to be removed.
         */
        @Override
        public void removeObserver(Observer<PlayerMoveStartup> observer) {
            super.removeObserver(observer);
        }

        /**
         * Notify all the listening observer the specified message.
         *
         * @param message Any object you wish to notify (consistently with the generics type declaration).
         */
        @Override
        public void notifyAll(PlayerMoveStartup message) {
            super.notifyAll(message);
        }
    }


    //STRING
    public class StringReceiver implements Observer<String> {

        /**
         * This method is called whenever the observed object is sent through notifyAll.
         *
         * @param message The observable object to handle with the update.
         */
        @Override
        public void update(String message) {
            handleString(message);
        }
    }

    protected void handleString(String message) {

    }

    public class StringSender extends Observable<String> {

        /**
         * Adds an observer of a specific type to the set of observers for this object, provided that it is not the same as some observer already in the set.
         *
         * @param observer An observer to be added (consistently with the generics type declaration).
         */
        @Override
        public void addObserver(Observer<String> observer) {
            super.addObserver(observer);
        }

        /**
         * Removes an observer from the set of observers of this object.
         *
         * @param observer An observer to be removed.
         */
        @Override
        public void removeObserver(Observer<String> observer) {
            super.removeObserver(observer);
        }

        /**
         * Notify all the listening observer the specified message.
         *
         * @param message Any object you wish to notify (consistently with the generics type declaration).
         */
        @Override
        public void notifyAll(String message) {
            super.notifyAll(message);
        }
    }
}
