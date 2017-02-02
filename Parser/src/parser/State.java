/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;

/**
 * Not yet fully implemented.
 * 
 * @author Michael Johnson
 */
public class State {
    private String stateMnemonic;
    private ArrayList<Transition> stateTransitions;
    
    State(String stateMnemonic) {
        this.stateMnemonic = stateMnemonic;
    }
    
    public void addTransition(Transition transition) {
        
    }
    
    public ArrayList<Transition> getStateTransitions() {
        return stateTransitions;
    }
}
