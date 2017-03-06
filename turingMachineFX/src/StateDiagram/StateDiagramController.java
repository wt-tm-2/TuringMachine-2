/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateDiagram;

import Controller.TMController;
import java.util.HashMap;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import parser.State;

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
        {150, 50},
        {250, 50},
        {50, 150},
        {150, 150},
        {250, 150},
        {50, 250},
        {150, 250},
        {250, 250}
    };
    public static void drawStateDiagram(StackPane sdPane){
        int numStates = TMController.getSize();
        Circle [] stateNodes = new Circle[numStates];
        
        for(int i=0; i<numStates; i++){
            stateNodes[i] = new Circle(25, Color.RED);
            stateNodes[i].relocate(positions[i][0], positions[i][1]);
            //stateNodes[i].setCenterX(positions[i][0]);
            //stateNodes[i].setCenterY(positions[i][1]);
            stateNodes[i].setRadius(25); // may not be needed
            sdPane.getChildren().add(stateNodes[i]);
        }
    }
}
