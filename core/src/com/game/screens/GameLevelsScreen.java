package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.game.gameworld.GameLevelsItems;
import com.game.gameworld.GameLevelsRenderer;
import com.game.gesturedetector.DirectionGestureDetector;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 12/29/16.
 */

public class GameLevelsScreen implements Screen {
    private GameLevelsItems items;
    private GameLevelsRenderer renderer;
    private InputMultiplexer inputMultiplexer;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font1;

    public GameLevelsScreen(MyGame game, String gameMode) {
        this.inputMultiplexer = game.getInputMultiplexer();
        inputMultiplexer.clear();
//        generator = new FreeTypeFontGenerator(Gdx.files.internal("code_light.otf"));
//        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.color = Color.BLACK;
//        font1 = create_BitmapFont(12);
        items = new GameLevelsItems(game, gameMode);
        renderer =  new GameLevelsRenderer(game,items);

        inputMultiplexer.addProcessor(new DirectionGestureDetector(new DirectionGestureDetector.DirectionListener() {
            @Override
            public void onUp() {
                //Gdx.app.log("Direction", "up");
                if(items.getDirection().equals("NON")) {
                    items.setDirection("up");}
            }

            @Override
            public void onRight() {
                //Gdx.app.log("Direction", "right");
                if(items.getDirection().equals("NON")) {
                    items.setDirection("right");}
            }

            @Override
            public void onLeft() {
                //Gdx.app.log("Direction", "left");
                if(items.getDirection().equals("NON")) {
                    items.setDirection("left");}
            }

            @Override
            public void onDown() {
                //Gdx.app.log("Direction", "down");
                if(items.getDirection().equals("NON")) {
                    items.setDirection("down");}
            }
        }));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        items.update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        items.dispose();
        renderer.dispose();
    }

}
