/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turingmachinefx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Controller.TMController;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.concurrent.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Canvas sdCanvas;
    @FXML
    private Button stepButton;
    @FXML
    private TextField currentState;
    @FXML
    private TextField currentSymbol;
    @FXML
    private TextField newSymbol;
    @FXML
    private TextField direction;
    @FXML
    private TextField newState;
    @FXML
    private TextField initialState;
    @FXML
    private TextField input;
    @FXML
    private TextField tape;
    @FXML
    private Button startButton1;
    @FXML
    private Button stepButton1;
    @FXML
    private Button stopButton1;
    @FXML
    private Button resetButton1;
    @FXML
    private Button loadButton1;
    @FXML
    private Label instructionCount;
    @FXML
    private ChoiceBox speed;
    private TMController controller = new TMController();
    private File currentfile;
    private boolean killThread = false;
    
    FileChooser fileChooser = new FileChooser();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        currentState.setText("HI");
    }
    
    @FXML
    private void handleLoadButtonAction(ActionEvent event) {
        controller.resetData();
        Node node = (Node) event.getSource();
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        currentfile = file;
        startSourceView(file);
        loadFile();
        tape.setText(input.getText());
        startButton1.setDisable(false);
        stepButton1.setDisable(false);
        stopButton1.setDisable(false);
        input.setEditable(false);
        loadButton1.setDisable(true);
        setNextState();
        
    }
    
    @FXML
    private void loadFile(){
        try {
            controller.loadData(input.getText(),initialState.getText(),currentfile.getPath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        controller.resetData();
        loadFile();
        input.setEditable(true);
        tape.setText(input.getText());
        startButton1.setDisable(false);
        stepButton1.setDisable(false);
        stopButton1.setDisable(false);
        instructionCount.setText("0");
        loadButton1.setDisable(false);
}
    @FXML
    private void handleStepButtonAction(ActionEvent event){
        int x = controller.step();
        setTape();
        setNextState();
        instructionCount.setText(String.valueOf(controller.getIC()));
        if (x == 1){
            halt();
        }
    }
    /**
     * This method runs the program by creating a thread which continually
     * calls the step method in the controller. It gets the speed form the drop
     * down box on the UI and then creates a delay (in milliseconds). The run
     * is halted when the stop button is clicked which changes a global variable
     * that kicks the thread out of the loop.
     * @param event
     * @throws InterruptedException 
     */
    @FXML
    private void handleStartButtonAction(ActionEvent event) throws InterruptedException{
        killThread = false;
        stepButton1.setDisable(true);
        resetButton1.setDisable(true);
        loadButton1.setDisable(true);
        startButton1.setDisable(true);

        int delay = 0;
        int st = 0;
        String s = (String) speed.getValue();
        switch (s){
            case "Instant": delay = 0;
                break;
            case "100%": delay = 500;
                break;
            case "50%": delay = 1000;
                break;
            case "25%": delay = 2000;
        }
        final int delay2 = delay;
            new Thread(){
                public void run(){
                            int st = 0;
                            while (st != 1 && !killThread){
                            st = controller.step();
                
                            Platform.runLater(new Runnable() {

                            public void run() {
                                    setTape();
                                    setNextState();
                                    instructionCount.setText(String.valueOf(controller.getIC()));
                            }
                            });
                                try {
                                    TimeUnit.MILLISECONDS.sleep(delay2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                            if (!killThread){    
                            halt();
                            }
                        stepButton1.setDisable(false);
                        resetButton1.setDisable(false);
                        loadButton1.setDisable(false);
                        startButton1.setDisable(false);
            }  
            }.start();
    }
    @FXML
    private void handleStopButtonAction(ActionEvent event){
        killThread = true;
    }
    
    private void startSourceView(File sourceFile) {
        try {
            Parent sourceViewRoot = FXMLLoader.load(getClass().getResource("SourceView.fxml"));
            Scene sourceViewScene = new Scene(sourceViewRoot);
            Stage sourceViewStage = new Stage();
            sourceViewStage.setScene(sourceViewScene);
            writeSourceLines(sourceFile, (TextArea) sourceViewScene.lookup("#sourceCodeView"));
            sourceViewStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeSourceLines(File sourceFile, TextArea sourceView) throws FileNotFoundException {
        Scanner scanner = new Scanner(sourceFile);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            sb.append('\n');
        }
        sourceView.setEditable(false);
        sourceView.setFont(new Font("FreeMono", 16));
        sourceView.setText(sb.toString());
    }
    
    /**
 * Gets data from controller and populates the boxes on the GUI that explain the
 * next step on the turing machine
 */
    @FXML
    public void setNextState(){
        String[] values = controller.getData();
        currentState.setText(values[0]);
        currentSymbol.setText(values[1]);
        newSymbol.setText(values[2]);
        direction.setText(values[3]);
        newState.setText(values[4]);
        
    }
/**
 * Gets the value of the tape stored in the controller method and stores it in the
 * tape field in the GUI.
 */
    @FXML
    public void setTape(){
        Vector<Character> tapeVector = controller.getTape();
        String newTape = new String();
        for(int i = 0; i < tapeVector.size(); i++){
            newTape = newTape + tapeVector.get(i);
        }
        tape.setText(newTape);
    }
    
    public void halt(){
        startButton1.setDisable(true);
        stepButton1.setDisable(true);
        stopButton1.setDisable(true);
   }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext gc = sdCanvas.getGraphicsContext2D();
        drawStateDiagram(gc);
    }    

    private void drawStateDiagram(GraphicsContext gc) {
        gc.strokeOval(50, 50, 100, 100);
    }
    
}
