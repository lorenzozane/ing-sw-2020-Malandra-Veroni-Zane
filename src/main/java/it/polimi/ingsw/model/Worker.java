package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Building.BuildingLevel;
import java.awt.*;

public class Worker {

    private final String idWorker;
    private Color color;
    private Position workerPosition;

    public Worker(String idWorkerPlayer){
        this.idWorker = idWorkerPlayer;
    }

    public String getIdWorker() {
        return idWorker;
    }

    public Color getColor() {
        return color;
    }

    public Position getWorkerPosition() {
        return workerPosition;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setWorkerPosition(Position workerPosition) {
        this.workerPosition = workerPosition;
    }


    /**
     * Set new Worker's position
     *
     * @param moveHere New position
     */
    public void move(Slot moveHere){
        setWorkerPosition(moveHere.getSlotPosition());
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
