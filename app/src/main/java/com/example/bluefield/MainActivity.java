package com.example.bluefield;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import com.example.bluefield.simulator.Simulator;

import org.apache.commons.math3.analysis.function.Exp;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements ViewStub.OnTouchListener {

    // listener declarations
    private GestureDetector gestureDetectorSpeed;
    private GestureDetector gestureDetectorQuantity;
    private LinearLayout speedLayout;
    private LinearLayout quantityLayout;

    // simulation parameters
    private TextView speedText;
    private TextView quantityText;
    private String message;

    private DecimalFormat df = new DecimalFormat("#.##");

    /*------------------------------------------ ON CREATE -------------------------------------------*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set application in landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // link layout to variables
        setContentView(R.layout.activity_main);
        speedLayout = findViewById(R.id.linLSpeed);
        quantityLayout = findViewById(R.id.linLQuantity);
        speedText = findViewById(R.id.tvSpeed);
        quantityText = findViewById(R.id.tvQuantity);
        MyDrawable myDrawing = new MyDrawable();
        ImageView image = findViewById(R.id.imageView);
        image.setImageDrawable(myDrawing);
        // start simulator exactly once
        if(!Simulator.getInstance().isAlive())
        {
            Simulator.getInstance().start();
        }

        // start animation, no need to check if already running (done in function)
        myDrawing.start();

        // default values shown
        message = "Speed: " + df.format(1/((float)Simulator.getInstance().getTimeMs()/1000));
        speedText.setText(message);
        message = "Quantity: " + Simulator.getInstance().getNbrOfLeukocytes();
        quantityText.setText(message);

        // transmit gestures to simulator
        gestureDetectorSpeed = new GestureDetector(this, new OnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                speedAction(direction);
                return true;
            }
        });
        gestureDetectorQuantity = new GestureDetector(this, new OnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                densityAction(direction);
                return true;
            }
        });

        // link listeners to layouts
        speedLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorSpeed.onTouchEvent(event);

                return true;
            }
        });
        quantityLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorQuantity.onTouchEvent(event);

                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * Take as input the user swipe direction and tell the simulator which parameter change.
     *
     * @param direction transport the user swipe's direction
     */
    public void speedAction(OnSwipeListener.Direction direction) {
        switch (direction) {
            case left:
                Log.d(TAG, "Speed swipe: left");
                Simulator.getInstance().changeTimeMs(+1);
                // display speed in [step/second] with max two decimals
                message = "Speed: " + df.format(1/((float)Simulator.getInstance().getTimeMs()/1000));
                speedText.setText(message);
                break;

            case right:
                Log.d(TAG, "Speed swipe: right");
                Simulator.getInstance().changeTimeMs(-1);
                // display speed in [step/second] with max two decimals
                message = "Speed: " + df.format(1/((float)Simulator.getInstance().getTimeMs()/1000));
                speedText.setText(message);
                break;

            default:
                Log.d(TAG, "Speed swipe: undefined");
        }
    }

    /**
     * Take as input the user swipe direction and tell the simulator which parameter change.
     *
     * @param direction transport the user swipe's direction
     */
    public void densityAction(OnSwipeListener.Direction direction) {
        switch (direction) {
            case left:
                Log.d(TAG, "Density swipe: left");
                // increase number by more than one to get a visible effect
                Simulator.getInstance().removeLeukocytes(5);
                message = "Density: " + Simulator.getInstance().getNbrOfLeukocytes();
                quantityText.setText(message);
                break;

            case right:
                Log.d(TAG, "Density swipe: right");
                // decrease number by more than one to get a visible effect
                Simulator.getInstance().addLeukocytes(5);
                message = "Density: " + Simulator.getInstance().getNbrOfLeukocytes();
                quantityText.setText(message);
                break;

            default:
                Log.d(TAG, "Density swipe: undefined");
        }
    }

    public void CHANGE(View v){

    }

    public void Video(View v){
        Intent intent=new Intent(getBaseContext(), Explaination.class);
        startActivity(intent);
    }
}
