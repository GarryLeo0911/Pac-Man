package pacman.model.entity.dynamic.ghost.observer;

import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

public interface BlinkyPositionSubject {

    /**
     * Adds an observer to list of observers for subject
     *
     * @param observer observer for PlayerPositionSubject
     */
    void registerObserver(BlinkyPositionObserver observer);

    /**
     * Removes an observer from list of observers for subject
     *
     * @param observer observer for PlayerPositionObserver
     */
    void removeObserver(BlinkyPositionObserver observer);

    /**
     * Notifies observer of change in player's position
     */
    void notifyObservers();
}
