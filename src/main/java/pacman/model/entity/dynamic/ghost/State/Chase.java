package pacman.model.entity.dynamic.ghost.State;

import pacman.model.entity.dynamic.ghost.GhostType;
import pacman.model.entity.dynamic.ghost.State.ChaseStrategy.*;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.Set;

public class Chase implements State {
    private ChaseStrategy strategy;

    public Vector2D getTargetLocation(Vector2D playerPosition, Vector2D ghostPosition, Direction playerDirection, Vector2D blinkyPosition, Vector2D targetCorner, GhostType ghostType, Set<Direction> possibleDirections) {
        if (ghostType == GhostType.BLINKY) {
            strategy = new Shadow();
            return strategy.calculateTargetLocation(playerPosition, ghostPosition, playerDirection, blinkyPosition);
        } else if (ghostType == GhostType.CLYDE) {
            strategy = new Pokey();
            return strategy.calculateTargetLocation(playerPosition, ghostPosition, playerDirection, targetCorner);
        } else if (ghostType == GhostType.INKY) {
            strategy = new Bashful();
            return strategy.calculateTargetLocation(playerPosition, ghostPosition, playerDirection, targetCorner);
        } else {
            strategy = new Speedy();
            return strategy.calculateTargetLocation(playerPosition, ghostPosition, playerDirection, targetCorner);
        }
    }
}
