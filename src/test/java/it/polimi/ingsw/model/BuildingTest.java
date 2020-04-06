package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {

    @Test
    public void analyzeHasProperty() {
        //Test per LEVEL1, LEVEL2, LEVEL3 equivalenti
        Building newBuilding = new Building(Building.BuildingLevel.LEVEL1);
        assertTrue(newBuilding.getLevel().hasProperty(Building.BuildingProperty.CAN_BUILD_ON_IT));
        assertTrue(newBuilding.getLevel().hasProperty(Building.BuildingProperty.IS_SCALABLE));
        newBuilding = new Building(Building.BuildingLevel.DOME);
        assertTrue(!newBuilding.getLevel().hasProperty(Building.BuildingProperty.CAN_BUILD_ON_IT));
        assertTrue(!newBuilding.getLevel().hasProperty(Building.BuildingProperty.IS_SCALABLE));
    }
}