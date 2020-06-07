package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.view.ViewMessage;

import java.util.ArrayList;


public class GameInitializationManager extends MessageForwarder {

    //TODO: Metodi inizializzazione deck e scelta carte (challenge)

    private final Game gameInstance;
    private final Turn turn;
    private final Deck deck;
    private final ArrayList<String> chosenCardList;
    private final PlayerMoveStartupReceiver playerMoveStartupReceiver = new PlayerMoveStartupReceiver();

    public GameInitializationManager(Game gameInstance) {
        this.gameInstance = gameInstance;
        this.turn = gameInstance.getTurn();
        this.deck = gameInstance.getDeck();
        chosenCardList = new ArrayList<>(gameInstance.getPlayerNumber());
    }


    //arriva il messaggio con il nome del dio scelto dal challenger e lo gestisce
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

    public void setPlayerColor(PlayerMoveStartup message) {
        PlayerColor playerColor = message.getChosenColor();

        turn.getCurrentPlayer().setPlayerColor(playerColor);
        gameInstance.removeColor(playerColor);
        turn.updateTurn();
    }

    //TODO: Completare posizionamento del worker
    private void placeWorker(PlayerMoveStartup message) {
        Position workerPosition = message.getWorkerPosition();

        Board gameBoard = gameInstance.getBoard();


    }

    @Override
    protected void handlePlayerMoveStartup(PlayerMoveStartup message) {
        if (message.getAction() == StartupActions.COLOR_REQUEST)
            setPlayerColor(message);
        else if (message.getAction() == StartupActions.CHOOSE_CARD_REQUEST)
            buildChosenCard(message);
        else if (message.getAction() == StartupActions.PICK_UP_CARD_REQUEST)
            pickUpCard(message);
        else if (message.getAction() == StartupActions.PLACE_WORKER)
            placeWorker(message);

        //TODO: fare update del turno? per inviare un nuovo turn message?
    }

    public PlayerMoveStartupReceiver getPlayerMoveStartupReceiver() {
        return playerMoveStartupReceiver;
    }

}
