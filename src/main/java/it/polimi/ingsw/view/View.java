package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.model.TurnEvents.Actions.ActionProperty;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.GuiController;

import java.util.ArrayList;
import java.util.Objects;

public class View extends MessageForwarder {

    private String playerOwnerNickname;
    private final UserInterface chosenUserInterface;
    private RemoteView remoteView;
    private final Cli playerCli;
    private final GuiController playerGui;
    private UpdateTurnMessage currentMove = null;
    private boolean activeReadResponse = false;
    protected boolean gameIsFinish = false;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();
    private final StringSender stringSender = new StringSender();
    private final StringReceiver stringReceiver = new StringReceiver();

    /**
     * Constructor of the View that interact with the player's CLI or GUI.
     *
     * @param playerCli Is the instance of the CLI that the player has chosen to use.
     */
    public View(Cli playerCli) {
        this.chosenUserInterface = UserInterface.CLI;
        this.playerCli = playerCli;
        this.playerCli.setViewOwner(this);
        this.playerGui = null;
    }

    /**
     * Constructor of the View that interact with the player's CLI or GUI.
     *
     * @param playerGui Is the instance of the GUI that the player has chosen to use.
     */
    public View(GuiController playerGui) {
        this.chosenUserInterface = UserInterface.GUI;
        this.playerGui = playerGui;
        this.playerGui.setViewOwner(this);
        this.playerCli = null;
    }

    //TODO: Non l'abbiamo collegato?
    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
    }

    /**
     * Allows to define the player owner of this view nickname.
     *
     * @param playerOwnerNickname The player nickname.
     */
    public void setPlayerOwnerNickname(String playerOwnerNickname) {
        if (this.playerOwnerNickname == null)
            this.playerOwnerNickname = playerOwnerNickname;
    }

    /**
     * Allows to know the player owner nickname of this view nickname.
     *
     * @return The player owner nickname of this view.
     */
    public String getPlayerOwnerNickname() {
        return playerOwnerNickname;
    }

    //TODO: Agli errori che passano di qua viene aggiunto un a capo. Verificare

    /**
     * Sends to the CLI or GUI an error message to be shown.
     *
     * @param errorToShow The error message to be shown.
     */
    public void showErrorMessage(String errorToShow) {
        if (chosenUserInterface == UserInterface.CLI)
            showMessage(errorToShow);
        else if (chosenUserInterface == UserInterface.GUI) {
            playerGui.showErrorMessage(currentMove, errorToShow);
            if (errorToShow.contains("Error: "))
                repeatCurrentMove(currentMove);
        }
    }

    /**
     * Sends to the CLI or GUI a message to be shown.
     *
     * @param messageToShow The message to be shown.
     */
    public void showMessage(String messageToShow) {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null) {
            if (currentMove.isStartupPhase()) {
                playerCli.showMessage(messageToShow);
            } else if (!(currentMove.getCurrentPlayer().getNickname().equals(playerOwnerNickname))) {
                playerCli.showMessage(messageToShow);
            } else
                playerCli.showMessage(messageToShow, currentMove.getCurrentPlayer().getPlayerColor());

        } else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            if (messageToShow.contains("Error: "))
                showErrorMessage(messageToShow);
            else {
                if (messageToShow.equals(ViewMessage.choseYourWorker))
                    messageToShow = ViewMessage.choseYourWorkerGui;
                if (messageToShow.equals(ViewMessage.quit))
                    messageToShow = ViewMessage.quitGui;
                if (messageToShow.equals(ViewMessage.buildDomeAnyLevel))
                    messageToShow = ViewMessage.buildDomeAnyLevelGui;
                playerGui.showMessage(currentMove, messageToShow);
            }
            return;
        }

        if (messageToShow.contains("Error: "))
            repeatCurrentMove(currentMove);
    }

    /**
     * Sends to the CLI or GUI a message sent to all player to be shown.
     *
     * @param messageToShow The message to be shown.
     */
    public void showSimultaneousMessage(String messageToShow) {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null) {
            playerCli.showSimultaneousMessage(messageToShow);
        } else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            playerGui.showStringMessage(messageToShow);
        }

        if (messageToShow.contains("Error: "))
            repeatCurrentMove(currentMove);
    }

