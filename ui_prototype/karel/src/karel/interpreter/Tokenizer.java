package karel.interpreter;

import java.util.ArrayList;
import java.util.Stack;

public class Tokenizer {
	
	private ArrayList<Token> tokens;
	private Stack<Integer> indentStack;

	public Tokenizer() {
		tokens = new ArrayList<Token>();
		indentStack = new Stack<Integer>();
		indentStack.push(0);
	}
	
	/** Tokenizes the given string. Adds each token to tokens.
	 * 
	 * @param text		text to be tokenized
	 * @return			the list of tokens
	 */
	public ArrayList<Token> tokenize(String text) throws InterpreterException {
        
            text = text.replaceAll("[ \t]*#[^\\n]*", "");    // replace all comments with an empty string
            String[] lines = text.split("\n");
            int numberOfLines = lines.length;
            int lineNum;

            for (lineNum = 1; lineNum <= numberOfLines; lineNum++ ) {

                String line = lines[lineNum - 1];

                if (!line.matches("[\\s]*")) {    // ignore empty lines and lines that contain only whitespace

                    determineIndent(line, lineNum);

                    String[] lineTokens = line.split("\\s+");
                    for (int i = 0; i < lineTokens.length; i++) {
                        String str = lineTokens[i];
                        if (!str.equals("")) {
                            determineToken(str, lineNum);
                        }
                    }
                    tokens.add(new Token(TokenType.NEWLINE, lineNum));
                }
            }

            while (indentStack.pop() > 0) {
                tokens.add(new Token(TokenType.DEDENT, lineNum));
            }

            return tokens;
    }
	
	/** Determines the tokens that the given string belongs to.
	 * 
	 * @param str				the string to be evaluated
	 */
	private void determineToken(String str, int lineNum) throws InterpreterException {
		
		switch(str) {
			case "def":
                        case "define":
				tokens.add(new Token(TokenType.DEF, lineNum));
				break;
                        case "def:":
                        case "define:":
                                throw new InterpreterException("Error on line " + lineNum + ": missing procedure name", lineNum);
			case "if":
				tokens.add(new Token(TokenType.IF, lineNum));
				break;
                        case "if:":
                                throw new InterpreterException("Error on line " + lineNum + ": missing condition", lineNum);
			case "else":
				tokens.add(new Token(TokenType.ELSE, lineNum));
				break;
			case "else:":
				tokens.add(new Token(TokenType.ELSE, lineNum));
				tokens.add(new Token(TokenType.COLON, lineNum));
				break;
			case "elif":
				tokens.add(new Token(TokenType.ELIF, lineNum));
				break;
                        case "elif:":
                                throw new InterpreterException("Error on line " + lineNum + ": missing condition", lineNum);
			case "while":
				tokens.add(new Token(TokenType.WHILE, lineNum));
				break;
                        case "while:":
                                throw new InterpreterException("Error on line " + lineNum + ": missing condition", lineNum);
			case "do":
				tokens.add(new Token(TokenType.DO, lineNum));
				break;
                        case "do:":
                                throw new InterpreterException("Error on line " + lineNum + ": missing integer literal", lineNum);
			case ":":
				tokens.add(new Token(TokenType.COLON, lineNum));
				break;
			default:
				
				int strLastIndex = str.length() - 1;
				
				if (str.matches("[a-zA-Z][a-zA-Z_]*:?")) {
					Test test;
					if (str.charAt(strLastIndex) == ':') {
						str = str.substring(0 , strLastIndex);
						if ((test = getTest(str)) != null) {
							tokens.add(new Token(TokenType.TEST, lineNum, test));
							tokens.add(new Token(TokenType.COLON, lineNum));
						}
						else {
							tokens.add(new Token(TokenType.NAME, lineNum, str));
							tokens.add(new Token(TokenType.COLON, lineNum));
						}
					}
					else {
						if ((test = getTest(str)) != null) {
							tokens.add(new Token(TokenType.TEST, lineNum, test));
						}
						else {
							tokens.add(new Token(TokenType.NAME, lineNum, str));
						}	
					}
				}
				else if (str.matches("[0-9]+:?")) {
					if (str.charAt(strLastIndex) == ':') {
						tokens.add(new Token(TokenType.INT_LIT, lineNum, str.substring(0, strLastIndex)));
						tokens.add(new Token(TokenType.COLON, lineNum));
					}
					else {
						tokens.add(new Token(TokenType.INT_LIT, lineNum, str));
					}
				}
				else {
					throw new InterpreterException("Error on line " + lineNum + ": unexpected token", lineNum);
				}
		}
	}
	
	
	private Test getTest (String str) {
		
		switch(str) {
		case "front_is_clear":
			return Test.FRONT_IS_CLEAR;
		case "front_is_blocked":
			return Test.FRONT_IS_BLOCKED;
		case "right_is_clear":
			return Test.RIGHT_IS_CLEAR;
		case "right_is_blocked":
			return Test.RIGHT_IS_BLOCKED;
		case "left_is_clear":
			return Test.LEFT_IS_CLEAR;
		case "left_is_blocked":
			return Test.LEFT_IS_BLOCKED;
		case "facing_north":
			return Test.FACING_NORTH;
		case "not_facing_north":
			return Test.NOT_FACING_NORTH;
		case "facing_east":
			return Test.FACING_EAST;
		case "not_facing_east":
			return Test.NOT_FACING_EAST;
		case "facing_south":
			return Test.FACING_SOUTH;
		case "not_facing_south":
			return Test.NOT_FACING_SOUTH;
		case "facing_west":
			return Test.FACING_WEST;
		case "not_facing_west":
			return Test.NOT_FACING_WEST;
		case "next_to_a_beeper":
			return Test.NEXT_TO_A_BEEPER;
		case "not_next_to_a_beeper":
			return Test.NOT_NEXT_TO_A_BEEPER;
		case "any_beepers_in_beeper_bag":
			return Test.ANY_BEEPERS_IN_BEEPER_BAG;
		case "no_beepers_in_beeper_bag":
			return Test.NO_BEEPERS_IN_BEEPER_BAG;
		default:
			return null;
		}
	}
	
