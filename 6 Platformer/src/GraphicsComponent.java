package com.example.platformermarc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.platformermarc.GOSpec.GameObjectSpec;

interface GraphicsComponent {

    //Added int mPixelPerMetre to scale the bitmap to the camera
    void initialize(Context c, GameObjectSpec spec, PointF objectSize, int pixelsPerMetre);

    //Updated to take a reference to a camera
    void draw(Canvas canvas, Paint paint, Transform t, Camera cam);
}
