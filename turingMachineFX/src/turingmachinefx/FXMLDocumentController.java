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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class FXMLDocumentController implements Initializable {
    
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
    private TMController controller;
    
    FileChooser fileChooser = new FileChooser();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        currentState.setText("HI");
    }
    
    @FXML
    private void handleLoadButtonAction(ActionEvent event) {
        Node node = (Node) event.getSource();
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        startSourceView(file);
        
        /*try {
            controller.loadData(input.getText(),initialState.getText());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tape.setText(input.getText());
        startButton1.setDisable(false);
        startButton1.setDisable(false);
        stepButton1.setDisable(false);
        stopButton1.setDisable(false);
        input.setEditable(false);
        loadButton1.setDisable(true);
        setNextState();
        */
    }
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        controller.resetData();
        input.setEditable(true);
        tape.setText(input.getText());
        startButton1.setDisable(true);
        stepButton1.setDisable(true);
        stopButton1.setDisable(true);
        instructionCount.setText("0");
        loadButton1.setDisable(false);
}
    @FXML
    private void handleStepButtonAction(ActionEvent event){
        controller.step();
        setTape();
        setNextState();
        instructionCount.setText(String.valueOf(controller.getIC()));
    }
    
    private void startSourceView(File sourceFile) {
        try {
            Parent sourceViewRoot = FXMLLoader.load(getClass().getResource("SourceView.fxml"));
            Scene sourceViewScene = new Scene(sourceViewRoot);
            Stage sourceViewStage = new Stage();
            sourceViewStage.setScene(sourceViewScene);
            writeSourceLines(sourceFile, (Label) sourceViewScene.lookup("#sourceCodeView"));
            sourceViewStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeSourceLines(File sourceFile, Label sourceView) throws FileNotFoundException {
        Scanner scanner = new Scanner(sourceFile);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            sb.append('\n');
        }
        sourceView.setText(sb.toString());
    }
    
    /**
 * Gets data from controller and populates the boxes on the GUI that explain the
 * next step on the turing machine
 */
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
    public void setTape(){
        Vector<Character> tapeVector = controller.getTape();
        String newTape = new String();
        for(int i = 0; i < tapeVector.size(); i++){
            newTape = newTape + tapeVector.get(i);
        }
        tape.setText(newTape);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
