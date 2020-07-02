package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.util.Arrays;
import java.util.Scanner;

public class Cli {

    protected Player playerOwner;
    protected View viewOwner = null;
    private static final int SLOT_HEIGHT = 6;
    private Scanner scanner = new Scanner(System.in);
    private boolean isActiveAsyncReadResponse = false;

    public void setViewOwner(View viewOwner) {
        if (this.viewOwner == null)
            this.viewOwner = viewOwner;
    }

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

    /**
     * Refresh the board after a change.
     *
     * @param board The instance of the board from which to get information.
     */
    public void refreshBoard(Board board) {
        currentGameBoard = deepCopyStringArray(emptyGameBoard);

        for (int i = 0; i < board.getBoardDimension(); i++) {
            for (int j = 0; j < board.getBoardDimension(); j++) {
                refreshSlot(board.getSlot(new Position(i, j)));
            }
        }
        printGameBoard();
    }

    /**
     * Refresh a specific slot during the board refreshing.
     *
     * @param slot The slot to be refreshed.
     */
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

    /**
     * Draw the new state of the specified slot.
     *
     * @param slot Slot to be redesigned.
     * @return Returns the new draw of the slot.
     */
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

        return refreshedSlot;
    }

    /**
     * Draw the levels in the specified slot.
     *
     * @param refreshedSlot Slot to be redesigned.
     * @param levels Levels to be added ad the slot draw.
     */
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

    /**
     * Draw the dome in the specified slot.
     *
     * @param refreshedSlot Slot to be redesigned.
     * @param startingFrom Height on which to draw the dome.
     */
    private void drawDome(String[][] refreshedSlot, int startingFrom) {
        if (startingFrom >= 0 && startingFrom <= SLOT_HEIGHT - 3) {
            refreshedSlot[startingFrom][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
            refreshedSlot[startingFrom + 1][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
            refreshedSlot[startingFrom + 2][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
        }
    }

    /**
     * Draw the worker in the specified slot.
     *
     * @param refreshedSlot Slot to be redesigned.
     * @param startingFrom Height on which to draw the worker.
     * @param playerColor The color to draw the worker.
     */
    private void drawWorker(String[][] refreshedSlot, int startingFrom, PlayerColor playerColor) {
        if (startingFrom >= 0 && startingFrom <= SLOT_HEIGHT - 3) {
            refreshedSlot[startingFrom][0] = playerColor.getEscape() + "         o          " + Color.RESET;
            refreshedSlot[startingFrom + 1][0] = playerColor.getEscape() + "        /|\\         " + Color.RESET;
            refreshedSlot[startingFrom + 2][0] = playerColor.getEscape() + "        /¯\\         " + Color.RESET;
        }
    }

    /**
     * Make a deep copy of the old board to redraw on that the modified part.
     *
     * @param original The old board.
     * @return The updated board.
     */
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

    /**
     * Analyze the building status of the specified slot to know what to draw.
     *
     * @param slot The slot to analyze.
     * @return The slot level status as sequence of int. Return 0 if the level is empty, an int specified the level otherwise.
     */
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

    /**
     * Print the board on the command line interface.
     */
    public void printGameBoard() {
        for (String[] strings : this.currentGameBoard) {
            for (String string : strings)
                System.out.print(string);

            System.out.println();
        }
    }

    /**
     * Print on the command line interface the specified message.
     *
     * @param messageToShow The message to be shown.
     */
    public void showMessage(String messageToShow) {
        if(messageToShow.contains(Message.gameOver))
            System.out.println(Message.gameOverCli);
        if(messageToShow.contains(ViewMessage.winner))
            System.out.println(Message.winCli);
        else
            System.out.println(messageToShow);
    }

    /**
     * Print on the command line interface the specified message with a specific color.
     *
     * @param messageToShow The message to be shown.
     * @param playerColor The color to write the message with.
     */
    public void showMessage(String messageToShow, PlayerColor playerColor) {
        System.out.println(playerColor.getEscape() + messageToShow + Color.RESET);
    }

    private boolean isActiveAsyncReadResponse() {
        return isActiveAsyncReadResponse;
    }

    /**
     * Activate a thread that listen asynchronously for the player input.
     */
    public void activateAsyncReadResponse() {
        isActiveAsyncReadResponse = true;
        asyncReadResponse();
    }

//    public void deactivateAsyncReadResponse() {
//        isActiveAsyncReadResponse = false;
//        if (asyncReaderResponse != null) {
//            asyncReaderResponse.interrupt();
//        }
//    }

    /**
     * Listen asynchronously for the player input.
     */
    private void asyncReadResponse() {
        Thread asyncReaderResponse = new Thread(() -> {
            while (isActiveAsyncReadResponse()) {
                String input = scanner.nextLine();
                new Thread(() -> viewOwner.handleResponse(input)).start();
            }
        });

        asyncReaderResponse.start();
    }

    /**
     * Show to the player a specific message which is shown to all players simultaneously.
     *
     * @param messageToShow The message to be shown.
     */
    public void showSimultaneousMessage(String messageToShow) {
        System.out.println(messageToShow);
        if (!messageToShow.contains("Error: ") && !messageToShow.equals(Message.lobby) && !messageToShow.equals(Message.wait) && !messageToShow.equals(Message.gameLoading))
            new Thread(this::readResponse).start();
    }

    /**
     * Listen for the player input not asynchronously.
     */
    private void readResponse() {
        try {
            String input = scanner.nextLine();
            viewOwner.handleResponse(input);
        } catch (Exception ex) {
            System.out.println("Scanner closed.");
        }
    }

//    public String showMessageImmediateResponse(String messageToShow) {
//        System.out.println(messageToShow);
//        Scanner scanner = new Scanner(System.in);
//        return scanner.nextLine();
//    }
}