package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

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
    public void handleMoveTest() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();
        Player player = new Player("foo");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player.setBirthday(date);
        gameInstance.setPlayerNumber(1);
        gameInstance.addPlayer(player);

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo");
        player.setPlayerCard(deck.pickUpCard("apollo"));

        turn.setUpTurn();

        Worker worker = player.getWorkers().get(0);
        Slot workerSlot = gameInstance.getBoard().getSlot(new Position(0, 0));
        Slot targetSlot = gameInstance.getBoard().getSlot(new Position(0, 1));
        worker.move(workerSlot);

        PlayerMove playerMove = new PlayerMove(worker,
                Actions.MOVE_STANDARD,
                targetSlot,
                gameInstance.getTurn(), null);

        gameManager.handleMove(playerMove);

        assertEquals(targetSlot, worker.getWorkerSlot());

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