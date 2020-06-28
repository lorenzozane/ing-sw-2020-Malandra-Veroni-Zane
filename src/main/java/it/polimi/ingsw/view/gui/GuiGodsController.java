package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.UpdateTurnMessage;
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
import javafx.stage.Stage;

import java.io.IOException;

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
        if (this.godsScene == null)
            this.godsScene = godsScene;
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
    }

    @FXML
    private void godsClick(ActionEvent event) {
        ((Button) event.getSource()).setDisable(true);
        ((Button) event.getSource()).setOpacity(0.4);
        godsCounterPlus(event);
    }

    private int godsCounter;

    private void godsCounterPlus(ActionEvent event) {
        this.godsCounter++;
        //TODO:da modificare
        if(this.godsCounter == 2) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/GuiStyle.fxml"));
            GuiGameController guiGameController = (GuiGameController) fxmlLoader.getController();

            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene mainScene = new Scene(root, 800, 600);
            guiGameController.setScene(mainScene);
            GuiController.getInstance().setCurrentScene(mainScene);

            SplitPane mainSplitPane = (SplitPane) mainScene.lookup("#mainSplitPane");

            AnchorPane leftMainAnchor = (AnchorPane) mainSplitPane.getItems().get(0);
            leftMainAnchor.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));
            leftMainAnchor.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainScene);

            stage.setMinHeight(600);
            DoubleBinding binding = stage.heightProperty().divide(0.7);
            stage.minWidthProperty().bind(binding);
            stage.maxWidthProperty().bind(binding);

            stage.setResizable(true);
        }
    }
}
