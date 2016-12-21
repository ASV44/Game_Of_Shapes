package com.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by hackintosh on 10/7/16.
 */

public class GameRendererBackUp {

    private GameItemsBackUp items;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Stage stage;
    private float time;

    public GameRendererBackUp(GameItemsBackUp items) {
        this.items = items;
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer();
        //shapeRenderer.setProjectionMatrix(camera.combined);
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("good_time.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = 28;
        font = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage();
        stage.addActor(items.getRestartButton());
        //Gdx.input.setInputProcessor(stage);

    }

    public void render() {
        //Gdx.app.log("GameRendererBackUp", "render");
        //Gdx.app.log("Screen", "x="+ Gdx.graphics.getWidth()+ " y="+ Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(255, 255, 255, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.rect(items.getRect().x, items.getRect().y,
                items.getRect().width, items.getRect().height);
        shapeRenderer.rect(items.getRect1().x, items.getRect1().y,
                items.getRect1().width, items.getRect1().height);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.rect(items.getRect().x, items.getRect().y,
                items.getRect().width, items.getRect().height);
        shapeRenderer.end();*/
        batch.begin();
        batch.draw(items.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //batch.draw(items.getShape(), (float) 0.42 * Gdx.graphics.getWidth(), (float) 0.175 *  Gdx.graphics.getHeight(), (float) 0.163 * Gdx.graphics.getWidth() , (float) 0.1 * Gdx.graphics.getHeight());
        //batch.draw(items.getShape(), (float) 0.42 * Gdx.graphics.getWidth(), (float) (0.175 *  Gdx.graphics.getHeight() + 3 * 0.1 * Gdx.graphics.getHeight() + 3*0.0115* Gdx.graphics.getHeight()) , (float) 0.163 * Gdx.graphics.getWidth() , (float) 0.1 * Gdx.graphics.getHeight());
        //batch.draw(items.getShape(), (float) (0.0532 * Gdx.graphics.getWidth() + 0*0.163 * Gdx.graphics.getWidth() + 0*0.0203* Gdx.graphics.getWidth() ), (float) 0.3985 *  Gdx.graphics.getHeight(), (float) 0.163 * Gdx.graphics.getWidth() , (float) 0.1 * Gdx.graphics.getHeight());
        for(int i = 0; i < items.getNumber_of_shapes(); i++) {
            if (items.getShapesVertical(i) != null) {
            drawShape_vertical(items.getShapesVertical(i));}
            if (items.getShapesHorizontal(i) != null) {
            drawShape_horizontal(items.getShapesHorizontal(i));}
        }

        font.draw(batch, "Moves:" + items.getMoves(),(float) 0.04* Gdx.graphics.getWidth(), (float) 0.77 * Gdx.graphics.getHeight());
        time = items.getTime();
        font.draw(batch, "Time:" + ((int) time / 60 ) + ":" +  ((int) time  % 60) ,150,300);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw();
    }

    public void drawShape_vertical(Shape shape) {
        if(shape.region == null && shape.position < 5) {
            batch.draw(shape, shape.x, shape.y, shape.width, shape.height);
        }
        if(shape.region != null && (shape.position == 4 || shape.position == 0)) {
            batch.draw(shape.region,shape.x, shape.y,shape.width,shape.height - shape.animated);
        }
        if(shape.region != null && (shape.position == items.getNumber_of_shapes() - 1 || shape.position == 5)) {
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
        if(shape.region != null && (shape.position == items.getNumber_of_shapes() - 1 || shape.position == 5)) {
            batch.draw(shape.region,shape.x, shape.y,shape.animated + shape.width - shape.animation, shape.height);
        }
    }

    public Stage getStage() { return stage;}
}
