package com.example.subhunter;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;  //???
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;


//public class MainActivity extends AppCompatActivity {
public class MainActivity extends Activity {

    //These variables can be seen
    //throughout the whole MainActivity Class
    int numberHorizontalPixels;
    int numberVerticalPixels;
    int blockSize;
    int gridWidth = 40;
    int gridHeight;
    float horizontalTouched = -100;
    float verticalTouched = -100;
    int subHorizontalPosition;
    int subVerticalPosition;
    boolean hit = false;
    int shotsTaken;
    int distanceFromSub;
    boolean debugging = false;
    ArrayList<Point> coordsOfShotsTaken = new ArrayList<Point>();

    //Here are all the objects (instances)
    //of classes that we need to do some drawing
    ImageView gameView;
    Bitmap blankBitmap;
    Canvas canvas;
    Paint paint;

    /*
    Android runs this code just before
    the player sees the app.
    This makes it a good place to add
    the code for the one-time setup phase.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Initialize our size based variables
        //based on the screen resolution
        numberHorizontalPixels = size.x;
        numberVerticalPixels = size.y;
        blockSize = numberHorizontalPixels / gridWidth;
        gridHeight = numberVerticalPixels / blockSize;

        //Initialize all the objects ready for drawing
        blankBitmap = Bitmap.createBitmap(numberHorizontalPixels, numberVerticalPixels,
                Bitmap.Config.ARGB_8888);

        canvas = new Canvas(blankBitmap);
        gameView = new ImageView(this);
        paint = new Paint();

        //Tell Android to set our drawing
        //as the view for this app
        setContentView(gameView);
        //setContentView(R.layout.activity_main);

        Log.d("Debugging", "In onCreate");
        newGame();
        draw();
    }

    /*
    This code will execute when a new
    game needs to be started. It will
    happen when the app is first started
    and after the player wins a game.
     */
    void newGame(){
        Random random = new Random();
        subHorizontalPosition = random.nextInt(gridWidth);
        subVerticalPosition = random.nextInt(gridHeight);
        shotsTaken = 0;
        coordsOfShotsTaken.clear();

        Log.d("Debugging", "In newGame");
    }

    /*
    Here we will do all the drawing.
    The grid lines, the HUD and
    the touch indicator.
     */
    void draw(){
        gameView.setImageBitmap(blankBitmap);

        //Wipe the screen with a white color
        canvas.drawColor(Color.argb(255, 255, 255, 255));

        //Change the paint color to black
        paint.setColor(Color.argb(255,0,0,0));

        //Draw the vertical lines of the grid
        for (int i = 1; i < gridWidth; i++) {
            canvas.drawLine(blockSize * i, 0, blockSize * i,
                    numberVerticalPixels - 1, paint);
        }

        //Draw the horizontal lines of the grid
        for (int i = 1; i < gridHeight; i++) {
            canvas.drawLine(0, blockSize * i, numberHorizontalPixels - 1,
                    blockSize * i, paint);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255,0,0,0));
        //Draw the shot points
        for (Point shot: coordsOfShotsTaken){
            canvas.drawRect(new Rect(shot.x * blockSize, shot.y * blockSize,
                    shot.x * blockSize + blockSize,
                    shot.y * blockSize + blockSize), paint);
            Log.d("ShotArrayList", "Shot.x = " + shot.x + ", Shot.y = " + shot.y);
        }

        //Re-size the text appropriate for the
        //score and distance text
        paint.setTextSize(blockSize * 2);
        paint.setColor(Color.argb(255, 0, 0, 255));
        canvas.drawText("Shots taken: " + shotsTaken + "  Distance: " + distanceFromSub,
                blockSize, blockSize * 1.75f, paint);


