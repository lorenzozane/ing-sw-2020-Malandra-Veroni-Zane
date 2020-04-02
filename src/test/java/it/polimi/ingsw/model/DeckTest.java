package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void buildDeckTest(){
        Deck deck = new Deck();
        ArrayList<GodsCard> cardList = deck.getCardList();
        List<String> godsName = new LinkedList<String>(Arrays.asList("apollo", "artemis", "athena", "atlas", "demeter", "hephaestus", "minotaur", "pan", "prometheus"));

        assertEquals(cardList.size(), godsName.size());

        for (int i = 0; i < cardList.size(); i++){
            assertTrue(godsName.contains(cardList.get(i).getCardName().toString()));
            godsName.remove(cardList.get(i).getCardName());
        }
    }

}