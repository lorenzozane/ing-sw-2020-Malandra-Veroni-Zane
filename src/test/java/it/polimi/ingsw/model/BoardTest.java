package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void initializeBoardTest() {
        Board board = new Board();

        final int BOARD_DIMENSION = 5;

        for (int i = 0; i < BOARD_DIMENSION; i++)
            for (int j = 0; j < BOARD_DIMENSION; j++)
                assertEquals(board.getSlot(new Position(i, j)).getSlotPosition(), new Position(i, j));
    }

    @Test //completed
    public void getAdjacentSlotsTest() {
        Board board = new Board();
        Slot slot = board.getSlot(new Position(1, 1));
        ArrayList<Slot> adjacentSlots = board.getAdjacentSlots(slot);

        ArrayList<Position> expectedPositions = new ArrayList<>(Arrays.asList(
                new Position(0, 0), new Position(0, 1),
                new Position(0, 2), new Position(1, 0),
                new Position(1, 2), new Position(2, 0),
                new Position(2, 1), new Position(2, 2)
        ));

        for (Position position : expectedPositions)
            assertTrue(adjacentSlots.contains(board.getSlot(position)));
    }

    
}