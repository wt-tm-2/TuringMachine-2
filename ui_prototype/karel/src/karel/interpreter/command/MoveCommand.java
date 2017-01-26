package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class MoveCommand implements Command {

	private Interpreter interp;
	private int lineNum;
	
	public MoveCommand(Interpreter interp, int lineNum) {
		this.interp = interp;
		this.lineNum = lineNum;
	}
	@Override
	public void execute() {
		interp.performMove();
                interp.incrementInstructionCount();
	}
	
	@Override
	public int getLineNum() {
		return lineNum;
	}
	
	public String toString() {
		return String.format("Line %d: move\n", lineNum);
	}
        
        @Override
        public void sleep(int duration) {

        }
}
