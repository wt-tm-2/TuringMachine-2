
package TuringMachineFX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * SourceView is the class that creates and manages a separate JavaFX
 * stage that displays source code and any syntax error messages. The class
 * encapsulates the JavaFX components that make up the source view window and
 * the methods that manage these components. This includes loading the contents
 * of a file into the source code view, displaying any syntax errors, highlighting
 * the current source code line being executed, and automatically scrolling to that 
 * highlighted line.
 * 
 * The class currently expects an FXML file named 'SourceView.fxml' containing
 * a WebView and TextArea with fxid's of 'sourceCodeView' and 'syntaxErrorView'
 * respectively to be available as project resources. Another requirement is
 * an HTML file named 'sourceCodeView.html' in the resources package.
 * 
 * @author Michael Johnson
 */
public class SourceView {
    
    private final String fxmlResourcePath = "SourceView.fxml";
    
    private final Stage sourceViewStage;
    private final Scene sourceViewScene;
    private final WebView sourceCodeView;
    private final WebEngine sourceCodeViewWebEngine;
    private final TextArea syntaxErrorView;
    private final String sourceCodeHTML;
    private String currentSourceCodeHTML;
    private boolean isOpen = false;
    
    public SourceView() throws IOException {
        /* Initialize the Source Code View Window */
        Parent sourceViewRoot = FXMLLoader.load(getClass().getResource(fxmlResourcePath));
        sourceViewScene = new Scene(sourceViewRoot);
        sourceViewStage = new Stage();
        sourceViewStage.setOnHiding((event) -> {
            isOpen = false;
        });
        sourceViewStage.setScene(sourceViewScene);
        sourceCodeView = (WebView) sourceViewScene.lookup("#sourceCodeView"); 
        sourceCodeViewWebEngine = sourceCodeView.getEngine();
        syntaxErrorView = (TextArea) sourceViewScene.lookup("#syntaxErrorView");
        syntaxErrorView.setWrapText(true);
        isOpen = true;
        
        /* Read in the HTML that will be used to display source files */
        InputStream inputStream =  getClass().getClassLoader().getResourceAsStream(
                "Resources/sourceCodeView.html");
        try(Scanner s = new Scanner(inputStream)) { 
            sourceCodeHTML = s.useDelimiter("\\A").next();
        }
    }
    
    /**
     * Call the show() method on the JavaFX stage causing the SourceView window
     * to appear on top of the main window.
     */
    public void show() {
        sourceViewStage.show();
    }
    
    /**
     * 
     * @return a boolean indicating whether the SourceView window is open.
     */
    public boolean isOpen() {
        return isOpen;
    }
    
    /**
     * Add a source code file as content to the SourceView.
     * 
     * @param sourceFile A File object which points to the source code that will
     *                   be loaded
     * @throws FileNotFoundException if HTML for source content isn't found
     */
    public void addContent(File sourceFile) throws FileNotFoundException {
        int lineCount = 0;
        Scanner scanner = new Scanner(sourceFile);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            lineCount++;
            sb.append("<span id=\"").append(lineCount).append("\">");
            sb.append(scanner.nextLine());
            sb.append("</span>");
        }

        currentSourceCodeHTML = sourceCodeHTML.replaceFirst("<!-- user src -->", 
                sb.toString());
        sourceCodeViewWebEngine.loadContent(currentSourceCodeHTML);
        sourceViewStage.setTitle(sourceFile.getName());
    }
    
    /**
     * Highlight the given line number. For our Turing Machine project, this is
     * used to indicate the current line of source code being executed.
     * 
     * @param lineNumber the line number in the file that should be highlighted
     */
    public void highlight(int lineNumber) {
        String idLine = "id=\"" + lineNumber + "\"";
        String highlightStyle = "style=\"background-color:blue;color:white;\"";
        currentSourceCodeHTML = currentSourceCodeHTML.replaceFirst(highlightStyle, "");
        currentSourceCodeHTML = currentSourceCodeHTML.replaceFirst(idLine, idLine + " " + highlightStyle);
        
        /* Listen for the worker to finish loading the new HTML and CSS that highlights a line */
        ChangeListener<Worker.State> changeListener = new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> 
                    observable, Worker.State oldValue, Worker.State newValue) {
                if( newValue != Worker.State.SUCCEEDED ) {
                    return;
                }
                /* When the worker is finished, scroll the highlighted line into view and remove this listener */
                scrollSourceView(lineNumber);
                sourceCodeViewWebEngine.getLoadWorker().stateProperty().removeListener(this);
            }
        };
        sourceCodeViewWebEngine.getLoadWorker().stateProperty().addListener(changeListener);
        sourceCodeView.getEngine().loadContent(currentSourceCodeHTML);
    }
           
    /*
     * Scroll the SourceView window to display the given line number in about
     * the middle of the screen.
     */
    private void scrollSourceView(int lineNumber) {
        String jsAutoScroll = "var ele = document.getElementById(\"" + lineNumber + "\");"
                + "document.body.scrollTop = ele.offsetTop - 150;";
        sourceCodeViewWebEngine.executeScript(jsAutoScroll);
    }
    
    /**
     * Display any syntax errors to the TextArea of the SourceView window.
     * 
     * @param syntaxErrorText contains the syntax errors
     */
    public void setSyntaxError(String syntaxErrorText) {
       syntaxErrorView.setText(syntaxErrorText);
    }
}
