package pacman.model.entity.dynamic.ghost.State;

import pacman.model.entity.dynamic.ghost.GhostType;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Set;

public class Scatter implements State {

    @Override
    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition, Vector2D targetCorner, GhostType ghostType, Set<Direction> possibleDirections) {
        return targetCorner;
    }
}
