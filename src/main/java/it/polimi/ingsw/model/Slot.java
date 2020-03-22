package it.polimi.ingsw.model;

public class Slot {

    private int buildingsStatus;
    private Position slotPosition;

    public Position getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    public int getBuildingsStatus() {
        return buildingsStatus;
    }

    public void setBuildingsStatus(int buildingsStatus) {
        this.buildingsStatus = buildingsStatus;
    }
}
