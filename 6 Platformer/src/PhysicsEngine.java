package com.example.platformermarc;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

class PhysicsEngine {

    void update(long fps, ArrayList<GameObject> objects, GameState gs){

        for (GameObject object : objects){
            object.update(fps,
                    objects.get(LevelManager.PLAYER_INDEX).getTransform());
        }

        detectCollisions(gs, objects);

    }

    private void detectCollisions(GameState gs,
                                  ArrayList<GameObject> objects){

        boolean collisionOccurred = false;

        //Something collides with some part of the player most frames so, let's make
        //some handy references. Get a reference to the player's position as we will
        //probably need to update it
        Transform playersTransform = objects.get(LevelManager.PLAYER_INDEX).getTransform();
        PlayerTransform playersPlayerTransform = (PlayerTransform) playersTransform;

        //Get the player's extra colliders
        ArrayList<RectF> playerColliders = playersPlayerTransform.getColliders();
        PointF playersLocation = playersTransform.getLocation();

        for (GameObject go : objects){

            //Just need to check player collisions with everything else
            if (go.checkActive()){

                //Object is active so check for collision with player - anywhere at all
                if (RectF.intersects(go.getTransform().getCollider(),
                        playersTransform.getCollider())){

                    //A collision of some kind has occurred so let's dig a little deeper
                    collisionOccurred = true;
                    //get a reference to the current (being tested) object's transform & location
                    Transform testedTransform = go.getTransform();
                    PointF testedLocation = testedTransform.getLocation();

                    //Don't test the player against itself
                    if (objects.indexOf(go) != LevelManager.PLAYER_INDEX){

                        //Where was the player hit?
                        for (int i = 0; i < playerColliders.size(); i++){

                            if (RectF.intersects(testedTransform.getCollider(),
                                    playerColliders.get(i))){

                                //React to the collision based on body part and object type
                                switch (go.getTag() + " with " + "" + i){

                                    //FEET - 0
                                    //Test feet first to avoid the player sinking in to a tile
                                    //and unnecessarily triggering right and left as well
                                    case "Movable Platform with 0":
                                        playersPlayerTransform.grounded();
                                        playersLocation.y =
                                                testedTransform.getLocation().y -
                                                        playersTransform.getSize().y;
                                        break;

                                    case "Death with 0":
                                        gs.death();
                                        break;

                                    case "Inert Tile with 0":
                                        playersPlayerTransform.grounded();
                                        playersLocation.y =
                                                testedTransform.getLocation().y -
                                                        playersTransform.getSize().y;
                                        break;

                                        //HEAD - 1
                                    case "Inert Tile with 1":
                                        //just update the player to a suitable height
                                        //but allow any jumps to continue
                                        playersLocation.y = testedLocation.y +
                                                testedTransform.getSize().y;
                                        playersPlayerTransform.triggerBumpedHead();
                                        break;

                                        //RIGHT - 2
                                    case "Collectible with 2":
                                        SoundEngine.playCoinPickup();
                                        //Switch off coin
                                        go.setInactive();
                                        //Tell the game state a coin has been found
                                        gs.coinCollected();
                                        break;

                                    case "Inert Tile with 2":
                                        //Stop the player moving right
                                        //playersTransform.stopMovingRight();
                                        //Move the player to the left of the tile
                                        playersLocation.x = testedTransform.getLocation().x
                                                - playersTransform.getSize().x;
                                        break;

                                        //LEFT - 3
                                    case "Collectible with 3":
                                        SoundEngine.playCoinPickup();
                                        //Switch off coin
                                        go.setInactive();
                                        //Tell the game state a coin has been found
                                        gs.coinCollected();
                                        break;

                                    case "Inert Tile with 3":
                                        //Stop the player moving left
                                        //playersTransform.stopMovingLeft();
                                        //Move the player to the right of the tile
                                        playersLocation.x = testedLocation.x
                                                + testedTransform.getSize().x;
                                        break;

                                        //Handle the player's feet reaching the object tile
                                    case "Objective Tile with 0":
                                        SoundEngine.playReachObjective();
                                        gs.objectiveReached();
                                        break;

                                    default:

                                        break;
                                }

                            }

                        }

                    }

                }

            }

        }

        if (!collisionOccurred){
            //No connection with main player, collider so not grounded
            playersPlayerTransform.notGrounded();
        }
    }

}
