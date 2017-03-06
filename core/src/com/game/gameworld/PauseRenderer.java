package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.screens.GameLevelsScreen;
import com.game.screens.GameModeScreen;
import com.game.shapes.MyGame;

import java.text.DecimalFormat;

/**
 * Created by hackintosh on 1/19/17.
 */

public class PauseRenderer {
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private int width, height;
    private SpriteBatch batch;
    private Texture foreground;
    private ImageButton levels;
    private ImageButton modes;
    private ImageButton vibration;
    private MyGame game;
    private Stage stage;
    private String gameMode;
    private Skin skin;
    private Color c;

    public PauseRenderer(final MyGame game, SpriteBatch batch, final String gameMode) {
        this.game = game;
        this.batch = batch;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("good_time.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (0.03 * Gdx.graphics.getHeight());;
        font = generator.generateFont(parameter);
        generator.dispose();
        foreground = new Texture(Gdx.files.internal("foreground.png"));
        this.gameMode = gameMode;
        stage = new Stage();
        skin = new Skin();
        skin.add("levels",new Texture(Gdx.files.internal("levels.png")));
        levels = new ImageButton(skin.getDrawable("levels"));
        levels.setBounds((float) 0.09 * width, (float) 0.225 * height,(float) 0.231 * width, (float) 0.082 * height);
        levels.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("ButtonLevels","works");
                createLevelsScreen();
                game.setScreen(game.getLevelsScreen());
            }
        });
        stage.addActor(levels);
        skin.add("vibration",new Texture(Gdx.files.internal("vibration.png")));
        skin.add("vibrationOff", new Texture(Gdx.files.internal("vibrationOff.png")));
        vibration = new ImageButton(skin.getDrawable("vibration"),skin.getDrawable("vibration"),skin.getDrawable("vibrationOff"));
        vibration.setBounds((float) 0.388 * width, (float) 0.225 * height,(float) 0.231 * width, (float) 0.082 * height);
        chooseVibrationBattonState();
        vibration.addListener(new ActorGestureListener() {
           @Override
           public  void tap(InputEvent event, float x, float y, int pointer, int button) {
               Gdx.app.log("ButtonVibration","works");
               if(gameMode.equals("Moves") || gameMode.equals("Time1")) {
                   if(game.getScreenMoves().getItems().getVibrate()) {
                       game.getScreenMoves().getItems().setVibrate(false);
                       vibration.setChecked(true);
                   }
                   else {
                       game.getScreenMoves().getItems().setVibrate(true);
                       vibration.setChecked(false);
                   }
               }
               if(gameMode.equals("Time")) {
                   if(game.getScreenTime().getItems().getVibrate()) {
                       game.getScreenTime().getItems().setVibrate(false);
                       vibration.setChecked(false);
                   }
                   else {
                       game.getScreenTime().getItems().setVibrate(true);
                       vibration.setChecked(true);
                   }
               }
           }
        });
        stage.addActor(vibration);
//        buttonTexture = new Texture(Gdx.files.internal("gameOver.png"));
//        skin.add("gameModes", new TextureRegion(buttonTexture,100,725,245,151));
        skin.add("gameModes",new Texture(Gdx.files.internal("main_menu.png")));
        modes = new ImageButton(skin.getDrawable("gameModes"));
        modes.setBounds((float) 0.692 * width, (float) 0.225 * height,(float) 0.231 * width, (float) 0.082 * height);
        modes.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Button_gameModes","works ");
                if(game.getModeScreen() == null) {
                    game.setScreenMode(new GameModeScreen(game));
                }
                else
                {
                    game.getInputMultiplexer().clear();
                    game.getInputMultiplexer().addProcessor(game.getModeScreen().getStage()); }
                game.setScreen(game.getModeScreen());
            }
        });
        stage.addActor(modes);
        game.getInputMultiplexer().addProcessor(stage);

    }

    public void render() {
        batch.begin();
        c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, .7f);//set alpha to 0.3
        batch.draw(foreground, 0, 0, width, height);
        batch.setColor(c.r, c.g, c.b, 1f); //set alpha to 1
        font.draw(batch,"Pause:", (float) 0.35 * Gdx.graphics.getWidth(), (float) 0.73 * Gdx.graphics.getHeight());
        font.draw(batch,"Current Score is: ", (float) 0.18 * Gdx.graphics.getWidth(), (float) 0.65 * Gdx.graphics.getHeight());
        showCurrentScore();
        font.draw(batch,"Best Score is: ", (float) 0.18 * Gdx.graphics.getWidth(), (float) 0.55 * Gdx.graphics.getHeight());
        showBestScore();
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw();
    }

    public void chooseVibrationBattonState() {
        if(gameMode.equals("Moves") || gameMode.equals("Time1")) {
            if(!game.getScreenMoves().getItems().getVibrate()) {
                vibration.setChecked(true);
            }
            else {
                vibration.setChecked(false);
            }
        }
        if(gameMode.equals("Time")) {
            if(!game.getScreenTime().getItems().getVibrate()) {
                vibration.setChecked(true);
            }
            else {
                vibration.setChecked(false);
            }
        }
    }

    public void createLevelsScreen() {
        if(game.getLevelsScreen() != null) { game.getLevelsScreen().dispose(); }
        if(gameMode.equals("Moves")) {
            game.setLevelsScreeen(new GameLevelsScreen(game,"countMoves"));
        }
        if(gameMode.equals("Time1")) {
            game.setLevelsScreeen(new GameLevelsScreen(game,"timeChallenge"));
        }
        if(gameMode.equals("Time")) {
            game.setLevelsScreeen(new GameLevelsScreen(game,"timeAttack"));
        }
    }

    public void showCurrentScore() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        if(gameMode.equals("Moves")) {
            font.draw(batch, "" + game.getScreenMoves().getItems().getScore(), (float) 0.45 * width, (float) 0.6 * height);
        }
        if(gameMode.equals("Time")) {
            font.draw(batch, "" + game.getScreenTime().getItems().getScore(), (float) 0.45 * width, (float) 0.6 * height);
        }
        if(gameMode.equals("Time1")) {
            font.draw(batch, "" + decimalFormat.format(game.getScreenMoves().getItems().getTime()), (float) 0.45 * width, (float) 0.6 * height);
        }
        if(gameMode.equals("Tutorial")) {
            font.draw(batch, "" + game.getTutorialScreen().getItems().getScore(), (float) 0.45 * width, (float) 0.6 * height);
        }
    }

    public void showBestScore() {
        if(gameMode.equals("Moves")) {
            font.draw(batch, "" + game.getHighScores().getScores(gameMode).get(game.getScreenMoves().getItems().getLevel() - 6),
                    (float) 0.45 * width, (float) 0.5 * height);
        }
        if(gameMode.equals("Time")) {
            font.draw(batch, "" + game.getHighScores().getScores(gameMode).get(game.getScreenTime().getItems().getLevel() - 6),
                    (float) 0.45 * width, (float) 0.5 * height);
        }
        if(gameMode.equals("Time1")) {
            font.draw(batch, "" + game.getHighScores().getScores(gameMode).get(game.getScreenMoves().getItems().getLevel() - 6) + " sec",
                    (float) 0.38 * width, (float) 0.5 * height);
        }
        if(gameMode.equals("Tutorial")) {
            font.draw(batch, "Do the Best", (float) 0.34 * width, (float) 0.5 * height);
        }
    }

    public  void dispose() {
        foreground.dispose();
        stage.dispose();
        font.dispose();
        skin.dispose();
    }

    public Stage getStage() { return this.stage; }


}
