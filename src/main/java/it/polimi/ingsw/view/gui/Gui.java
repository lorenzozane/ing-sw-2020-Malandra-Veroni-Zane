package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    protected static Client client;
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiController guiController = GuiController.getInstance();
        View view = new View(guiController);
        client.setChosenUserInterface(view, Client.UserInterface.GUI);

        client.addUpdateTurnMessageObserver(view.getUpdateTurnMessageReceiver());
        client.addStringObserver(view.getStringReceiver());

        view.addPlayerMoveObserver(client.getPlayerMoveReceiver());
        view.addPlayerMoveStartupObserver(client.getPlayerMoveStartupReceiver());
        view.addStringObserver(client.getStringReceiver());

//        Thread thread = new Thread(() -> client.run());
//        try {
//            client.run();
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiSetting.fxml"));
        Parent root = fxmlLoader.load();
        GuiSettingController guiSettingController = (GuiSettingController) fxmlLoader.getController();
        Scene settingScene = new Scene(root, 400, 700);

        primaryStage.setTitle("SANTORINI");
        primaryStage.setScene(settingScene);
        primaryStage.setResizable(false);


        guiSettingController.setScene(settingScene);
        guiController.setCurrentScene(settingScene);

        guiController.setGuiSettingController(guiSettingController);
//        new Thread(primaryStage::show).start();
//        Platform.setImplicitExit(false);
//        new Thread(() -> Platform.runLater(primaryStage::show)).start();

//        client.run();
        new Thread(client::run).start();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

