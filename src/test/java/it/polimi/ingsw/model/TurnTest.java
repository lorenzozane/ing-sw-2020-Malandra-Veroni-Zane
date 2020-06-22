package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.TurnEvents.Actions.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class TurnTest {

    @Test
    public void updateToNextPlayerTurnTest() throws ParseException {
        Game gameInstance = new Game();
        gameInstance.setPlayerNumber(2);
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        Worker worker1Player1 = player1.getWorkers().get(0);
        Worker worker2Player1 = player1.getWorkers().get(1);
        Worker worker1Player2 = player2.getWorkers().get(0);
        Worker worker2Player2 = player2.getWorkers().get(1);
        worker1Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(0, 0)));
        worker2Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 4)));
        worker1Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(1, 0)));
        worker2Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 3)));

        turn.setPlayerOrder(player1, player2);
        turn.setUpGameTurn();

        assertEquals(turn.getCurrentPlayerWorkers(), player2.getWorkers());

        turn.updateToNextPlayerTurn();
        assertEquals(turn.getCurrentPlayerWorkers(), player1.getWorkers());
    }

    @Test
    public void getNextPlayerTest() throws ParseException {
        Game gameInstance = new Game();
        gameInstance.setPlayerNumber(2);
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/2000");
        player2.setBirthday(date);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        Worker worker1Player1 = player1.getWorkers().get(0);
        Worker worker2Player1 = player1.getWorkers().get(1);
        Worker worker1Player2 = player2.getWorkers().get(0);
        Worker worker2Player2 = player2.getWorkers().get(1);
        worker1Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(0, 0)));
        worker2Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 4)));
        worker1Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(1, 0)));
        worker2Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 3)));

        turn.setPlayerOrder(player1, player2);
        turn.setUpGameTurn();

        assertEquals(turn.getNextPlayer(), player1);

        turn.updateToNextPlayerTurn();
        assertEquals(turn.getNextPlayer(), player2);
    }

    @Test
    public void setUpMoveSequenceTest() throws ParseException {
        Game gameInstance = new Game();
        gameInstance.setPlayerNumber(2);
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        turn.setPlayerOrder(player1, player2);
        turn.setUpTurnSequence();

        ArrayList<TurnEvents.Actions> expectedActions = new ArrayList<>(Arrays.asList(CHOSE_WORKER, MOVE_OPPONENT_SLOT_FLIP, BUILD_STANDARD, WAIT_FOR_UNDO));
        assertEquals(expectedActions, turn.getCurrentPlayerTurnSequence().getMoveSequence());

        ArrayList<TurnEvents.WinConditions> expectedWinConditions = new ArrayList<>(Collections.singletonList(TurnEvents.WinConditions.WIN_STANDARD));
        assertEquals(expectedWinConditions, turn.getCurrentPlayerTurnSequence().getWinConditions());
    }

}