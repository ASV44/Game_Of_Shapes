package com.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.game.gameworld.GameModeRenderer;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 12/17/16.
 */

public class GameModeScreen implements Screen {
    private GameModeRenderer renderer;
    private InputMultiplexer inputMultiplexer;

    public GameModeScreen(MyGame game) {
        this.inputMultiplexer = game.getInputMultiplexer();
        inputMultiplexer.clear();
        renderer = new GameModeRenderer(game); }

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
}
