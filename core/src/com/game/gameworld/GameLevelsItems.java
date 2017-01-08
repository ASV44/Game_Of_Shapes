package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.game.screens.GameScreenMoves;
import com.game.screens.GameScreenTime;
import com.game.shapes.HighScores;
import com.game.shapes.MyGame;

import java.util.List;
import java.util.Random;

/**
 * Created by hackintosh on 1/6/17.
 */

public class GameLevelsItems {
    private MyGame game;
    private String gameMode;
    public List<String>  highScores;
    private SpriteBatch batch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Color c;
    private FrameBuffer frameBuffer;
    private Sprite current;
    private Sprite next = null;
    private Sprite previous = null;
    private Texture Shape;
    private String shapeName = "Levels/";
    private int randomShape;
    private int prev_randomShape = 0;
    private int currentLevel;
    private int width;
    private int height;
    private Random random;
    private String direction;
    private float animation;
    private float animated = 0;
    private float increment = 25;

    public GameLevelsItems(MyGame game, String gameMode) {
        this.game = game;
        this.gameMode = gameMode;
        chooseScores(gameMode, game.getHighScores());
        random = new Random();
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("code_light.otf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (0.06 * Gdx.graphics.getHeight());;
        font = generator.generateFont(parameter);
        generator.dispose();
        c = batch.getColor();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        animation = (float) (0.824 * width);
        direction = "NON";
        currentLevel = this.highScores.size();
        createShapeTexture();
        createFrameBufferShape(currentLevel);
        current = new Sprite(frameBuffer.getColorBufferTexture());
        current.setBounds((float) (0.175 * width),(float) (0.3377 * height),(float) (0.648 * width),(float) (0.364 * height));
        current.flip(false,true);
    }

    public void update() {
        if(direction.equals("left")) { moveLeft(); }
        if(direction.equals("right")) { moveRight(); }
        if(direction.equals("up")) { moveUp(); }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.getInputMultiplexer().clear();
            game.setScreen(game.getModeScreen());
            game.getInputMultiplexer().addProcessor(game.getModeScreen().getStage());
        }

    }

    public void chooseScores(String gameMode, HighScores highScores) {
        if(gameMode.equals("countMoves")) { this.highScores = highScores.countMoves; }

        if(gameMode.equals("timeAttack")) { this.highScores = highScores.timeAttack; }

        if(gameMode.equals("timeChallenge")) { this.highScores = highScores.timeChallenge; }
    }

    public void createShapeTexture() {
        while((randomShape = random.nextInt(24) + 1) == prev_randomShape) {}
        Shape = new Texture(Gdx.files.internal(shapeName + randomShape + ".png"));
        prev_randomShape = randomShape;
    }

    public void createFrameBufferShape(int level) {
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height,false);
        frameBuffer.begin();
        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(Shape,0,0,width,height);
        font.draw(batch,"Level",(float) (0.37 * width),(float) (0.9375 * height));
        font.draw(batch,(level + 5) + " x " + (level + 5),(float) (0.37 * width),(float) (0.825 * height));
        font.draw(batch,"Best Score",(float) (0.225 * width),(float) (0.295 * height));
        font.draw(batch,"" + highScores.get(level - 1),(float) (0.4 * width),(float) (0.195 * height));
        batch.end();
        frameBuffer.end();
    }

    public void moveUp() {
        float animationVertical = (float) (0.682 * height);

        if(animated < animationVertical) {
            current.setY(current.getY() + increment);
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
            startLevel(gameMode);
        }
    }

    public void moveLeft() {
        if(next == null && currentLevel < highScores.size()) {
            createShapeTexture();
            createFrameBufferShape(currentLevel + 1);
            next = new Sprite(frameBuffer.getColorBufferTexture());
            next.setBounds((float) (width), (float) (0.3377 * height), (float) (0.648 * width), (float) (0.364 * height));
            next.flip(false, true);
        }
        if(animated < animation && currentLevel < highScores.size()) {
            current.setX(current.getX() - increment);
            next.setX(next.getX() - increment);
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
        }
        if(direction.equals("NON") && currentLevel < highScores.size()) {
            current = next;
            next = null;
            currentLevel ++;
        }
    }

    public void moveRight() {
        if(previous == null && currentLevel > 1) {
            createShapeTexture();
            createFrameBufferShape(currentLevel - 1);
            previous = new Sprite(frameBuffer.getColorBufferTexture());
            previous.setBounds((float) (0 - 0.648 * width), (float) (0.3377 * height), (float) (0.648 * width), (float) (0.364 * height));
            previous.flip(false, true);
        }
        if(animated < animation && currentLevel > 1) {
            current.setX(current.getX() + increment);
            previous.setX(previous.getX() + increment);
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
        }
        if(direction.equals("NON") && currentLevel > 1) {
            current = previous;
            previous = null;
            currentLevel --;
        }
    }

    public void startLevel(String gameMode) {
        if(gameMode.equals("countMoves")) {
            game.setScreenMoves(new GameScreenMoves(game, "Moves", currentLevel));
            game.setScreen(game.getScreenMoves());
        }
        if(gameMode.equals("timeChallenge")) {
            game.setScreenMoves(new GameScreenMoves(game, "Time1", currentLevel));
            game.setScreen(game.getScreenMoves());
        }
        if(gameMode.equals("timeAttack")) {
            game.setScreenTime(new GameScreenTime(game, "Time", currentLevel));
            game.setScreen(game.getScreenTime());
        }
    }

    public Sprite getCurrent() { return this.current; }

    public Sprite getNext() { return this.next; }

    public Sprite getPrevious() { return this.previous; }

    public String getDirection() { return this.direction; }

    public Texture getShape() { return Shape; }

    public SpriteBatch getBatch() { return batch; }

    public void setDirection(String direction) { this.direction = direction; }

}
