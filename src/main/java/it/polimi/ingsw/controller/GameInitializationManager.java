package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.SetUpActions;
import it.polimi.ingsw.observer.Observer;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;


public class GameInitializationManager implements Observer<PlayerMoveStartup> {

    //TODO: Metodi inizializzazione deck e scelta carte (challenge)

    private final Game gameInstance;
    private final Turn turn;
    private final Deck deck;
    private final ArrayList<String> chosenCardList;

    public GameInitializationManager(Game gameInstance) {
        this.gameInstance = gameInstance;
        this.turn = gameInstance.getTurn();
        this.deck = gameInstance.getDeck();
        chosenCardList = new ArrayList<>(gameInstance.getPlayerNumber());
    }

    public void buildChosenCard(PlayerMoveStartup message) {
        String godCardName = message.getChosenCard();

        if (deck.isAGodName(godCardName))
            chosenCardList.add(godCardName);

        if (chosenCardList.size() == gameInstance.getPlayerNumber())
            deck.chooseCards(chosenCardList.toArray(new String[0]));
    }

    public void pickUpCard(PlayerMoveStartup message) {
        String godCardName = message.getChosenCard();

        if (deck.isAGodName(godCardName))
            turn.getCurrentPlayer().setPlayerCard(deck.pickUpCard(godCardName));
    }

    public void setPlayerColor(String playerColor) {
        switch (playerColor) {
            case "red":
                turn.getCurrentPlayer().setPlayerColor(PlayerColor.RED);
                gameInstance.removeColor(PlayerColor.RED);
                break;
            case "cyan":
                turn.getCurrentPlayer().setPlayerColor(PlayerColor.CYAN);
                gameInstance.removeColor(PlayerColor.CYAN);
                break;
            case "yellow":
                turn.getCurrentPlayer().setPlayerColor(PlayerColor.YELLOW);
                gameInstance.removeColor(PlayerColor.YELLOW);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setPlayerColor(PlayerMoveStartup message) {
        PlayerColor playerColor = message.getChosenColor();

        turn.getCurrentPlayer().setPlayerColor(playerColor);
        gameInstance.removeColor(playerColor);
    }

    private void placeWorker(PlayerMoveStartup message) {
        Position workerPosition = message.getWorkerPosition();

        Board gameBoard = gameInstance.getBoard();


    }

//    @Override
//    public void update(SimpleEntry<Class<?>, String> message) {
//        if (message.getKey() == Color.class) {
//            setPlayerColor(message.getValue());
//        } else if (deck.isAGodName(message.getValue())) {
//            buildChosenCard(message.getValue());
//        }
//    }

    @Override
    public void update(PlayerMoveStartup message) {
        if (message.getAction() == SetUpActions.COLOR_REQUEST)
            setPlayerColor(message);
        else if (message.getAction() == SetUpActions.CHOOSE_CARD_REQUEST)
            buildChosenCard(message);
        else if (message.getAction() == SetUpActions.PICK_UP_CARD_REQUEST)
            pickUpCard(message);
        else if (message.getAction() == SetUpActions.PLACE_WORKER)
            placeWorker(message);
    }

}
