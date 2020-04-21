package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void initializeBoardTest() {
        Board board = new Board();

        final int BOARD_DIMENSION = 5;

        for (int i = 0; i < BOARD_DIMENSION; i++)
            for (int j = 0; j < BOARD_DIMENSION; j++)
                assertEquals(board.getSlot(new Position(i, j)).getSlotPosition(), new Position(i, j));
    }

    @Test //completed
    public void getAdjacentSlotsTest() {
        Board board = new Board();
        Slot slot = board.getSlot(new Position(1, 1));
        ArrayList<Slot> adjacentSlots = board.getAdjacentSlots(slot);

        ArrayList<Position> expectedPositions = new ArrayList<>(Arrays.asList(
                new Position(0, 0), new Position(0, 1),
                new Position(0, 2), new Position(1, 0),
                new Position(1, 2), new Position(2, 0),
                new Position(2, 1), new Position(2, 2)
        ));

        for (Position position : expectedPositions)
            assertTrue(adjacentSlots.contains(board.getSlot(position)));
    }

    @Test
    public void printGameBoard() {
        Board board = new Board();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        board.printGameBoard();

        Position posizione1 = new Position(4,3);
        Slot slot1 = new Slot(posizione1);
        Building b1 = new Building(Building.BuildingLevel.LEVEL1);
        Building b2= new Building(Building.BuildingLevel.LEVEL2);
        Building b3= new Building(Building.BuildingLevel.LEVEL3);
        Building b4= new Building(Building.BuildingLevel.DOME);

        slot1.setBuilding(b1);
        slot1.setBuilding(b2);
        slot1.setBuilding(b3);
        slot1.setBuilding(b4);

        Position posizione2 = new Position(0,0);
        Slot slot2 = new Slot(posizione2);
        Building bb1 = new Building(Building.BuildingLevel.LEVEL1);
        Building bb2= new Building(Building.BuildingLevel.LEVEL2);
        Building bb4= new Building(Building.BuildingLevel.DOME);

        slot2.setBuilding(bb1);
        slot2.setBuilding(bb2);

        board.updateBuildingOnBoard(slot1);
        board.updateBuildingOnBoard(slot2);

        Player p1 = new Player("nickname1");
        Worker worker1=new Worker(p1, 0);
        Color color=Color.ANSI_YELLOW;
        worker1.setColor(color);
        worker1.setWorkerSlot(slot2);


        board.putWorkerOnBoard(worker1.getWorkerSlot());


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        board.printGameBoard();

        board.removeWorkerOnBoard(worker1.getWorkerSlot());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        board.printGameBoard();

    }
}