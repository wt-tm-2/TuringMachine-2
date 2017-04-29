
package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.scene.shape.Circle;

/**
 * Defines a state in which the Turing Machine simulator can be in.
 * A state consists of a mnemonic name which identifies it, and a list
 * of transitions. 
 * 
 * Note: This class now also defines some graphic properties for the state.
 * 
 * @see Transition
 * 
 * @author Michael Johnson
 * @author Zach Gutierrez
 */
public class State {
    private final String stateMnemonic;
    private boolean initialState = false;
    
    /* Graphics properties */
    private ArrayList<Transition> stateTransitions;
    private Circle stateGraphic;
    
    /**
     * Construct a new Turing Machine state with an empty transition list.
     * 
     * @param stateMnemonic denotes the mnemonic which will identify the state
     */
    State(String stateMnemonic) {
        this.stateMnemonic = stateMnemonic;
        stateTransitions = new ArrayList<>();
        stateGraphic = new Circle();
        stateGraphic.setId(stateMnemonic);
    }
    
    /**
     * Add a transition to the state transition list.
     * 
     * @param transition a new transition
     * @see Transition
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
    
    /**
     * 
     * @return whether or not this State is the state the Turing Machine
     *         begins execution in
     */
    public boolean isInitialState() {
        return initialState;
    }
    
    /**
     * Sets whether this State is to be the Turing Machine's initial state.
     * 
     * @param initialState whether or not this State is the initial State
     */
    public void setIsInitialState(boolean initialState) {
        this.initialState = initialState;
    }
    
    public static State getInitialState(HashMap<String, State> stateList) {
        for (State state : stateList.values()) {
            if (state.isInitialState()) {
                return state;
            }
        }
        return null;
    }
    
    public void setGraphicAttributes(double centerX, double centerY, double radius){
        stateGraphic.setCenterX(centerX);
        stateGraphic.setCenterY(centerY);
        stateGraphic.setRadius(radius);
    }
    
    public Circle getStateGraphic(){
        return stateGraphic;
    }
    
    public String getStateID(){
        return stateGraphic.getId();
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
