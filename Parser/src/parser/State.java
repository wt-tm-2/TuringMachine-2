/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Not yet fully implemented.
 * 
 * @author Michael Johnson
 */
public class State {
    private final String stateMnemonic;
    private ArrayList<Transition> stateTransitions;
    
    State(String stateMnemonic) {
        this.stateMnemonic = stateMnemonic;
    }
    
    public void addTransition(Transition transition) {
        stateTransitions.add(transition);
    }
    
    public ArrayList<Transition> getStateTransitions() {
        return stateTransitions;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.stateMnemonic);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (!Objects.equals(this.stateMnemonic, other.stateMnemonic)) {
            return false;
        }
        return true;
    }
    
    
}
