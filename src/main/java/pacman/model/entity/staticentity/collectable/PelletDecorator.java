package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;

public abstract class PelletDecorator extends StaticEntityImpl implements Collectable {
    protected Collectable pellet;

    public PelletDecorator(Collectable collectable) {
        super(collectable.getBoundingBox(), collectable.getLayer(), collectable.getImage());
        this.pellet = collectable;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public void collect() {
        pellet.collect();
    }

    @Override
    public boolean isCollectable() {
        return pellet.isCollectable();
    }

    @Override
    public int getPoints() {
        return pellet.getPoints();
    }

    @Override
    public boolean isPowerPellet() {
        return false;
    }

    @Override
    public Image getImage() {
        return pellet.getImage();
    }

    @Override
    public double getWidth() {
        return pellet.getWidth();
    }

    @Override
    public double getHeight() {
        return pellet.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return pellet.getPosition();
    }

    @Override
    public Layer getLayer() {
        return pellet.getLayer();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return pellet.getBoundingBox();
    }

    @Override
    public void reset() {
        pellet.reset();
    }
}
