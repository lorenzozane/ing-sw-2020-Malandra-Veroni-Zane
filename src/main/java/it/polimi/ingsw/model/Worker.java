package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;
import java.awt.*;

public class Worker {

    private final String idWorker;
    private Color color;
    private Slot workerSlot;      //ridondanza con slot su chi c'è dentro

    public Worker(String idWorkerPlayer){
        this.idWorker = idWorkerPlayer;
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

    public void setColor(Color color) {
        this.color = color;
    }

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
        //notify alla view
    }


    /**
     * Creation of a new building
     *
     * @param buildHere New construction position
     * @param level New construction level
     */
    public void buildConstruction(Slot buildHere, BuildingLevel level){
        Building newBuilding = new Building(level);
        buildHere.setBuilding(newBuilding);
        //notify alla view
    }







}
