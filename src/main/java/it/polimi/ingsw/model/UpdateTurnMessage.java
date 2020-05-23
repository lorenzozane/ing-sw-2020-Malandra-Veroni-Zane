package it.polimi.ingsw.model;

import java.util.ArrayList;

public class UpdateTurnMessage {

    private final Board boardCopy;
    private final Player lastMovePerformedBy;
    private final TurnEvents.SetUpActions nextStartupMove;
    private final TurnEvents.Actions nextMove;
    //TODO: Mosse precedenti (?)
    private final boolean startupPhase;
    private final Player currentPlayer;
    private final Worker currentWorker;
//    private final Turn turn;
    private ArrayList<Color.PlayerColor> availableColor = new ArrayList<>(); //passiamo i colori disponibili in quel momento
    private ArrayList<GodsCard> choosenGodsCard= new ArrayList<>(); //passaimo le carte fra cui scegliere


    public UpdateTurnMessage(TurnEvents.SetUpActions nextStartupMove, Player currentPlayer) {
        this.startupPhase = true;
        this.nextStartupMove = nextStartupMove;
        this.currentPlayer = currentPlayer;
        this.currentWorker = null;
//        this.turn = turn;
        this.boardCopy = null;
        this.lastMovePerformedBy = null;
        this.nextMove = null;

    }

    public UpdateTurnMessage(Board boardCopy, Player lastMovePerformedBy, TurnEvents.Actions nextMove, Player currentPlayer, Worker currentWorker) {
        this.startupPhase = false;
        this.nextMove = nextMove;
        this.currentPlayer = currentPlayer;
        this.currentWorker = currentWorker;
//        this.turn = turn;
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public Player getLastMovePerformedBy() {
        return lastMovePerformedBy;
    }
}
