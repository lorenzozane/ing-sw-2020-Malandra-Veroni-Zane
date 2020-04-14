package it.polimi.ingsw.model;

/**
 * Enums useful to describe the possible actions and win conditions available to players
 */
public class TurnEvents {

    /**
     * Set of possible actions that the player can perform (depending on which God owns)
     */
    public enum Actions {
        MOVE,
        MOVE_NOT_INITIAL_POSITION,
        MOVE_OPPONENT_SLOT_FLIP,
        MOVE_OPPONENT_SLOT_PUSH,

        BUILD,
        BUILD_NOT_SAME_PLACE,
        BUILD_SAME_PLACE_NOT_DOME,
        BUILD_DOME_ANY_LEVEL;
    }

    /**
     * Set of possible win conditions for the players (depending of which God owns)
     */
    public enum WinConditions {
        WIN_STANDARD,
        WIN_DOUBLE_MOVE_DOWN;
    }
}
