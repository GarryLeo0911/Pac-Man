package pacman.model.entity.dynamic.ghost;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.State.Chase;
import pacman.model.entity.dynamic.ghost.State.Frightened;
import pacman.model.entity.dynamic.ghost.State.Scatter;
import pacman.model.entity.dynamic.ghost.State.State;
import pacman.model.entity.dynamic.ghost.observer.BlinkyPositionObserver;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private static final int minimumDirectionCount = 8;
    private Layer layer = Layer.FOREGROUND;
    private Image image;
    private final Image originalImage;
    private final Image frightenedImage;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private final Set<BlinkyPositionObserver> observers;
    private Vector2D blinkyPosition;
    private State state;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private int currentDirectionCount = 0;
    private GhostType ghostType;
    private Direction playerDirection;
    private boolean eaten;

    private static final Map<GhostMode, State> ghostState = new HashMap<>();

    static {
        ghostState.put(GhostMode.CHASE, new Chase());
        ghostState.put(GhostMode.SCATTER, new Scatter());
        ghostState.put(GhostMode.FRIGHTENED, new Frightened());
    }

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, GhostType ghostType) {
        this.image = image;
        this.originalImage = image;
        this.frightenedImage = new Image("maze/ghosts/frightened.png");
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.state = ghostState.get(ghostMode);
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.ghostType = ghostType;
        this.observers = new HashSet<>();
        this.eaten = false;
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void update() {
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
        if (this.ghostType == GhostType.BLINKY) {
            notifyObservers();
        }
        if (this.ghostMode == GhostMode.FRIGHTENED) {
            this.image = frightenedImage;
        } else {
            this.image = originalImage;
        }
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections) && ghostMode != GhostMode.FRIGHTENED) {
            this.targetLocation = getTargetLocation();
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    private Vector2D getTargetLocation() {
        return state.getTargetLocation(playerPosition, kinematicState.getPosition(), playerDirection, blinkyPosition, targetCorner, ghostType, possibleDirections);
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        if (this.ghostMode == GhostMode.FRIGHTENED) {
            // Filter out the opposite direction to avoid immediate reversal
            List<Direction> directionsList = new ArrayList<>(possibleDirections);
            directionsList.remove(currentDirection != null ? currentDirection.opposite() : null);

            // If no other options are available, reverse
            if (directionsList.isEmpty()) {
                return currentDirection.opposite();
            }

            // Select a random direction from the remaining options
            return directionsList.get(ThreadLocalRandom.current().nextInt(directionsList.size()));
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        this.state = ghostState.get(ghostMode);
        this.kinematicState.setSpeed(speeds.get(ghostMode));
        // ensure direction is switched
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            if (this.ghostMode == GhostMode.FRIGHTENED && !this.eaten) {
                // Ghost is eaten by Pac-Man
                this.eaten = true;
                level.eatGhost();
                respawnGhostAfterDelay();
            } else {
                // Pac-Man loses a life if the ghost is not in FRIGHTENED mode
                level.handleLoseLife();
            }
        }
    }

    private void respawnGhostAfterDelay() {
        // Temporarily stop the ghost from moving
        this.layer = Layer.INVISIBLE;
        this.image = originalImage; // Reset to the original image

        // Use the reset method to respawn the ghost
        this.reset();

        // Create a Timeline for a 1-second delay before respawning
        Timeline respawnTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            this.layer = Layer.FOREGROUND;
            // Set ghost mode and speed to SCATTER mode after respawn
            this.setGhostMode(GhostMode.SCATTER);
        }));

        // Start the Timeline to trigger the delay
        respawnTimeline.setCycleCount(1);
        respawnTimeline.play();
    }


    @Override
    public void update(Vector2D playerPosition, Direction direction) {
        this.playerPosition = playerPosition;
        this.playerDirection = direction;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // return ghost to starting position
        this.eaten = false;
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        this.ghostMode = GhostMode.SCATTER;
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public void registerObserver(BlinkyPositionObserver observer) {
        if (this.ghostType == GhostType.BLINKY) {
            this.observers.add(observer);
            observer.updateBlinkyPosition(this.kinematicState.getPosition());
        }
    }

    @Override
    public void removeObserver(BlinkyPositionObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (BlinkyPositionObserver observer : this.observers) {
            observer.updateBlinkyPosition(this.kinematicState.getPosition());
        }
    }

    @Override
    public void updateBlinkyPosition(Vector2D position) {
        this.blinkyPosition = position;
    }
}
