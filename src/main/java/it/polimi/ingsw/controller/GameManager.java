package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.observer.MessageForwarder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager extends MessageForwarder {

    private final Game gameInstance;
    private final Turn turn;
    private final MoveVerifier moveVerifier;
    private String errorMessage = "";
    private Timer timer = new Timer();
    boolean undoDone = false;
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
        if (!turn.isPlayerTurn(move.getPlayerOwner()) &&
                (move.getMove() != Actions.QUIT || move.getMove() == Actions.GAME)) {
            move.getRemoteView().errorMessage(Message.wrongTurnMessage);
            return;
        } else {
            if (move.getMove() == Actions.QUIT) {
                return;
            } else if (move.getMove() == Actions.GAME) {
                return;
            }
        }
        if (move.getMove().getActionType() == Actions.ActionType.COMMAND) {
            if (move.getMove() == Actions.UNDO)
                if (turn.areThereMovesToUndo()) {
                    undoDone = true;
                    turn.restoreToLastMovePerformed();
                } else {
                    move.getRemoteView().errorMessage(Message.cannotUndo);
                    return;
                }
            else if (move.getMove() == Actions.SKIP)
                if (turn.getCurrentPlayerTurnSequence().getMoveSequence().get(turn.getCurrentMoveIndex() - 1) == Actions.BUILD_BEFORE) {
                    turn.getCurrentPlayerTurnSequence().setCanMoveUp(true);
                    updateTurn();
                } else {
                    move.getRemoteView().errorMessage(Message.cannotSkipThisMove);
                    return;
                }
        } else if (move.getMove().getActionType() == Actions.ActionType.SETUP) {
            if (move.getMove() == Actions.CHOSE_WORKER) {
                if (move.getTargetSlot().getWorkerInSlot() != null) {
                    if (turn.getCurrentPlayer().getWorkers().contains(move.getTargetSlot().getWorkerInSlot())) {
                        setCurrentWorker(move);
                    } else {
                        move.getRemoteView().errorMessage(Message.choseNotYourWorker);
                        return;
                    }
                } else {
                    move.getRemoteView().errorMessage(Message.noWorkerInSlot);
                    return;
                }
            } else if (move.getMove() == Actions.WAIT_FOR_UNDO) {
                updateTurn();
            }
        }
//        if (turn.getCurrentWorker() == null)
//            workerToSet = true;
//        else if (!turn.getCurrentWorker().getIdWorker().equals(move.getMovedWorker().getIdWorker())) {
//            move.getRemoteView().errorMessage(Message.wrongWorkerMessage);
//            return;
//        }
        else if (move.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
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
                            if (move.getMove() == Actions.MOVE_OPPONENT_SLOT_FLIP) {
                                PlayerMove tempMove = new PlayerMove(move.getMovedWorker().getIdWorker(),
                                        Actions.MOVE_STANDARD,
                                        new Position(-1, -1),
                                        turn.getCurrentPlayer().getNickname());
                                tempMove.setTargetSlot(new Slot(tempMove.getTargetPosition()));
                                tempMove.setMovedWorker(gameInstance.getWorkerByName(tempMove.getMovedWorkerId()));
                                tempMove.setForcedMove(move.getPlayerOwner());
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
     * Makes the worker perform a move
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performMove(PlayerMove move) {
        move.getMovedWorker().move(move.getTargetSlot());
        turn.addLastMovePerformed(move);
        if (!move.getForcedMove()) {
            if (!checkWinConditions(move))
                updateTurn();
            else
                turn.win(move.getPlayerOwner());
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
        updateTurn();
    }

    /**
     * Makes the worker perform a build forcing it to be a dome
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected void performBuildingDome(PlayerMove move) {
        move.getMovedWorker().forcedDomeBuild(move.getTargetSlot(), true);
        turn.addLastMovePerformed(move);
        updateTurn();
    }

    /**
     * Set the current worker (the worker the player decided to play with) for the current turn
     *
     * @param move The player move containing the information about the worker to set as current worker
     */
    protected void setCurrentWorker(PlayerMove move) {
//        if (!move.getForcedMove() && turn.getCurrentWorker() == null) {
        turn.setCurrentWorker(move.getMovedWorker());
        turn.addLastMovePerformed(move);
        updateTurn();
//        }
    }

    /**
     * Check if the current player has made a move that leads to victory
     *
     * @param move The player move containing the information about the worker and the target slot of the move
     */
    protected boolean checkWinConditions(PlayerMove move) {
        boolean winner = false;

        if (move.getPlayerOwner() == turn.getCurrentPlayer() &&
                turn.getCurrentPlayerTurnSequence().getWinConditions().contains(TurnEvents.WinConditions.WIN_DOUBLE_MOVE_DOWN)) {
            if (Slot.calculateHeightDifference(move.getStartingSlot(), move.getTargetSlot()) <= -2)
                winner = true;
        }

        if (move.getTargetSlot().getConstructionTopLevel() == Building.BuildingLevel.LEVEL3 &&
                move.getStartingSlot().getConstructionTopLevel() == Building.BuildingLevel.LEVEL2)
            winner = true;

        return winner;
    }

    /**
     * Update the turn and, in case it's the last move of the turn, wait five seconds to allow the player to undo.
     */
    protected void updateTurn() {
        try {
            boolean isLastMove = false;
            undoDone = false;
            timer.cancel();
            timer = new Timer();

            if (turn.getCurrentMoveIndex() == turn.getCurrentPlayerTurnSequence().getMoveSequence().size())
                isLastMove = true;

            if (isLastMove) {
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (!undoDone)
                                    turn.updateTurn();
                            }
                        },
                        5000
                );
            } else
                turn.updateTurn();
        } catch (Exception ex) {
            System.out.println("Exception thrown from GameManager.updateTurn. " + ex.getMessage());
        }
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
