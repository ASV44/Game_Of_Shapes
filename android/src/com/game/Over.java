package com.game;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Over extends Activity {
    public final static String EXTRA_MESSAGE = "com.game.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        TextView textView=(TextView) findViewById(R.id.tv);
        textView.setTextSize(50);
        Intent intent=getIntent();
        int result=intent.getIntExtra(Over.EXTRA_MESSAGE,0);
        String score = "Time is Over !\n";
        score += String.valueOf(result);
        textView.setText(score);
    }

    public void Restart(View view){
        Intent intent=new Intent(this, AndroidLauncher.class);
        startActivity(intent);
    }
}
