package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.*;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.observer.MessageForwarder;

import java.util.ArrayList;

public class GameManager extends MessageForwarder {

    private final Game gameInstance;
    private final Turn turn;
    private final MoveVerifier moveVerifier;
    private String errorMessage = "";
    private boolean workerToSet = false;
    private final PlayerMoveReceiver playerMoveReceiver = new PlayerMoveReceiver();

    /**
     * Constructor of the GameManager that deals with managing the game logic (movement and construction)
     *
     * @param gameInstance Is the current game instance
     */
    public GameManager(Game gameInstance) {
        this.gameInstance = gameInstance;
        this.turn = this.gameInstance.getTurn();
        this.moveVerifier = new MoveVerifier(this.gameInstance, this);
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Logic of game moves
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected synchronized void handleMove(PlayerMove move) {
        if (!turn.isPlayerTurn(move.getPlayer())) {
            move.getRemoteView().errorMessage(Message.wrongTurnMessage);
            return;
        }
        if (turn.getCurrentWorker() == null)
            workerToSet = true;
        else if (!turn.getCurrentWorker().getIdWorker().equals(move.getMovedWorker().getIdWorker())) {
            move.getRemoteView().errorMessage(Message.wrongWorkerMessage);
            return;
        }
        if (move.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
            if (moveVerifier.moveValidator(move)) {
                //Move in opponent slot handling
                if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP || move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH) {
                    if (move.getTargetSlot().getWorkerInSlot() != null) {
                        PlayerMove opponentMove = null;
                        if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP) {
                            opponentMove = new PlayerMove(move.getTargetSlot().getWorkerInSlot(),
                                    Actions.MOVE_STANDARD,
                                    move.getStartingSlot(),
                                    turn, move.getRemoteView());
                        } else if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH) {
                            Slot backwardsSlot = gameInstance.getBoard().getBackwardsSlot(move.getStartingSlot(), move.getTargetSlot());
                            if (backwardsSlot == null) {
                                move.getRemoteView().errorMessage(Message.outOfBoardBorderMessage);
                                return;
                            }
                            opponentMove = new PlayerMove(move.getTargetSlot().getWorkerInSlot(),
                                    Actions.MOVE_STANDARD,
                                    gameInstance.getBoard().getBackwardsSlot(move.getStartingSlot(), move.getTargetSlot()),
                                    turn, move.getRemoteView());
                        }

                        assert opponentMove != null;
                        opponentMove.setForcedMove(move.getPlayer());

                        //Temporary movement of player's worker in a "TempSlot"
                        //TODO: Verificare che l'UNDO funzioni correttamente
                        if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP) {
                            PlayerMove tempMove = new PlayerMove(move.getMovedWorker(),
                                    Actions.MOVE_STANDARD,
                                    new Slot(new Position(-1, -1)),
                                    turn, move.getRemoteView());
                            performMove(tempMove);
                        }

                        if (moveVerifier.moveValidator(opponentMove))
                            performMove(opponentMove);
                        else {
                            move.getRemoteView().errorMessage(errorMessage);
                            return;
                        }
                    }
                    //Move disabling opponent can move up handling
                } else if (move.getMove() == Actions.MOVE_DISABLE_OPPONENT_UP) {
                    if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) > 0)
                        turn.setOtherPlayerCanMoveUpTo(false);
                    else
                        turn.setOtherPlayerCanMoveUpTo(true);
                }

                performMove(move);
            } else {
                //TODO: Aggiungere a tutti gli altri casi d'errore (serve?)
                move.getRemoteView().resetBoard(gameInstance.getBoard());
                move.getRemoteView().errorMessage(errorMessage);
                return;
            }
        } else if (move.getMove().getActionType() == Actions.ActionType.BUILDING) {
            if (moveVerifier.buildValidator(move)) {
                if (move.getMove() == Actions.BUILD_DOME_ANY_LEVEL) {
                    performBuildingDome(move);
                    return;
                }

                performBuilding(move);
            } else {
                move.getRemoteView().errorMessage(errorMessage);
                return;
            }
        }
    }

    /**
     * Check if, at the beginning of a turn, the player's workers have a slot adjacent them available to move on
     *
     * @return Returns an ArrayList of worker who can move in an adjacent slot
     */
    protected ArrayList<Worker> initialCheckMovableWorker() {
        ArrayList<Worker> movableWorkers = new ArrayList<>(2);
        for (Worker worker : turn.getCurrentPlayerWorkers()) {
            boolean canMove = moveVerifier.checkIfStuck(worker);

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
        if (!move.getForcedMove()) {
            if (workerToSet)
                setCurrentWorker(move);
            checkWinConditions(move);
            turn.updateTurn();
        }
    }

    /**
     * Makes the worker perform a build
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performBuilding(PlayerMove move) {
        move.getMovedWorker().build(move.getTargetSlot());
        turn.addLastMovePerformed(move);
        if (workerToSet)
            setCurrentWorker(move);
        turn.updateTurn();
    }

    /**
     * Makes the worker perform a build forcing it to be a dome
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performBuildingDome(PlayerMove move) {
        move.getMovedWorker().forcedDomeBuild(move.getTargetSlot(), true);
        turn.addLastMovePerformed(move);
        if (workerToSet)
            setCurrentWorker(move);
        turn.updateTurn();
    }

    /**
     * Set the current worker for the current turn
     *
     * @param move The player move containing the information about the worker to set as current worker
     */
    protected void setCurrentWorker(PlayerMove move) {
        if (workerToSet && !move.getForcedMove() && turn.getCurrentWorker() == null)
            turn.setCurrentWorker(move.getMovedWorker());
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
    protected void handlePlayerMove(PlayerMove message) {
        errorMessage = "";
        handleMove(message);
    }

    public PlayerMoveReceiver getPlayerMoveReceiver() {
        return playerMoveReceiver;
    }
}
