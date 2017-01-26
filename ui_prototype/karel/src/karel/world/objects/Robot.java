/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Random;
import karel.world.BeeperException;
import karel.world.RobotMoveException;

/**
 *
 * @author Ryan Ball
 */
public class Robot extends Path2D.Double{
    
    Shape robot;
    private Rooms<Integer,Integer,Room> rooms;
    private int numberOfBeepers;
    private int row;
    private int col;
    private Direction dir;
    private Color robotColor = Color.blue;
    
    public Robot(int row, int col, Direction dir, int numberOfBeepers, 
            Rooms<Integer,Integer,Room> rooms) {
        
        this.numberOfBeepers = numberOfBeepers;
        this.rooms = rooms;
        this.row = row;
        this.col = col;
        this.dir = dir;
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(robotColor);
        g2.draw(this);
    }
    
    public void draw(Graphics2D g2, int x, int y, double moveOffset, double turnOffset, int cellSize) {
        
        g2.setColor(robotColor);
        if (turnOffset == 0) {
            switch(dir) {
                case NORTH:
                    //Draw RC style robot facing North
                    g2.fillRect(x + cellSize/4, y - (cellSize - 8) - (int)moveOffset, cellSize/2, cellSize-10);
                    g2.fillPolygon(new int[]{x + cellSize/4,x + cellSize/2,x + 3*cellSize/4},
                    new int[]{y - (cellSize - 8) - (int)moveOffset,y-(int)(cellSize*0.95)-(int)moveOffset,
                    y - (cellSize - 8) - (int)moveOffset}, 3);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x+ cellSize/4 - 7, y - (cellSize-4) - (int)moveOffset, 8, 16);
                    g2.fillOval(x+ 3*cellSize/4 - 2, y - (cellSize-4) - (int)moveOffset, 8, 16);
                    g2.fillOval(x+ cellSize/4 - 7, y - 15 - (int)moveOffset, 8, 16);
                    g2.fillOval(x+ 3*cellSize/4 - 2, y - 15 - (int)moveOffset, 8, 16);
                    break;
                case SOUTH:
                    //Draw RC style robot facing South
                    g2.fillRect(x + cellSize/4, y - (cellSize - 2) + (int)moveOffset, cellSize/2, cellSize-10);
                    g2.fillPolygon(new int[]{x + cellSize/4,x + cellSize/2,x + 3*cellSize/4}, 
                      new int[]{y-8+(int)moveOffset,y-(int)(cellSize*0.05)+(int)moveOffset,y-8+(int)moveOffset},3);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x+ cellSize/4 - 7, y - (cellSize) + (int)moveOffset, 8, 16);
                    g2.fillOval(x+ 3*cellSize/4 - 2, y - (cellSize) + (int)moveOffset, 8, 16);
                    g2.fillOval(x+ cellSize/4 - 7, y - 20 + (int)moveOffset, 8, 16);
                    g2.fillOval(x+ 3*cellSize/4 - 2, y - 20 + (int)moveOffset, 8, 16);
                    break;
                case EAST:
                    //Draw RC style robot facing East
                    g2.fillRect(x + 2 + (int)moveOffset,y - 3*cellSize/4 , cellSize - 10, cellSize/2);
                    g2.fillPolygon(new int[]{x+cellSize-8+(int)moveOffset,x+(int)(cellSize*0.95)+(int)moveOffset,
                            x+cellSize-8+(int)moveOffset},
                            new int[]{y - 3*cellSize/4,y - cellSize/2,y - cellSize/4},3);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x + (int)moveOffset, y - (3*cellSize/4)-7 , 16, 8);
                    g2.fillOval(x + (int)moveOffset, y - (cellSize/4)- 1 , 16, 8);
                    g2.fillOval(x + cellSize - 20 +(int)moveOffset, y - (3*cellSize/4)-7 , 16, 8);
                    g2.fillOval(x + cellSize - 20 +(int)moveOffset, y - (cellSize/4)- 1 , 16, 8);
                    break;
                case WEST:
                    //Draw RC style robot facing West
                    g2.fillRect(x + 8 - (int)moveOffset,y - 3*cellSize/4 , cellSize - 10, cellSize/2);
                    g2.fillPolygon(new int[]{x+8-(int)moveOffset,x+(int)(cellSize*0.05)-(int)moveOffset,x+8-(int)moveOffset},
                            new int[]{y-3*cellSize/4,y-cellSize/2,y-cellSize/4},3);
                    g2.setColor(Color.BLACK);
                    g2.fillOval(x + 4 - (int)moveOffset, y - (3*cellSize/4)-7 , 16, 8);
                    g2.fillOval(x + 4 - (int)moveOffset, y - (cellSize/4)- 1 , 16, 8);
                    g2.fillOval(x + cellSize - 16 -(int)moveOffset, y - (3*cellSize/4)-7 , 16, 8);
                    g2.fillOval(x + cellSize - 16 -(int)moveOffset, y - (cellSize/4)- 1 , 16, 8);
            }
            g2.draw(this);
        }
        else {
            
            Graphics2D rc = (Graphics2D)g2.create();
            switch(dir){
                case NORTH:
                    rc.rotate(-Math.toRadians(turnOffset),x + cellSize/2,y-cellSize/2);
                    break;
                case WEST:
                    rc.rotate(-Math.toRadians(turnOffset+90),x + cellSize/2,y-cellSize/2);
                    break;
                case SOUTH:
                    rc.rotate(-Math.toRadians(turnOffset+180),x + cellSize/2,y-cellSize/2);
                    break;
                case EAST:
                    rc.rotate(-Math.toRadians(turnOffset+270),x + cellSize/2,y-cellSize/2);
                    break;
            }
            //Redrawn robot after rotation of coordinates
            rc.setColor(Color.BLUE);
            rc.fillRect(x + cellSize/4,y - (cellSize - 8), cellSize/2, cellSize-10);
            rc.fillPolygon(new int[]{x + cellSize/4,x + cellSize/2,x + 3*cellSize/4},
                    new int[]{y - (cellSize - 8),y - (int)(cellSize*0.95), y - (cellSize - 8)}, 3);
            rc.setColor(Color.BLACK);
            rc.fillOval(x + cellSize/4 - 7,y - (cellSize-4), 8, 16);
            rc.fillOval(x + 3*cellSize/4 - 2, y - (cellSize-4), 8, 16);
            rc.fillOval(x + cellSize/4 - 7,y - 15, 8, 16);
            rc.fillOval(x + 3*cellSize/4 - 2, y - 15, 8, 16);
            rc.dispose();    
        }
    }
    
    //This method displays randomly placed dots within the relative vacinity of 
    //the robot. The colors are meant to look like a small fire and smoke explosion. 
    public void explode(Graphics2D g2,int x, int y, int cellSize) {

        int randX, randY;
        Random randGen = new Random();
        
        Color [] colors = {Color.YELLOW, Color.RED, Color.ORANGE,Color.BLACK};
        
        for (int i = 0; i < 30; i++) {
            randX = randGen.nextInt(cellSize);
            randY = randGen.nextInt(cellSize);
            g2.fillOval(x+randX,y-randY,27,27);
            g2.setColor(colors[i%4]);
        }
        
    }
    
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public int getNumberOfBeepers() {
        return numberOfBeepers;
    }
    
    public Direction getDirection() {
        return dir;
    }
    
    public Direction getLeft() {
        switch(dir) {
            case NORTH:
                return Direction.WEST;
            case SOUTH:
                return Direction.EAST;
            case EAST:
                return Direction.NORTH;
            case WEST:
                return Direction.SOUTH;
            default:
                return null;
        }
    }
    
    public Direction getRight() {
        switch(dir) {
            case NORTH:
                return Direction.EAST;
            case SOUTH:
                return Direction.WEST;
            case EAST:
                return Direction.SOUTH;
            case WEST:
                return Direction.NORTH;
            default:
                return null;
        }
    }
    
    public void putBeeper() throws BeeperException {
        if (numberOfBeepers == 0 ) {
            throw new BeeperException("Robot is out of beepers and is turning off.");
        }
        else {
            numberOfBeepers--;
        }
    }
    
    public void pickBeeper() {
        numberOfBeepers++;
    }
    
    public void turnleft() {
        switch(dir) {
            case NORTH:
                dir = Direction.WEST;
                break;
            case SOUTH:
                 dir = Direction.EAST;
                break;
            case WEST:
                 dir = Direction.SOUTH;
                break;
            case EAST:
                 dir = Direction.NORTH;
                break; 
        }
    }
    
    public String getStatus() {
        Room room = rooms.get(row, col);
        int roomBeepers = 0;
        
        if (room != null) {
            roomBeepers = room.getNumOfBeepers();
        }
        
        return String.format("Robot is at position [%d,%d], facing %s, carrying " +
                "%d beepers, and on top of %d beepers.", col, row, dir.toString().toLowerCase(),
                numberOfBeepers, roomBeepers);
    }
    
    public void move(Direction dir) {
        switch(dir) {
            case NORTH:
                row++;
                break;
            case SOUTH:
                row--;
                break;
            case WEST:
                col--;
                break;
            case EAST:
                col++;
                break;
        }
    }
}
