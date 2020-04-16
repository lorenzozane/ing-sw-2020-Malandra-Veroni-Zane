package it.polimi.ingsw.network;

import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public interface Connection {

    void closeConnection();

    void addObserver(Observer<String> observer);

    void asyncSend(Object message);


}
