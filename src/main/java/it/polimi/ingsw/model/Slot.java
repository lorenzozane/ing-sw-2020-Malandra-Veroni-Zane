package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.Building.BuildingProperty.*;

public class Slot {

    private Building[] buildingsStatus = new Building[4];
    private Worker workerInSlot;
    private Position slotPosition;
    private int constructionHeight = 0;

    public Slot(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    public Worker getWorkerInSlot() {
        return this.workerInSlot;
    }

    public int getConstructionHeight() {
        return constructionHeight;
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

    public void setBuilding(Building building) {
        try {
            if (buildingsStatus[constructionHeight - 1].getLevel().hasProperty(CAN_BUILD_ON_IT)) {
                int level = building.getLevelAsInt();
                buildingsStatus[level - 1] = building;
                if (level > constructionHeight)
                    constructionHeight = level;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<String> getBuildingStatus() {   //Crea una copia del buildings status sotto forma di array di stringhe
        String[] buildingsStatusCopy = new String[4];
        for (int i = 0; i < buildingsStatusCopy.length; i++)
            buildingsStatusCopy[i] = buildingsStatus[i] != null ? buildingsStatus[i].getLevel().toString() : "NULL";

        return Arrays.asList(buildingsStatusCopy);
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

    public static int calculateHeightDifference(Slot startingPosition, Slot targetPosition) {
        return targetPosition.getConstructionHeight() - startingPosition.getConstructionHeight();
    }
}
