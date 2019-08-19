package com.example.bluefield.simulator;


import java.util.LinkedList;
import java.util.ListIterator;

import com.example.bluefield.simulator.Head;
import com.example.bluefield.simulator.LeukocytePart;
import com.example.bluefield.simulator.Path;
import com.example.bluefield.simulator.Tail;

/**
 * Represents a complete leukocyte
 *
 * @author SA
 * @version 1.0
 */
public class Leukocyte {

    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/
    private LinkedList<LeukocytePart> body;
    private Path path;
    private int headPtr;    //  points to where the head is in the path

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Default constructor
     */
    public Leukocyte(){
        // create a simple head+tail leukocyte
        body = new LinkedList<LeukocytePart>();
        body.add(new Head());
        body.add(new Tail());

        path = null;
        headPtr = 1;
    }

    /**
     * Constructor with associated path
     *
     * @param n : number of body parts (head and tail excluded)
     * @param  path : associated path
     */
    public Leukocyte(int n, Path path){
        this.path = path;

        // create a head + n body part + tail leukocyte and directly position it on the path
        int index = n+1;
        body = new LinkedList<LeukocytePart>();
        body.add(new Head(path.getPosition(index--)));
        for(int i = 0; i<n; i++){
            body.add(new LeukocytePart(path.getPosition(index--)));
        }
        body.add(new Tail(path.getPosition(index)));
        headPtr = body.size()-1;
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/
    public int getLength() {
        return body.size();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        // replace path
        this.path = path;

        // reposition leukocyte to beginning of the path
        headPtr = body.size()-1;
        ListIterator<LeukocytePart> iter = body.listIterator();
        while(iter.hasNext()){
            iter.next().setPosition(this.path.getPosition(body.size()-iter.previousIndex()));
        }
    }

    public LinkedList<LeukocytePart> getBody() {
        return body;
    }

    /*----------------------------------------- METHODS ------------------------------------------*/
    @Override
    public String toString() {
        String retVal = "Leukocyte:\t";
        retVal += body.getFirst().toString();
        retVal += "\tlength: "+body.size();
        retVal += "\r\n" + path.toString();

        return retVal;
    }

    /**
     * Move leukocyte along the path
     *
     * @param step : number of steps of displacement
     *
     * @ return void
     */
    public void move(int step){
        // repeat for n step
        for(int j = 0; j < step; j++){
            // move element by 1 from tail to head not included
            for(int i = body.size()-1; i > 0;) {
                body.get(i).setPosition(body.get(--i).getPosition());
            }

            // move head of 1 element
            headPtr = (headPtr+1)%path.getLength();
            body.getFirst().setPosition(path.getPosition(headPtr));
        }
    }
}