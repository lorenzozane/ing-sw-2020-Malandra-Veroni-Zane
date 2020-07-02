package it.polimi.ingsw.view;

/**
 * Class containing all the message to be shown through the View of the players.
 */
public final class ViewMessage {

    //WinActions
    public static String winner = "You win";
    public static String winOthers = " wins the game!";
    public static String lose = "You lose!";
    public static String loseOther = " lose!";

    //SetUpActions
    public static String colorRequest = "Choose worker's color from these colors:";
    public static String pickLastColor = "You have been assigned the following color: ";
    public static String chooseCardRequest = "Choose cards from these cards: ";
    public static String pickUpCardRequest = "Pick up your card from these card: ";
    public static String pickLastCard = "You have been assigned the following card: ";
    public static String placeWorker = "Choose worker's position.";
    public static String undoInFiveSeconds = "You have five seconds to UNDO before your turn ends.";

    //SetUpActions-ForOthers
    public static String colorRequestOthers = " is choosing between these colors:";
    public static String chooseCardRequestOthers = " is choosing the cards to play with.\n";
    public static String pickUpCardRequestOthers = " is choosing his card.\n";
    public static String placeWorkerOthers = " is placing his workers.\n";

    //Actions-SETUP-ForMe
    public static String choseYourWorker = "Chose the worker you want to play with this turn (insert the location of the worker): ";
    public static String choseYourWorkerGui = "Chose the worker you want to play with this turn: ";

    //Actions-SETUP-ForOthers
    public static String choseYourWorkerOthers = " is choosing the worker to play with this turn.\n";

    //Actions-MOVE-ForMe
    public static String moveStandard = "Move your selected Worker into one of the neighboring spaces (not containing a Worker or Dome).\n";
    public static String moveNotInitialPosition = "Move your Worker one additional time, but not back to its initial space (or \"skip\" to skip the move).\n";
    public static String moveOpponentSlotFlip = "Move your Worker into an opponent Worker’s space by forcing their Worker to the space yours just vacated.\n";
    public static String moveOpponentSlotPush = "Move your Worker into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.\n";
    public static String moveDisableOpponentUp = "Move your selected Worker into one of the neighboring spaces (not containing a Worker or Dome). If you move up, other player can't move up this turn.\n";

    //Actions-BUILD-ForMe
    public static String buildStandard = "Build a block or dome on an unoccupied space neighboring the moved Worker.\n";
    public static String buildBefore = "Build a block or dome on an unoccupied space neighboring the moved Worker before moving (or \"skip\" to skip the move).\n";
    public static String buildNotSamePlace = "Build one additional time, but not on the same space (or \"skip\" to skip the move).\n";
    public static String buildSamePlaceNotDome = "Build one additional block (not dome) on top of your first block. If it's not possible, you must skip (or \"skip\" to skip the move).\n";
    public static String buildDomeAnyLevel = "Build a block or dome on an unoccupied space neighboring the moved Worker. You can build a dome at any level (typing \"*coordinates* dome\").\n";
    public static String buildDomeAnyLevelGui = "Build a block or dome on an unoccupied space neighboring the moved Worker. You can build a dome at any level.\n";

    //Actions-MOVE-ForOthers
    public static String moveStandardOthers = " is making a single move.\n";
    public static String moveNotInitialPositionOthers = " is making a second move, but not back to its initial space.\n";
    public static String moveOpponentSlotFlipOthers = " is exchanging his worker's position with another worker's position.\n";
    public static String moveOpponentSlotPushOthers = " is forcing another worker to move one space straight back.\n";
    public static String moveDisableOpponentUpOthers = " is making a single move. If move up, you can not move up this turn.\n";

    //Actions-BUILD-ForOthers
    public static String buildStandardOthers = " is building a block or dome.\n";
    public static String buildBeforeOthers = " is building a block or dome before moving.\n";
    public static String buildNotSamePlaceOthers = " is building one additional time, but not on the same space.\n";
    public static String buildSamePlaceNotDomeOthers = " is building one additional block (not dome) on top of his first block.\n";
    public static String buildDomeAnyLevelOthers = " is building a dome.\n";

    //OtherMessage
    public static String stuck = "Your worker is stuck or can't build. Try to UNDO and maybe chose the other worker!";
    public static String quit = "Write QUIT to exit the game: ";
    public static String quitGui = "QUIT to exit the game.";
    public static String justQuit = " just quit the game!";

    //Error-Message
    public static String wrongTurnMessage = "Error: It's not your turn. Wait your turn to make a move!\n";
    public static String wrongInput = "Error: The input entered is not an accepted command.\n";
    public static String wrongCardChose = "Error: This card is not available or it doesn't exists.\n";
    public static String wrongInputCoordinates = "Error: The coordinates entered are not in a valid format.\n";
    public static String wrongColorChose = "Error: This color doesn't exists or has already been chosen.\n";
    public static String slotOccupied = "Error: This slot isn't empty. Chose another slot for your worker.\n";
    public static String cardAlreadyChoose = "Error: You have already chose these card.\n";
    public static String cannotUndo = "Error: You have not move to UNDO!\n";
    public static String cannotSkipThisMove = "Error: You can't skip this move!\n";
    public static String noWorkerInSlot = "Error: In this slot there are no workers!\n";
    public static String choseNotYourWorker = "Error: You must chose one of your's workers!\n";
    public static String canOnlyUndo = "Error: You can only UNDO now!\n";
    public static String canOnlyQuit = "Error: You can only QUIT now!\n";
}
