package it.polimi.ingsw.model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TurnTest {

    @Test
    public void setUpMoveSequenceTest() throws ParseException {
        Turn turn = new Turn();
        Player player1 = new Player("pippo");
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Deck deck = new Deck();
        deck.chooseCards("apollo");
        GodsCard godsCard = deck.pickUpCard("apollo");
        player1.setPlayerCard(godsCard);

        turn.setPlayerOrder(player1);

        turn.setUpTurnSequence();

    }

}