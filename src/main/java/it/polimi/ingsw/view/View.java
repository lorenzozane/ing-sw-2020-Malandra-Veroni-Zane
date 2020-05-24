package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.PlayerMoveStartup;
import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

public class View extends MessageForwarder {

    private String playerOwnerNickname;
    private final UserInterface chosenUserInterface;
    private final Cli playerCli;
    private final Gui playerGui;
    private final UpdateTurnMessageReceiver updateTurnMessageReceiver = new UpdateTurnMessageReceiver();
    private final PlayerMoveSender playerMoveSender = new PlayerMoveSender();
    private final PlayerMoveStartupSender playerMoveStartupSender = new PlayerMoveStartupSender();

    //TODO: Mich completa tu i costruttori in base a quello che ti serve
    //Ne ho fatti due in modo da averne uno in caso di creazione CLI e uno GUI (se non serve risistema tu)
    public View(Cli playerCli) {
        this.chosenUserInterface = UserInterface.CLI;
        this.playerCli = playerCli;
        this.playerGui = null;
    }

    public View(Gui playerGui) {
        this.chosenUserInterface = UserInterface.GUI;
        this.playerGui = playerGui;
        this.playerCli = null;
    }

    public void setPlayerOwnerNickname(String playerOwnerNickname) {
        if (this.playerOwnerNickname == null)
            this.playerOwnerNickname = playerOwnerNickname;
    }

    private void showMessage(String messageToShow) {
        if (chosenUserInterface == UserInterface.CLI && playerCli != null)
            playerCli.showMessage(messageToShow);
        else if (chosenUserInterface == UserInterface.GUI && playerGui != null) {
            //TODO: Gui
        }
    }

    private void handleMessageForMe(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == TurnEvents.StartupActions.COLOR_REQUEST)
                showMessage(ViewMessage.colorRequest);
            else if (message.getNextStartupMove() == TurnEvents.StartupActions.PICK_LAST_COLOR)
                showMessage(ViewMessage.pickLastColor);
            else if (message.getNextStartupMove() == TurnEvents.StartupActions.CHOOSE_CARD_REQUEST)
                showMessage(ViewMessage.chooseCardRequest);
            else if (message.getNextStartupMove() == TurnEvents.StartupActions.PICK_UP_CARD_REQUEST)
                showMessage(ViewMessage.pickUpCardRequest);
            else if (message.getNextStartupMove() == TurnEvents.StartupActions.PICK_LAST_CARD)
                showMessage(ViewMessage.pickLastCard);
            else if (message.getNextStartupMove() == TurnEvents.StartupActions.PLACE_WORKER)
                showMessage(ViewMessage.placeWorker);
        }
        else {
            if (message.getNextMove() == TurnEvents.Actions.MOVE_STANDARD)
                showMessage(ViewMessage.moveStandard);
            else if (message.getNextMove() == TurnEvents.Actions.MOVE_NOT_INITIAL_POSITION)
                showMessage(ViewMessage.moveNotInitialPosition);
            else if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_FLIP)
                showMessage(ViewMessage.moveOpponentSlotFlip);
            else if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_PUSH)
                showMessage(ViewMessage.moveOpponentSlotPush);
            else if (message.getNextMove() == TurnEvents.Actions.MOVE_DISABLE_OPPONENT_UP)
                showMessage(ViewMessage.moveDisableOpponentUp);
            else if (message.getNextMove() == TurnEvents.Actions.BUILD_STANDARD)
                showMessage(ViewMessage.buildStandard);
            else if (message.getNextMove() == TurnEvents.Actions.BUILD_BEFORE)
                showMessage(ViewMessage.buildBefore);
            else if (message.getNextMove() == TurnEvents.Actions.BUILD_NOT_SAME_PLACE)
                showMessage(ViewMessage.buildNotSamePlace);
            else if (message.getNextMove() == TurnEvents.Actions.BUILD_SAME_PLACE_NOT_DOME)
                showMessage(ViewMessage.buildSamePlaceNotDome);
            else if (message.getNextMove() == TurnEvents.Actions.BUILD_DOME_ANY_LEVEL)
                showMessage(ViewMessage.buildDomeAnyLevel);
        }
    }

    //TODO: Nel caso la lastMovePerformedBy non sia stata fatta dal playerOwner della view deve anche essere refreshata la board
    //Bisogna anche gestire tutti gli aggiornamenti grafici oltre ai messaggi che possono funzionare da log

    //TODO: Correggere: Così stai dicendo che è stata fatta al turno prima la mossa che dev'essere eseguita in questo turno
    private void handleMessageForOthers(UpdateTurnMessage message) {
        if (message.getNextMove() == TurnEvents.Actions.MOVE_STANDARD)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.moveStandardOthers);
        else if (message.getNextMove() == TurnEvents.Actions.MOVE_NOT_INITIAL_POSITION)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.moveNotInitialPositionOthers);
        else if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_FLIP)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.moveOpponentSlotFlipOthers);
        else if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_PUSH)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.moveOpponentSlotPushOthers);
        else if (message.getNextMove() == TurnEvents.Actions.MOVE_DISABLE_OPPONENT_UP)
            showMessage(ViewMessage.moveDisableOpponentUpOthers);
        else if (message.getNextMove() == TurnEvents.Actions.BUILD_STANDARD)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.buildStandardOthers);
        else if (message.getNextMove() == TurnEvents.Actions.BUILD_BEFORE)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.buildBeforeOthers);
        else if (message.getNextMove() == TurnEvents.Actions.BUILD_NOT_SAME_PLACE)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.buildNotSamePlaceOthers);
        else if (message.getNextMove() == TurnEvents.Actions.BUILD_SAME_PLACE_NOT_DOME)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.buildSamePlaceNotDomeOthers);
        else if (message.getNextMove() == TurnEvents.Actions.BUILD_DOME_ANY_LEVEL)
            showMessage(message.getLastMovePerformedBy() + ViewMessage.buildDomeAnyLevelOthers);
    }

    @Override
    protected void handleUpdateTurn(UpdateTurnMessage message) {
        if (message.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
            handleMessageForMe(message);
        else
            handleMessageForOthers(message);
    }

    public UpdateTurnMessageReceiver getUpdateTurnMessageReceiver() {
        return updateTurnMessageReceiver;
    }

    public void addPlayerMoveObserver(Observer<PlayerMove> observer) {
        playerMoveSender.addObserver(observer);
    }

    public void addPlayerMoveStartupObserver(Observer<PlayerMoveStartup> observer) {
        playerMoveStartupSender.addObserver(observer);
    }
}

