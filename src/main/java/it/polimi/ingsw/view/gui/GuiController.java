package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.view.View;
import javafx.scene.Scene;

public class GuiController {

    private static GuiController instance = null;

    private GuiGameController guiGameController;
    private GuiGodsController guiGodsController;
    private GuiSettingController guiSettingController;

    private Scene currentScene = null;
    private View viewOwner = null;
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

    protected void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void showStringMessage(String message) {
        guiSettingController.handleStringMessage(message);
    }

    public void showMessage(UpdateTurnMessage currentMove) {
        this.currentMove = currentMove;

        if (currentMove.isStartupPhase() &&
                (currentMove.getNextStartupMove() != StartupActions.PLACE_WORKER_1 ||
                        currentMove.getNextStartupMove() != StartupActions.PLACE_WORKER_2)) {
            if (currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST ||
                    currentMove.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) {
                guiSettingController.showMessage(currentMove);
            } else {
                guiGodsController.showMessage(currentMove);
            }
        } else {
            guiGameController.showMessage(currentMove);
        }
    }

    public void showErrorMessage(UpdateTurnMessage currentMoveError) {

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
