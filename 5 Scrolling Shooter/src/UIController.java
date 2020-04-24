package com.example.scrollingshooter;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

class UIController implements InputObserver {

    public UIController(GameEngineBroadcaster b){
        b.addObserver(this);
    }

    @Override
    public void handleInput(MotionEvent event, GameState gameState, ArrayList<Rect> buttons) {

        int i = event.getActionIndex();
        int x = (int) event.getX(i);
        int y = (int) event.getY(i);

        int eventType = event.getAction() & MotionEvent.ACTION_MASK;

        if (eventType == MotionEvent.ACTION_UP || eventType == MotionEvent.ACTION_POINTER_UP){
            if (buttons.get(HUD.PAUSE).contains(x, y)){
                //Player pressed the PAUSE button
                //Respond differently depending upon the game's state

                //If the game is not paused
                if (!gameState.getPaused()){
                    //pause the game
                    gameState.pause();
                } else if (gameState.getGameOver()){
                    //if game is over start a new game
                    gameState.startNewGame();
                } else if (gameState.getPaused() && !gameState.getGameOver()){
                    //Paused and not game over
                    gameState.resume();
                }
            }
        }
    }
}
