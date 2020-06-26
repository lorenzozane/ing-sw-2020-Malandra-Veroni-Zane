package it.polimi.ingsw.view.gui;

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
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene mainScene = new Scene(root, 800, 600);
            SplitPane mainSplitPane = (SplitPane) mainScene.lookup("#mainSplitPane");
//        SplitPane.Divider divider = mainSplitPane.getDividers().get(0);
//        AnchorPane leftMainAnchor = (AnchorPane) mainSplitPane.lookup("#leftMainAnchor");
            AnchorPane leftMainAnchor = (AnchorPane) mainSplitPane.getItems().get(0);
            leftMainAnchor.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));
            leftMainAnchor.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.7));
//        leftMainAnchor.setMinWidth(scene.getHeight());
//        leftMainAnchor.setMaxWidth(scene.getHeight());
//        double leftComponentSize = 100.0;
//        mainSplitPane.getItems().filtered().forEach(div ->  div.setMouseTransparent(true) );
//        leftComponent.setMinWidth(leftComponentSize);
//        leftComponent.setMaxWidth(leftComponentSize);
//        VBox rightComponent = new VBox();
//        rightComponent.setMinWidth(leftComponentSize);
//        rightComponent.setMaxWidth(leftComponentSize);
//        mainSplitPane.getItems().addAll(leftComponent, rightComponent);
//        mainSplitPane.setDividerPositions(leftComponentSize / scene.getWidth());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainScene);
            stage.minWidthProperty().bind(mainScene.heightProperty().divide(0.7));
            stage.minHeightProperty().bind(mainScene.widthProperty().multiply(0.7));
        }
    }
}
