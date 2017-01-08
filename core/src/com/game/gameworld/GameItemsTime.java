package com.game.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.shapes.MyGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 * Created by hackintosh on 11/16/16.
 */

public class GameItemsTime {
    private Texture background;
    //private Shape[] shape;
    private Shape shape;
    private Shape[] vertical_shapes;
    private Shape[] horizontal_shapes;
    private String direction;
    private int moves;
    private int number_of_shapes;
    private TextureRegion region;
    private int increment;
    private int lala = 0;
    private ImageButton restartButton;
    private ImageButton backButton;
    private Drawable drawable;
    private float time;
    private String gameState;
    private MyGame.CallBack callBack;
    private int score;
    private MyGame game;
    private String gameMode;
    private int level;
    private boolean vibrate = true;


    public GameItemsTime(final MyGame game, String gameMode, int level) {
        background = new Texture(Gdx.files.internal("background.png"));
        //shape = new Texture(Gdx.files.internal("shape1.png"));
        number_of_shapes = level + 5;
        vertical_shapes = new Shape[number_of_shapes];
        horizontal_shapes = new Shape[number_of_shapes];
        for(int i = 0; i < number_of_shapes; i++) {
            if(i == 2) {
                //vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, "Shapes/Light/11_30.png");//choose_shape());
                //vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, "Shapes/Dark/14_10.png");//choose_shape());
                continue;
            }
            else {
                try {
                    vertical_shapes[i] = new Shape("vertical", i, choose_shape());
                    horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                } catch(IOException exception) {
                    Gdx.app.log("Exception", "", exception);
                }
            }
        }
        Random random = new Random();
        if(random.nextInt(2) == 0) {
            if(random.nextInt(2) == 0) {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(horizontal_shapes[3].name));
            }
            else {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(horizontal_shapes[1].name));
            }
        }
        else {
            if(random.nextInt(2) == 0) {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(vertical_shapes[3].name));
            }
            else {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(vertical_shapes[1].name));
            }
        }
        //shape = new Shape("horizontal", 4);
        direction = "NON";
        moves = 0;
        region = null;
        increment = 15;
        time = 90;
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
                    Gdx.app.log("Button_restart", "works ");
                    Restart();
                }
            }
        });
        drawable = new TextureRegionDrawable(new TextureRegion(background, 804, 290, 118, 121));
        backButton = new ImageButton(drawable);
        backButton.setBounds((float) 0.744 * Gdx.graphics.getWidth(), (float) 0.785 * Gdx.graphics.getHeight(), (float) 0.109 * Gdx.graphics.getWidth(), (float) 0.063 * Gdx.graphics.getHeight());
        backButton.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState.equals("Active")) {
                    Gdx.app.log("Button_back", "works ");
                    game.setScreen(game.getMenuScreen());
                    game.getInputMultiplexer().addProcessor(game.getMenuScreen().getStage());
                }
            }
        });
        this.gameMode = gameMode;
        this.level = level + 5;
    }

    public void update(float delta) {
        //Gdx.app.log("GameItemsBackUp", "update");
        /*rect.x++;
        rect1.y++;
        if (rect.x > Gdx.graphics.getWidth()) {
            rect.x = 0;
        }
        if(rect1.y > Gdx.graphics.getHeight()) {
            rect1.y = 0;
        }*/
        if(gameState.equals("Active")) {
            if (direction.equals("up")) { moveUp();}
            if (direction.equals(("down"))) { moveDown(); }
            if (direction.equals("right")) { moveRight(); }
            if (direction.equals("left")) { moveLeft(); }
            time -= delta;
            if (time < 0) { gameState = "gameOver"; }//callBack.gameOver(score);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.getInputMultiplexer().clear();
            game.setScreen(game.getMenuScreen());
            game.getInputMultiplexer().addProcessor(game.getMenuScreen().getStage());
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

    public String getHomogeneous_Shape(String shape_name) {
        String homogeneous_shape = "Shapes/";
        String connect;
        if(shape_name.contains("Light")) {
            homogeneous_shape += "Dark/";
        }
        else {
            homogeneous_shape += "Light/";
        }
        shape_name = shape_name.substring(shape_name.lastIndexOf("/") + 1, shape_name.lastIndexOf("."));
        homogeneous_shape += shape_name.substring(0, shape_name.lastIndexOf("_")) + "_";
        connect = shape_name.substring(shape_name.lastIndexOf("_") + 1);
        for(int j = 0; j < 2; j++) {
            if(connect.charAt(j) == '0') {homogeneous_shape += "0";}
            else{
                if(connect.charAt(j) == '5') { homogeneous_shape += "5"; }
                else {
                    if(Character.getNumericValue(connect.charAt(j)) <= 2){
                        homogeneous_shape +=String.valueOf(Character.getNumericValue(connect.charAt(j)) + 2);
                    }
                    else {homogeneous_shape +=String.valueOf(Character.getNumericValue(connect.charAt(j)) - 2);}
                }
            }
        }
        homogeneous_shape += ".png";

        return homogeneous_shape;
    }

    public void create_shape_homogeneous(String orientation, int position) {
        Shape[] currentShapes;
        Random random = new Random();
        String randomShape_name;
        int randomShape_position = position;

        if(orientation.equals("vertical")) {
            currentShapes = vertical_shapes;
        }
        else { currentShapes = horizontal_shapes; }
        while(randomShape_position == position || randomShape_position == 2) {
            randomShape_position = random.nextInt(currentShapes.length);
        }
        if(random.nextInt(2) == 0)
        { randomShape_name = vertical_shapes[randomShape_position].name; }
        else { randomShape_name = horizontal_shapes[randomShape_position].name; }
        currentShapes[position] = new Shape(orientation,position,getHomogeneous_Shape(randomShape_name));
    }

    public void shiftShapes_vertical_up(int ignoreFrom, int ignoreTo) {
        Shape temp = null;
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
                if(ignoreFrom !=0 && ignoreTo !=0) {
                    create_shape_homogeneous("vertical",i);
                }
            }
        }
        horizontal_shapes[2] = vertical_shapes[2];
    }

    public void shiftShapes_vertical_down(int ignoreFrom, int ignoreTo) {
        Shape temp = vertical_shapes[number_of_shapes - 1];
        for(int i = 0; i < number_of_shapes; i++) {
            if(i <= ignoreFrom && i >= ignoreTo && ignoreFrom != -1 && ignoreTo != -1) { continue; }
            if(i != 0) {
                if(i == number_of_shapes - 1) {
                    vertical_shapes[i - 1] = temp;
                    if(ignoreFrom != -1 && ignoreTo != -1) {
                        create_shape_homogeneous("vertical",i);
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
    }

    public void shiftShapes_horizontal_right(int ignoreFrom, int ignoreTo) {
        Shape temp = null;
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
                if(ignoreFrom !=0 && ignoreTo !=0) {
                    create_shape_homogeneous("horizontal",i);
                }
            }
        }
        vertical_shapes[2] = horizontal_shapes[2];
    }

    public void shiftShapes_horizontal_left(int ignoreFrom, int ignoreTo) {
        Shape temp = horizontal_shapes[number_of_shapes - 1];
        for(int i = 0; i < number_of_shapes; i++) {
            if(i >= ignoreFrom && i <= ignoreTo && ignoreFrom != -1 && ignoreTo != -1) { continue; }
            if(i != 0) {
                if(i == number_of_shapes - 1) {
                    horizontal_shapes[i - 1] = temp;
                    if(ignoreFrom != -1 && ignoreTo != -1) {
                        create_shape_homogeneous("horizontal",i);
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
    }

    public void shiftShapes_connected_vertical_up() {
        for(int i = 2; i >= -1; i--) {
            if(i > 0) {
                //vertical_shapes[i].dispose();
                vertical_shapes[i] = null;
                horizontal_shapes[2] = null;
            }
            if (i == 0) {
                vertical_shapes[i + 1] = vertical_shapes[i];
            }
            if (i == -1) {
                vertical_shapes[0] = vertical_shapes[number_of_shapes - 1];
                create_shape_homogeneous("vertical",number_of_shapes - 1);
            }
        }
    }

    public void shiftShapes_connected_vertical_down() {
        for(int i = 2; i < number_of_shapes; i++) {
            if(i < 4) {
                //vertical_shapes[i].dispose();
                vertical_shapes[i] = null;
                horizontal_shapes[2] = null;
            }
            if (i >= 4) {
                vertical_shapes[i - 1] = vertical_shapes[i];
            }
            if (i == number_of_shapes - 1) {
                create_shape_homogeneous("vertical",number_of_shapes - 1);
            }
        }
    }

    public void shiftShapes_connected_horizontal_right() {
        for(int i = 2; i >= -1; i--) {
            if(i > 0) {
                //vertical_shapes[i].dispose();
                horizontal_shapes[i] = null;
                vertical_shapes[2] = null;
            }
            if (i == 0) {
                horizontal_shapes[i + 1] = horizontal_shapes[i];
            }
            if (i == -1) {
                horizontal_shapes[0] = horizontal_shapes[number_of_shapes - 1];
                create_shape_homogeneous("horizontal",number_of_shapes - 1);
            }
        }
    }

    public void shiftShapes_connected_horizontal_left() {
        for(int i = 2; i < number_of_shapes; i++) {
            if(i < 4) {
                //vertical_shapes[i].dispose();
                horizontal_shapes[i] = null;
                vertical_shapes[2] = null;
            }
            if (i >= 4) {
                horizontal_shapes[i - 1] = horizontal_shapes[i];
            }
            if (i == number_of_shapes - 1) {
                create_shape_homogeneous("horizontal",number_of_shapes - 1);
            }
        }
    }


    public void moveUp() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = 0;
        int ignoreTo = 0;
        if(vertical_shapes[2] == null) {
            moveToCenter = 1;
            ignoreFrom = 2;
            ignoreTo = number_of_shapes - 2;
        } else {
            vertical_shapes[2].animation = (float) (0.108 * Gdx.graphics.getHeight());
        }
        if(vertical_shapes[2] != null && vertical_shapes [1] != null) {
            if (vertical_shapes[2].id == vertical_shapes[1].id && (vertical_shapes[2].connect.charAt(0) == '1' || vertical_shapes[2].connect.charAt(0) == '5') && (!vertical_shapes[2].connect.equals(vertical_shapes[1].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
                vibration();
            }
        }

        if (vertical_shapes[number_of_shapes - 1] != null)
        {vertical_shapes[number_of_shapes - 1].restoreShape_up();}
        for(int i = 0; i < number_of_shapes; i++) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (vertical_shapes[i] != null) {
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
        if(direction.equals("NON")) {
            moves++;
            if (connectShapes == 0 || moveToCenter == 1) { shiftShapes_vertical_up(ignoreFrom, ignoreTo); }
            else { shiftShapes_connected_vertical_up(); score += 1;}
            if(vertical_shapes[0] != null) { vertical_shapes[0].region_height = 256; }
            if(vertical_shapes[5] != null) { vertical_shapes[5].region_height = 256;}
            for(int i = 0; i < number_of_shapes; i++) { Gdx.app.log("Vertical_shape " + i, "" + vertical_shapes[i]);}
            if(connectShapes == 1) { direction = "up"; }
        }
    }


    public void moveDown() {
        int connectShapes = 0;
        int moveToCenter = 0;
        int ignoreFrom = -1;
        int ignoreTo = -1;
        if(vertical_shapes[2] == null) {
            moveToCenter = 1;
            ignoreFrom = 2;
            ignoreTo = 0;
        } else {
            vertical_shapes[2].animation = (float) (0.108 * Gdx.graphics.getHeight());
        }
        if(vertical_shapes[2] != null && vertical_shapes[3] != null) {
            if (vertical_shapes[2].id == vertical_shapes[3].id && (vertical_shapes[2].connect.charAt(0) == '3' || vertical_shapes[2].connect.charAt(0) == '5') && (!vertical_shapes[2].connect.equals(vertical_shapes[3].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = 0;
                vibration();
            }
        }
        //if (vertical_shapes[number_of_shapes - 1] != null)
        //{vertical_shapes[number_of_shapes - 1].restoreShape_up();}
        for(int i = number_of_shapes - 1; i > -1; i--) {
            if ((connectShapes != 0 || moveToCenter !=0) && i <= ignoreFrom && i >= ignoreTo) {
                continue;
            }
            if (vertical_shapes[i] != null) {
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
        if(direction.equals("NON")) {
            moves++;
            if(vertical_shapes[0] != null) { vertical_shapes[0].region_height = 256; }
            if(vertical_shapes[5] != null) { vertical_shapes[5].region_height = 256;}
            if (connectShapes == 0 || moveToCenter == 1) { shiftShapes_vertical_down(ignoreFrom, ignoreTo); }
            else { shiftShapes_connected_vertical_down(); score += 1; }
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
        if(horizontal_shapes[2] == null) {
            moveToCenter = 1;
            ignoreFrom = 2;
            ignoreTo = number_of_shapes - 2;
        } else {
            horizontal_shapes[2].animation = (float) (0.19 * Gdx.graphics.getWidth());
        }
        if(horizontal_shapes[2] != null && horizontal_shapes [1] != null) {
            if (horizontal_shapes[2].id == horizontal_shapes[1].id && (horizontal_shapes[2].connect.charAt(1) == '2' || horizontal_shapes[2].connect.charAt(1) == '5') && (!horizontal_shapes[2].connect.equals(horizontal_shapes[1].connect))) {
                connectShapes = 1;
                ignoreFrom = 2;
                ignoreTo = number_of_shapes - 2;
                vibration();
            }
        }

        Gdx.app.log("ignoreFrom","" + ignoreFrom);
        Gdx.app.log("ignoreTo","" + ignoreTo);
        if (horizontal_shapes[number_of_shapes - 1] != null)
        {horizontal_shapes[number_of_shapes - 1].restoreShape_right();}
        for(int i = 0; i < number_of_shapes; i++) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (horizontal_shapes[i] != null) {

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
        if(direction.equals("NON")) {
            moves++;
            /*for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("shape " + i, "" + horizontal_shapes[i]);}*/
            if (connectShapes == 0 || moveToCenter == 1) { shiftShapes_horizontal_right(ignoreFrom, ignoreTo); }
            else { shiftShapes_connected_horizontal_right(); score += 1; }
            if(horizontal_shapes[0] != null) { horizontal_shapes[0].region_width = 256; }
            if(horizontal_shapes[5] != null) { horizontal_shapes[5].region_width = 256;}
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
        if(horizontal_shapes[2] == null) {
            moveToCenter = 1;
            ignoreFrom = 0;
            ignoreTo = 2;
        } else {
            horizontal_shapes[2].animation = (float) (0.19 * Gdx.graphics.getWidth());
        }
        if(horizontal_shapes[2] != null && horizontal_shapes[3] != null) {
            if (horizontal_shapes[2].id == horizontal_shapes[3].id && (horizontal_shapes[2].connect.charAt(1) == '4' || horizontal_shapes[2].connect.charAt(1) == '5') && (!horizontal_shapes[2].connect.equals(horizontal_shapes[3].connect))) {
                connectShapes = 1;
                ignoreFrom = 0;
                ignoreTo = 2;
                vibration();
            }
        }
        //if (vertical_shapes[number_of_shapes - 1] != null)
        //{vertical_shapes[number_of_shapes - 1].restoreShape_up();}


        for(int i = number_of_shapes - 1; i > -1; i--) {
            if ((connectShapes != 0 || moveToCenter !=0) && i >= ignoreFrom && i <= ignoreTo) {
                continue;
            }
            if (horizontal_shapes[i] != null) {
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
        if(direction.equals("NON")) {
            moves++;
            if(horizontal_shapes[0] != null) { horizontal_shapes[0].region_width = 256; }
            if(horizontal_shapes[5] != null) { horizontal_shapes[5].region_width = 256;}
            if (connectShapes == 0 || moveToCenter == 1) { shiftShapes_horizontal_left(ignoreFrom, ignoreTo); }
            else { shiftShapes_connected_horizontal_left(); score += 1; }
            for(int i = 0; i < number_of_shapes; i++)
            { Gdx.app.log("Horizontal_shape " + i, "" + horizontal_shapes[i]);}
            if(connectShapes == 1) { direction = "left"; }
        }
    }

    public void Restart() {
        for(int i = 0; i < number_of_shapes; i++) {
            if(vertical_shapes[i] != null) {
                vertical_shapes[i].dispose(); }
            if (horizontal_shapes[i] != null) {
                horizontal_shapes[i].dispose();}
            try {
                if(i == 2) { continue;}
                else {
                    vertical_shapes[i] = new Shape("vertical", i, choose_shape());
                    horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                }
            } catch(IOException exception) {
                Gdx.app.log("Exception", "", exception);
            }
            game.getScreenTime().getRenderer().setLevel(level);
        }
        Random random = new Random();
        if(random.nextInt(2) == 0) {
            if(random.nextInt(2) == 0) {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(horizontal_shapes[3].name));
            }
            else {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(horizontal_shapes[1].name));
            }
        }
        else {
            if(random.nextInt(2) == 0) {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(vertical_shapes[3].name));
            }
            else {
                vertical_shapes[2] = horizontal_shapes[2] = new Shape("vertical", 2, getHomogeneous_Shape(vertical_shapes[1].name));
            }
        }
        time = 90;
        score = 0;
        moves = 0;
        direction = "NON";
    }

    /*public void showShapes() {
        for(int i = 0; i < number_of_shapes ; i++){
            Gdx.app.log("Shape " + i, "" + vertical_shapes[i]);
        }
    }*/

    public void add_HighScore() {

        Gdx.app.log("level","" + (level - 5) );
        if(level - 5 > game.getHighScores().timeAttack.size()) {
            game.getHighScores().append_HighScore("Levels/timeAttack.txt", score);
        }
        else {
            game.getHighScores().change_HighScore("Levels/timeAttack.txt",level,String.valueOf(score));
        }
        game.getHighScores().update_HighScores();

    }

    public void vibration() {
        if(vibrate) { Gdx.input.vibrate(500); }
    }

    public Texture getBackground() {
        return background;
    }

    public Shape getShapesVertical(int i) { return vertical_shapes[i]; }

    public Shape getShapesHorizontal(int i) { return horizontal_shapes[i]; }

    public int getMoves() { return  moves; }

    public TextureRegion getRegion() { return region; }

    public int getNumber_of_shapes() { return number_of_shapes; }

    public String getDirection() { return  direction; }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ImageButton getRestartButton() { return restartButton; }

    public ImageButton getBackButton() { return  backButton; }

    public float getTime() { return time; }

    public void setCallBack(MyGame.CallBack callBack) {
        this.callBack = callBack;
    }

    public String  getGameState() { return gameState; }

    public String getGameMode() { return gameMode; }

    public MyGame getGame() { return this.game; }

    public int getScore() { return  this.score; }

    public void setGameState(String gameState) { this.gameState = gameState; }

    public void setLevel(int level) { this.level = level; }

    public int getLevel() { return  this.level; }

    public void setVibrate(boolean vibrate) { this.vibrate = vibrate; }

    public boolean getVibrate() { return this.vibrate; }
}
