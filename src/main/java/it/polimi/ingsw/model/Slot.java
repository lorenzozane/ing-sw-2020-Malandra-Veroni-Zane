package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;

public class Slot {

    private BuildingLevel buildingsStatus;
    private Position slotPosition;

    public Position getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    public BuildingLevel getBuildingsStatus() {
        return buildingsStatus;
    }

    public void setBuildingsStatus(BuildingLevel buildingsStatus) {
        this.buildingsStatus = buildingsStatus;
    }
}
