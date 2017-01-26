package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class PutBeeperCommand implements Command {

	private Interpreter interp;
	private int lineNum;
	
	public PutBeeperCommand(Interpreter interp, int lineNum) {
		this.interp = interp;
		this.lineNum = lineNum;
	}
	
	@Override
	public void execute() {
		interp.performPutBeeper();
                interp.incrementInstructionCount();
	}
	
	public int getLineNum() {
		return lineNum;
	}

	public String toString() {
		return String.format("Line %d: putbeeper\n", lineNum);
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
}
