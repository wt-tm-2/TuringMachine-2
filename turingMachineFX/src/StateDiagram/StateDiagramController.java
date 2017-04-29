/**
 *   StateDiagramController is a public class to be called from FXMLDocumentController.java
 *   PRECONDITIONS: This class assumes that the HashMap passed to it upon instantiation has been 
 *   populated. It also assumes that a Pane used for the state diagram exists.
 *   POSTCONDITIONS: Based upon the assumptions, the public methods, drawStateDiagram and 
 *   clearPane, will draw on and clear the correct Pane of the GUI respectively.
 */
package StateDiagram;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import Parser.State;
import Parser.Transition;

/**
 *
 * @author Zach Gutierrez
 */
public class StateDiagramController {
    //HashMap used for getting the states
    private static HashMap<String, State> stateList;
    
    /**
        * 
        * @param sl is a HashMap of State objects that has already been populated
        * POSTCONDITION: The instance of StateDiagramController has access to the HashMap of States
        * 
        */
    public StateDiagramController(HashMap<String, State> sl){
        stateList = sl;
    }
    
    private static double positions [][] = new double[20][2];
    
    /**
        *   The method drawStateDiagram draws a state diagram which is a graphical representation of the 
        *   source code for the Turning Machine.
        * 
        * @param sdPane is the JavaFX class Pane used to draw the state diagram.
        *   PRECONDITION: sdPane is a Pane that already exists.
        *   POSTCONDITION: A state transition diagram is drawn in the sdPane using the Circle attributes
        *   from the class State.java. Each Circle represents a state, and the lines connecting the Circles 
        *   represent the transitions.
        */
    public void drawStateDiagram(Pane sdPane){
        
        //Distribute the States evenly around an ellipse
        int angle = 360/stateList.size();
        for(int ind=0; ind<stateList.size(); ind++){
            positions[ind][0]= 425 + 340*Math.cos(Math.toRadians(angle*ind));
            positions[ind][1]= 300 + 240*Math.sin(Math.toRadians(angle*ind));
        }
        //Initialize the positions of the States
        int i=0;
        for(State state : stateList.values()){
            state.setGraphicAttributes(positions[i][0], positions[i][1], 25);
            state.getStateGraphic().setFill(Color.AQUA);
            i++;
        }
        
        //Draw the transitions first so when the states are drawn, the states cover the lines going
        //to the center of the Circles
        for(State state  : stateList.values()){
            String [] nextStates = getTransitionState(state);
            if(nextStates[0]!=null){
                for(String nState : nextStates){
                    if(!nState.equalsIgnoreCase("halt")){
                        if(state.getStateMnemonic().equalsIgnoreCase(nState)){
                            int yValue;
                            if(state.getStateGraphic().getCenterY()>300)
                                yValue = 1;
                            else
                                yValue = 0;
                            drawLoop(state.getStateGraphic(),yValue,sdPane);
                        }
                        else{
                            connectCircles(state.getStateGraphic(),stateList.get(nState).getStateGraphic(),sdPane);
                        }
                    }
                }
            }
        }
        //Draw the labels for the transitions
        setLabels(sdPane);
        
        //Draw the labeled states last
        for(State state: stateList.values()){
            Label label = new Label(state.getStateMnemonic());
            label.setTextFill(Color.BLACK);
            label.setLayoutX(state.getStateGraphic().getCenterX() - state.getStateGraphic().getRadius() +5);
            label.setLayoutY(state.getStateGraphic().getCenterY() - 8);
            sdPane.getChildren().addAll(state.getStateGraphic(),label);
        }
    }
    
    /*
        *  This method gets each of the states the current state can transition to.
        *  It returns a String array with the state mneumonics of the states being transitioned to.
        *  PRECONDITION: State state is a state that exists.
        *  POSTCONDITION: newStates is a String array of the states the current state can transition to.
        */
    private String [] getTransitionState(State state){
        ArrayList<Transition> trans = state.getStateTransitions();
        String [] newStates = new String[trans.size()];
        for(int i=0; i<trans.size(); i++){
            newStates[i] = trans.get(i).getNewState();
        }
        return newStates;
    }
    
