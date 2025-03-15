package angryflappybird;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//The Application layer
/**
 * The user collects eggs while avoiding pigs and pipes
 * white egg: add 5 points
 * gold egg: auto pilot mode for 6 seconds
 * 3 lives total
 * the bird should not collide with pigs, pipes, and floor
* @author Team Flappy; Yerim, Ayesha, Manasvi
*/

public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
    
    // time related attributes
    private long clickTime, startTime, elapsedTime;   
    private AnimationTimer timer;
    
    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Sprite> pipes;
    private ArrayList<Sprite> pigs;
    private ArrayList<Sprite> bonusEggs;
    private ArrayList<Sprite> autoEggs;
    private ArrayList<Sprite> checkBoxes;
    
    // background components
    private Timeline dayToNight;
    private Timeline nightToDay;
    
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER, AUTO_MODE, COLLISION;
    
    // auto pilot mode
    private Timeline trackTime;
    int second = 6;
    private Label labelAutoTime = new Label();
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private GraphicsContext gc;	
    
    // score and lives
    private int score = 0;
    private Label labelScore = new Label("0");
    private int live = 3;
    private Label labelLive = new Label("3");
    
    // game level button
    private RadioButton gameLevelEasy;
    private RadioButton gameLevelMedium;
    private RadioButton gameLevelHard;
    private String gameLevel = "";
    private Label levelSelected;
    
    // description
    private Text bonusEggDescription = new Text("Bonus points");
    private Text autoEggDescription = new Text("Auto pilot mode");
    private Text pigDescription = new Text("Avoid pigs");
    
    // Start, end game title
    private Label getReady = new Label();
    private Label gameOver = new Label();
    
    /** 
     * the mandatory main method
     * @param args
     * launch the game
     */
    public static void main(String[] args) {
        launch(args);
    }
       
    /** 
     * the start method sets the Stage layer
     * @param primaryStage start the game
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		
		// set Get Ready before the game starts
		DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        getReady.setEffect(ds);
        getReady.setCache(true);
        getReady.setText("Get Ready!");
        getReady.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        getReady.setTextFill(Color.web("#D03A20"));
        getReady.setLayoutX(70);
        getReady.setLayoutY(250);
        
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
		gameScene.getChildren().add(getReady);
		
		// add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);        
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /** 
     * The getContent method sets the Scene layer
     * set UI for game control
     * set RadioButton for difficulty levels of the game
     * add game descriptions with images
     */
    void resetGameControl() {
        
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        // set buttons for game level
        levelSelected = new Label("nothing selected");
        
        gameLevelEasy = new RadioButton("Easy");
        gameLevelMedium = new RadioButton("Medium");
        gameLevelHard = new RadioButton("Hard");
        
        ToggleGroup levelButtons = new ToggleGroup();
        
        gameLevelEasy.setToggleGroup(levelButtons);
        gameLevelMedium.setToggleGroup(levelButtons);
        gameLevelHard.setToggleGroup(levelButtons);
        
        levelButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton selectedLevelButton = (RadioButton)levelButtons.getSelectedToggle();
                if (selectedLevelButton != null) {
                    gameLevel = selectedLevelButton.getText();
                    // change the label
                    levelSelected.setText(gameLevel + " mode selected");
                }
            }
        });
        
        // set descriptions about the game
        ImageView bonusEggDescriptionImg = DEF.IMVIEW.get("bonusEgg");
        bonusEggDescriptionImg.setFitWidth(35);
        bonusEggDescriptionImg.setFitHeight(35);
        HBox bonusEggBox = new HBox(bonusEggDescriptionImg, bonusEggDescription);
        bonusEggBox.setSpacing(10);
        
        ImageView autoEggDescriptionImg = DEF.IMVIEW.get("autoEgg");
        autoEggDescriptionImg.setFitWidth(35);
        autoEggDescriptionImg.setFitHeight(35);
        HBox autoEggBox = new HBox(autoEggDescriptionImg, autoEggDescription);
        autoEggBox.setSpacing(10);
        
        ImageView pigDescriptionImg = DEF.IMVIEW.get("pig");
        pigDescriptionImg.setFitWidth(35);
        pigDescriptionImg.setFitHeight(35);
        HBox pigBox = new HBox(pigDescriptionImg, pigDescription);
        pigBox.setSpacing(10);
        
        gameControl = new VBox();
        gameControl.setSpacing(20);
        gameControl.setPadding(new Insets(10, 10, 10, 10));
        gameControl.getChildren().addAll(DEF.startButton);
        gameControl.getChildren().addAll(gameLevelEasy, gameLevelMedium, gameLevelHard);
        gameControl.getChildren().add(levelSelected);
        // add game descriptions
        gameControl.getChildren().addAll(bonusEggBox, autoEggBox, pigBox);
    }
    
    /** 
     * @param e the user clicked with the mouse
     */
    void mouseClickHandler(MouseEvent e) {
    	if (GAME_OVER) {
            resetGameScene(false);
        }
    	else if (GAME_START){
            clickTime = System.nanoTime();   
        }
    	GAME_START = true;
        CLICKED = true;
    }
    
    /** 
     * Reset the game scene
     * set background for the game
     * set score and lives label on the game scene
     * initialize floor, bird, pipes, pigs, and eggs
     * @param firstEntry
     */
    void resetGameScene(boolean firstEntry) {
    	
    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        AUTO_MODE = false;
        COLLISION = false;
        floors = new ArrayList<>();
        pipes = new ArrayList<>();
        bonusEggs = new ArrayList<>();
        autoEggs = new ArrayList<>();
        pigs = new ArrayList<>();
        checkBoxes = new ArrayList<>();
        Random random = new Random();
        
    	if(firstEntry) {
    		
    		// create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView day = DEF.IMVIEW.get("dayBackground");
            ImageView night = DEF.IMVIEW.get("nightBackground");
            
            // create the game scene
            gameScene = new Group();
            
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
            
            // set score text
            labelScore.setEffect(ds);
            labelScore.setCache(true);
            labelScore.setText(Integer.toString(score));
            labelScore.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
            labelScore.setTextFill(Color.web("#ffffff"));
            labelScore.setLayoutX(15);
            labelScore.setLayoutY(10);
            
            // set live text
            labelLive.setEffect(ds);
            labelLive.setCache(true);
            labelLive.setText(Integer.toString(live) + " lives left");
            labelLive.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
            labelLive.setTextFill(Color.web("#ffffff"));
            labelLive.setLayoutX(240);
            labelLive.setLayoutY(525);
           
            gameScene.getChildren().addAll(day, canvas, labelScore, labelLive, labelAutoTime);
            
            // change backgrounds every 11 seconds
            dayToNight = new Timeline(new KeyFrame(Duration.seconds(11), ev -> {
                gameScene.getChildren().removeAll(day, canvas, labelScore, labelLive, labelAutoTime);
                gameScene.getChildren().addAll(night, canvas, labelScore, labelLive, labelAutoTime);
                if (GAME_OVER) {
                	dayToNight.stop();
                }
            }));
            dayToNight.setCycleCount(Animation.INDEFINITE);
            dayToNight.play();
            
            nightToDay = new Timeline(new KeyFrame(Duration.seconds(22), ev -> {
                gameScene.getChildren().removeAll(night, canvas, labelScore, labelLive, labelAutoTime);
                gameScene.getChildren().addAll(day, canvas, labelScore, labelLive, labelAutoTime);
                if (GAME_OVER) {
                	nightToDay.stop();
                }
            }));
            nightToDay.setCycleCount(Animation.INDEFINITE);
            nightToDay.play();
      	}
    	
    	// initialize floor
    	for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    		
    		int posX = i * DEF.FLOOR_WIDTH;
    		int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
    		floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		floor.render(gc);
    		
    		floors.add(floor);
    	}
        
        // initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird1"));
        blob.render(gc);
        
        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
        
        // initialize pipes
        int posX_pipe;
        int posY_pipe_1 = 373;
        int posY_pipe_2 = 313;
        int posY_pipe_3 = 343;
        
        // initialize eggs
        int posY_egg1 = posY_pipe_1 - DEF.EGG_HEIGHT;
        int posY_egg2 = posY_pipe_2 - DEF.EGG_HEIGHT;
        int posY_egg3 = posY_pipe_3 - DEF.EGG_HEIGHT;
        
        // initialize pigs
        int posY_pig;
        
        // initialize differently by the difficulty
        if (gameLevel == "Easy" || gameLevel == "") {
        	for (int i=0; i<DEF.PIPE_COUNT; i++) {
	    	    posX_pipe = (i+2) * 180;
	    	    
	    	    int rand_num1 = random.nextInt(2); 
	            // random number is to randomly determine the pipe height
	    	    if (rand_num1 == 0) {
	    	    	// set first set of pipes
	    	        Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_1, DEF.IMAGE.get("bottomPipe1"));
	    	        bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	        bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe1"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	        	    	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                int rand_num4 = random.nextInt(10); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 110 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	                
	    	    } else {
	    	    	// set second set of pipes
	    	    	Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_2, DEF.IMAGE.get("bottomPipe2"));
	    	    	bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	    	bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe2"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	                	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(10); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 50 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	    	    }
        	}
        } else if (gameLevel == "Medium") {
        	for (int i=0; i<DEF.PIPE_COUNT; i++) {
	    	    posX_pipe = (i+2) * 180;
	    	    
	    	    int rand_num1 = random.nextInt(2); 
	            // random number is to randomly determine the pipe height
	    	    if (rand_num1 == 0) {
	    	    	// set first set of pipes
	    	        Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_1, DEF.IMAGE.get("bottomPipe1"));
	    	        bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	        bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe1"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	        	    	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(7); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 110 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	                
	    	    } else {
	    	    	// set second set of pipes
	    	    	Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_2, DEF.IMAGE.get("bottomPipe2"));
	    	    	bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	    	bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe2"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	                	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(7); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 50 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	    	    }
        	}
        } else {
	        for (int i=0; i<DEF.PIPE_COUNT; i++) {
	    	    posX_pipe = (i+2) * 180;
	    	    
	    	    int rand_num1 = random.nextInt(3); 
	            // random number is to randomly determine the pipe height
	    	    if (rand_num1 == 0) {
	    	    	// set first set of pipes
	    	        Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_1, DEF.IMAGE.get("bottomPipe1"));
	    	        bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	        bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe1"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	        	    	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg1, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(7); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 110 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	                
	    	    } else if (rand_num1 == 1){
	    	    	// set second set of pipes
	    	    	Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_2, DEF.IMAGE.get("bottomPipe2"));
	    	    	bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	    	bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe2"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	                	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg2, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(7); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 50 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	    	    } else { // for hard level
	    	    	// set second set of pipes
	    	    	Sprite bottomPipe = new Sprite(posX_pipe, posY_pipe_3, DEF.IMAGE.get("bottomPipe3"));
	    	    	bottomPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	    	    	bottomPipe.render(gc);
	                pipes.add(bottomPipe);
	                
	                Sprite topPipe = new Sprite(posX_pipe, 0, DEF.IMAGE.get("topPipe3"));
	                topPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                topPipe.render(gc);
	                pipes.add(topPipe);
	                
	                Sprite checkBox = new Sprite(posX_pipe, 0, DEF.IMAGE.get("checkBox"));
	                checkBox.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	                checkBox.render(gc);
	                checkBoxes.add(checkBox);
	                
	                int rand_num2 = random.nextInt(8); 
	                // random number is to randomly determine the egg appearance
	                if (rand_num2 == 0) {
	                	// set bonus eggs on the bottom pipe
	        	        Sprite bonusEgg = new Sprite(posX_pipe, posY_egg3, DEF.IMAGE.get("bonusEgg"));
	        	        bonusEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        bonusEgg.render(gc);
	        	        bonusEggs.add(bonusEgg);
	                } else {
	                	int rand_num3 = random.nextInt(10); 
	                    // random number is to randomly determine the egg appearance
	                    if (rand_num3 == 0) {
	                    	// set auto eggs on the bottom pipe
	            	        Sprite autoEgg = new Sprite(posX_pipe, posY_egg3, DEF.IMAGE.get("autoEgg"));
	            	        autoEgg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	            	        autoEgg.render(gc);
	            	        autoEggs.add(autoEgg);
	                    }
	                }
	                
	                int rand_num4 = random.nextInt(7); 
	                // random number is to randomly determine the pigs appearance
	                if (rand_num4 == 0) {
	                	posY_pig = 90 - i * 43;
	        	    	// set pigs come down from the top pipe
	        	        Sprite pig = new Sprite(posX_pipe, posY_pig, DEF.IMAGE.get("pig"));
	        	        pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
	        	        pig.render(gc);
	        	        pigs.add(pig);
	                }
	    	    }
	        }
        }
    }

    /** 
     * timer stuff
     */
    class MyTimer extends AnimationTimer {
    	
    	int counter = 0;
    	
    	 @Override
    	 public void handle(long now) {   		 
    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	     
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

    	     if (GAME_START && !AUTO_MODE) {
    	    	 gameScene.getChildren().remove(getReady);
    	    	 gameScene.getChildren().remove(gameOver);
    	    	 
    	    	 if (live == 0) {
                     live = 3;
                     score = 0;
                     labelScore.setText(Integer.toString(score));
                     labelLive.setText(Integer.toString(live)+" lives left");
                 }
    	    	 
    	    	 moveFloor();
      	    	 moveBlob();
    	    	 movePipes();
    	    	 moveCheckBox();
    	    	 moveBonusEggs();
    	    	 moveAutoEggs();
    	    	 movePigs();
    	    	 
    	    	 checkCollisionFloor();
    	    	 checkCollisionPipes();
    	    	 checkPassPipes();
    	    	 checkCollisionPigs();
    	    	 checkCollisionPigsAndEggs();
    	    	 checkCollisionBonusEggs();
    	    	 checkCollisionAutoEggs();
    	     }
    	     
    	     if (GAME_START && AUTO_MODE) {
    	    	 gameScene.getChildren().remove(getReady);

    	    	 moveFloor();
    	    	 stopBlob(); // the bird stops during the auto mode
    	    	 movePipes();
    	    	 moveCheckBox();
    	    	 moveBonusEggs();
    	    	 moveAutoEggs();
    	    	 movePigs();
    	     }
    	 }

    	 
    	 /** 
 	     * step 1: update floor
 	     */
    	 void moveFloor() {
    		
    		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
    				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
    	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    	        	floors.get(i).setPositionXY(nextX, nextY);
    			}
    			floors.get(i).render(gc);
    			floors.get(i).update(DEF.SCENE_SHIFT_TIME);
    		}
    	 }
    	 
    	 /** 
          * step 2: update blob
          */
    	 private void moveBlob() {
    		 
			long diffTime = System.nanoTime() - clickTime;
			if (COLLISION) {
				//blob fly backwards (Ayesha's edits)
				blob.setVelocity(-500, 1300);				
			} else {
				// blob flies upward with animation
				if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
						
					int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
					imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
					blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex+1)));
					blob.setVelocity(0, DEF.BLOB_FLY_VEL);
				}
				// blob drops after a period of time without button click
				else {
					blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
					CLICKED = false;
				}
			}
			
			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
    	 }
    	 
    	 /** 
          * step 3: move pipes
          */
    	 private void movePipes() {
             for(Sprite pipe: pipes) {
                 pipe.setVelocity(-DEF.PIPE_VELOCITY, 0);
                 pipe.update(elapsedTime * DEF.NANOSEC_TO_SEC);
                 pipe.render(gc);
             }
         }
    	  
    	 /** 
          * step 4: move pigs
          */
    	 private void movePigs() {
             for(Sprite pig: pigs) {
            	 pig.setVelocity(-DEF.PIPE_VELOCITY, DEF.PIG_FALL_VEL);
            	 pig.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            	 pig.render(gc);
             }
         }
    	 
    	 /** 
          * step 5: set bonus eggs randomly
          */
    	 private void moveBonusEggs() {
             for(Sprite bonusEgg: bonusEggs) {
            	 bonusEgg.setVelocity(-DEF.PIPE_VELOCITY, 0);
            	 bonusEgg.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            	 bonusEgg.render(gc);
             }
         }
    	 
    	 /** 
          * step 6: set autopilot mode eggs randomly
          */
    	 private void moveAutoEggs() {
             for(Sprite autoEgg: autoEggs) {
            	 autoEgg.setVelocity(-DEF.PIPE_VELOCITY, 0);
            	 autoEgg.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            	 autoEgg.render(gc);
             }
         }
    	 
    	 /** 
          * when the bird gets the auto eggs, it stops flapping
          */
    	 private void stopBlob() {
    		 blob.setImage(DEF.IMAGE.get("bird1"));
    		 blob.setPositionXY(DEF.BLOB_POS_X, 200);
    		 blob.setVelocity(DEF.BLOB_FLY_VEL,0);
    		 blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    		 blob.render(gc);
    	 }
    	 
    	 /** 
          * step 7: set invisible box for checking points
          */
    	 private void moveCheckBox() {
             for(Sprite checkBox: checkBoxes) {
            	 checkBox.setVelocity(-DEF.PIPE_VELOCITY, 0);
            	 checkBox.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            	 checkBox.render(gc);
             }
         }  	 
    	 
    	 /** 
          * check collision with the bird and floor
          */
    	 public void checkCollisionFloor() {
    		 
    		// check collision with floor
			for (Sprite floor: floors) {
				GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
			}
			
			// end the game when the bird hits the floor
			if (GAME_OVER) {
				showHitEffect(); 
				for (Sprite floor: floors) {
					floor.setVelocity(0, 0);
				}
				if (live == 1) {
                    // display game over
                    live = 0;
                    labelLive.setText(Integer.toString(live)+" lives left");
                    score = 0;
                    
            		// set Game Over before the game starts
            		DropShadow ds = new DropShadow();
                    ds.setOffsetY(3.0f);
                    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
                    gameOver.setEffect(ds);
                    gameOver.setCache(true);
                    gameOver.setText("GAME OVER");
                    gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
                    gameOver.setTextFill(Color.web("#D03A20"));
                    gameOver.setLayoutX(70);
                    gameOver.setLayoutY(250);
                    gameScene.getChildren().add(gameOver);
                    
                    resetGameScene(false);
                } else {
                	score = 0;
                	labelScore.setText(Integer.toString(score));
                    live = live - 1;
                    labelLive.setText(Integer.toString(live)+" lives left");
                    resetGameScene(false);
                }
			timer.stop();
			}
    	 }
    	 
    	 /** 
          * check collision with the bird and pipes
          */
    	 public void checkCollisionPipes() {
    		 
    		// check collision with pipes
    		for (Sprite pipe: pipes) {
 				if (blob.intersectsSprite(pipe)) {
 					COLLISION = true;
 					pipe.setVelocity(0, 0);
 				}
 			}
    	 }
    	 
    	 /** 
          * add score when bird passes each pipe
          */
    	 public void checkPassPipes() {
    		 Iterator<Sprite> iterator = checkBoxes.iterator();
    		 while (iterator.hasNext()) {
    			 Sprite checkBox = iterator.next();
    			 if (blob.intersectsSprite(checkBox)) {
    				 score = score + 1;
    				 labelScore.setText(Integer.toString(score));
    				 iterator.remove();
    			 }
    		 }
    	 }
    	 
    	 /** 
          * check collision with the bird and pigs
          */
    	 public void checkCollisionPigs() {
    		 
    		 for (Sprite pig: pigs) {
 				if (blob.intersectsSprite(pig)) {
 					COLLISION = true;
 					pig.setVelocity(0, 0);
 				}
 			}
    	 }
    	 
    	 /**
    	 * check collision with the pig and eggs
    	 */
    	 public void checkCollisionPigsAndEggs() {
    		 for (Sprite pig: pigs) {
    			 // check collision with the pig and bonus egg
    			 for (Sprite bonusEgg: bonusEggs) {
    				 if (pig.intersectsSprite(bonusEgg)) {
    					 score = score - 3;
    					 labelScore.setText(Integer.toString(score));
    				 }
    			 }
    			 // check collision with the pig and auto egg
    			 for (Sprite autoEgg: autoEggs) {
    				 if (pig.intersectsSprite(autoEgg)) {
    					 score = score - 3;
    					 labelScore.setText(Integer.toString(score));
    				 }
    			 }
    		 }
    	 }
    	 
    	 /**
     	 * check collision with the bird and bonus egg
     	 */
    	 public void checkCollisionBonusEggs() {
    		 Iterator<Sprite> iterator = bonusEggs.iterator();
    		 while (iterator.hasNext()) {
    			 Sprite bonusEgg = iterator.next();
    			 if (blob.intersectsSprite(bonusEgg)) {
    				 score = score + 5;
    				 labelScore.setText(Integer.toString(score));
    				 iterator.remove();
    			 }
    		 }
    	 }
    	 
    	 /**
     	 * check collision with the bird and auto egg
     	 */
    	 public void checkCollisionAutoEggs() {
    		 Iterator<Sprite> iterator = autoEggs.iterator();
    		 while (iterator.hasNext()) {
    			 Sprite autoEgg = iterator.next();
    			 if (blob.intersectsSprite(autoEgg)) {
    				 autoPilotMode();
    				 iterator.remove();
    			 }
    		 }
    	 }
	
    	 /**
      	 * set auto pilot mode for 6 minutes
      	 * count how many seconds are left for the game to resume
      	 */
    	 public void autoPilotMode() {		
    		 second = 6;
	         AUTO_MODE = true;
	         
    		 DropShadow ds = new DropShadow();
	         ds.setOffsetY(3.0f);
	         ds.setColor(Color.color(0.4f, 0.4f, 0.4f));            
	         // set score text
	         labelAutoTime.setEffect(ds);
	         labelAutoTime.setCache(true);
	         labelAutoTime.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
	         labelAutoTime.setTextFill(Color.web("#ffffff"));
	         labelAutoTime.setLayoutX(50);
	         labelAutoTime.setLayoutY(250);
	         labelAutoTime.setText(Integer.toString(second) + " seconds left");
    		 
	         trackTime = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
	        	 second = second - 1;
	        	 labelAutoTime.setText(Integer.toString(second) + " seconds left");
    				 
	        	 if (second == 0) {
	        		 trackTime.stop();
	         		 AUTO_MODE = false;
	        		 labelAutoTime.setText("");
	        	 }
	         }));
	         trackTime.setCycleCount(Timeline.INDEFINITE);   
	         trackTime.playFromStart();  
    	 }
    	 
    	 
    	 /** 
          * show hit effect when there is a collision
          */
	     private void showHitEffect() {
	        ParallelTransition parallelTransition = new ParallelTransition();
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME/5), gameScene);
	        fadeTransition.setToValue(0);
	        fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
	        fadeTransition.setAutoReverse(true);
	        parallelTransition.getChildren().add(fadeTransition);
	        parallelTransition.play();
	     }
    	 
    } // End of MyTimer class

} // End of AngryFlappyBird Class

