package it.polimi.ingsw.model;

import it.polimi.ingsw.network.Connection;

import java.util.ArrayList;
import java.util.Date;

public class Player {

    //TODO: Non mi fa impazzire che Player abbia bisogno di un'istanza di game. Si può gestire il colore in modo che non serva?

    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private ArrayList<Worker> workers = new ArrayList<>();
    private GodsCard playerCard;
    private boolean isPlaying;
    private Connection connection;

    public Player(String nickname) {
        this.nickname = nickname;
        this.isPlaying = true;

        for (int i = 0; i < 2; i++) {
            workers.add(new Worker(this, (i + 1)));
        }
    }

    public String getNickname() {
        return nickname;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    protected void setPlaying(boolean playing) {
        isPlaying = playing;
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

    public ArrayList<Worker> getWorkers() {
        return workers;
    }
}
