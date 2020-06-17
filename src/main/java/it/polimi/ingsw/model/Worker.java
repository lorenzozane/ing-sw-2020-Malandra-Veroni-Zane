package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.Color.PlayerColor;

import java.io.Serializable;

public class Worker implements Serializable {

    private Player playerOwner;
    private String idWorker;
    private PlayerColor color;
    private Slot workerSlot;

    public Worker(Player playerOwner, int idWorkerInt){
        this.playerOwner = playerOwner;
        this.idWorker = playerOwner.getNickname() + "_" + idWorkerInt;
    }


    public Player getPlayerOwner(){
        return playerOwner;
    }

    public String getIdWorker() {
        return idWorker;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Slot getWorkerSlot() {
        return workerSlot;
    }

    public Position getWorkerPosition() {
        return workerSlot.getSlotPosition();
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    /**
     * Set the worker in the slot specified
     *
     * @param workerSlot The slot target of the movement
     */
    public void setWorkerSlot(Slot workerSlot) {
        if (this.workerSlot != null)
            this.workerSlot.setWorkerInSlot(null);
        this.workerSlot = workerSlot;
        this.workerSlot.setWorkerInSlot(this);
    }

    /**
     * Set new Worker's position
     *
     * @param moveHere New position
     */
    public void move(Slot moveHere){
        setWorkerSlot(moveHere);
    }

    /**
     * Creation of a new building
     *
     * @param buildHere New construction position
     */
    public void build(Slot buildHere){
        BuildingLevel slotTopBuilding = buildHere.getConstructionTopLevel();
        Building newBuilding = new Building(BuildingLevel.getNextLevel(slotTopBuilding));
        buildHere.setBuilding(newBuilding);
    }

    /**
     * Creation of a new building, forcibly a dome
     *
     * @param buildHere New construction position
     * @param buildDome Boolean describing the choice to build forcibly a dome
     */
    public void forcedDomeBuild(Slot buildHere, boolean buildDome){
        if (buildDome){
            Building newBuilding = new Building(BuildingLevel.DOME);
            buildHere.setBuilding(newBuilding);
        } else
            build(buildHere);
    }
}
