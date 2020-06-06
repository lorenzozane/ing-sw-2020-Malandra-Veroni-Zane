package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color.PlayerColor;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.TurnEvents.Actions;
import it.polimi.ingsw.model.TurnEvents.StartupActions;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;
import java.util.ArrayList;

public class View extends MessageForwarder {

    private String playerOwnerNickname;
    private final UserInterface chosenUserInterface;
    private RemoteView remoteView;
    private final Cli playerCli;
    private final Gui playerGui;
    private UpdateTurnMessage currentMove = null;
    private boolean activeReadResponse = false;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();
    private final StringSender stringSender = new StringSender();
    private final StringReceiver stringReceiver = new StringReceiver();

    //TODO: Mich completa tu i costruttori in base a quello che ti serve
    //Ne ho fatti due in modo da averne uno in caso di creazione CLI e uno GUI (se non serve risistema tu)
    public View(Cli playerCli) {
        this.chosenUserInterface = UserInterface.CLI;
        this.playerCli = playerCli;
        this.playerCli.setViewOwner(this);
        this.playerGui = null;
    }

    public View(Gui playerGui) {
        this.chosenUserInterface = UserInterface.GUI;
        this.playerGui = playerGui;
        this.playerCli = null;
    }

    //TODO: Non l'abbiamo collegato?
    public void setRemoteView(RemoteView remoteView) {
        if (this.remoteView == null)
            this.remoteView = remoteView;
    }

    public void setPlayerOwnerNickname(String playerOwnerNickname) {
        if (this.playerOwnerNickname == null)
            this.playerOwnerNickname = playerOwnerNickname;
    }

    public void showErrorMessage(String errorToShow) {
        showMessage(errorToShow);
    }