//    public String showMessageImmediateResponse(String messageToShow) {
//        if (chosenUserInterface == UserInterface.CLI && playerCli != null) {
//            return playerCli.showMessageImmediateResponse(messageToShow);
//        } else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
//        }
//        return null;
//    }

    /**
     * Causes the player to repeat the last move, usually after an error.
     *
     * @param currentMove The move to repeat.
     */
    private void repeatCurrentMove(UpdateTurnMessage currentMove) {
        handleUpdateTurnMessage(currentMove);

//        if (lastMove.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
//            handleMessageForMe(currentMove);
//        else
//            handleMessageForOthers(currentMove);
    }

    /**
     * Logic used to handle the player response to a move request (or more generally a response).
     *
     * @param response The player response to handle.
     */
    public void handleResponse(String response) {
        if (currentMove == null) {
            stringSender.notifyAll(response);

        } else if (currentMove.getCurrentPlayer().getNickname().equals(playerOwnerNickname) ||
                response.equalsIgnoreCase("quit")) {
            if (currentMove.isStartupPhase()) {
                PlayerMoveStartup moveStartupToSend = createPlayerMoveStartup();

                if (currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST ||
                        currentMove.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) {
                    PlayerColor chosenPlayerColor = convertStringToPlayerColor(response);
                    if (chosenPlayerColor == null) {
                        showErrorMessage(ViewMessage.wrongColorChose);
                        return;
                    } else {
                        moveStartupToSend.setChosenColor(chosenPlayerColor);
                    }
                } else if (currentMove.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST ||
                        currentMove.getNextStartupMove() == StartupActions.PICK_UP_CARD_REQUEST ||
                        currentMove.getNextStartupMove() == StartupActions.PICK_LAST_CARD) {
                    moveStartupToSend.setChosenCard(response);
                } else if (currentMove.getNextStartupMove() == StartupActions.PLACE_WORKER_1 ||
                        currentMove.getNextStartupMove() == StartupActions.PLACE_WORKER_2) {
                    if (convertStringToPosition(response) != null)
                        moveStartupToSend.setWorkerPosition(convertStringToPosition(response));
                    else {
                        showErrorMessage(ViewMessage.wrongInputCoordinates);
                        return;
                    }
                }

                sendPlayerMoveStartup(moveStartupToSend);
            } else if (gameIsFinish &&
                    !(response.equalsIgnoreCase("quit"))) {
                showErrorMessage(ViewMessage.canOnlyQuit);
                return;
            } else {
                if (response.equalsIgnoreCase("undo")) {
                    if (currentMove.getNextMove() != Actions.CHOSE_WORKER) {
                        PlayerMove playerMoveToSend = createPlayerMoveCommand(Actions.UNDO);
                        sendPlayerMove(playerMoveToSend);
                    } else {
                        showErrorMessage(ViewMessage.cannotUndo);
                        return;
                    }
                } else if (response.equalsIgnoreCase("skip")) {
                    if (currentMove.getNextMove().hasProperty(ActionProperty.SKIPPABLE)) {
                        PlayerMove playerMoveToSend = createPlayerMoveCommand(Actions.SKIP);
                        sendPlayerMove(playerMoveToSend);
                    } else {
                        showErrorMessage(ViewMessage.cannotSkipThisMove);
                        return;
                    }
                } else if (response.equalsIgnoreCase("quit")) {
                    PlayerMove playerMoveToSend = createPlayerMoveCommand(Actions.QUIT);
                    sendPlayerMove(playerMoveToSend);
                } else if (currentMove.getNextMove() == Actions.WAIT_FOR_UNDO) {
                    showErrorMessage(ViewMessage.canOnlyUndo);
                } else if (currentMove.getNextMove() == Actions.WIN) {
                    showErrorMessage(ViewMessage.canOnlyQuit);
                } else {
                    Worker workerInSlot;

                    if (convertStringToPosition(response) == null &&
                            currentMove.getNextMove() != Actions.BUILD_DOME_ANY_LEVEL) {
                        showErrorMessage(ViewMessage.wrongInputCoordinates);
                    } else {
                        if (currentMove.getNextMove() == Actions.CHOSE_WORKER) {
                            workerInSlot = currentMove.getBoardCopy().getSlot(Objects.requireNonNull(convertStringToPosition(response))).getWorkerInSlot();
                            if (workerInSlot != null) {
                                if (currentMove.getCurrentPlayer().getWorkers().stream().map(Worker::getIdWorker).anyMatch(workerInSlot.getIdWorker()::equalsIgnoreCase)) {
                                    Worker finalWorkerInSlot = workerInSlot;
                                    workerInSlot = currentMove.getCurrentPlayer().getWorkers().stream().filter(worker -> worker.getIdWorker().equals(finalWorkerInSlot.getIdWorker())).findFirst().orElse(null);
                                    assert workerInSlot != null;
                                    PlayerMove playerMoveToSend = createPlayerMove(convertStringToPosition(response), workerInSlot.getIdWorker());
                                    sendPlayerMove(playerMoveToSend);
                                } else {
                                    showErrorMessage(ViewMessage.choseNotYourWorker);
                                    return;
                                }
                            } else {
                                showErrorMessage(ViewMessage.noWorkerInSlot);
                                return;
                            }
                        } else {
                            if (currentMove.getNextMove() == Actions.BUILD_DOME_ANY_LEVEL) {
                                String[] splittedResponse = response.split(" ");
                                if (!(splittedResponse.length == 2 && splittedResponse[1].equalsIgnoreCase("dome"))) {
                                    PlayerMove playerMoveToSend = createPlayerMoveStandardBuild(convertStringToPosition(response));
                                    sendPlayerMove(playerMoveToSend);
                                    return;
                                } else {
                                    response = splittedResponse[0];
                                }
                            }
                            PlayerMove playerMoveToSend = createPlayerMove(convertStringToPosition(response));
                            sendPlayerMove(playerMoveToSend);
                        }
                    }
                }
            }
        } else {
            if (gameIsFinish)
                showMessage(ViewMessage.canOnlyQuit);
            else
                showMessage(ViewMessage.wrongTurnMessage);
            return;
        }
    }

    /**
     * Sends to the controller the move performed by the player during the game.
     *
     * @param playerMove PlayerMove performed by the player.
     */
    private void sendPlayerMove(PlayerMove playerMove) {
        playerMoveSender.notifyAll(playerMove);
    }

    /**
     * Sends to the controller the move performed by the player during the startup phase.
     *
     * @param playerMoveStartup PlayerMoveStartup performed by the player.
     */
    private void sendPlayerMoveStartup(PlayerMoveStartup playerMoveStartup) {
        playerMoveStartupSender.notifyAll(playerMoveStartup);
    }

    /**
     * Converts the player's input into coordinates.
     *
     * @param coordinates Player's input to convert.
     * @return Returns the coordinates created from the player's input.
     */
    private Position convertStringToPosition(String coordinates) {
        if (coordinates.length() != 2) {
            showErrorMessage(ViewMessage.wrongInputCoordinates);
            return null;
        }

        int coordinateX = coordinates.charAt(1);
        int coordinateY = coordinates.charAt(0);
        if (coordinateX >= 49 && coordinateX <= 53)
            if (coordinateY >= 65 && coordinateY <= 69)
                return new Position(coordinateX - 49, coordinateY - 65);
            else if (coordinateY >= 97 && coordinateY <= 101)
                return new Position(coordinateX - 49, coordinateY - 97);

        return null;
    }

    /**
     * Converts the player's input into a PlayerColor.
     *
     * @param playerColor Player's input to convert.
     * @return Returns the PlayerColor chosen by the player.
     */
    private PlayerColor convertStringToPlayerColor(String playerColor) {
        switch (playerColor.toLowerCase()) {
            case "red":
                if (currentMove.getAvailableColor().contains(PlayerColor.RED))
                    return PlayerColor.RED;
                break;
            case "cyan":
                if (currentMove.getAvailableColor().contains(PlayerColor.CYAN))
                    return PlayerColor.CYAN;
                break;
            case "yellow":
                if (currentMove.getAvailableColor().contains(PlayerColor.YELLOW))
                    return PlayerColor.YELLOW;
                break;
        }

        return null;
    }

    /**
     * Allows to know which PlayerColor are available to be chosen from the current player.
     *
     * @param availableColor ArrayList of PlayerColor available to be chosen.
     * @return Returns a formatted string to be shown at the player containing all the available PlayerColor as text.
     */
    private String getAvailableColorBuilder(ArrayList<PlayerColor> availableColor) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PlayerColor playerColor : availableColor) {
            stringBuilder.append(" ").append(playerColor.getEscape()).append(playerColor.getColorAsString()).append(Color.RESET).append(" or");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        return String.valueOf(stringBuilder);
    }

    /**
     * Allows to know which GodsCard are available to be chosen from the current player.
     *
     * @param availableCards ArrayList of GodsCard available to be chosen.
     * @return Returns a formatted string to be shown at the player containing all the available GodsCard as text.
     */
    private String getAvailableCardsBuilder(ArrayList<GodsCard> availableCards) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GodsCard godsCard : availableCards) {
            stringBuilder.append("\n").append(godsCard.getCardName().toUpperCase()).append(": ").append(godsCard.getCardDescription());
        }

        return String.valueOf(stringBuilder);
    }

    /**
     * Create the PlayerMove to be send at the controller based on player's input. Used when the worker is known.
     *
     * @param targetSlotPosition The position of the target slot chose by the player for the current move.
     * @return Returns the PlayerMove ready to be forwarded to the controller.
     */
    protected PlayerMove createPlayerMove(Position targetSlotPosition) {
        return new PlayerMove(
                currentMove.getCurrentWorker().getIdWorker(),
                currentMove.getNextMove(),
                targetSlotPosition,
                currentMove.getCurrentPlayer().getNickname());
    }

    /**
     * Create the PlayerMove to be send at the controller based on player's input. Used while choosing the worker.
     *
     * @param targetSlotPosition The position of the target slot chose by the player for the current move.
     * @param currentWorker      The worker chose by the player to play with this turn.
     * @return Returns the PlayerMove ready to be forwarded to the controller.
     */
    protected PlayerMove createPlayerMove(Position targetSlotPosition, String currentWorker) {
        return new PlayerMove(
                currentWorker,
                currentMove.getNextMove(),
                targetSlotPosition,
                currentMove.getCurrentPlayer().getNickname());
    }

    /**
     * Create the PlayerMove to be send at the controller based on player's input. Used when the Actions is
     * BUILD_DOME_ANY_LEVEL and the player chose not to build a dome.
     *
     * @param targetSlotPosition The position of the target slot chose by the player for the current move.
     * @return Returns the PlayerMove ready to be forwarded to the controller.
     */
    protected PlayerMove createPlayerMoveStandardBuild(Position targetSlotPosition) {
        return new PlayerMove(
                currentMove.getCurrentWorker().getIdWorker(),
                Actions.BUILD_STANDARD,
                targetSlotPosition,
                currentMove.getCurrentPlayer().getNickname());
    }

    /**
     * Create the PlayerMove to be send at the controller with a specific Actions command.
     *
     * @param command Actions command to send to the controller.
     * @return Returns the PlayerMove ready to be forwarded to the controller.
     */
    protected PlayerMove createPlayerMoveCommand(Actions command) {
        if (currentMove.getCurrentWorker() != null &&
                currentMove.getCurrentPlayer().getNickname().equalsIgnoreCase(playerOwnerNickname)) {
            return new PlayerMove(
                    currentMove.getCurrentWorker().getIdWorker(),
                    command,
                    currentMove.getCurrentWorker().getWorkerPosition(),
                    currentMove.getCurrentPlayer().getNickname()
            );
        } else {
            return new PlayerMove(
                    null,
                    command,
                    null,
                    playerOwnerNickname
            );
        }
    }

    /**
     * Create the PlayerMoveStartup to be send at the controller based on player's input in the startup phase of the game.
     *
     * @return Returns the PlayerMoveStartup ready to be forwarded to the controller.
     */
    protected PlayerMoveStartup createPlayerMoveStartup() {
        return new PlayerMoveStartup(currentMove.getNextStartupMove());
    }

    /**
     * Handle the move received from server that are meant for me.
     *
     * @param message The message received from the server to handle.
     */
    private void handleMessageForMe(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == StartupActions.COLOR_REQUEST)
                showMessage(ViewMessage.colorRequest + getAvailableColorBuilder(message.getAvailableColor()));
            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_COLOR) {
                showMessage(ViewMessage.pickLastColor + getAvailableColorBuilder(message.getAvailableColor()));
                new Thread(() -> handleResponse(message.getAvailableColor().get(0).getColorAsString())).start();
            } else if (message.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST)
                showMessage(ViewMessage.chooseCardRequest + getAvailableCardsBuilder(message.getAvailableCards()));
            else if (message.getNextStartupMove() == StartupActions.PICK_UP_CARD_REQUEST)
                showMessage(ViewMessage.pickUpCardRequest + getAvailableCardsBuilder(message.getAvailableCards()));
            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_CARD) {
                showMessage(ViewMessage.pickLastCard + message.getAvailableCards().get(0).getCardName().toUpperCase());
                new Thread(() -> handleResponse(message.getAvailableCards().get(0).getCardName())).start();
            } else if (message.getNextStartupMove() == StartupActions.PLACE_WORKER_1 ||
                    message.getNextStartupMove() == StartupActions.PLACE_WORKER_2) {
                refreshView(message.getBoardCopy(), chosenUserInterface);
                showMessage(ViewMessage.placeWorker);
            }
        } else {
            refreshView(message.getBoardCopy(), chosenUserInterface);

            if (message.isStuck())
                showErrorMessage(ViewMessage.stuck);

            if (message.getNextMove() == Actions.CHOSE_WORKER)
                showMessage(ViewMessage.choseYourWorker);
            else if (message.getNextMove() == Actions.WIN) {
                if (message.isGameFinish())
                    gameIsFinish = true;
                showMessage(ViewMessage.winner);
                showMessage(ViewMessage.quit);
            } else if (message.getNextMove() == Actions.LOSE) {
                if (message.isGameFinish())
                    gameIsFinish = true;
                showMessage(ViewMessage.lose);
                showMessage(ViewMessage.quit);
            } else if (message.getNextMove() == Actions.MOVE_STANDARD)
                showMessage(ViewMessage.moveStandard);
            else if (message.getNextMove() == Actions.MOVE_NOT_INITIAL_POSITION)
                showMessage(ViewMessage.moveNotInitialPosition);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_FLIP)
                showMessage(ViewMessage.moveOpponentSlotFlip);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_PUSH)
                showMessage(ViewMessage.moveOpponentSlotPush);
            else if (message.getNextMove() == Actions.MOVE_DISABLE_OPPONENT_UP)
                showMessage(ViewMessage.moveDisableOpponentUp);
            else if (message.getNextMove() == Actions.BUILD_STANDARD)
                showMessage(ViewMessage.buildStandard);
            else if (message.getNextMove() == Actions.BUILD_BEFORE)
                showMessage(ViewMessage.buildBefore);
            else if (message.getNextMove() == Actions.BUILD_NOT_SAME_PLACE)
                showMessage(ViewMessage.buildNotSamePlace);
            else if (message.getNextMove() == Actions.BUILD_SAME_PLACE_NOT_DOME)
                showMessage(ViewMessage.buildSamePlaceNotDome);
            else if (message.getNextMove() == Actions.BUILD_DOME_ANY_LEVEL)
                showMessage(ViewMessage.buildDomeAnyLevel);
            else if (message.getNextMove() == Actions.WAIT_FOR_UNDO) {
                showMessage(ViewMessage.undoInFiveSeconds);
                PlayerMove playerMoveToSend = createPlayerMoveCommand(Actions.WAIT_FOR_UNDO);
                sendPlayerMove(playerMoveToSend);
            }
        }
    }

    //Bisogna anche gestire tutti gli aggiornamenti grafici oltre ai messaggi che possono funzionare da log

    /**
     * Handle the move received from server that are meant for another player.
     *
     * @param message The message received from the server to handle.
     */
    private void handleMessageForOthers(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == StartupActions.COLOR_REQUEST)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.colorRequestOthers + getAvailableColorBuilder(message.getAvailableColor()));
