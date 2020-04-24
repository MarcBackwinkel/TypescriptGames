package com.example.pong;

import android.graphics.RectF;

class Ball {

    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    //default variable access modifier
    //default like protected - can be seen from all classes within same package
    Ball(int screenX){

        //Make the ball square and 1% of screen width
        mBallHeight = mBallWidth = screenX / 100;

        //Initialize the RetF with 0, 0, 0, 0
        //We do it here because we only want to do it once
        //We will initialize the detail at the start of each game
        mRect = new RectF();
    }

    void reset(int screenResolutionX, int screenResolutionY){
        mRect.left = screenResolutionX / 2;
        mRect.top = 0;
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mBallHeight;

        //How fast will the ball travel?
        mYVelocity = (screenResolutionY / 3);
        mXVelocity = screenResolutionX / 3;
    }

    void increaseVelocity(){
        //increase the speed by 10%
        mXVelocity *= 1.1f;
        mYVelocity *= 1.1f;
    }

    void reverseXVelocity(){
        mXVelocity = -mXVelocity;
    }

    void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }

    //Bounce the ball back based on whether it hits the left / right side of the bat
    void batBounce(RectF batPosition){

        //detect center of bat
        float batCenter = batPosition.left + (batPosition.width() / 2);

        //detect the center of the ball
        float ballCenter = mRect.left + (mBallWidth / 2);

        //Where on the bat did the ball hit?
        float relativeIntersect = (batCenter - ballCenter);

        //Pick a bounce direction
        if (relativeIntersect < 0){
            //Go right
            mXVelocity = Math.abs(mXVelocity);
        } else {
            //Go left
            mXVelocity = -Math.abs(mXVelocity);
        }

        reverseYVelocity();
    }

    //default variable access modifier
    //default like protected - can be seen from all classes within same package
    RectF getRect(){
        return mRect;
    }

    void update(long fps){
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;
    }


}
