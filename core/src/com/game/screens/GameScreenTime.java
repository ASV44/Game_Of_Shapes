package com.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.gameworld.GameItemsTime;
import com.game.gameworld.GameRendererTime;
import com.game.gesturedetector.DirectionGestureDetector;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 11/16/16.
 */

public class GameScreenTime implements Screen {

    private GameItemsTime items;
    private GameRendererTime renderer;
    private InputMultiplexer inputMultiplexer;
    private DirectionGestureDetector directionGestureDetector;

    public GameScreenTime(MyGame game, String gameMode,int level) {
        this.inputMultiplexer = game.getInputMultiplexer();
        inputMultiplexer.clear();
        items = new GameItemsTime(game, gameMode,level);
        renderer = new GameRendererTime(items);
        directionGestureDetector =  new DirectionGestureDetector(new DirectionGestureDetector.DirectionListener() {
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
        });
        inputMultiplexer.addProcessor(directionGestureDetector);
        //Gdx.input.setInputProcessor(game.getInputMultiplexer());

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        items.update(delta);
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
        inputMultiplexer.addProcessor(directionGestureDetector);
        inputMultiplexer.addProcessor(getRenderStage());
        if(renderer.getGameOver() != null) {
            inputMultiplexer.addProcessor(renderer.getGameOverStage());
        }
        if(renderer.getPause() != null) {
            inputMultiplexer.addProcessor(renderer.getPauseStage());
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        items.dispose();
        renderer.dispose();
    }


    public void setItems(String direction) {
        items.setDirection(direction);
    }
    public GameItemsTime getItems() { return items; }
    public String getItemsDirection() { return items.getDirection();}
    public Stage getRenderStage() { return renderer.getStage(); }
    public GameRendererTime getRenderer() { return renderer; }
    public InputMultiplexer getInputMultiplexer() { return  this.inputMultiplexer; }
    public DirectionGestureDetector getDirectionGestureDetector() { return this.directionGestureDetector; }
}
