
package parser;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

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

    protected static HashMap<String, State> stateList;
    private static boolean PARSER_ERROR = false;
    private static final int[] TOKEN_SEQUENCE = {
        Token.STATE, Token.SYMBOL, Token.SYMBOL, Token.DIRECTION, Token.STATE
    };
    
    public static void parseSourceFile(String filePath) throws FileNotFoundException {
        Scanner sourceFile = new Scanner(new File(filePath));

	while (sourceFile.hasNextLine()) {
            parseSourceLine(sourceFile.nextLine());
	}
	sourceFile.close();
    }
    
    private static void parseSourceLine(String sourceLine) {
        if (isCommentLine(sourceLine)) {
            return;
        }
        String[] lexemes = sourceLine.split("\\s+");
        for (int i = 0; i < TOKEN_SEQUENCE.length; i++) {
            if (!Token.lexemeMatches(TOKEN_SEQUENCE[i], lexemes[i])) {
                printParserError(TOKEN_SEQUENCE[i], lexemes[i]);
                PARSER_ERROR = true;
                break;
            }
        }
        if (!PARSER_ERROR) {
            addStateTransition(lexemes);
        }
    }
    
    private static boolean isCommentLine(String sourceLine) {
        return Token.lexemeMatches(Token.COMMENT, sourceLine);
    }
    
    private static void addStateTransition(String[] lexemes) {
        State currentState = stateList.get(lexemes[0]);
        if (currentState == null) {
            currentState = new State(lexemes[0]);
        }
        currentState.addTransition(new Transition(lexemes[1], lexemes[2],
                lexemes[3], lexemes[4]));
    }
    
    private static void printParserError(int token, String lexeme) {
        System.out.println("Expected token " + token + " but got: " + lexeme);
    }
}
