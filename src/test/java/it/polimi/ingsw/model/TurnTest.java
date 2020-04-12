package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class TurnTest {

    @Test
    public void setUpMoveSequenceTest(){
        Turn turn = new Turn();
        Game game = Game.getInstance();
        game.setPlayerNumber(1);
        Player player1 = new Player("pippo");
        Date date = new Date("1/1/2011");
        player1.setBirthday(date);
        Deck deck = new Deck();
        deck.chooseCards("apollo");
        GodsCard godsCard = deck.pickUpCard("apollo");
        player1.setPlayerCard(godsCard);

        turn.setPlayerOrder(player1);

        turn.setUpMoveSequence();

    }

}