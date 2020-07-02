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

    /**
     * Called during the GuiSettingController initialization.
     */
    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        guiController.setGuiSettingController(this);
    }

    @FXML
    Label errorNickname;

    protected void setScene(Scene settingScene) {
        if (this.settingScene == null) {
            this.settingScene = settingScene;

            GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");
            for (Node node : gridSettings.getChildren())
                node.setDisable(true);
        }
    }

    /**
     * Handle the string messages that contains request for the player.
     *
     * @param message The received string message.
     */
    protected void handleStringMessage(String message) {
        GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");

        if (message.equalsIgnoreCase(Message.chooseNickname)) {
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
        }
        else if(message.equalsIgnoreCase(Message.chooseNicknameAgain)) {
            errorNickname.setVisible(true);
        }
        else if (message.equalsIgnoreCase(Message.birthday) ||
                message.equalsIgnoreCase(Message.birthdayAgain)) {
            errorNickname.setVisible(false);
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

    /**
     * Handle the UpdateTurnMessages that contains request for the player.
     *
     * @param currentMove The received UpdateTurnMessage.
     */
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

        GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");
        for (Node node : gridSettings.getChildren()) {
            if (node.getStyleClass().stream().anyMatch(x -> x.contains("number"))) {
                node.setDisable(true);
            }
        }
    }

    /**
     * Take the player input of the player and use it to set his nickname.
     *
     * @param event Event generated by a send button.
     */
    @FXML
    private void setNickname(ActionEvent event) {
        TextField textNickname = (TextField) settingScene.lookup("#nickname");

        guiController.handleResponse(textNickname.getText());
    }

    /**
     * Take the player input of the player and use it to set his birthday.
     *
     * @param event Event generated by a send button.
     */
    @FXML
    private void setBirthday(ActionEvent event) {
        try {
            DatePicker pickerBirthday = (DatePicker) settingScene.lookup("#dateBirthday");

            guiController.handleResponse(pickerBirthday.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("birthday"))) {
                    node.setOpacity(0.5);
                    node.setDisable(false);
                }
            }
        } catch (Exception ex) {
            System.out.println("Invalid date insert.");
        }
    }

    /**
     * Take the player input of the player and use it to set the player number of the game.
     *
     * @param event Event generated by a send button.
     */
    @FXML
    private void setNumber(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button buttonClicked = (Button) event.getSource();

            guiController.handleResponse(buttonClicked.getText());
            GridPane gridSettings = (GridPane) settingScene.lookup("#gridSettings");
            for (Node node : gridSettings.getChildren()) {
                if (node.getStyleClass().stream().anyMatch(x -> x.contains("number"))) {
                    node.setOpacity(0.5);
                    node.setDisable(false);
                }
            }
        }
    }

    /**
     * Take the player input of the player and use it to set his chosen color.
     *
     * @param event Event generated by a send button.
     */
    @FXML
    private void setColor(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button buttonClicked = (Button) event.getSource();

            String color = buttonClicked.getId().replace("buttonColor", "").toLowerCase();
            guiController.handleResponse(color);
        }
    }

    /**
     * Change the current scene of the graphic user interface to the next scene.
     */
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

        Scene godsScene = new Scene(root, 550, 750);

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
