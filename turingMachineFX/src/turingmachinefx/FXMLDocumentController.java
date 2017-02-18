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

/**
 *
 * @author WTSTUDENTS\zg977947
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button stepButton;
    @FXML
    private TextField currentState;
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
