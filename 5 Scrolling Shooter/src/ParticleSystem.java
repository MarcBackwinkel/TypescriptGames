package com.example.scrollingshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

class ParticleSystem {

    float mDuration;

    ArrayList<Particle> mParticles;
    Random random = new Random();

    //track whether or not the particle system is currently being shown (updating
    //and drawing)
    boolean mIsRunning = false;

    //Init will just be run once per game!
    void init(int numParticles){

        mParticles = new ArrayList<>();

        //Create the particles
        for (int i = 0; i < numParticles; i++){
            float angle = (random.nextInt(360));
            angle = angle * 3.14f / 180.f;
            float speed = random.nextInt(20) + 1;

            PointF direction = new PointF((float) Math.cos(angle) * speed,
                    (float) Math.sin(angle) * speed);
            mParticles.add(new Particle(direction));
        }
    }

    //emitParticles will be run whenever the effect is needed
    void emitParticles(PointF startPosition){
        mIsRunning = true;
        mDuration = 1f;

        for (Particle p : mParticles){
            p.setPosition(startPosition);
        }
    }

    void update(long fps){
        mDuration -= (1f/fps);

        for (Particle p : mParticles){
            p.update();
        }

        if (mDuration < 0){
            mIsRunning = false;
        }
    }

    void draw(Canvas canvas, Paint paint){

        for (Particle p : mParticles){
            paint.setARGB(255,
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));

            //Uncomment the next line to have plain white particles
            paint.setColor(Color.argb(255, 255, 255, 255));

            canvas.drawRect(p.getmPosition().x,
                    p.getmPosition().y,
                    p.getmPosition().x + 25,
                    p.getmPosition().y + 25,
                    paint);
        }

    }

}
