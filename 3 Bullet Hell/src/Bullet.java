package com.example.bullethell;

import android.graphics.RectF;

class Bullet {

    private RectF mRect;

    //How fast is the bullet travelling?
    private float mXVelocity;
    private float mYVelocity;

    //How big is the bullet?
    private float mWidth;
    private float mHeight;

    Bullet(int screenX){

        mWidth = screenX / 100;
        mHeight = screenX / 100;
        mRect = new RectF();
        mXVelocity = screenX / 5;
        mYVelocity = screenX / 5;

    }

    void update(long fps){
        mRect.left += mXVelocity / fps;
        mRect.top += mYVelocity / fps;
        mRect.right = mRect.left + mWidth;
        mRect.bottom = mRect.top + mHeight;
    }

    void reverseXVelocity(){
        mXVelocity = -mXVelocity;
    }

    void reverseYVelocity(){
        mYVelocity = -mYVelocity;
    }

    void spawn(int pX, int pY, int vX, int vY){

        mRect.left = pX;
        mRect.top = pY;
        mRect.right = pX + mWidth;
        mRect.bottom = pY + mHeight;

        mXVelocity = mXVelocity * vX;
        mYVelocity = mYVelocity * vY;
    }

    RectF getRect(){
        return mRect;
    }

    float getXVelocity(){
        return mXVelocity;
    }

    float getYVelocity(){
        return mYVelocity;
    }

    float getWidth(){
        return mWidth;
    }

    float getHeight(){
        return mHeight;
    }

    void correctBottom(int y){
        mRect.top = y - mHeight;
        mRect.bottom = y;
    }

    void correctTop(int y){
        mRect.top = y;
        mRect.bottom = y + mHeight;
    }

    void correctLeft(int x){
        mRect.left = x;
        mRect.right = x + mWidth;
    }

    void correctRight(int x){
        mRect.left = x - mWidth;
        mRect.right = x;
    }

}
