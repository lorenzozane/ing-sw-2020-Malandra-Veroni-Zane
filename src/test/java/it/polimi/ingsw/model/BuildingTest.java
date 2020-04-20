package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {

    @Test //completed
    public void hasPropertyTest() {
        //Test per LEVEL1, LEVEL2, LEVEL3 equivalenti
        Building newBuilding = new Building(BuildingLevel.LEVEL1);
        assertTrue(newBuilding.getLevel().hasProperty(Building.BuildingProperty.CAN_BUILD_ON_IT));
        assertTrue(newBuilding.getLevel().hasProperty(Building.BuildingProperty.IS_SCALABLE));
        newBuilding = new Building(BuildingLevel.DOME);
        assertFalse(newBuilding.getLevel().hasProperty(Building.BuildingProperty.CAN_BUILD_ON_IT));
        assertFalse(newBuilding.getLevel().hasProperty(Building.BuildingProperty.IS_SCALABLE));
    }

    @Test //completed
    public void getLevelAsIntTest() {
        Building newBuilding = new Building(BuildingLevel.LEVEL1);
        assertEquals(newBuilding.getLevelAsInt(), 1);
    }

    @Test //completed
    public void getNextLevelTest() {
        Building newBuilding = new Building(BuildingLevel.LEVEL1);
        BuildingLevel nextLevel = BuildingLevel.getNextLevel(newBuilding.getLevel());
        Building secondBuilding = new Building(nextLevel);

        assertEquals(secondBuilding.getLevel(), BuildingLevel.LEVEL2);

        Slot slot = new Slot(new Position(1, 1));
        BuildingLevel slotTopLevel = slot.getConstructionTopLevel();
        nextLevel = BuildingLevel.getNextLevel(slotTopLevel);
        secondBuilding = new Building(nextLevel);

        assertEquals(secondBuilding.getLevel(), BuildingLevel.LEVEL1);

        slot.setBuilding(new Building(BuildingLevel.DOME));
        slotTopLevel = slot.getConstructionTopLevel();
        nextLevel = BuildingLevel.getNextLevel(slotTopLevel);

        assertNull(nextLevel);
    }
}