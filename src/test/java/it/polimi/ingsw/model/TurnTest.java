package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TurnTest {

    @Test
    public void updateToNextPlayerTurnTest() throws ParseException {
        Game gameInstance = new Game();
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("lorenzo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("pippo");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        gameInstance.setPlayerNumber(2);
        turn.setPlayerOrder(player1, player2);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        turn.setUpGameTurn();

        assertEquals(turn.getCurrentPlayerWorkers(), player2.getWorkers());

        turn.updateToNextPlayerTurn();
        assertEquals(turn.getCurrentPlayerWorkers(), player1.getWorkers());
    }

    @Test
    public void getNextPlayerTest() throws ParseException {
        Game gameInstance = new Game();
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("lorenzo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("pippo");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        gameInstance.setPlayerNumber(2);
        turn.setPlayerOrder(player1, player2);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        turn.setUpGameTurn();

        assertEquals(turn.getNextPlayer(), player1);

        turn.updateToNextPlayerTurn();
        assertEquals(turn.getNextPlayer(), player2);
    }

    @Test
    public void setUpMoveSequenceTest() throws ParseException {
        Game gameInstance = new Game();
        gameInstance.setPlayerNumber(1);
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("pippo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Deck deck = new Deck(gameInstance);
        deck.chooseCards("apollo");
        GodsCard godsCard = deck.pickUpCard("apollo");
        player1.setPlayerCard(godsCard);

        turn.setPlayerOrder(player1);

        turn.setUpTurnSequence();

    }

}