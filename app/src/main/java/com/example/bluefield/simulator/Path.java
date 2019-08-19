package com.example.bluefield.simulator;


import android.graphics.Point;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Represents a path (based on capillaries structure) that a leukocyte follows
 *
 * @author SA
 * @version 1.0
 */
public class Path {

    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/
    // constants
    private static final int MIN_POINTS = 60;
    private static final int MAX_POINTS = 100;

    private LinkedList<Point> points;

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    public Path(){
        // create path with minimum length
        this(MIN_POINTS);
    }

    /**
     * Constructor with determined path
     *
     * @param  points : list of points forming the path
     */
    public Path(LinkedList<Point> points){
        points = new LinkedList<Point>();
        ListIterator<Point> iter = points.listIterator();

        while(iter.hasNext()){
            this.points.add(new Point(iter.next()));
        }
    }

    /**
     * Constructor with determined path length
     *
     * @param  length: number of points forming the path
     */
    public Path(int length){
        points = new LinkedList<Point>();
        length = (length<MIN_POINTS)? MIN_POINTS : length;
        length = (length>MAX_POINTS)? MAX_POINTS : length;
        for(int i = 0; i < length; i++){
            points.add(new Point(0,0));
        }
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/
    public int getLength() {
        return points.size();
    }

    public Point getPosition(int index){
        return ((index < points.size())? new Point(points.get(index)) : null);
    }

    public  void setPosition(int index, Point point){
        if(index < points.size()){
            points.set(index,point);
        }
    }

    public LinkedList<Point> getPoints() {
        return (LinkedList<Point>) points.clone();
    }

    public void setPoints(LinkedList<Point> points) {
        this.points.clear();
        ListIterator<Point> iter = points.listIterator();

        while(iter.hasNext()){
            this.points.add(new Point(iter.next()));
        }
    }

    public static int getMinPoints() {
        return MIN_POINTS;
    }

    public static int getMaxPoints() {
        return MAX_POINTS;
    }

    /*----------------------------------------- METHODS ------------------------------------------*/
    @Override
    public String toString() {
        String retVal = "Path:";
        ListIterator<Point> iter = points.listIterator();
        Point temp;
        int i = 0;

        while(iter.hasNext()){
            temp = new Point(iter.next());
            retVal += "\r\n"+(i++)+": ("+temp.x+","+temp.y+")";
        }
        return retVal;
    }

    /**
     * Add a new position to the end of the path
     *
     * @param  point : new point to add
     *
     * @return void
     */
    public void addPoint(Point point){
        if(points.size() < MAX_POINTS){
            points.add(new Point(point));
        }
    }

    /**
     * Remove a position at the end of the path
     *
     * @return void
     */
    public void removePoint(){
        if(points.size() > MIN_POINTS){
            points.removeLast();
        }
    }
}
