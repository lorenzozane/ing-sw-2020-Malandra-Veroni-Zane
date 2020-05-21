package it.polimi.ingsw.view;

public class ViewMessage {

    //SetUpActions
    public static String colorRequest = "Choose worker's color from these colors: ";
    public static String pickLastColor = "The last color left is: ";
    public static String chooseCardRequest = "Choose cards from these cards: ";
    public static String pickUpCardRequest = "Pick up your card from these card: ";
    public static String pickLastCard = "The last card left is: ";
    public static String placeWorker = "Choose worker's position.";

    //Actions-MOVE
    public static String moveStandard = "Move your selected Worker into one of the neighboring spaces (not containing a Worker or Dome).";
    public static String moveNotInitialPosition = "Move your Worker one additional time, but not back to its initial space.";
    public static String moveOpponentSlotFlip = "Move your Worker into an opponent Worker’s space by forcing their Worker to the space yours just vacated.";
    public static String moveOpponentSlotPush = "Move your Worker into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
    public static String moveDisableOpponentUp = "Your Workers cannot move up this turn.";

    //Actions-BUILD
    public static String buildStandard = "Build a block or dome on an unoccupied space neighboring the moved Worker.";
    public static String buildBefore = "Build a block or dome on an unoccupied space neighboring the moved Worker before moving.";
    public static String buildNotSamePlace = "Build one additional time, but not on the same space.";
    public static String buildSamePlaceNotDome = "Build one additional block (not dome) on top of your first block." ;
    public static String buildDomeAnyLevel = "Build a dome at any level." ;
}
