/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateDiagram;

import Controller.TMController;
import javafx.scene.shape.Circle;
import parser.State;

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class StateDiagramController {
    private static int numStates = TMController.getSize();
    private static Circle [] stateNodes = new Circle[numStates];
    
    static double positions [][] = {
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
    public static void drawStateDiagram(){
        for(int i=0; i<numStates; i++){
            stateNodes[i].setCenterX(positions[i][0]);
            stateNodes[i].setCenterY(positions[i][1]);
            stateNodes[i].setRadius(100);
        }
    }
}
