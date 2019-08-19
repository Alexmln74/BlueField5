package com.example.bluefield.simulator;


import android.graphics.Point;

/**
 * Represents the head of a leukocyte
 *
 * @author SA
 * @version 1.0
 */
public class Head extends LeukocytePart {
    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    public Head() {
        super();
    }


    /**
     * Constructor with initial position
     *
     * @param  position : Initial position
     */
    public Head(Point position) {
        super(position);
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/

    /*----------------------------------------- METHODS ------------------------------------------*/
    @Override
    public String toString() {
        String retVal = "leukocyte head: (x,y): ("+position.x+":"+position.y+")";

        return retVal;
    }
}