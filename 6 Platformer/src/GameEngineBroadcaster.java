package com.example.platformermarc;

interface GameEngineBroadcaster {

    //This allows the Player and the UI Controller components to add themselves
    //as listeners of the GameEngine class
    void addObserver(InputObserver o);

}
