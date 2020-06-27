package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class GuiController {

    @FXML
    Button level100;

    @FXML
    Button level200;

    @FXML
    Button level300;

    @FXML
    Button dome00;

    @FXML
    Button level101;

    @FXML
    Button level201;


    @FXML
    private void setVisible00(ActionEvent event) {
        level100.setVisible(true);
        level200.setVisible(true);
        level300.setVisible(true);
        dome00.setVisible(true);
    }

    @FXML
    private void setVisible01(ActionEvent event) {
        level101.setVisible(true);
        level201.setVisible(true);
    }

    @FXML
    private void chooseCell(Event event) {
        if (event.getSource() instanceof Button) {
            Button buttonPress = (Button) event.getSource();
            Integer colIndex = GridPane.getColumnIndex(buttonPress);
            Integer rowIndex = GridPane.getRowIndex(buttonPress);

            String coordinate = convertPositionToString(colIndex, rowIndex);
            System.out.println("Cella cliccata: " + coordinate);
            //TODO: Passare a View la risposta
        }
    }

    private String convertPositionToString(int colIndex, int rowIndex) {
        String coordinate = "";
        char firstChar = (char) (rowIndex + 65);
        char secondChar = (char) (colIndex + 49);
        coordinate = coordinate + firstChar + secondChar;
        return coordinate;
    }
}
