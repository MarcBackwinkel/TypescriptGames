package com.example.scrollingshooter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

class GameEngine extends SurfaceView implements Runnable, GameStarter,
        GameEngineBroadcaster, PlayerLaserSpawner, AlienLaserSpawner {

    private Thread mThread = null;
    private long mFPS;

    private ArrayList<InputObserver> inputObservers = new ArrayList<>();

    UIController mUIController;

    private GameState mGameState;
    private SoundEngine mSoundEngine;
    HUD mHUD;
    Renderer mRenderer;
    ParticleSystem mParticleSystem;
    PhysicsEngine mPhysicsEngine;
    Level mLevel;

    public GameEngine(Context context, Point size){
        super(context);

        mUIController = new UIController(this);
        mGameState = new GameState(this, context);
        mSoundEngine = new SoundEngine(context);
        mHUD = new HUD(size);
        mRenderer = new Renderer(this);
        mPhysicsEngine = new PhysicsEngine();

        mParticleSystem = new ParticleSystem();
        //Even just 10 particles look good
        //But why have less when you can have more...
        mParticleSystem.init(1000);

        mLevel = new Level(context, new PointF(size.x, size.y), this);
    }

    @Override
    public void run() {

        while (mGameState.getThreadRunning()) {
            long frameStartTime = System.currentTimeMillis();

            ArrayList<GameObject> objects = mLevel.getGameObjects();

            if (!mGameState.getPaused()) {
                //Update all the game objects here in a new way

                //This call to update will evolve with the project
                //update returns a boolean - has the player died?
                if (mPhysicsEngine.update(mFPS, objects, mGameState,
                        mSoundEngine, mParticleSystem)) {
                    //Player hit
                    deSpawnReSpawn();
                }
            }

            //Draw all the game objects here in a new way
            mRenderer.draw(objects, mGameState, mHUD, mParticleSystem);

            //Measure the frames per second in the usual way
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if (timeThisFrame >= 1) {
                final int MILLIS_IN_SECOND = 1000;
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        //Handle the player's input here - but in a new way
        for (InputObserver o : inputObservers){
            o.handleInput(motionEvent, mGameState, mHUD.getControls());
        }

        return true;
    }

    public void stopThread(){
        //New code here soon
        mGameState.stopEverything();

        try{
            mThread.join();
        } catch (InterruptedException e){
            Log.e("Exception","stopThread()" + e.getMessage());
        }
    }

    public void startThread(){
        //New code here soon
        mGameState.startThread();

        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void deSpawnReSpawn() {
        //Eventually this will despawn and then respawn all the game objects
        ArrayList<GameObject> objects = mLevel.getGameObjects();

        for (GameObject o : objects){
            o.setInactive();
        }

        objects.get(Level.PLAYER_INDEX)
                .spawn(objects.get(Level.PLAYER_INDEX).getTransform());
        objects.get(Level.BACKGROUND_INDEX)
                .spawn(objects.get(Level.BACKGROUND_INDEX).getTransform());

        for (int i = Level.FIRST_ALIEN; i != Level.LAST_ALIEN; i++){
            objects.get(i).spawn(objects.get(Level.PLAYER_INDEX).getTransform());
        }
    }

    @Override
    public void addObserver(InputObserver o) {
        inputObservers.add(o);
    }

    @Override
    public boolean spawnPlayerLaser(Transform transform) {
        ArrayList<GameObject> objects = mLevel.getGameObjects();

        //try to spawn a laser, if successful ...
        if (objects.get(Level.mNextPlayerLaser).spawn(transform)){
            Level.mNextPlayerLaser++;
            mSoundEngine.playShoot();
            if (Level.mNextPlayerLaser == Level.LAST_PLAYER_LASER + 1){
                //Just used the last laser
                Level.mNextPlayerLaser = Level.FIRST_PLAYER_LASER;
            }
        }

        return true;
    }

    @Override
    public void spawnAlienLaser(Transform transform) {
        ArrayList<GameObject> objects = mLevel.getGameObjects();

        //Shoot laser IF AVAILABLE
        //Pass in the transform to the ship that requested the shot to be fired
        if (objects.get(Level.mNextAlienLaser).spawn(transform)){
            Level.mNextAlienLaser++;
            mSoundEngine.playShoot();
            if (Level.mNextAlienLaser == Level.LAST_ALIEN_LASER + 1) {
                //Just used the last laser
                Level.mNextAlienLaser = Level.LAST_ALIEN_LASER;
            }
        }
    }

}
