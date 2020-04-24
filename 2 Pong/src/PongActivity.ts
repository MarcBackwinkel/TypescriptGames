package com.example.pong;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class PongActivity extends Activity {

    //m...-name of a variable declares a member variable --> scope of a class
    //a member variable is declared outside of any method
    private PongGame mPongGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mPongGame = new PongGame(this, size.x, size.y);

        setContentView(mPongGame);
        //setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPongGame.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mPongGame.pause();
    }
}
