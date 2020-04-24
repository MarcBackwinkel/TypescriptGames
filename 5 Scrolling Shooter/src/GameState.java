package com.example.scrollingshooter;

import android.content.Context;
import android.content.SharedPreferences;

class GameState {

    //volatile: no local copy of this variable in each Thread
    //so a volatile variable is the same in each Thread

    //static: variables of the class and not of a specific instance
    //volatile: we can safely access the variables from inside & outside the thread
    private static volatile boolean mThreadRunning = false;
    private static volatile boolean mPaused = true;
    private static volatile boolean mGameOver = true;
    private static volatile boolean mDrawing = false;

    //This object will have access to the deSpawnReSpawn method in GameEngine
    private GameStarter gameStarter;

    private int mScore;
    private int mHighScore;
    private int mNumShips;

    //This is how we make all the high scores persist
    private SharedPreferences.Editor mEditor;

    GameState(GameStarter gs, Context context){

        //This initializes the gameStarter reference
        gameStarter = gs;

        //Get the current high score
        SharedPreferences prefs;
        //access to file "HiScore", which is private to this app
        //if the file does not exist it will be created
        //prefs= readOnly File for the hishScore
        prefs = context.getSharedPreferences("HiScore", Context.MODE_PRIVATE);

        //Initialize the mEditor ready
        //mEditor --> write into file
        mEditor = prefs.edit();

        //Load high score from a entry in the file "HiScore" with the label "hi_score"
        //If label is not found, the 0 is returned
        mHighScore = prefs.getInt("hi_score", 0);
    }

    void startNewGame(){
        mScore = 0;
        mNumShips = 3;

        //Don't want to be drawing objects while deSpawnReSpawn is clearing and
        //spawning them again
        stopDrawing();
        gameStarter.deSpawnReSpawn();
        resume();

        //Now we can draw again
        startDrawing();
    }

    void loseLife(SoundEngine se){
        mNumShips--;
        se.playPlayerExplode();
        if (mNumShips == 0){
            pause();
            endGame();
        }
    }

    private void endGame(){
        mGameOver = true;
        mPaused = true;
        if (mScore > mHighScore){
            mHighScore = mScore;
            //Save high score
            mEditor.putInt("hi_score", mHighScore);
            mEditor.commit();
        }
    }

    int getNumShips(){
        return mNumShips;
    }

    void increaseScore(){
        mScore++;
    }

    int getScore(){
        return mScore;
    }

    int getHighScore(){
        return mHighScore;
    }

    void pause(){
        mPaused = true;
    }

    void resume(){
        mGameOver = false;
        mPaused = false;
    }

    void stopEverything(){
        mPaused = true;
        mGameOver = true;
        mThreadRunning = false;
    }

    boolean getThreadRunning(){
        return mThreadRunning;
    }

    void startThread(){
        mThreadRunning = true;
    }

    private void stopDrawing(){
        mDrawing = false;
    }

    private void startDrawing(){
        mDrawing = true;
    }

    boolean getDrawing(){
        return mDrawing;
    }

    boolean getPaused(){
        return mPaused;
    }

    boolean getGameOver(){
        return mGameOver;
    }
}
