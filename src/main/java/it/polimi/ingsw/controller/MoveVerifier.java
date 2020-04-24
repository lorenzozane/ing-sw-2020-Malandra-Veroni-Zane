package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Building.BuildingLevel;
import it.polimi.ingsw.model.Building.BuildingProperty;
import it.polimi.ingsw.model.TurnEvents.Actions;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.model.TurnEvents.Actions.ActionType;

public final class MoveVerifier {

    private static final Game game;
    private static final Turn turn;

    static {
        game = Game.getInstance();
        turn = game.getTurn();
    }

    private MoveVerifier() {
    }

    /**
     * Check if, at the beginning of a turn, the worker has a slot adjacent him available to move on
     *
     * @param worker The worker whose ability to move is to be checked
     * @return Returns a boolean who approves or refuses the possibility to move of the worker
     */
    public static boolean checkIfStuck(Worker worker) {
        boolean canMove = false;
        Slot workerSlot = worker.getWorkerSlot();
        ArrayList<Slot> slotsToVerify = game.getBoard().getAdjacentSlots(workerSlot);

        int i = 0;
        while (!canMove && i < slotsToVerify.size()) {
            Slot slotToVerify = slotsToVerify.get(i);
            if (Slot.calculateHeightDifference(workerSlot, slotToVerify) < 2)
                if (!slotToVerify.getBuildingsStatus().contains(BuildingLevel.DOME))
                    if (slotToVerify.getWorkerInSlot() == null ||
                            turn.getCurrentPlayerTurnSequence().getMoveSequence().containsAll(Arrays.asList(Actions.MOVE_OPPONENT_SLOT_FLIP, Actions.MOVE_OPPONENT_SLOT_PUSH)))
                        canMove = true;
            i++;
        }

        return canMove;
    }

    /**
     * Valid the request to move a worker in a specific slot
     *
     * @param move The move that contains all the information related to the movement and the target slot
     * @return Returns a boolean who approves or refuses the movement request
     */
    public static boolean moveValidator(PlayerMove move) {
        if (move.getMove().getActionType() != ActionType.MOVEMENT)
            return false;
        if (Slot.calculateDistance(move.getStartingSlot(), move.getTargetSlot()) != 1)
            return false;
        if (move.getTargetSlot().getBuildingsStatus().contains(BuildingLevel.DOME))
            return false;
        if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 1)
            if (!move.getForcedMove())
                return false;
        if (!Turn.canCurrentPlayerMoveUp() && Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0)
            return false;
        if (move.getTargetSlot().getWorkerInSlot() != null) {
            return move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP || move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH;
        } else {
            if (move.getMove() == Actions.MOVE_NOT_INITIAL_POSITION) { //TODO: Test
                PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                        .getActionType() == ActionType.MOVEMENT).reduce((first, second) -> second).orElse(null);
                if (initialMove == null || move.getTargetSlot() == initialMove.getStartingSlot())
                    return false;
            } else if (move.getPlayer().getPlayerCard().getCardName().equalsIgnoreCase("prometheus") &&
                    move.getMove() == Actions.MOVE_STANDARD) { //Migliorabile(?) TODO: Test
                PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove() == Actions.BUILD_BEFORE)
                        .reduce((first, second) -> second).orElse(null);
                if (initialMove != null)
                    if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0)
                        return false;
            }
        }
        return true;
    }

    /**
     * Check if, at the beginning of a turn, the worker has a slot adjacent him available to build
     *
     * @param worker The worker whose ability to build is to be checked
     * @return Returns a boolean who approves or refuses the possibility to build of the worker
     */
    public static boolean checkIfCanBuild(Worker worker) {
        boolean canBuild = false;
        Slot workerSlot = worker.getWorkerSlot();
        ArrayList<Slot> slotsToVerify = game.getBoard().getAdjacentSlots(workerSlot);

        int i = 0;
        while (!canBuild && i < slotsToVerify.size()) {
            Slot slotToVerify = slotsToVerify.get(i);
            if (Slot.calculateHeightDifference(workerSlot, slotToVerify) < 2)
                if (slotToVerify.getWorkerInSlot() == null)
                    if (slotToVerify.getConstructionTopLevel().hasProperty(BuildingProperty.CAN_BUILD_ON_IT))
                        canBuild = true;

            i++;
        }

        return canBuild;
    }

    /**
     * Valid the request to build a new element in a specific slot
     *
     * @param move The move that contains all the information related to the new construction and the target slot
     * @return Returns a boolean who approves or refuses the new construction request
     */
    public static boolean buildValidator(PlayerMove move) {
        if (move.getMove().getActionType() != ActionType.BUILDING)
            return false;
        if (Slot.calculateDistance(move.getStartingSlot(), move.getTargetSlot()) != 1)
            return false;
        if (move.getTargetSlot().getWorkerInSlot() != null)
            return false;
        if (move.getTargetSlot().getBuildingsStatus().contains(BuildingLevel.DOME))
            return false;
        if (move.getMove() == Actions.BUILD_NOT_SAME_PLACE) {
            PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                    .getActionType() == ActionType.BUILDING).reduce((first, second) -> second).orElse(null);
            if (initialMove == null || move.getTargetSlot() == initialMove.getTargetSlot())
                return false;
        } else if (move.getMove() == Actions.BUILD_SAME_PLACE_NOT_DOME) {
            PlayerMove initialMove = turn.getMovesPerformed().stream().filter(x -> x.getMove()
                    .getActionType() == ActionType.BUILDING).reduce((first, second) -> second).orElse(null);
            if (initialMove == null || move.getTargetSlot() != initialMove.getTargetSlot())
                return false;
            if (move.getTargetSlot().getConstructionTopLevel() == BuildingLevel.LEVEL3)
                return false;
        }

        return true;
    }
}
