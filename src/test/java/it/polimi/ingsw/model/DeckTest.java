package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DeckTest {

    Game gameInstance = new Game();

    @Test
    public void buildDeckTest() {
        Deck deck = gameInstance.getDeck();
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
        gameInstance.setPlayerNumber(3);
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "athena");
        ArrayList<GodsCard> chosenCards = deck.getChosenCardsCopy();
//        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (GodsCard godCard : chosenCards) {
            assertTrue(chosenCardsExpected.contains(godCard.getCardName()));
            chosenCardsExpected.remove(godCard.getCardName());
        }
//        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
//            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
//            chosenCardsExpected.remove(godCard.getValue().getCardName());
//        }
    }

    @Test
    public void pickUpCardTest() {
        gameInstance.setPlayerNumber(3);
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "athena");
        GodsCard godsCard = deck.pickUpCard("apollo");
        GodsCard godsCardExpected = new GodsCard("apollo");

        assertEquals(godsCardExpected.getCardName(), godsCard.getCardName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionArgumentNumber() {
        Deck deck = gameInstance.getDeck();
        gameInstance.setPlayerNumber(3);
        deck.chooseCards("apollo");
        ArrayList<GodsCard> chosenCards = deck.getChosenCardsCopy();
//        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (GodsCard godCard : chosenCards) {
            assertTrue(chosenCardsExpected.contains(godCard.getCardName()));
            chosenCardsExpected.remove(godCard.getCardName());
        }
//        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
//            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
//            chosenCardsExpected.remove(godCard.getValue().getCardName());
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionWrongCard() {
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "foo");
        ArrayList<GodsCard> chosenCards = deck.getChosenCardsCopy();
//        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (GodsCard godCard : chosenCards) {
            assertTrue(chosenCardsExpected.contains(godCard.getCardName()));
            chosenCardsExpected.remove(godCard.getCardName());
        }
//        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
//            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
//            chosenCardsExpected.remove(godCard.getValue().getCardName());
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseCardsTestExceptionSameCardTwice() {
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "apollo");
        ArrayList<GodsCard> chosenCards = deck.getChosenCardsCopy();
//        HashMap<String, GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis", "athena"));

        for (GodsCard godCard : chosenCards) {
            assertTrue(chosenCardsExpected.contains(godCard.getCardName()));
            chosenCardsExpected.remove(godCard.getCardName());
        }
//        for (Map.Entry<String, GodsCard> godCard : chosenCards.entrySet()) {
//            assertTrue(chosenCardsExpected.contains(godCard.getValue().getCardName()));
//            chosenCardsExpected.remove(godCard.getValue().getCardName());
//        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void pickUpCardTestException() {
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "athena");
        GodsCard godsCard = deck.pickUpCard("atlas");
        GodsCard godsCardExpected = new GodsCard("atlas");

        assertEquals(godsCardExpected.getCardName(), godsCard.getCardName());
    }

    @Test
    public void getCardDescriptionTest() {
        gameInstance.setPlayerNumber(3);
        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis", "athena");
        GodsCard godsCard = deck.pickUpCard("apollo");

        assertEquals("Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.",
                godsCard.getCardDescription());
        assertEquals("God APOLLO: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.",
                godsCard.toString());
    }
}