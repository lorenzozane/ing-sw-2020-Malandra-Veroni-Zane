package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.GodsCard;
import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.UpdateTurnMessage;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;

public class GuiGodsController {

    private GuiController guiController;
    private Scene godsScene;
    private UpdateTurnMessage currentMove;

    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        guiController.setGuiGodsController(this);
    }

    protected void setScene(Scene godsScene) {
        if (this.godsScene == null) {
            this.godsScene = godsScene;

            GridPane gridGods = (GridPane) godsScene.lookup("#gridGods");
            for (Node node : gridGods.getChildren())
                if (node instanceof Button)
                    node.setDisable(true);
        }
    }

    protected Scene getGodsScene() {
        return godsScene;
    }

    @FXML
    Button apollo;

    @FXML
    Button artemis;

    @FXML
    Button athena;

    @FXML
    Button atlas;

    @FXML
    Button demeter;

    @FXML
    Button hephaestus;

    @FXML
    Button minotaur;

    @FXML
    Button pan;

    @FXML
    Button prometheus;

    protected void showMessage(UpdateTurnMessage currentMove) {
        this.currentMove = currentMove;
        GridPane gridGods = (GridPane) godsScene.lookup("#gridGods");

        if ((currentMove.getNextStartupMove() == TurnEvents.StartupActions.CHOOSE_CARD_REQUEST ||
                currentMove.getNextStartupMove() == TurnEvents.StartupActions.PICK_UP_CARD_REQUEST ||
                currentMove.getNextStartupMove() == TurnEvents.StartupActions.PICK_LAST_CARD) &&
                currentMove.getCurrentPlayer().getNickname().equalsIgnoreCase(guiController.getPlayerOwnerNickname())) {
            for (Node node : gridGods.getChildren()) {
                if (node instanceof Button &&
                        currentMove.getAvailableCards().stream().map(GodsCard::getCardName)
                                .anyMatch(x -> x.equalsIgnoreCase(node.getId())) &&
                        currentMove.getNextStartupMove() != TurnEvents.StartupActions.PICK_LAST_CARD) {
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
    private void godsClick(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button godButton = (Button) event.getSource();

            godButton.setDisable(true);
            godButton.setOpacity(0.5);

            guiController.handleResponse(godButton.getId());
        }
    }

    protected void goToNextScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiStyle.fxml"));
        Parent root = null;

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GuiGameController guiGameController = (GuiGameController) fxmlLoader.getController();

        Scene mainScene = new Scene(root, 800, 600);

        guiGameController.setScene(mainScene);
        guiController.setCurrentScene(mainScene);
        guiController.setGuiGameController(guiGameController);

        SplitPane mainSplitPane = (SplitPane) mainScene.lookup("#mainSplitPane");
        AnchorPane leftMainAnchor = (AnchorPane) mainSplitPane.getItems().get(0);
        leftMainAnchor.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));
        leftMainAnchor.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));

        Platform.runLater(() -> {
            Stage stage = (Stage) godsScene.getWindow();
            stage.setResizable(true);
            stage.setScene(mainScene);

            stage.setMinHeight(600);
            DoubleBinding binding = stage.heightProperty().divide(0.7);
            stage.minWidthProperty().bind(binding);
            stage.maxWidthProperty().bind(binding);
        });
    }

}
