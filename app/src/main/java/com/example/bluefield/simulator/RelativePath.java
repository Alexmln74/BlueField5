package com.example.bluefield.simulator;


import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;//EnumeratedIntegerDistribution;
/**
 * Represents a relative path
 *
 * @author SA
 * @version 1.0
 */
public class RelativePath {

    /*---------------------------------------- ATTRIBUTES ----------------------------------------*/
    private int[][] path;
    private int dX = 0;
    private int dY = 0;
    private int length;

    /* Directions are associated with the following values:
      0 : diagonal up-left
      1 : up
      2 : diagonal up-right
      3 : right							0  1  2
      4 : diagonal down-right				7  x  3
      5 : down							6  5  4
      6 : diagonal down-left
      7 : left
   */
    private final static int[] DIRECTIONS = {0,1,2,3,4,5,6,7};

    /*--------------------------------------- CONSTRUCTORS ---------------------------------------*/
    /**
     * Constructor with path parameters
     *
     * @param  nSteps : number of points forming the path
     * @param probabilities : table of probabilities associated with each direction
     */
    public RelativePath(int nSteps, double[] probabilities) {

        length = nSteps;
        path = new int[nSteps][2];
        int[] directions = new int[nSteps-1];
        EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(DIRECTIONS,probabilities);
        int deltaX = 0;
        int deltaY = 0;
        final int delta = 4;

        // chose random direction for each steps according to distributed probabilities
        for(int i = 0; i < directions.length; i++) {
            directions[i] = distribution.sample();
        }

        // encode direction in 2D displacement + compute cumulative sum
        path[0][0] = 0;
        path[0][1] = 0;
        for(int i = 0; i < directions.length; i+= delta) {
            switch (directions[i]) {
                // diagonal up-left
                case 0 :
                    deltaX = -1;
                    deltaY = -1;
                    break;

                // up
                case 1 :
                    deltaX = 0;
                    deltaY = -1;
                    break;

                // diagonal up-right
                case 2 :
                    deltaX = 1;
                    deltaY = -1;
                    break;

                //	right
                case 3 :
                    deltaX = 1;
                    deltaY = 0;
                    break;

                // diagonal down-right
                case 4 :
                    deltaX = 1;
                    deltaY = 1;
                    break;

                // down
                case 5 :
                    deltaX = 0;
                    deltaY = 1;
                    break;

                // diagonal down-left
                case 6 :
                    deltaX = -1;
                    deltaY = 1;
                    break;

                // left
                case 7 :
                    deltaX = -1;
                    deltaY = 0;
                    break;

                default :
                    break;
            }

            // compute global deltas
            dX += deltaX;
            dY += deltaY;

            // repeat direction to create longer movements
            for(int j = 0; j < delta; j++){
                if(i+j < directions.length) {
                    path[i + j + 1][0] = path[i + j][0] + deltaX;
                    path[i + j + 1][1] = path[i + j][1] + deltaY;

                    dX += deltaX;
                    dY += deltaY;
                }
            }
        }
    }

    /*------------------------------------ GETTERS & SETTERS -------------------------------------*/
    public int[][] getPath() {
        return path;
    }

    public int getRelativePosition(int index, int dim) {
        int retVal = 0;
        // boundary check
        if(index < path.length && dim < path[0].length){
            retVal = path[index][dim];
        }
        return retVal;
    }

    public int getdX() {
        return dX;
    }

    public int getdY() {
        return dY;
    }

    public int getLength() {
        return length;
    }

    /*----------------------------------------- METHODS ------------------------------------------*/
}
