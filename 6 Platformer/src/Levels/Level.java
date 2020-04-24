package com.example.platformermarc.Levels;

import java.util.ArrayList;

public abstract class Level {

    //if you want to build a new level then extend this class
    ArrayList<String> tiles;

    public ArrayList<String> getTiles(){
        return tiles;
    }

}
