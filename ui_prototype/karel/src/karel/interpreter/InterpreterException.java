package karel.interpreter;

@SuppressWarnings("serial")
public class InterpreterException extends Exception {

        private final int lineNum;
        
	public InterpreterException(String message, int lineNum) {
		super(message);
                this.lineNum = lineNum;
	}
        
        public int getLineNum() {
            return lineNum;
        }
} 