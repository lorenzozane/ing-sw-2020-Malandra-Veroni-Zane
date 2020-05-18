package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CliTest {

    @Test
    public void printGameBoard() {

        Cli cli = new Cli();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        cli.printGameBoard();



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

        cli.updateBuildingOnBoard(slot1);
        cli.updateBuildingOnBoard(slot2);

        Player p1 = new Player("nickname1");
        Worker worker1=new Worker(p1, 0);
        Color.PlayerColor color = Color.PlayerColor.YELLOW;
        worker1.setColor(color);
        worker1.setWorkerSlot(slot2);


        cli.putWorkerOnBoard(worker1.getWorkerSlot());


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        cli.printGameBoard();

        cli.removeWorkerOnBoard(worker1.getWorkerSlot());

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        cli.printGameBoard();
    }
}