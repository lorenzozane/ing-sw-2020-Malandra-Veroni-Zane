package it.polimi.ingsw.network;

import java.net.Socket;

public interface Connection {

    void closeConnection();

//    void addObserver(Observer<String> observer);

    void asyncSend(Object message);

    Socket getSocket();

}
