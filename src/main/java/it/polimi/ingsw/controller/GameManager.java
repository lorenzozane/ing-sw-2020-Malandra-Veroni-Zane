package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.Actions.*;
import it.polimi.ingsw.observer.Observer;

import java.util.ArrayList;

public class GameManager implements Observer<PlayerMove> {

    private final Game game;
    private Turn turn;

    public GameManager() {
        this.game = Game.getInstance();
        turn = this.game.getTurn();
    }

    protected synchronized void HandleMove(PlayerMove move) {
        if (!turn.isPlayerTurn(move.getPlayer())) {
            //WrongTurnMessage
            return;
        }

        //TODO: Logica gestione mosse
    }

    protected void initialCheck() {
        for (Worker worker : turn.getCurrentPlayerWorkers()) {
            Slot workerSlot = worker.getWorkerSlot();
            ArrayList<Slot> slotsToVerify = game.getBoard().getAdjacentSlots(workerSlot);

            for (Slot slot : slotsToVerify){
                //TODO: Logica check se può muoversi
            }
        }
    }

    protected void performMove() {

    }

    protected void performBuilding() {

    }

    protected void checkWinConditions() {

    }

    protected void checkTurnIsOver() {

    }

    @Override
    public void update(PlayerMove message) {
        HandleMove(message);
    }
}
