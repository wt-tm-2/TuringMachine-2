
package parser;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * Assumptions for the syntax of the TM simulator language:
 * 
 * 1. Comments begin with the '#' character, however this conflicts with the
 *    read and write symbols being any printable ASCII character.
 * 
 * 2. Each line defines a single state transition in the form of the following tuple:
 *    (state, read symbol, write symbol, move direction, new state)
 * 
 * @author Michael Johnson
 */
public class Parser {

    protected ArrayList<State> stateList;
    private static final int[] TOKEN_SEQUENCE = {
        Token.STATE, Token.SYMBOL, Token.SYMBOL, Token.DIRECTION, Token.STATE
    };
    
    public static void parseSourceFile(String filePath) throws FileNotFoundException {
        Scanner sourceFile = new Scanner(new File(filePath));

		while (sourceFile.hasNextLine()) {
            for (int token : TOKEN_SEQUENCE) {
                String lexeme = sourceFile.next();
                if (Token.lexemeMatches(Token.COMMENT, lexeme)) {
                    sourceFile.nextLine();
                    break;
                }
                if (Token.lexemeMatches(token, lexeme)) {
                    System.out.println("Found token " + token + ": " + lexeme);
                } else {
                    System.out.println("Expected token " + token + " but got: " + lexeme);
                }
            }

		}
		sourceFile.close();
    }
    
}
