package it.polimi.ingsw.observer;

/**
 * A class can implement the Observer interface when it wants to be informed of changes in observable objects.
 *
 * @param <T> Type of the object to be observed.
 */
public interface Observer<T> {

    /**
     * This method is called whenever the observed object is sent through notifyAll.
     *
     * @param message The observable object to handle with the update.
     */
    void update(T message);
}