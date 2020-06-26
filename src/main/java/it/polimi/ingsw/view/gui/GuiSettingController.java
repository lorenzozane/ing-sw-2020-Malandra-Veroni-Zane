package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiSettingController {

    @FXML
    Button buttonNickname;

    @FXML
    Label labelNickname;

    @FXML
    TextField nickname;

    @FXML
    Label labelBirthday;

    @FXML
    DatePicker dateBirthday;

    @FXML
    Button buttonBirthday;

    @FXML
    Label labelPlayers;

    @FXML
    Button buttonPlayer2;

    @FXML
    Button buttonPlayer3;

    @FXML
    Button buttonPlayer;

    @FXML
    Label labelColor;

    @FXML
    Button buttonColorRed;

    @FXML
    Button buttonColorCyan;

    @FXML
    Button buttonColorYellow;

    @FXML
    Button nextSceneSetting;

    @FXML
    private void setNickname(ActionEvent event){
        buttonNickname.setVisible(false);
        labelNickname.setVisible(false);
        nickname.setVisible(false);
        labelBirthday.setVisible(true);
        dateBirthday.setVisible(true);
        buttonBirthday.setVisible(true);
    }

    @FXML
    private void setBirthday(ActionEvent event){
        labelBirthday.setVisible(false);
        dateBirthday.setVisible(false);
        buttonBirthday.setVisible(false);
        labelPlayers.setVisible(true);
        buttonPlayer2.setVisible(true);
        buttonPlayer3.setVisible(true);
        buttonPlayer.setVisible(true);
    }

    @FXML
    private void setColor(ActionEvent event){
        labelPlayers.setVisible(false);
        buttonPlayer2.setVisible(false);
        buttonPlayer3.setVisible(false);
        buttonPlayer.setVisible(false);
        labelColor.setVisible(true);
        buttonColorCyan.setVisible(true);
        buttonColorRed.setVisible(true);
        buttonColorYellow.setVisible(true);
        nextSceneSetting.setVisible(true);
    }


    @FXML
    private void setNextSceneSetting(ActionEvent event){

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiGods.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene godsScene = new Scene(root, 500, 700);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(godsScene);
    }

}
