package it.polimi.ingsw.model;

import java.io.Serializable;

public class UpdateTurnMessage implements Serializable {

    private final Board boardCopy;
    private final String lastMovePerformedBy;
    private final TurnEvents.StartupActions nextStartupMove;
    private final TurnEvents.Actions nextMove;
    //TODO: Mosse precedenti (?)
    private final boolean startupPhase;
    private final Player currentPlayer;
    private final Worker currentWorker;
    //private ArrayList<Color.PlayerColor> availableColor = new ArrayList<>(); //passiamo i colori disponibili in quel momento
    //private ArrayList<GodsCard> chosenGodsCard= new ArrayList<>(); //passiamo le carte fra cui scegliere


    public UpdateTurnMessage(TurnEvents.StartupActions nextStartupMove, Player currentPlayer) {
        this.startupPhase = true;
        this.nextStartupMove = nextStartupMove;
        this.currentPlayer = currentPlayer;
        this.currentWorker = null;
//        this.turn = turn;
        this.boardCopy = null;
        this.lastMovePerformedBy = null;
        this.nextMove = null;

    }

    public UpdateTurnMessage(Board boardCopy, String lastMovePerformedBy, TurnEvents.Actions nextMove, Player currentPlayer, Worker currentWorker) {
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

    public TurnEvents.StartupActions getNextStartupMove() {
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

    public String getLastMovePerformedBy() {
        return lastMovePerformedBy;
    }




}
