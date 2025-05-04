# AngryFlappyBird
Team Flappy: Ayesha Binte Khalid, Manasvi Agarwal, Yerim Oh

## Overview
The program is an implementation of the game "AngryFlappyBird" which is a modified version of the popular game "Flappy Bird". The game is played by a player who controls a bird through a scrolling scene with the goal of avoiding pipes and pigs while collecting eggs. The player gains points by successfully navigating the bird through pipes and collecting eggs, and loses points when a pig steals an egg. The game has different levels with varying difficulty levels and features such as different pipe lengths and increasing numbers of pigs. The game also has a user interface that includes a button, a level selector, and icons with text descriptions for different game components. The game has custom-designed icons and images for the different characters and components, including an animation for the flappy bird while it is flying. Overall, the program provides a fun and engaging game for players to enjoy.

### Bird
- [x] A player uses the button to control the bird’s flight
- [x] Collects eggs to earn points

### Pipe
- [x] Appears in pairs every fixed amount of time
- [x] One life is taken from the bird when a collision with any pipe occurs

### White Egg
- [x] Randomly on the upward facing pipes. Could be collected either by the bird or the pig
- [x] If an egg is collected, points will be added
- [x] If a pig collects an egg, points are lost

### Golden Egg
- [x] Randomly on the upward facing pipes Could be collected either by the bird or the pig
- [x] If a golden egg is collected, **6 seconds of autopilot mode** will be triggered.
- [x] If a pig collects an wreath, points are lost

### Pig
- [x] Randomly falls from downward facing pipes
- [x] Could collect eggs right beneath it and lead to points lost if the egg is not collected by the bird first
- [x] The game is over and score is reset to 0 if the bird collides with a pig

### Floor
- [x] Scrolls through the scene consistently during the game until a collision happens.
- [x] The game is over and the score is reset to 0 if the bird collides with the floor.
- [x] The bird stops moving immediately upon collision.

### Background
- [x] Changes from day to night periodically.

### UI panel
- [x] A button that controls the start of the game and the wing flap of the bird.
- [x] A selector for the user to choose the difficulty levels. (3 levels - easy, medium, hard)
- [x] An icon with text description of the white egg, golden egg and the pigs is required.

### Keep score and lives
- [x] Show score and lives text
- [x] Keep score and lives accumulative

## Example

Here's an example output you might see after you run the program:

#### Level selection and starting the game
![Alt text](https://github.com/yerimoh-23/AngryFlappyBird/blob/master/GIFfiles/levelSelect.GIF)

#### How to control the bird's flight
![Alt text](https://github.com/yerimoh-23/AngryFlappyBird/blob/master/GIFfiles/birdFlying.GIF)

#### Bird collecting a white egg
![Alt text](https://github.com/yerimoh-23/AngryFlappyBird/blob/master/GIFfiles/bonusEgg.GIF)

#### Bird collecting a golden egg and the autopilot mode begins
![Alt text](https://github.com/yerimoh-23/AngryFlappyBird/blob/master/GIFfiles/autoEgg.GIF)

#### Collision happens
![Alt text](https://github.com/yerimoh-23/AngryFlappyBird/blob/master/GIFfiles/collision.GIF)

## Game Installation
### Step-by-step instructions
on how to install the program, including how to download and configure any required dependencies.
1. Download the game files: You'll first need to download the game files from our GitHub repository.
2. Install the required programming language or game engine: Depending on the implementation of the game, you may need to install Java
3. Install any required libraries or dependencies: Since the game relies on JavaFX, you'll need to install those as well.
4. Configure the game settings: Once you have all the necessary software and dependencies installed, you will need to configure the game settings. This includes things like setting the game difficulty level, or specifying the path to any required game assets such as images.
5. Launch the game: Finally, you should be ready to launch the game! Simply click the angryFlappyBird.java file and run it on Eclipse to start the game. You can also run the game through the command-line interface.

### Requirements
- Programming language: **Java**
- Specific libraries or dependencies for the chosen programming language: **JavaFX Application**
- Assets: **Images** for game scene (score, day/night background), design (floor, pipes) and specific characters (bird, egg, pig)
- A development environment or ID to edit and run the code: **Eclipse**

### Usage
Run the game developed in Java using JAvaFX in Eclipse (make sure you have Java and JavaFX installed on your system). Open a terminal or command prompt and navigate to the directory containing the game files. Run the command ‘java angryFlappyBird' to launch the game. Make sure you have the correct arguments or configuration options in Eclipse. Press the ‘GO’ button in the UI interface to start the game. You can select a particular game level (Easy/Medium/Hard) before pressing ‘GO’.

### Troubleshooting
- **"Missing library or dependency" error**: This error can occur if the necessary libraries or dependencies are not installed on the user's system. To resolve this issue, the user should check the program's documentation or installation instructions for a list of required libraries or dependencies, and ensure that they are installed on their system.
- **"Game not starting" error/”Constructor exception” error**: This error can occur if there is an issue with some missing files including images/class files/etc. To resolve this issue, the user should try installing all the game files in the repository.
- **"Game running slowly" error**: This error can occur if the user's system does not meet the minimum hardware requirements for the game or if there are too many other applications running at the same time. To resolve this issue, the user should close any unnecessary applications, check the game's documentation for hardware requirements, and consider upgrading their system hardware if necessary.
- **"Game crashes unexpectedly" error**: This error can occur if there is a bug or other issue with the program's code. To resolve this issue, the user should check the program's documentation or error logs for more information on the crash, and consider contacting the program's developer for support.
- **"Difficulty understanding how to play the game" issue**: This issue can occur if the game's instructions or user interface are unclear or confusing. To resolve this issue, the user should review the game's ‘overview’ section, and consider providing feedback to the game's developer on how to improve the user experience.
