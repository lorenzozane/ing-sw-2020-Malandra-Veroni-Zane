package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.Building.BuildingProperty;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Message;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.model.TurnEvents.Actions.ActionType;

public final class MoveVerifier {

    private final Game gameInstance;
    private final Turn turn;
    private final GameManager gameManager;

    /**
     * Constructor of the MoveVerifier which assists the GameManager in the management of the game logic (movement and construction).
     *
     * @param gameInstance The current game instance.
     * @param gameManager The game manager instance.
     */
    protected MoveVerifier(Game gameInstance, GameManager gameManager) {
        this.gameInstance = gameInstance;
        this.turn = gameInstance.getTurn();
        this.gameManager = gameManager;
    }

    //If we wanted to write all methods as static, we should also pass all the game and turn
    //instance directly to the methods (and not having game and turn instances belonging to the class)

    /**
     * Valid the request to move a worker in a specific slot
     *
     * @param move The move that contains all the information related to the movement and the target slot
     * @return Returns a boolean who approves or refuses the movement request
     */
    public boolean moveValidator(PlayerMove move) {
        if (move == null || move.getMove().getActionType() != ActionType.MOVEMENT) {
            gameManager.setErrorMessage(Message.moveNotAllowed);
            return false;
        }
        if (Slot.calculateDistance(move.getStartingSlot(), move.getTargetSlot()) != 1) {
            gameManager.setErrorMessage(Message.moveNotAllowed);
            return false;
        }
        if (move.getTargetSlot().getBuildingsStatus().contains(BuildingLevel.DOME)) {
            gameManager.setErrorMessage(Message.domeOccupiedCellMessage);
            return false;
        }
        if (!move.getForcedMove()) {
            if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 1) {
                gameManager.setErrorMessage(Message.tooHighMoveMessage);
                return false;
            }
            if (!turn.canCurrentPlayerMoveUp() && Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0) {
                gameManager.setErrorMessage(Message.tooHighMoveMessage);
                return false;
            }
        }
        if (move.getTargetSlot().getWorkerInSlot() != null) {
            if (move.getMove() != Actions.MOVE_OPPONENT_SLOT_FLIP && move.getMove() != Actions.MOVE_OPPONENT_SLOT_PUSH) {
                gameManager.setErrorMessage(Message.occupiedCellMessage);
                return false;
            }
        } else {
            if (move.getMove() == Actions.MOVE_NOT_INITIAL_POSITION) {
                PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                        .getActionType() == ActionType.MOVEMENT).reduce((first, second) -> second).orElse(null);
                if (initialMove == null || move.getTargetSlot() == initialMove.getStartingSlot()) {
                    gameManager.setErrorMessage(Message.notInitialPositionMessage);
                    return false;
                }
            }
//            } else if (move.getPlayer().getPlayerCard().getCardName().equalsIgnoreCase("prometheus") &&
//                    move.getMove() == Actions.MOVE_STANDARD) {
//
//            } else if (turn.getCurrentPlayerTurnSequence().getMoveSequence().contains(Actions.BUILD_BEFORE) &&
//                    move.getMove() == Actions.MOVE_STANDARD) {
//                PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove() == Actions.BUILD_BEFORE)
//                        .reduce((first, second) -> second).orElse(null);
//                if (initialMove != null)
//                    if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0) {
//                        gameManager.setErrorMessage(Message.tooHighMoveMessage);
//                        return false;
//                    }
//            }
        }

        return true;
    }

    /**
     * Valid the request to build a new element in a specific slot
     *
     * @param move The move that contains all the information related to the new construction and the target slot
     * @return Returns a boolean who approves or refuses the new construction request
     */
    public boolean buildValidator(PlayerMove move) {
        if (move == null || move.getMove().getActionType() != ActionType.BUILDING) {
            gameManager.setErrorMessage(Message.moveNotAllowed);
            return false;
        }
        if (Slot.calculateDistance(move.getStartingSlot(), move.getTargetSlot()) != 1) {
            gameManager.setErrorMessage(Message.buildMoveError);
            return false;
        }
        if (move.getTargetSlot().getWorkerInSlot() != null) {
            gameManager.setErrorMessage(Message.occupiedCellMessage);
            return false;
        }
        if (move.getTargetSlot().getBuildingsStatus().contains(BuildingLevel.DOME)) {
            gameManager.setErrorMessage(Message.domeOccupiedCellMessage);
            return false;
        }
        if (move.getMove() == Actions.BUILD_NOT_SAME_PLACE) {
            PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                    .getActionType() == ActionType.BUILDING).reduce((first, second) -> second).orElse(null);
            if (initialMove == null || move.getTargetSlot() == initialMove.getTargetSlot()) {
                gameManager.setErrorMessage(Message.buildNotSamePlaceMessage);
                return false;
            }
        } else if (move.getMove() == Actions.BUILD_SAME_PLACE_NOT_DOME) {
            PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                    .getActionType() == ActionType.BUILDING).reduce((first, second) -> second).orElse(null);
            if (initialMove == null || move.getTargetSlot() != initialMove.getTargetSlot()) {
                gameManager.setErrorMessage(Message.mustBuildSamePlaceMessage);
                return false;
            }
            if (move.getTargetSlot().getConstructionTopLevel() == BuildingLevel.LEVEL3) {
                gameManager.setErrorMessage(Message.cantBuildADomeMessage);
                return false;
            }
        }

        return true;
    }
}
