package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observer.Observer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


//TODO: Modificare gestione tipo osservato (Tupla o Enum)
public class GameInitializationManager implements Observer<AbstractMap.SimpleEntry> {

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
            deck.chooseCards(chosenCardList.toArray(new String[0])); //TODO: Check
    }

    public void setPlayerColor(String playerColor) {
        switch (playerColor) {
            case "red":
                turn.getCurrentPlayer().setPlayerColor(Color.ANSI_RED);
                gameInstance.removeColor(Color.ANSI_RED);
                break;
            case "cyan":
                turn.getCurrentPlayer().setPlayerColor(Color.ANSI_BRIGHT_CYAN);
                gameInstance.removeColor(Color.ANSI_BRIGHT_CYAN);
                break;
            case "yellow":
                turn.getCurrentPlayer().setPlayerColor(Color.ANSI_YELLOW);
                gameInstance.removeColor(Color.ANSI_YELLOW);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setBirthday(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat();
        turn.getCurrentPlayer().setBirthday(dateFormat.parse(date));
    }


    @Override
    public void update(AbstractMap.SimpleEntry message) {

    }
}
