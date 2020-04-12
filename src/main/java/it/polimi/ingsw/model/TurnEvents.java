package it.polimi.ingsw.model;

public enum TurnEvents {
    MOVE,
    MOVE_NOT_INITIAL_POSITION,
    MOVE_OPPONENT_SLOT_FLIP,
    MOVE_OPPONENT_SLOT_PUSH,

    BUILD,
    BUILD_NOT_SAME_PLACE,
    BUILD_SAME_PLACE_NOT_DOME,
    BUILD_DOME_ANY_LEVEL;
}
