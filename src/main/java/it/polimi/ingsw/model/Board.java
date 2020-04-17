package it.polimi.ingsw.model;

import java.util.Arrays;

public class Board {

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
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "    ⎧__________⎫    " + Color.RESET;
                cube[1][0] = Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;

            case "[1, 2, 0, 4]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = "  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "    ⎧__________⎫    " + Color.RESET;
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;

            case "[1, 0, 0, 4]":
                cube[5][0] = "∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0] = Color.ANSI_BRIGHT_BLUE + "    ⎧__________⎫    " + Color.RESET;
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
