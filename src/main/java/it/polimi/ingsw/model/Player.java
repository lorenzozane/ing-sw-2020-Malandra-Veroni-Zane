package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Date;

public class Player {

    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private final ArrayList<Worker> workers = new ArrayList<>(2);
    private boolean isFirstPlayer;
    private GodsCard playerCard;
    private boolean isPlaying;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isPlaying = true;

        for (int i = 0; i < 2; i++) {
            workers.add(new Worker(nickname + "_" + (i+1)));
        }
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    protected void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    protected void setIsFirstPlayer(){
        this.isFirstPlayer = true;
    }

    public boolean getIsFirstPlayer(){
        return isFirstPlayer;
    }

    public Date getBirthday() {
        return birthday;
    }

    protected void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    protected void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;

        for (Worker worker : workers)
            worker.setColor(this.playerColor);
    }

    protected void setWorkerInBoard(Worker worker, Slot slot) throws IllegalAccessError{     //oppure chiamare il metodo passando entrambi i worker e settandoli entrambi
        if(workers.contains(worker))
            worker.setWorkerSlot(slot);
    }

    protected ArrayList<Worker> getWorkers(){
        return workers;
    }

    public GodsCard getPlayerCard() {
        return playerCard;
    }

    protected void setPlayerCard(GodsCard playerCard) {
        this.playerCard = playerCard;
    }

//    public void setupGame(){
//        if (isFirstPlayer == true){
//            Deck deck = new Deck();
//        }
//        return;
//    }
}
