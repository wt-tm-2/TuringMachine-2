/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JScrollBar;
import static karel.view.KarelView.worldEditor;
import karel.world.objects.Direction;
import karel.world.objects.Robot;
import karel.world.objects.Room;
import karel.world.objects.Rooms;
import java.util.Arrays;

/**
 *
 * @author Ryan Ball
 */
public class WorldGrid extends javax.swing.JPanel implements MouseListener {

    private JScrollBar verticalBar;
    private JScrollBar horizontalBar;
    
    private int pHeight;
    private int pWidth;
    
    private Rooms<Integer,Integer,Room> rooms;
    private Robot robot;

    private int cellSize;
    private int dotRadius;
    private int dotOffset;
    private int xOffset;
    private int yOffset;
    private double moveOffset = 0;
    private double turnOffset = 0;
    public static boolean explodeFlag = false;
    
    // bottom-most row number and left-most column number
    private int rowNumber = 1;
    private int colNumber = 1;
    
    private int beepercount = 0;
    
    private int fontHeight;
    private FontMetrics fontMetrics; 
    
    public WorldGrid() {
        this(new Rooms<Integer,Integer,Room>(), new JScrollBar(), new JScrollBar(), 50);
        addMouseListener(this);
    }
    
    /**
     * Creates new form WorldGrid
     */
    public WorldGrid(Rooms<Integer,Integer,Room> rooms,
            JScrollBar verticalBar, JScrollBar horizontalBar, int cellSize) {
        addMouseListener(this);
        setCellSize(cellSize);
        this.rooms = rooms;
        this.verticalBar = verticalBar;
        this.horizontalBar = horizontalBar;
        initComponents();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        pHeight = this.getHeight();
        pWidth = this.getWidth();
		
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Sans Serif", Font.BOLD, cellSize/4 + 1));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        fontMetrics = g2.getFontMetrics();
        fontHeight = fontMetrics.getHeight();
    
        rowNumber = verticalBar.getMaximum() - verticalBar.getValue();
        colNumber = horizontalBar.getValue();
        
        yOffset = rowNumber % cellSize;
        xOffset = colNumber % cellSize;
        
        rowNumber /= cellSize;
        colNumber /= cellSize;
        rowNumber++;
        colNumber++;
        
        int tempColNumber = colNumber;
        int tempRowNumber = rowNumber;

        // Render Grid
        for(int x = -xOffset; x <= pWidth; x+=cellSize, tempColNumber++){
            tempRowNumber = rowNumber;
            for(int y = pHeight+ yOffset; y >= 0; y-=cellSize, tempRowNumber++) {
                drawGridDot(g2, x, y, Color.black);
                if (rooms != null) {
                    Room room = rooms.get(tempRowNumber,tempColNumber);
                    if (room != null) {
                        drawWalls(g2, room, x, y);
                        if (room.getNumOfBeepers() != 0) {
                            drawGridDot(g2, x, y, Color.white);
                            drawBeeperStack(g2, room, x, y);
                        }
                    }
                }
            }			
        }
        // draw robot if it exists
        if (robot != null) {
            g2.setStroke(new BasicStroke(3));
            int col = robot.getCol();
            int row = robot.getRow();
            // draw robot if it is located within the bounds of the current display
            if (col >= colNumber && col < tempColNumber &&
                row >= rowNumber && row < tempRowNumber) {

                int x = Math.abs(robot.getCol() - colNumber) * cellSize - xOffset;
                int y = pHeight + yOffset - Math.abs(robot.getRow() - rowNumber) * cellSize;
                if(explodeFlag==true){
                    robot.explode(g2, x, y, cellSize);
                }
                else 
                    robot.draw(g2, x, y, moveOffset, turnOffset, cellSize);
            }
        }
    }
    
    /*
     * drawGridDot - Draws a single dot on the grid in the room given the x and 
     *               y positions of where the room starts. Seting dotColor to white
     *               will "erase" the dot from the grid.
     */
    private void drawGridDot(Graphics2D grid, int roomStartX, int roomStartY, Color dotColor) {
        grid.setColor(dotColor);
        grid.fillOval(roomStartX + dotOffset - 3, roomStartY - dotOffset - 4, 
                dotRadius, dotRadius);
    }
    
