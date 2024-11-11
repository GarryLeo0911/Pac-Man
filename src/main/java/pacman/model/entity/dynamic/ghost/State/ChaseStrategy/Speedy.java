package pacman.model.entity.dynamic.ghost.State.ChaseStrategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class Speedy implements ChaseStrategy {
    @Override
    public Vector2D calculateTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition) {
        // Speedy targets four grid spaces ahead of Pac-Man's current direction
        Vector2D target = playerPosition;
        switch (playerDirection) {
            case UP -> target = playerPosition.add(new Vector2D(0, -4 * 16)); // assuming each grid is 16x16 pixels
            case DOWN -> target = playerPosition.add(new Vector2D(0, 4 * 16));
            case LEFT -> target = playerPosition.add(new Vector2D(-4 * 16, 0));
            case RIGHT -> target = playerPosition.add(new Vector2D(4 * 16, 0));
        }
        return target;
    }
}
