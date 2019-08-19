package com.example.bluefield.simulator;


import android.graphics.Point;

/**
 * Represents a general leukocyte part
 *
 * @author SA
 * @version 1.0
 */
public class LeukocytePart {


    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/
    protected Point position;

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    public LeukocytePart() {
        position = new Point(0,0);
    }

    /**
     * Constructor with initial position
     *
     * @param  position : Initial position
     */
    public LeukocytePart(Point position) {
        this();
        this.setPosition(position);
    }
    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/
    public Point getPosition() {
        return new Point(position.x,position.y);
    }

    public void setPosition(Point position) {

        this.position.x = position.x;
        this.position.y = position.y;
    }

    /*----------------------------------------- METHODS ------------------------------------------*/
    @Override
    public String toString() {
        String retVal = "leukocyte part: (x,y): ("+position.x+":"+position.y+")";

        return retVal;
    }
}
