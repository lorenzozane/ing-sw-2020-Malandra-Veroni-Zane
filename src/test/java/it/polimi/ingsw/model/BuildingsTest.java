package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingsTest {

    @Test
    public void analyzeHasProperty() {
        //Test per LEVEL1, LEVEL2, LEVEL3 equivalenti
        Buildings newBuilding = new Buildings(Buildings.BuildingsLevel.LEVEL1);
        assertTrue(newBuilding.level.hasProperty(Buildings.BuildingsProperty.CAN_BUILD_ON_IT));
        assertTrue(newBuilding.level.hasProperty(Buildings.BuildingsProperty.IS_SCALABLE));
        newBuilding = new Buildings(Buildings.BuildingsLevel.DOME);
        assertTrue(!newBuilding.level.hasProperty(Buildings.BuildingsProperty.CAN_BUILD_ON_IT));
        assertTrue(!newBuilding.level.hasProperty(Buildings.BuildingsProperty.IS_SCALABLE));
    }
}