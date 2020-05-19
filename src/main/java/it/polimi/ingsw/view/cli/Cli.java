package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Slot;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.Arrays;

public class Cli implements Observer<UpdateTurnMessage> {
    private String[][] gameBoard = {{" " , Color.ANSI_BRIGHT_BLUE + "         1          " + Color.RESET , " ", Color.ANSI_BRIGHT_BLUE + "          2         " + Color.RESET , " " , Color.ANSI_BRIGHT_BLUE + "         3          " + Color.RESET, " ", Color.ANSI_BRIGHT_BLUE + "          4         " + Color.RESET , " " , Color.ANSI_BRIGHT_BLUE + "          5         " + Color.RESET},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "A" + Color.RESET ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "B" + Color.RESET ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "C" + Color.RESET ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "D" + Color.RESET ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},

            {" " ,Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET, Color.ANSI_GREEN + "╬" + Color.RESET, Color.ANSI_GREEN + "════════════════════" + Color.RESET},

            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {Color.ANSI_BRIGHT_BLUE + "E" + Color.RESET ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},
            {" " ,"                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    ", Color.ANSI_GREEN + "║" + Color.RESET, "                    "},};


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
                value[i] = slotBuilding.getBuildingsStatus().get(i).getLevelAsInt();
        }

        switch (Arrays.toString(value)) {
            case "[1, 0, 0, 0]":
                cube[5][0] = "████████████████████";
                break;

            case "[1, 2, 0, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                break;

            case "[1, 2, 3, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = "    ████████████    ";
                break;

            case "[1, 2, 3, 4]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = "    ████████████    ";
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
                cube[1][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
                cube[0][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
                break;

            case "[0, 0, 0, 4]":
                cube[5][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
                cube[4][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
                break;

            case "[1, 0, 0, 4]":
                cube[5][0] = "████████████████████";
                cube[4][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
                break;

            case "[1, 2, 0, 4]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = Color.ANSI_BRIGHT_BLUE + "    ████████████    " + Color.RESET;
                cube[2][0] = Color.ANSI_BRIGHT_BLUE + "     ██████████     " + Color.RESET;
                cube[1][0] = Color.ANSI_BRIGHT_BLUE + "       ▄▄▄▄▄▄       " + Color.RESET;
                break;

            default:
                throw new IllegalArgumentException();

        }

        int[] position = new int[2];
        position[0] = slotBuilding.getSlotPosition().getCoordinateX();
        position[1] = slotBuilding.getSlotPosition().getCoordinateY();

        for (int i=0; i<6; i++){
            gameBoard[i+(position[0]*7)+1][(position[1]*2)+1]=cube[i][0];
        }

    }


    public void removeWorkerOnBoard(Slot slotBeforeMove){

        int[] value = new int[slotBeforeMove.getBuildingsStatus().size()];
        String[][] cube = {{"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "}};

        for (int i = 0; i < slotBeforeMove.getBuildingsStatus().size(); i++) {
            if (slotBeforeMove.getBuildingsStatus().get(i) == null) {
                value[i] = 0;
            } else
                value[i] = slotBeforeMove.getBuildingsStatus().get(i).getLevelAsInt();
        }


        switch (Arrays.toString(value)) {
            case "[1, 0, 0, 0]":
                cube[5][0] = "████████████████████";
                break;

            case "[1, 2, 0, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                break;

            case "[1, 2, 3, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = "    ████████████    ";
                break;

            default:
                throw new IllegalArgumentException();
        }


        int[] position = new int[2];
        position[0] = slotBeforeMove.getSlotPosition().getCoordinateX();
        position[1] = slotBeforeMove.getSlotPosition().getCoordinateY();

        for (int i=0; i<6; i++){
            gameBoard[i+(position[0]*7)+1][(position[1]*2)+1]=cube[i][0];
        }

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
                value[i] = slotMove.getBuildingsStatus().get(i).getLevelAsInt();
        }



        switch (Arrays.toString(value)) {
            case "[0, 0, 0, 0]":
                cube[5][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
                cube[4][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
                cube[3][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
                break;

            case "[1, 0, 0, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
                cube[3][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
                cube[2][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
                break;

            case "[1, 2, 0, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
                cube[2][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
                cube[1][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
                break;

            case "[1, 2, 3, 0]":
                cube[5][0] = "████████████████████";
                cube[4][0] = "  ████████████████  ";
                cube[3][0] = "    ████████████    ";
                cube[2][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /¯\\         " + Color.RESET;
                cube[1][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "        /|\\         " + Color.RESET;
                cube[0][0] = slotMove.getWorkerInSlot().getColor().getEscape() + "         o          " + Color.RESET;
                break;

            default:
                throw new IllegalArgumentException();
        }


        int[] position = new int[2];
        position[0] = slotMove.getSlotPosition().getCoordinateX();
        position[1] = slotMove.getSlotPosition().getCoordinateY();

        for (int i=0; i<6; i++){
            gameBoard[i+(position[0]*7)+1][(position[1]*2)+1]=cube[i][0];
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

    @Override
    public void update(UpdateTurnMessage message) {

    }
}