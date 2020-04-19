package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Date;

public class Player {

    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private ArrayList<Worker> workers = new ArrayList<>();
    private boolean isFirstPlayer;
    private GodsCard playerCard;
    private boolean isPlaying;
    private boolean gui = false;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isPlaying = true;

        for (int i = 0; i < 2; i++) {
            workers.add(new Worker(nickname + "_" + (i + 1)));
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

    protected void setIsFirstPlayer() {
        this.isFirstPlayer = true;
    }

    public boolean getIsFirstPlayer() {
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

    public void setPlayerColor(String playerColor) {
        switch (playerColor) {
            case "purple":
                this.playerColor = Color.ANSI_PURPLE;
                Game.getInstance().removeColor(Color.ANSI_PURPLE);
                break;
            case "cyan":
                this.playerColor = Color.ANSI_BRIGHT_CYAN;
                Game.getInstance().removeColor(Color.ANSI_BRIGHT_CYAN);
                break;
            case "blue":
                this.playerColor = Color.ANSI_BLUE;
                Game.getInstance().removeColor(Color.ANSI_BLUE);
                break;
            default:
                throw new IllegalArgumentException();
        }

        for (Worker worker : workers)
            worker.setColor(this.playerColor);
    }

    protected void setWorkerInBoard(Worker worker, Slot slot) throws IllegalAccessError {     //oppure chiamare il metodo passando entrambi i worker e settandoli entrambi
        if (workers.contains(worker))
            worker.setWorkerSlot(slot);
    }

    public GodsCard getPlayerCard() {
        return playerCard;
    }

    public void setPlayerCard(GodsCard playerCard) {
        this.playerCard = playerCard;
    }

    public void setGui(String s) {
        if (s.equals("GUI")) {
            this.gui = true;
        }
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }
}
