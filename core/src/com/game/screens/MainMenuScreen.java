package com.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.gameworld.MainMenuRenderer;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 11/27/16.
 */

public class MainMenuScreen implements Screen {
    private MainMenuRenderer renderer;

    public MainMenuScreen(MyGame game) {
        renderer = new MainMenuRenderer(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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

    public Stage getStage() { return renderer.getStage();}
}