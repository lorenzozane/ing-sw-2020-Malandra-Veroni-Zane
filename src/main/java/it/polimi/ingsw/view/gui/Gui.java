package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    private static Client client;
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

        Thread thread = new Thread(() -> client.run());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GuiSetting.fxml"));
        GuiSettingController guiSettingController = (GuiSettingController) fxmlLoader.getController();
        Parent root = fxmlLoader.load();
        Scene settingScene = new Scene(root, 400, 700);
        guiSettingController.setScene(settingScene);

        primaryStage.setTitle("SANTORINI");
        primaryStage.setScene(settingScene);
        primaryStage.setResizable(false);


        guiController.setCurrentScene(settingScene);

        primaryStage.show();

    }

    public static void main(String[] args) throws IOException {
        if(args.length < 2)
            client = new Client("127.0.0.1", 12345);
        else
            client = new Client(args[0], Integer.parseInt(args[1]));

        launch(args);
    }
}

