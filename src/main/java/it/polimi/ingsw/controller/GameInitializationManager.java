package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.view.ViewMessage;

import java.util.ArrayList;

/**
 * Handle the game logic during the startup phase.
 */
public class GameInitializationManager extends MessageForwarder {

    private final Game gameInstance;
    private final Turn turn;
    private final Deck deck;
    private final ArrayList<String> chosenCardList;
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();

    /**
     * Constructor of the GameInitializationManager that deals with managing the game logic during the game startup phase.
     *
     * @param gameInstance Is the current game instance.
     */
    public GameInitializationManager(Game gameInstance) {
        this.gameInstance = gameInstance;
        this.turn = gameInstance.getTurn();
        this.deck = gameInstance.getDeck();
        chosenCardList = new ArrayList<>(gameInstance.getPlayerNumber());
    }

    /**
     * Set the god cards chosen by the challenger at the beginning of the game.
     *
     * @param message The message from which to extract the chosen god cards.
     */
    public void buildChosenCard(PlayerMoveStartup message) {
        String godCardName = message.getChosenCard();

        if (deck.isAGodName(godCardName)) {
            if (!chosenCardList.contains(godCardName)) {
                chosenCardList.add(godCardName);
                deck.removeAvailableCard(godCardName);

                if (chosenCardList.size() == gameInstance.getPlayerNumber())
                    deck.chooseCards(chosenCardList.toArray(new String[0]));

                turn.updateTurn();
            } else {
                message.getRemoteView().errorMessage(ViewMessage.cardAlreadyChoose);
            }
        } else {
            message.getRemoteView().errorMessage(ViewMessage.wrongInput);
        }
    }

    /**
     * Set the god card chosen by the player to the player himself at the beginning of the game.
     *
     * @param message The message from which to extract the chosen god card.
     */
    public void pickUpCard(PlayerMoveStartup message) {
        String godCardName = message.getChosenCard();

        if (deck.isAGodName(godCardName)) {
            try {
                turn.getCurrentPlayer().setPlayerCard(deck.pickUpCard(godCardName));
                turn.updateTurn();
            } catch (IllegalArgumentException ex) {
                message.getRemoteView().errorMessage(ViewMessage.wrongInput);
            }
        } else {
            message.getRemoteView().errorMessage(ViewMessage.wrongInput);
        }
    }

    /**
     * Set the color chosen by the player to the player himself at the beginning of the game.
     *
     * @param message The message from which to extract the chosen player color.
     */
    public void setPlayerColor(PlayerMoveStartup message) {
        PlayerColor playerColor = message.getChosenColor();

        turn.getCurrentPlayer().setPlayerColor(playerColor);
        gameInstance.removeColor(playerColor);
        turn.updateTurn();
    }

    /**
     * Place the worker in the chosen slot at the beginning of the game.
     *
     * @param message     The message from which to extract the chosen position.
     * @param workerIndex Index necessary to identify the current worker whose position to set.
     */
    private void placeWorker(PlayerMoveStartup message, int workerIndex) {
        Position workerPosition = message.getWorkerPosition();
        Board gameBoard = gameInstance.getBoard();
        Slot targetSlot = gameBoard.getSlot(workerPosition);
        if (targetSlot.getWorkerInSlot() == null) {
            message.getPlayerOwner().getWorkers().get(workerIndex - 1).setWorkerSlot(targetSlot);
            turn.updateTurn();
        } else {
            message.getRemoteView().errorMessage(ViewMessage.slotOccupied);
        }
    }

    /**
     * Method to handle message received by the PlayerMoveStartupReceiver.
     *
     * @param message The message to handle.
     */
    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        message.setPlayerOwner(gameInstance.getPlayerByName(message.getPlayerOwnerNickname()));

        if (message.getAction() == StartupActions.COLOR_REQUEST ||
                message.getAction() == StartupActions.PICK_LAST_COLOR)
            setPlayerColor(message);
        else if (message.getAction() == StartupActions.CHOOSE_CARD_REQUEST)
            buildChosenCard(message);
        else if (message.getAction() == StartupActions.PICK_UP_CARD_REQUEST ||
                message.getAction() == StartupActions.PICK_LAST_CARD)
            pickUpCard(message);
        else if (message.getAction() == StartupActions.PLACE_WORKER_1)
            placeWorker(message, 1);
        else if (message.getAction() == StartupActions.PLACE_WORKER_2)
            placeWorker(message, 2);
    }

    public PlayerMoveStartupReceiver getPlayerMoveStartupReceiver() {
        return playerMoveStartupReceiver;
    }

}
