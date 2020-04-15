package it.polimi.ingsw.model;

import java.util.Arrays;

public class Board {

    private String[][] gameBoard = {{"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},

            {Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET},

            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},

            {Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET},

            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},

            {Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET},

            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},

            {Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET, Color.ANSI_GREEN+ "+" + Color.RESET, Color.ANSI_GREEN+ "--------------------" + Color.RESET},

            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},
            {"                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    ", Color.ANSI_GREEN+ "|" + Color.RESET, "                    "},};





    public void updateBuildingOnBoard(Slot slotBuilding){
        int[] value= new int[4];
        String[][] cube = {{"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "},
                {"                    "}};
        for(int i=0; i<4; i++){
            if (slotBuilding.getBuildingStatus()[i]==null){
                value[i]=0;
            }
            else
                value[i]=slotBuilding.getBuildingStatus()[i].getLevelAsInt();
        }

        switch (Arrays.toString(value)){
            case "[1, 0, 0, 0]":
                cube[5][0]="∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                break;

            case "[1, 2, 0, 0]":
                cube[5][0]="∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0]="  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                break;

            case "[1, 2, 3, 0]":
                cube[5][0]="∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0]="  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0]="    ∏∏∏∏∏∏∏∏∏∏∏∏    ";
                break;

            case "[1, 2, 3, 4]":
                cube[5][0]="∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏";
                cube[4][0]="  ∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏∏  ";
                cube[3][0]="    ∏∏∏∏∏∏∏∏∏∏∏∏    ";
                cube[2][0]=Color.ANSI_BRIGHT_BLUE + "    ⎧__________⎫    " + Color.RESET;
                cube[1][0]=Color.ANSI_BRIGHT_BLUE + "     __________     " + Color.RESET;
                break;


        }

        int[] position = new int[2];
        position[0]=slotBuilding.getSlotPosition().getCoordinateX();
        position[1]=slotBuilding.getSlotPosition().getCoordinateY();
        switch (Arrays.toString(position)){
            case "[0, 0]":
                for(int i=0; i<5; i++){
                    gameBoard[i][0]=cube[i][0];
                }
            break;
        }

    }






    public void printGameBoard(){
        for (int i=0; i<this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard[i].length; j++) {
                System.out.print(this.gameBoard[i][j]);
            }
            System.out.println();

        }

    }



}
