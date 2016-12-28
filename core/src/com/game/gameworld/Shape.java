package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by hackintosh on 10/9/16.
 */

public class Shape extends Texture {
    public String name;
    public float width;
    public float height;
    public float x;
    public float y;
    public TextureRegion region;
    public int position;
    public float animated;
    public  float animation;
    public int region_width;
    public int region_height;
    public int id;
    public String connect;
    public Texture opacity;
    public TextureRegion opacityRegion;

    public Shape (String orientation, int position, String shape_name) {
        super(Gdx.files.internal(shape_name));
        name = shape_name;
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        width = (float) 0.1694 * screen_width;
        height = (float) 0.0973 * screen_height;
        /*Gdx.app.log("Width", "" + width);
        Gdx.app.log("Height", "" + height);*/
        this.position = position;
        if(orientation.equals("vertical")) {
            x = (float) 0.413 * screen_width;
            y = (float) (0.210 *  screen_height + position * height + position * 0.0104 * screen_height);
            animation = (float) 0.1077 * screen_height;
        }
        if(orientation.equals("horizontal")) {
            x = (float) (0.0314 * screen_width + position * width + position * 0.0231 * screen_width );
            y = (float) 0.426 *  screen_height;
            animation = (float) (0.1925 * screen_width);
        }
        //Gdx.app.log("Animation", "" + animation);
        animated = 0;
        region_width = 256;
        region_height = 256;
        region = null;
        Gdx.app.log("shape_name",shape_name);
        shape_name = shape_name.substring(shape_name.lastIndexOf("/") + 1, shape_name.lastIndexOf("."));
        id = Integer.parseInt(shape_name.substring(0, shape_name.lastIndexOf("_")));
        connect = shape_name.substring(shape_name.lastIndexOf("_") + 1);
        Gdx.app.log("id","" + id);
        Gdx.app.log("connect","" + connect);
        opacity = new Texture(Gdx.files.internal("opacity.png"));
    }

    public Shape (Shape initial_shape, String orientation) {
        super(Gdx.files.internal(initial_shape.name));
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        name = initial_shape.name;
        width = initial_shape.width;
        height = initial_shape.height;
        if(initial_shape.position == 0) {position = 5;}
        else {position = initial_shape.position + 1;}
        if(orientation.equals("vertical")) {
            x = (float) 0.413 * screen_width;
            y = (float) (0.210 *  screen_height + position * height + position * 0.0104 * screen_height);
            animation = (float) 0.1077 * screen_height;
        }
        if(orientation.equals("horizontal")) {
            x = (float) (0.0314 * screen_width + position * width + position * 0.0231 * screen_width );
            y = (float) 0.426 *  screen_height;
            animation = (float) (0.1925 * screen_width);
        }
        animated = 0;
        region_width = 256;
        region_height = 256;
        region = null;
        id = initial_shape.id;
        connect = initial_shape.connect;
        opacity = initial_shape.opacity;
    }

    public void  restoreShape_up() {
        int position = 0;
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        x = (float) 0.413 * screen_width;
        y = (float) (0.210 *  screen_height + position * height + position * 0.0104 * screen_height);
        animation = (float) 0.1077 * screen_height;
    }

    public void  restoreShape_down(int number_of_shapes) {
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        x = (float) 0.413 * screen_width;
        y = (float) (0.210 *  screen_height + position * height + position * 0.0104 * screen_height);
        animation = (float) 0.1077 * screen_height;
    }

    public void  restoreShape_right() {
        int position = 0;
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        x = (float) (0.0314 * screen_width + position * width + position * 0.0231 * screen_width );
        y = (float) 0.426 *  screen_height;
        animation = (float) (0.1925 * screen_width); //(float) (0.163 * screen_width + 0.0203 * screen_width);
    }

    public void restoreShape_left(int number_of_shapes) {
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        x = (float) (0.0314 * screen_width + position * width + position * 0.0231 * screen_width );
        y = (float) 0.426 *  screen_height;
        animation = (float) (0.1925 * screen_width);
    }

    public void updateShape_coordinates(String orientation) {
        int screen_width = Gdx.graphics.getWidth();
        int screen_height = Gdx.graphics.getHeight();
        if(orientation.equals("vertical")) {
            x = (float) 0.413 * screen_width;
            y = (float) (0.210 *  screen_height + position * height + position * 0.0104 * screen_height);
            animation = (float) 0.1077 * screen_height;
        }
        if(orientation.equals("horizontal")) {
            x = (float) (0.0314 * screen_width + position * width + position * 0.0231 * screen_width );
            y = (float) 0.426 *  screen_height;
            animation = (float) (0.1925 * screen_width);
        }
    }

    public void decrementRegion(int increment, String direction) {
        int iterations;
        int decrement_region;
        int add_to_region;
        iterations = (int) (animation/increment);
        decrement_region =  256/iterations;
        if(decrement_region * iterations != 256) {
            add_to_region = 256 - (decrement_region * iterations);
            if(animated % (increment * (iterations / add_to_region) ) == 0) {
                if(direction.equals("vertical")){ region_height --; }
                else { region_width --; }
            }
        }
        if(direction.equals("vertical")) { region_height -= decrement_region; }
        else { region_width -= decrement_region; }
        if(region_height < 0) { region_height = 0; }
        if(region_width < 0) { region_width = 0; }
    }

    /*public void moveShape_up() {
        //String direction;
        if (animated < animation) {
            if (position == 4) {
                region_height -= 5;
                region = new TextureRegion(this, 0, 256 - region_height, region_width, region_height);
            }
            if(position ==  - 1) {
                region_height -= 4.75;
                region = new TextureRegion(this, 0, 0, region_width, 256 - region_height);
            }
            if(position != number_of_shapes - 1) {
                y += 4;
            }
            animated += 4;
        } else {
            if(direction.equals("up")) {
                direction = "NON";
            }
            if(position != number_of_shapes - 1) {
                position++;
            }
            else {
                position = 0;
            }
            if(position == 5) {
                region_height = 256;
            }
            else if(position == 0) {
                region_height = 256;
            }
            animated = 0;
            region = null;
        }
    }

    public void setDirection(String direction) {

    }*/
}
