package it.polimi.ingsw.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an observable object, an observable object can have one or more observers.
 * An observer may be any object that implements interface Observer. After an observable instance changes,
 * an application calling the Observable's notifyAll method causes all of its observers to be notified of the
 * change by a call to their update method.
 *
 * @param <T>Type of the object to be observed.
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new LinkedList<>();

    /**
     * Adds an observer of a specific type to the set of observers for this object, provided that it is not the same as some observer already in the set.
     *
     * @param observer An observer to be added (consistently with the generics type declaration).
     */
    public void addObserver(Observer<T> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the set of observers of this object.
     *
     * @param observer An observer to be removed.
     */
    public void removeObserver(Observer<T> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify all the listening observer the specified message.
     *
     * @param message Any object you wish to notify (consistently with the generics type declaration).
     */
    protected void notifyAll(T message) {
        synchronized (observers) {
            for (Observer<T> observer : observers)
                if (observer != null)
                    observer.update(message);
        }
    }
}