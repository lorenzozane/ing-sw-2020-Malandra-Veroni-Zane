package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.UpdateTurnMessage;

/**
 * Encompasses all the Receiver and Sender for all the message type, necessary for the correct functioning
 * of the game messages.
 */
public abstract class MessageForwarder {

    //UPDATE_TURN

    /**
     * Class deed to receive instance of UpdateTurnMessage.
     */
    public class UpdateTurnMessageReceiver implements Observer<UpdateTurnMessage> {

        /**
         * This method is called whenever the observed object is sent through notifyAll.
         *
         * @param message The observable object to handle with the update.
         */
        @Override
        public void update(UpdateTurnMessage message) {
            handleUpdateTurnMessage(message);
        }
    }

    /**
     * Method to override to handle message received by the UpdateTurnMessageReceiver.
     *
     * @param message The message to handle.
     */
    protected void handleUpdateTurnMessage(UpdateTurnMessage message) {

    }

    /**
     * Class deed to send instance of UpdateTurnMessage.
     */
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
    /**
     * Class deed to receive instance of PlayerMove.
     */
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

    /**
     * Method to override to handle message received by the PlayerMoveReceiver.
     *
     * @param message The message to handle.
     */
    protected void handlePlayerMove(PlayerMove message) {

    }

    /**
     * Class deed to send instance of PlayerMove.
     */
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
    /**
     * Class deed to receive instance of PlayerMoveStartup.
     */
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

    /**
     * Method to override to handle message received by the PlayerMoveStartupReceiver.
     *
     * @param message The message to handle.
     */
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {

    }

    /**
     * Class deed to send instance of PlayerMoveStartup.
     */
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
    /**
     * Class deed to receive instance of String.
     */
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

    /**
     * Method to override to handle message received by the StringReceiver.
     *
     * @param message The message to handle.
     */
    protected void handleString(String message) {

    }

    /**
     * Class deed to send instance of String.
     */
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
