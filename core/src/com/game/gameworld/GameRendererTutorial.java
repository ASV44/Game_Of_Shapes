package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by hackintosh on 3/1/17.
 */

public class GameRendererTutorial {
    private GameItemsTutorial items;
    private SpriteBatch batch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Stage stage;
    private float time;
    private GameOverRenderer gameOver;
    private String level;
    private PauseRenderer pauseRenderer;

    public GameRendererTutorial(GameItemsTutorial items) {
        this.items = items;
        //shapeRenderer.setProjectionMatrix(camera.combined);
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("good_time.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) (0.015 * Gdx.graphics.getHeight());
        font = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage();
        stage.addActor(items.getRestartButton());
        stage.addActor(items.getPauseButton());
        this.items.getGame().getInputMultiplexer().addProcessor(stage);
        gameOver = null;
        level = "" + items.getLevel() + " x " + items.getLevel();
        pauseRenderer = null;

    }

    public void render() {
        //Gdx.app.log("GameRendererBackUp", "render");
        //Gdx.app.log("Screen", "x="+ Gdx.graphics.getWidth()+ " y="+ Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1f); //set alpha to 1
        batch.draw(items.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for(int i = 0; i < items.getNumber_of_shapes_vertical(); i++) {
            if (items.getShapesVertical(i) != null) {
                drawOpacity_vertical(items.getShapesVertical(i));}
        }
        for(int i = 0; i < items.getNumber_of_shapes_horizontal(); i++) {
            if (items.getShapesHorizontal(i) != null && i != 2) {
                drawOpacity_horizontal(items.getShapesHorizontal(i));
            }
        }
        for(int i = 0; i < items.getNumber_of_shapes_vertical(); i++) {
            if (items.getShapesVertical(i) != null) {
                drawShape_vertical(items.getShapesVertical(i));}
        }
        for(int i = 0; i < items.getNumber_of_shapes_horizontal(); i++) {
            if (items.getShapesHorizontal(i) != null && i != 2) {
                drawShape_horizontal(items.getShapesHorizontal(i));
            }
        }
        if(!items.getHand_direction().equals("NONE")) {
            if(items.getHand_direction().equals("left") || items.getHand_direction().equals("right")) {
                items.getHand().draw(batch, items.getHand_x(), items.getHand_y(),(float) 0.185 * Gdx.graphics.getWidth() , (float) 0.225 * Gdx.graphics.getHeight());
            }
            else {
                items.getHand().draw(batch, items.getHand_x(), items.getHand_y(), (float) 0.370 * Gdx.graphics.getWidth(), (float) 0.112 * Gdx.graphics.getHeight());
            }
        }

        font.draw(batch, level,(float) 0.1* Gdx.graphics.getWidth(), (float) 0.82 * Gdx.graphics.getHeight());
        font.draw(batch, "Score:" + items.getScore(),(float) 0.33* Gdx.graphics.getWidth(), (float) 0.82 * Gdx.graphics.getHeight());
        time = items.getTime();
        if(items.getMode().equals("Time") || items.getMode().equals("Time1")) { font.draw(batch, "Time:" + ((int) time / 60 ) + ":" +  ((int) time  % 60) ,(float)0.8* Gdx.graphics.getWidth(), (float) 0.68 * Gdx.graphics.getHeight());}
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw();
        if(items.getGameState().equals("gameOver")) {
            if(gameOver == null) { gameOver = new GameOverRenderer(items.getGame(), batch, items.getMode());}
            gameOver.render();
        }
        else {
            if(gameOver != null) {
                items.getGame().getInputMultiplexer().removeProcessor(gameOver.getStage());
                gameOver.dispose();
                gameOver = null;
            }
        }
        if(items.getGameState().equals("Pause")) {
            if(pauseRenderer == null) { pauseRenderer = new PauseRenderer(items.getGame(), batch, items.getMode()); }
            pauseRenderer.render();
        }
        else {
            if(pauseRenderer != null) {
                items.getGame().getInputMultiplexer().removeProcessor(pauseRenderer.getStage());
                pauseRenderer.dispose();
                pauseRenderer = null;
            }
        }
    }

    public void drawShape_vertical(Shape shape) {
        if(shape.region == null && shape.position < 5) {
            batch.draw(shape, shape.x, shape.y, shape.width, shape.height);
        }
        if(shape.region != null && (shape.position == 4 || shape.position == 0)) {
            batch.draw(shape.region,shape.x, shape.y,shape.width,shape.height - shape.animated);
        }
        if(shape.region != null && (shape.position == items.getNumber_of_shapes_vertical() - 1 || shape.position == 5)) {
            batch.draw(shape.region,shape.x, shape.y,shape.width, shape.animated + shape.height - shape.animation);
        }
    }

    public void drawShape_horizontal(Shape shape) {
        if(shape.region == null && shape.position < 5) {
            batch.draw(shape, shape.x, shape.y, shape.width, shape.height);
        }
        if(shape.region != null && (shape.position == 4 || shape.position ==0)) {
            batch.draw(shape.region,shape.x, shape.y,shape.width - shape.animated,shape.height);
        }
        if(shape.region != null && (shape.position == items.getNumber_of_shapes_horizontal() - 1 || shape.position == 5)) {
            batch.draw(shape.region,shape.x, shape.y,shape.animated + shape.width - shape.animation, shape.height);
        }
    }

    public void drawOpacity_vertical(Shape shape) {
        if(shape.region == null && shape.position < 5) {
            batch.draw(shape.opacity,shape.x,shape.y,shape.width,shape.height);
        }
        if(shape.region != null && (shape.position == 4 || shape.position == 0)) {
            batch.draw(shape.opacityRegion,shape.x,shape.y,shape.width,shape.height - shape.animated);
        }
        if(shape.region != null && (shape.position == items.getNumber_of_shapes_vertical() - 1 || shape.position == 5)) {
            batch.draw(shape.opacityRegion,shape.x, shape.y,shape.width, shape.animated + shape.height - shape.animation);
        }
    }

    public void drawOpacity_horizontal(Shape shape) {
        if(shape.region == null && shape.position < 5) {
            batch.draw(shape.opacity,shape.x,shape.y,shape.width,shape.height);
        }
        if(shape.region != null && (shape.position == 4 || shape.position ==0)) {
            batch.draw(shape.opacityRegion,shape.x, shape.y,shape.width - shape.animated,shape.height);
        }
        if(shape.region != null && (shape.position == items.getNumber_of_shapes_horizontal() - 1 || shape.position == 5)) {
            batch.draw(shape.opacityRegion,shape.x, shape.y,shape.animated + shape.width - shape.animation, shape.height);
        }
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        stage.dispose();
        if(gameOver != null) {
            gameOver.dispose();
        }
    }

    public Stage getStage() { return stage; }

    public void setLevel(int level) { this.level = this.level.replaceAll(String.valueOf(level - 1), String.valueOf(level));}

    public Stage getGameOverStage() {
        return gameOver.getStage();
    }

    public Stage getPauseStage() {
        return pauseRenderer.getStage();
    }

    public GameOverRenderer getGameOver() { return this.gameOver; }

    public PauseRenderer getPause() { return  this.pauseRenderer; }


}
