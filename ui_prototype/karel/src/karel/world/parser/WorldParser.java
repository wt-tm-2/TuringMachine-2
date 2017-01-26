/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world.parser;

import java.util.Scanner;
import karel.view.KarelView;
import karel.world.World;
import karel.world.objects.Robot;
import karel.world.objects.Room;
import karel.world.objects.Rooms;
import karel.world.objects.Direction;

/**
 *
 * @author Ryan Ball
 */
public class WorldParser {
    
    private final KarelView view;
    private final World world;
    private Rooms<Integer, Integer, Room> rooms;
    private Robot robot;
    private int lineNum;
    
    public WorldParser(KarelView view, World world) {
        this.world = world;
        this.view = view;
    }
    
    public void createWorld(String text) throws WorldParserException {
        
        if (text.length() == 0) {
            throw new WorldParserException("Error: World editor is empty.", -1);
        }
        
        text = text.replaceAll("[ \t]*#[^\\n]*", "");    // replace all comments with an empty string
        
        rooms = new Rooms<>();
        world.setBoundaryWalls(rooms);
        robot = null;
        
        Scanner scanner = new Scanner(text);
        lineNum = 1;
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            
            if (!line.matches("[\\s]*")) {    // ignore empty lines and lines that contain only whitespace
                parseLine(line);
                lineNum++;
            }
        }
        if (robot == null) {
            throw new WorldParserException("Error: a robot has not been added to the world.", -1);
        }
        
