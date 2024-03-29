package it.polimi.ingsw.model;


import it.polimi.ingsw.model.Color.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * The active game player, with all it's information.
 */
public class Player implements Serializable {

    private String nickname;
    private Date birthday;
    private PlayerColor playerColor = null;
    private final ArrayList<Worker> workers = new ArrayList<>();
    private GodsCard playerCard;
//    private boolean isPlaying;

    public Player(String nickname) {
        this.nickname = nickname;
//        this.isPlaying = true;

        for (int i = 0; i < 2; i++) {
            workers.add(new Worker(this, (i + 1)));
        }
    }

    public String getNickname() {
        return nickname;
    }

//    public boolean isPlaying() {
//        return isPlaying;
//    }

//    protected void setPlaying(boolean playing) {
//        isPlaying = playing;
//    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;

        for (Worker worker : workers)
            worker.setColor(this.playerColor);
    }

    public GodsCard getPlayerCard() {
        return playerCard;
    }

    public void setPlayerCard(GodsCard playerCard) {
        this.playerCard = playerCard;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }
}
