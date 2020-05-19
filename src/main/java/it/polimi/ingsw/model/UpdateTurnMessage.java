package it.polimi.ingsw.model;

public class UpdateTurnMessage {

    private final Board boardCopy;
    private final Player lastMovePerformedBy;
    private final TurnEvents.SetUpActions nextStartupMove;
    private final TurnEvents.Actions nextMove;
    private final boolean startupPhase;
    private final Turn turn;
    private Color.PlayerColor chosenColor;


    public UpdateTurnMessage(TurnEvents.SetUpActions nextStartupMove, Turn turn) {
        this.startupPhase = true;
        this.nextStartupMove = nextStartupMove;
        this.turn = turn;
        this.boardCopy = null;
        this.lastMovePerformedBy = null;
        this.nextMove = null;
    }

    public UpdateTurnMessage(Board boardCopy, Player lastMovePerformedBy, TurnEvents.Actions nextMove, Turn turn) {
        this.startupPhase = false;
        this.nextMove = nextMove;
        this.turn = turn;
        this.boardCopy = boardCopy;
        this.lastMovePerformedBy = lastMovePerformedBy;
        this.nextStartupMove = null;
    }

    public boolean isStartupPhase() {
        return startupPhase;
    }

    public TurnEvents.SetUpActions getNextStartupMove() {
        return nextStartupMove;
    }

    public TurnEvents.Actions getNextMove() {
        return nextMove;
    }
}
