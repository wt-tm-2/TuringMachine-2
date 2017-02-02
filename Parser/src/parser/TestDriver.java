
package parser;

import java.io.FileNotFoundException;

/**
 * Test 
 * 
 * @author Michael Johnson
 */
public class TestDriver {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Parser.parseSourceFile("src/test1.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