    public void drawWalls(Graphics2D g2, Room room, int x, int y) {
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(cellSize/10));
        if (room.getWall(Direction.EAST)) {
            g2.drawLine(x + cellSize, y, x + cellSize, y - cellSize);
        }
        if (room.getWall(Direction.WEST)) {
            g2.drawLine(x, y, x, y - cellSize);
        }
        if (room.getWall(Direction.NORTH)) {
            g2.drawLine(x, y - cellSize, x + cellSize, y - cellSize);
        }
        if (room.getWall(Direction.SOUTH)) {
            g2.drawLine(x, y, x + cellSize, y);
        }
    }
    
    public void drawBeeperStack(Graphics2D g2, Room room, int x, int y) {
        // draw beeper oval
        int xLoc = (int) (x + cellSize / 4.0);
        int yLoc = (int) (y - cellSize * 3.0/4.0);
        int cellHalved = (int) (cellSize/2.0);
        g2.setColor(new Color(0, 150, 150));
        g2.fillOval(xLoc, yLoc, cellHalved, cellHalved);

        // draw beeper number
        int numberOfBeepers = room.getNumOfBeepers();
        g2.setColor(Color.yellow);
        String beeperString = String.valueOf(numberOfBeepers);
        int stringPixelLength = fontMetrics.stringWidth(beeperString);
        g2.drawString(beeperString, (int)(x + cellSize/2.0 - stringPixelLength/2.0),
                (int)(y - cellSize/2.0 + fontHeight/4.0));
    }
    
    public void setRooms(Rooms<Integer,Integer,Room> rooms) {
        this.rooms = rooms;
    }
    
    public void setRobot(Robot robot) {
        this.robot = robot;
    }
    
    public void moveRobot(final int speed) throws RobotMoveException {
        
        Direction dir = robot.getDirection();
        int robotRow = robot.getRow();
        int robotCol = robot.getCol();
        
        Room room = rooms.get(robotRow, robotCol);

        String errorMessage = "Robot ran into wall and is now turning off.";
        
        switch(dir) {
            case NORTH:
                
                if (room != null  && room.getWall(dir)) {
                    explodeFlag=true;
                    animateExplode();
                    throw new RobotMoveException(errorMessage);
                }
                
                animateMove(dir, speed);
                
                if (robotRow + 1 == rowNumber + pHeight/cellSize) {
                    verticalBar.setValue(verticalBar.getValue() - cellSize);
                }
                break;
            case SOUTH:
                
                if (room != null  && room.getWall(dir)) {
                    explodeFlag=true;
                    animateExplode();
                    throw new RobotMoveException(errorMessage);
                }
                
                animateMove(dir, speed);
                
                if (robotRow - 1 == rowNumber) {
                    verticalBar.setValue(verticalBar.getValue() + cellSize);
                }
                break;
            case WEST:
                
                if (room != null  && room.getWall(dir)) {
                    explodeFlag=true;
                    animateExplode();
                    throw new RobotMoveException(errorMessage);
                }
                
                animateMove(dir, speed);
                
                if (robotCol - 1 == colNumber) {
                    horizontalBar.setValue(horizontalBar.getValue() - cellSize);
                }
                break;
            case EAST:
                
                if (room != null  && room.getWall(dir)) {
                    explodeFlag=true;
                    animateExplode();
                    throw new RobotMoveException(errorMessage);
                }
                
                animateMove(dir, speed);
                
                if (robotCol + 1 == colNumber + pWidth/cellSize) {
                    horizontalBar.setValue(horizontalBar.getValue() + cellSize);
                }
                break;   
        }
    }
      
    private void animateMove(Direction dir, int speed) {
        
        double distance = cellSize/15;
        int mspf = speed/15;    // milliseconds per frame
        
        for (moveOffset = 0; moveOffset <= cellSize; moveOffset+=distance) {
            try {
                Thread.sleep(mspf);
            } catch (InterruptedException ex) {
                assert false;
            }
            this.repaint();
        }
        moveOffset = 0;

        robot.move(dir);
    }
    
    private void animateExplode(){
        //Repaints 20 times to make the appearance of an explosion.
        for(int i=0; i<20; i++){
            try{
                Thread.sleep(100);
            } catch (InterruptedException ex){
                assert false;
            }
            this.repaint();
        }
    }
   
    public void turnRobot(int speed) {
        double angle = 90/15;
        int mspf = speed/15;    // milliseconds per frame
        
        for (turnOffset = 0; turnOffset <= 90; turnOffset+=angle) {
            try {
                Thread.sleep(mspf);
            } catch (InterruptedException ex) {
                assert false;
            }
            this.repaint();
        }
        turnOffset = 0;
        robot.turnleft();
    }
    
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        dotRadius = cellSize / 6;
        dotOffset = cellSize /2;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent me) {
    
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int currentX = me.getX();
        int currentY = me.getY();
        
        int scrolledX = horizontalBar.getValue();
        int scrolledY = verticalBar.getValue();
        
        int actualX = currentX + scrolledX;
        int actualY = Math.abs(pHeight - currentY) + Math.abs(verticalBar.getMaximum() - scrolledY);
        
        int xCell = (actualX/cellSize) + 1;
        int yCell = (actualY/cellSize) + 1;
        
        int xOffset = Math.abs(cellSize - (actualX % cellSize));
        int yOffset = Math.abs(cellSize - (actualY % cellSize));
        
        int xClickBound = 0;
        int yClickBound = 0;
        int xBeeperClick = 0;
        int yBeeperClick = 0;
        
        
        switch(cellSize){
                case 50: //Zoom at 100%
                    yClickBound = xClickBound = 7;
                    xBeeperClick = yBeeperClick = 15;
                    break;
                case 40: //Zoom at 80%
                    yClickBound = xClickBound = 6;
                    xBeeperClick = yBeeperClick = 10;
                    break;
                case 30: //Zoom at 60%
                    yClickBound = xClickBound = 5;
                    xBeeperClick = yBeeperClick = 5;
                    break;
        }

        String newWall = null;
        String newBeeper = null;
        
        if (me.getButton() == MouseEvent.BUTTON1){
            if ((xOffset < xClickBound) && (xCell > 0) && (xCell < 1000)){
                newWall = "wall " + xCell + " " + yCell + " E";
                if (xCell == 0 || yCell == 0){
                    newWall = null;
                }
                if (karel.view.KarelView.DEBUG == true)
                {
                    //System.out.println("Where the wall should go, and what the code should say");
                    //System.out.println(newWall);
                }
            }
            else if (((actualX%cellSize) < ((cellSize/2) + xBeeperClick)) && ((actualX%cellSize) > ((cellSize/2) - xBeeperClick)) 
                    && ((actualY%cellSize) < ((cellSize/2) + yBeeperClick)) && ((actualY%cellSize) > ((cellSize/2) - yBeeperClick))){
                xCell = (actualX/cellSize) + 1;
                yCell = (actualY/cellSize) + 1;
                newBeeper = "beeper " + xCell + " " + yCell; 
                int oldbeeper = getOldbeeper(newBeeper);
                newBeeper = "beeper " + xCell + " " + yCell + " " + oldbeeper;
                worldEditor.delete("\n" + newBeeper);
                if(oldbeeper == 1 || oldbeeper == 0){
                    newBeeper = null;
                }
                else{
                newBeeper = "beeper " + xCell + " " + yCell + " " + --oldbeeper;
                }

            }
            else if (((actualX % cellSize) < xClickBound) && (xCell > 0) && (xCell < 1000)){
                newWall = "wall " + (xCell-1) + " " + yCell + " E";
                if (xCell-1 ==0 || yCell == 0){
                    newWall = null;
                }
                if (karel.view.KarelView.DEBUG == true)
                {
                    //System.out.println("Where the wall should go, and what the code should say");
                    //System.out.println(newWall);                
                }
            } 
            else if ((yOffset < yClickBound) && (yCell > 0) && (yCell < 1000)){
                newWall = "wall " + xCell + " " + yCell + " N";
                if (xCell == 0 || yCell == 0){
                    newWall = null;
                }
                if (karel.view.KarelView.DEBUG == true){
                    //System.out.println("Where the wall should go, and what the code should say");
                    //System.out.println(newWall);
                }
            }
            else if (((actualY % cellSize) < yClickBound) && (yCell > 0) && (yCell < 1000)){
                newWall = "wall " + xCell + " " + (yCell-1) + " N";
                if (xCell == 0 || yCell-1 == 0){
                    newWall = null;
                }
                if (karel.view.KarelView.DEBUG == true){
                    //System.out.println("Where the wall should go, and what the code should say");
                    //System.out.println(newWall);
                }
            }
        }
        else if(me.getButton() == MouseEvent.BUTTON3)
        {
            if (((actualX%cellSize) < ((cellSize/2) + xBeeperClick)) && ((actualX%cellSize) > ((cellSize/2) - xBeeperClick)) 
                    && ((actualY%cellSize) < ((cellSize/2) + yBeeperClick)) && ((actualY%cellSize) > ((cellSize/2) - yBeeperClick))){
                xCell = (actualX/cellSize) + 1;
                yCell = (actualY/cellSize) + 1;
                newBeeper = "beeper " + xCell + " " + yCell;
                if (worldEditor.contains(newBeeper)){
                   int oldbeeper = getOldbeeper(newBeeper);
                   newBeeper = "beeper " + xCell + " " + yCell + " " + oldbeeper;
                   worldEditor.delete("\n" + newBeeper);
                   newBeeper = "beeper " + xCell + " " + yCell + " " + ++oldbeeper;
                }
                else{
                newBeeper = "beeper " + xCell + " " + yCell + " 1";
                }
            }
        }
        
        if (newWall != null)
        {
            karel.view.KarelView.guiAddWall(newWall);
        }
        if (newBeeper != null)
        {
            karel.view.KarelView.guiAddBeeper(newBeeper);
        }
        //Debug statements
        if (karel.view.KarelView.DEBUG == true){
            //System.out.println(currentX + "," + currentY);
            //System.out.println(scrolledX + "," + scrolledY);
            //System.out.println("Actual values for x,y");
            //System.out.println(actualX + "," + actualY);
            //System.out.println("This should be the wall to the right of " + ((actualX/cellSize) + 1));
            //System.out.println("This should be the wall above " + ((actualY/cellSize) + 1));
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    public int getOldbeeper(String newBeeper){
        int index = 0;
        String s = worldEditor.getText();
        String[] array = s.split("\n");
            for (int i =0; i < array.length; i++){
                if(array[i].contains(newBeeper)){
                    index = i;
                    }
                }
        String currentBeeper = array[index];
        String[] array2 = currentBeeper.split(" ");
        int oldbeeper = Integer.parseInt(array2[array2.length - 1]);
        return oldbeeper;
    }
            

}
