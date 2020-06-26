package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

public class UpdateTurnMessageTest {

    Game gameInstance = new Game();
    Turn turn = gameInstance.getTurn();

    @Before
    public void setUp() throws ParseException, IllegalAccessException {
        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteView = new RemoteView(player1.getNickname(), socketConnection);

        turn.addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("prometheus", "artemis");
        player1.setPlayerCard(deck.pickUpCard("prometheus"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        Worker worker1Player1 = player1.getWorkers().get(0);
        Worker worker2Player1 = player1.getWorkers().get(1);
        Worker worker1Player2 = player2.getWorkers().get(0);
        Worker worker2Player2 = player2.getWorkers().get(1);
        worker1Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(0, 0)));
        worker2Player1.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 4)));
        worker1Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(1, 0)));
        worker2Player2.setWorkerSlot(gameInstance.getBoard().getSlot(new Position(4, 3)));

        turn.setUpGameTurn();

        Slot startingSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        Slot opponentSlot = gameInstance.getBoard().getSlot(new Position(1, 0));
        Slot buildingSlot = gameInstance.getBoard().getSlot(new Position(1, 1));
        Slot buildBeforeSlot = gameInstance.getBoard().getSlot(new Position(0, 2));
    }

    @Test
    public void stuckTest() {
        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                gameInstance.getBoard(),
                null,
                TurnEvents.Actions.MOVE_STANDARD,
                turn.currentPlayer,
                turn.currentWorker
        );
        updateTurnMessage.setStuck(true);
        assertTrue(updateTurnMessage.isStuck());
        updateTurnMessage.setStuck(false);
        assertFalse(updateTurnMessage.isStuck());
    }

    @Test
    public void gameFinishTest() {
        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                gameInstance.getBoard(),
                null,
                TurnEvents.Actions.MOVE_STANDARD,
                turn.currentPlayer,
                turn.currentWorker
        );
        updateTurnMessage.setGameFinish(true);
        assertTrue(updateTurnMessage.isGameFinish());
        updateTurnMessage.setGameFinish(false);
        assertFalse(updateTurnMessage.isGameFinish());
    }

    @Test
    public void getNextStartupMove() {
        ArrayList<Color.PlayerColor> availableColors = new ArrayList<>(Arrays.asList(Color.PlayerColor.RED, Color.PlayerColor.YELLOW));

        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                TurnEvents.StartupActions.CHOOSE_CARD_REQUEST,
                turn.currentPlayer,
                availableColors
        );
        assertEquals(TurnEvents.StartupActions.CHOOSE_CARD_REQUEST, updateTurnMessage.getNextStartupMove());
    }

    @Test
    public void getNextMove() {
        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                gameInstance.getBoard(),
                null,
                TurnEvents.Actions.MOVE_STANDARD,
                turn.currentPlayer,
                turn.currentWorker
        );
        assertEquals(TurnEvents.Actions.MOVE_STANDARD, updateTurnMessage.getNextMove());
    }

    @Test
    public void getCurrentPlayer() {
        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                gameInstance.getBoard(),
                null,
                TurnEvents.Actions.MOVE_STANDARD,
                turn.currentPlayer,
                turn.currentWorker
        );
        assertEquals(turn.currentPlayer, updateTurnMessage.getCurrentPlayer());
    }

    @Test
    public void getCurrentWorker() {
        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                gameInstance.getBoard(),
                null,
                TurnEvents.Actions.MOVE_STANDARD,
                turn.currentPlayer,
                turn.currentWorker
        );
        assertEquals(turn.currentWorker, updateTurnMessage.getCurrentWorker());
    }

    @Test
    public void availableColorTest() {
        ArrayList<Color.PlayerColor> availableColors = new ArrayList<>(Arrays.asList(Color.PlayerColor.RED, Color.PlayerColor.YELLOW));

        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                TurnEvents.StartupActions.CHOOSE_CARD_REQUEST,
                turn.currentPlayer,
                availableColors
        );
        assertEquals(availableColors, updateTurnMessage.getAvailableColor());
    }

    @Test
    public void availableCardTest() {
        ArrayList<Color.PlayerColor> availableColors = new ArrayList<>(Arrays.asList(Color.PlayerColor.RED, Color.PlayerColor.YELLOW));
        ArrayList<GodsCard> availableCards = new ArrayList<>(Collections.singletonList(gameInstance.getDeck().getCardListCopy().get(0)));

        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                TurnEvents.StartupActions.CHOOSE_CARD_REQUEST,
                turn.currentPlayer,
                availableColors
        );
        updateTurnMessage.setAvailableCards(availableCards);
        assertEquals(availableCards, updateTurnMessage.getAvailableCards());

        //Test isStartupPhase
        assertTrue(updateTurnMessage.isStartupPhase());
    }

    @Test
    public void boardCopyTest() {
        ArrayList<Color.PlayerColor> availableColors = new ArrayList<>(Arrays.asList(Color.PlayerColor.RED, Color.PlayerColor.YELLOW));

        UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(
                TurnEvents.StartupActions.PLACE_WORKER_1,
                turn.currentPlayer,
                availableColors
        );
        updateTurnMessage.setBoardCopy(gameInstance.getBoard());
        final int BOARD_DIMENSION = 5;
        for (int i = 0; i < BOARD_DIMENSION; i++)
            for (int j = 0; j < BOARD_DIMENSION; j++)
                assertEquals(gameInstance.getBoard().getSlot(new Position(i, j)), updateTurnMessage.getBoardCopy().getSlot(new Position(i, j)));
    }
}