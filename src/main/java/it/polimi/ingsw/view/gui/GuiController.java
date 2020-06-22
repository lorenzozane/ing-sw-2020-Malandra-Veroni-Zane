package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


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
    Button workerRed01;


   @FXML
   private void setVisible00(ActionEvent event) {
       level100.setVisible(true);
       level200.setVisible(true);
       level300.setVisible(true);
       dome00.setVisible(true);
   }

   @FXML
   private void setVisible01(ActionEvent event){
       level101.setVisible(true);
       level201.setVisible(true);
   }

    public void bottonPress(Event event){

        System.out.println("Botton pressed");
    }
}
