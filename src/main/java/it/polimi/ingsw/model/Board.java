package it.polimi.ingsw.model;

public class Board {
    private final int BOARD_DIMENSION=5;

    private Slot[][] table;

    public Board(){
        table= new Slot[BOARD_DIMENSION][BOARD_DIMENSION];
    }
}
