package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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

    //TODO: Sfruttare il metodo in turn / Probabilmente si può togliere
    private void setYoungestPlayer() {
        this.playerList.sort(Comparator.comparing(Player::getBirthday).reversed());   //mette già in ordine di età
        this.challengerPlayer = playerList.get(0);
    }



    public void removePlayerByName(String nickname) throws IllegalAccessException {   //chiamata nel caso si sconnetta dal server prima di iniziare a giocare
        playerList.removeIf(p -> p.getNickname().equals(nickname));
        stopGame();
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public void challenge() {
        //mostrare alla view del challenger tutti gli dei

    }

    private void stopGame() throws IllegalAccessException { //quando si disconnette un player la partita finisce
        if(playerNumber == 2){
            //System.out.println("ramo di stopGame Hai vinto");// al playerList.get(0) (l'unico rimasto)  notificare che ha vinto
        }
        else if(playerNumber == 3){
            for (Player p : playerList) {
                //notificare ai due player rimasti che la partita è finita in pareggio
            }
        }
        else {
            throw new IllegalAccessException();
        }
    }
}


