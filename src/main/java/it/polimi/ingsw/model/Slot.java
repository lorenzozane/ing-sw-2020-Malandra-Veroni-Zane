package it.polimi.ingsw.model;

public class Slot {

    private Building[] buildingsStatus = new Building[4];
    private Player playerInSlot;
    private Position slotPosition;

    public Position getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(Position slotPosition) {
        this.slotPosition = slotPosition;
    }

    public void setBuilding(Building building) {
        try {
            int level = building.getLevel();
            buildingsStatus[level] = building;
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
