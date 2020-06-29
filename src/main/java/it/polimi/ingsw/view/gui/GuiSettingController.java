package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GodsCard;
import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Message;
import javafx.application.Platform;
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
import java.time.format.DateTimeFormatter;

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

    protected void handleStringMessage(String message) {
        GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");

        if (message.equalsIgnoreCase(Message.chooseNickname) ||
                message.equalsIgnoreCase(Message.chooseNicknameAgain)) {
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("nickname"))) {
                    node.setOpacity(1);
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
                    node.setOpacity(1);
                    node.setVisible(true);
                    node.setDisable(false);
                } else {
                    node.setOpacity(0.50);
                    node.setDisable(true);
                }
            }
        } else if (message.equalsIgnoreCase(Message.chooseNoPlayer) ||
                message.equalsIgnoreCase(Message.chooseNoPlayerAgain)) {
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("number"))) {
                    node.setOpacity(1);
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
        GridPane gridColor = (GridPane) settingScene.lookup("#gridColor");

        if ((currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST ||
                currentMove.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) &&
                currentMove.getCurrentPlayer().getNickname().equalsIgnoreCase(guiController.getPlayerOwnerNickname())) {

            gridColor.setOpacity(1.00);
            gridColor.setVisible(true);
            gridColor.setDisable(false);

            for (Node node : gridColor.getChildren()) {
                if (node instanceof Button &&
                        node.getStyleClass().stream().anyMatch(x -> x.contains("color")) &&
                        currentMove.getAvailableColor().stream().map(Color.PlayerColor::getColorAsString)
                                .anyMatch(x -> x.equalsIgnoreCase(node.getId().replace("buttonColor", "").toLowerCase())) &&
                        currentMove.getNextStartupMove() != StartupActions.PICK_LAST_COLOR) {
                    node.setOpacity(1.00);
                    node.setVisible(true);
                    node.setDisable(false);
                } else {
                    node.setOpacity(0.25);
                    node.setDisable(true);
                }
            }
        } else {
            gridColor.setOpacity(0.50);
            gridColor.setDisable(true);
        }
    }

    @FXML
    private void setNickname(ActionEvent event) {
        TextField textNickname = (TextField) settingScene.lookup("#nickname");

        guiController.handleResponse(textNickname.getText());
    }

    @FXML
    private void setBirthday(ActionEvent event) {
        DatePicker pickerBirthday = (DatePicker) settingScene.lookup("#dateBirthday");

        guiController.handleResponse(pickerBirthday.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @FXML
    private void setNumber(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button buttonClicked = (Button) event.getSource();

            guiController.handleResponse(buttonClicked.getText());
        }
    }

    @FXML
    private void setColor(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button buttonClicked = (Button) event.getSource();

            String color = buttonClicked.getId().replace("buttonColor", "").toLowerCase();
            guiController.handleResponse(color);
        }
    }

    protected void goToNextScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiGods.fxml"));
        Parent root = null;

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GuiGodsController guiGodsController = (GuiGodsController) fxmlLoader.getController();

        Scene godsScene = new Scene(root, 500, 700);

        guiGodsController.setScene(godsScene);
        guiController.setCurrentScene(godsScene);
        guiController.setGuiGodsController(guiGodsController);

        Platform.runLater(() -> {
            Stage stage = (Stage) settingScene.getWindow();
            stage.setResizable(false);
            stage.setScene(godsScene);
        });
    }

}
