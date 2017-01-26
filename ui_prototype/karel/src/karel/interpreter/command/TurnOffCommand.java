package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class TurnOffCommand implements Command {

	private Interpreter interp;
	private int lineNum;
	
	public TurnOffCommand(Interpreter interp, int lineNum) {
		this.interp = interp;
		this.lineNum = lineNum;
	}
	
	@Override
	public void execute() {
		interp.performTurnOff("Robot turned off");
                interp.incrementInstructionCount();
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public String toString() {
		return String.format("Line %d: turnoff\n", lineNum);
	}
        
        @Override
        public void sleep(int duration) {
        }
}
