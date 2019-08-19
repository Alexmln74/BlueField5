package com.example.bluefield.simulator;


import android.graphics.Point;

/**
 * Represents the tail of a leukocyte
 *
 * @author SA
 * @version 1.0
 */
public class Tail extends LeukocytePart {
    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    public Tail() {
        super();
    }

    /**
     * Constructor with initial position
     *
     * @param  position : Initial position
     */
    public Tail(Point position) {
        super(position);
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/

    /*----------------------------------------- METHODS ------------------------------------------*/
    @Override
    public String toString() {
        String retVal = "leukocyte tail: (x,y): ("+position.x+":"+position.y+")";

        return retVal;
    }
}
