package it.polimi.ingsw.model;

public class Slot {

    private Building[] buildingsStatus = new Building[4];
    private Worker workerInSlot;
    private Position slotPosition;

    public Slot(Position slotPosition){
        this.slotPosition = slotPosition;
    }

    public Worker getWorkerInSlot(){
        return this.workerInSlot;
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
            int level = building.getLevelAsInt();
            buildingsStatus[level] = building;
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public Building[] getBuildingStatus(){
        return buildingsStatus;
    }

}
