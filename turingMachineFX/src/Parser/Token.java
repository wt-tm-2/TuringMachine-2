
package Parser;

import java.util.HashMap;

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
    
    /**
     *  Tape to switch to
     */
    public static final int TAPE = 4;
    
    /* Map tokens to their regular expression pattern */
    private static final HashMap<Integer, String> TOKEN_REGEX;
    static {
        TOKEN_REGEX = new HashMap<>();
        TOKEN_REGEX.put(STATE, "\\w*");                // Match alphanumeric and _
        TOKEN_REGEX.put(SYMBOL, "\\p{Print}{1}");     // Match any one printable ASCII character
        TOKEN_REGEX.put(DIRECTION, "(r|R|l|L|n|N)");   // Match either right, left, or no direction
        TOKEN_REGEX.put(COMMENT, "\\s*#.*");         // Match white spaced followed by comment char
        TOKEN_REGEX.put(TAPE, "(_|1|2|3)");  // Match 1, 2 or 3 for tape index or _ for current tape
    }
    
    /* Description of  tokens. Used for descriptive syntax errors. */
    public static final String[] TOKEN_DESCRIPTIONS = {
        "state mnemonic",
        "read or write symbol (an ASCII character)",
        "move direction (l or L for left, r or R for right, n or N for no direction)",
        "comment character '#'",
        "tape switch ('_' for current tape, or '1', '2', or '3')"
    };

    /**
     * Tests whether the lexeme matches the expected token.
     * 
     * @param token  category the lexeme is expected to belong to (STATE, SYMBOL, DIRECTION, etc) 
     * @param lexeme the lexeme to verify
     * @return       true if the lexeme is in the token category, otherwise false 
     */
    public static boolean lexemeMatches(int token, String lexeme) {
        return lexeme.matches(TOKEN_REGEX.get(token));
    }
}
