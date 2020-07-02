package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.scene.Scene;

public class GuiController {

    private static GuiController instance = null;

    protected Gui gui;
    private GuiGameController guiGameController;
    private GuiGodsController guiGodsController;
    private GuiSettingController guiSettingController;
    private GuiNetworkController guiNetworkController;
    private Scene currentScene = null;
    private View viewOwner = null;
    private String playerOwnerNickname = null;
    private UpdateTurnMessage currentMove;

    public static GuiController getInstance() {
        if (instance == null)
            instance = new GuiController();

        return instance;
    }

    public void setViewOwner(View viewOwner) {
        if (this.viewOwner == null)
            this.viewOwner = viewOwner;
    }

    protected String getPlayerOwnerNickname() {
        return playerOwnerNickname;
    }

    protected void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    /**
     * Show string messages to the player.
     *
     * @param message The string message to be shown.
     */
    public void showStringMessage(String message) {
        if (guiGameController != null)
            guiGameController.showErrorMessage(currentMove, message);
        else if (guiSettingController != null)
            guiSettingController.handleStringMessage(message);

        if (message.equalsIgnoreCase(Message.birthday))
            playerOwnerNickname = viewOwner.getPlayerOwnerNickname();
    }

    /**
     * Sort the message to be shown in the graphic user interface.
     *
     * @param currentMove   The current move from which to take information to show.
     * @param messageToShow The message to be shown.
     */
    public void showMessage(UpdateTurnMessage currentMove, String messageToShow) {
        this.currentMove = currentMove;

        if ((currentMove.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST) ||
                (currentMove.getNextStartupMove() == StartupActions.PLACE_WORKER_1))
            try {
//                new Thread(() -> Platform.runLater(() -> changeToNextScene(currentMove))).start();
//                Platform.runLater(() -> changeToNextScene(currentMove));
                changeToNextScene(currentMove);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        if (currentMove.isStartupPhase() &&
                (currentMove.getNextStartupMove() != StartupActions.PLACE_WORKER_1 &&
                        currentMove.getNextStartupMove() != StartupActions.PLACE_WORKER_2)) {
            if (currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST ||
                    currentMove.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) {
                guiSettingController.showMessage(currentMove);
            } else {
                guiGodsController.showMessage(currentMove);
            }
        } else {
            guiGameController.showMessage(currentMove, messageToShow);
        }
    }

    /**
     * Show the error messages to the player.
     *
     * @param currentMoveError The current move from which to take information to show.
     * @param errorToShow      The error message to be shown.
     */
    public void showErrorMessage(UpdateTurnMessage currentMoveError, String errorToShow) {
        if (!currentMoveError.isStartupPhase() ||
                (currentMoveError.getNextStartupMove() == StartupActions.PLACE_WORKER_1 ||
                        currentMoveError.getNextStartupMove() == StartupActions.PLACE_WORKER_2)) {
            guiGameController.showErrorMessage(currentMoveError, errorToShow);
        }
    }

    /**
     * Handle the player's response to a specific move request.
     *
     * @param response Player's response.
     */
    protected void handleResponse(String response) {
        viewOwner.handleResponse(response);
    }

    /**
     * Change the current scene of the graphic user interface to the next scene.
     *
     * @param message The current move, useful to handle the scene change.
     */
    private void changeToNextScene(UpdateTurnMessage message) {
        try {
            if (message.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST &&
                    (guiGodsController == null ||
                            guiGodsController.getGodsScene() == null ||
                            currentScene != guiGodsController.getGodsScene())) {
                guiSettingController.goToNextScene();
            } else if (message.getNextStartupMove() == StartupActions.PLACE_WORKER_1 &&
                    (guiGameController == null ||
                            guiGameController.getMainScene() == null ||
                            currentScene != guiGameController.getMainScene())) {
                guiGodsController.goToNextScene();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    protected void setGuiGameController(GuiGameController guiGameController) {
        this.guiGameController = guiGameController;
    }

    protected void setGuiGodsController(GuiGodsController guiGodsController) {
        this.guiGodsController = guiGodsController;
    }

    protected void setGuiSettingController(GuiSettingController guiSettingController) {
        this.guiSettingController = guiSettingController;
    }
}
