package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static it.polimi.ingsw.model.TurnEvents.Actions.ActionProperty.SKIPPABLE;
import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class GameManagerTest {

    Game gameInstance = new Game();
    GameManager gameManager = new GameManager(gameInstance);

    private class ClientTest {
        
    }

    @Test
    public void handleMoveMovementTest() throws ParseException, IllegalAccessException, IOException {
        Turn turn = gameInstance.getTurn();

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

//        RemoteView remoteView = Mockito.mock(RemoteView.class);
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
        buildingSlot.setBuilding(new Building(BuildingLevel.LEVEL1));


        //TEST_CHOSE_WORKER
        PlayerMove playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.CHOSE_WORKER,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(turn.getCurrentWorker(), worker1Player1);


        //TEST MOVE_STANDARD
        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(targetSlot, worker1Player1.getWorkerSlot());

        //TEST MOVE_NOT_INITIAL_POSITION
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_NOT_INITIAL_POSITION,
                startingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        assertEquals("", gameManager.getErrorMessage());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.notInitialPositionMessage, gameManager.getErrorMessage());

        //TEST MOVE_OPPONENT_SLOT_FLIP
        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_OPPONENT_SLOT_FLIP,
                opponentSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(opponentSlot, worker1Player1.getWorkerSlot());
        assertEquals(startingSlot, worker1Player2.getWorkerSlot());

        //TEST MOVE_OPPONENT_SLOT_PUSH
        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_OPPONENT_SLOT_PUSH,
                opponentSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(gameInstance.getBoard().getSlot(new Position(2, 0)), worker1Player2.getWorkerSlot());
        assertEquals(opponentSlot, worker1Player1.getWorkerSlot());

        //TEST MOVE_DISABLE_OPPONENT_UP
        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_DISABLE_OPPONENT_UP,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertFalse(turn.canPlayerMoveUp(player2));

        //TEST BUILD_BEFORE
        worker1Player1.move(targetSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.BUILD_BEFORE,
                buildBeforeSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));
        assertEquals(expectedBuildings, buildBeforeSlot.getBuildingsStatus());

        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.tooHighMoveMessage, gameManager.getErrorMessage());
        assertEquals(targetSlot, worker1Player1.getWorkerSlot());


        gameInstance.removePlayerByName(player1.getNickname());
        gameInstance.removePlayerByName(player2.getNickname());
    }

    @Test
    public void handleMoveBuildingTest() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();
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

        turn.setUpGameTurn();

        Worker workerPlayer1 = player1.getWorkers().get(0);
        Worker workerPlayer2 = player2.getWorkers().get(0);
        Slot startingSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        Slot opponentSlot = gameInstance.getBoard().getSlot(new Position(1, 0));
        Slot buildingSlot = gameInstance.getBoard().getSlot(new Position(1, 1));
        buildingSlot.setBuilding(new Building(BuildingLevel.LEVEL1));


        //TEST BUILD_STANDARD
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);
        PlayerMove playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));
        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());

        //TEST BUILD_NOT_SAME_PLACE
        targetSlot.destroyTopBuilding();
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_NOT_SAME_PLACE,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.buildNotSamePlaceMessage, gameManager.getErrorMessage());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_NOT_SAME_PLACE,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, null, null));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        //TEST BUILD_DOME_ANY_LEVEL
        targetSlot.destroyTopBuilding();
        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_DOME_ANY_LEVEL,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, BuildingLevel.DOME));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_DOME_ANY_LEVEL,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.domeOccupiedCellMessage, gameManager.getErrorMessage());

        //TEST BUILD_SAME_PLACE_NOT_DOME
        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.mustBuildSamePlaceMessage, gameManager.getErrorMessage());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, BuildingLevel.LEVEL3, null));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(Message.cantBuildADomeMessage, gameManager.getErrorMessage());
    }

    @Test
    public void handleMoveCommandTest() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();
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
        buildingSlot.setBuilding(new Building(BuildingLevel.LEVEL1));


        PlayerMove playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.CHOSE_WORKER,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);


        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);

        //TEST UNDO
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.UNDO,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(startingSlot.getSlotPosition(), worker1Player1.getWorkerSlot().getSlotPosition());

        while (!turn.getCurrentPlayerTurnSequence().getMoveSequence().get(turn.getCurrentMoveIndex() - 1).hasProperty(SKIPPABLE)) {
            turn.updateTurn();
        }

        //TEST SKIP
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.SKIP,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
        assertEquals(startingSlot.getSlotPosition(), worker1Player1.getWorkerSlot().getSlotPosition());


        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.UNDO,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteView);
        gameManager.handlePlayerMove(playerMove);
    }

    @Test
    public void handleMoveErrorTest() throws ParseException, IllegalAccessException, IOException {
        Turn turn = gameInstance.getTurn();

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

//        RemoteView remoteView = Mockito.mock(RemoteView.class);
        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteViewMock = Mockito.mock(RemoteView.class);
        String errorMessage = "";

//        when(remoteView.errorMessage(anyString())).thenAnswer(i -> i.getArguments()[0]);
//        doAnswer(returnsFirstArg()).when(remoteViewMock).errorMessage(anyString());

        turn.addUpdateTurnMessageObserver(remoteViewMock.getUpdateTurnMessageReceiver());

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
        buildingSlot.setBuilding(new Building(BuildingLevel.LEVEL1));

        //TEST CANNOT_UNDO
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.cannotUndo, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        PlayerMove playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.UNDO,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);


        //TEST CANNOT_SKIP
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.cannotSkipThisMove, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.SKIP,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);


        //TEST_CHOSE_WORKER_NO_WORKER_IN_SLOT
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.noWorkerInSlot, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.CHOSE_WORKER,
                new Position(3, 3),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);
        gameManager.handlePlayerMove(playerMove);

        //TEST_CHOSE_WORKER_CHOSE_NOT_YOUR_WORKER
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.choseNotYourWorker, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.CHOSE_WORKER,
                worker1Player2.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);
        gameManager.handlePlayerMove(playerMove);


        //TEST_CHOSE_WORKER
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.CHOSE_WORKER,
                worker1Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname()
        );
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);
        gameManager.handlePlayerMove(playerMove);


        //TEST MOVE_STANDARD_MOVE_NOT_ALLOWED
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.moveNotAllowed, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player1.getIdWorker(),
                Actions.MOVE_STANDARD,
                worker2Player1.getWorkerPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);
        gameManager.handlePlayerMove(playerMove);


        //TEST WRONG_TURN
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            assertEquals(Message.wrongTurnMessage, arg0);
            return null;
        }).when(remoteViewMock).errorMessage(any(String.class));

        worker1Player1.move(startingSlot);
        worker1Player2.move(opponentSlot);
        playerMove = new PlayerMove(worker1Player2.getIdWorker(),
                Actions.MOVE_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        playerMove.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMove.setRemoteView(remoteViewMock);
        gameManager.handlePlayerMove(playerMove);
    }

