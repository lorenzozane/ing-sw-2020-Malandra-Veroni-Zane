package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.Building.BuildingLevel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SlotTest {

    @Test //completed
    public void setBuildingTest() {
        Slot slot = new Slot(new Position(1, 1));
        Building building = new Building(BuildingLevel.LEVEL1);

        assertNull(slot.getBuildingsStatus().get(0));

        slot.setBuilding(building);

        assertEquals(slot.getBuildingsStatus().get(0), building.getLevel());
    }

    @Test //completed
    public void getBuildingsStatus() {
        Slot slot = new Slot(new Position(0, 0));
        Building newBuilding = new Building(BuildingLevel.LEVEL1);
        Building newBuildingDome = new Building(BuildingLevel.DOME);

        ArrayList<BuildingLevel> expectedBuildingsStatus = new ArrayList<>(Arrays.asList(null, null, null, null));
        ArrayList<BuildingLevel> buildingsStatus = slot.getBuildingsStatus();

        assertEquals(buildingsStatus, expectedBuildingsStatus);

        slot.setBuilding(newBuilding);
        slot.setBuilding(newBuildingDome);

        buildingsStatus = slot.getBuildingsStatus();
        expectedBuildingsStatus = new ArrayList<>(Arrays.asList(BuildingLevel.LEVEL1, null, null, BuildingLevel.DOME));

        assertEquals(buildingsStatus, expectedBuildingsStatus);
    }

    @Test //completed
    public void getConstructionTopLevelTest() {
        Slot slot = new Slot(new Position(1, 1));
        BuildingLevel buildingLevel = slot.getConstructionTopLevel();

        assertNull(buildingLevel);

        slot.setBuilding(new Building(BuildingLevel.LEVEL1));
        buildingLevel = slot.getConstructionTopLevel();

        assertEquals(buildingLevel, BuildingLevel.LEVEL1);
    }

    @Test //completed
    public void calculateDistanceTest() {
        Slot slot1 = new Slot(new Position(1, 1));
        Slot slot2 = new Slot(new Position(3, 3));
        Slot slot3 = new Slot(new Position(1, 2));

        assertEquals(Slot.calculateDistance(slot1, slot2), 2);
        assertEquals(Slot.calculateDistance(slot1, slot3), 1);
    }

    @Test //completed
    public void calculateHeightDifferenceTest() {
        Slot slot1 = new Slot(new Position(1, 1));
        Slot slot2 = new Slot(new Position(1, 2));

        assertEquals(Slot.calculateHeightDifference(slot1, slot2), 0);

        slot2.setBuilding(new Building(BuildingLevel.LEVEL1));

        assertEquals(Slot.calculateHeightDifference(slot1, slot2), 1);
    }
}