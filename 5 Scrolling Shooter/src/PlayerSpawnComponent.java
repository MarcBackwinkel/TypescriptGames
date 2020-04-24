package com.example.scrollingshooter;

class PlayerSpawnComponent implements SpawnComponent {


    @Override
    public void spawn(Transform playerTransform, Transform t) {

        //Spawn in the centre of the screen
        t.setLocation(t.getScreenSize().x/2, t.getScreenSize().y/2);

    }
}
