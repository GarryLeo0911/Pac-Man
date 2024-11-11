package pacman.model.entity.dynamic.ghost.State.ChaseStrategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class Shadow implements ChaseStrategy {
    @Override
    public Vector2D calculateTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition) {
        // Shadow targets Pac-Man's current position
        return playerPosition;
    }
}
