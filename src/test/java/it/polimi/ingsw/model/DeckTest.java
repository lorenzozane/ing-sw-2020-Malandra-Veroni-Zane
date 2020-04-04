package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void buildDeckTest() {
        Deck deck = new Deck();
        ArrayList<GodsCard> cardList = deck.getCardList();
        ArrayList<String> godsName = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena", "atlas", "demeter", "hephaestus", "minotaur", "pan", "prometheus"));

        assertEquals(cardList.size(), godsName.size());

        for (int i = 0; i < cardList.size(); i++) {
            assertTrue(godsName.contains(cardList.get(i).getCardName()));
            godsName.remove(cardList.get(i).getCardName());
        }
    }

    @Test
    public void chooseCardsTest() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "athena");
        ArrayList<GodsCard> chosenCards = deck.getChosenCards();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (int i = 0; i < chosenCards.size(); i++) {
            assertTrue(chosenCardsExpected.contains(chosenCards.get(i).getCardName()));
            chosenCardsExpected.remove(chosenCards.get(i).getCardName());
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

    @Test (expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionArgumentNumber() {
        Deck deck = new Deck();
        deck.chooseCards("apollo");
        ArrayList<GodsCard> chosenCards = deck.getChosenCards();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (int i = 0; i < chosenCards.size(); i++) {
            assertTrue(chosenCardsExpected.contains(chosenCards.get(i).getCardName()));
            chosenCardsExpected.remove(chosenCards.get(i).getCardName());
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionWrongCard() {
        Deck deck = new Deck();
        deck.chooseCards("apollo", "artemis", "foo");
        ArrayList<GodsCard> chosenCards = deck.getChosenCards();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (int i = 0; i < chosenCards.size(); i++) {
            assertTrue(chosenCardsExpected.contains(chosenCards.get(i).getCardName()));
            chosenCardsExpected.remove(chosenCards.get(i).getCardName());
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionSameCardTwice() {

    }

    @Test (expected = IllegalArgumentException.class)
    public void pickUpCardTestException() {

    }
}