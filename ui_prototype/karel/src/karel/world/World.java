/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world;

import java.awt.Component;
import java.awt.Font;
import karel.interpreter.Test;
import karel.world.objects.Direction;
import karel.world.objects.Robot;
import karel.world.objects.Room;
import karel.world.objects.Rooms;

/**
 *
 * @author Ryan Ball
 */
public class World extends javax.swing.JPanel {
    
    private Font headerFont = new Font("Sans Sarif", Font.BOLD, 14);
    private int worldSize = 1000;
    private int cellSize = 50;
    private Robot robot;
    private Rooms<Integer,Integer,Room> rooms = new Rooms<>();
    private boolean gridClicked = false;
    
    /**
     * Creates new form WorldGrid
     */
    public World() {
        
        setBoundaryWalls(this.rooms);
        
        initComponents();
        
        verticalBar.addAdjustmentListener(new ScrollListener(worldRowHeader, worldGrid));
        horizontalBar.addAdjustmentListener(new ScrollListener(worldColumnHeader, worldGrid));
        
        int maxScroll = worldSize * cellSize - worldRowHeader.getPreferredSize().height;
        verticalBar.setMaximum(maxScroll);
        verticalBar.setValue(maxScroll);
        
        horizontalBar.setMaximum(worldSize * cellSize - worldColumnHeader.getWidth());
    }
    
    public void setCellSize(int cellSize) {
        
        int row = (verticalBar.getMaximum() - verticalBar.getValue())/this.cellSize;
        int col = horizontalBar.getValue() / this.cellSize;
        
        this.cellSize = cellSize;
        worldRowHeader.setCellSize(cellSize);
        worldColumnHeader.setCellSize(cellSize);
        worldGrid.setCellSize(cellSize);
        
        verticalBar.setMaximum(worldSize * cellSize - worldRowHeader.getHeight());
        horizontalBar.setMaximum(worldSize * cellSize - worldColumnHeader.getWidth());
        
        verticalBar.setValue(verticalBar.getMaximum() - row  * cellSize);
        horizontalBar.setValue(col * cellSize);
    }
    
    public void setWorldObjects(Rooms<Integer,Integer,Room> rooms, Robot robot) {
        this.rooms = rooms;
        this.robot = robot;
        worldGrid.setRobot(robot);
        worldGrid.setRooms(rooms);
        worldGrid.repaint();
        
        verticalBar.setValue(verticalBar.getMaximum() - (robot.getRow() - 1) * cellSize + worldGrid.getHeight()/2);
        horizontalBar.setValue((robot.getCol()- 1) * cellSize - worldGrid.getWidth()/2);
    }
    
    public void moveRobot(int speed) throws RobotMoveException {
        worldGrid.moveRobot(speed);
    }
    
    public void turnRobot(int speed) {
        worldGrid.turnRobot(speed);
    }
    
    public void putBeeper() throws BeeperException {

        int row = robot.getRow();
        int col = robot.getCol();
        robot.putBeeper();
      
        Room room = rooms.get(row, col);
        if (room == null) {
            room = new Room();
            rooms.put(row, col, room);
        }
        room.incrementBeeperStack();

        worldGrid.repaint();
    }
    
    public void pickBeeper() throws BeeperException {
      
        Room room = rooms.get(robot.getRow(), robot.getCol());
        if (room == null || room.getNumOfBeepers() == 0) {
           throw new BeeperException("No beepers to pick up. Robot is turning off."); 
        }
        room.decrementBeeperStack();
        robot.pickBeeper();

        worldGrid.repaint();
    }
    
    public String getRobotStatus() {
        return robot.getStatus();
    }
    
    public boolean checkTest(Test test) {
        
        Direction dir = robot.getDirection();
        Room room = rooms.get(robot.getRow(), robot.getCol());
        boolean result = false;
        
        switch(test) {
            case FRONT_IS_CLEAR:
                return room == null || (room != null && !room.getWall(dir));
            case FRONT_IS_BLOCKED:
                return room != null && room.getWall(dir);
            case RIGHT_IS_CLEAR:
                return room == null || (room != null && !room.getWall(robot.getRight()));
            case RIGHT_IS_BLOCKED:
                return room != null && room.getWall(robot.getRight());
            case LEFT_IS_CLEAR:
                return room == null || (room != null && !room.getWall(robot.getLeft()));
            case LEFT_IS_BLOCKED:
                return room != null && room.getWall(robot.getLeft());
            case FACING_NORTH:
                return robot.getDirection() == Direction.NORTH;
            case NOT_FACING_NORTH:
                return robot.getDirection() != Direction.NORTH;
            case FACING_EAST:
                return robot.getDirection() == Direction.EAST;
            case NOT_FACING_EAST:
                return robot.getDirection() != Direction.EAST;
            case FACING_SOUTH:
                return robot.getDirection() == Direction.SOUTH;
            case NOT_FACING_SOUTH:
                return robot.getDirection() != Direction.SOUTH;
            case FACING_WEST:
                return robot.getDirection() == Direction.WEST;
            case NOT_FACING_WEST:
                return robot.getDirection() != Direction.WEST;
            case NEXT_TO_A_BEEPER:
                return (room != null && room.getNumOfBeepers() > 0);
            case NOT_NEXT_TO_A_BEEPER:
                return (room == null) || (room != null && room.getNumOfBeepers() == 0);
            case ANY_BEEPERS_IN_BEEPER_BAG:
                return robot.getNumberOfBeepers() > 0;
            case NO_BEEPERS_IN_BEEPER_BAG:
                return robot.getNumberOfBeepers() == 0;
            default:
                    return false;
        }
    }
    
