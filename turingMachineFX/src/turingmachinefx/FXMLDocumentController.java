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
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ChoiceBox;
import javafx.concurrent.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import StateDiagram.StateDiagramController;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import parser.ParserException;

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Pane sdPane;
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
    private TextField input1;
    @FXML
    private TextField input2;
    @FXML
    private TextField tape;
    @FXML
    private TextField tape2;
    @FXML
    private TextField tape3;
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
    private Label currentTape;
    @FXML
    private ChoiceBox speed;
    @FXML
    private Scene sourceViewScene;
    @FXML
    private Stage sourceViewStage;
    @FXML
    private WebView sourceCodeView;
    @FXML
    private TextArea syntaxErrorView;
    private String sourceCodeHtml;
    private boolean sourceViewWindowOpen = false;
    private TMController controller = new TMController();
    private File currentFile;
    private boolean killThread = false;
    private StateDiagramController sdController; 
    FileChooser fileChooser = new FileChooser();
    
    
    @FXML
    private void handleLoadButtonAction(ActionEvent event) {
        controller.resetData();
        Node node = (Node) event.getSource();
        currentFile = fileChooser.showOpenDialog(node.getScene().getWindow());
        startSourceView();
        loadFile();
        sdController.clearPane(sdPane);
        tape.setText(input.getText());
        tape2.setText(input1.getText());
        tape3.setText(input2.getText());
        startButton1.setDisable(false);
        stepButton1.setDisable(false);
        stopButton1.setDisable(false);
        input.setEditable(false);
        loadButton1.setDisable(false);
        setNextState();
        sdController.drawStateDiagram(sdPane);
        
    }
    
    @FXML
    private void loadFile(){
        try {
            controller.loadData(input.getText(),input1.getText(),input2.getText(),initialState.getText(),currentFile.getPath());
            syntaxErrorView.setText(currentFile.getName() + " compiled successfully.");
            setInitialState();
            sdController = new StateDiagramController(controller.getStateList());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserException ex) {
            syntaxErrorView.setText(ex.getMessage());
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
        currentTape.setText(String.valueOf(controller.getCT()));
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
                                    currentTape.setText(String.valueOf(controller.getCT()));
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
    
    private void startSourceView() {
        try {
            if (!sourceViewWindowOpen) {
                Parent sourceViewRoot = FXMLLoader.load(getClass().getResource("SourceView.fxml"));
                sourceViewScene = new Scene(sourceViewRoot);
                sourceViewStage = new Stage();
                sourceViewStage.setOnHiding((event) -> {
                    sourceViewWindowOpen = false;
                });
                sourceViewStage.setScene(sourceViewScene);
                sourceCodeView = (WebView) sourceViewScene.lookup("#sourceCodeView"); 
                syntaxErrorView = (TextArea) sourceViewScene.lookup("#syntaxErrorView");
                syntaxErrorView.setWrapText(true);
                sourceViewStage.show();
                sourceViewWindowOpen = true;
            }
                writeSourceLines();
                sourceViewStage.setTitle(currentFile.getName());

        } catch(IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeSourceLines() throws FileNotFoundException {
        int lineCount = 0;
        Scanner scanner = new Scanner(currentFile);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            lineCount++;
            sb.append("<span id=\"").append(lineCount).append("\">");
            sb.append(scanner.nextLine());
            sb.append("</span>");
        }

        InputStream inputStream =  getClass().getClassLoader().getResourceAsStream(
                "resources/sourceCodeView.html");
        try(Scanner s = new Scanner(inputStream)) { 
            sourceCodeHtml = s.useDelimiter("\\A").next();
        }

        sourceCodeHtml = sourceCodeHtml.replaceFirst("<!-- user src -->", sb.toString());
        sourceCodeView.getEngine().loadContent(sourceCodeHtml);
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
        
        int lineNumber = controller.getNextTransition().getLineNumber();
        highlight(lineNumber);
    }
    
    private void highlight(int lineNumber) {
        String idLine = "id=\"" + lineNumber + "\"";
        String highlightStyle = "style=\"background-color:blue;color:white;\"";
        sourceCodeHtml = sourceCodeHtml.replaceFirst(highlightStyle, "");
        sourceCodeHtml = sourceCodeHtml.replaceFirst(idLine, idLine + " " + highlightStyle);
        sourceCodeView.getEngine().getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> observable, 
                        Worker.State oldValue, Worker.State newValue) -> {
            if( newValue != Worker.State.SUCCEEDED ) {
                return;
            }
            scrollSourceView(lineNumber);
        });
        sourceCodeView.getEngine().loadContent(sourceCodeHtml);
    }
                
    private void scrollSourceView(int lineNumber) {
        String jsAutoScroll = "var ele = document.getElementById(\"" + lineNumber + "\");"
                + "document.body.scrollTop = ele.offsetTop - 150;";
        sourceCodeView.getEngine().executeScript(jsAutoScroll);
    }
  
    private void setInitialState() {
        String[] values = controller.getData();
        initialState.setText(values[0]);
    }
    
/**
 * Gets the value of the tape stored in the controller method and stores it in the
 * tape field in the GUI.
 */
    @FXML
    public void setTape(){
        Vector<Character> tapeVector = controller.getTape1();
        String newTape = new String();
        for(int i = 0; i < tapeVector.size(); i++){
            newTape = newTape + tapeVector.get(i);
        }
        tape.setText(newTape);
        tapeVector = controller.getTape2();
        newTape ="";
        for(int i = 0; i < tapeVector.size(); i++){
            newTape = newTape + tapeVector.get(i);
        }
        tape2.setText(newTape);
        tapeVector = controller.getTape3();
        newTape ="";
        for(int i = 0; i < tapeVector.size(); i++){
            newTape = newTape + tapeVector.get(i);
        }
        tape3.setText(newTape);
        
    }
    
    public void halt(){
        startButton1.setDisable(true);
        stepButton1.setDisable(true);
        stopButton1.setDisable(true);
   }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sdPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
    }    

    
}
