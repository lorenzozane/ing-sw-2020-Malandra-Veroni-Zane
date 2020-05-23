package it.polimi.ingsw.observer;

import java.util.LinkedList;
import java.util.List;

public class Observable<T> {

    private final List<Observer<T>> observers = new LinkedList<>();

    public void addObserver(Observer<T> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer<T> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notifyAll(T message) {
        synchronized (observers) {
            for (Observer<T> observer : observers)
                observer.update(message);
        }
    }
}