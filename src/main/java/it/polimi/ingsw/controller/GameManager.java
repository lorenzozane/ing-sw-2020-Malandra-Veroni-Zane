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

    /**
     * Logic of game moves
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
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
                } else if (move.getMove() == Actions.MOVE_DISABLE_OPPONENT_UP) {

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

    /**
     * Check if, at the beginning of a turn, the player's workers have a slot adjacent them available to move on
     *
     * @return Returns an ArrayList of worker who can move in an adjacent slot
     */
    protected ArrayList<Worker> initialCheckMovableWorker() {
        ArrayList<Worker> movableWorkers = new ArrayList<>(2);
        for (Worker worker : turn.getCurrentPlayerWorkers()) {
            boolean canMove = MoveVerifier.checkIfStuck(worker);

            if (canMove)
                movableWorkers.add(worker);
        }

        return movableWorkers;
    }

    /**
     * Makes the worker perform a move
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performMove(PlayerMove move) {
        move.getMovedWorker().move(move.getTargetSlot());
        turn.addLastMovePerformed(move);
    }

    /**
     * Makes the worker perform a build
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performBuilding(PlayerMove move) {
        move.getMovedWorker().build(move.getTargetSlot());
        turn.addLastMovePerformed(move);
    }

    /**
     * Check if the current player has made a move that leads to victory
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
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
