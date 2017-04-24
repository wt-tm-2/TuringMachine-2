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
import Parser.Parser;
import Parser.ParserException;
import Parser.State;
import TuringMachineFX.FXMLDocumentController;
import Parser.Transition;


public class TMController {
    
    private static HashMap<String, State> stateList = new HashMap<String, State>();
    private Vector<Character> tape1 = new Vector<Character>();
    private Vector<Character> tape2 = new Vector<Character>();
    private Vector<Character> tape3 = new Vector<Character>();
    private String currentState = new String();
    private char overwrite = '1';
    private Transition nextTransition;
    ArrayList<Transition> trans;
    private int instructionCounter = 0;
    private int currentTape = 1;
    
    //private TMView tmView;
    private int[] index1 = new int[] {0};
    private int[] index2 = new int[] {0};
    private int[] index3 = new int[] {0};
    
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
     * @throws ParserException
     */
    
    public void loadData(String Gtape1, String Gtape2, String Gtape3, String IS, 
            String path) throws FileNotFoundException, ParserException {
        stateList = Parser.parseSourceFile(path);
        for (int i = 0, n = Gtape1.length(); i < n; i++) {
            tape1.add(Gtape1.charAt(i));
        }
        for (int i = 0, n = Gtape2.length(); i < n; i++) {
            tape2.add(Gtape2.charAt(i));
        }
        for (int i = 0, n = Gtape3.length(); i < n; i++) {
            tape3.add(Gtape3.charAt(i));
        }
        State state = State.getInitialState(stateList);
        trans = state.getStateTransitions();
        //int next = trans.indexOf(tape.get(index));
        
        for (int i = 0; i < trans.size(); i++){
            if(trans.get(i).getReadSymbol().equals(tape1.get(index1[0]).toString())){
                nextTransition = trans.get(i);
            }
        }
        currentState = state.getStateMnemonic();
    }
    
    /**
     * Clears all global variables for this class to start fresh on the next load.
     */
    public void resetData(){
        if(stateList.isEmpty() == false){
        stateList.clear();
        tape1.clear();
        tape2.clear();
        tape3.clear();
        trans.clear();
        index1[0] = 0;
        index2[0] = 0;
        index3[0] = 0;
        instructionCounter = 0;
        currentTape = 1;
        }
    }
    /*
    Checks on which tape is the current tape and performs a step on the turing machine
    */
    public int step(){
        int x = 0;
        if (currentTape == 1){
            x = stepPart1(index1,tape1);
        }
        else if (currentTape == 2){
            x = stepPart1(index2,tape2);
        }
        else if (currentTape == 3){
            x = stepPart1(index3,tape3);
        }
        if(x != 1){
            if (currentTape == 1){
            x = stepPart2(index1,tape1);
        }
        else if (currentTape == 2){
            x = stepPart2(index2,tape2);
        }
        else if (currentTape == 3){
            x = stepPart2(index3,tape3);
        }
        }
        return x;
    }
    
    /**
     * Executes the current line in the turing machine and then finds the next
     * step to be executed.
     */
    public int stepPart1(int[] index, Vector<Character> tape){
        tape.set(index[0], nextTransition.getWriteSymbol().charAt(0));
        if (nextTransition.getDirection().equals("l") || nextTransition.getDirection().equals("L")){
            index[0]--;
        }
        if (nextTransition.getDirection().equals("r") || nextTransition.getDirection().equals("R")){
            index[0]++;
        }
        if (nextTransition.getNewState().equals("halt")){
            instructionCounter++;
            return 1;
        }
        else{
        currentState = nextTransition.getNewState();
        if (nextTransition.getTapeIndex().equals("_")){
        }
        else {
        currentTape = Integer.parseInt(nextTransition.getTapeIndex());
        }
        State state = stateList.get(currentState);
        trans = state.getStateTransitions();
        return 0;
        }
    }
    /*
    Prepares the next instruction to be able to be executed next time stepPart1 is called
    */
    public int stepPart2(int[] index, Vector<Character> tape){
        if (tape.size() == index[0]){
            tape.add("_".charAt(0));
        }
        if (index[0]==-1){
            tape.add(0,"_".charAt(0));
            index[0] = 0;
        }
        for (int i = 0; i < trans.size(); i++){
            if(trans.get(i).getReadSymbol().equals(tape.get(index[0]).toString())){
                nextTransition = trans.get(i);
            }
        }
        instructionCounter++;
        return 0;
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
    
    public Transition getNextTransition() {
        return nextTransition;
    }
    
    public Vector<Character> getTape1(){
        return tape1;
    }
    
    public Vector<Character> getTape2(){
        return tape2;
    }
    
    public Vector<Character> getTape3(){
        return tape3;
    }
    
    public int getIC(){
        return instructionCounter;
    }
    
    public int getCT(){
        return currentTape;
    }
    
    public static int getSize(){
        return stateList.size();
    }
    
    public static HashMap<String, State> getStateList(){
        return stateList;
    }
    
}
