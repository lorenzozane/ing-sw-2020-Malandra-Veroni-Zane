package it.polimi.ingsw.model;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.SocketConnection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Player {

    //TODO: Non mi fa impazzire che Player abbia bisogno di un'istanza di game. Si può gestire il colore in modo che non serva?

    private final Game gameInstance;
    private final String nickname;
    private Date birthday;
    private Color playerColor;
    private ArrayList<Worker> workers = new ArrayList<>();
    private GodsCard playerCard;
    private boolean isPlaying;
    private Connection connection;

    public Player(Game gameInstance, String nickname) {
        this.gameInstance = gameInstance;
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

    public void setPlayerColor(String playerColor) {
        switch (playerColor) {
            case "red":
                this.playerColor = Color.ANSI_RED;
                gameInstance.removeColor(Color.ANSI_RED);
                break;
            case "cyan":
                this.playerColor = Color.ANSI_BRIGHT_CYAN;
                gameInstance.removeColor(Color.ANSI_BRIGHT_CYAN);
                break;
            case "yellow":
                this.playerColor = Color.ANSI_YELLOW;
                gameInstance.removeColor(Color.ANSI_YELLOW);
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


    public ArrayList<Worker> getWorkers() {
        return workers;
    }
}
