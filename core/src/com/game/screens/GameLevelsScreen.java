package com.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.game.gameworld.GameLevelsItems;
import com.game.gameworld.GameLevelsRenderer;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 12/29/16.
 */

public class GameLevelsScreen implements Screen {
    private GameLevelsItems items;
    private GameLevelsRenderer renderer;
    private InputMultiplexer inputMultiplexer;

    public GameLevelsScreen(MyGame game, String gameMode) {
        this.inputMultiplexer = game.getInputMultiplexer();
        inputMultiplexer.clear();
        items = new GameLevelsItems(gameMode, game.getHighScores());
        renderer =  new GameLevelsRenderer(game);
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

    }
}
