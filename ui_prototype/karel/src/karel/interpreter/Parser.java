package karel.interpreter;

import karel.interpreter.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Parser {
	
	//private String indentString = "";		// used to format indented test output
	
	private Interpreter interp;
	private String[] primitives = {"move", "turnleft", "pickbeeper", "putbeeper", "turnoff"};
	
	private ArrayList<Token> tokens;
	private ArrayList<Command> commands = new ArrayList<Command>();
	private HashMap<String, Integer> procHash = new HashMap<String, Integer>();		// hash table containing the starting index
																					// of all defined procedures
	private int index = 0;			// index of current token in token list
	private int commandIndex = 0;           // index of next command
	private Token token = null;		// next token in  token list

	public Parser (Interpreter interp) {
		this.interp = interp;
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public void parse(ArrayList<Token> tokens) throws InterpreterException {
		
		this.tokens = tokens;
		
		//System.out.println("Entering <start>");
		//indentString += "    ";
		start();
	}

	private void start() throws InterpreterException {
		
		getNextToken();
		
		while (token.getType() == TokenType.DEF) {
			//System.out.println(indentString + "Entering <funcdef>");
			//indentString += "    ";
			funcdef();
			getNextToken();
		}
		
		interp.setControlPointer(commandIndex);
		while (isStmt()) {
			//System.out.println(indentString + "Entering <stmt>");
			//indentString += "    ";
			stmt();
			getNextToken();
		}
                int lineNum = token.getLineNum();
                
                if (token.getType() == TokenType.INDENT) {
                    throw new InterpreterException("Error on line " + lineNum + ": unexpected indentation", lineNum);
                }
		
		if (token.getType() != TokenType.EOF){
			throw new InterpreterException("Error on line " + lineNum, lineNum);
		}
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <start>");
	}
	
	private void funcdef() throws InterpreterException {
	
		getNextToken();
                int lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.NAME){
			String name = token.getLexeme();
                        if (procHash.get(name) != null) {
                            throw new InterpreterException("Error on line " + lineNum + ": procedure has already"
                                    + " been defined.", lineNum);
                        }
                        for (int i = 0; i < primitives.length; i++) {
                            if (name.equals(primitives[i])) {
                                throw new InterpreterException("Error on line " + lineNum + ": procedure name cannot"
                                        + " be the same as a primitive instruction.", lineNum);
                            }
                        }
			procHash.put(name, commandIndex);
			
			getNextToken();
                        lineNum = token.getLineNum();
			
			if (token.getType() == TokenType.COLON){
				//System.out.println(indentString + "Entering <block>");
				//indentString += "    ";
				block();
                                
                                commands.add(new ReturnCommand(interp));
                                commandIndex++;
			} else {
                                
				throw new InterpreterException("Error on line " + lineNum + ": missing colon", lineNum);
                        }
		} else {
			throw new InterpreterException("Error on line " + lineNum + ": missing procedure name", lineNum);
                }
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <funcdef>");
	}
	
	private void stmt() throws InterpreterException {
            
                int lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.NAME){
			String name = token.getLexeme();

			// check if name is a primitive or a defined procedure
			if (!Arrays.asList(primitives).contains(name) && !procHash.containsKey(name)) {
				throw new InterpreterException("Error on line " + lineNum + ": procedure has not been defined", lineNum);
			}
			
			if (procHash.containsKey(name)) {
				commands.add(new CallCommand(interp, procHash.get(name), ++commandIndex, token.getLineNum()));
			}
			else {
				switch(name) {
				case "move":
					commands.add(new MoveCommand(interp, token.getLineNum()));
					break;
				case "turnleft":
					commands.add(new TurnLeftCommand(interp, token.getLineNum()));
					break;
				case "pickbeeper":
					commands.add(new PickBeeperCommand(interp, token.getLineNum()));
					break;
				case "putbeeper":
					commands.add(new PutBeeperCommand(interp, token.getLineNum()));
					break;
				case "turnoff":
					commands.add(new TurnOffCommand(interp, token.getLineNum()));
					break;
				default:
				}
				commandIndex++;
			}
			
			getNextToken();
                        lineNum = token.getLineNum();
			if (token.getType() != TokenType.NEWLINE) {
				throw new InterpreterException("Error on line " + lineNum, lineNum);
			}
		}
		else if (token.getType() == TokenType.IF){
			//System.out.println(indentString + "Entering <if_stmt>");
			//indentString += "    ";
			if_stmt();
		}
		
		else if (token.getType() == TokenType.WHILE){
			//System.out.println(indentString + "Entering <while_stmt>");
			//indentString += "    ";
			while_stmt();
		}
		
		else if (token.getType() == TokenType.DO){
			//System.out.println(indentString + "Entering <do_stmt>");
			//indentString += "    ";
			do_stmt();
		}
                else if (token.getType() == TokenType.INDENT) {
                    throw new InterpreterException("Error on line " + lineNum + ": unexpected indent", lineNum);
                }
		
		else throw new InterpreterException("Error on line: " + lineNum, lineNum);
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <stmt>");
	}
	
	private void if_stmt() throws InterpreterException {
		
		getNextToken();
                int lineNum = token.getLineNum();
                JumpCommand jumpCommand = new JumpCommand(interp);
		
		if (token.getType() == TokenType.TEST) {
			
			TestCommand testCommand = new TestCommand(interp, token.getTest(), token.getLineNum());
			commands.add(testCommand);
			commandIndex++;
			
			getNextToken();
                        lineNum = token.getLineNum();
			
			if (token.getType() == TokenType.COLON) {
				
				//System.out.println(indentString + "Entering <block>");
				//indentString += "    ";
				
				block();
				
                                commands.add(jumpCommand);
				commandIndex++;
				testCommand.setGoTo(commandIndex);
			}
			else {
				throw new InterpreterException("Error on line " + lineNum, lineNum);
			}
		}
		else {
			throw new InterpreterException("Error on line " + lineNum, lineNum);
		}
		
		int numberOfTokens = tokens.size();
		

                while (index < numberOfTokens && peekNextToken() == TokenType.ELIF) {
                        getNextToken();
                        //System.out.println(indentString + "Entering <elif_stmt>");
                        //indentString += "    ";
                        elif_stmt(jumpCommand);
                }
		
		if (index < numberOfTokens && peekNextToken() == TokenType.ELSE) {
			getNextToken();
			//System.out.println(indentString + "Entering <else_stmt>");
			//indentString += "    ";
			else_stmt();
		}
		
		jumpCommand.setGoTo(commandIndex);
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <if_stmt>");
	}
	
	private void elif_stmt(JumpCommand jumpCommand) throws InterpreterException {
		
		getNextToken();
                int lineNum = lineNum = token.getLineNum();
		
		if(token.getType() == TokenType.TEST) {
			
			TestCommand testCommand = new TestCommand(interp, token.getTest(), lineNum);
			commands.add(testCommand);
			commandIndex++;
			
			getNextToken();
                        lineNum = lineNum = token.getLineNum();
			
			if(token.getType() == TokenType.COLON) {
				
				//System.out.println(indentString + "Entering <block>");
				//indentString += "    ";
				
				block();
				
				commands.add(jumpCommand);
				commandIndex++;
				testCommand.setGoTo(commandIndex);
				
			} else {
				throw new InterpreterException("Error on line " + lineNum, lineNum);
			}
		}
                else {
                    throw new InterpreterException("Error on line " + lineNum, lineNum);
                }
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <elif_stmt>");
	}
	
	private void else_stmt() throws InterpreterException {
		
		getNextToken();
                int lineNum = lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.COLON) {
			
			//System.out.println(indentString + "Entering <block>");
			//indentString += "    ";
			
			block();
			
		} else
			throw new InterpreterException("Error on line " + lineNum, lineNum);
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <else_stmt>");
	}
	
	private void while_stmt() throws InterpreterException {
		
		getNextToken();
                int lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.TEST) {
			
			int whileIndex = commandIndex;
			TestCommand whileCommand = new TestCommand(interp, token.getTest(), lineNum);
			
			commands.add(whileCommand);
			commandIndex++;
			
			getNextToken();
			lineNum = token.getLineNum();
                        
			if (token.getType() == TokenType.COLON) {
				
				//System.out.println(indentString + "Entering <block>");
				//indentString += "    ";
				
				block();
				
				commands.add(new JumpCommand(interp, whileIndex));
				commandIndex++;
				whileCommand.setGoTo(commandIndex);
				
			}
			else {
				throw new InterpreterException("Error on line " + lineNum + ": missing colon", lineNum);
			}
		}
		else {
			throw new InterpreterException("Error on line " + lineNum + ": missing condition", lineNum);
		}

		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <while_stmt>");
	}
	
	private void do_stmt() throws InterpreterException {
	
		DoCommand doCommand;
		int doIndex = commandIndex;
		
		getNextToken();
                int lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.INT_LIT){
			
			doCommand = new DoCommand(interp, Integer.parseInt(token.getLexeme()), lineNum);
			commands.add(doCommand);
			commandIndex++;
			
			getNextToken();
                        lineNum = token.getLineNum();
			
			if (token.getType() == TokenType.COLON){
				
				//System.out.println(indentString + "Entering <block>");
				//indentString += "    ";
				
				block();
				
				commands.add(new JumpCommand(interp, doIndex));
				commandIndex++;
				doCommand.setGoTo(commandIndex);
				
			} else {
				throw new InterpreterException("Error on line " + lineNum + ": missing colon", lineNum);
                        }
		} else {
			throw new InterpreterException("Error on line " + lineNum + ": missing integer literal", lineNum);
                }
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <do_stmt>");
	}
	
	private void block() throws InterpreterException {
		
		getNextToken();
                int lineNum = token.getLineNum();
		
		if (token.getType() == TokenType.NEWLINE) {
			getNextToken();
                        lineNum = token.getLineNum();
		} else {
			throw new InterpreterException("Error on line: " + lineNum, lineNum);
                }
		
		if (token.getType() == TokenType.INDENT) {
			getNextToken();
                        lineNum = token.getLineNum();
			while (isStmt()) {
				//System.out.println(indentString + "Entering <stmt>");
				//indentString += "    ";
				stmt();
				getNextToken();
                                lineNum = token.getLineNum();
			}
		} else {
			throw new InterpreterException("Error on line " + lineNum + ": expected indentation", lineNum);
                }
		
		if (token.getType() != TokenType.DEDENT) {
			throw new InterpreterException("Error on line: " + lineNum, lineNum);
		}
		
		//indentString = indentString.replaceFirst("    ", "");
		//System.out.println(indentString + "Exiting <block>");
	}
	
	/** Updates the global token variable to the next token in the list */
	private void getNextToken() {
		if (!(index >= tokens.size())) {
			token = tokens.get(index);
			index++;
		
		//System.out.println(indentString + "Next token is: " + token.getType() +
		//		" Next lexeme is: " + token.getLexeme());
		}
		else {
			token = new Token(TokenType.EOF, tokens.size(), "EOF");
			//System.out.println(indentString + "Next token is: " + token.getType() +
			//		" Next lexeme is: " + token.getLexeme());
		}
	}
	
	/** Gets the next token without changing the index.
	 * 
	 * @return	the TokenType of the next Token
	 */
	
	private TokenType peekNextToken() {
		return tokens.get(index).getType();
	}
	
	/** Checks if the current token is a stmt token.
	 * 
	 * @return	true if stmt token, false otherwise
	 */
	private boolean isStmt() {
		boolean isStmt = false;
		TokenType tokenType = token.getType();
		
		if (tokenType == TokenType.NAME || tokenType == TokenType.IF || tokenType == TokenType.WHILE || tokenType == TokenType.DO) {
			isStmt = true;
		}
		return isStmt;
	}
}