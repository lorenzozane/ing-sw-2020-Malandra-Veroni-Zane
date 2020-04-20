package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;
import java.awt.*;

public class Worker {

    private final Player playerOwner;
    private final String idWorker;
    private Color color;
    private Slot workerSlot;      //ridondanza con slot su chi c'è dentro

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

    public Color getColor() {
        return color;
    }

    public Slot getWorkerSlot() {
        return workerSlot;
    }

    protected void setColor(Color color) {
        this.color = color;
    }

    protected void setWorkerSlot(Slot workerSlot) {
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
        //notify alla view
    }

    //TODO: Test
    /**
     * Creation of a new building
     *
     * @param buildHere New construction position
     */
    public void build(Slot buildHere){
        BuildingLevel slotTopBuilding = buildHere.getConstructionTopLevel();
        Building newBuilding = new Building(BuildingLevel.getNextLevel(slotTopBuilding));
        buildHere.setBuilding(newBuilding);
        //notify alla view
    }

    public void forcedDomeBuild(Slot buildHere, boolean buildDome){
        if (buildDome){
            Building newBuilding = new Building(BuildingLevel.DOME);
            buildHere.setBuilding(newBuilding);
        } else
            build(buildHere);
    }
}
