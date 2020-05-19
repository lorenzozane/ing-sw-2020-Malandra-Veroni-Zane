package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable {

    private static final int BOARD_DIMENSION = 5;

    private Slot[][] gameBoardSlots = new Slot[BOARD_DIMENSION][BOARD_DIMENSION];

    public Board(){
        initializeBoard();
    }

    /**
     * Initialize the game board instantiating all the slots
     */
    private void initializeBoard(){
        for (int i = 0; i < BOARD_DIMENSION; i++)
            for (int j = 0; j < BOARD_DIMENSION; j++)
                gameBoardSlots[i][j] = new Slot(new Position(i, j));
    }

    public Slot getSlot(Position position) {
        return gameBoardSlots[position.getCoordinateX()][position.getCoordinateY()];
    }

    /**
     * Allows to obtain all the slots adjacent to the central one
     *
     * @param centerSlot The central slot
     * @return An ArrayList of Slot containing all the adjacent slots
     */
    public ArrayList<Slot> getAdjacentSlots(Slot centerSlot){
        ArrayList<Slot> adjacentSlots = new ArrayList<>(8);
        int centerSlotX = centerSlot.getSlotPosition().getCoordinateX();
        int centerSlotY = centerSlot.getSlotPosition().getCoordinateY();

        for (int x = (centerSlotX > 0 ? -1 : 0); x <= (centerSlotX < BOARD_DIMENSION - 1 ? 1 : 0); x++)
            for (int y = (centerSlotY > 0 ? -1 : 0); y <= (centerSlotY < BOARD_DIMENSION - 1 ? 1 : 0); y++)
                if (x != 0 || y != 0)
                    adjacentSlots.add(gameBoardSlots[centerSlotX + x][centerSlotY + y]);

        return adjacentSlots;
    }

    /**
     * Allows to obtain the next slot along the movement direction
     *
     * @param startingSlot The starting slot of the movement
     * @param targetSlot The target slot of the movement
     * @return The next slot along the movement direction
     */
    public Slot getBackwardsSlot(Slot startingSlot, Slot targetSlot) {
        int nextSlotX = targetSlot.getSlotPosition().getCoordinateX() + (targetSlot.getSlotPosition().getCoordinateX() - startingSlot.getSlotPosition().getCoordinateX());
        int nextSlotY = targetSlot.getSlotPosition().getCoordinateY() + (targetSlot.getSlotPosition().getCoordinateY() - startingSlot.getSlotPosition().getCoordinateY());

        if (nextSlotX < 0 || nextSlotX > BOARD_DIMENSION - 1 || nextSlotY < 0 || nextSlotY > BOARD_DIMENSION - 1)
            return null;

        return gameBoardSlots[nextSlotX][nextSlotY];
    }

    @Override
    protected final Board clone() {
        final Board result = new Board();
        for (int i = 0; i < BOARD_DIMENSION; i++)
            result.gameBoardSlots[i] = gameBoardSlots[i].clone();   //TODO: Check

        return result;
    }

}
