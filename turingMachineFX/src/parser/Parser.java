
package parser;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Parses a Turing Machine simulator source file and produces a list of 
 * machine states.
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

    /**
     * Map that stores the list of states for the Turing Machine keyed by
     * their mnemonic.
     */
    private static HashMap<String, State> stateList = new HashMap<>();
    private static int lineCount = 0;  // keep track of what line we are in the file
    private static StringBuilder syntaxErrors = new StringBuilder();
    
    /* The expected sequence of tokens */
    private static final int[] TOKEN_SEQUENCE = {
        Token.STATE, Token.SYMBOL, Token.SYMBOL, Token.DIRECTION, Token.STATE,
        Token.TAPE
    };
    private static final int tokensLength = TOKEN_SEQUENCE.length;
    
    /**
     * Parses the source file and produces the state list output.
     * @param filePath a path to the source file
     * @return a map containing the Turing Machine states keyed by their mnemonic
     * @throws FileNotFoundException 
     * @throws ParserException
     */
    public static HashMap<String, State> parseSourceFile(String filePath) throws FileNotFoundException, ParserException {
        Scanner sourceFile = new Scanner(new File(filePath));

        while (sourceFile.hasNextLine()) {
            lineCount++;
            parseSourceLine(sourceFile.nextLine());
        }
        sourceFile.close();
        
        if (syntaxErrors.length() > 0) {
            throw new ParserException(syntaxErrors.toString());
        }
        
        return stateList;
    }
    
    /*
     Parse a single source file line
    */
    private static void parseSourceLine(String sourceLine) {
        if (isCommentLine(sourceLine) | isBlankLine(sourceLine)) {
            return;
        }
        boolean parseError = false;
        String[] lexemes = sourceLine.split("\\s+");
        for (int i = 0; i < tokensLength - 1; i++) {
            if (!Token.lexemeMatches(TOKEN_SEQUENCE[i], lexemes[i])) {
                printParserError(TOKEN_SEQUENCE[i], lexemes[i]);
                parseError = true;
                break;
            }
        }
        /* Check for the tape switch if any */
        if (lexemes.length == tokensLength) {
            if (!Token.lexemeMatches(TOKEN_SEQUENCE[tokensLength - 1], lexemes[tokensLength - 1])) {
                printParserError(TOKEN_SEQUENCE[tokensLength - 1], lexemes[tokensLength -1]);
                parseError = true;
            }
        } else {
            lexemes = Arrays.copyOf(lexemes, tokensLength);
            lexemes[tokensLength - 1] = "_";
        }
        if (!parseError) {
            addStateTransition(lexemes);
        }
    }
    
    /*
     Check if the source file line is a comment line
    */
    private static boolean isCommentLine(String sourceLine) {
        return Token.lexemeMatches(Token.COMMENT, sourceLine);
    }
    
    /*
     Check if the source file line is all whitespace or the empty string
    */
    private static boolean isBlankLine(String sourceLine) {
        return sourceLine.matches("(^\\s+$|^$)");
    }
    
    /*
     Adds the state transition from the source line to the state it belongs to.
     Creates a new state if the state isn't found in the state list.
    */
    private static void addStateTransition(String[] lexemes) {
        State currentState = stateList.get(lexemes[0]);
        if (currentState == null) {
            currentState = new State(lexemes[0]);
            if (stateList.isEmpty()) {
                currentState.setIsInitialState(true);
            }
            stateList.put(lexemes[0], currentState);
        }
        currentState.addTransition(new Transition(lexemes[1], lexemes[2],
                lexemes[3], lexemes[4], lexemes[5], lineCount));
        
    }
    
    /*
     Place holder method to print a simple parser error. This will be made
     more sophisticated in the future.
    */
    private static void printParserError(int token, String lexeme) {
        String error = "Syntax Error Line " + lineCount +": expected token " +
                token + " but got: " + lexeme;
        syntaxErrors.append(error);
        syntaxErrors.append('\n');
    }
}
