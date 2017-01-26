package karel.interpreter.command;

import java.util.logging.Level;
import java.util.logging.Logger;
import karel.interpreter.Interpreter;

public class CallCommand implements Command {

	private Interpreter interp;
	private int callIndex;
	private int returnIndex;
	private int lineNum;
	
	public CallCommand(Interpreter interp, int callIndex, int returnIndex, int lineNum) {
		this.interp = interp;
		this.callIndex = callIndex;
		this.returnIndex = returnIndex;
		this.lineNum = lineNum;
	}
	
	@Override
	public void execute() {
		interp.performCall(callIndex, returnIndex);
	}
	
	public int getLineNum() {
		return lineNum;
	}
        
        @Override
        public void sleep(int duration) {
            try {
                if (duration != 0)
                    Thread.sleep(duration);
            } catch (InterruptedException ex) {
                assert false;
            }
        }
	
	public String toString() {
		return String.format("Line %d: call command %d return to command %d\n", lineNum, callIndex, returnIndex);
	}
}
