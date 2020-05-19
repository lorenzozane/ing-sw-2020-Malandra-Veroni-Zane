package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observer.Observer;

import java.util.Arrays;

public class Cli implements Observer<UpdateTurnMessage> {

    protected Player playerOwner;
    private static final int SLOT_HEIGHT = 6;

    private final String[][] emptyGameBoard = {{" ", Color.ANSI_BRIGHT_BLUE + "         1          " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "          2         " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "         3          " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "          4         " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "          5         " + Color.RESET},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "A" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" ", Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "B" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" ", Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "C" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" ", Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "D" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" ", Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "E" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" ", "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},};

    final String[][] emptySlot = {{"                    "},
            {"                    "},
            {"                    "},
            {"                    "},
            {"                    "},
            {"                    "}};

    protected String[][] currentGameBoard = deepCopyStringArray(emptyGameBoard);

    public void refreshBoard(Board board) {
        currentGameBoard = deepCopyStringArray(emptyGameBoard);

        for (int i = 0; i < board.getBoardDimension(); i++)
            for (int j = 0; j < board.getBoardDimension(); j++)
                refreshSlot(board.getSlot(new Position(i, j)));
    }

    public void refreshSlot(Slot slot) {
        String[][] refreshedSlot;

        refreshedSlot = drawSlot(slot);

        int[] position = {
                slot.getSlotPosition().getCoordinateX(),
                slot.getSlotPosition().getCoordinateY()
        };

        for (int i = 0; i < SLOT_HEIGHT; i++) {
            currentGameBoard[i + (position[1] * 7) + 1][(position[0] * 2) + 1] = refreshedSlot[i][0];
        }
    }

    private String[][] drawSlot(Slot slot) {
        String[][] refreshedSlot = deepCopyStringArray(emptySlot);
        int[] slotStatusValue = analyzeSlotStatus(slot);
        int workerOrDomeIndex = 3;

        for (int i = 0; i < slot.getBuildingsStatus().size() - 1; i++)
            if (slotStatusValue[i] != 0) {
                drawLevel(refreshedSlot, i + 1);
                workerOrDomeIndex--;
            }

        if (slot.getWorkerInSlot() != null)
            drawWorker(refreshedSlot, workerOrDomeIndex, slot.getWorkerInSlot().getColor());
        else if (slotStatusValue[slot.getBuildingsStatus().size() - 1] != 0)
            drawDome(refreshedSlot, workerOrDomeIndex);

//        switch (Arrays.toString(slotStatusValue)) {
//            case "[0, 0, 0, 0]":
//                if (slot.getWorkerInSlot() != null)
//                    drawWorker(refreshedSlot, 3, slot.getWorkerInSlot().getColor());
//                break;
//
//            case "[1, 0, 0, 0]":
//                drawLevel(refreshedSlot, 1);
//
//                if (slot.getWorkerInSlot() != null)
//                    drawWorker(refreshedSlot, 2, slot.getWorkerInSlot().getColor());
//                break;
//
//            case "[1, 2, 0, 0]":
//                drawLevel(refreshedSlot, 1, 2);
//
//                if (slot.getWorkerInSlot() != null)
//                    drawWorker(refreshedSlot, 1, slot.getWorkerInSlot().getColor());
//                break;
//
//            case "[1, 2, 3, 0]":
//                drawLevel(refreshedSlot, 1, 2, 3);
//
//                if (slot.getWorkerInSlot() != null)
//                    drawWorker(refreshedSlot, 0, slot.getWorkerInSlot().getColor());
//                break;
//
//            case "[1, 2, 3, 4]":
//                drawDome(refreshedSlot, 0);
//                drawLevel(refreshedSlot, 1, 2, 3);
//                break;
//
//            case "[0, 0, 0, 4]":
//                drawDome(refreshedSlot, 3);
//                break;
//
//            case "[1, 0, 0, 4]":
//                drawDome(refreshedSlot, 2);
//                drawLevel(refreshedSlot, 1);
//                break;
//
//            case "[1, 2, 0, 4]":
//                drawDome(refreshedSlot, 1);
//                drawLevel(refreshedSlot, 1, 2);
//                break;
//
//            default:
//                throw new IllegalArgumentException();
//        }

        return refreshedSlot;
    }

    private void drawLevel(String[][] refreshedSlot, int... levels) {
        for (int level : levels) {
            if (level == 1)
                refreshedSlot[5][0] = "████████████████████";
            else if (level == 2)
                refreshedSlot[4][0] = "  ████████████████  ";
            else if (level == 3)
                refreshedSlot[3][0] = "    ████████████    ";
        }
    }

    private void drawDome(String[][] refreshedSlot, int startingFrom) {
        if (startingFrom >= 0 && startingFrom <= SLOT_HEIGHT - 3) {
            refreshedSlot[startingFrom][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
            refreshedSlot[startingFrom + 1][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
            refreshedSlot[startingFrom + 2][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
        }
    }

    private void drawWorker(String[][] refreshedSlot, int startingFrom, Color.PlayerColor playerColor) {
        if (startingFrom >= 0 && startingFrom <= SLOT_HEIGHT - 3) {
            refreshedSlot[startingFrom][0] = playerColor.getEscape() + "         o          " + Color.RESET;
            refreshedSlot[startingFrom + 1][0] = playerColor.getEscape() + "        /|\\         " + Color.RESET;
            refreshedSlot[startingFrom + 2][0] = playerColor.getEscape() + "        /¯\\         " + Color.RESET;
        }
    }

//    public void removeWorkerOnBoard(Slot slotBeforeMove) {
//
//        int[] value = new int[slotBeforeMove.getBuildingsStatus().size()];
//        String[][] cube = {{"                    "},
//                {"                    "},
//                {"                    "},
//                {"                    "},
//                {"                    "},
//                {"                    "}};
//
//        for (int i = 0; i < slotBeforeMove.getBuildingsStatus().size(); i++) {
//            if (slotBeforeMove.getBuildingsStatus().get(i) == null) {
//                value[i] = 0;
//            } else
//                value[i] = slotBeforeMove.getBuildingsStatus().get(i).getLevelAsInt();
//        }
//
//
//        switch (Arrays.toString(value)) {
//            case "[1, 0, 0, 0]":
//                cube[5][0] = "████████████████████";
//                break;
//
//            case "[1, 2, 0, 0]":
//                cube[5][0] = "████████████████████";
//                cube[4][0] = "  ████████████████  ";
//                break;
//
//            case "[1, 2, 3, 0]":
//                cube[5][0] = "████████████████████";
//                cube[4][0] = "  ████████████████  ";
//                cube[3][0] = "    ████████████    ";
//                break;
//
//            default:
//                throw new IllegalArgumentException();
//        }
//
//
//        int[] position = new int[2];
//        position[0] = slotBeforeMove.getSlotPosition().getCoordinateX();
//        position[1] = slotBeforeMove.getSlotPosition().getCoordinateY();
//
//        for (int i = 0; i < 6; i++) {
//            emptyGameBoard[i + (position[0] * 7) + 1][(position[1] * 2) + 1] = cube[i][0];
//        }
//
//    }

//    public String[][] updateWorkerOnBoard(Slot slot) {
//        String[][] refreshedSlot = emptySlot;
//        int[] slotStatusValue = analyzeSlotStatus(slot);
//
//        switch (Arrays.toString(slotStatusValue)) {
//            case "[0, 0, 0, 0]":
//                refreshedSlot[5][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
//                refreshedSlot[4][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
//                refreshedSlot[3][0] = slot.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
//                break;
//
//            case "[1, 0, 0, 0]":
//                refreshedSlot[5][0] = "████████████████████";
//                refreshedSlot[4][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
//                refreshedSlot[3][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
//                refreshedSlot[2][0] = slot.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
//                break;
//
//            case "[1, 2, 0, 0]":
//                refreshedSlot[5][0] = "████████████████████";
//                refreshedSlot[4][0] = "  ████████████████  ";
//                refreshedSlot[3][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
//                refreshedSlot[2][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
//                refreshedSlot[1][0] = slot.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
//                break;
//
//            case "[1, 2, 3, 0]":
//                refreshedSlot[5][0] = "████████████████████";
//                refreshedSlot[4][0] = "  ████████████████  ";
//                refreshedSlot[3][0] = "    ████████████    ";
//                refreshedSlot[2][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
//                refreshedSlot[1][0] = slot.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
//                refreshedSlot[0][0] = slot.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
//                break;
//
//            default:
//                throw new IllegalArgumentException();
//        }
//
//        return refreshedSlot;
//    }

    private static String[][] deepCopyStringArray(String[][] original) {
        if (original == null) {
            return null;
        }

        final String[][] result = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    private int[] analyzeSlotStatus(Slot slot) {
        int[] slotStatusValue = new int[slot.getBuildingsStatus().size()];
        for (int i = 0; i < slot.getBuildingsStatus().size(); i++) {
            if (slot.getBuildingsStatus().get(i) == null)
                slotStatusValue[i] = 0;
            else
                slotStatusValue[i] = slot.getBuildingsStatus().get(i).getLevelAsInt();
        }

        return slotStatusValue;
    }

    public void printGameBoard() {
        for (String[] strings : this.currentGameBoard) {
            for (String string : strings)
                System.out.print(string);

            System.out.println();
        }
    }

    @Override
    public void update(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.COLOR_REQUEST) {

            }
        }
    }
}
