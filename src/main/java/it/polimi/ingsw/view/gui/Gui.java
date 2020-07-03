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

/**
 * Gui Main instance. Startup the graphical user interface.
 */
public class Gui extends Application {

    protected static Client client;
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Create the main stage for javafx and set the first scene.
     * @param primaryStage is the main stage
     * @throws Exception thrown when fxmlLoader fail opening a fxml file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        GuiController guiController = GuiController.getInstance();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Gui.class.getResource("/NetworkGui.fxml"));
        Parent root = fxmlLoader.load();

        GuiNetworkController guiNetworkController = (GuiNetworkController) fxmlLoader.getController();
        Scene networkScene = new Scene(root, 350, 450);

        primaryStage.setTitle("SANTORINI");
        primaryStage.setScene(networkScene);
        primaryStage.setResizable(false);

        guiNetworkController.setScene(networkScene);
        guiController.setCurrentScene(networkScene);

        primaryStage.show();
    }

    protected static void setClient(Scene networkScene, Client client) throws IOException {
        if (Gui.client == null) {
            Gui.client = client;

            startupGame(networkScene);
        }

    }

    /**
     * Method that set all Observer/Observable and set the settingScene

     * @param networkScene The network scene.
     * @throws IOException Exception thrown if there are problems opening the fxml file.
     */
    private static void startupGame(Scene networkScene) throws IOException {
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
        fxmlLoader.setLocation(Gui.class.getResource("/GuiSetting.fxml"));
        Parent root = fxmlLoader.load();
        GuiSettingController guiSettingController = (GuiSettingController) fxmlLoader.getController();
        Scene settingScene = new Scene(root, 400, 700);

        Platform.runLater(() -> {
            Stage stage = (Stage) networkScene.getWindow();
            stage.setResizable(false);
            stage.setScene(settingScene);
        });
//        primaryStage.setTitle("SANTORINI");
//        primaryStage.setScene(settingScene);
//        primaryStage.setResizable(false);


        guiSettingController.setScene(settingScene);
        guiController.setCurrentScene(settingScene);

        guiController.setGuiSettingController(guiSettingController);
//        new Thread(primaryStage::show).start();
//        Platform.setImplicitExit(false);
//        new Thread(() -> Platform.runLater(primaryStage::show)).start();

//        client.run();
        new Thread(client::run).start();

//        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

