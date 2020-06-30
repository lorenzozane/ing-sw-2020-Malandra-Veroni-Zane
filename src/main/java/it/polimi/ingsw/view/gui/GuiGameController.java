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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class GuiGameController {

    private GuiController guiController;
    private Scene mainScene;
    private UpdateTurnMessage currentMove;

    @FXML
    GridPane gridClickable;

    @FXML
    GridPane gridBoard;

    @FXML
    TextField txtNickname;

    @FXML
    TextField txtGod;

    @FXML
    TextField txtMove;

    @FXML
    TextField txtError;


    protected void setScene(Scene mainScene) {
        if (this.mainScene == null) {
            this.mainScene = mainScene;

            gridClickable.setDisable(true);
            for (Node node : gridClickable.getChildren()) {
                if (node instanceof Button)
                    node.setDisable(false);
            }
        }
    }

    protected Scene getMainScene() {
        return mainScene;
    }

    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        guiController.setGuiGameController(this);
    }

    protected void showMessage(UpdateTurnMessage currentMove, String messageToShow) {
        this.currentMove = currentMove;

        Platform.runLater(() -> refreshBoard(currentMove.getBoardCopy()));

        if (currentMove.getCurrentPlayer().getNickname().equalsIgnoreCase(guiController.getPlayerOwnerNickname())) {
            gridClickable.setDisable(false);
        } else {
            gridClickable.setDisable(true);
        }

        Platform.runLater(() -> {
            txtNickname.setText(currentMove.getCurrentPlayer().getNickname());
            txtGod.setText(currentMove.getCurrentPlayer().getPlayerCard().toString());
            txtMove.setText(messageToShow);
        });
    }

    @FXML
    private void commandButton(Event event) {
        if (event.getSource() instanceof Button) {
            Button buttonPress = (Button) event.getSource();
            String command = buttonPress.getId().replace("button", "").toLowerCase();

            guiController.handleResponse(command);
        }
    }

    @FXML
    private void chooseCell(Event event) {
        if (event.getSource() instanceof Button) {
            gridClickable.setDisable(true);

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

            guiController.handleResponse(coordinate);
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

        int coordinateX = slot.getSlotPosition().getCoordinateX();
        int coordinateY = slot.getSlotPosition().getCoordinateY();


        for (Node buttonNode : gridBoard.getChildren()) {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;
                if (GridPane.getColumnIndex(button) != null &&
                        GridPane.getRowIndex(button) != null &&
                        (int) GridPane.getColumnIndex(button) == coordinateX &&
                        (int) GridPane.getRowIndex(button) == coordinateY) {

                    if (button.getStyleClass().stream().anyMatch(x -> x.contains("level"))) {
                        if ((slot.getBuildingsStatus().get(0) == null && button.isVisible()) ||
                                slot.getBuildingsStatus().get(0) != null && !button.isVisible())
                            return true;
//                      TODO: Platform.runLater(() -> refreshSlot(slot));
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
        }

        return false;
    }

    private void refreshSlot(Slot slot) {
        for (Node buttonNode : gridBoard.getChildren()) {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;
                if (GridPane.getColumnIndex(button) != null &&
                        GridPane.getRowIndex(button) != null &&
                        GridPane.getColumnIndex(button) == slot.getSlotPosition().getCoordinateX() &&
                        GridPane.getRowIndex(button) == slot.getSlotPosition().getCoordinateY()) {
                    if (button.getStyleClass().stream().anyMatch(x -> x.contains("level"))) {
                        if (slot.getBuildingsStatus().get(2) != null) {
                            button.setVisible(true);
                            button.setStyle("-fx-background-image: url('/images/image-block1-2-3.jpg')");
                        } else if (slot.getBuildingsStatus().get(1) != null) {
                            button.setVisible(true);
                            button.setStyle("-fx-background-image: url('/images/image-block1-2.jpg')");
                        } else if (slot.getBuildingsStatus().get(0) != null) {
                            button.setVisible(true);
                            button.setStyle("-fx-background-image: url('/images/image-block1.jpg')");
                        } else {
                            button.setVisible(false);
                        }
//                    if (slot.getBuildingsStatus().get(0) == null)
//                        button.setVisible(false);
//                    else
//                        button.setVisible(true);
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

                            if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.RED) {
                                button.setStyle("-fx-background-image: url('/images/worker_red.jpg')");
                                button.setStyle("-fx-background-size: 100% 100%");
                            } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.YELLOW) {
                                button.setStyle("-fx-background-image: url('/images/worker_yellow.jpg')");
                                button.setStyle("-fx-background-size: 100% 100%");
                            } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.CYAN) {
                                button.setStyle("-fx-background-image: url('/images/worker_cyan.jpg')");
                                button.setStyle("-fx-background-size: 100% 100%");
                            }
                        }
                    }
                }
            }
        }
    }
}
