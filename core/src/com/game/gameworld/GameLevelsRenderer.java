package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 12/29/16.
 */

public class GameLevelsRenderer {
    private SpriteBatch batch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Texture background;
    GameLevelsItems items;
    MyGame game;
    private int width;
    private int height;
    private Texture[] Shapes;
    private Color c;
    Sprite sprite;

    public GameLevelsRenderer(MyGame game,GameLevelsItems items) {
        this.game = game;
        this.items = items;
        batch = new SpriteBatch();
        c = batch.getColor();
        background = new Texture(Gdx.files.internal("levelsBackground.png"));
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public void render() {
        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setColor(c.r, c.g, c.b, 1f); //set alpha to 1
        //sprite = items.getCurrent();
        batch.begin();
        batch.draw(background,0,0,width,height);
        items.getCurrent().draw(batch);
        if(items.getNext() != null) { items.getNext().draw(batch); }
        if(items.getPrevious() != null) { items.getPrevious().draw(batch); }
        batch.end();
    }

    public void dispose() {
        background.dispose();
        batch.dispose();
    }

}
