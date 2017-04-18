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
 * @author WTSTUDENTS\zg977947
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
                            drawLoop(transitionLabel(state, nState),state.getStateGraphic(),yValue,sdPane);
                        }
                        else{
                            connectCircles(transitionLabel(state, nState),state.getStateGraphic(),stateList.get(nState).getStateGraphic(),sdPane);
                        }
                    }
                }
            }
        }
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
    
    
    private void connectCircles(String label, Circle c1, Circle c2, Pane sdPane){
        Line line = new Line();
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());
        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
        sdPane.getChildren().add(line);
        drawLabel(c1, c2, label, sdPane);
    }
    
    private String transitionLabel(State state, String nState){
        String lbl = "";
        for(int j = 0; j < state.getStateTransitions().size(); j++){
            if(state.getStateTransitions().get(j).getNewState().equals(nState)){
                lbl += "(" +state.getStateTransitions().get(j).getReadSymbol() + ", " +
                        state.getStateTransitions().get(j).getWriteSymbol() + ", " +
                        state.getStateTransitions().get(j).getDirection() + ")";
            }
        }
        return lbl;
    }
    
    private void drawLoop(String label, Circle c1, int y, Pane sdPane){
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
        drawArcLabel(c1,label,sdPane);
        
    }
    
    private void drawLabel(Circle c1, Circle c2, String transition, Pane sdPane){
        double angle;
        if(c2.getCenterX()==c1.getCenterX())
            angle = 0;
        else
            angle = Math.toDegrees(Math.atan((c2.getCenterY()-c1.getCenterY())/(c2.getCenterX()-c1.getCenterX())));
        Label trans = new Label();       
        trans.setText(transition);
        trans.relocate((c1.getCenterX()+c2.getCenterX())/2, (c1.getCenterY()+c2.getCenterY())/2);
        
        trans.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        trans.getTransforms().add(new Rotate(angle,0,0));
        sdPane.getChildren().add(trans);
    }
    
    private void drawArcLabel(Circle c1, String transition, Pane sdPane){
        double y;
        if(c1.getCenterY() > 300)
            y = 52;
        else
            y = -65;
        Label trans = new Label();
        trans.setText(transition);
        trans.relocate(c1.getCenterX(), c1.getCenterY()+y);
        trans.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        sdPane.getChildren().add(trans);
    }
       
    public void clearPane(Pane sdPane){
        sdPane.getChildren().clear();
    }
}
