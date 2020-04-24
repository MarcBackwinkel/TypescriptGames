package com.example.scrollingshooter;

import android.graphics.PointF;
import android.graphics.RectF;

class Transform {

    //These two members are for scrolling background
    private int mXClip;
    private boolean mReversedFirst = false;

    private RectF mCollider;
    //pixel position of top-left corner of a game object
    private PointF mLocation;
    private boolean mFacingRight = true;
    private boolean mHeadingUp = false;
    private boolean mHeadingDown = false;
    private boolean mHeadingLeft = false;
    private boolean mHeadingRight = false;
    private float mSpeed;
    private float mObjectHeight;
    private float mObjectWidth;

    //mScreenSize is static so it is a variable of the class, not the instance
    //and means there is only one copy of mScreenSize shared across all instances
    //of Transform
    private static PointF mScreenSize;

    Transform(float speed, float objectWidth, float objectHeight,
              PointF startingLocation, PointF screenSize){

        mCollider = new RectF();
        mSpeed = speed;
        mObjectHeight = objectHeight;
        mObjectWidth = objectWidth;
        mLocation = startingLocation;
        mScreenSize = screenSize;

    }

    //Here are some helper methods that the background will use
    boolean getReversedFirst(){
        return mReversedFirst;
    }

    void flipReversedFirst(){
        mReversedFirst = !mReversedFirst;
    }

    int getXClip(){
        return mXClip;
    }

    void setXClip(int newXClip){
        mXClip = newXClip;
    }


    PointF getScreenSize(){
        return mScreenSize;
    }

    void headUp(){
        mHeadingUp = true;
        mHeadingDown = false;
    }

    void headDown(){
        mHeadingUp = false;
        mHeadingDown = true;
    }

    void headRight(){
        mHeadingRight = true;
        mHeadingLeft = false;
        mFacingRight = true;
    }

    void headLeft(){
        mHeadingRight = false;
        mHeadingLeft = true;
        mFacingRight = false;
    }

    boolean headingUp(){
        return mHeadingUp;
    }

    boolean headingDown(){
        return mHeadingDown;
    }

    boolean headingRight(){
        return mHeadingRight;
    }

    boolean headingLeft(){
        return mHeadingLeft;
    }

    void updateCollider(){

        //Pull the borders in a bit (10%)
        mCollider.top = mLocation.y + (mObjectHeight / 10);
        mCollider.left = mLocation.x + (mObjectWidth / 10);
        mCollider.bottom = (mCollider.top + mObjectHeight) - mObjectHeight / 10;
        mCollider.right = (mCollider.left + mObjectWidth) - mObjectWidth / 10;
    }

    float getObjectHeight(){
        return mObjectHeight;
    }

    void stopVertical(){
        mHeadingDown = false;
        mHeadingUp = false;
    }

    float getSpeed(){
        return mSpeed;
    }

    void setLocation(float horizontal, float vertical){
        mLocation = new PointF(horizontal, vertical);
        updateCollider();
    }

    PointF getLocation(){
        return mLocation;
    }

    PointF getSize(){
        return new PointF((int)mObjectWidth, (int) mObjectHeight);
    }

    void flip(){
        mFacingRight = !mFacingRight;
    }

    boolean getFacingRight(){
        return mFacingRight;
    }

    RectF getCollider(){
        return mCollider;
    }

    PointF getFiringLocation(float laserLength){
        PointF mFiringLocation = new PointF();

        if (mFacingRight){
            mFiringLocation.x = mLocation.x + (mObjectWidth / 8f);
        } else {
            mFiringLocation.x = mLocation.x + (mObjectWidth / 8f) - laserLength;
        }

        //Move the height down a bit of ship height from origin
        mFiringLocation.y = mLocation.y + (mObjectHeight / 1.28f);

        return mFiringLocation;
    }

}
