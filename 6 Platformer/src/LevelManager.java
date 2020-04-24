package com.example.platformermarc;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.example.platformermarc.GOSpec.BackgroundCitySpec;
import com.example.platformermarc.GOSpec.BackgroundMountainSpec;
import com.example.platformermarc.GOSpec.BackgroundUndergroundSpec;
import com.example.platformermarc.GOSpec.BrickTileSpec;
import com.example.platformermarc.GOSpec.CartTileSpec;
import com.example.platformermarc.GOSpec.CoalTileSpec;
import com.example.platformermarc.GOSpec.CollectibleObjectSpec;
import com.example.platformermarc.GOSpec.ConcreteTileSpec;
import com.example.platformermarc.GOSpec.DeadTreeTileSpec;
import com.example.platformermarc.GOSpec.FireTileSpec;
import com.example.platformermarc.GOSpec.GrassTileSpec;
import com.example.platformermarc.GOSpec.InvisibleDeathTenByTenSpec;
import com.example.platformermarc.GOSpec.LamppostTileSpec;
import com.example.platformermarc.GOSpec.MovablePlatformSpec;
import com.example.platformermarc.GOSpec.ObjectiveTileSpec;
import com.example.platformermarc.GOSpec.PlayerSpec;
import com.example.platformermarc.GOSpec.ScorchedTileSpec;
import com.example.platformermarc.GOSpec.SnowTileSpec;
import com.example.platformermarc.GOSpec.SnowyTreeTileSpec;
import com.example.platformermarc.GOSpec.StalactiteTileSpec;
import com.example.platformermarc.GOSpec.StalagmiteTileSpec;
import com.example.platformermarc.GOSpec.StonePileTileSpec;
import com.example.platformermarc.Levels.Level;
import com.example.platformermarc.Levels.LevelCity;
import com.example.platformermarc.Levels.LevelMountains;
import com.example.platformermarc.Levels.LevelUnderground;

import java.util.ArrayList;

final class LevelManager {

    static int PLAYER_INDEX = 0;
    private ArrayList<GameObject> objects;
    private Level currentLevel;
    private GameObjectFactory factory;

    LevelManager(Context context, GameEngine ge, int pixelsPerMetre){
        objects = new ArrayList<>();
        factory = new GameObjectFactory(context, ge, pixelsPerMetre);
    }

    void setCurrentLevel(String level){
        switch (level){
            case "underground":
                currentLevel = new LevelUnderground();
                break;
            case "city":
                currentLevel = new LevelCity();
                break;
            case "mountains":
                currentLevel = new LevelMountains();
                break;
        }
    }

    void buildGameObjects(GameState gs){
        // Backgrounds 1, 2, 3 (City, Underground, Mountain...)
        // p = Player
        // g = Grass tile
        // o = Objective
        // m = Movable platform
        // b = Brick tile
        // c = mine Cart
        // s = Stone pile
        // l = coaL
        // n = coNcrete
        // a = lAmpost
        // r = scoRched tile
        // w = snoW tile
        // t = stalacTite
        // i = stalagmIte
        // d = Dead tree
        // e = snowy trEe
        // x = Collectable
        // z = Fire
        // y = invisible death_invisible

        gs.resetCoins();
        objects.clear();
        ArrayList<String> levelToLoad = currentLevel.getTiles();

        for (int row = 0; row < levelToLoad.size(); row++){
            for (int column = 0; column < levelToLoad.get(row).length(); column++){

                PointF coords = new PointF(column, row);

                switch (levelToLoad.get(row).charAt(column)){
                    case '1':
                        objects.add(factory.create(new BackgroundCitySpec(), coords));
                        break;
                    case '2':
                        objects.add(factory.create(new BackgroundUndergroundSpec(), coords));
                        break;
                    case '3':
                        objects.add(factory.create(new BackgroundMountainSpec(), coords));
                        break;

                    case 'p':
                        objects.add(factory.create(new PlayerSpec(), coords));
                        //Remember the location of the player
                        PLAYER_INDEX = objects.size() - 1;
                        break;

                    case 'g':
                        objects.add(factory.create(new GrassTileSpec(), coords));
                        break;
                    case 'o':
                        objects.add(factory.create(new ObjectiveTileSpec(), coords));
                        break;
                    case 'm':
                        objects.add(factory.create(new MovablePlatformSpec(), coords));
                        break;
                    case 'b':
                        objects.add(factory.create(new BrickTileSpec(), coords));
                        break;
                    case 'c':
                        objects.add(factory.create(new CartTileSpec(), coords));
                        break;
                    case 's':
                        objects.add(factory.create(new StonePileTileSpec(), coords));
                        break;
                    case 'l':
                        objects.add(factory.create(new CoalTileSpec(), coords));
                        break;
                    case 'n':
                        objects.add(factory.create(new ConcreteTileSpec(), coords));
                        break;
                    case 'a':
                        objects.add(factory.create(new LamppostTileSpec(), coords));
                        break;
                    case 'r':
                        objects.add(factory.create(new ScorchedTileSpec(), coords));
                        break;
                    case 'w':
                        objects.add(factory.create(new SnowTileSpec(), coords));
                        break;
                    case 't':
                        objects.add(factory.create(new StalactiteTileSpec(), coords));
                        break;
                    case 'i':
                        objects.add(factory.create(new StalagmiteTileSpec(), coords));
                        break;
                    case 'd':
                        objects.add(factory.create(new DeadTreeTileSpec(), coords));
                        break;
                    case 'e':
                        objects.add(factory.create(new SnowyTreeTileSpec(), coords));
                        break;
                    case 'x':
                        objects.add(factory.create(new CollectibleObjectSpec(), coords));
                        gs.coinAddedToLevel();
                        break;
                    case 'z':
                        objects.add(factory.create(new FireTileSpec(), coords));
                        break;
                    case 'y':
                        objects.add(factory.create(new InvisibleDeathTenByTenSpec(), coords));
                        break;
                    case '.':
                        //Nothing to see here
                        break;
                    default:
                        Log.e("Unhandled item in level",
                                "row: " + row + " column:" + column);
                        break;

                }

            }

        }
    }

    ArrayList<GameObject> getGameObjects(){
        return objects;
    }

}
