
package parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Defines a state in which the Turing Machine simulator can be in.
 * A state consists of a mnemonic name which identifies it, and a list
 * of transitions. 
 * @see parser.Transition
 * 
 * @author Michael Johnson
 */
public class State {
    private final String stateMnemonic;
    private ArrayList<Transition> stateTransitions;
    
    /**
     * Construct a new Turing Machine state with an empty transition list.
     * @param stateMnemonic denotes the mnemonic which will identify the state
     */
    State(String stateMnemonic) {
        this.stateMnemonic = stateMnemonic;
        stateTransitions = new ArrayList<>();
    }
    
    /**
     * Add a transition to the state transition list.
     * @param transition a new transition
     * @see parser.Transition
     */
    public void addTransition(Transition transition) {
        stateTransitions.add(transition);
    }
    
    /**
     * 
     * @return the list of transitions for this state
     */
    public ArrayList<Transition> getStateTransitions() {
        return stateTransitions;
    }
    
    /**
     * 
     * @return the mnemonic that identifies this state
     */
    public String getStateMnemonic() {
        return stateMnemonic;
    }

    /* Override methods implemented by NetBeans */
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
