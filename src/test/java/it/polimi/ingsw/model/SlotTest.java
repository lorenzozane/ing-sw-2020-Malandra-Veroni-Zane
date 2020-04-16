package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

public class SlotTest {

    @Test
    public void getBuildingsStatus(){
        Building newBuilding = new Building(Building.BuildingLevel.LEVEL1);
        Slot slot = new Slot(new Position(0, 0));
        slot.setBuilding(newBuilding);

        ArrayList<Building.BuildingLevel> buildingsStatus = slot.getBuildingsStatus();
    }

}