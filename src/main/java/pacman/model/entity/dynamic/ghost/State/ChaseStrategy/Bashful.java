package pacman.model.entity.dynamic.ghost.State.ChaseStrategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class Bashful implements ChaseStrategy{
    @Override
    public Vector2D calculateTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition) {
        // Calculate the point two grid spaces ahead of Pac-Man
        Vector2D twoAhead = switch (playerDirection) {
            case UP -> playerPosition.add(new Vector2D(0, -2 * 16));
            case DOWN -> playerPosition.add(new Vector2D(0, 2 * 16));
            case LEFT -> playerPosition.add(new Vector2D(-2 * 16, 0));
            case RIGHT -> playerPosition.add(new Vector2D(2 * 16, 0));
        };
        // Calculate vector from Blinky to that point and double it
        double X_distance = 2 * (twoAhead.getX() - blinkyPosition.getX());
        double Y_distance = 2 * (twoAhead.getY() - blinkyPosition.getY());
        double targetX = playerPosition.getX() + X_distance;
        double targetY = playerPosition.getY() + Y_distance;

        return new Vector2D(targetX, targetY);
    }
}
