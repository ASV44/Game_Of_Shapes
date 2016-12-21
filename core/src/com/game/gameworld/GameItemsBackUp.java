package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.game.gesturedetector.DirectionGestureDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Created by hackintosh on 10/7/16.
 */

public class GameItemsBackUp {

    //private Rectangle rect = new Rectangle(0, 0, 17, 12);
    //private Rectangle rect1 = new Rectangle(0, 0, 17, 12);
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
    private Drawable drawable;
    private float time;
    private boolean hz;

    public GameItemsBackUp() {
        background = new Texture(Gdx.files.internal("background.png"));
        //shape = new Texture(Gdx.files.internal("shape1.png"));
        number_of_shapes = 30;
        vertical_shapes = new Shape[number_of_shapes];
        horizontal_shapes = new Shape[number_of_shapes];
        for(int i = 0; i < number_of_shapes; i++) {
            if(i == 1) {
                try {
                    //vertical_shapes[i] = new Shape("vertical", i, "Shapes/Dark/11_10.png");
                    //horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                    vertical_shapes[i] = new Shape("vertical", i, "Shapes/Light/14_30.png");
                    horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                    continue;
                }
                catch (IOException exception) {
                    Gdx.app.log("Exception","",exception);
                }
            }
            if(i == 2) {
                //vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, "Shapes/Light/11_30.png");//choose_shape());
                vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, "Shapes/Dark/14_10.png");//choose_shape());
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
        //shape = new Shape("horizontal", 4);
        direction = "NON";
        moves = 0;
        region = null;
        increment = 15;
        time = 90;
        hz = false;
        drawable = new TextureRegionDrawable(new TextureRegion(background,950,410,100,100));
        restartButton = new ImageButton(drawable);
        restartButton.setBounds((float) 0.879 * Gdx.graphics.getWidth(), (float) 0.733 * Gdx.graphics.getHeight(), 100, 100);
        restartButton.addListener(new InputListener() {
            @Override
            public  boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Button","works ");
                Restart();
                return false;
            }
        });
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
        if (direction.equals("up")) { moveUp(); }
        if (direction.equals(("down"))){ moveDown(); }
        if (direction.equals("right")) {moveRight(); }
        if (direction.equals("left")) {moveLeft(); }

        //time = Gdx.graphics.getDeltaTime();
        time -= delta;
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
                if(ignoreFrom !=0 && ignoreTo !=0) { vertical_shapes[i] = null; }
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
                    if(ignoreFrom != -1 && ignoreTo != -1) { vertical_shapes[i] = null; }
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
                if(ignoreFrom !=0 && ignoreTo !=0) { horizontal_shapes[i] = null; }
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
                    if(ignoreFrom != -1 && ignoreTo != -1) { horizontal_shapes[i] = null; }
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
                vertical_shapes[number_of_shapes - 1] = null;
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
                vertical_shapes[number_of_shapes - 1] = null;
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
                horizontal_shapes[number_of_shapes - 1] = null;
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
                horizontal_shapes[number_of_shapes - 1] = null;
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
                Gdx.input.vibrate(500);
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
                    }
                    if (i == number_of_shapes - 1) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, 0, vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
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
            else { shiftShapes_connected_vertical_up(); }
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
                Gdx.input.vibrate(500);
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
                    }
                    if (i == 5) {
                        vertical_shapes[i].decrementRegion(increment, "vertical");
                        vertical_shapes[i].region = new TextureRegion(vertical_shapes[i], 0, vertical_shapes[i].region_height , vertical_shapes[i].region_width, 256 - vertical_shapes[i].region_height);
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
            else { shiftShapes_connected_vertical_down(); }
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
                Gdx.input.vibrate(500);
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
                    }
                    if (i == number_of_shapes - 1) {
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], horizontal_shapes[i].region_width, 0, 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
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
            else { shiftShapes_connected_horizontal_right(); }
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
                Gdx.input.vibrate(500);
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
                    }
                    if (i == 5) {
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].decrementRegion(increment, "horizontal");
                        //Gdx.app.log("width_region", ""+ horizontal_shapes[i].region_width);
                        horizontal_shapes[i].region = new TextureRegion(horizontal_shapes[i], 0, 0 , 256 - horizontal_shapes[i].region_width, horizontal_shapes[i].region_height);
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
            else { shiftShapes_connected_horizontal_left(); }
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
                if(i == 2) { vertical_shapes[i] = horizontal_shapes[i] = new Shape("vertical", i, choose_shape());}
                else {
                    vertical_shapes[i] = new Shape("vertical", i, choose_shape());
                    horizontal_shapes[i] = new Shape("horizontal", i, choose_shape());
                }
            } catch(IOException exception) {
                Gdx.app.log("Exception", "", exception);
            }
        }
    }

    /*public void showShapes() {
        for(int i = 0; i < number_of_shapes ; i++){
            Gdx.app.log("Shape " + i, "" + vertical_shapes[i]);
        }
    }*/

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

    public float getTime() { return time; }
}

