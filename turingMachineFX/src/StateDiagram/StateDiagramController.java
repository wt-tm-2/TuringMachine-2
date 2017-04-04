/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateDiagram;

import Controller.TMController;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import parser.State;
import parser.Transition;

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class StateDiagramController {
    private static HashMap<String, State> stateList;
    
    public StateDiagramController(HashMap<String, State> sl){
        stateList = sl;
    }
    
    private static double positions [][] = {
        {50, 50 },
        {50, 150},
        {250, 150},
        {250, 50},
        {150, 100},
        {150, 200},
        {50, 250},
        {250, 250},
        {150, 300}
    };
    public void drawStateDiagram(Pane sdPane){
        Group group = new Group();
        
        StackPane sPane = new StackPane();
        
        int numStates = TMController.getSize();
        Circle stateNodes = new Circle();
        int i=0;
        
        for(State state  : stateList.values()){
            Label label = new Label(state.getStateMnemonic());
            label.setTextFill(Color.BLACK);
            state.setGraphicAttributes(positions[i][0], positions[i][1], 25);
            state.getStateGraphic().setFill(Color.BLUE);
            
            String [] nextStates = getTransitionState(state);
            if(nextStates[0]!=null){
                for(String nState : nextStates){
                    if(!nState.equalsIgnoreCase("halt")){
                        Line line = new Line();
                        connectCircles(state.getStateGraphic(),stateList.get(nState).getStateGraphic(),line);
                        sdPane.getChildren().add(line);
                    }
                }
            }
            label.setLayoutX(state.getStateGraphic().getCenterX() - state.getStateGraphic().getRadius());
            label.setLayoutY(state.getStateGraphic().getCenterY());
            sdPane.getChildren().addAll(state.getStateGraphic(),label);
            i++;
        }
    }
    
    private String [] getTransitionState(State state){
        ArrayList<Transition> trans = state.getStateTransitions();
        String [] newStates = new String[trans.size()];
        for(int i=0; i<trans.size(); i++){
            newStates[i] = trans.get(i).getNewState();
        }
        return newStates;
    }
    
    private void connectCircles(Circle c1, Circle c2, Line line){
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());
        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
    }
    
    public void clearPane(Pane sdPane){
        sdPane.getChildren().clear();
    }
}
