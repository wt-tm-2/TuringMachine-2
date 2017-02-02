/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 * Not yet fully implemented.
 * 
 * @author Michael Johnson
 */
public class Transition {
    private String readSymbol;
    private String writeSymbol;
    private String direction;
    private String newStateMnemonic;
    
    Transition(String readSymbol, String writeSymbol, String direction, String newStateMnemonic) {
        this.readSymbol = readSymbol;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
        this.newStateMnemonic = newStateMnemonic;
    }

    public String getReadSymbol() {
        return readSymbol;
    }

    public String getWriteSymbol() {
        return writeSymbol;
    }

    public String getDirection() {
        return direction;
    }

    public String getNewState() {
        return newStateMnemonic;
    }
    
    
}
