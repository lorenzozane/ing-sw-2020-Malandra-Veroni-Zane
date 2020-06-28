package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class GuiGameController {

    private GuiController guiController;
    private Scene mainScene;
    private UpdateTurnMessage currentMove;

    protected void setScene(Scene mainScene) {
        if (this.mainScene == null)
            this.mainScene = mainScene;
    }

    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        guiController.setGuiGameController(this);
    }

    @FXML
    Button level100;

    @FXML
    Button level200;

    @FXML
    Button level300;

    @FXML
    Button dome00;

    @FXML
    Button level101;

    @FXML
    Button level201;


    @FXML
    private void setVisible00(ActionEvent event) {
        level100.setVisible(true);
        level200.setVisible(true);
        level300.setVisible(true);
        dome00.setVisible(true);
    }

    @FXML
    private void setVisible01(ActionEvent event) {
        level101.setVisible(true);
        level201.setVisible(true);
    }

    protected void showMessage(UpdateTurnMessage currentMove) {
        this.currentMove = currentMove;
    }

    @FXML
    private void commandButton(Event event) {
        if (event.getSource() instanceof Button) {
            Button buttonPress = (Button) event.getSource();
            String command = buttonPress.getId().replace("button", "").toLowerCase();
            //TODO: Passare comando alla view
        }
    }

    @FXML
    private void chooseCell(Event event) {
        if (event.getSource() instanceof Button) {
            Button buttonPress = (Button) event.getSource();
            Integer colIndex = GridPane.getColumnIndex(buttonPress);
            Integer rowIndex = GridPane.getRowIndex(buttonPress);

            String coordinate = convertPositionToString(colIndex, rowIndex);
//            System.out.println("Cella cliccata: " + coordinate);

            Scene mainScene = buttonPress.getScene();
            CheckBox checkBoxDome = (CheckBox) mainScene.lookup("#buttonDome");
            if (checkBoxDome.isSelected() &&
                    (currentMove != null && currentMove.getNextMove() == Actions.BUILD_DOME_ANY_LEVEL)) {
                coordinate = coordinate.concat(" dome");
            }
            System.out.println(coordinate);
            //TODO: Passare a View la risposta
        }
    }

    private String convertPositionToString(int colIndex, int rowIndex) {
        String coordinate = "";
        char firstChar = (char) (rowIndex + 65);
        char secondChar = (char) (colIndex + 49);
        coordinate = coordinate + firstChar + secondChar;
        return coordinate;
    }

    public void refreshBoard(Board board) {
        ArrayList<Slot> slotsToRefresh = new ArrayList<>();

        for (int i = 0; i < board.getBoardDimension(); i++) {
            for (int j = 0; j < board.getBoardDimension(); j++) {
                if (needRefreshSlot(board.getSlot(new Position(i, j))))
                    slotsToRefresh.add(board.getSlot(new Position(i, j)));
            }
        }

        for (Slot slot : slotsToRefresh) {
            refreshSlot(slot);
        }
    }

    private boolean needRefreshSlot(Slot slot) {
        GridPane gridBoard = (GridPane) mainScene.lookup("#gridBoard");

        for (Node buttonNode : gridBoard.getChildren()) {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;

                if (button.getStyleClass().stream().anyMatch(x -> x.contains("level1"))) {
                    if ((slot.getBuildingsStatus().get(0) == null && button.isVisible()) ||
                            slot.getBuildingsStatus().get(0) != null && !button.isVisible())
                        return true;
//                      TODO: Platform.runLater(() -> refreshSlot(slot));
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("level2"))) {
                    if ((slot.getBuildingsStatus().get(1) == null && button.isVisible()) ||
                            slot.getBuildingsStatus().get(1) != null && !button.isVisible())
                        return true;
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("level3"))) {
                    if ((slot.getBuildingsStatus().get(2) == null && button.isVisible()) ||
                            slot.getBuildingsStatus().get(2) != null && !button.isVisible())
                        return true;
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("dome"))) {
                    if ((slot.getBuildingsStatus().get(3) == null && button.isVisible()) ||
                            slot.getBuildingsStatus().get(3) != null && !button.isVisible())
                        return true;
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("worker"))) {
                    if ((slot.getWorkerInSlot() == null && button.isVisible()) ||
                            slot.getWorkerInSlot() != null && !button.isVisible())
                        return true;
                    if (slot.getWorkerInSlot() != null) {
                        if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.RED) {
                            if (!button.getBackground().getImages().get(0).getImage().getUrl().contains("red"))
                                return true;
                        } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.YELLOW) {
                            if (!button.getBackground().getImages().get(0).getImage().getUrl().contains("yellow"))
                                return true;
                        } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.CYAN) {
                            if (!button.getBackground().getImages().get(0).getImage().getUrl().contains("cyan"))
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void refreshSlot(Slot slot) {
        GridPane gridBoard = (GridPane) mainScene.lookup("#gridBoard");

        for (Node buttonNode : gridBoard.getChildren()) {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;

                if (button.getStyleClass().stream().anyMatch(x -> x.contains("level1"))) {
                    if (slot.getBuildingsStatus().get(0) == null)
                        button.setVisible(false);
                    else
                        button.setVisible(true);
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("level2"))) {
                    if (slot.getBuildingsStatus().get(1) == null)
                        button.setVisible(false);
                    else
                        button.setVisible(true);
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("level3"))) {
                    if (slot.getBuildingsStatus().get(2) == null)
                        button.setVisible(false);
                    else
                        button.setVisible(true);
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("dome"))) {
                    if (slot.getBuildingsStatus().get(3) == null)
                        button.setVisible(false);
                    else
                        button.setVisible(true);
                } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("worker"))) {
                    if (slot.getWorkerInSlot() == null)
                        button.setVisible(false);
                    else {
                        button.setVisible(true);

                        if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.RED)
                            button.setStyle("-fx-background-image: url('/images/worker_red.jpg')");
                        else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.YELLOW)
                            button.setStyle("-fx-background-image: url('/images/worker_yellow.jpg')");
                        else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.CYAN)
                            button.setStyle("-fx-background-image: url('/images/worker_cyan.jpg')");
                    }
                }
            }
        }
    }
}
