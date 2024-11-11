package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private static final int NUM_POINTS = 100;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    private final boolean isPowerPellet;

    public PelletFactory(char renderableType) {
        this.isPowerPellet = (renderableType == RenderableType.POWER_PELLET);
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            Vector2D adjustedPosition = isPowerPellet ? position.add(new Vector2D(-8, -8)) : position;
            double width = isPowerPellet ? PELLET_IMAGE.getWidth() * 2 : PELLET_IMAGE.getWidth();
            double height = isPowerPellet ? PELLET_IMAGE.getHeight() * 2 : PELLET_IMAGE.getHeight();

            BoundingBox boundingBox = new BoundingBoxImpl(
                    adjustedPosition,
                    height,
                    width
            );

            Collectable pellet = new Pellet(boundingBox, layer, PELLET_IMAGE, NUM_POINTS);
            Collectable powerPellet = new PowerPellet(pellet);

            return !isPowerPellet
                    ? pellet
                    : powerPellet;

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid pellet configuration | %s", e));
        }
    }
}
