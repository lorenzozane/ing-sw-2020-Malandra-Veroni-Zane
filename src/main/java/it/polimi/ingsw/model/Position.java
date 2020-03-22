package it.polimi.ingsw.model;

public class Position {

    private int coordinateX, coordinateY;

    public Position(int coordinateX, int coordinateY){
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    @Override
    public boolean equals(Object o){
        Position toCompare = (Position) o;

        if (this.coordinateX == toCompare.coordinateX && this.coordinateY == toCompare.coordinateY)
            return true;
        return false;
    }
}
