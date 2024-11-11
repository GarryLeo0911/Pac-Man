package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PowerPellet extends PelletDecorator {

    public PowerPellet(Collectable pellet) {
        super(pellet);
    }

    @Override
    public boolean isPowerPellet() {
        return true;
    }
}
