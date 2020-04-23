package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TurnTest {

    @Test //completed
    public void updateTurnTest() throws ParseException {
        Game game = Game.getInstance();
        Turn turn = game.getTurn();
        Player player1 = new Player("lorenzo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("pippo");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        game.setPlayerNumber(2);
        Turn.setPlayerOrder(player1, player2);

        turn.updateTurn();
        assertEquals(turn.getCurrentPlayerWorkers(), player2.getWorkers());

        turn.updateTurn();
        assertEquals(turn.getCurrentPlayerWorkers(), player1.getWorkers());
    }

    @Test //completed
    public void getNextPlayerTest() throws ParseException {
        Game game = Game.getInstance();
        Turn turn = game.getTurn();
        Player player1 = new Player("lorenzo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("pippo");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        game.setPlayerNumber(2);
        Turn.setPlayerOrder(player1, player2);

        turn.updateTurn();
        assertEquals(turn.getNextPlayer(), player1);

        turn.updateTurn();
        assertEquals(turn.getNextPlayer(), player2);
    }

    @Test
    public void setUpMoveSequenceTest() throws ParseException {
        Game game = Game.getInstance();
        game.setPlayerNumber(1);
        Turn turn = new Turn();
        Player player1 = new Player("pippo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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