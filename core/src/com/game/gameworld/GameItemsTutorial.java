package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.shapes.MyGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Created by hackintosh on 3/1/17.
 */

public class GameItemsTutorial {
    private Texture background;
    //private Shape[] shape;
    private Shape shape;
    private Shape[] vertical_shapes;
    private Shape[] horizontal_shapes;
    private String direction;
    private int level;
    private int moves;
    private int number_of_shapes_vertical;
    private int number_of_shapes_horizontal;
    private TextureRegion region;
    private int increment;
    private int lala = 0;
    private ImageButton restartButton;
    private ImageButton pauseButton;
    private Drawable drawable;
    private float time;
    private String gameState;
    private MyGame.CallBack callBack;
    private int score;
    private MyGame game;
    private String mode;
    private int connectionPerformed;
    private int wasCopied;
    private boolean vibrate = true;
    Deque<String> stack;
    private String hand_direction;
    private Drawable hand;
    private int hand_x;
    private int hand_y;
    private Skin skin;


    public GameItemsTutorial(final MyGame game, String mode, int level) {
        background = new Texture(Gdx.files.internal("background.png"));
        //shape = new Texture(Gdx.files.internal("shape1.png"));
        this.level = level + 5;
        number_of_shapes_vertical = this.level;
        number_of_shapes_horizontal = number_of_shapes_vertical + 1;
        vertical_shapes = new Shape[number_of_shapes_vertical];
        horizontal_shapes = new Shape[number_of_shapes_horizontal];
        //create_shapes_homogeneous();
        create_tutorial_shapes();
        direction = "NON";
        moves = 0;
        region = null;
        increment = 15;
        time = 0;
        score = 0;
        gameState = "Active";
        this.game = game;
        drawable = new TextureRegionDrawable(new TextureRegion(background,942,290,118,121));
        restartButton = new ImageButton(drawable);
        restartButton.setBounds((float) 0.872 * Gdx.graphics.getWidth(), (float) 0.785 * Gdx.graphics.getHeight(), (float) 0.109 * Gdx.graphics.getWidth(), (float) 0.063 * Gdx.graphics.getHeight());
        restartButton.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState.equals("Active")) {
                    Gdx.app.log("RestartButton","works ");
                    Restart();
                }
            }
        });
        drawable = new TextureRegionDrawable(new TextureRegion(background, 804, 290, 118, 121));
        pauseButton = new ImageButton(drawable);
        pauseButton.setBounds((float) 0.744 * Gdx.graphics.getWidth(), (float) 0.785 * Gdx.graphics.getHeight(), (float) 0.109 * Gdx.graphics.getWidth(), (float) 0.063 * Gdx.graphics.getHeight());
        pauseButton.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState.equals(("Active"))) {
                    Gdx.app.log("PauseButton", "works ");
                    setGameState("Pause");
                }
            }
        });
        this.mode = mode;
        wasCopied = 0;
        connectionPerformed = 0;
        skin = new Skin();
        skin.add("up",new Texture(Gdx.files.internal("up.png")));
        skin.add("down",new Texture(Gdx.files.internal("down.png")));
        skin.add("left",new Texture(Gdx.files.internal("left.png")));
        skin.add("right",new Texture(Gdx.files.internal("right.png")));
        restoreHandCoordinates();
    }

    public void create_tutorial_shapes() {
        String[] vertical_shapes_names = {"Dark/3_54","Dark/10_30","Dark/6_32","Dark/11_10","Light/10_10","Light/18_54"};
        String[] horizontal_shapes_names = {"Light/11_30","Light/6_14","Dark/6_32","Light/3_52","Dark/18_52","Dark/24_04","Light/24_02"};

        for(int i = 0; i < horizontal_shapes_names.length; i++) {
            if(i < vertical_shapes_names.length) {
                vertical_shapes[i] = new Shape("vertical",i,"Shapes/" + vertical_shapes_names[i] + ".png");
            }
            horizontal_shapes[i] = new Shape("horizontal",i,"Shapes/" + horizontal_shapes_names[i] + ".png");
        }
        if(stack != null) {
            stack.clear();
        }
        else {
            stack = new ArrayDeque<String>();
        }
        stack.push("right");
        stack.push("right");
        stack.push("down");
        stack.push("left");
        stack.push("up");
        stack.push("down");
        //stack.push("left");
        hand_direction = "right";
    }

    public void update(float delta) {
        if(gameState.equals("Active")) {
            if (direction.equals("up")) { moveUp();}
            if (direction.equals(("down"))) { moveDown(); }
            if (direction.equals("right")) { moveRight(); }
            if (direction.equals("left")) { moveLeft(); }
            if (mode.equals("Time")) {
                time -= delta;
                if (time < 0) { gameState = "gameOver"; }//callBack.gameOver(score);
            }
            if(mode.equals("Time1")) {
                time += delta;
            }
            moveHand();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(gameState.equals("Active") || gameState.equals("gameOver")) {
                game.getInputMultiplexer().clear();
                game.setScreen(game.getMenuScreen());
                game.getInputMultiplexer().addProcessor(game.getMenuScreen().getStage());
            }
            if(gameState.equals("Pause")) {
                gameState = "Active";
            }
        }
    }

    public String choose_shape() throws IOException {
        FileHandle light = Gdx.files.internal("Shapes/Light/Light.txt");
        FileHandle dark = Gdx.files.internal("Shapes/Dark/Dark.txt");
        BufferedReader bufferedReader_light = new BufferedReader(light.reader());
        BufferedReader bufferedReader_dark = new BufferedReader(dark.reader());
        String light_name = null;
        String dark_name = null;
        String[] Light = new String[24];
        String[] Dark = new String[24];
        int i = 0;

        try {
            while ((light_name = bufferedReader_light.readLine()) != null && (dark_name = bufferedReader_dark.readLine()) != null) {
                Light[i] = light_name;
                Dark[i] = dark_name;
                i++;
            }
        }finally {
            bufferedReader_light.close();
            bufferedReader_dark.close();
        }
        String shape_name = "Shapes/";
        int random_shape;
        Random random = new Random();
        random_shape = random.nextInt(24);
        if(random.nextInt(2) == 0) {
            shape_name += "Dark/";
            shape_name += Dark[random_shape];
        }
        else {
            shape_name +="Light/";
            shape_name +=Light[random_shape];
        }
        shape_name += ".png";

        return shape_name;
    }

    public void create_shapes_homogeneous() {
        List<Integer> vertical_null_elements = new ArrayList<Integer>();
        List<Integer> horizontal_null_elements = new ArrayList<Integer>();
        List<Integer> current_null_elements;
        Shape[] current_array_shapes;
        Random random = new Random();
        int random_shape;
        String reflex_shape_name;
        String orientation;
        int created_shapes = 0;
        int current_shape;
        int number_of_shapes = number_of_shapes_vertical;
        for(int i = 0; i < number_of_shapes + 1; i++) {
            if( i < number_of_shapes && vertical_shapes[i] == null) { vertical_null_elements.add(i); }
            if(i != 2 && horizontal_shapes[i] == null) { horizontal_null_elements.add(i); }
        }
        for(int i = 0; i < vertical_null_elements.size(); i++) {
            Gdx.app.log("Vertical_Shape" + i, "" + vertical_null_elements.get(i));
        }
        for(int i = 0; i < horizontal_null_elements.size(); i++) {
            Gdx.app.log("Horizontal_Shape" + i, "" + horizontal_null_elements.get(i));
        }
        while(created_shapes < 2 * number_of_shapes){
            reflex_shape_name = "Shapes/";
            if(random.nextInt(2) == 0 && vertical_null_elements.size() > 0) {
                current_null_elements = vertical_null_elements;
                current_array_shapes = vertical_shapes;
                orientation = "vertical";
            }
            else {
                if(horizontal_null_elements.size() > 0) {
                    current_null_elements = horizontal_null_elements;
                    current_array_shapes = horizontal_shapes;
                    orientation = "horizontal";
                }
                else {
                    current_null_elements = vertical_null_elements;
                    current_array_shapes = vertical_shapes;
                    orientation = "vertical";
                }
            }
            current_shape = current_null_elements.get(random.nextInt(current_null_elements.size()));
            current_null_elements.remove(current_null_elements.indexOf(current_shape));
            if(current_shape != 2) {
                try{current_array_shapes[current_shape] = new Shape(orientation, current_shape, choose_shape());}
                catch (IOException exception) {Gdx.app.log("Exception", "", exception);}
            }
            else {
                try{vertical_shapes[current_shape] = horizontal_shapes[current_shape] = new Shape(orientation, current_shape, choose_shape());}
                catch (IOException exception) {Gdx.app.log("Exception", "", exception);}
            }
            created_shapes ++;
            if(current_array_shapes[current_shape].name.contains("Light")) {reflex_shape_name += "Dark/";}
            else {reflex_shape_name += "Light/";}
            reflex_shape_name += String.valueOf(current_array_shapes[current_shape].id) + "_";
            for(int j = 0; j < 2; j++) {
                if(current_array_shapes[current_shape].connect.charAt(j) == '0') {reflex_shape_name += "0";}
                else{
                    if(current_array_shapes[current_shape].connect.charAt(j) == '5') { reflex_shape_name += "5"; }
                    else {
                        if(Character.getNumericValue(current_array_shapes[current_shape].connect.charAt(j)) <= 2){
                            reflex_shape_name +=String.valueOf(Character.getNumericValue(current_array_shapes[current_shape].connect.charAt(j)) + 2);
                        }
                        else {reflex_shape_name +=String.valueOf(Character.getNumericValue(current_array_shapes[current_shape].connect.charAt(j)) - 2);}
                    }
                }
            }
            reflex_shape_name += ".png";
            Gdx.app.log(orientation + "_Shape" + current_shape, current_array_shapes[current_shape].name);
            if(random.nextInt(2) == 0 && vertical_null_elements.size() > 0) {
                current_null_elements = vertical_null_elements;
                current_array_shapes = vertical_shapes;
                orientation = "vertical";
            }
            else {
                if(horizontal_null_elements.size() > 0) {
                    current_null_elements = horizontal_null_elements;
                    current_array_shapes = horizontal_shapes;
                    orientation = "horizontal";
                }
                else {
                    current_null_elements = vertical_null_elements;
                    current_array_shapes = vertical_shapes;
                    orientation = "vertical";
                }
            }
            random_shape = current_null_elements.get(random.nextInt(current_null_elements.size()));
            Gdx.app.log(orientation + "_Shape" + random_shape, reflex_shape_name);
            if(random_shape != 2) {current_array_shapes[random_shape] = new Shape(orientation, random_shape, reflex_shape_name);}
            else {vertical_shapes[random_shape] = horizontal_shapes[2] =  new Shape("vertical", random_shape, reflex_shape_name);}
            created_shapes ++;
            current_null_elements.remove(current_null_elements.indexOf(random_shape));
        }
    }


    public void shiftShapes_vertical_up(int ignoreFrom, int ignoreTo) {
        Shape temp = null;
        int number_of_shapes = vertical_shapes.length;
        for(int i = number_of_shapes - 1; i > -1; i--) {
            if(i >= ignoreFrom && i <= ignoreTo && ignoreFrom != 0 && ignoreTo != 0) { continue; }
            if(i != number_of_shapes - 1) {
                if(i == 0) {
                    vertical_shapes[i +1 ] = temp;
                }
                else {
                    vertical_shapes[i + 1] = vertical_shapes[i];
                }
            }
            else{
                temp = vertical_shapes[0];
                vertical_shapes[0] = vertical_shapes[i];
                if(number_of_shapes <= 6 && vertical_shapes[i] != null && ignoreFrom != 0 && ignoreTo != 0) { vertical_shapes[i] = null; }
                if(ignoreFrom !=0 && ignoreTo !=0) {
                    if(mode.equals("Time")) {
                        try { vertical_shapes[i] = new Shape("vertical", i, choose_shape()); }
                        catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                    }
                    if(mode.equals("Moves") || mode.equals("Time1")) {
                        if(number_of_shapes > 6) {
                            vertical_shapes = Arrays.copyOf(vertical_shapes, number_of_shapes - 1);
                            number_of_shapes -= 1;
                        }
                    }
                }
            }
        }
        horizontal_shapes[2] = vertical_shapes[2];
        number_of_shapes_vertical = number_of_shapes;
    }

    public void shiftShapes_vertical_down(int ignoreFrom, int ignoreTo) {
        Shape temp = vertical_shapes[vertical_shapes.length - 1];
        int number_of_shapes = vertical_shapes.length;
        for(int i = 0; i < number_of_shapes; i++) {
            if(i <= ignoreFrom && i >= ignoreTo && ignoreFrom != -1 && ignoreTo != -1) { continue; }
            if(i != 0) {
                if(i == number_of_shapes - 1) {
                    vertical_shapes[i - 1] = temp;
                    if(number_of_shapes <= 6 && vertical_shapes[i] != null && ignoreFrom != -1 && ignoreTo != -1) { vertical_shapes[i] = null; }
                    if(ignoreFrom != -1 && ignoreTo != -1) {
                        if(mode.equals("Time")) {
                            try { vertical_shapes[i] = new Shape("vertical", i, choose_shape()); }
                            catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                        }
                        if(mode.equals("Moves") || mode.equals("Time1")) {
                            if (number_of_shapes > 6) {
                                vertical_shapes = Arrays.copyOf(vertical_shapes, number_of_shapes - 1);
                                number_of_shapes -= 1;
                            }
                        }
                    }
                }
                else {
                    vertical_shapes[i - 1] = vertical_shapes[i];
                }
            }
            else{
                temp = vertical_shapes[number_of_shapes - 1];
                vertical_shapes[number_of_shapes - 1] = vertical_shapes[i];
                //if(ignoreFrom !=0 && ignoreTo !=0) { vertical_shapes[i] = null; }
            }
        }
        horizontal_shapes[2] = vertical_shapes[2];
        number_of_shapes_vertical = number_of_shapes;
    }

    public void shiftShapes_horizontal_right(int ignoreFrom, int ignoreTo) {
        Shape temp = null;
        int number_of_shapes = horizontal_shapes.length;
        for(int i = number_of_shapes - 1; i > -1; i--) {
            if(i >= ignoreFrom && i <= ignoreTo && ignoreFrom != 0 && ignoreTo != 0) { continue; }
            if(i != number_of_shapes - 1) {
                if(i == 0) {
                    horizontal_shapes[i +1 ] = temp;
                }
                else {
                    horizontal_shapes[i + 1] = horizontal_shapes[i];
                }
            }
            else{
                temp = horizontal_shapes[0];
                horizontal_shapes[0] = horizontal_shapes[i];
                if(number_of_shapes <= 6 && horizontal_shapes[i] != null && ignoreFrom != 0 && ignoreTo != 0) { horizontal_shapes[i] = null; }
                if (ignoreFrom != 0 && ignoreTo != 0) {
                    if (mode.equals("Time")) {
                        try { horizontal_shapes[i] = new Shape("horizontal", i, choose_shape()); }
                        catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                    }
                    if (mode.equals("Moves") || mode.equals("Time1")) {
                        if (number_of_shapes > 6) {
                            horizontal_shapes = Arrays.copyOf(horizontal_shapes, number_of_shapes - 1);
                            number_of_shapes -= 1;
                        }
                    }
                }
            }
        }
        vertical_shapes[2] = horizontal_shapes[2];
        number_of_shapes_horizontal = number_of_shapes;
    }

    public void shiftShapes_horizontal_left(int ignoreFrom, int ignoreTo) {
        Shape temp = horizontal_shapes[horizontal_shapes.length - 1];
        int number_of_shapes = horizontal_shapes.length;
        for(int i = 0; i < number_of_shapes; i++) {
            if(i >= ignoreFrom && i <= ignoreTo && ignoreFrom != -1 && ignoreTo != -1) { continue; }
            if(i != 0) {
                if(i == number_of_shapes - 1) {
                    horizontal_shapes[i - 1] = temp;
                    if(number_of_shapes <= 6 && horizontal_shapes[i] != null && ignoreFrom != -1 && ignoreTo != -1) { horizontal_shapes[i] = null; }
                    if(ignoreFrom != -1 && ignoreTo != -1) {
                        if(mode.equals("Time")) {
                            try { horizontal_shapes[i] = new Shape("horizontal", i, choose_shape()); }
                            catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                        }
                        if(mode.equals("Moves") || mode.equals("Time1")) {
                            if (number_of_shapes > 6) {
                                horizontal_shapes = Arrays.copyOf(horizontal_shapes, number_of_shapes - 1);
                                number_of_shapes -= 1;
                            }
                        }
                    }
                }
                else {
                    horizontal_shapes[i - 1] = horizontal_shapes[i];
                }
            }
            else{
                temp = horizontal_shapes[number_of_shapes - 1];
                horizontal_shapes[number_of_shapes - 1] = horizontal_shapes[i];
                //if(ignoreFrom !=0 && ignoreTo !=0) { vertical_shapes[i] = null; }
            }
        }
        vertical_shapes[2] = horizontal_shapes[2];
        number_of_shapes_horizontal = number_of_shapes;
    }

    public void shiftShapes_connected_vertical_up() {
        int number_of_shapes = vertical_shapes.length;
        for(int i = 2; i >= -1; i--) {
            if(i > 0) {
                vertical_shapes[i].dispose();
                vertical_shapes[i] = null;
                horizontal_shapes[2] = null;
            }
            if (i == 0) {
                vertical_shapes[i + 1] = vertical_shapes[i];
            }
            if (i == -1) {
                vertical_shapes[0] = vertical_shapes[number_of_shapes - 1];
                vertical_shapes[number_of_shapes - 1] = null;
                if (mode.equals("Time")) {
                    try { vertical_shapes[number_of_shapes - 1] = new Shape("vertical", number_of_shapes - 1, choose_shape()); }
                    catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                }
                if (mode.equals("Moves") || mode.equals("Time1")) {
                    if(number_of_shapes > 6) {
                        vertical_shapes = Arrays.copyOf(vertical_shapes, number_of_shapes - 1);
                        number_of_shapes = number_of_shapes - 1;
                    }
                }
            }
        }
        number_of_shapes_vertical = number_of_shapes;
    }

    public void shiftShapes_connected_vertical_down() {
        int number_of_shapes = vertical_shapes.length;
        for(int i = 2; i < number_of_shapes; i++) {
            if(i < 4) {
                vertical_shapes[i].dispose();
                vertical_shapes[i] = null;
                horizontal_shapes[2] = null;
            }
            if (i >= 4) {
                vertical_shapes[i - 1] = vertical_shapes[i];
            }
            if (i == number_of_shapes - 1) {
                vertical_shapes[number_of_shapes - 1] = null;
                if (mode.equals("Time")) {
                    try { vertical_shapes[number_of_shapes - 1] = new Shape("vertical", number_of_shapes - 1, choose_shape());}
                    catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                }
                if (mode.equals("Moves") || mode.equals("Time1")) {
                    if (number_of_shapes > 6) {
                        vertical_shapes = Arrays.copyOf(vertical_shapes, number_of_shapes - 1);
                        number_of_shapes = number_of_shapes - 1;
                    }
                }
            }
        }
        number_of_shapes_vertical = number_of_shapes;
    }

    public void shiftShapes_connected_horizontal_right() {
        int number_of_shapes = horizontal_shapes.length;
        for(int i = 2; i >= -1; i--) {
            if(i > 0) {
                horizontal_shapes[i].dispose();
                horizontal_shapes[i] = null;
                vertical_shapes[2] = null;
            }
            if (i == 0) {
                horizontal_shapes[i + 1] = horizontal_shapes[i];
            }
            if (i == -1) {
                horizontal_shapes[0] = horizontal_shapes[number_of_shapes - 1];
                horizontal_shapes[number_of_shapes - 1] = null;
                if (mode.equals("Time")) {
                    try { horizontal_shapes[number_of_shapes - 1] = new Shape("horizontal", number_of_shapes - 1, choose_shape()); }
                    catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                }
                if (mode.equals("Moves") || mode.equals("Time1")) {
                    if(number_of_shapes > 6) {
                        horizontal_shapes = Arrays.copyOf(horizontal_shapes, number_of_shapes - 1);
                        number_of_shapes = number_of_shapes - 1;
                    }
                }
            }
        }
        number_of_shapes_horizontal = number_of_shapes;
    }

    public void shiftShapes_connected_horizontal_left() {
        int number_of_shapes = horizontal_shapes.length;
        for(int i = 0; i < number_of_shapes; i++)
        { Gdx.app.log("Horizontal_shape " + i, "" + horizontal_shapes[i]);}
        for(int i = 2; i < number_of_shapes; i++) {
            if(i < 4) {
                horizontal_shapes[i].dispose();
                horizontal_shapes[i] = null;
                vertical_shapes[2] = null;
            }
            if (i >= 4) {
                horizontal_shapes[i - 1] = horizontal_shapes[i];
            }
            if (i == number_of_shapes - 1) {
                horizontal_shapes[number_of_shapes - 1] = null;
                if (mode.equals("Time")) {
                    try { horizontal_shapes[number_of_shapes - 1] = new Shape("horizontal", number_of_shapes - 1, choose_shape()); }
                    catch (IOException exception) { Gdx.app.log("Exception", "", exception); }
                }
                if (mode.equals("Moves") || mode.equals("Time1")) {
                    if (number_of_shapes > 6) {
                        horizontal_shapes = Arrays.copyOf(horizontal_shapes, number_of_shapes - 1);
                        number_of_shapes = number_of_shapes - 1;
                    }
                }
            }
        }
        number_of_shapes_horizontal = number_of_shapes;
    }


    public void moveUp() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = 0;
        int ignoreTo = 0;
        int wasAnimated =0;
        int number_of_shapes = vertical_shapes.length;
        if(!hand_direction.equals("NONE")) { hand_direction = "NONE"; }
        //int shapes_in_array = 0;
        //if(vertical_shapes[5] == null) { shapes_in_array = count_not_null(vertical_shapes); }
        if(vertical_shapes[2] == null) {
            if(connectionPerformed == 1 || number_of_shapes > 6) {
                moveToCenter = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
            }
        } else {
            vertical_shapes[2].animation = (float) 0.1077 * Gdx.graphics.getHeight();
        }
        if(vertical_shapes[2] != null && vertical_shapes [1] != null) {
            if (vertical_shapes[2].id == vertical_shapes[1].id && (vertical_shapes[2].connect.charAt(0) == '1' || vertical_shapes[2].connect.charAt(0) == '5') && (!vertical_shapes[2].connect.equals(vertical_shapes[1].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
                connectionPerformed = 1;
                vibration();
            }
        }
        if(vertical_shapes[5] == null && vertical_shapes[4] != null && ignoreFrom == 0 && ignoreTo == 0 && number_of_shapes <= 6) {
            vertical_shapes[number_of_shapes - 1] = new Shape(vertical_shapes[number_of_shapes - 2], "vertical");
            wasCopied = 1;
        }
        //Gdx.app.log("Number of shapes","" + number_of_shapes);
        if (vertical_shapes[number_of_shapes - 1] != null)
        {vertical_shapes[number_of_shapes - 1].restoreShape_up();}
        for(int i = 0; i < number_of_shapes; i++) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (vertical_shapes[i] != null) {
                wasAnimated ++;
                Gdx.app.log("Shape" + i,"" + vertical_shapes[i].animated);
                if (vertical_shapes[i].animated < vertical_shapes[i].animation) {
                    if (i == 4) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, 256 - vertical_shapes[i].region_height, vertical_shapes[i].region_width, vertical_shapes[i].region_height);
                        vertical_shapes[i].opacityRegion = new TextureRegion(vertical_shapes[i].opacity,0, 256 - vertical_shapes[i].region_height, vertical_shapes[i].region_width, vertical_shapes[i].region_height);
                    }
                    if (i == number_of_shapes - 1) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, 0, vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
                        vertical_shapes[i].opacityRegion = new TextureRegion(vertical_shapes[i].opacity, 0, 0, vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
                    }
                    if (i != number_of_shapes - 1) {
                        vertical_shapes[i].y += increment;
                    }
                    vertical_shapes[i].animated += increment;
                } else {
                    if (direction.equals("up")) {
                        direction = "NON";
                    }
                    if (i != number_of_shapes - 1) {
                        vertical_shapes[i].position++;
                    } else {
                        vertical_shapes[i].position = 0;
                    }
                    vertical_shapes[i].animated = 0;
                    vertical_shapes[i].region = null;
                    vertical_shapes[i].updateShape_coordinates("vertical");
                }
            }
        }
        if(wasAnimated == 0) {direction = "NON";}
        if(direction.equals("NON")) {
            if(moveToCenter != 1 && connectShapes != 1) {
                moves++;
                if (score > 0) {
                    score -= 10;
                    if(score == 0  && mode.equals("Moves")) { gameState = "gameOver";}
                }
            }
            if (connectShapes == 0 || moveToCenter == 1) {
                shiftShapes_vertical_up(ignoreFrom, ignoreTo);
                if(stack.size() !=0) { hand_direction = stack.pop(); }
                restoreHandCoordinates();
            }
            else { shiftShapes_connected_vertical_up(); score += 100;}
            if(vertical_shapes[0] != null) { vertical_shapes[0].region_height = 256; }
            if(vertical_shapes[5] != null) { vertical_shapes[5].region_height = 256;}
            if(wasCopied == 1) { vertical_shapes[number_of_shapes - 1] = null; wasCopied = 0; }
            if(moveToCenter == 1) {connectionPerformed = 0; }
            number_of_shapes = number_of_shapes_vertical;
            count_null();
            for(int i = 0; i < number_of_shapes; i++) { Gdx.app.log("Vertical_shape " + i, "" + vertical_shapes[i]);}
            if(connectShapes == 1) { direction = "up"; }
        }
    }


    public void moveDown() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = -1;
        int ignoreTo = -1;
        int wasAnimated = 0;
        int number_of_shapes = vertical_shapes.length;
        if(!hand_direction.equals("NONE")) { hand_direction = "NONE"; }
        if(vertical_shapes[2] == null) {
            if(connectionPerformed == 1 || number_of_shapes > 6) {
                moveToCenter = 1;
                ignoreFrom = 2;
                ignoreTo = 0;
            }
        } else {
            vertical_shapes[2].animation = (float) 0.1077 * Gdx.graphics.getHeight();
        }
        if(vertical_shapes[2] != null && vertical_shapes[3] != null) {
            if (vertical_shapes[2].id == vertical_shapes[3].id && (vertical_shapes[2].connect.charAt(0) == '3' || vertical_shapes[2].connect.charAt(0) == '5') && (!vertical_shapes[2].connect.equals(vertical_shapes[3].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = 0;
                vibration();
                connectionPerformed = 1;
            }
        }
        if(vertical_shapes[5] == null && vertical_shapes[0] != null && ignoreFrom == -1 && ignoreTo == -1 && number_of_shapes <= 6) {
            vertical_shapes[number_of_shapes - 1] = new Shape(vertical_shapes[0], "vertical");
            wasCopied = 1;
        }
        //if (vertical_shapes[number_of_shapes - 1] != null)
        //{vertical_shapes[number_of_shapes - 1].restoreShape_up();}
        for(int i = number_of_shapes - 1; i > -1; i--) {
            if ((connectShapes != 0 || moveToCenter !=0) && i <= ignoreFrom && i >= ignoreTo) {
                continue;
            }
            if (vertical_shapes[i] != null) {
                wasAnimated ++;
                if (vertical_shapes[i].animated < vertical_shapes[i].animation) {
                    if (i == 0) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, 0, vertical_shapes[i].region_width, vertical_shapes[i].region_height);
                        vertical_shapes[i].opacityRegion = new TextureRegion(vertical_shapes[i].opacity, 0, 0, vertical_shapes[i].region_width, vertical_shapes[i].region_height);
                    }
                    if (i == 5) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, vertical_shapes[i].region_height , vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
                        vertical_shapes[i].opacityRegion = new TextureRegion(vertical_shapes[i].opacity, 0, vertical_shapes[i].region_height , vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
                    }
                    if (i != 0) {
                        vertical_shapes[i].y -= increment;
                    }
                    vertical_shapes[i].animated += increment;
                } else {
                    if (direction.equals("down")) {
                        direction = "NON";
                    }
                    if (i != 0) {
                        vertical_shapes[i].position--;
                    } else {
                        vertical_shapes[i].position = number_of_shapes - 1;
                        vertical_shapes[i].restoreShape_down(number_of_shapes);
                    }
                    vertical_shapes[i].animated = 0;
                    vertical_shapes[i].region = null;
                    vertical_shapes[i].updateShape_coordinates("vertical");
                }
            }
        }
        if(wasAnimated == 0) {direction = "NON";}
        if(direction.equals("NON")) {
            if(moveToCenter != 1  && connectShapes != 1) {
                moves++;
                if (score > 0) {
                    score -= 10;
                    if(score == 0  && mode.equals("Moves")) { gameState = "gameOver";}
                }
            }
            if(vertical_shapes[0] != null) { vertical_shapes[0].region_height = 256; }
            if(vertical_shapes[5] != null) { vertical_shapes[5].region_height = 256;}
            if (connectShapes == 0 || moveToCenter == 1) {
                shiftShapes_vertical_down(ignoreFrom, ignoreTo);
                if(stack.size() !=0) { hand_direction = stack.pop(); }
                restoreHandCoordinates();
            }
            else { shiftShapes_connected_vertical_down(); score += 100; }
            if(wasCopied == 1) { vertical_shapes[number_of_shapes - 1] = null; wasCopied = 0; }
            if(moveToCenter == 1) {connectionPerformed = 0; }
            number_of_shapes = number_of_shapes_vertical;
            count_null();
            for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("Vertical_shape " + i, "" + vertical_shapes[i]);}
            if(connectShapes == 1) { direction = "down"; }
        }
    }

    public void moveRight() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = 0;
        int ignoreTo = 0;
        int wasAnimated = 0;
        int number_of_shapes = horizontal_shapes.length;
        if(!hand_direction.equals("NONE")) { hand_direction = "NONE"; }
        Gdx.app.log("Number_of_Shapes", "" + number_of_shapes);
        if(horizontal_shapes[2] == null) {
            if(connectionPerformed == 1  || number_of_shapes > 6) {
                moveToCenter = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
            }
        } else {
            horizontal_shapes[2].animation = (float) (0.1925 * Gdx.graphics.getWidth());
        }
        if(horizontal_shapes[2] != null && horizontal_shapes [1] != null) {
            if (horizontal_shapes[2].id == horizontal_shapes[1].id && (horizontal_shapes[2].connect.charAt(1) == '2' || horizontal_shapes[2].connect.charAt(1) == '5') && (!horizontal_shapes[2].connect.equals(horizontal_shapes[1].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
                vibration();
                connectionPerformed = 1;
            }
        }
        if(horizontal_shapes[5] == null && horizontal_shapes[4] != null && ignoreFrom == 0 && ignoreTo == 0 && number_of_shapes <= 6) {
            horizontal_shapes[number_of_shapes - 1] = new Shape(horizontal_shapes[4], "horizontal");
            wasCopied = 1;
        }
        if (horizontal_shapes[number_of_shapes - 1] != null)
        {horizontal_shapes[number_of_shapes - 1].restoreShape_right();}
        for(int i = 0; i < number_of_shapes; i++) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (horizontal_shapes[i] != null) {
                wasAnimated ++;
                if (horizontal_shapes[i].animated < horizontal_shapes[i].animation) {
                    Gdx.app.log("i","" + i);
                    if (i == 4) {
                        //Gdx.app.log("Region width at i=4","" + horizontal_shapes[i].region_width);
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        //Gdx.app.log("Region width at i=4","" + horizontal_shapes[i].region_width);
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], 0, 0, horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                        horizontal_shapes[i].opacityRegion = new TextureRegion(horizontal_shapes[i].opacity, 0, 0, horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                    }
                    if (i == number_of_shapes - 1) {
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], horizontal_shapes[i].region_width, 0, 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                        horizontal_shapes[i].opacityRegion = new TextureRegion(horizontal_shapes[i].opacity, horizontal_shapes[i].region_width, 0, 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                    }
                    if (i != number_of_shapes - 1) {
                        horizontal_shapes[i].x += increment;
                    }
                    horizontal_shapes[i].animated += increment;
                } else {
                    if (direction.equals("right")) {
                        direction = "NON";
                    }
                    if (i != number_of_shapes - 1) {
                        horizontal_shapes[i].position++;
                    } else {
                        horizontal_shapes[i].position = 0;
                    }
                    Gdx.app.log("animated","" + horizontal_shapes[i].animated);
                    horizontal_shapes[i].animated = 0;
                    horizontal_shapes[i].region = null;
                    horizontal_shapes[i].updateShape_coordinates("horizontal");
                }
            }
        }
        if(wasAnimated == 0) {direction = "NON";}
        if(direction.equals("NON")) {
            if(moveToCenter != 1 && connectShapes != 1) {
                moves++;
                if (score > 0) {
                    score -= 10;
                    if(score == 0  && mode.equals("Moves")) { gameState = "gameOver";}
                }
            }
            /*for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("shape " + i, "" + horizontal_shapes[i]);}*/
            if (connectShapes == 0 || moveToCenter == 1) {
                shiftShapes_horizontal_right(ignoreFrom, ignoreTo);
                if(stack.size() !=0) { hand_direction = stack.pop(); }
                restoreHandCoordinates();
            }
            else { shiftShapes_connected_horizontal_right(); score += 100; }
            if(horizontal_shapes[0] != null) { horizontal_shapes[0].region_width = 256; }
            if(horizontal_shapes[5] != null) { horizontal_shapes[5].region_width = 256;}
            if(wasCopied == 1) { horizontal_shapes[number_of_shapes - 1] = null; wasCopied = 0; }
            if(moveToCenter == 1) {connectionPerformed = 0; }
            number_of_shapes = number_of_shapes_horizontal;
            count_null();
            for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("Horizontal_shape " + i, "" + horizontal_shapes[i]);}
            if(connectShapes == 1) { direction = "right"; }
        }
    }

    public void moveLeft() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = -1;
        int ignoreTo = -1;
        int wasAnimated = 0;
        int number_of_shapes = horizontal_shapes.length;
        if(!hand_direction.equals("NONE")) { hand_direction = "NONE"; }
        if(horizontal_shapes[2] == null) {
            if(connectionPerformed == 1  || number_of_shapes > 6) {
                moveToCenter = 1;
                ignoreFrom = 0;
                ignoreTo = 2;
            }
        } else {
            horizontal_shapes[2].animation = (float) (0.1925 * Gdx.graphics.getWidth());
        }
        if(horizontal_shapes[2] != null && horizontal_shapes[3] != null) {
            if (horizontal_shapes[2].id == horizontal_shapes[3].id && (horizontal_shapes[2].connect.charAt(1) == '4' || horizontal_shapes[2].connect.charAt(1) == '5') && (!horizontal_shapes[2].connect.equals(horizontal_shapes[3].connect))) {
                connectShapes = 1;
                ignoreFrom = 0;
                ignoreTo = 2;
                vibration();
                connectionPerformed = 1;
            }
        }
        if(horizontal_shapes[5] == null && horizontal_shapes[0] != null && ignoreFrom == -1 && ignoreTo == -1 && number_of_shapes <= 6) {
            horizontal_shapes[number_of_shapes - 1] = new Shape(horizontal_shapes[0], "horizontal");
            wasCopied = 1;
        }
        //if (vertical_shapes[number_of_shapes - 1] != null)
        //{vertical_shapes[number_of_shapes - 1].restoreShape_up();}


        for(int i = number_of_shapes - 1; i > -1; i--) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (horizontal_shapes[i] != null) {
                wasAnimated ++;
                if (horizontal_shapes[i].animated < horizontal_shapes[i].animation) {
                    if (i == 0) {
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], 256 - horizontal_shapes[i].region_width, 0, horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                        horizontal_shapes[i].opacityRegion = new TextureRegion(horizontal_shapes[i].opacity, 256 - horizontal_shapes[i].region_width, 0, horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                    }
                    if (i == 5) {
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], 0, 0 , 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                        horizontal_shapes[i].opacityRegion = new TextureRegion(horizontal_shapes[i].opacity, 0, 0 , 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
                    }
                    if (i != 0) {
                        horizontal_shapes[i].x -= increment;
                    }
                    horizontal_shapes[i].animated += increment;
                } else {
                    if (direction.equals("left")) {
                        direction = "NON";
                    }
                    if (i != 0) {
                        horizontal_shapes[i].position--;
                    } else {
                        horizontal_shapes[i].position = number_of_shapes - 1;
                        horizontal_shapes[i].restoreShape_left(number_of_shapes);
                    }
                    horizontal_shapes[i].animated = 0;
                    horizontal_shapes[i].region = null;
                    horizontal_shapes[i].updateShape_coordinates("horizontal");
                }
            }
        }
        if(wasAnimated == 0) {direction = "NON";}
        if(direction.equals("NON")) {
            if(moveToCenter != 1 && connectShapes != 1) {
                moves++;
                if (score > 0 && connectionPerformed != 1) {
                    score -= 10;
                    if(score == 0 && mode.equals("Moves")) { gameState = "gameOver";}
                }
            }
            if(horizontal_shapes[0] != null) { horizontal_shapes[0].region_width = 256; }
            if(horizontal_shapes[5] != null) { horizontal_shapes[5].region_width = 256;}
            if (connectShapes == 0 || moveToCenter == 1) {
                shiftShapes_horizontal_left(ignoreFrom, ignoreTo);
                if(stack.size() !=0) { hand_direction = stack.pop(); }
                restoreHandCoordinates();
            }
            else { shiftShapes_connected_horizontal_left(); score += 100; }
            if(wasCopied == 1) { horizontal_shapes[number_of_shapes - 1] = null; wasCopied = 0; }
            if(moveToCenter == 1) {connectionPerformed = 0; }
            number_of_shapes = number_of_shapes_horizontal;
            count_null();
            for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("Horizontal_shape " + i, "" + horizontal_shapes[i]);}
            if(connectShapes == 1) { direction = "left"; }
        }
    }

    public void moveHand() {
        int x_upp_bound = (int) (0.74 * Gdx.graphics.getWidth());
        int x_down_bound = (int) (0.092 * Gdx.graphics.getWidth());
        int y_upp_bound = (int) (0.563 * Gdx.graphics.getHeight());
        int y_down_bound = (int) (0.056 * Gdx.graphics.getHeight());
        int increment = (int) (0.01481 * Gdx.graphics.getWidth());
        if(!hand_direction.equals("NONE") && hand != skin.getDrawable(hand_direction)) {
            hand = skin.getDrawable(hand_direction);
        }
        if(hand_direction.equals("up")) {
            hand_y += increment;
            if(hand_y > y_upp_bound) { restoreHandCoordinates();}
        }
        else if(hand_direction.equals("down")) {
            hand_y -= increment;
            if(hand_y < y_down_bound) { restoreHandCoordinates();}
        }
        else if (hand_direction.equals("left")) {
            hand_x -= increment;
            if(hand_x < x_down_bound) { restoreHandCoordinates();}
        }
        else if (hand_direction.equals("right")) {
            hand_x += increment;
            if(hand_x > x_upp_bound) { restoreHandCoordinates();}
        }
    }

    public void restoreHandCoordinates() {
        if(hand_direction.equals("up")) {
            hand_y = (int) (0.112 * Gdx.graphics.getHeight());
            hand_x = (int) (0.092 * Gdx.graphics.getWidth());
        }
        else if(hand_direction.equals("down")) {
            hand_y = (int) (0.563 * Gdx.graphics.getHeight());
            hand_x = (int) (0.092 * Gdx.graphics.getWidth());
        }
        else if (hand_direction.equals("left")) {
            hand_y = (int) (0.112 * Gdx.graphics.getHeight());
            hand_x = (int) (0.74 * Gdx.graphics.getWidth());
        }
        else if (hand_direction.equals("right")) {
            hand_y = (int) (0.112 * Gdx.graphics.getHeight());
            hand_x = (int) (0.092 * Gdx.graphics.getWidth());
        }
    }

    public void getHandTexture() {
        if(!hand_direction.equals("NONE")) {
            hand = skin.getDrawable(hand_direction);
        }
        else { hand = null; }
    }

    public void Restart() {
        for(int i = 0; i < number_of_shapes_horizontal; i++) {
            if(i < number_of_shapes_vertical && vertical_shapes[i] != null) {
                vertical_shapes[i].dispose(); }
            if (horizontal_shapes[i] != null) {
                horizontal_shapes[i].dispose();}
//            System.gc();
//            Runtime.getRuntime().gc();
            /*try {
                if(i == 2) { vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, choose_shape());}
                else {
                    vertical_shapes[i] = new Shape("vertical", i, choose_shape());
                    horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                }
            } catch(IOException exception) {
                Gdx.app.log("Exception", "", exception);
            }*/
        }
        number_of_shapes_vertical = level;
        number_of_shapes_horizontal = number_of_shapes_vertical +1;
        vertical_shapes = new Shape[number_of_shapes_vertical];
        horizontal_shapes = new Shape[number_of_shapes_horizontal];
        //create_shapes_homogeneous();
        create_tutorial_shapes();
        time = 0;
        score = 0;
        moves = 0;
        direction = "NON";
        game.getTutorialScreen().getRenderer().setLevel(level);
    }

    public int count_null() {
        int null_shapes = 0;
        for(Shape current_shape :  vertical_shapes) {
            if(current_shape == null) { null_shapes++; }
        }
        for(Shape current_shape : horizontal_shapes) {
            if(current_shape == null) { null_shapes ++; }
        }
        //gameState is changed to gameOver at 12 null Shapes because
        // when user combine all Shapes in vertical and horizontal arrays remains
        //only 6 null elements in each, other null elements are deleted
        if(null_shapes == 12) { gameState = "gameOver"; }
        return null_shapes;
    }

    public void add_HighScore() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        Gdx.app.log("level","" + (level - 5) );
        if(mode.equals("Moves")) {
            if(level - 5 > game.getHighScores().countMoves.size()) {
                game.getHighScores().append_HighScore("Levels/countMoves.txt", score);
            }
            else {
                game.getHighScores().change_HighScore("Levels/countMoves.txt",level,String.valueOf(score));
            }
        }
        if(mode.equals("Time1")) {
            if(level - 5 > game.getHighScores().timeChallenge.size()) {
                game.getHighScores().append_HighScore("Levels/timeChallenge.txt", score);
            }
            else {
                game.getHighScores().change_HighScore("Levels/timeChallenge.txt",level,String.valueOf(decimalFormat.format(time)));
            }
        }
        game.getHighScores().update_HighScores();

    }

    public void vibration() {
        Gdx.app.log("Vibration","" + vibrate);
        if(vibrate) { Gdx.input.vibrate(500); }
    }

    public void showShapes() {
        Gdx.app.log("", "*********************** ");
        for(int i = 0; i < vertical_shapes.length ; i++){
            Gdx.app.log("Vertical_Shape " + i, "" + vertical_shapes[i]);
        }
        Gdx.app.log("", "*********************** ");
        for(int i = 0; i < horizontal_shapes.length ; i++){
            Gdx.app.log("Horizontal_Shape " + i, "" + horizontal_shapes[i]);
        }
    }

    public void dispose() {
        background.dispose();
        for(int i = 0; i < vertical_shapes.length; i++) {
            if(vertical_shapes[i] != null) { vertical_shapes[i].dispose();}
        }
        for(int i = 0; i < horizontal_shapes.length; i++) {
            if(horizontal_shapes[i] != null) { horizontal_shapes[i].dispose();}
        }
    }

    public Texture getBackground() {
        return background;
    }

    public Shape getShapesVertical(int i) { return vertical_shapes[i]; }

    public Shape getShapesHorizontal(int i) { return horizontal_shapes[i]; }

    public int getMoves() { return  moves; }

    public TextureRegion getRegion() { return region; }

    public int getNumber_of_shapes_vertical() { return number_of_shapes_vertical; }

    public int getNumber_of_shapes_horizontal() { return number_of_shapes_horizontal; }

    public String getDirection() { return  direction; }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ImageButton getRestartButton() { return restartButton; }

    public ImageButton getPauseButton() { return pauseButton; }

    public float getTime() { return time; }

    public void setCallBack(MyGame.CallBack callBack) {
        this.callBack = callBack;
    }

    public String  getGameState() { return gameState; }

    public String getMode() { return "Tutorial"; }

    public MyGame getGame() { return  this.game; }

    public int getScore() {return this.score; }

    public int getLevel() { return this.level; }

    public boolean getVibrate() { return this.vibrate; }

    public void setGameState(String gameState) { this.gameState = gameState; }

    public void setLevel(int level) { this.level = level; }

    public void setVibrate(boolean vibrate) { this.vibrate = vibrate; }

    public Drawable getHand() { return this.hand; }

    public String getHand_direction() { return this.hand_direction; }

    public int getHand_x() { return this.hand_x; }

    public int getHand_y() { return this.hand_y; }
}
