
package Parser;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Parser contains a method for reading a Turing Machine simulator source file 
 * and producing a list of machine states.
 * 
 * Assumptions for the syntax of the TM simulator language:
 * 
 * 1. Comments begin with the '#' character.
 * 
 * 2. Each line defines a single state transition in the form of the following tuple:
 *    (state, read symbol, write symbol, move direction, new state, tape switch (optionally))
 * 
 * @author Michael Johnson
 */
public class Parser {

    /**
     * Map that stores the list of states for the Turing Machine keyed by
     * their mnemonic.
     */
    private static final HashMap<String, State> stateList = new HashMap<>();
    private static int lineCount;  // keep track of what line we are in the file
    private static final StringBuilder syntaxErrors = new StringBuilder();
    
    /* The expected sequence of tokens */
    private static final int[] TOKEN_SEQUENCE = {
        Token.STATE, Token.SYMBOL, Token.SYMBOL, Token.DIRECTION, Token.STATE,
        Token.TAPE
    };
    private static final int TOKENS_LENGTH = TOKEN_SEQUENCE.length;
    
    /**
     * Parses the source file and produces the state list output.
     * 
     * @param filePath a path to the source file
     * @return a map containing the Turing Machine states keyed by their mnemonic
     * @throws FileNotFoundException 
     * @throws ParserException
     */
    public static HashMap<String, State> parseSourceFile(String filePath) throws 
            FileNotFoundException, ParserException {
        Scanner sourceFile = new Scanner(new File(filePath));
        lineCount = 0;
        while (sourceFile.hasNextLine()) {
            lineCount++;
            parseSourceLine(sourceFile.nextLine());
        }
        sourceFile.close();
        
        /* When finished parsing, throw a parser exception if there are syntax errors */
        if (syntaxErrors.length() > 0) {
            throw new ParserException(syntaxErrors.toString());
        }
        
        return stateList;
    }
    
    /*
     * Parse a single source file line
     */
    private static void parseSourceLine(String sourceLine) {
        if (isCommentLine(sourceLine) | isBlankLine(sourceLine)) {
            return;
        }
        boolean parseError = false;
        String[] lexemes = sourceLine.split("\\s+");
        for (int i = 0; i < TOKENS_LENGTH - 1; i++) {
            if (!Token.lexemeMatches(TOKEN_SEQUENCE[i], lexemes[i])) {
                addParserError(TOKEN_SEQUENCE[i], lexemes[i]);
                parseError = true;
                break;
            }
        }
        /* Check for the tape switch if any */
        if (lexemes.length == TOKENS_LENGTH) {
            if (!Token.lexemeMatches(TOKEN_SEQUENCE[TOKENS_LENGTH - 1], lexemes[TOKENS_LENGTH - 1])) {
                addParserError(TOKEN_SEQUENCE[TOKENS_LENGTH - 1], lexemes[TOKENS_LENGTH -1]);
                parseError = true;
            }
        } else {
            lexemes = Arrays.copyOf(lexemes, TOKENS_LENGTH);
            lexemes[TOKENS_LENGTH - 1] = "_";
        }
        if (!parseError) {
            addStateTransition(lexemes);
        }
    }
    
    /*
     * Check if the source file line is a comment line
     */
    private static boolean isCommentLine(String sourceLine) {
        return Token.lexemeMatches(Token.COMMENT, sourceLine);
    }
    
    /*
     * Check if the source file line is all whitespace or the empty string
     */
    private static boolean isBlankLine(String sourceLine) {
        return sourceLine.matches("(^\\s+$|^$)");
    }
    
    /*
     * Adds the state transition from the source line to the state it belongs to.
     * Creates a new state if the state isn't found in the state list.
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
     * Adds a syntax error to the syntax error string builder. The string builder
     * is used to detail all the syntax errors found in the source file.
     */
    private static void addParserError(int token, String lexeme) {
        String error = "Syntax Error Line " + lineCount +": expected a " +
                Token.TOKEN_DESCRIPTIONS[token] + " but got: " + lexeme;
        syntaxErrors.append(error);
        syntaxErrors.append('\n');
    }
}
