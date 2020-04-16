package it.polimi.ingsw.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class Player {

    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private ArrayList<Worker> workers;
//    private ArrayList<TurnEvents> moveSequence;
    private boolean isFirstPlayer;
    private GodsCard playerCard;
    private boolean isPlaying;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isPlaying = true;
        Game game = Game.getInstance();
        game.setPlayerNumber(game.getPlayerNumber() + 1);
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setIsFirstPlayer(){
        this.isFirstPlayer = true;
    }

    public boolean getIsFirstPlayer(){
        return isFirstPlayer;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public void setWorkerInBoard(Worker worker, Slot slot) throws IllegalAccessError{     //oppure chiamare il metodo passando entrambi i worker e settandoli entrambi
        if(workers.contains(worker))
            worker.setWorkerSlot(slot);
    }

    public GodsCard getPlayerCard() {
        return playerCard;
    }

    public void setPlayerCard(GodsCard playerCard) {
        this.playerCard = playerCard;
    }

//    public void setupGame(){
//        if (isFirstPlayer == true){
//            Deck deck = new Deck();
//        }
//        return;
//    }
}
