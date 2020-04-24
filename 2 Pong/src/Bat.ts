package com.example.pong;

import android.graphics.RectF;

class Bat {

    private RectF mRect;
    private float mLength;
    private float mXCoord;
    private float mBatSpeed;
    private int mScreenX;

    final int STOPPED = 0;
    final int LEFT = 1;
    final int RIGHT = 2;

    //Keeps track of if and how the bat is moving
    //Starting with STOPPED
    private int mBatMoving = STOPPED;

    Bat(int screenX, int screenY){
        mScreenX = screenX;

        //Configure the size of the bat based on the screen resolution
        mLength = mScreenX / 8;
        float height = screenY / 40;

        //Configure the starting location of the bat
        //Roughly the middle horizontally
        mXCoord = mScreenX / 2;
        float mYCoord = screenY - height;

        //Init mRect based on the size and position
        mRect = new RectF(mXCoord, mYCoord, mXCoord + mLength, mYCoord + height);

        //Configure the speed of the bat
        //This code means the bat can cover the width of the screen in 1 second
        mBatSpeed = mScreenX;
    }

    //Update the movement state passed in by the onTouchEvent method
    void setMovementState(int state){
        mBatMoving = state;
    }

    void update(long fps){
        if (mBatMoving == LEFT){
            mXCoord = mXCoord - mBatSpeed / fps;
        }
        if (mBatMoving == RIGHT){
            mXCoord = mXCoord + mBatSpeed / fps;
        }

        //Stop the bat moving off the screen
        if (mXCoord < 0){
            mXCoord = 0;
        } else if (mXCoord + mLength > mScreenX){
            mXCoord = mScreenX - mLength;
        }

        //Update position of bat
        mRect.left = mXCoord;
        mRect.right = mXCoord + mLength;
    }

    RectF getRect(){
        return mRect;
    }

}
