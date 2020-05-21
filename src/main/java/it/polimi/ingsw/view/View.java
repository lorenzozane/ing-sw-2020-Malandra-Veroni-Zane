package it.polimi.ingsw.view;

import it.polimi.ingsw.model.TurnEvents;
import it.polimi.ingsw.model.UpdateTurnMessage;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

public class View implements Observer<UpdateTurnMessage> {

    private String playerOwnerNickname;
    private final UserInterface chosenUserInterface;
    private final Cli playerCli;
    private final Gui playerGui;

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

    //TODO: Teo, ho modificato solo i primi due, fai pure tu
    private void handleMessageForMe(UpdateTurnMessage message) {
        if (message.isStartupPhase()) {
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.COLOR_REQUEST)
                showMessage(ViewMessage.colorRequest);
            else if (message.getNextStartupMove() == TurnEvents.SetUpActions.PICK_LAST_COLOR)
                showMessage(ViewMessage.pickLastColor);
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.CHOOSE_CARD_REQUEST){
                System.out.println(ViewMessage.chooseCardRequest);
            }
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.PICK_UP_CARD_REQUEST){
                System.out.println(ViewMessage.pickUpCardRequest);
            }
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.PICK_LAST_CARD){
                System.out.println(ViewMessage.pickLastCard);
            }
            if (message.getNextStartupMove() == TurnEvents.SetUpActions.PLACE_WORKER){
                System.out.println(ViewMessage.placeWorker);
            }
        }else {
            if (message.getNextMove() == TurnEvents.Actions.MOVE_STANDARD){
                System.out.println(ViewMessage.moveStandard);
            }
            if (message.getNextMove() == TurnEvents.Actions.MOVE_NOT_INITIAL_POSITION){
                System.out.println(ViewMessage.moveNotInitialPosition);
            }
            if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_FLIP){
                System.out.println(ViewMessage.moveOpponentSlotFlip);
            }
            if (message.getNextMove() == TurnEvents.Actions.MOVE_OPPONENT_SLOT_PUSH){
                System.out.println(ViewMessage.moveOpponentSlotPush);
            }
            if (message.getNextMove() == TurnEvents.Actions.MOVE_DISABLE_OPPONENT_UP){
                System.out.println(ViewMessage.moveDisableOpponentUp);
            }
            if (message.getNextMove() == TurnEvents.Actions.BUILD_STANDARD){
                System.out.println(ViewMessage.buildStandard);
            }
            if (message.getNextMove() == TurnEvents.Actions.BUILD_BEFORE){
                System.out.println(ViewMessage.buildBefore);
            }
            if (message.getNextMove() == TurnEvents.Actions.BUILD_NOT_SAME_PLACE){
                System.out.println(ViewMessage.buildNotSamePlace);
            }
            if (message.getNextMove() == TurnEvents.Actions.BUILD_SAME_PLACE_NOT_DOME){
                System.out.println(ViewMessage.buildSamePlaceNotDome);
            }
            if (message.getNextMove() == TurnEvents.Actions.BUILD_DOME_ANY_LEVEL){
                System.out.println(ViewMessage.buildDomeAnyLevel);
            }
        }
    }

    private void handleMessageForOthers(UpdateTurnMessage message) {

    }

    @Override
    public void update(UpdateTurnMessage message) {
        if (message.getCurrentPlayer().getNickname().equals(playerOwnerNickname))
            handleMessageForMe(message);
        else
            handleMessageForOthers(message);


    }
}

