
package parser;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Test 
 * 
 * @author Michael Johnson
 */
public class TestDriver {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Demo for the parser */
        try {
            HashMap<String, State> stateList = Parser.parseSourceFile("error3.txt");
            
            /* Loop through the states defined in the source file */
            for (State state : stateList.values()) {
                System.out.println("At state: " + state.getStateMnemonic());
                
                for (Transition transition : state.getStateTransitions()) {
                    /* Loop through each transition the state has */
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
