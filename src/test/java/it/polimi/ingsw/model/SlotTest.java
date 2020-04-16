package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SlotTest {

    @Test
    public void getBuildingsStatus(){
        Building newBuilding = new Building(Building.BuildingLevel.LEVEL1);
        Slot slot = new Slot(new Position(0, 0));
        slot.setBuilding(newBuilding);

        List<String> buildingsStatus = slot.getBuildingStatus();
    }

}