/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world.objects;

/**
 *
 * @author Ryan and Nicole
 */
public enum Direction {
    NORTH("N"), SOUTH("S"), EAST("E"), WEST("W");
    
    private final String text;

    Direction(String text) {
      this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Direction fromString(String text) {
        if (text != null) {
            for (Direction dir : Direction.values()) {
                if (text.equalsIgnoreCase(dir.text)) {
                    return dir;
                }
            }
        }
    throw new IllegalArgumentException("--direction must be N | W | S | E\n");
  }
}
