/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import parser.State;
import parser.Transition;

/**
 *
 * @author Zach Gutierrez
 */
public class StateDiagramController {
    private static HashMap<String, State> stateList;
    
    public StateDiagramController(HashMap<String, State> sl){
        stateList = sl;
    }
    
    private static double positions [][] = {
        {400, 100 },
        {600, 150},
        {800, 300},
        {600, 450},
        {400, 500},
        {200, 450},
        {100, 300},
        {200, 150},
        {150, 300}
    };
    public void drawStateDiagram(Pane sdPane){

        int i=0;
        for(State state : stateList.values()){
            state.setGraphicAttributes(positions[i][0], positions[i][1], 25);
            state.getStateGraphic().setFill(Color.AQUA);
            i++;
        }
        
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
        drawLabels(sdPane);
        for(State state: stateList.values()){
            Label label = new Label(state.getStateMnemonic());
            label.setTextFill(Color.BLACK);
            label.setLayoutX(state.getStateGraphic().getCenterX() - state.getStateGraphic().getRadius() +5);
            label.setLayoutY(state.getStateGraphic().getCenterY() - 8);
            sdPane.getChildren().addAll(state.getStateGraphic(),label);
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
    
    
    private void connectCircles(Circle c1, Circle c2, Pane sdPane){
        Line line = new Line();
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());
        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
        sdPane.getChildren().add(line);
    }

    
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
    
    private void drawArcLabel(Circle c1, Label trans){
        double y;
        if(c1.getCenterY() > 300)
            y = 52;
        else
            y = -65;
        trans.relocate(c1.getCenterX(), c1.getCenterY()+y);
        trans.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    private void drawLabels(Pane sdPane){
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
       
    public void clearPane(Pane sdPane){
        sdPane.getChildren().clear();
    }
}
