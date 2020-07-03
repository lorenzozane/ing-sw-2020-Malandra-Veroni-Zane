package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Possible positions on the game board.
 */
public class Position implements Serializable {

    private static final long serialVersionUID = -3555337604374755501L;
    private int coordinateX, coordinateY;

    public Position(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    @Override
    public boolean equals(Object o) {
        Position toCompare = (Position) o;

        if (this.coordinateX == toCompare.coordinateX && this.coordinateY == toCompare.coordinateY)
            return true;
        return false;
    }
}
