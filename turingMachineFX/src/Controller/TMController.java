/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import parser.Parser;
import parser.State;
import turingmachinefx.FXMLDocumentController;
import parser.Transition;


public class TMController {
    
    private static HashMap<String, State> stateList = new HashMap<>();
    private Vector<Character> tape = new Vector<Character>();
    private String currentState = new String();
    private char overwrite = '1';
    private Transition nextTransition;
    ArrayList<Transition> trans;
    private int instructionCounter = 0;

    //private TMView tmView;
    private int index = 0;
    
    public TMController(){
        //tmView = new TMView(this);
        
    }
    
    public void disposeTMView(){
        //tmView.dispose();
    }
    
    /**
     * Takes data from the view and stores it to be used in the main logic
     * of the code. Then uses this data to find the state and prepare the
     * turing machine for execution.
     * @param tape2 the tape initially entered in the text box on GUI
     * @param IS the initial state entered in the text box on the GUI
     * @throws FileNotFoundException 
     */
    
    public void loadData(String tape2,String IS, String path) throws FileNotFoundException{
        stateList = Parser.parseSourceFile(path);
        for (int i = 0, n = tape2.length(); i < n; i++) {
            tape.add(tape2.charAt(i));
        }
        State state = stateList.get(IS);
        trans = state.getStateTransitions();
        //int next = trans.indexOf(tape.get(index));
        for (int i = 0; i < trans.size(); i++){
            if(trans.get(i).getReadSymbol().equals(tape.get(index).toString())){
                nextTransition = trans.get(i);
            }
        }
        currentState = IS;
            

    }
    
    /**
     * Clears all global variables for this class to start fresh on the next load.
     */
    public void resetData(){
        if(stateList.isEmpty() == false){
        stateList.clear();
        tape.clear();
        trans.clear();
        index = 0;
        instructionCounter = 0;
        }
    }
    
    
    /**
     * Executes the current line in the turing machine and then finds the next
     * step to be executed.
     */
    
    public int step(){
        tape.set(index, nextTransition.getWriteSymbol().charAt(0));
        if (nextTransition.getDirection().equals("l") || nextTransition.getDirection().equals("L")){
            index--;
        }
        if (nextTransition.getDirection().equals("r") || nextTransition.getDirection().equals("R")){
            index++;
        }
        if (nextTransition.getNewState().equals("halt")){
            instructionCounter++;
            return 1;
        }
        else{
        currentState = nextTransition.getNewState();
        State state = stateList.get(currentState);
        trans = state.getStateTransitions();
        if (tape.size() == index){
            tape.add("_".charAt(0));
        }
        if (index==-1){
            tape.add(0,"_".charAt(0));
            index = 0;
        }
        for (int i = 0; i < trans.size(); i++){
            if(trans.get(i).getReadSymbol().equals(tape.get(index).toString())){
                nextTransition = trans.get(i);
            }
        }
        instructionCounter++;
        return 0;
        }
    }
    
    public String[] getData(){
        String[] data = new String[5];
        data[0] = currentState;
        data[1] = nextTransition.getReadSymbol();
        data[2] = nextTransition.getWriteSymbol();
        data[3] = nextTransition.getDirection();
        data[4] = nextTransition.getNewState();
        return data;
    }
    
    public Vector<Character> getTape(){
        return tape;
    }
    
    public int getIC(){
        return instructionCounter;
    }
    
    public static int getSize(){
        return stateList.size();
    }
    
}