//            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_COLOR)
//                showMessage(ViewMessage.pickLastCard);
            else if (message.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.chooseCardRequestOthers);
            else if (message.getNextStartupMove() == StartupActions.PICK_UP_CARD_REQUEST)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.pickUpCardRequestOthers);
//            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_CARD)
//                showMessage(ViewMessage.pickLastCard);
            else if (message.getNextStartupMove() == StartupActions.PLACE_WORKER_1 ||
                    message.getNextStartupMove() == StartupActions.PLACE_WORKER_2) {
                refreshView(message.getBoardCopy(), chosenUserInterface);
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.placeWorkerOthers);
            }
        } else {
            if (message.getNextMove() != Actions.WAIT_FOR_UNDO &&
                    message.getNextMove() != Actions.QUIT)
                refreshView(message.getBoardCopy(), chosenUserInterface);
            if (message.getNextMove() == Actions.CHOSE_WORKER)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.choseYourWorkerOthers);
            else if (message.getNextMove() == Actions.WIN) {
                if (message.isGameFinish())
                    gameIsFinish = true;
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.winOthers);
                showMessage(ViewMessage.quit);
            } else if (message.getNextMove() == Actions.LOSE) {
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.loseOther);
                if (message.isGameFinish()) {
                    gameIsFinish = true;
                    showMessage(ViewMessage.winner);
                    showMessage(ViewMessage.quit);
                }
            } else if (message.getNextMove() == Actions.GAME_END) {
                if (message.isGameFinish())
                    gameIsFinish = true;
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.justQuit);
                showMessage(ViewMessage.quit);
            } else if (message.getNextMove() == Actions.MOVE_STANDARD)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.moveStandardOthers);
            else if (message.getNextMove() == Actions.MOVE_NOT_INITIAL_POSITION)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.moveNotInitialPositionOthers);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_FLIP)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.moveOpponentSlotFlipOthers);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_PUSH)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.moveOpponentSlotPushOthers);
            else if (message.getNextMove() == Actions.MOVE_DISABLE_OPPONENT_UP)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.moveDisableOpponentUpOthers);
            else if (message.getNextMove() == Actions.BUILD_STANDARD)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.buildStandardOthers);
            else if (message.getNextMove() == Actions.BUILD_BEFORE)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.buildBeforeOthers);
            else if (message.getNextMove() == Actions.BUILD_NOT_SAME_PLACE)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.buildNotSamePlaceOthers);
            else if (message.getNextMove() == Actions.BUILD_SAME_PLACE_NOT_DOME)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.buildSamePlaceNotDomeOthers);
            else if (message.getNextMove() == Actions.BUILD_DOME_ANY_LEVEL)
                showMessage(message.getCurrentPlayer().getNickname() + ViewMessage.buildDomeAnyLevelOthers);
        }
    }

    /**
     * Redraws the updated board to show to the player.
     *
     * @param newBoard      The updated board to be shown.
     * @param userInterface User chosen interface.
     */
    public void refreshView(Board newBoard, UserInterface userInterface) {
        if (userInterface == UserInterface.CLI && playerCli != null) {
            clearConsole(); //funziona solo fuori da IntelliJ
            playerCli.refreshBoard(newBoard);
        }
    }

    /**
     * Activate the response reader in the user interface to handle the player's response during the game.
     */
    private void activateReadResponse() {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null)
            playerCli.activateAsyncReadResponse();

        activeReadResponse = true;
    }