    public int getWorldSize() {
        return worldSize;
    }
    
    public void setBoundaryWalls(Rooms<Integer, Integer, Room> rooms) {
        
        Room room;
        int minRow = 1;
        int maxRow = worldSize;
        int minCol = 1;
        int maxCol = worldSize;
        
        for (int i = 2 ; i < worldSize; i++) {
        
            // set western boundary walls
            room = new Room();
            room.setWall(Direction.WEST);
            rooms.put(i, minCol, room);

            // set southern boundary walls
            room = new Room();
            room.setWall(Direction.SOUTH);
            rooms.put(minRow, i, room);

            
            // set northern boundary walls
            room = new Room();
            room.setWall(Direction.NORTH);
            rooms.put(maxRow, i, room);

            
            // set eastern boundary walls
            room = new Room();
            room.setWall(Direction.EAST);
            rooms.put(i, maxCol, room);
        }
        
        // set south-west corner
        room = new Room();
        room.setWall(Direction.SOUTH);
        room.setWall(Direction.WEST);
        rooms.put(1, 1, room);
        
        // set south-east corner
        room = new Room();
        room.setWall(Direction.SOUTH);
        room.setWall(Direction.EAST);
        rooms.put(1, worldSize, room);
        
        // set north-west corner
        room = new Room();
        room.setWall(Direction.NORTH);
        room.setWall(Direction.WEST);
        rooms.put(worldSize, 1, room);
        
        // set north-east corner
        room = new Room();
        room.setWall(Direction.NORTH);
        room.setWall(Direction.EAST);
        rooms.put(worldSize, worldSize, room);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        verticalBar = new javax.swing.JScrollBar();
        horizontalBar = new javax.swing.JScrollBar();
        worldColumnHeader = new karel.world.WorldColumnHeader(horizontalBar, headerFont, cellSize, worldSize);
        worldRowHeader = new karel.world.WorldRowHeader(verticalBar, headerFont, cellSize, worldSize);
        worldGrid = new karel.world.WorldGrid(rooms, verticalBar, horizontalBar, cellSize);

        setPreferredSize(new java.awt.Dimension(682, 685));

        verticalBar.setBlockIncrement(50);
        verticalBar.setUnitIncrement(10);
        verticalBar.setVisibleAmount(0);
        verticalBar.setPreferredSize(new java.awt.Dimension(20, 700));

        horizontalBar.setBlockIncrement(50);
        horizontalBar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        horizontalBar.setUnitIncrement(10);
        horizontalBar.setVisibleAmount(0);
        horizontalBar.setPreferredSize(new java.awt.Dimension(600, 20));

        worldColumnHeader.setBackground(new java.awt.Color(255, 255, 255));
        worldColumnHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        worldColumnHeader.setPreferredSize(new java.awt.Dimension(550, 50));

        javax.swing.GroupLayout worldColumnHeaderLayout = new javax.swing.GroupLayout(worldColumnHeader);
        worldColumnHeader.setLayout(worldColumnHeaderLayout);
        worldColumnHeaderLayout.setHorizontalGroup(
            worldColumnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        worldColumnHeaderLayout.setVerticalGroup(
            worldColumnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        worldRowHeader.setBackground(new java.awt.Color(255, 255, 255));
        worldRowHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        worldRowHeader.setPreferredSize(new java.awt.Dimension(50, 550));

        javax.swing.GroupLayout worldRowHeaderLayout = new javax.swing.GroupLayout(worldRowHeader);
        worldRowHeader.setLayout(worldRowHeaderLayout);
        worldRowHeaderLayout.setHorizontalGroup(
            worldRowHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );
        worldRowHeaderLayout.setVerticalGroup(
            worldRowHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        worldGrid.setBackground(new java.awt.Color(255, 255, 255));
        worldGrid.setPreferredSize(new java.awt.Dimension(700, 700));
        worldGrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                worldGridMousePressed(evt);
            }
        });

        javax.swing.GroupLayout worldGridLayout = new javax.swing.GroupLayout(worldGrid);
        worldGrid.setLayout(worldGridLayout);
        worldGridLayout.setHorizontalGroup(
            worldGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        worldGridLayout.setVerticalGroup(
            worldGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(worldRowHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(worldGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(horizontalBar, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE))
                    .addComponent(worldColumnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verticalBar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(verticalBar, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addGap(113, 113, 113))
            .addGroup(layout.createSequentialGroup()
                .addComponent(worldRowHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addGap(103, 103, 103))
            .addGroup(layout.createSequentialGroup()
                .addComponent(worldGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(worldColumnHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(horizontalBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void worldGridMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_worldGridMousePressed
        gridClicked = true;
        Component source=(Component) evt.getSource();
        source.getParent().dispatchEvent(evt); // Pass event onto KarelView
    }//GEN-LAST:event_worldGridMousePressed

    public boolean gridWasClicked() {
        return gridClicked;
    }
    
    public void setGridClicked(boolean clicked) {
        gridClicked = clicked;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollBar horizontalBar;
    private javax.swing.JScrollBar verticalBar;
    private karel.world.WorldColumnHeader worldColumnHeader;
    private karel.world.WorldGrid worldGrid;
    private karel.world.WorldRowHeader worldRowHeader;
    // End of variables declaration//GEN-END:variables

}
