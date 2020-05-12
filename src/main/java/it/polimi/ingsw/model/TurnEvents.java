package it.polimi.ingsw.model;

/**
 * Enums useful to describe the possible actions and win conditions available to players
 */
public class TurnEvents {

    public enum SetUpActions {
        COLOR_REQUEST,
        CHOOSE_CARD_REQUEST,
        PICK_UP_CARD_REQUEST;
    }

    /**
     * Set of possible actions that the player can perform (depending on which God owns)
     */
    public enum Actions {
        MOVE_STANDARD(ActionType.MOVEMENT),
        MOVE_NOT_INITIAL_POSITION(ActionType.MOVEMENT),
        MOVE_OPPONENT_SLOT_FLIP(ActionType.MOVEMENT),
        MOVE_OPPONENT_SLOT_PUSH(ActionType.MOVEMENT),
        MOVE_DISABLE_OPPONENT_UP(ActionType.MOVEMENT),

        BUILD_STANDARD(ActionType.BUILDING),
        BUILD_BEFORE(ActionType.BUILDING),
        BUILD_NOT_SAME_PLACE(ActionType.BUILDING),
        BUILD_SAME_PLACE_NOT_DOME(ActionType.BUILDING),
        BUILD_DOME_ANY_LEVEL(ActionType.BUILDING);

        public enum ActionType {
            MOVEMENT,
            BUILDING;
        }

        private ActionType type;

        Actions(ActionType type) {
            this.type = type;
        }

        public ActionType getActionType() {
            return this.type;
        }
    }

    /**
     * Set of possible win conditions for the players (depending of which God owns)
     */
    public enum WinConditions {
        WIN_STANDARD,
        WIN_DOUBLE_MOVE_DOWN;
    }
}
