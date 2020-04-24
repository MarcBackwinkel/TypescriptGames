package com.example.scrollingshooter;

class BackgroundSpawnComponent implements SpawnComponent {

    @Override
    public void spawn(Transform playerTransform, Transform t) {

        //Place the background in the top left corner
        t.setLocation(0f, 0f);

    }
}
