package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
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
        if (this.settingScene == null) {
            this.settingScene = settingScene;

            GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");
            for (Node node : gridSettings.getChildren())
                node.setDisable(true);
        }
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
        GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");

        if (message.equalsIgnoreCase(Message.chooseNickname) ||
                message.equalsIgnoreCase(Message.chooseNicknameAgain)) {
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("nickname"))) {
                    node.setOpacity(1.00);
                    node.setVisible(true);
                    node.setDisable(false);
                } else {
                    node.setOpacity(0.50);
                    node.setDisable(true);
                }
            }
        } else if (message.equalsIgnoreCase(Message.birthday) ||
                message.equalsIgnoreCase(Message.birthdayAgain)) {
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("birthday"))) {
                    node.setOpacity(1.00);
                    node.setVisible(true);
                    node.setDisable(false);
                } else {
                    node.setOpacity(0.50);
                    node.setDisable(true);
                }
            }
        }
    }

    protected void showMessage(UpdateTurnMessage currentMove) {
        this.currentMove = currentMove;
        GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");

        if (currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST ||
        currentMove.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) {
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("color"))) {
                    node.setOpacity(1.00);
                    node.setVisible(true);
                    node.setDisable(false);
                } else {
                    node.setOpacity(0.50);
                    node.setDisable(true);
                }
            }
        }
    }

    @FXML
    private void setNickname(ActionEvent event) {

    }

    @FXML
    private void setBirthday(ActionEvent event) {

    }

    @FXML
    private void setColor(ActionEvent event) {

    }

    @FXML
    private void setNextSceneSetting(ActionEvent event) {

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
