package it.polimi.ingsw.model;

public class Board {
    private final int BOARD_DIMENSION=5;

    private Slot[][] table;
    private String[][] gameBoard = new String[5][5];
    private String cube = new String();

    public Board(){
        table= new Slot[BOARD_DIMENSION][BOARD_DIMENSION];
    }

    public void emptyGameBoard(String gameBoard[][]){
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                gameBoard[i][j] = "                    |\n" +
                        "                    |\n" +
                        "                    |\n" +
                        "                    |\n" +
                        "                    |\n" +
                        "--------------------+\n";
            }
        }
    }
    public void printGameBoard(String gameBoard[][]){
        for (int i=0; i<5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }
    }



}
