package karel.interpreter.command;

import karel.interpreter.Interpreter;
import karel.interpreter.Test;

public class TestCommand implements Command {

	private Interpreter interp;
	private Test test;
	private int goToIndex;
	private int lineNum;
	
	public TestCommand(Interpreter interp, Test test, int lineNum) {
		this.interp = interp;
		this.test = test;
		this.lineNum = lineNum;
	}
	
	@Override
	public void execute() {
		if (interp.performCheckTest(test)) {
			interp.incrementControlPointer();
		}
		else {
			interp.setControlPointer(goToIndex);
		}
                interp.incrementConditionalCount();
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
	
	public void setGoTo(int index) {
		goToIndex = index;
	}
	
	public int getLineNum() {
		return lineNum;
	}

	public String toString() {
		return String.format("Line %d: while|if|elif %s else go to %d\n", lineNum, test.toString(), goToIndex);
	}
}
