package com.example.bluefield.simulator;

import android.graphics.Point;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.util.*;
import java.util.concurrent.Semaphore;

import com.example.bluefield.simulator.Leukocyte;
import com.example.bluefield.simulator.Path;

/**
 * Represents the simulation data
 *
 * @author SA
 * @version 1.0
 */
public class Simulator extends Thread{

    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/
    private static Simulator instance = null;

    // constants
    private final int N_PATH = 400;
    private final int INITIAL_WIDTH = 400;
    private final int INITIAL_HEIGHT = 400;
    private final double CLEARNCE_FACTOR = 0.2;
    private final int INITIAL_TIME = 20;
    private final int INITIAL_NUMBER = 100;
    private final int RANDOM_CUM = 2;

    private int width;
    private int height;
    private long timeMs;
    private LinkedList<Path> paths;
    private LinkedList<Leukocyte> leukocytes;
    private int clearanceWidth;
    private int clearanceHeight;

    private Semaphore mutex;

    private Random rng;

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    private Simulator(){
        mutex = new Semaphore(1);

        // initialize random number generator
        rng = new Random();

        // initialize attributes with constants
        this.width = INITIAL_WIDTH;
        this.height = INITIAL_HEIGHT;
        this.timeMs = INITIAL_TIME;
        clearanceWidth = (int)(CLEARNCE_FACTOR*width);
        clearanceHeight = (int)(CLEARNCE_FACTOR*height);

        // create empty path and leukocyte lists
        paths = new LinkedList<Path>();
        leukocytes = new LinkedList<Leukocyte>();

        // create paths and leukocytes
        generatePaths();
        addLeukocytes(INITIAL_NUMBER);
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/
    public static Simulator getInstance(){
        if(instance == null){
            instance = new Simulator();
        }

        return instance;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {

        this.width = width;
        clearanceWidth = (int)(CLEARNCE_FACTOR*this.width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {

        this.height = height;
        clearanceWidth = (int)(CLEARNCE_FACTOR*this.width);
    }

    public long getTimeMs() {
        return timeMs;
    }

    public synchronized void setTimeMs(long timeMs) {

        if(timeMs > 0){
            this.timeMs = timeMs;
        }
    }

    public LinkedList<Leukocyte> getLeukocytes() {
        return (LinkedList<Leukocyte>) leukocytes.clone();
    }

    public int getNbrOfLeukocytes(){
        int retVal = 0;
        try {
            mutex.acquire();
            retVal =  leukocytes.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }

        return retVal;
    }

    /*----------------------------------------- METHODS ------------------------------------------*/
    /**
     * Add n new leukocyte to the simulation
     *
     * @param n : number leukocytes to add
     *
     * @ return void
     */
    public void addLeukocytes(int n){
        try {
            mutex.acquire();
            // repeat for n new leukocyte
            for(int i = 0; i < n; i++){
                // select path (path = leukocyte index)
                leukocytes.add(new Leukocyte(2,paths.get(leukocytes.size()%N_PATH)));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    /**
     * Add n new leukocyte to the simulation
     *
     * @param n : number leukocytes to remove
     *
     * @ return void
     */
    public void removeLeukocytes(int n){
        try {
            mutex.acquire();
            ListIterator<Leukocyte> iter = (ListIterator<Leukocyte>) leukocytes.listIterator();
            int i = 0;
            while(iter.hasNext() && i < n){
                iter.next();
                iter.remove();
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    /**
     * Indicate to all leukocyte in simulation to move
     *
     * @ return void
     */
    private void move(){
        try {
            mutex.acquire();
            ListIterator<Leukocyte> iter = leukocytes.listIterator();
            while (iter.hasNext()){
                iter.next().move(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    /**
     * Set new time in between each move with a delta of time
     *
     * @param delta : time difference
     *
     * @ return void
     */
    public void changeTimeMs(int delta) {
        if(delta > (-timeMs)){
            setTimeMs(timeMs += delta);
        }
    }

    /**
     * Generate a set of random path only if none exists
     *
     * @ return void
     */
    private void generatePaths(){
        if(paths.isEmpty()){
            double[] probabilities = new double[8];

            for(int j = 0; j < N_PATH; j++) {
                // pick random length and create path
                int length = (int)(rng.nextInt(Path.getMaxPoints()-Path.getMinPoints())+Path.getMinPoints());
                paths.add(new Path(length));

                // pick random x coordinate of starting point
                Point startingPt = new Point(0,0);
                for(int i = 0; i < RANDOM_CUM; i++) {
                    startingPt.x += rng.nextInt(width);
                }
                startingPt.x /= RANDOM_CUM;

                // pick random y coordinate of starting point (but not in clearance area)
                if(startingPt.x > (width-clearanceWidth)/2 && startingPt.x < (width+clearanceWidth)/2)
                {
                    // clearance area
                    for(int i = 0; i < RANDOM_CUM; i++) {
                        startingPt.y += rng.nextInt((height-clearanceHeight)/2);
                    }
                    startingPt.y /= RANDOM_CUM;

                    startingPt.y += (rng.nextInt() < 0.5)? 0 : (height+clearanceHeight)/2;
                }
                else{
                    for(int i = 0; i < RANDOM_CUM; i++) {
                        startingPt.y += rng.nextInt(height);
                    }
                    startingPt.y /= RANDOM_CUM;
                }

                // set directions probabilities according to quadrant
                if(startingPt.x > width/2 && startingPt.y < height/2){
                    // quadrant 1
                    probabilities[0] = 0.14;
                    probabilities[1] = 0;
                    probabilities[2] = 0;
                    probabilities[3] = 0.14;
                    probabilities[4] = 0.14;
                    probabilities[5] = 0.14;
                    probabilities[6] = 0.3;
                    probabilities[7] = 0.14;
                }
                else if(startingPt.x <  width/2 && startingPt.y < height/2){
                    // quadrant 2
                    probabilities[0] = 0;
                    probabilities[1] = 0;
                    probabilities[2] = 0.14;
                    probabilities[3] = 0.14;
                    probabilities[4] = 0.3;
                    probabilities[5] = 0.14;
                    probabilities[6] = 0.14;
                    probabilities[7] = 0.14;
                }
                else if(startingPt.x <  width/2 && startingPt.y > height/2){
                    // quadrant 3
                    probabilities[0] = 0.14;
                    probabilities[1] = 0.14;
                    probabilities[2] = 0.3;
                    probabilities[3] = 0.14;
                    probabilities[4] = 0.14;
                    probabilities[5] = 0;
                    probabilities[6] = 0;
                    probabilities[7] = 0.14;
                }
                else{
                    // quadrant 4
                    probabilities[0] = 0.3;
                    probabilities[1] = 0.14;
                    probabilities[2] = 0.14;
                    probabilities[3] = 0.14;
                    probabilities[4] = 0;
                    probabilities[5] = 0;
                    probabilities[6] = 0.14;
                    probabilities[7] = 0.14;
                }

                // compute path
                RelativePath relativePath;
                do {// make sure entire path is in boundaries
                    relativePath = new RelativePath(length, probabilities);
                }while((startingPt.x+relativePath.getdX() < 0)||(startingPt.x+relativePath.getdX()>width-1)
                        ||(startingPt.y+relativePath.getdY() <0)||(startingPt.y+relativePath.getdY() >height+1));
                paths.get(j).getPosition(0).set(startingPt.x,startingPt.y);
                for(int i = 0; i < length; i++) {
                    paths.get(j).setPosition(i,new Point(relativePath.getRelativePosition(i,0)+startingPt.x,relativePath.getRelativePosition(i,1)+startingPt.y));
                }
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            // move all leukocytes
            move();
            try {
                // suspend thread for a certain time before moving again
                this.sleep(timeMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


