package it.polimi.ingsw.model;

import java.awt.*;
import java.util.Date;

public class Player {

    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private boolean isFirstPlayer;
    private GodsCard playerCard;
    private boolean isPlaying;

    public Player(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
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

    public GodsCard getPlayerCard() {
        return playerCard;
    }

    public void setPlayerCard(GodsCard playerCard) {
        this.playerCard = playerCard;
    }

    //TODO: Implementare getter e setter
}