    /*
        *  This method connects States with a straight line.
        *  PRECONDITION: Circles c1 and c2 both exist, and sdPane is an existing Pane able to be draw on.
        *  POSTCONDITION: A line is drawn between the circles which is bound by the circles' centers.
        */
    private void connectCircles(Circle c1, Circle c2, Pane sdPane){
        Line line = new Line();
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());
        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
        sdPane.getChildren().add(line);
    }

    /*
        *  This method draws a loop from a circle back to itself to represent a state transitioning back to
        *  itself. 
        *  PRECONDTION: Circle c1 and Pane sdPane exist.
        *  POSTCONDITION: The loop is draw either above or below the circle, depending on the value of
        *  y passed to the method. y represents the y coordinate the circle's center is located.
        */
    private void drawLoop(Circle c1, int y, Pane sdPane){
        Arc arc = new Arc();
        double startAngle = 180*y;
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(Color.BLACK);
        arc.setType(ArcType.OPEN);
        arc.setCenterX(c1.getCenterX());
        arc.setCenterY(c1.getCenterY());
        arc.setRadiusX(20);
        arc.setRadiusY(50);
        arc.setStartAngle(startAngle);
        arc.setLength(180);
        sdPane.getChildren().add(arc);
    }
    
    /*
        *  This method draws labels along a tranisition line in the state diagram. 
        *  PRECONDITION: Circles c1 and c2 and Label trans all exist.
        *  POSTCONDITION: The Label trans is drawn along a line connecting Circles c1 and c2.
        */
    private void drawLabel(Circle c1, Circle c2, Label trans){
        double angle;
        if(c2.getCenterX()==c1.getCenterX())
            angle = 0;
        else
            angle = Math.toDegrees(Math.atan((c2.getCenterY()-c1.getCenterY())/(c2.getCenterX()-c1.getCenterX())));
        trans.relocate((c1.getCenterX()+c2.getCenterX())/2, (c1.getCenterY()+c2.getCenterY())/2);      
        trans.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        trans.getTransforms().clear();
        trans.getTransforms().add(new Rotate(angle,0,0));
    }
    
    /*
        *  This method draws a Label on an arc in the state diagram.
        *  PRECONDITION: Circle c1 and Label trans exist.
        *  POSTCONDITION: An arc in the state diagram is labeled.
        */
    private void drawArcLabel(Circle c1, Label trans){
        double y;
        if(c1.getCenterY() > 300)
            y = 52;
        else
            y = -65;
        trans.relocate(c1.getCenterX(), c1.getCenterY()+y);
        trans.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    /*
        *  This method gets the transition values and adds them to the Label for each transition line. 
        *  Each Label is given an id, which is how a label is updated.
        *  PRECONDITION: The states exist in the Pane sdPane, and the states have transition values.
        *  POSTCONDITION: The Labels are added to sdPane along the correct transition line with the 
        *  correct transition values.
        */
    private void setLabels(Pane sdPane){
        setLabelIds(sdPane);
        String lineLabel;
        String arcLabel;
        for(State state : stateList.values()){ 
            String [] nextStates = getTransitionState(state);
            for(int i=0; i<nextStates.length; i++){
                if(state.getStateMnemonic().equalsIgnoreCase(nextStates[i])){
                    Label transLabel = (Label) sdPane.lookup("#"+state.getStateMnemonic()+"To"+nextStates[i]);
                    arcLabel = transLabel.getText();
                    arcLabel += "(" +state.getStateTransitions().get(i).getReadSymbol() + ", " +
                        state.getStateTransitions().get(i).getWriteSymbol() + ", " +
                        state.getStateTransitions().get(i).getDirection() + ")";
                    transLabel.setText(arcLabel);
                }
                else{
                    if(nextStates[i].equalsIgnoreCase("halt")){
                        //Do nothing. The halt state is not a valid state.
                    }
                    else{
                        Label transLabel = (Label) sdPane.lookup("#"+state.getStateMnemonic()+"To"+nextStates[i]);
                        lineLabel = transLabel.getText();
                        lineLabel += "(" +state.getStateTransitions().get(i).getReadSymbol() + ", " +
                            state.getStateTransitions().get(i).getWriteSymbol() + ", " +
                            state.getStateTransitions().get(i).getDirection() + ")";
                        transLabel.setText(lineLabel);
                    }
                }
                if(nextStates[i].equalsIgnoreCase("halt")){
                    //Do nothing. The halt state is not a valid state.
                }
                else{                   
                    if(state.getStateMnemonic().equalsIgnoreCase(nextStates[i])){
                        drawArcLabel(state.getStateGraphic(),(Label)sdPane.lookup("#"+state.getStateMnemonic()+"To"+nextStates[i]));                       
                    }
                    else{                       
                        drawLabel(state.getStateGraphic(),stateList.get(nextStates[i]).getStateGraphic(),
                                (Label)sdPane.lookup("#"+state.getStateMnemonic()+"To"+nextStates[i]));                       
                    }
                }
            }
        }
    }
    
    /*
        *  This method sets the Label ids to the format  "<currentState>To<nextState>", where
        *  currentState and nextState are the state mneumonics.
        *  PRECONDITION: Pane sdPane exists, and State state exists.
        *  POSTCONDITION: Each transition label is initialized and added to the Pane but is not visible yet.
        */
    private void setLabelIds(Pane sdPane){
        for(State state : stateList.values()){
            String [] nextStates = getTransitionState(state);
            for(int i=0; i<nextStates.length; i++){
                if(!nextStates[i].equalsIgnoreCase("halt")){
                    String id = state.getStateMnemonic() + "To" + nextStates[i];
                    if(((Label) sdPane.lookup("#"+id))==null){
                        Label transLabel = new Label("");
                        transLabel.setId(id);
                        sdPane.getChildren().add(transLabel);
                    }
                }
            }
        }    
    }
    
    /*
        *  This method clears the Pane of all nodes. It is used to "reset" the state diagram.
        *  PRECONDITION: Pand sdPane exists.
        *  POSTCONDITION: The Pane is cleared of all nodes.
        */
    public void clearPane(Pane sdPane){
        sdPane.getChildren().clear();
    }
}
