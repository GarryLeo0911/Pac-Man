package pacman.model.entity.dynamic.ghost.State.ChaseStrategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class Pokey implements ChaseStrategy {
    @Override
    public Vector2D calculateTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition) {
        // Calculate the distance between Clyde and Pac-Man
        double distance = Vector2D.calculateEuclideanDistance(ghostPosition, playerPosition);
        if (distance > 8 * 16) { // if more than 8 grid spaces away
            return playerPosition;
        } else {
            // Otherwise, target the bottom-left corner
            return new Vector2D(0, 16 * 34);
        }
    }
}
