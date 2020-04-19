package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
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

    public static boolean checkIfStuck(Worker worker) {
        boolean canMove = false;
        Slot workerSlot = worker.getWorkerSlot();
        ArrayList<Slot> slotsToVerify = game.getBoard().getAdjacentSlots(workerSlot);

        int i = 0;
        while (!canMove && i < slotsToVerify.size()) {
            Slot slotToVerify = slotsToVerify.get(i);
            if (Slot.calculateHeightDifference(workerSlot, slotToVerify) < 2)
                if (!slotToVerify.getBuildingsStatus().contains(Building.BuildingLevel.DOME))
                    if (slotToVerify.getWorkerInSlot() == null ||
                            turn.getCurrentPlayerTurnSequence().getMoveSequence().containsAll(Arrays.asList(Actions.MOVE_OPPONENT_SLOT_FLIP, Actions.MOVE_OPPONENT_SLOT_PUSH)))
                        canMove = true;
            i++;
        }

        return canMove;
    }

    public static boolean moveValidator(PlayerMove move) {
        if (move.getMove().getActionType() != ActionType.MOVEMENT)
            return false;
        if (Slot.calculateDistance(move.getStartingSlot(), move.getTargetSlot()) != 1)
            return false;
        if (move.getTargetSlot().getBuildingsStatus().contains(Building.BuildingLevel.DOME))
            return false;
        if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 1)
            return false;
        if (!Turn.canCurrentPlayerMoveUp() && Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0)
            return false;
        if (move.getTargetSlot().getWorkerInSlot() != null) {
            return move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP || move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH;
        } else {
            if (move.getMove() == Actions.MOVE_NOT_INITIAL_POSITION)
                return false; //TODO: Verificare se slot destinazione è quello di partenza
        }

        //TODO: Implementare controllo canMoveUpPrometheus
        return true;
    }

    public static boolean checkIfCanBuild() {
        boolean canBuild = false;

        return canBuild;
    }

    public static boolean buildValidator(PlayerMove move) {

        return true;
    }
}
