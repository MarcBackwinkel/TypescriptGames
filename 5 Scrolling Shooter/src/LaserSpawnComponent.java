package com.example.scrollingshooter;

import android.graphics.PointF;

class LaserSpawnComponent implements SpawnComponent{

    @Override
    public void spawn(Transform playerTransform, Transform t) {

        //playerTransform --> Transform-Object of the Player
        //t --> Transform-Object of the laser

        PointF startPosition = playerTransform.getFiringLocation(t.getSize().x);
        t.setLocation((int) startPosition.x, (int) startPosition.y);

        if (playerTransform.getFacingRight()){
            t.headRight();
        } else {
            t.headLeft();
        }

    }

}
