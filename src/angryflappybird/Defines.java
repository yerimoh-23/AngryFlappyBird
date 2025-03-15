package angryflappybird;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

    // coefficients related to the bird
    final int BLOB_WIDTH = 70;
    final int BLOB_HEIGHT = 59;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -45;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 400;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    
    // coefficients related to egg
    final int EGG_WIDTH = 60;
    final int EGG_HEIGHT = 60;
    
 // coefficients related to pig
    final int PIG_WIDTH = 60;
    final int PIG_HEIGHT = 60;
    final int PIG_FALL_VEL = 31;
    
    // coefficients related to the pipes
    final int PIPE_WIDTH = 65;
    final int PIPE_HEIGHT1 = 165;
    final int PIPE_HEIGHT2 = 105;
    final int PIPE_HEIGHT3 = 135;
    final int PIPE_VELOCITY = 130;
    final int PIPE_COUNT = 300;
    final int SCORE_WIDTH = 65;
    final int SCORE_HEIGHT = 570;
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../resources/";
    final String[] IMAGE_FILES = {"dayBackground", "nightBackground", "bird1", "bird2", "bird3", "bird4", "floor", "bottomPipe1", "topPipe1", "bottomPipe2", "topPipe2", "bottomPipe3", "topPipe3", "pig", "bonusEgg", "autoEgg", "checkBox"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    //nodes on the scene graph
    Button startButton;
    
    // constructor
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 6) { //floor
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			}
			else if (i == 2 || i == 3 || i == 4 || i == 5){ //bird
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			}
			else if (i == 7 || i == 10){ //bottomPipe1, topPipe2
				img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT2, false, false);
			}
			else if (i == 8 || i == 9){ //topPipe1, bottomPipe2
				img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT1, false, false);
			}
			else if (i == 11 || i == 12){ //bottomPipe3, topPipe3
				img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT3, false, false);
			}
			else if (i == 13){ //pig
				img = new Image(pathImage(IMAGE_FILES[i]), PIG_WIDTH, PIG_HEIGHT, false, false);
			}
			else if (i == 14 || i == 15){ //eggs
				img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
			}
			else if (i == 16){ //check score box
				img = new Image(pathImage(IMAGE_FILES[i]), SCORE_WIDTH, SCORE_HEIGHT, false, false);
			}
			else { //background
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");
		startButton.setOnAction(event ->{
			//flap.play();
		});
	}
	
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
	
	
}
