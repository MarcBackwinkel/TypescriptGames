package com.example.scrollingshooter;

import java.util.Random;

class AlienVerticalSpawnComponent implements SpawnComponent {

    @Override
    public void spawn(Transform playerTransform, Transform t) {

        //Spawn just off screen randomly but within the screen width
        Random random = new Random();
        float xPosition = random.nextInt((int) t.getScreenSize().x);

        //Set the height to vertically just above the visible game
        float spawnHeight = random.nextInt(300) - t.getObjectHeight();

        //Spawn the ship
        t.setLocation(xPosition, spawnHeight);
        //Always going down
        t.headDown();

    }

}