    public void showMessage(String messageToShow) {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null) {
            playerCli.showMessage(messageToShow);
        } else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            //TODO: Gui
        }

        if (messageToShow.contains("Error: "))
            repeatCurrentMove(currentMove);
    }

    public void showSimultaneousMessage(String messageToShow) {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null) {
            playerCli.showSimultaneousMessage(messageToShow);
        } else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            //TODO: Gui
        }

        if (messageToShow.contains("Error: "))
            repeatCurrentMove(currentMove);
    }

    private void repeatCurrentMove(UpdateTurnMessage currentMove) {
        handleUpdateTurnFromSocket(currentMove);

//        if (lastMove.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
//            handleMessageForMe(currentMove);
//        else
//            handleMessageForOthers(currentMove);
    }

    public void handleResponse(String response) {
        if (currentMove == null) {
            //TODO: Implementare gestione risposte pre partita (a posto così?)
            stringSender.notifyAll(response);

        } else if (currentMove.getCurrentPlayer().getNickname().equals(playerOwnerNickname)) {
            if (currentMove.isStartupPhase()) {
                PlayerMoveStartup moveStartupToSend = createPlayerMoveStartup();

                if (currentMove.getNextStartupMove() == StartupActions.COLOR_REQUEST) {
                    PlayerColor chosenPlayerColor = convertStringToPlayerColor(response);
                    if (chosenPlayerColor == null) {
                        showMessage(ViewMessage.wrongInput);
                        return;
                    } else {
                        moveStartupToSend.setChosenColor(chosenPlayerColor);
                    }
                } else if (currentMove.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST ||
                        currentMove.getNextStartupMove() == StartupActions.PICK_UP_CARD_REQUEST) {
                    moveStartupToSend.setChosenCard(response);
                } else if (currentMove.getNextStartupMove() == StartupActions.PLACE_WORKER) {
                    moveStartupToSend.setWorkerPosition(convertStringToPosition(response));
                }

                sendPlayerMoveStartup(moveStartupToSend);
            } else {
                PlayerMove playerMoveToSend = createPlayerMove(convertStringToPosition(response));
                convertStringToPosition(response);
                sendPlayerMove(playerMoveToSend);
            }
        } else
            showMessage(Message.wrongTurnMessage);
    }

    private void sendPlayerMove(PlayerMove playerMove) {
        playerMoveSender.notifyAll(playerMove);
    }

    private void sendPlayerMoveStartup(PlayerMoveStartup playerMoveStartup) {
        playerMoveStartupSender.notifyAll(playerMoveStartup);
    }

    private Position convertStringToPosition(String coordinates) {
        if (coordinates.length() > 2) {
            showErrorMessage(ViewMessage.wrongInputCoordinates);
            return null;
        }

        int coordinateX = coordinates.charAt(0);
        int coordinateY = coordinates.charAt(1);
        if (coordinateY >= 1 && coordinateY <= 5)
            if (coordinateX >= 65 && coordinateX <= 69)
                return new Position(coordinateX - 65, coordinateY - 1);
            else if (coordinateX >= 97 && coordinateX <= 101)
                return new Position(coordinateX - 97, coordinateY - 1);

        showErrorMessage(ViewMessage.wrongInputCoordinates);
        return null;

//        int x = -1, y = -1;
//        for (int i = 0; i < 5; i++) {
//            if ((int) coordinates.charAt(0) == (i+65)) {
//                x=i;
//            }
//            if ((int) coordinates.charAt(1) == (i+1)) {
//                y=i;
//            }
//        }
//        return new Slot(new Position(x, y));
    }

    private PlayerColor convertStringToPlayerColor(String playerColor) {
        switch (playerColor.toLowerCase()) {
            case "red":
                return PlayerColor.RED;
//                TODO: Queste cose le dovrà fare il GameInitializationManager
//                turn.getCurrentPlayer().setPlayerColor(Color.PlayerColor.RED);
//                gameInstance.removeColor(Color.PlayerColor.RED);
            case "cyan":
                return PlayerColor.CYAN;
            case "yellow":
                return PlayerColor.YELLOW;
            default:
                return null;
        }
    }

    private String getAvailableColorBuilder(ArrayList<PlayerColor> availableColor) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Color.PlayerColor playerColor : availableColor) {
            stringBuilder.append(" ").append(playerColor.getEscape()).append(playerColor.getColorAsString(playerColor)).append(Color.RESET).append(" or");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        return String.valueOf(stringBuilder);
    }

    protected boolean colorChecker(PlayerColor playerColor) {
        return playerColor != null;
    }

    protected PlayerMove createPlayerMove(Position targetSlotPosition) {
        return new PlayerMove(
                        currentMove.getCurrentWorker(),
                        currentMove.getNextMove(),
                        targetSlotPosition,
                        currentMove.getCurrentPlayer().getNickname());
    }

    protected PlayerMoveStartup createPlayerMoveStartup() {
        return new PlayerMoveStartup(
                        currentMove.getCurrentPlayer(),
                        currentMove.getNextStartupMove());

        //TODO: Logica set proprietà
    }

    private void handleMessageForMe(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == StartupActions.COLOR_REQUEST)
                showMessage(ViewMessage.colorRequest + getAvailableColorBuilder(message.getAvailableColor()));
            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_COLOR)
                showMessage(ViewMessage.pickLastColor);
            else if (message.getNextStartupMove() == StartupActions.CHOOSE_CARD_REQUEST)
                showMessage(ViewMessage.chooseCardRequest);
            else if (message.getNextStartupMove() == StartupActions.PICK_UP_CARD_REQUEST)
                showMessage(ViewMessage.pickUpCardRequest);
            else if (message.getNextStartupMove() == StartupActions.PICK_LAST_CARD)
                showMessage(ViewMessage.pickLastCard);
            else if (message.getNextStartupMove() == StartupActions.PLACE_WORKER)
                showMessage(ViewMessage.placeWorker);
        } else {
            if (!message.getLastMovePerformedBy().equals(playerOwnerNickname))
                refreshView(message.getBoardCopy(), chosenUserInterface);
            if (message.getNextMove() == Actions.MOVE_STANDARD)
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
        }
    }

    //TODO: Nel caso la lastMovePerformedBy non sia stata fatta dal playerOwner della view deve anche essere refreshata la board
    //Bisogna anche gestire tutti gli aggiornamenti grafici oltre ai messaggi che possono funzionare da log
    private void handleMessageForOthers(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == StartupActions.COLOR_REQUEST)
                showMessage(message.getCurrentPlayer().getNickname() + " is choosing between these colors " + getAvailableColorBuilder(message.getAvailableColor()));

        } else {
            if (!message.getLastMovePerformedBy().equals(playerOwnerNickname))
                refreshView(message.getBoardCopy(), chosenUserInterface);
            if (message.getNextMove() == Actions.MOVE_STANDARD)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.moveStandardOthers);
            else if (message.getNextMove() == Actions.MOVE_NOT_INITIAL_POSITION)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.moveNotInitialPositionOthers);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_FLIP)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.moveOpponentSlotFlipOthers);
            else if (message.getNextMove() == Actions.MOVE_OPPONENT_SLOT_PUSH)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.moveOpponentSlotPushOthers);
            else if (message.getNextMove() == Actions.MOVE_DISABLE_OPPONENT_UP)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.moveDisableOpponentUpOthers);
            else if (message.getNextMove() == Actions.BUILD_STANDARD)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.buildStandardOthers);
            else if (message.getNextMove() == Actions.BUILD_BEFORE)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.buildBeforeOthers);
            else if (message.getNextMove() == Actions.BUILD_NOT_SAME_PLACE)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.buildNotSamePlaceOthers);
            else if (message.getNextMove() == Actions.BUILD_SAME_PLACE_NOT_DOME)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.buildSamePlaceNotDomeOthers);
            else if (message.getNextMove() == Actions.BUILD_DOME_ANY_LEVEL)
                showMessage(message.getCurrentPlayer().getNickname() + " " + ViewMessage.buildDomeAnyLevelOthers);
        }
    }


    public void refreshView(Board newBoard, UserInterface userInterface) {
        if (userInterface == UserInterface.CLI && playerCli != null)
            playerCli.refreshBoard(newBoard);
        else if (userInterface == UserInterface.GUI && playerGui != null) {
            //TODO: Gui
            //playerGui.refreshBoard()
        }
    }





    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }

    public void addStringObserver(Observer<String> observer){
        stringSender.addObserver(observer);
    }


    @Override
    protected void handleUpdateTurnFromSocket(UpdateTurnMessage message) {
        if (!activeReadResponse)
            activateReadResponse();

        this.currentMove = message;

        if (message.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
            handleMessageForMe(message);
        else
            handleMessageForOthers(message);
    }

    private void activateReadResponse() {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null)
            playerCli.activateAsyncReadResponse();
        else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            //TODO: Gui
        }

        activeReadResponse = true;
    }

    @Override
    public void handleString(String messageString){
        showSimultaneousMessage(messageString);
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public StringReceiver getStringReceiver(){ return stringReceiver;}












}

