package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
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
    public void handleMoveTest() throws ParseException, IllegalAccessException, IOException {
        Turn turn = gameInstance.getTurn();
        Player player1 = new Player("foo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("boo");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        RemoteView remoteView = new RemoteView(player1, "boo", new SocketConnection(new Socket(), new Server()));

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");
        player1.setPlayerCard(deck.pickUpCard("apollo"));
        player2.setPlayerCard(deck.pickUpCard("artemis"));

        turn.setUpTurn();

        Worker workerPlayer1 = player1.getWorkers().get(0);
        Worker workerPlayer2 = player2.getWorkers().get(0);
        Slot startingSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        Slot opponentSlot = gameInstance.getBoard().getSlot(new Position(1, 0));
        workerPlayer1.move(startingSlot);
        workerPlayer2.move(opponentSlot);

        PlayerMove playerMove = new PlayerMove(workerPlayer1,
                Actions.MOVE_STANDARD,
                targetSlot,
                gameInstance.getTurn(), remoteView);
        gameManager.handleMove(playerMove);
        assertEquals(targetSlot, workerPlayer1.getWorkerSlot());

        workerPlayer1.move(startingSlot);
        playerMove = new PlayerMove(workerPlayer1,
                Actions.MOVE_OPPONENT_SLOT_FLIP,
                opponentSlot,
                gameInstance.getTurn(), remoteView);
        gameManager.handleMove(playerMove);
        assertEquals(opponentSlot, workerPlayer1.getWorkerSlot());
        assertEquals(startingSlot, workerPlayer2.getWorkerSlot());





        //TODO: Continuare con altri casi di TEST

        //TODO: Riattivare quando si corregge il bug in removePlayerByName
//        gameInstance.removePlayerByName(player.getNickname());
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

        PlayerMove playerMove = new PlayerMove(worker,
                Actions.MOVE_STANDARD,
                targetSlot,
                gameInstance.getTurn(), null);

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

        PlayerMove playerMove = new PlayerMove(worker,
                Actions.BUILD_STANDARD,
                targetSlot,
                gameInstance.getTurn(), null);

        gameManager.performBuilding(playerMove);
        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<BuildingLevel>(Arrays.asList(BuildingLevel.LEVEL1, null, null, null));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());

        gameManager.performBuilding(playerMove);
        expectedBuildings = new ArrayList<BuildingLevel>(Arrays.asList(BuildingLevel.LEVEL1, BuildingLevel.LEVEL2, null, null));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
    }

    @Test //completed
    public void performBuildingDomeTest() {
        Player player = new Player("foo");
        Worker worker = player.getWorkers().get(0);
        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        worker.move(workerSlot);

        PlayerMove playerMove = new PlayerMove(worker,
                Actions.BUILD_STANDARD,
                targetSlot,
                gameInstance.getTurn(), null);

        gameManager.performBuildingDome(playerMove);
        ArrayList<BuildingLevel> expectedBuildings = new ArrayList<BuildingLevel>(Arrays.asList(null, null, null, BuildingLevel.DOME));

        assertEquals(expectedBuildings, targetSlot.getBuildingsStatus());
    }

    @Test
    public void checkWinConditionsTest() {
    }

    @Test
    public void checkTurnIsOverTest() {
    }
}