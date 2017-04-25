
package Parser;

/**
 * ParserException is used in the Parser class when a syntax error is encountered.
 * The caller can decided what to do with the exception if parsing the source file
 * throws this exception. 
 * 
 * @author Michael Johnson
 */
public class ParserException extends Exception {
    
    public ParserException(String message) {
        super(message);
    }
}
