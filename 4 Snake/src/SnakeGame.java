package com.example.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

class SnakeGame extends SurfaceView implements Runnable{

    //Objects for the game loop/thread
    private Thread mThread = null;

    //Control pausing between updates
    private long mNextFrameTime;

    //Is the game currently playing or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    //for playing sound effects
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;

    //The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;

    //How many points does the player have?
    private int mScore;

    //Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    private Snake mSnake;
    private Apple mApple;

    public SnakeGame(Context context, Point size){
        super(context);

        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
        }

        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            //Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);
        } catch (IOException e){
            //Error
        }

        //Init the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        //Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
    }

    public void newGame(){

        //reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        //get the apple ready for dinner
        mApple.spawn();

        //reset the mScore
        mScore = 0;

        //setup mNextFrameTime so an update can be triggered
        mNextFrameTime = System.currentTimeMillis();

    }

    @Override
    public void run() {

        while (mPlaying){
            if (!mPaused){

                //Update 10 times a second
                if (updateRequired()){
                    update();
                }

            }
            draw();
        }

    }


    //Check to see if it is time for an update
    public boolean updateRequired(){

        //Run at 10 frames per second
        final long TARGET_FPS = 10;
        //There are 1000 milliseconds in one second
        final long MILLIS_PER_SECOND = 1000;

        //Are we due to update the frame?
        if (mNextFrameTime <= System.currentTimeMillis()){
            //Tenth of a second has passed

            //Setup when the next update will be triggered
            mNextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / TARGET_FPS;

            return true;
        }

        return false;

    }

    public void update(){

        //Move the snake
        mSnake.move();

        //Did the head of the snake eat the apple?
        if (mSnake.checkDinner(mApple.getLocation())){
            mApple.spawn();
            mScore++;
            mSP.play(mEat_ID, 1, 1, 0, 0, 1);
        }

        //Did the snake die?
        if (mSnake.detectDeath()){
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
            mPaused = true;
        }

    }

    public void draw(){

        //Get a lock on the canvas
        if (mSurfaceHolder.getSurface().isValid()){
            mCanvas = mSurfaceHolder.lockCanvas();

            //Fill the screen with a color
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));

            //Set the size and color of the mPaint for the text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(120);

            //Draw the score
            mCanvas.drawText("" + mScore, 20 , 120, mPaint);

            //Draw the apple and the snake
            mApple.draw(mCanvas, mPaint);
            mSnake.draw(mCanvas, mPaint);

            //Draw some text while pause
            if (mPaused){

                //Set the size and color of mPaint for the Text
                mPaint.setColor(Color.argb(255, 255, 255, 255));
                mPaint.setTextSize(150);

                //Draw the message
                //We will give this an international upgrade soon
                //mCanvas.drawText("Tap to Play!", 200, 700, mPaint);
                mCanvas.drawText(getResources().getString(R.string.tap_to_play),
                        200, 500, mPaint);
            }

            //Unlock the Canvas to show graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                if (mPaused){
                    mPaused = false;
                    newGame();

                    //Don't want to process snake direction of this tap
                    return true;
                }

                //Let the snake class handle the input
                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;
        }
        return true;
    }

    public void pause(){
        mPlaying = false;
        try{
            mThread.join();
        } catch (InterruptedException e){
            //Error
        }
    }

    public void resume(){
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

}
