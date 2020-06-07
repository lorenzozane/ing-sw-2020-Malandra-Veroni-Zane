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

    public ArrayList<PlayerColor> getColorList() {
        return colorList;
    }

    private void reAddColor(PlayerColor color){
        colorList.add(color);
    }

    public void removeColor(PlayerColor delete) {
        if (!colorList.isEmpty())
            colorList.remove(delete);
    }

    public void addPlayer(Player newPlayer) throws IllegalAccessException {
        if (checkPlayer(newPlayer) && playerList.size() < playerNumber) {
            playerList.add(newPlayer);
            if (playerList.size() == playerNumber)
                turn.setPlayerOrder(playerList.toArray(new Player[0]));
        } else
            throw new IllegalAccessException();
    }

    private boolean checkPlayer(Player newPlayer) {
        return !playerList.contains(newPlayer);
    }



    public void removePlayerByName(String nickname) throws IllegalAccessException {   //chiamata nel caso si sconnetta dal server prima di iniziare a giocare
        playerList.removeIf(p -> p.getNickname().equals(nickname));
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

}


