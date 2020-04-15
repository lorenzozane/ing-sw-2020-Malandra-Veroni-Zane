package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Slot;
import it.polimi.ingsw.model.TurnEvents.Actions;

import static it.polimi.ingsw.model.TurnEvents.Actions.ActionType;

public final class MoveVerifier {

    private MoveVerifier() {
    }

    public static boolean checkIfStuck() {

        return false;
    }

    public static boolean moveValidator(Actions move, Slot startingSlot, Slot targetSlot) {
        if (move.getActionType() != ActionType.MOVEMENT)
            return false;
        if (Slot.calculateDistance(startingSlot, targetSlot) != 1)
            return false;
        if (targetSlot.getBuildingStatus().contains("DOME"))
            return false;
        if (Slot.calculateHeightDifference(startingSlot, targetSlot) > 1)
            return false;
        if (targetSlot.getWorkerInSlot() != null) {
            return move == Actions.MOVE_OPPONENT_SLOT_FLIP || move == Actions.MOVE_OPPONENT_SLOT_PUSH;
        } else {
            if (move == Actions.MOVE_NOT_INITIAL_POSITION)
                return false; //TODO: Verificare se slot destinazione è quello di partenza
        }

        return true;
    }

    public static void checkIfCanBuild() {

    }

    public static boolean buildValidator() {

        return true;
    }
}
