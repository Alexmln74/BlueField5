package com.example.bluefield;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import com.example.bluefield.simulator.Head;
import com.example.bluefield.simulator.Leukocyte;
import com.example.bluefield.simulator.LeukocytePart;
import com.example.bluefield.simulator.Simulator;
import com.example.bluefield.simulator.Tail;

public class MyDrawable extends Drawable implements Drawable.Callback, Runnable{
    private final Paint paint;
    private boolean running=false;

    /**
     * Default constructor
     */
    public MyDrawable() {
        // Set up color and text size
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        // Redraw the entire simulation

        // Get the drawable's bounds
        int width = getBounds().width();
        int height = getBounds().height();
        float x = (float)(width)/Simulator.getInstance().getWidth();
        float y = (float)(height)/Simulator.getInstance().getHeight();

        // Blue background
        paint.setColor(Color.parseColor("#356BFE"));                             //setARGB(200,0x00,0x44,0xff);
        canvas.drawRect(40,80,width,height,paint);

        // Draw all leukocytes
        for (Leukocyte leukocyte : Simulator.getInstance().getLeukocytes()) {
            // draw each part of leukocyte
            for (LeukocytePart part : leukocyte.getBody()) {
                if (part instanceof Head) {
                    // Head in white
                    paint.setARGB(255,0x98,0xD5,0xFF);
                } else if (part instanceof Tail) {
                    // Tail in dark blue
                    paint.setARGB(200,0x26,0x3E,0x7F);
                } else {
                    // Regular body part in medium blue
                    paint.setARGB(200,0x4C,0x7C,0xFF);
                }
                canvas.drawRect(x*part.getPosition().x,y*part.getPosition().y,
                        x*(part.getPosition().x+1),y*(part.getPosition().y+1),paint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        super.invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        invalidateDrawable(who);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        super.unscheduleSelf(what);
    }

    /**
     * Loads next animation frame
     *
     * @ return void
     */
    public void nextFrame(){
        // deactivate previous "timer"
        unscheduleSelf(this);
        // reactivate new "timer" @ 41 ms -> 24 frames per sec
        scheduleSelf(this, SystemClock.uptimeMillis() + 41);
    }


    @Override
    public void run() {
        // is called with the scheduler, automatically goes back to sleep after
        invalidateSelf();   // android calls draw()
        nextFrame();
    }

    /**
     * Start the animation
     *
     * @ return void
     */
    public void start(){
        if(!running){
            running=true;
            nextFrame();
        }
    }

    /**
     * Stops the animation
     *
     * @ return void
     */
    public void stop(){
        running=false;
        unscheduleSelf(this);
    }

}