        world.setWorldObjects(rooms, robot);
    }
    
    private void parseLine(String line) throws WorldParserException {
        
        String[] fields = line.split("[\\s]+");
        
        switch(fields[0].toLowerCase()) {
            case "robot":
                parseRobotLine(fields);
                break;
            case "wall":
                parseWallLine(fields);
                break;
            case "beeper":
                parseBeeperLine(fields);
                break;
            default:
                throw new WorldParserException("Error on line " + lineNum, lineNum);
        }
    }
    
    private void parseRobotLine(String[] fields) throws WorldParserException {
        
        String message = "Error on line " + lineNum +
                ": Usage --> Robot column row (N|S|E|W) [optional numberOfBeepers]\n";
        
        if (robot != null) {
            throw new WorldParserException("Error on line " + lineNum + ": Robot already defined\n", lineNum);
        }
        
        if (fields.length < 4 || fields.length > 5) {
            throw new WorldParserException(message, lineNum);
        }
       
        boolean error = false;
        
        int row = 0;
        int col = 0;
        int numberOfBeepers = 0;
        
        try {
            col = Integer.parseInt(fields[1]);
            if (col < 1 || col > 1000) {
                message += "--column value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        try {
            row = Integer.parseInt(fields[2]);
            if (row < 1 || row > 1000) {
                message += "--row value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        if (fields.length == 5) {
            
            try {
                numberOfBeepers = Integer.parseInt(fields[4]);
                if (numberOfBeepers < 0) {
                    message += "--numberOfBeepers must be positive\n";
                    error = true;
                }
            } catch(NumberFormatException e) {
                error = true;
            }
        }
        
        try {
            robot = new Robot(row, col, Direction.fromString(fields[3]), numberOfBeepers, rooms);
        } catch (IllegalArgumentException e) {
            message += e.getMessage();
            error = true;
        }
        
        if (error) {
            throw new WorldParserException(message, lineNum);
        }
    }
    
    private void parseWallLine(String[] fields) throws WorldParserException {
        
        String message = "Error on line " + lineNum + ": Usage -->"
                + " Wall column row (N|S|E|W) [optional length]\n";
        
        if (fields.length < 4 || fields.length > 5) {
            throw new WorldParserException(message, lineNum);
        }
        
        boolean error = false;
        
        Direction dir = null;
        int row = 0;
        int col = 0;
        int length = 1;
        
        try {
            col = Integer.parseInt(fields[1]);
            if (col < 1 || col > 1000) {
                message += "--column value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        try {
            row = Integer.parseInt(fields[2]);
            if (row < 1 || row > 1000) {
                message += "--row value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        try {
            dir = Direction.fromString(fields[3]);
        } catch (IllegalArgumentException e) {
            message += e.getMessage();
            error = true;
        }
        
        if (fields.length == 5) {
            try {
            length = Integer.parseInt(fields[4]);
                if (length < 0 || length > 1000) {
                    message += "--length must be between 1 and 1000\n";
                    error = true;
                }
            } catch(NumberFormatException e) {
                error = true;
            }
        }
        
        if (error) {
            throw new WorldParserException(message, lineNum);
        }
       
        setWalls(dir, col, row, length);
        
    }
    
    private void setWalls(Direction dir, int col, int row, int length) {
        
        if (dir == Direction.NORTH) {
            for (int i = 0 ; i < length; i++, col++) {
                // set first room wall
                Room room = rooms.get(row, col);
                if (room == null) {
        
                    room = new Room();
                    room.setWall(dir);
                    rooms.put(row, col, room);
                }
                else {
                    room.setWall(dir);
                }
                
                // set second room wall
                room = rooms.get(row + 1, col);
                if (room == null) {
                    room = new Room();
                    room.setWall(Direction.SOUTH);
                    rooms.put(row + 1, col, room);
                }
                else {
                    room.setWall(Direction.SOUTH);
                }
            }
        }
        else if (dir == Direction.SOUTH) {
            for (int i = 0 ; i < length; i++, col++) {
                // set first room wall
                Room room = rooms.get(row, col);
                if (room == null) {
        
                    room = new Room();
                    room.setWall(dir);
                    rooms.put(row, col, room);
                }
                else {
                    room.setWall(dir);
                }
                
                // set second room wall
                if (row != 0) {
                    room = rooms.get(row - 1, col);
                    if (room == null) {
                        room = new Room();
                        room.setWall(Direction.NORTH);
                        rooms.put(row - 1, col, room);
                    }
                    else {
                        room.setWall(Direction.NORTH);
                    }
                }
            }
        }
        else if (dir == Direction.WEST) {
            for (int i = 0 ; i < length; i++, row++) {
                
                // set first room wall
                Room room = rooms.get(row, col);
                if (room == null) {
                   
                    room = new Room();
                    room.setWall(dir);
                    rooms.put(row, col, room);
                }
                else {
                    room.setWall(dir);
                } 
                
                // set second room wall
                if (col != 0) {
                    room = rooms.get(row, col - 1);
                    if (room == null) {
                        room = new Room();
                        room.setWall(Direction.EAST);
                        rooms.put(row, col - 1, room);
                    }
                    else {
                        room.setWall(Direction.EAST);
                    }
                }
            }
        }
        else {
            for (int i = 0 ; i < length; i++, row++) {
                
                // set first room wall
                Room room = rooms.get(row, col);
                if (room == null) {
                   
                    room = new Room();
                    room.setWall(dir);
                    rooms.put(row, col, room);
                }
                else {
                    room.setWall(dir);
                } 
                
                // set second room wall
                room = rooms.get(row, col + 1);
                if (room == null) {
                    room = new Room();
                    room.setWall(Direction.WEST);
                    rooms.put(row, col + 1, room);
                }
                else {
                    room.setWall(Direction.WEST);
                }
            }
        }
    }
    
    private void parseBeeperLine(String[] fields) throws WorldParserException{
        
        String message = "Error on line " + lineNum + ": Usage --> Beeper column row numberOfBeepers\n";    
        
        if (fields.length != 4) {
            throw new WorldParserException(message, lineNum);
        }
        
        boolean error = false;
        
        int row = 0;
        int col = 0;
        int numberOfBeepers = 0;
        
        try {
            col = Integer.parseInt(fields[1]);
            if (col < 1 || col > 1000) {
                message += "--column value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        try {
            row = Integer.parseInt(fields[2]);
            if (row < 1 || row > 1000) {
                message += "--row value must be between 1 and 1000\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        try {
            numberOfBeepers = Integer.parseInt(fields[3]);
            if (numberOfBeepers < 0) {
                message += "--numberOfBeepers must be positive\n";
                error = true;
            }
        } catch(NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            throw new WorldParserException(message, lineNum);
        }
        
        Room room = rooms.get(row,col);
        message = "Error on line %d: the number of beepers in cell (%d,%d) has already been set.";
        
        if (room == null) {
            room = new Room();
            if (!room.setBeeperStack(numberOfBeepers)) {
                throw new WorldParserException(String.format(message, lineNum, col, row), lineNum);
            }
            rooms.put(row,col,room);
        }
        else {
            if (!room.setBeeperStack(numberOfBeepers)) {
                throw new WorldParserException(String.format(message, lineNum, col, row), lineNum);
            }
        }
    }
    
    public Rooms<Integer,Integer,Room> getWorldRooms() {
        return rooms;
    }
}
