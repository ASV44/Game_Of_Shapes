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
    MyGame game;
    private FrameBuffer frameBuffer;
    private int width;
    private int height;
    private Texture[] Shapes;
    private Color c;
    Sprite sprite;

    public GameLevelsRenderer(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("code_light.otf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (0.06 * Gdx.graphics.getHeight());;
        font = generator.generateFont(parameter);
        generator.dispose();
        c = batch.getColor();
        background = new Texture(Gdx.files.internal("levelsBackground.png"));
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height,false);//(int) (0.3 * width),(int) (0.1 * height), false);
        Shapes = new Texture[1];
        Shapes[0] = new Texture(Gdx.files.internal("shape1.png"));
    }

    public void render() {
        frameBuffer.begin();
        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(Shapes[0],0,0,width,height);
        font.draw(batch,"Level",(float) (0.4 * width),(float) (0.9375 * height));
        font.draw(batch,"6 x 6",(float) (0.4 * width),(float) (0.825 * height));
        font.draw(batch,"Best Score",(float) (0.225 * width),(float) (0.295 * height));
        font.draw(batch,"0000",(float) (0.4 * width),(float) (0.195 * height));
        batch.end();
        frameBuffer.end();
        batch.setColor(c.r, c.g, c.b, 1f); //set alpha to 1
        batch.begin();
        batch.draw(background,0,0,width,height);
        sprite = new Sprite(frameBuffer.getColorBufferTexture());
        sprite.flip(false,true);
        sprite.setBounds((float) (0.185 * width),(float) (0.3645 * height),(float) (0.653 * width),(float) (0.367 * height));
        //batch.draw(,100,500,(float) (0.653 * width),(float) (0.367 * height));
        sprite.draw(batch);
        //batch.draw(Shapes[0],100,100,(float) (0.653 * width),(float) (0.367 * height));
        batch.end();
    }
}
