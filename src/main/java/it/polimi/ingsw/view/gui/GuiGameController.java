package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.view.ViewMessage;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;


public class GuiGameController {

    private GuiController guiController;
    private Scene mainScene;
    private UpdateTurnMessage currentMove;
    private boolean keepShowingError = false;

    @FXML
    GridPane gridClickable;

    @FXML
    GridPane gridBoard;

    @FXML
    Label txtNickname;

    @FXML
    Label txtGod;

    @FXML
    Label txtMove;

    @FXML
    Label txtError;


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

        if(messageToShow.equals(ViewMessage.winner)){
            try {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("YOU WIN");
                    alert.setContentText("Congratulation for your game!\n" + "Try a new GAME or QUIT");

                    alert.showAndWait();
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        if(messageToShow.equals(ViewMessage.lose) || messageToShow.contains(ViewMessage.loseOther) || messageToShow.contains(ViewMessage.winOthers)){
            try {
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText("YOU LOSE");
                    alert.setContentText(messageToShow + "\nTry a new GAME or QUIT");

                    alert.showAndWait();
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        if (currentMove.getCurrentPlayer().getNickname().equalsIgnoreCase(guiController.getPlayerOwnerNickname())) {
            gridClickable.setDisable(false);
        } else {
            gridClickable.setDisable(true);
        }

        Platform.runLater(() -> {
            txtNickname.setText(currentMove.getCurrentPlayer().getNickname());
            txtGod.setText(currentMove.getCurrentPlayer().getPlayerCard().toString());
            txtMove.setText(messageToShow);
            if (keepShowingError)
                keepShowingError = false;
            else
                txtError.setText("");
        });
    }

    protected void showErrorMessage(UpdateTurnMessage currentMove, String errorToShow) {
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
            txtError.setText(errorToShow);
        });

        keepShowingError = true;
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
        Integer coordinateX = slot.getSlotPosition().getCoordinateX();
        Integer coordinateY = slot.getSlotPosition().getCoordinateY();

        for (Node buttonNode : gridBoard.getChildren()) {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;
                String str = button.getId();
                str = str.replaceAll("[^\\d.]", "");
                if (coordinateX.equals(Integer.parseInt(String.valueOf(str.charAt(1)))) &&
                        coordinateY.equals(Integer.parseInt(String.valueOf(str.charAt(0))))) {

                    if (button.getStyleClass().stream().anyMatch(x -> x.contains("level"))) {
                        if ((slot.getBuildingsStatus().get(0) == null && button.isVisible()) ||
                                slot.getBuildingsStatus().get(0) != null && !button.isVisible())
                            return true;
                        if (slot.getBuildingsStatus().get(0) != null &&
                                !(button.getBackground().getImages().get(0).getImage().getUrl()).contains("block1"))
                            return true;
                        else if (slot.getBuildingsStatus().get(1) != null &&
                                !button.getBackground().getImages().get(0).getImage().getUrl().contains("block1-2"))
                            return true;
                        else if (slot.getBuildingsStatus().get(2) != null &&
                                !button.getBackground().getImages().get(0).getImage().getUrl().contains("block1-2-3"))
                            return true;

                        if (slot.getBuildingsStatus().get(0) == null &&
                                !button.getBackground().getImages().isEmpty() &&
                                button.getBackground().getImages().get(0).getImage().getUrl().contains("block1"))
                            return true;
                        else if (slot.getBuildingsStatus().get(1) == null &&
                                !button.getBackground().getImages().isEmpty() &&
                                button.getBackground().getImages().get(0).getImage().getUrl().contains("block1-2"))
                            return true;
                        else if (slot.getBuildingsStatus().get(2) == null &&
                                !button.getBackground().getImages().isEmpty() &&
                                button.getBackground().getImages().get(0).getImage().getUrl().contains("block1-2-3"))
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
                            if (button.getBackground().getImages().isEmpty()) {
                                return true;
                            } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.RED) {
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
                String str = button.getId();
                str = str.replaceAll("[^\\d.]", "");
                if (((Integer) slot.getSlotPosition().getCoordinateX()).equals(Integer.parseInt(String.valueOf(str.charAt(1)))) &&
                        ((Integer) slot.getSlotPosition().getCoordinateY()).equals(Integer.parseInt(String.valueOf(str.charAt(0))))) {
                    if (button.getStyleClass().stream().anyMatch(x -> x.contains("level"))) {
                        if (slot.getBuildingsStatus().get(2) != null) {
                            button.setVisible(true);
                            Image image = new Image("/images/image-block1-2-3.png", button.getWidth(), button.getHeight(), false, true, true);
                            BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                            Background backGround = new Background(bImage);
                            button.setBackground(backGround);
//                            button.setStyle("-fx-background-image: url('/images/image-block1-2-3.jpg')");
//                            button.setStyle("-fx-background-size: 100% 100%");
                        } else if (slot.getBuildingsStatus().get(1) != null) {
                            button.setVisible(true);
                            Image image = new Image("/images/image-block1-2.png", button.getWidth(), button.getHeight(), false, true, true);
                            BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                            Background backGround = new Background(bImage);
                            button.setBackground(backGround);
//                            button.setStyle("-fx-background-image: url('/images/image-block1-2.jpg')");
//                            button.setStyle("-fx-background-size: 100% 100%");
                        } else if (slot.getBuildingsStatus().get(0) != null) {
                            button.setVisible(true);
                            Image image = new Image("/images/image-block1.png", button.getWidth(), button.getHeight(), false, true, true);
                            BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                            Background backGround = new Background(bImage);
                            button.setBackground(backGround);
//                            button.setStyle("-fx-background-image: url('/images/image-block1.jpg')");
//                            button.setStyle("-fx-background-size: 100% 100%");
                        } else {
                            button.setVisible(false);
                        }
//                    if (slot.getBuildingsStatus().get(0) == null)
//                        button.setVisible(false);
//                    else
//                        button.setVisible(true);
                    } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("dome"))) {
                        if (slot.getBuildingsStatus().get(3) != null) {
                            button.setVisible(true);
                            Image image = new Image("/images/image-blockD.png", button.getWidth(), button.getHeight(), false, true, true);
                            BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                            Background backGround = new Background(bImage);
                            button.setBackground(backGround);
//                            button.setStyle("-fx-background-image: url('/images/image-block1.jpg')");
//                            button.setStyle("-fx-background-size: 100% 100%");
                        } else {
                            button.setVisible(false);
                        }
                    } else if (button.getStyleClass().stream().anyMatch(x -> x.contains("worker"))) {
                        if (slot.getWorkerInSlot() == null)
                            button.setVisible(false);
                        else {
                            button.setVisible(true);

                            if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.RED) {
                                Image image = new Image("/images/worker_red.png", button.getWidth(), button.getHeight(), false, true, true);
                                BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                                Background backGround = new Background(bImage);
                                button.setBackground(backGround);
//                                button.setStyle("-fx-background-image: url('/images/worker_red.jpg')");
//                                button.setStyle("-fx-background-size: 100% 100%");
                            } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.YELLOW) {
                                Image image = new Image("/images/worker_cyan.png", button.getWidth(), button.getHeight(), false, true, true);
                                BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                                Background backGround = new Background(bImage);
                                button.setBackground(backGround);
//                                button.setStyle("-fx-background-image: url('/images/worker_yellow.jpg')");
//                                button.setStyle("-fx-background-size: 100% 100%");
                            } else if (slot.getWorkerInSlot().getColor() == Color.PlayerColor.CYAN) {
                                Image image = new Image("/images/worker_yellow.png", button.getWidth(), button.getHeight(), false, true, true);
                                BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));
                                Background backGround = new Background(bImage);
                                button.setBackground(backGround);
//                                button.setStyle("-fx-background-image: url('/images/worker_cyan.jpg')");
//                                button.setStyle("-fx-background-size: 100% 100%");
                            }
                        }
                    }
                }
            }
        }
    }
}
