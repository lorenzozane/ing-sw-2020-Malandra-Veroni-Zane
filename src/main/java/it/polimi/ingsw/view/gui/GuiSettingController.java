package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiSettingController {

    private GuiController guiController;
    private Scene settingScene;
    private UpdateTurnMessage currentMove;

    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        guiController.setGuiSettingController(this);
    }

    protected void setScene(Scene settingScene) {
        if (this.settingScene == null)
            this.settingScene = settingScene;
    }

    @FXML
    Button buttonNickname;

    @FXML
    Label labelNickname;

    @FXML
    TextField nickname;

    @FXML
    Label errorNickname;

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

    protected void handleStringMessage(String message) {
        if (message.equalsIgnoreCase(Message.chooseNickname)) {
            GridPane gridSettings = (GridPane)settingScene.lookup("#gridSettings");
            for (Node node : gridSettings.getChildren()) {

            }
        }
    }

    protected void showMessage(UpdateTurnMessage currentMove) {
        this.currentMove = currentMove;
    }

    @FXML
    private void setNickname(ActionEvent event){
      //  if (nickname già inserito){
        //    errorNickname.setVisible(true);
      //  }
      //  else{
            buttonNickname.setVisible(false);
            labelNickname.setVisible(false);
            nickname.setVisible(false);
            labelBirthday.setVisible(true);
            dateBirthday.setVisible(true);
            buttonBirthday.setVisible(true);
      //  }
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
        GuiGodsController guiGodsController = (GuiGodsController) fxmlLoader.getController();
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
        guiGodsController.setScene(godsScene);
        GuiController.getInstance().setCurrentScene(godsScene);
    }

}
