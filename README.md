# Flappy Bird Game - Java Implementation

A classic Flappy Bird game built in Java using Swing framework. Navigate the bird through pipes, avoid obstacles, and maximize your score!

## Project Overview

This is a fully functional Flappy Bird game implementation featuring smooth physics, collision detection, and dynamic gameplay. The game includes customizable falling speeds, alternating backgrounds, and intuitive controls.

## Features

- **Smooth Bird Physics**: Realistic gravity and velocity system
- **Collision Detection**: Accurate collision detection with pipes
- **Dynamic Backgrounds**: Alternating backgrounds every 10 seconds
- **Score Tracking**: Real-time score display
- **Game Over Detection**: Automatic game-over when colliding with pipes or ground
- **Restart Functionality**: Press Space to restart after game over

## Project Structure

### Files

#### `App.java`
The entry point of the application. This file contains the main method that initializes and launches the game window.
- Creates the JFrame (window)
- Sets up the game panel
- Handles window properties (size, title, exit behavior)

#### `FlappyBird.java`
The core game logic file. Contains all game mechanics, rendering, and user interaction.

**Key Components:**
- **Bird Class**: Represents the player's bird with position, dimensions, and image
- **Pipe Class**: Represents obstacles with collision detection states
- **Game Loop**: Runs at 60 FPS using a Swing Timer
- **Event Handlers**: Keyboard input handling for jump mechanics
- **Drawing & Rendering**: Graphics rendering for all game elements

**Important Variables:**
- `gravity`: Controls falling speed (default: 0.5)
- `velocityY`: Bird's vertical velocity
- `velocityX`: Horizontal movement of pipes
- `score`: Player's current score
- `gameOver`: Game state flag

**Key Methods:**
- `move()`: Updates bird and pipe positions, checks collisions
- `draw()`: Renders all game components (background, bird, pipes, score)
- `collision()`: Detects collisions between bird and pipes
- `placePipes()`: Randomly generates pipe obstacles
- `actionPerformed()`: Main game loop callback
- `keyPressed()`: Handles spacebar input for jumping

#### Image Assets
- `flappybird.png`: Bird sprite
- `flappybirdbg1.png`: First background image
- `flappybirdbg2.png`: Second background image
- `toppipe.png`: Top pipe obstacle
- `bottompipe.png`: Bottom pipe obstacle
- `jump.wav`: Sound played when Space is pressed
- `hit.wav`: Sound played when the bird collides with a pipe or falls

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed
- All image files in the same directory as the Java files

### Running the Game

**Option 1: Using Command Line**
```bash
cd path/to/flappy-bird-java
javac *.java
java App
```

**Option 2: Using an IDE (IntelliJ, Eclipse, VS Code)**
1. Open the project folder
2. Compile all Java files
3. Run the `App` class

### Game Controls
- **SPACEBAR**: Make the bird jump/flap
- **Jump at right time**: Avoid pipes and stay between them
- **SPACEBAR (after game over)**: Restart the game

## Gameplay Mechanics

1. **Gravity System**: The bird falls naturally due to gravity (0.5 units per frame)
2. **Jumping**: Pressing space applies an upward velocity of -9 units
3. **Pipes**: Appear every 1.5 seconds at random heights with consistent spacing
4. **Scoring**: Earn 1 point for each pair of pipes successfully passed
5. **Background Switching**: Background alternates between two images every 10 seconds
6. **Game Over**: Triggered by collision with pipes or falling below the screen

## Customization Options

### Adjust Falling Speed
Edit the `gravity` variable in `FlappyBird.java`:
- Increase for faster falling (e.g., `0.7`)
- Decrease for slower falling (e.g., `0.3`)

### Change Jump Force
Edit the `velocityY = -9` value in the `keyPressed()` method:
- Higher negative values = stronger jump
- Lower negative values = weaker jump

### Pipe Appearance Rate
Edit `placePipeTimer` interval (currently 1500ms = 1.5 seconds)

### Background Switch Interval
Edit `backgroundSwitchTimer` interval (currently 10000ms = 10 seconds)

## Technical Details

- **Framework**: Java Swing
- **Graphics**: 2D image rendering
- **Frame Rate**: 60 FPS
- **Event Handling**: KeyListener for user input, ActionListener for timers
- **Collision Algorithm**: AABB (Axis-Aligned Bounding Box) collision detection

## Author

**GitHub**: [https://github.com/ghost-bw](https://github.com/ghost-bw)

## License

This project is open source and available for personal and educational use.

## Future Enhancements

- Sound effects and background music
- High score persistence
- Difficulty levels
- Pause functionality
- Power-ups and special items
