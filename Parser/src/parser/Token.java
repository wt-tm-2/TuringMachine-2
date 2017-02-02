
package parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the possible tokens in the TM simulator language. Provides regular
 * expression matching to verify lexemes are a given token.
 * 
 * @author Michael Johnson
 */
public class Token {
    /**
     * Turing Machine state token
     */
    public static final int STATE = 0;
    
    /**
     * Symbol to read/write token
     */
	public static final int SYMBOL = 1;
    
    /**
     * Direction to move token
     */
	public static final int DIRECTION = 2;
    
    /**
     * Source code comment character
     */
    public static final int COMMENT = 3;
    
    /* Map tokens to their regular expression pattern */
    private static final Map<Integer, String> TOKEN_REGEX;
    static {
        TOKEN_REGEX = new HashMap<>();
        TOKEN_REGEX.put(STATE, "\\w*");                // Match alphanumeric and _
        TOKEN_REGEX.put(SYMBOL, "\\p{Print}{1}");     // Match any one printable ASCII character
        TOKEN_REGEX.put(DIRECTION, "(r|R|l|L|n|N)");   // Match either right, left, or no direction
        TOKEN_REGEX.put(COMMENT, "\\s*#");           // Match white spaced followed by comment char
    }

    /**
     * Tests whether the lexeme matches the expected token.
     * 
     * @param token  category the lexeme is expected to belong to (STATE, SYMBOL or DIRECTION) 
     * @param lexeme the lexeme to verify
     * @return       true if the lexeme is in the token category, otherwise false 
     */
    public static boolean lexemeMatches(int token, String lexeme) {
        return lexeme.matches(TOKEN_REGEX.get(token));
    }
}
