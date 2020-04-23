package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.*;
import it.polimi.ingsw.observer.Observer;

import java.util.ArrayList;

public class GameManager implements Observer<PlayerMove> {

    private final Game game;
    private final Turn turn;
//    private final MoveVerifier moveVerifier = new MoveVerifier();

    public GameManager() {
        this.game = Game.getInstance();
        this.turn = this.game.getTurn();
    }

    protected synchronized void handleMove(PlayerMove move) {
        if (!turn.isPlayerTurn(move.getPlayer())) {
            //WrongTurnMessage
            return;
        }
        if (move.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
            if (MoveVerifier.moveValidator(move)) {
                //Move in opponent slot handling
                if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP || move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH) {
                    PlayerMove opponentMove = new PlayerMove(move.getTargetSlot().getWorkerInSlot(),
                            Actions.MOVE_STANDARD,
                            game.getBoard().getBackwardsSlot(move.getStartingSlot(), move.getTargetSlot()));
                    opponentMove.setForcedMove(move.getPlayer());

                    if (MoveVerifier.moveValidator(opponentMove))
                        performMove(opponentMove);
                    else
                        return; //MoveNotAllowedException
                }


                performMove(move);
                checkWinConditions(move);
            } else
                return; //MoveNotAllowedException
        } else if (move.getMove().getActionType() == Actions.ActionType.BUILDING)
            if (MoveVerifier.buildValidator(move))
                performBuilding(move);
            else
                return; //MoveNotAllowedException

        //TODO: Logica gestione mosse
    }

    protected ArrayList<Worker> initialCheckMovableWorker() {
        ArrayList<Worker> movableWorkers = new ArrayList<>(2);
        for (Worker worker : turn.getCurrentPlayerWorkers()) {
            boolean canMove = MoveVerifier.checkIfStuck(worker);

            if (canMove)
                movableWorkers.add(worker);
        }

        return movableWorkers;
    }

    protected void performMove(PlayerMove move) {
        move.getMovedWorker().move(move.getTargetSlot());
    }

    protected void performBuilding(PlayerMove move) {
        move.getMovedWorker().build(move.getTargetSlot());
    }

    protected void checkWinConditions(PlayerMove move) {
        boolean winner = false;

        if (move.getTargetSlot().getConstructionTopLevel() == Building.BuildingLevel.LEVEL3 &&
                move.getStartingSlot().getConstructionTopLevel() == Building.BuildingLevel.LEVEL2)
            winner = true;

        if (winner)
            return; //TODO: Notifica vittoria
    }

    protected void checkTurnIsOver() {

    }

    @Override
    public void update(PlayerMove message) {
        handleMove(message);
    }
}
