package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Color.PlayerColor;

import java.util.ArrayList;
import java.util.Comparator;

public class Game {

    private final Turn turn = new Turn(this);
    private ArrayList<Player> playerList = new ArrayList<>();
    private int playerNumber = 1;
    private final Board board = new Board();
    private final Deck deck = new Deck(this);
    private Player challengerPlayer;
    private ArrayList<PlayerColor> colorList = new ArrayList<PlayerColor>() {{
        add(PlayerColor.CYAN);
        add(PlayerColor.RED);
        add(PlayerColor.YELLOW);
    }};

    public Game() {

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

    public String getAvailableColor() {
        StringBuilder temp = new StringBuilder();
        for (PlayerColor color : colorList) {
            //TODO: Sarà da spostare nella view
            temp.append(" ").append(color.getEscape()).append(color.getColorAsString(color)).append(Color.RESET).append(" or");
        }
        temp.replace(temp.length() - 2, temp.length(), "");
        return String.valueOf(temp);
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
                turn.setPlayerOrder(playerList.toArray(new Player[0])); //TODO: Check
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

    protected void removePlayer(Player playerToDelete) {
        playerList.remove(playerToDelete);
        //TODO: Check: ??
    }

    //TODO: Buggato. Throws concurrentmodificationexception (usare iterator)
    public void removePlayerByName(String nickname){   //chiamata nel caso si sconnetta dal server prima di iniziare a giocare
        for(Player p : playerList){
            if(p.getNickname().equals(nickname)){
                if(p.getPlayerColor() != null)
                    reAddColor(p.getPlayerColor());
                removePlayer(p);

            }
        }
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public void challenge(){
        //mostrare alla view del challenger tutti gli dei

    }

    public void setup(){



    }

}


