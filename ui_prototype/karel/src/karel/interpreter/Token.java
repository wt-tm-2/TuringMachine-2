package karel.interpreter;

public class Token {
		
	private TokenType type;
	private int lineNum;
	private String lexeme = null;
	private Test testType = null;
	
	public Token(TokenType type, int lineNum) {
		this.type = type;
		this.lineNum = lineNum;
	}
	
	public Token(TokenType type, int lineNum, String lexeme) {
		this.type = type;
		this.lineNum = lineNum;
		this.lexeme = lexeme;
	}
	
	public Token(TokenType type, int lineNum, Test test) {
		this.type = type;
		this.lineNum = lineNum;
		this.testType = test;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public String getLexeme() {
		return lexeme;
	}
	
	public Test getTest() {
		return testType;
	}
}