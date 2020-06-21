package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Gui extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiStyle.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(scene.heightProperty().divide(0.7));
        primaryStage.minHeightProperty().bind(scene.widthProperty().multiply(0.7));

        SplitPane mainSplitPane = (SplitPane) scene.lookup("#mainSplitPane");
//        AnchorPane leftMainAnchor = (AnchorPane) mainSplitPane.lookup("#leftMainAnchor");
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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
