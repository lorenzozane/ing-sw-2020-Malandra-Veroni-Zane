package it.polimi.ingsw.view;

public class ViewMessage {

    //SetUpActions
    public static String colorRequest = "Choose worker's color from these colors: ";
    public static String pickLastColor = "You have been assigned the following color: ";
    public static String chooseCardRequest = "Choose cards from these cards: ";
    public static String pickUpCardRequest = "Pick up your card from these card: ";
    public static String pickLastCard = "You have been assigned the following card: ";
    public static String placeWorker = "Choose worker's position.\n";

    //Actions-MOVE-ForMe
    public static String moveStandard = "Move your selected Worker into one of the neighboring spaces (not containing a Worker or Dome).\n";
    public static String moveNotInitialPosition = "Move your Worker one additional time, but not back to its initial space.\n";
    public static String moveOpponentSlotFlip = "Move your Worker into an opponent Worker’s space by forcing their Worker to the space yours just vacated.\n";
    public static String moveOpponentSlotPush = "Move your Worker into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.\n";
    public static String moveDisableOpponentUp = "Your Workers cannot move up this turn.\n";

    //Actions-BUILD-ForMe
    public static String buildStandard = "Build a block or dome on an unoccupied space neighboring the moved Worker.\n";
    public static String buildBefore = "Build a block or dome on an unoccupied space neighboring the moved Worker before moving.\n";
    public static String buildNotSamePlace = "Build one additional time, but not on the same space.\n";
    public static String buildSamePlaceNotDome = "Build one additional block (not dome) on top of your first block.\n" ;
    public static String buildDomeAnyLevel = "Build a dome at any level.\n" ;

    //Actions-MOVE-ForOthers
    public static String moveStandardOthers = " made a single move.\n";
    public static String moveNotInitialPositionOthers = " made a second move, but not back to its initial space.\n";
    public static String moveOpponentSlotFlipOthers = " he swapped his worker's position with your worker's position.\n";
    public static String moveOpponentSlotPushOthers = " forced your worker to move one space straight back.\n";
    public static String moveDisableOpponentUpOthers = "Your Workers cannot move up this turn.\n";

    //Actions-BUILD-ForOthers
    public static String buildStandardOthers = " built a block or dome.\n";
    public static String buildBeforeOthers = " built a block or dome before moving.\n";
    public static String buildNotSamePlaceOthers = " built one additional time, but not on the same space.\n";
    public static String buildSamePlaceNotDomeOthers = " built one additional block (not dome) on top of his first block.\n" ;
    public static String buildDomeAnyLevelOthers = " built a dome.\n" ;
}
