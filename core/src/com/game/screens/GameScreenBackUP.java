package com.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.gameworld.GameItemsBackUp;
import com.game.gameworld.GameRendererBackUp;

/**
 * Created by hackintosh on 10/7/16.
 */

public class GameScreenBackUP implements Screen {

    private GameItemsBackUp items;
    private GameRendererBackUp renderer;

    public GameScreenBackUP(){
        items = new GameItemsBackUp();
        renderer = new GameRendererBackUp(items);
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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void setItems(String direction) {
        items.setDirection(direction);
    }
    public String getItemsDirection() { return items.getDirection();}
    public Stage getRenderStage() { return renderer.getStage(); }
}
