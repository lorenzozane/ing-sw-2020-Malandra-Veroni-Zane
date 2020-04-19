package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Cloneable {

    private static final int BOARD_DIMENSION = 5;

    private Slot[][] gameBoardSlots = new Slot[BOARD_DIMENSION][BOARD_DIMENSION];

    public Board(){
        initializeBoard();
    }

    private void initializeBoard(){
        for (int i = 0; i < BOARD_DIMENSION; i++)
            for (int j = 0; j < BOARD_DIMENSION; j++)
                gameBoardSlots[i][j] = new Slot(new Position(i, j));
    }

    public ArrayList<Slot> getAdjacentSlots(Slot centerSlot){
        ArrayList<Slot> adjacentSlots = new ArrayList<>(8);
        int centerSlotX = centerSlot.getSlotPosition().getCoordinateX();
        int centerSlotY = centerSlot.getSlotPosition().getCoordinateY();

        for (int x = (centerSlotX > 0 ? -1 : 0); x <= (centerSlotX < BOARD_DIMENSION ? 1 : 0); x++)
            for (int y = (centerSlotY > 0 ? -1 : 0); y <= (centerSlotY < BOARD_DIMENSION ? 1 : 0); y++)
                if (x != 0 || y != 0)
                    adjacentSlots.add(gameBoardSlots[centerSlotX + x][centerSlotY + y]);

        return adjacentSlots;
    }

    @Override
    protected final Board clone() {
        final Board result = new Board();
        for (int i = 0; i < BOARD_DIMENSION; i++)
            result.gameBoardSlots[i] = gameBoardSlots[i].clone();   //TODO: Check

        return result;
    }

    private String[][] gameBoard = {{" " , Color.ANSI_BRIGHT_BLUE + "         1          " + Color.RESET , " ", Color.ANSI_BRIGHT_BLUE + "          2         " + Color.RESET , " " , Color.ANSI_BRIGHT_BLUE + "         3          " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "          4         " + Color.RESET , " " , Color.ANSI_BRIGHT_BLUE + "          5         " + Color.RESET},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "A" + Color.RESET ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "B" + Color.RESET ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "C" + Color.RESET ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "D" + Color.RESET ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET, Color.ANSI_GREEN + "+" + Color.RESET, Color.ANSI_GREEN + "--------------------" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "E" + Color.RESET ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    ", Color.ANSI_GREEN + "|" + Color.RESET, "                    "},};


    public void updateBuildingOnBoard(Slot slotBuilding) {
        int[] value = new int[slotBuilding.getBuildingsStatus().size()];
        String[][] cube = {{"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "}};

        for (int i = 0; i < slotBuilding.getBuildingsStatus().size(); i++) {
            if (slotBuilding.getBuildingsStatus().get(i) == null) {
                value[i] = 0;
            } else
                value[i] = slotBuilding.getBuildingsStatus().get(i).getLevelValue();
        }

        switch (Arrays.toString(value)) {
            case "[1, 0, 0, 0]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                break;

            case "[1, 2, 0, 0]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                break;

            case "[1, 2, 3, 0]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0] = "    ∏∏∏∏∏∏∏∏∏∏∏∏    ";
                break;

            case "[1, 2, 3, 4]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0] = "    ∏∏∏∏∏∏∏∏∏∏∏∏    ";
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "    /__________\\    " + Color.RESET;
                cube[1][0] = Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;

            case "[1, 2, 0, 4]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "    /__________\\    " + Color.RESET;
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;

            case "[1, 0, 0, 4]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = Color.ANSI_BRIGHT_BLUE + "    /__________\\    " + Color.RESET;
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;


        }

        int[] position = new int[2];
        position[0] = slotBuilding.getSlotPosition().getCoordinateX();
        position[1] = slotBuilding.getSlotPosition().getCoordinateY();
        switch (Arrays.toString(position)) {
            case "[0, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][1] = cube[i][0];
                }
                break;

            case "[0, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][3] = cube[i][0];
                }
                break;

            case "[0, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][5] = cube[i][0];
                }
                break;

            case "[0, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][7] = cube[i][0];
                }
                break;

            case "[0, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][9] = cube[i][0];
                }
                break;

            case "[1, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][1] = cube[i][0];
                }
                break;

            case "[1, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][3] = cube[i][0];
                }
                break;

            case "[1, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][5] = cube[i][0];
                }
                break;

            case "[1, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][7] = cube[i][0];
                }
                break;

            case "[1, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][9] = cube[i][0];
                }
                break;

            case "[2, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][1] = cube[i][0];
                }
                break;

            case "[2, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][3] = cube[i][0];
                }
                break;

            case "[2, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][5] = cube[i][0];
                }
                break;

            case "[2, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][7] = cube[i][0];
                }
                break;

            case "[2, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][9] = cube[i][0];
                }
                break;

            case "[3, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][1] = cube[i][0];
                }
                break;

            case "[3, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][3] = cube[i][0];
                }
                break;

            case "[3, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][5] = cube[i][0];
                }
                break;

            case "[3, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][7] = cube[i][0];
                }
                break;

            case "[3, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][9] = cube[i][0];
                }
                break;

            case "[4, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][1] = cube[i][0];
                }
                break;

            case "[4, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][3] = cube[i][0];
                }
                break;

            case "[4, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][5] = cube[i][0];
                }
                break;

            case "[4, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][7] = cube[i][0];
                }
                break;

            case "[4, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][9] = cube[i][0];
                }
                break;
        }
    }


    public void removeWorkerOnBoard(Slot slotBeforeMove){

    }

    public void putWorkerOnBoard(Slot slotMove){
        int[] value = new int[slotMove.getBuildingsStatus().size()];
        String[][] cube = {{"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "}};

        for (int i = 0; i < slotMove.getBuildingsStatus().size(); i++) {
            if (slotMove.getBuildingsStatus().get(i) == null) {
                value[i] = 0;
            } else
                value[i] = slotMove.getBuildingsStatus().get(i).getLevelValue();
        }



        switch (Arrays.toString(value)) {
            case "[0, 0, 0, 0]":
                cube[5][0] = slotMove.getWorkerInSlot().getColor() + "        / \\         " + Color.RESET;
                cube[4][0] = slotMove.getWorkerInSlot().getColor() + "        /|\\         " + Color.RESET;
                cube[3][0] = slotMove.getWorkerInSlot().getColor() + "         o          " + Color.RESET;
                break;

            case "[1, 0, 0, 0]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = slotMove.getWorkerInSlot().getColor() + "        / \\         " + Color.RESET;
                cube[3][0] = slotMove.getWorkerInSlot().getColor() + "        /|\\         " + Color.RESET;
                cube[2][0] = slotMove.getWorkerInSlot().getColor() + "         o          " + Color.RESET;
                break;

            case "[1, 2, 0, 0]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0] = "        / \\         ";
                cube[2][0] = "        /|\\         ";
                cube[1][0] = "         o          ";
                break;
        }


        int[] position = new int[2];
        position[0] = slotMove.getSlotPosition().getCoordinateX();
        position[1] = slotMove.getSlotPosition().getCoordinateY();
        switch (Arrays.toString(position)) {
            case "[0, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][1] = cube[i][0];
                }
                break;

            case "[0, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][3] = cube[i][0];
                }
                break;

            case "[0, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][5] = cube[i][0];
                }
                break;

            case "[0, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][7] = cube[i][0];
                }
                break;

            case "[0, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+1][9] = cube[i][0];
                }
                break;

            case "[1, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][1] = cube[i][0];
                }
                break;

            case "[1, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][3] = cube[i][0];
                }
                break;

            case "[1, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][5] = cube[i][0];
                }
                break;

            case "[1, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][7] = cube[i][0];
                }
                break;

            case "[1, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+8][9] = cube[i][0];
                }
                break;

            case "[2, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][1] = cube[i][0];
                }
                break;

            case "[2, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][3] = cube[i][0];
                }
                break;

            case "[2, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][5] = cube[i][0];
                }
                break;

            case "[2, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][7] = cube[i][0];
                }
                break;

            case "[2, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+15][9] = cube[i][0];
                }
                break;

            case "[3, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][1] = cube[i][0];
                }
                break;

            case "[3, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][3] = cube[i][0];
                }
                break;

            case "[3, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][5] = cube[i][0];
                }
                break;

            case "[3, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][7] = cube[i][0];
                }
                break;

            case "[3, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+22][9] = cube[i][0];
                }
                break;

            case "[4, 0]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][1] = cube[i][0];
                }
                break;

            case "[4, 1]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][3] = cube[i][0];
                }
                break;

            case "[4, 2]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][5] = cube[i][0];
                }
                break;

            case "[4, 3]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][7] = cube[i][0];
                }
                break;

            case "[4, 4]":
                for (int i = 0; i < 6; i++) {
                    gameBoard[i+29][9] = cube[i][0];
                }
                break;


        }

    }

    public void printGameBoard() {
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard[i].length; j++) {
                System.out.print(this.gameBoard[i][j]);
            }
            System.out.println();
        }
    }
}
