# Pacman Game Project

This Pacman game is developed using Java and JavaFX. The project implements several design patterns to ensure modularity, maintainability, and extensibility.

## How to Run the Code

1. **Prerequisites**:
    - Ensure you have Java JDK 8 or higher installed.
    - JavaFX must be set up if not included in your Java installation.

2. **Running the Application**:
    - Navigate to the project’s root directory in your terminal.
    - Use the following command to compile and run the application:
      ```bash
      gradle run
      ```
    - If there are issues with JavaFX, verify the JavaFX SDK path is correctly set in your environment. You may need to specify the JavaFX library path if not already included.

3. **Project Structure**:
    - The main application entry point is located in `App.java`.
    - Configurations such as the maze layout are stored in `src/main/resources/config.json`.

## Implemented Design Patterns

### 1. **Strategy Pattern**
- **Purpose**: Defines unique chase behaviors for each ghost type in the game.
- **Files and Classes**:
    - `ChaseStrategy` interface located in `pacman/model/entity/dynamic/ghost/State/ChaseStrategy`.
    - Concrete strategy implementations:
        - `Shadow.java`
        - `Pokey.java`
        - `Bashful.java`
        - `Speedy.java`
    - `GhostImpl.java` in `pacman/model/entity/dynamic/ghost` relies on `ChaseStrategy` to execute ghost chase behaviors.

### 2. **State Pattern**
- **Purpose**: Manages different states for ghosts (e.g., Chase, Scatter, Frightened).
- **Files and Classes**:
    - `State` interface located in `pacman/model/entity/dynamic/ghost/State`.
    - Concrete state implementations:
        - `Chase.java`
        - `Scatter.java`
        - `Frightened.java`
    - `GhostImpl.java` references the `State` interface to switch ghost behaviors dynamically.

### 3. **Observer Pattern**
- **Purpose**: Allows ghosts to observe changes in Blinky’s position for coordinated behavior.
- **Files and Classes**:
    - `BlinkyPositionSubject` interface in `pacman/model/entity/dynamic/ghost/observer`.
    - `BlinkyPositionObserver` interface in `pacman/model/entity/dynamic/ghost/observer`.
    - Concrete implementation in `GhostImpl.java`.

### 4. **Decorator Pattern**
- **Purpose**: Extends collectible functionalities with additional features.
- **Files and Classes**:
    - `Collectable` interface in `pacman/model/entity/staticentity/collectable`.
    - `PelletDecorator` abstract class in `pacman/model/entity/staticentity/collectable`.
    - Concrete decorator example:
        - `PowerPellet.java`, extending `PelletDecorator`.

### 5. **Factory Pattern**
- **Purpose**: Handles the creation of game entities like walls, pellets, and ghosts.
- **Files and Classes**:
    - `RenderableFactoryRegistry` in `pacman/model/factories` maintains factories.
    - Specific factories:
        - `PelletFactory.java`
        - `GhostFactory.java`
        - `WallFactory.java`
        - `PacmanFactory.java`
    - `MazeCreator.java` uses the factory registry to instantiate game entities.