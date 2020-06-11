package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.observer.MessageForwarder;

import java.util.ArrayList;

public class GameManager extends MessageForwarder {

    private final Game gameInstance;
    private final Turn turn;
    private final MoveVerifier moveVerifier;
    private String errorMessage = "";
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
        if (!turn.isPlayerTurn(move.getPlayerOwner())) {
            move.getRemoteView().errorMessage(Message.wrongTurnMessage);
            return;
        }
        if (move.getMove().getActionType() == Actions.ActionType.COMMAND) {
            if (move.getMove() == Actions.UNDO)
                if (move.getMove() == Actions.UNDO)
                    turn.restoreToLastMovePerformed();
                else
                    move.getRemoteView().errorMessage(Message.cannotUndo);
            else if (move.getMove() == Actions.SKIP)
                if (turn.getCurrentPlayerTurnSequence().getMoveSequence().get(turn.getCurrentMoveIndex() - 1) == Actions.BUILD_BEFORE) {
                    turn.getCurrentPlayerTurnSequence().setCanMoveUp(true);
                    turn.updateTurn();
                }
                else
                    move.getRemoteView().errorMessage(Message.cannotSkipThisMove);
        }
        if (move.getMove().getActionType() == Actions.ActionType.SETUP && move.getMove() == Actions.CHOSE_WORKER) {
            if (move.getTargetSlot().getWorkerInSlot() != null) {
                if (turn.getCurrentPlayer().getWorkers().contains(move.getTargetSlot().getWorkerInSlot())) {
                    setCurrentWorker(move);
                } else {
                    move.getRemoteView().errorMessage(Message.choseNotYourWorker);
                }
            } else {
                move.getRemoteView().errorMessage(Message.noWorkerInSlot);
            }
        }
//        if (turn.getCurrentWorker() == null)
//            workerToSet = true;
//        else if (!turn.getCurrentWorker().getIdWorker().equals(move.getMovedWorker().getIdWorker())) {
//            move.getRemoteView().errorMessage(Message.wrongWorkerMessage);
//            return;
//        }
        if (move.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
            if (moveVerifier.moveValidator(move)) {
                //Move in opponent slot handling
                if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP || move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH) {
                    if (move.getTargetSlot().getWorkerInSlot() != null) {
                        if (!move.getPlayerOwner().getWorkers().contains(move.getTargetSlot().getWorkerInSlot())) {
                            PlayerMove opponentMove = null;
                            if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP) {
                                opponentMove = new PlayerMove(move.getTargetSlot().getWorkerInSlot().getIdWorker(),
                                        Actions.MOVE_STANDARD,
                                        move.getStartingSlot().getSlotPosition(),
                                        turn.getCurrentPlayer().getNickname());
                                opponentMove.setTargetSlot(gameInstance.getBoard().getSlot(opponentMove.getTargetPosition()));
                            } else if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_PUSH) {
                                Position backwardsSlotPosition = gameInstance.getBoard().getBackwardsSlotPosition(move.getStartingPosition(), move.getTargetPosition());
                                if (backwardsSlotPosition == null) {
                                    move.getRemoteView().errorMessage(Message.outOfBoardBorderMessage);
                                    return;
                                }
                                opponentMove = new PlayerMove(move.getTargetSlot().getWorkerInSlot().getIdWorker(),
                                        Actions.MOVE_STANDARD,
                                        backwardsSlotPosition,
                                        turn.getCurrentPlayer().getNickname());
                                opponentMove.setTargetSlot(gameInstance.getBoard().getSlot(backwardsSlotPosition));
                            }

                            assert opponentMove != null;
                            opponentMove.setMovedWorker(gameInstance.getWorkerByName(opponentMove.getMovedWorkerId()));
                            opponentMove.setForcedMove(move.getPlayerOwner());

                            //Temporary movement of player's worker in a "TempSlot"
                            //TODO: Verificare che l'UNDO funzioni correttamente
                            //TODO: Cambiare chi va in (-1, -1) per poter verificare winConditions (?)
                            if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP) {
                                PlayerMove tempMove = new PlayerMove(move.getMovedWorker().getIdWorker(),
                                        Actions.MOVE_STANDARD,
                                        new Position(-1, -1),
                                        turn.getCurrentPlayer().getNickname());
                                tempMove.setTargetSlot(new Slot(tempMove.getTargetPosition()));
                                tempMove.setMovedWorker(gameInstance.getWorkerByName(tempMove.getMovedWorkerId()));
                                tempMove.setForcedMove(move.getPlayerOwner());
//                                tempMove.setPlayerOwner(tempMove.getMovedWorker().getPlayerOwner());
                                performMove(tempMove);
                            }

                            if (moveVerifier.moveValidator(opponentMove))
                                performMove(opponentMove);
                            else {
                                move.getRemoteView().errorMessage(errorMessage);
                                return;
                            }
                        } else {
                            move.getRemoteView().errorMessage(Message.mustBeOpponentWorker);
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
                } else if (move.getMove() == Actions.BUILD_BEFORE) {
                    turn.getCurrentPlayerTurnSequence().setCanMoveUp(false);
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
        turn.updateTurn();
    }

    /**
     * Set the current worker for the current turn
     *
     * @param move The player move containing the information about the worker to set as current worker
     */
    protected void setCurrentWorker(PlayerMove move) {
//        if (!move.getForcedMove() && turn.getCurrentWorker() == null) {
        turn.setCurrentWorker(move.getMovedWorker());
        turn.updateTurn();
//        }
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
        //TODO: Avere un "lastMove" nell'UpdateTurnMessage può servire per il countdown per UNDO (?)
    }

    @Override
    protected void handlePlayerMove(PlayerMove message) {
        errorMessage = "";
        message.setPlayerOwner(gameInstance.getPlayerByName(message.getPlayerOwnerNickname()));
        message.setMovedWorker(gameInstance.getWorkerByName(message.getMovedWorkerId()));
        message.setTargetSlot(gameInstance.getBoard().getSlot(message.getTargetPosition()));
        handleMove(message);
    }

    public PlayerMoveReceiver getPlayerMoveReceiver() {
        return playerMoveReceiver;
    }
}
