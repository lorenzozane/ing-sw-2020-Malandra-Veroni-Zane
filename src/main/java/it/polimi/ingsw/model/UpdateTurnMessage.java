package it.polimi.ingsw.model;

public class UpdateTurnMessage {

    private final Board boardCopy;
    private final Player lastMovePerformedBy;
    private final TurnEvents nextMove;
    private final boolean startupPhase;
    private final Turn turn;

    public UpdateTurnMessage(TurnEvents nextMove, Turn turn) {
        this.boardCopy = null;
        this.lastMovePerformedBy = null;
        this.nextMove = nextMove;
        this.turn = turn;
        this.startupPhase = true;
    }

    public UpdateTurnMessage(Board boardCopy, Player lastMovePerformedBy, TurnEvents nextMove, Turn turn) {
        this.boardCopy = boardCopy;
        this.lastMovePerformedBy = lastMovePerformedBy;
        this.nextMove = nextMove;
        this.turn = turn;
        this.startupPhase = false;
    }

}
