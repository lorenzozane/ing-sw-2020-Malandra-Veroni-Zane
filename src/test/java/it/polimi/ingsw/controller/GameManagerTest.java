package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class GameManagerTest {

    Game gameInstance = new Game();
    GameManager gameManager = new GameManager(gameInstance);

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

        turn.setUpGameTurn();

        Worker workerPlayer1 = player1.getWorkers().get(0);
        Worker workerPlayer2 = player2.getWorkers().get(0);
        Slot startingSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        Slot opponentSlot = gameInstance.getBoard().getSlot(new Position(1, 0));
        Slot buildingSlot = gameInstance.getBoard().getSlot(new Position(1, 1));
        Slot buildBeforeSlot = gameInstance.getBoard().getSlot(new Position(0, 2));
        buildingSlot.setBuilding(new Building(BuildingLevel.LEVEL1));


        //TEST MOVE_STANDARD
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);
        PlayerMove playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_STANDARD,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(targetSlot, workerPlayer1.getWorkerSlot());

        //TEST MOVE_NOT_INITIAL_POSITION
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_NOT_INITIAL_POSITION,
                startingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        assertEquals("", gameManager.getErrorMessage());
        gameManager.handleMove(playerMove);
        assertEquals(Message.notInitialPositionMessage, gameManager.getErrorMessage());

        //TEST MOVE_OPPONENT_SLOT_FLIP
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_OPPONENT_SLOT_FLIP,
                opponentSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(opponentSlot, workerPlayer1.getWorkerSlot());
        assertEquals(startingSlot, workerPlayer2.getWorkerSlot());

        //TODO: Sistemare errore turno
        //TEST MOVE_OPPONENT_SLOT_PUSH
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_OPPONENT_SLOT_PUSH,
                opponentSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(gameInstance.getBoard().getSlot(new Position(2, 0)), workerPlayer2.getWorkerSlot());
        assertEquals(opponentSlot, workerPlayer1.getWorkerSlot());

        //TEST MOVE_DISABLE_OPPONENT_UP
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_DISABLE_OPPONENT_UP,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertFalse(turn.canPlayerMoveUp(player2));

        //TEST BUILD_BEFORE
        workerPlayer1.move(targetSlot);
        workerPlayer2.move(opponentSlot);
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_BEFORE,
                buildBeforeSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);

        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));
        assertEquals(expectedBuildings, buildBeforeSlot.getBuildingsStatus());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.MOVE_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(Message.tooHighMoveMessage, gameManager.getErrorMessage());
        assertEquals(targetSlot, workerPlayer1.getWorkerSlot());


        //TODO: Continuare con altri casi di TEST

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

        RemoteView remoteView = Mockito.mock(RemoteView.class);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

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
        gameManager.handleMove(playerMove);

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
        gameManager.handleMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_NOT_SAME_PLACE,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(Message.buildNotSamePlaceMessage, gameManager.getErrorMessage());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_NOT_SAME_PLACE,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, null, null));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        //TEST BUILD_DOME_ANY_LEVEL
        targetSlot.destroyTopBuilding();
        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_DOME_ANY_LEVEL,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, BuildingLevel.DOME));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_DOME_ANY_LEVEL,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(Message.domeOccupiedCellMessage, gameManager.getErrorMessage());

        //TEST BUILD_SAME_PLACE_NOT_DOME
        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                targetSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(Message.mustBuildSamePlaceMessage, gameManager.getErrorMessage());

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, BuildingLevel.LEVEL3, null));
        assertEquals(expectedBuildings, buildingSlot.getBuildingsStatus());

        buildingSlot.destroyTopBuilding();
        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_STANDARD,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);

        playerMove = new PlayerMove(workerPlayer1.getIdWorker(),
                Actions.BUILD_SAME_PLACE_NOT_DOME,
                buildingSlot.getSlotPosition(),
                turn.getCurrentPlayer().getNickname());
        gameManager.handleMove(playerMove);
        assertEquals(Message.cantBuildADomeMessage, gameManager.getErrorMessage());
    }

    @Test
    public void initialCheckMovableWorkerTest() {
    }

    @Test //completed
    public void performMoveTest() {
        Player player = new Player("foo");
        Worker worker = player.getWorkers().get(0);
        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        worker.move(workerSlot);

        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
                Actions.MOVE_STANDARD,
                targetSlot.getSlotPosition(),
                gameInstance.getTurn().getCurrentPlayer().getNickname());

        gameManager.performMove(playerMove);

        assertEquals(targetSlot, worker.getWorkerSlot());
    }

    @Test //completed
    public void performBuildingTest() {
        Player player = new Player("foo");
        Worker worker = player.getWorkers().get(0);
        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        worker.move(workerSlot);

        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
                Actions.BUILD_STANDARD,
                targetSlot.getSlotPosition(),
                gameInstance.getTurn().getCurrentPlayer().getNickname());

        gameManager.performBuilding(playerMove);
        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());

        gameManager.performBuilding(playerMove);
        expectedBuildings = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, null, null));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
    }

    @Test //completed
    public void performBuildingDomeTest() {
        Player player = new Player("foo");
        Worker worker = player.getWorkers().get(0);
        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        worker.move(workerSlot);

        PlayerMove playerMove = new PlayerMove(worker.getIdWorker(),
                Actions.BUILD_STANDARD,
                targetSlot.getSlotPosition(),
                gameInstance.getTurn().getCurrentPlayer().getNickname());

        gameManager.performBuildingDome(playerMove);
        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<>(Arrays.asList(null, null, null, BuildingLevel.DOME));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
    }

    @Test
    public void checkWinConditionsTest() {
    }

    @Test
    public void checkTurnIsOverTest() {
    }
}