package com.game.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.screens.GameLevelsScreen;
import com.game.screens.GameScreenMoves;
import com.game.screens.GameScreenTime;
import com.game.shapes.MyGame;

/**
 * Created by hackintosh on 12/17/16.
 */

public class GameModeRenderer {
    private MyGame game;
    private SpriteBatch batch;
    /*private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;*/
    private Stage stage;
    private Texture background;
    private Texture buttonTexture;
    private Drawable drawable;
    private ImageButton timeAttack;
    private ImageButton countMoves;
    private ImageButton timeChallenge;
    private  ImageButton backButton;

    public GameModeRenderer(final MyGame game) {
        this.game = game;
        background = new Texture(Gdx.files.internal("game_mode.png"));
        batch = new SpriteBatch();
        /*generator = new FreeTypeFontGenerator(Gdx.files.internal("good_time.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (0.045 * Gdx.graphics.getHeight());
        font = generator.generateFont(parameter);
        generator.dispose();*/
        stage = new Stage();
        buttonTexture = new Texture(Gdx.files.internal("game_mode.png"));
        drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture,0,507,621,223));
        countMoves = new ImageButton(drawable);
        countMoves.setBounds((float) 0 * Gdx.graphics.getWidth(), (float) 0.622 * Gdx.graphics.getHeight(),(float) 0.575 * Gdx.graphics.getWidth(), (float) 0.116 * Gdx.graphics.getHeight());
        countMoves.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Button_count_moves","works ");
                if(game.getLevelsScreen() != null) { game.getLevelsScreen().dispose(); }
                game.setLevelsScreeen(new GameLevelsScreen(game,"countMoves"));
                game.setScreen(game.getLevelsScreen());
            }
        });
        stage.addActor(countMoves);
        drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture,0,843,621,223));
        timeAttack = new ImageButton(drawable);
        timeAttack.setBounds((float) 0 * Gdx.graphics.getWidth(), (float) 0.44 * Gdx.graphics.getHeight(), (float) 0.575 * Gdx.graphics.getWidth(), (float) 0.116 * Gdx.graphics.getHeight());
        timeAttack.addListener(new ActorGestureListener(){
            @Override
            public  void tap(InputEvent event, float x, float y, int count, int button) {
                Gdx.app.log("Button_time_attack","works ");
                if(game.getLevelsScreen() != null) { game.getLevelsScreen().dispose(); }
                game.setLevelsScreeen(new GameLevelsScreen(game,"timeAttack"));
                game.setScreen(game.getLevelsScreen());
                //return true;
            }
        });
        stage.addActor(timeAttack);
        drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture,0,1181,621,223));
        timeChallenge = new ImageButton(drawable);
        timeChallenge.setBounds((float) 0 * Gdx.graphics.getWidth(), (float) 0.272 * Gdx.graphics.getHeight(), (float) 0.575 * Gdx.graphics.getWidth(), (float) 0.116 * Gdx.graphics.getHeight());
        timeChallenge.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int count, int button) {
                Gdx.app.log("Button_time1","works ");
                if(game.getLevelsScreen() != null) { game.getLevelsScreen().dispose(); }
                game.setLevelsScreeen(new GameLevelsScreen(game,"timeChallenge"));
                game.setScreen(game.getLevelsScreen());
                //return true;
            }
        });
        //Gdx.input.setInputProcessor(stage);
        stage.addActor(timeChallenge);
        drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture,40,53,155,145));
        backButton = new ImageButton(drawable);
        backButton.setBounds((float) 0.038 * Gdx.graphics.getWidth(), (float) 0.895 * Gdx.graphics.getHeight(), (float) 0.143 * Gdx.graphics.getWidth(), (float) 0.0755 * Gdx.graphics.getHeight());
        backButton.addListener(new ActorGestureListener() {
            @Override
            public  void tap(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.log("Button_back", "works ");
                    game.setScreen(game.getMenuScreen());
                    game.getInputMultiplexer().addProcessor(game.getMenuScreen().getStage());
            }
        });
        stage.addActor(backButton);
        game.getInputMultiplexer().addProcessor(stage);
    }

    public void render() {

        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        //stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        //stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.getInputMultiplexer().clear();
            game.setScreen(game.getMenuScreen());
            game.getInputMultiplexer().addProcessor(game.getMenuScreen().getStage());
        }
    }

    public Stage getStage() { return this.stage; }
}
