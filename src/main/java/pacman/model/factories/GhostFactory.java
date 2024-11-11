package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.GhostType;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    private static final Map<Character, Image> IMAGES = new HashMap<>();
    private static final Map<Character, Vector2D> TARGET_CORNERS = new HashMap<>();
    private static final Map<Character, GhostType> GHOST_TYPE = new HashMap<>();

    static {
        IMAGES.put(RenderableType.BLINKY_GHOST, new Image("maze/ghosts/blinky.png"));
        IMAGES.put(RenderableType.INKY_GHOST, new Image("maze/ghosts/inky.png"));
        IMAGES.put(RenderableType.CLYDE_GHOST, new Image("maze/ghosts/clyde.png"));
        IMAGES.put(RenderableType.PINKY_GHOST, new Image("maze/ghosts/pinky.png"));

        // Define target corners for each ghost type
        TARGET_CORNERS.put(RenderableType.PINKY_GHOST, new Vector2D(0, TOP_Y_POSITION_OF_MAP));               // Top-left corner
        TARGET_CORNERS.put(RenderableType.BLINKY_GHOST, new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP)); // Top-right corner
        TARGET_CORNERS.put(RenderableType.CLYDE_GHOST, new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP));            // Bottom-left corner
        TARGET_CORNERS.put(RenderableType.INKY_GHOST, new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)); // Bottom-right corner

        //Define the ghost type
        GHOST_TYPE.put(RenderableType.BLINKY_GHOST, GhostType.BLINKY);
        GHOST_TYPE.put(RenderableType.INKY_GHOST, GhostType.INKY);
        GHOST_TYPE.put(RenderableType.CLYDE_GHOST, GhostType.CLYDE);
        GHOST_TYPE.put(RenderableType.PINKY_GHOST, GhostType.PINKY);
    }

    private final Image GHOST_IMAGE;
    private final Vector2D targetCorner;
    private final GhostType ghostType;

    public GhostFactory(char renderableType) {
        this.GHOST_IMAGE = IMAGES.get(renderableType);
        this.targetCorner = TARGET_CORNERS.get(renderableType);
        this.ghostType = GHOST_TYPE.get(renderableType);
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    GHOST_IMAGE.getHeight(),
                    GHOST_IMAGE.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();

            return new GhostImpl(
                    GHOST_IMAGE,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    targetCorner,
                    ghostType);
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
