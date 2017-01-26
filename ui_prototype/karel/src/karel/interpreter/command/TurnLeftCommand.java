package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class TurnLeftCommand implements Command {

	private Interpreter interp;
	private int lineNum;
	
	public TurnLeftCommand(Interpreter interp, int lineNum) {
		this.interp = interp;
		this.lineNum = lineNum;
	}
	@Override
	public void execute() {
		interp.performTurnLeft();
                interp.incrementInstructionCount();
	}
	
	public int getLineNum() {
		return lineNum;
	}

	public String toString() {
		return String.format("Line %d: turnleft\n", lineNum);
	}
        
        @Override
        public void sleep(int duration) {
        }
}
