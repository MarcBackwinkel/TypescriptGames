package com.example.bullethell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

class Bob {

    RectF mRect;
    float mBobHeight;
    float mBobWidth;
    boolean mTeleporting = false;

    Bitmap mBitmap;

    public Bob(Context context, float screenX, float screenY){
        mBobHeight = screenY / 10;
        mBobWidth = mBobHeight / 2;

        mRect = new RectF(screenX / 2, screenY / 2,
                screenX / 2 + mBobWidth,
                screenY / 2 + mBobHeight);

        //Prepare the bitmap
        //Load Bob from his .png file
        //Bob practices responsible encapsulation looking after his own resources
        //R.drawable.bob --> reference to bob.png in drawable folder
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bob);

    }

    boolean teleport(float newX, float newY){

        //Did Bob manage to teleport?
        boolean success = false;

        //Move Bob to the new position if not already teleporting
        if (!mTeleporting){

            //Make him roughly central to the touch
            mRect.left = newX - mBobWidth / 2;
            mRect.top = newY - mBobHeight / 2;
            mRect.right = mRect.left + mBobWidth;
            mRect.bottom = mRect.top + mBobHeight;

            mTeleporting = true;

            //Notify BulletHellGame that teleport attempt was successful
            success = true;
        }
        return success;
    }

    void setTeleportAvailable(){
        mTeleporting = false;
    }

    RectF getRect(){
        return mRect;
    }

    Bitmap getBitmap(){
        return mBitmap;
    }

}
