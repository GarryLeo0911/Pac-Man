package pacman.model.entity.dynamic.ghost.observer;

import pacman.model.entity.dynamic.physics.Vector2D;

public interface BlinkyPositionObserver {

    /**
     * Updates observer with the new position of the player
     *
     * @param position the player's position
     */
    void updateBlinkyPosition(Vector2D position);
}