        Log.d("Debugging", "In draw");
        if (debugging) {
            printDebuggingText();
        }
    }

    /*
    This part of the code will
    handle detecting that the player
    has tapped the screen.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        Log.d("Debugging", "In onTouchEvent");

        //Has the player removed their finger from the screen?

        //motionEvent.getAction() gets the Info from the touchEvent
        //ACTION_MASK is a mask to just filter the Action Bits of getAction()
        //if the filtered bits of getAction() == ACTION_UP(), then the finger is released!!!
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){

            //Process the player's shot by passing the
            //coordinates of the player's finger to takeShot
            takeShot(motionEvent.getX(), motionEvent.getY());
        }

        return true;
    }

    /*
    The code here will execute when
    the player taps the screen. It will
    calculate the distance from the sub
    and decide a hit or a miss.
     */
    void takeShot(float touchX, float touchY){
        Log.d("Debugging", "In takeShot");

        //Add one to the shots taken variable
        shotsTaken++;

        //Convert the float screen coordinates
        //into int grid coordinates
        horizontalTouched = (int) touchX / blockSize;
        verticalTouched = (int) touchY / blockSize;

        Point shot = new Point((int) horizontalTouched, (int) verticalTouched);
        coordsOfShotsTaken.add(shot);

        //Did the shot hit the sub?
        hit = horizontalTouched == subHorizontalPosition &&
                verticalTouched == subVerticalPosition;

        //How far away horizontally and vertically
        //was the shot from the sub?
        int horizontalGap = (int) horizontalTouched - subHorizontalPosition;
        int verticalGap = (int) verticalTouched - subVerticalPosition;

        //Use Pythagora's theorem to get the
        //distance travelled in a straight line
        distanceFromSub = (int) Math.sqrt(
                horizontalGap * horizontalGap + verticalGap * verticalGap
        );

        //If there is a hit call Boom
        if (hit)
            boom();
        else
            //Otherwise call draw() as usual
            draw();
    }

    //This code says "BOOM!"
    void boom(){
        gameView.setImageBitmap(blankBitmap);

        //Wipe the screen with a red color
        canvas.drawColor(Color.argb(255,255,0,0));

        //Draw some huge white text
        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(blockSize * 10);

        canvas.drawText("BOOM", blockSize * 4, blockSize * 14, paint);

        //Draw some text to prompt restarting
        paint.setTextSize(blockSize * 2);
        canvas.drawText("Take a shot to start again", blockSize * 8,
                blockSize * 18, paint);

        //Start a new game
        newGame();
    }

    //This code prints the debugging screen
    void printDebuggingText(){

        paint.setTextSize(blockSize);
        canvas.drawText("numberHorizontalPixels = " + numberHorizontalPixels, 50,
                blockSize * 3, paint);
        canvas.drawText("numberVerticalPixels = " + numberVerticalPixels, 50,
                blockSize * 4, paint);
        canvas.drawText("blockSize = " + blockSize, 50,
                blockSize * 5, paint);
        canvas.drawText("gridWidth = " + gridWidth, 50,
                blockSize * 6, paint);
        canvas.drawText("gridHeight = " + gridHeight, 50,
                blockSize * 7, paint);
        canvas.drawText("horizontalTouched = " + horizontalTouched, 50,
                blockSize * 8, paint);
        canvas.drawText("verticalToched = " + verticalTouched, 50,
                blockSize * 9, paint);
        canvas.drawText("subHorizontalPosition = " + subHorizontalPosition, 50,
                blockSize * 10, paint);
        canvas.drawText("subVerticalPosition = " + subVerticalPosition, 50,
                blockSize * 11, paint);
        canvas.drawText("hit = " + hit, 50,
                blockSize * 12, paint);
        canvas.drawText("shotsTaken = " + shotsTaken, 50,
                blockSize * 13, paint);
        canvas.drawText("debugging = " + debugging, 50,
                blockSize * 14, paint);
        canvas.drawText("distanceFromSub = " + distanceFromSub, 50,
                blockSize * 15, paint);

        /*
        //Logging Debug Information to Logcat
        Log.d("numberHorizontalPixels", "" + numberHorizontalPixels);
        Log.d("numberVerticalPixels", "" + numberVerticalPixels);
        Log.d("blockSize", "" + blockSize);
        Log.d("gridWidth", "" + gridWidth);
        Log.d("gridHeight", "" + gridHeight);
        Log.d("horizontalTouched", "" + horizontalTouched);
        Log.d("verticalTouched", "" + verticalTouched);
        Log.d("subHorizontalPosition", "" + subHorizontalPosition);
        Log.d("subVerticalPosition", "" + subVerticalPosition);
        Log.d("hit", "" + hit);
        Log.d("shotsTaken", "" + shotsTaken);
        Log.d("debugging", "" + debugging);
        Log.d("distanceFromSub", "" + distanceFromSub);
        */
    }

}