//    @Test
//    public void performMoveTest() {
//        Player player = new Player("foo");
//        Worker worker = player.getWorkers().get(0);
//        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
//        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
//        worker.move(workerSlot);
//
//        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
//                Actions.MOVE_STANDARD,
//                targetSlot.getSlotPosition(),
//                gameInstance.getTurn().getCurrentPlayer().getNickname());
//
//        gameManager.performMove(playerMove);
//
//        assertEquals(targetSlot, worker.getWorkerSlot());
//    }
//
//    @Test
//    public void performBuildingTest() {
//        Player player = new Player("foo");
//        Worker worker = player.getWorkers().get(0);
//        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
//        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
//        worker.move(workerSlot);
//
//        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
//                Actions.BUILD_STANDARD,
//                targetSlot.getSlotPosition(),
//                gameInstance.getTurn().getCurrentPlayer().getNickname());
//
//        gameManager.performBuilding(playerMove);
//        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));
//
//        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
//
//        gameManager.performBuilding(playerMove);
//        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, null, null));
//
//        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
//    }
//
//    @Test
//    public void performBuildingDomeTest() {
//        Player player = new Player("foo");
//        Worker worker = player.getWorkers().get(0);
//        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
//        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
//        worker.move(workerSlot);
//
//        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
//                Actions.BUILD_STANDARD,
//                targetSlot.getSlotPosition(),
//                gameInstance.getTurn().getCurrentPlayer().getNickname());
//
//        gameManager.performBuildingDome(playerMove);
//        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(null, null, null, BuildingLevel.DOME));
//
//        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
//    }
}