	/** Determines the indentation level of the given line.
	 * 
	 * @param line		the line to be evaluated
	 * @return			the number of spaces of the indentation level
	 */
	private int getIndentationLevel(String line) {
		
		int indentationLevel = 0;
		int tabSpaces = 4;
		int lineLen = line.length();
		boolean lookingForWhitespace = true;
		
		for (int i = 0; i < lineLen && lookingForWhitespace ; i++ ) {
			if (line.charAt(i) == '\t') {
				/* replace tab so that the indentation level is a multiple of 4 */
				if (indentationLevel != 0 ) {
					indentationLevel += tabSpaces - (indentationLevel % tabSpaces);
				}
				else {
					indentationLevel = tabSpaces;
				}
			}
			else if ( line.charAt(i) == ' ') {
				indentationLevel += 1;
			}
			else {
				lookingForWhitespace = false;
			}
		}
		return indentationLevel;
	}
	
	/** Compares the indentation level of the given line with the values on indentStack.
	 *  Values on indentStack are strictly increasing from bottom to top.
	 *  
	 *  Adds an INDENT token to the tokens list if the given line has greater 
	 *  indentation than the previous line and pushes the given line's indentation 
	 *  value onto the stack. 
	 *  
	 *  If the given line has smaller indentation than the previous line, then for each
	 *  value on top of the stack greater than the given line's indentation, the value
	 *  is popped off the stack and a DEDENT token is added to the tokens list. If the
	 *  given line's indentation does not match any of the values on the stack, then
	 *  the line has inconsistent indentation and an exception is thrown.
	 *  
	 * @param line		the line to be compared
	 */
	private void determineIndent(String line, int lineNum) throws InterpreterException {
		int indentLevel = getIndentationLevel(line);
		int prevIndent = indentStack.peek();
		if (indentLevel > prevIndent) {
			indentStack.push(indentLevel);
			tokens.add(new Token(TokenType.INDENT, lineNum));
		}
		else if (indentLevel < prevIndent){
			while (indentLevel < indentStack.peek()) {
				indentStack.pop();
				tokens.add(new Token(TokenType.DEDENT, lineNum));
			}
			if (indentLevel != indentStack.peek()) {
				throw new InterpreterException("Error on line " + lineNum + ": inconsistent indentation" , lineNum);
			}
		}
	}
}