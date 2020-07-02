package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private final Turn turn = new Turn(this);
    private final ArrayList<Player> playerList = new ArrayList<>();
    private int playerNumber/* = 1*/;
    private final Board board = new Board();
    private final Deck deck = new Deck(this);
    private final ArrayList<PlayerColor> colorList = new ArrayList<>();
    private Player challengerPlayer;

    public Game() {
        colorList.addAll(Arrays.asList(PlayerColor.values()));
    }

    public Turn getTurn() {
        return turn;
    }

    public Board getBoard() {
        return board.clone();
    }

    public Deck getDeck() {
        return deck;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Allows to know which PlayerColor are available to be chosen from the player.
     *
     * @return Returns the ArrayList containing the available color selectable.
     */
    public ArrayList<PlayerColor> getColorList() {
        return colorList;
    }

//    private void reAddColor(PlayerColor color){
//        colorList.add(color);
//    }

    /**
     * When a PlayerColor is chosen by a player is removed from the available color.
     *
     * @param delete The PlayerColor to be removed.
     */
    public void removeColor(PlayerColor delete) {
        if (!colorList.isEmpty())
            colorList.remove(delete);
    }

    /**
     * Add the player instance to the game.
     *
     * @param newPlayer The player to be added to the game.
     * @throws IllegalAccessException Exception thrown if the player already exists or the number of player is greater
     * than the playerNumber.
     */
    public void addPlayer(Player newPlayer) throws IllegalAccessException {
        if (checkPlayer(newPlayer) && playerList.size() < playerNumber) {
            playerList.add(newPlayer);
            if (playerList.size() == playerNumber)
                turn.setPlayerOrder(playerList.toArray(new Player[0]));
        } else
            throw new IllegalAccessException();
    }

    /**
     * Check if the player is already contained in the list of active players.
     *
     * @param newPlayer The player to be verified.
     * @return Returns a confirmation that the player is active in game or not.
     */
    private boolean checkPlayer(Player newPlayer) {
        return !playerList.contains(newPlayer);
    }

    /**
     * Remove the player with the name specified as parameter from the list of active players.
     *
     * @param nickname The nickname of the player you want to remove from the list of active players.
     */
    public void removePlayerByName(String nickname) {   //chiamata nel caso si sconnetta dal server prima di iniziare a giocare
        playerList.removeIf(p -> p.getNickname().equals(nickname));
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    /**
     * Select the player instance that coincides with the string passed as parameter.
     *
     * @param playerNickname The nickname of the player you want to receive as Player instance.
     * @return Returns the player instance with the specified name.
     */
    public Player getPlayerByName(String playerNickname) {
        return playerList.stream().filter(player -> player.getNickname().equalsIgnoreCase(playerNickname)).findFirst().orElse(null);
    }

    /**
     * Select the worker instance that coincides with the string passed as parameter.
     *
     * @param workerId The worker ID of the worker you want to receive as Worker instance.
     * @return Returns the worker instance with the specified name.
     */
    public Worker getWorkerByName(String workerId) {
        Worker workerFounded = null;
        for (Player player : playerList) {
            workerFounded = player.getWorkers().stream().filter(worker -> worker.getIdWorker().equalsIgnoreCase(workerId)).findFirst().orElse(null);
            if (workerFounded != null)
                return workerFounded;
        }

        return null;
    }
}


