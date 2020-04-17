package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void printGameBoard() {
        Board board = new Board();
        Position posizione = new Position(4,3);
        Slot slot1 = new Slot(posizione);
        Building b1 = new Building(Building.BuildingLevel.LEVEL1);
        Building b2= new Building(Building.BuildingLevel.LEVEL2);
        Building b3= new Building(Building.BuildingLevel.LEVEL3);
        Building b4= new Building(Building.BuildingLevel.DOME);

        slot1.setBuilding(b1);
        slot1.setBuilding(b2);
        slot1.setBuilding(b3);
        slot1.setBuilding(b4);
        board.printGameBoard();
        board.updateBuildingOnBoard(slot1);
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