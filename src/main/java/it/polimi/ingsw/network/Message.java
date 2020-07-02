package it.polimi.ingsw.network;

/**
 * Class containing all the message that can be send to a player.
 */
public final class Message {
    public static String santorini =
                    "                                                \n" +
                    "                    __              __       __ \n" +
                    " .-----.---.-.-----|  |_.-----.----|__.-----|__|\n" +
                    " |__ --|  _  |     |   _|  _  |   _|  |     |  |\n" +
                    " |_____|___._|__|__|____|_____|__| |__|__|__|__|\n" +
                    "                                                \n";

    public static String gameOverCli =
                    " _______                            _______                   \n" +
                    "|     __|.---.-.--------.-----.    |       |.--.--.-----.----.\n" +
                    "|    |  ||  _  |        |  -__|    |   -   ||  |  |  -__|   _|\n" +
                    "|_______||___._|__|__|__|_____|    |_______| \\___/|_____|__| \n" +
                    "                                                              \n";

    public static String chooseNickname = "What is your nickname?";
    public static String chooseNicknameAgain = "Nickname already chosen, try again";
    public static String lobby = "Adding to lobby... it may take few seconds";
    public static String chooseNoPlayer = "Choose number of player, 2 or 3:";
    public static String chooseNoPlayerAgain = "Number of player could be only 2 or 3, choose again";
    public static String birthday = "When is your birthday (dd/MM/yyyy)";
    public static String birthdayAgain = "The date could be only in this format (dd/MM/yyyy)";
    public static String chooseColor = "Choose a color for your worker";
    public static String chooseColorAgain = "Color not available, choose another one between";
    public static String wait = "Waiting for the other players";
    public static String gameLoading = "All players are ready, the game will start soon...";
    public static String gameOver = "Game Over";
    public static String undoFiveSeconds = "You have five seconds to UNDO before your turn ends.";

    //Message about not allowed movement
    public static String error = "Error: Error network";
    public static String wrongTurnMessage = "Error: It's not your turn. Wait your turn to make a move!";
    public static String moveNotAllowed = "Error: This move is not allowed!";
    public static String occupiedCellMessage = "Error: The chosen cell is occupied!";
    public static String domeOccupiedCellMessage = "Error: The chosen cell is occupied by a dome!";
    public static String tooHighMoveMessage = "Error: You cannot go that high! Lay low.";
    public static String notInitialPositionMessage = "Error: You cannot go back to your starting slot!";
    public static String buildNotSamePlaceMessage = "Error: You cannot build again in the same slot!";
    public static String mustBuildSamePlaceMessage = "Error: If you want to build again, you must build in the same slot!";
    public static String cantBuildADomeMessage = "Error: You cannot build a dome as second build!";
    public static String buildMoveError = "Error: You cannot build in this cell!";
    public static String outOfBoardBorderMessage = "Error: Your opponent's worker cannot be pushed off the board!";
    public static String noWorkerInSlot = "Error: In this slot there are no workers!";
    public static String choseNotYourWorker = "Error: You must chose one of your's workers!";
    public static String mustBeOpponentWorker = "Error: You can only move in an opponent worker's slot (or an empty one)!";
    public static String cannotSkipThisMove = "Error: You can't skip this move!";
    public static String cannotUndo = "Error: You have not move to UNDO!";
    public static String canOnlyQuit = "Error: You can only QUIT now!\n";
//    public static String wrongWorkerMessage = "Error: You must use the same worker during the entire turn!";
}
