package com.game.shapes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.game.gameworld.GameLevelsRenderer;
import com.game.screens.GameLevelsScreen;
import com.game.screens.GameModeScreen;
import com.game.screens.GameScreenMoves;
import com.game.screens.GameScreenTime;
import com.game.screens.MainMenuScreen;

public class MyGame extends Game {

	public interface CallBack {
		public void gameOver(int score);
	}
    //GameScreenBackUP screen;
	private GameScreenMoves screenMoves;
	private GameScreenTime screenTime;
	private InputMultiplexer inputMultiplexer;
	//private CallBack callBack;
	private MainMenuScreen menuScreen;
	private GameModeScreen modeScreen;
	private GameLevelsScreen levelsScreen;
	private HighScores highScores;
	private String gameMode;

	@Override
	public void create() {
		Gdx.app.log("Shapes_Game", "created");
		Gdx.app.log("Width",""+ Gdx.graphics.getWidth());
		Gdx.app.log("Height",""+Gdx.graphics.getHeight());
		highScores = new HighScores();
		inputMultiplexer = new InputMultiplexer();
        //screen = new GameScreenBackUP();
		//screen = new GameScreenTime(this);
		//screenMoves = new GameScreenMoves(this, "Moves");
		menuScreen = new MainMenuScreen(this);
		/*AsyncExecutor executor = new AsyncExecutor(1);
		try {
			executor.submit(new gestureExecution());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		//setScreen(screenMoves);
		setScreen(menuScreen);
		//screenMoves.getItems().setCallBack(callBack);
		//inputMultiplexer = new InputMultiplexer();
		//inputMultiplexer.addProcessor(screen.getRenderStage());
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);

			/*Gdx.input.setInputProcessor(new DirectionGestureDetector(new DirectionGestureDetector.DirectionListener() {
				@Override
				public void onUp() {
					//Gdx.app.log("Direction", "up");
					if(screen.getItemsDirection().equals("NON")) {
					screen.setItems("up");}
				}

				@Override
				public void onRight() {
					//Gdx.app.log("Direction", "right");
					if(screen.getItemsDirection().equals("NON")) {
						screen.setItems("right");}
				}

				@Override
				public void onLeft() {
					//Gdx.app.log("Direction", "left");
					if(screen.getItemsDirection().equals("NON")) {
						screen.setItems("left");}
				}

				@Override
				public void onDown() {
					//Gdx.app.log("Direction", "down");
					if(screen.getItemsDirection().equals("NON")) {
					screen.setItems("down");}
				}
			}));*/
	}
	/*class gestureExecution implements AsyncTask<Void> {
		@Override
		public Void call() throws Exception {
			Gdx.app.log("Async","execute");
			if(screen.getItemsDirection().equals("NON")) {
				Gdx.input.setInputProcessor(new DirectionGestureDetector(new DirectionGestureDetector.DirectionListener() {

					@Override
					public void onUp() {
						//Gdx.app.log("Direction", "up");
						screen.setItems("up");
					}

					@Override
					public void onRight() {
						//Gdx.app.log("Direction", "right");
					}

					@Override
					public void onLeft() {
						//Gdx.app.log("Direction", "left");
					}

					@Override
					public void onDown() {
						//Gdx.app.log("Direction", "down");
						screen.setItems("down");
					}
				}));
			};
			return null;
		}
	}*/
	public GameScreenTime getScreenTime() { return screenTime; }
	public GameScreenMoves getScreenMoves() { return screenMoves; }
	public MainMenuScreen getMenuScreen() { return  menuScreen; }
	public GameModeScreen getModeScreen() { return  modeScreen; }
	public GameLevelsScreen getLevelsScreen() { return levelsScreen; }
	//public void setCallBack(CallBack callBack) { this.callBack = callBack; }
	public InputMultiplexer getInputMultiplexer() { return  inputMultiplexer; }
	public String getGameMode() { return this.gameMode; }
	public HighScores getHighScores() { return this.highScores; }
	public void setScreenMoves(GameScreenMoves screenMoves) {this.screenMoves = screenMoves; }
	public void setScreenTime(GameScreenTime screenTime) {this.screenTime = screenTime; }
	public void setScreenMode(GameModeScreen screenMode) {this.modeScreen = screenMode; }
	public void setLevelsScreeen(GameLevelsScreen screenLevels) {this.levelsScreen = screenLevels; }
	public void setGameMode(String gameMode) {this.gameMode = gameMode; }

}
