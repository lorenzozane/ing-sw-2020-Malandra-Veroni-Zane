package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void buildDeckTest() {
        Deck deck = new Deck();
        ArrayList<GodsCard> cardList = deck.getCardListCopy();
        ArrayList<String> godsName = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena", "atlas", "demeter", "hephaestus", "minotaur", "pan", "prometheus"));

        assertEquals(cardList.size(), godsName.size());

        for (int i = 0; i < cardList.size(); i++) {
            assertTrue(godsName.contains(cardList.get(i).getCardName()));
            godsName.remove(cardList.get(i).getCardName());
        }
    }

    @Test
    public void chooseCardsTest() {
        Game getInstance = Game.getInstance();
        getInstance.setPlayerNumber(3);
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "athena");
        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
            chosenCardsExpected.remove(godCard.getValue().getCardName());
        }
    }

    @Test
    public void pickUpCardTest() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "athena");
        GodsCard godsCard = deck.pickUpCard("apollo");
        GodsCard godsCardExpected = new GodsCard("apollo");

        assertEquals(godsCardExpected.getCardName(), godsCard.getCardName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionArgumentNumber() {
        Deck deck = new Deck();
        deck.chooseCards("apollo");
        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
            chosenCardsExpected.remove(godCard.getValue().getCardName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionWrongCard() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "foo");
        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
            chosenCardsExpected.remove(godCard.getValue().getCardName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionSameCardTwice() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "apollo");
        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
            chosenCardsExpected.remove(godCard.getValue().getCardName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void pickUpCardTestException() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "athena");
        GodsCard godsCard = deck.pickUpCard("atlas");
        GodsCard godsCardExpected = new GodsCard("atlas");

        assertEquals(godsCardExpected.getCardName(), godsCard.getCardName());
    }
}