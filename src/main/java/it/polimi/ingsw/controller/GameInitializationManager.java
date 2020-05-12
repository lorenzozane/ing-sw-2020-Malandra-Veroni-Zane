package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.observer.Observer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;


public class GameInitializationManager implements Observer<SimpleEntry<Class<?>, String>> {

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

    public void buildChosenCard(String godCardName) {
        if (deck.isAGodName(godCardName))
            chosenCardList.add(godCardName);

        if (chosenCardList.size() == gameInstance.getPlayerNumber())
            deck.chooseCards(chosenCardList.toArray(new String[0]));
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

    //TODO: Togliere (?) non serve più, chiediamo la data di nascita prima dell'inizio della partita
    public void setBirthday(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat();
            turn.getCurrentPlayer().setBirthday(dateFormat.parse(date));
        } catch (ParseException ex) {
            System.err.println("ParseException already handled, something went wrong in the client side date control.");
            turn.getCurrentPlayer().setBirthday(new Date("1/1/2020"));
        }
    }


    @Override
    public void update(SimpleEntry<Class<?>, String> message) {
        if (message.getKey() == Date.class) {
            setBirthday(message.getValue());
        } else if (message.getKey() == Color.class) {
            setPlayerColor(message.getValue());
        } else if (deck.isAGodName(message.getValue())) {
            buildChosenCard(message.getValue());
        }
    }
}
