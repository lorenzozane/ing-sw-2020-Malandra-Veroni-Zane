package it.polimi.ingsw.model;

import java.util.ArrayList;

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

    public ArrayList<Building.BuildingLevel> getBuildingsStatus() {   //Crea una copia del buildings status sotto forma di array di stringhe
        ArrayList<Building.BuildingLevel> buildingLevelsCopy = new ArrayList<>(buildingsStatus.length);
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

    public static int calculateHeightDifference(Slot startingPosition, Slot targetPosition) {
        return targetPosition.getConstructionHeight() - startingPosition.getConstructionHeight();
    }
}
