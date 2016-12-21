package com.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.shapes.MyGame;

public class AndroidLauncher extends AndroidApplication implements MyGame.CallBack {

		public final static String EXTRA_MESSAGE = "com.game.MESSAGE";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyGame myGame = new MyGame();
		myGame.setCallBack(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(myGame, config);
	}

	@Override
	public void gameOver(int score) {
		Intent intent = new Intent(this, Over.class);
		intent.putExtra(EXTRA_MESSAGE, score);
		startActivity(intent);
		//Toast.makeText(this, "Time is Over", Toast.LENGTH_SHORT).show();
		Log.v("Time","out");
	}
}
