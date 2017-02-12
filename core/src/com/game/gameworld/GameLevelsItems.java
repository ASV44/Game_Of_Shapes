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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private BitmapFont font1;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private FrameBuffer frameBuffer = null;
    private FrameBuffer prev_frameBuffer = null;
    private Sprite current;
    private Sprite next = null;
    private Sprite previous = null;
    private Texture Shape = null;
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
    private float increment;
    private Sprite sprite;
    private ImageButton level;
    private Skin skin = null;
    private Stage stage = null;
    private FrameBuffer[] levelsFrames;
    private FrameBuffer[] prev_levelsFrames;
    private ImageButton[] levelsButtons = null;
    private ImageButton[] following_levelsButtons = null;

    public GameLevelsItems(MyGame game, String gameMode) {
        this.game = game;
        this.gameMode = gameMode;
        chooseScores(gameMode, game.getHighScores());
        random = new Random();
        batch = new SpriteBatch();
        font = create_BitmapFont(6);
        font1 = create_BitmapFont(12);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        increment = (int) (0.037 * height);
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
        if(direction.equals("up")) {
            if(levelsButtons == null) { moveUp(); }
            else { direction = "NON"; }
        }
        if(direction.equals("down")) {
            if(levelsButtons == null) { moveDown(); }
            else { direction = "NON"; }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
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
        if(Shape != null) { Shape.dispose(); }
        Shape = new Texture(Gdx.files.internal(shapeName + randomShape + ".png"));
        prev_randomShape = randomShape;
    }

    public void createFrameBufferShape(int level) {
        if(prev_frameBuffer != null) { prev_frameBuffer.dispose(); }
        if(frameBuffer != null) { prev_frameBuffer = frameBuffer; }
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
            startLevel(gameMode, currentLevel);
        }
    }

    public void moveDown() {
        float animationVertical = (float) (0.682 * height);

        if(animated < animationVertical) {
            current.setY(current.getY() - increment);
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
        }
        if(direction.equals("NON")) {
            if(stage == null) { stage = new Stage(); }
            create_levelsFrames();
            levelsButtons = create_levelsButtons("center");
            current = null;
        }
    }

    public void moveLeft() {
        if(current == null) { moveGrid_Left(); }
        if(current != null) { moveLevel_Left();}
    }

    public void moveLevel_Left() {
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
        if(current == null) { moveGrid_Right(); }
        if(current != null) { moveLevel_Right(); }
    }

    public void moveLevel_Right() {
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

    public void startLevel(String gameMode, int level) {
        if(game.getScreenMoves() != null) {
            game.getScreenMoves().dispose();
            game.setScreenMoves(null);
        }
        if(game.getScreenTime() != null) {
            game.getScreenTime().dispose();
            game.setScreenTime(null);
        }
        if(gameMode.equals("countMoves")) {
            game.setScreenMoves(new GameScreenMoves(game, "Moves", level));
            game.setCurrent_gameScreen(game.getScreenMoves());
            game.setScreen(game.getScreenMoves());
        }
        if(gameMode.equals("timeChallenge")) {

            game.setScreenMoves(new GameScreenMoves(game, "Time1", level));
            game.setCurrent_gameScreen(game.getScreenMoves());
            game.setScreen(game.getScreenMoves());
        }
        if(gameMode.equals("timeAttack")) {
            game.setScreenTime(new GameScreenTime(game, "Time", level));
            game.setCurrent_gameScreen(game.getScreenTime());
            game.setScreen(game.getScreenTime());
        }
    }

    public void create_levelsFrames() {
        Gdx.app.log("create levels Frames","start");
        Gdx.app.log("current level","" + currentLevel);
        int levels_onScreen = currentLevel % 9;
        if(levels_onScreen != 0) { levels_onScreen += (highScores.size() - currentLevel); }
        int startLevel = 6 + (((currentLevel - 1) / 9) * 9);
        if(prev_levelsFrames != null) {
            for(int i = 0; i < prev_levelsFrames.length; i++) { prev_levelsFrames[i].dispose(); }
        }
        if(levelsFrames != null) { prev_levelsFrames = levelsFrames; }
        if(levels_onScreen >= 9 || levels_onScreen == 0) {
            levelsFrames = new FrameBuffer[9];
        }
        else {
            levelsFrames = new FrameBuffer[levels_onScreen];
        }
        for(int i = 0; i < levelsFrames.length; i++) {
            levelsFrames[i] = new FrameBuffer(Pixmap.Format.RGBA8888, width, height,false);
            createShapeTexture();
            levelsFrames[i].begin();
            Gdx.gl.glClearColor(255, 255, 255, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(Shape,0,0,width,height);
            font1.draw(batch,(startLevel + i) + " x " + (startLevel + i),(float) (0.17 * width),(float) (0.625 * height));
            font1.draw(batch,"" + highScores.get(startLevel - 6 + i),(float) (0.3 * width),(float) (0.245 * height));
            batch.end();
            levelsFrames[i].end();
        }
        currentLevel = startLevel - 5;
    }

    public  ImageButton[] create_levelsButtons(String position) {
        final ImageButton[] levelsButtons = new ImageButton[levelsFrames.length];
        for(int i = 0; i < levelsFrames.length; i++) {
            if(skin != null) { skin.dispose(); }
            skin = new Skin();
            sprite = new Sprite(levelsFrames[i].getColorBufferTexture());
            sprite.setBounds(0, 0, (float) (0.648 * width), (float) (0.3164 * height));
            sprite.flip(false, true);
            skin.add("sprite", sprite);
            levelsButtons[i] = new ImageButton(skin.getDrawable("sprite"));
            levelsButtons[i].setBounds((float) (0.05 * width + (i % 3) * (0.32 * width)), (float) (0.65 * height - (i / 3) * (0.2 * height)),
                    (float) (0.2592 * width), (float) (0.1456 * height));
            if(position.equals("ToLeft")) {
                levelsButtons[i].setX((float) (width + 15 + (i % 3) * (0.32 * width)));
            }
            if(position.equals("ToRight")) {
                levelsButtons[i].setX((float) (-(2 * 0.32 + 0.28 ) * width + (i % 3) * (0.32 * width)));
            }
            final int lvl = i;
            levelsButtons[i].addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    Gdx.app.log("Button " + (currentLevel + lvl),"tap works");
                    startLevel(gameMode,currentLevel + lvl);
                }

                @Override
                public boolean longPress(Actor actor, float x, float y) {
                    Gdx.app.log("Button " + (currentLevel + lvl),"long press works");
                    setCurrentLevel(currentLevel + lvl);
                    createShapeTexture();
                    createFrameBufferShape(currentLevel);
                    current = new Sprite(frameBuffer.getColorBufferTexture());
                    current.setBounds((float) (0.175 * width),(float) (0.3377 * height),(float) (0.648 * width),(float) (0.364 * height));
                    current.flip(false,true);
                    levelsGrid_dispose();
                    return true;
                }
            });
            stage.addActor(levelsButtons[i]);
        }
        skin.dispose();
        skin = null;
        game.getInputMultiplexer().addProcessor(stage);

        return  levelsButtons;
    }

    public void moveGrid_Left() {
        float animation = (float) (0.95 * width);

        if(following_levelsButtons == null && (currentLevel + 9) <= highScores.size()) {
            currentLevel += 9;
            create_levelsFrames();
            following_levelsButtons = create_levelsButtons("ToLeft");
        }
        if (animated < animation && following_levelsButtons != null) {
            for(int i = 0; i < levelsButtons.length; i++) {
                levelsButtons[i].setX(levelsButtons[i].getX() - increment);
            }
            for(int i = 0; i <following_levelsButtons.length; i++) {
                following_levelsButtons[i].setX(following_levelsButtons[i].getX() - increment);
            }
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
            if(following_levelsButtons != null) {
                levelsButtons = following_levelsButtons;
            }
            following_levelsButtons = null;
        }
    }

    public void moveGrid_Right() {
        float animation = (float) (0.95 * width);

        if(following_levelsButtons == null && (currentLevel - 9) >= 1) {
            currentLevel -= 9;
            create_levelsFrames();
            following_levelsButtons = create_levelsButtons("ToRight");
        }
        if (animated < animation && following_levelsButtons != null) {
            for(int i = 0; i < levelsButtons.length; i++) {
                levelsButtons[i].setX(levelsButtons[i].getX() + increment);
            }
            for(int i = 0; i <following_levelsButtons.length; i++) {
                following_levelsButtons[i].setX(following_levelsButtons[i].getX() + increment);
            }
            animated += increment;
        }
        else {
            direction = "NON";
            animated = 0;
            if(following_levelsButtons != null) {
                levelsButtons = following_levelsButtons;
            }
            following_levelsButtons = null;
        }
    }

    public BitmapFont create_BitmapFont(float fontSize) {
        fontSize = fontSize / 100;
        BitmapFont font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("code_light.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (fontSize * Gdx.graphics.getHeight());
        font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    public void levelsGrid_dispose() {
        if(stage != null) {
            stage.dispose();
            stage = null;
        }
        levelsButtons = null;
        following_levelsButtons = null;
        if(levelsFrames != null) {
            arrayFrameBuffer_dispose(levelsFrames);
            levelsFrames = null;
        }
        if(prev_levelsFrames != null) {
            arrayFrameBuffer_dispose(prev_levelsFrames);
            prev_levelsFrames = null;
        }
    }

    public void arrayFrameBuffer_dispose(FrameBuffer[] frameBuffers) {
        for(int i = 0; i < frameBuffers.length; i++) {
            frameBuffers[i].dispose();
        }
    }

    public void dispose() {
        batch.dispose();
        Shape.dispose();
        font.dispose();
        font1.dispose();
        frameBuffer.dispose();
        if(prev_frameBuffer != null) { frameBuffer.dispose(); }
        levelsGrid_dispose();
    }

    public Sprite getCurrent() { return this.current; }

    public Sprite getNext() { return this.next; }

    public Sprite getPrevious() { return this.previous; }

    public String getDirection() { return this.direction; }

    public void setDirection(String direction) { this.direction = direction; }

    public Stage getStage() { return  this.stage; }

    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }


}
