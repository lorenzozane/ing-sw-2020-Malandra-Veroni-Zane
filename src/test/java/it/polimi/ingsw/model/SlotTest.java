package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

public class SlotTest {

    @Test
    public void getBuildingsStatus(){
        Building newBuilding = new Building(Building.BuildingLevel.LEVEL1);
        Building newBuildingDome = new Building(Building.BuildingLevel.DOME);
        Slot slot = new Slot(new Position(0, 0));
        slot.setBuilding(newBuilding);
        slot.setBuilding(newBuildingDome);

        ArrayList<Building.BuildingLevel> buildingsStatus = slot.getBuildingsStatus();
    }

}