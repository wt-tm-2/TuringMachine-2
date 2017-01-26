/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world.objects;

/**
 *
 * @author Ryan Ball
 */
public class Room {
    
    private int numberOfBeepers = 0;
    
    private boolean southWall = false;
    private boolean northWall = false;
    private boolean westWall = false;
    private boolean eastWall = false;
    
    public void setWall(Direction dir) {
        switch(dir) {
            case NORTH:
                northWall = true;
                break;
            case SOUTH:
                southWall = true;
                break;
            case EAST:
                eastWall = true;
                break;
            case WEST:
                westWall = true;
                break;
            default:
                throw new IllegalArgumentException("Illegal direction.");
        }
    }
    
    /** Return true if beeper stack was set, false if beeper stack was 
     * already set.
     * @param numberOfBeepers
     * @return 
     */
    public boolean setBeeperStack(int numberOfBeepers) {
        
        boolean beepersNotSet = this.numberOfBeepers == 0;
        if (beepersNotSet) {
            this.numberOfBeepers = numberOfBeepers;
        }
        return beepersNotSet;
        
    }
    
    public void incrementBeeperStack() {
        numberOfBeepers++;
    }
    
    /** Return true if successful, false if no more beepers to take.
     * 
     * @return 
     */
    public boolean decrementBeeperStack() {
        return --numberOfBeepers >= 0;
    }
    
    public boolean getWall(Direction dir) throws IllegalArgumentException{
        switch(dir) {
            case NORTH:
                return northWall;
            case SOUTH:
                return southWall;
            case EAST:
                return eastWall;
            case WEST:
                return westWall;
            default:
                throw new IllegalArgumentException("Illegal direction.");
        }
    }
    
    public int getNumOfBeepers() {
        return numberOfBeepers;
    }
}