//    private void deactivateReadResponse() {
//        if (chosenUserInterface == UserInterface.CLI && playerCli != null)
//            playerCli.deactivateAsyncReadResponse();
//        else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
//        }
//
//        activeReadResponse = false;
//    }

    /**
     * Adds an observer of PlayerMove to the set of observers for this object, provided that it is not the same as some observer already in the set.
     *
     * @param observer An observer to be added (consistently with the generics type declaration).
     */
    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    /**
     * Adds an observer of PlayerMoveStartup to the set of observers for this object, provided that it is not the same as some observer already in the set.
     *
     * @param observer An observer to be added (consistently with the generics type declaration).
     */
    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }

    /**
     * Adds an observer of String to the set of observers for this object, provided that it is not the same as some observer already in the set.
     *
     * @param observer An observer to be added (consistently with the generics type declaration).
     */
    public void addStringObserver(Observer<String> observer) {
        stringSender.addObserver(observer);
    }

    /**
     * Method to override to handle message received by the UpdateTurnMessageReceiver.
     *
     * @param message The message to handle.
     */
    @Override
    protected void handleUpdateTurnMessage(UpdateTurnMessage message) {
        if (!activeReadResponse)
            activateReadResponse();

        if (!(message.getNextMove() == Actions.QUIT && !message.getCurrentPlayer().getNickname().equals(playerOwnerNickname)))
            this.currentMove = message;

        if (message.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
            handleMessageForMe(message);
        else
            handleMessageForOthers(message);
    }

    /**
     * Method to override to handle message received by the StringReceiver.
     *
     * @param messageString The message to handle.
     */
    @Override
    public void handleString(String messageString) {
        showSimultaneousMessage(messageString);
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public StringReceiver getStringReceiver() {
        return stringReceiver;
    }

    /**
     * Clear completely the console.
     */
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception ignored) {
        }
    }
}

