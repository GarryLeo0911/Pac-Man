package pacman.model.entity.dynamic.ghost.State.ChaseStrategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface ChaseStrategy {
    Vector2D calculateTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition);
}
