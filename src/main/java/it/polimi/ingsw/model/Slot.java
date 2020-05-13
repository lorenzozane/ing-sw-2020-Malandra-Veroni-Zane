package it.polimi.ingsw.model;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Building.*;
import static it.polimi.ingsw.model.Building.BuildingProperty.*;

public class Slot {

    private final Building[] buildingsStatus = new Building[4];
    private Worker workerInSlot;
    private Position slotPosition;
    private int constructionHeight = 0;

    public Slot(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    public Worker getWorkerInSlot() {
        return this.workerInSlot;
    }

    /**
     * Returns as an integer the height value of the buildings in the slot
     *
     * @return the integer value of the highest building
     */
    public int getConstructionHeight() {
        return constructionHeight;
    }

    /**
     * Returns the BuildingLevel of the highest building in slot
     *
     * @return the BuildingLevel value of the highest construction in slot
     */
    public BuildingLevel getConstructionTopLevel() {
        BuildingLevel topLevel = null;
        for (Building building : buildingsStatus)
            if (building != null)
                topLevel = building.getLevel();

        return topLevel;
    }

    public void setWorkerInSlot(Worker workerInSlot) {
        this.workerInSlot = workerInSlot;
    }

    public Position getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    /**
     * Allows the construction of a new building
     *
     * @param building Building element to be added above the highest construction
     */
    public void setBuilding(Building building) {
        try {
            if (constructionHeight == 0 || buildingsStatus[constructionHeight - 1].getLevel().hasProperty(CAN_BUILD_ON_IT)) {
                int level = building.getLevelAsInt();
                buildingsStatus[level - 1] = building;
                if (level > constructionHeight)
                    constructionHeight = level;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Allows to destroy the top building currently in the slot
     */
    public void destroyTopBuilding() {
        if (this.getConstructionTopLevel() == null)
            return;
        else {
            int index = this.getConstructionTopLevel().getLevelAsInt() - 1;
            buildingsStatus[index] = null;
        }

        if (this.getConstructionTopLevel() == null)
            constructionHeight = 0;
        else
            constructionHeight = this.getConstructionTopLevel().getLevelAsInt();
    }

    /**
     * Returns a copy of the buildingStatus as an ArrayList of BuildingLevel
     *
     * @return ArrayList of BuildingLevel containing the BuildingLevel value at the construction index, or null
     */
    public ArrayList<BuildingLevel> getBuildingsStatus() {
        ArrayList<BuildingLevel> buildingLevelsCopy = new ArrayList<>(buildingsStatus.length);
        for (int i = 0; i < buildingsStatus.length; i++)
            buildingLevelsCopy.add(buildingsStatus[i] != null ? buildingsStatus[i].getLevel() : null);

        return buildingLevelsCopy;
    }

    /**
     * Calculate the distance (minimum number of moves required to reach the target slot) between two slots
     *
     * @param startingPosition Starting slot
     * @param targetPosition   Target slot
     * @return Return the distance between two slots (as the minimum number of moves required to reach the target slot)
     */
    public static int calculateDistance(Slot startingPosition, Slot targetPosition) {
        int distanceX = Math.abs(startingPosition.slotPosition.getCoordinateX() - targetPosition.getSlotPosition().getCoordinateX());
        int distanceY = Math.abs(startingPosition.slotPosition.getCoordinateY() - targetPosition.getSlotPosition().getCoordinateY());

        return Math.max(distanceX, distanceY);
    }

    /**
     * Calculate the height difference between two slots
     *
     * @param startingPosition Starting slot
     * @param targetPosition   Target slot
     * @return Return the height difference (not in absolute value) between tho slots
     */
    public static int calculateHeightDifference(Slot startingPosition, Slot targetPosition) {
        return targetPosition.getConstructionHeight() - startingPosition.getConstructionHeight();
    }
}
