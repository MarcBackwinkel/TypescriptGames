package com.example.bullethell;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class BulletHellActivity extends Activity {

    private BulletHellGame mBHGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mBHGame = new BulletHellGame(this, size.x, size.y);

        setContentView(mBHGame);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mBHGame.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mBHGame.pause();
    }
}
