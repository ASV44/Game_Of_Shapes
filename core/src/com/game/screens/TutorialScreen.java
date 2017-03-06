package com.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.gameworld.GameItemsMoves;
import com.game.gameworld.GameItemsTutorial;
import com.game.gameworld.GameRendererMoves;
import com.game.gameworld.GameRendererTutorial;
import com.game.gesturedetector.DirectionGestureDetector;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 3/1/17.
 */

public class TutorialScreen implements Screen {
    private GameItemsTutorial items;
    private GameRendererTutorial renderer;
    private InputMultiplexer inputMultiplexer;
    private DirectionGestureDetector directionGestureDetector;

    public TutorialScreen(MyGame game, String mode, int level) {
        if(game.getLevelsScreen() != null) {
            game.getLevelsScreen().dispose();
            game.setLevelsScreeen(null);
        }
        this.inputMultiplexer = game.getInputMultiplexer();
        inputMultiplexer.clear();
        items = new GameItemsTutorial(game, mode, level);
        renderer = new GameRendererTutorial(items);

        directionGestureDetector = new DirectionGestureDetector(new DirectionGestureDetector.DirectionListener() {
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
        //Gdx.input.setInputProcessor(inputMultiplexer);
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
    public GameItemsTutorial getItems() { return items; }
    public GameRendererTutorial getRenderer() { return renderer; }
    public String getItemsDirection() { return items.getDirection();}
    public Stage getRenderStage() { return renderer.getStage(); }
}
