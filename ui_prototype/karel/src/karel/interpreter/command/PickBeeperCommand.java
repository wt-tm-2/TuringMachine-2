package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class PickBeeperCommand implements Command {

	private Interpreter interp;
	private int lineNum;
	
	public PickBeeperCommand(Interpreter interp, int lineNum) {
		this.interp = interp;
		this.lineNum = lineNum;
	}
	
	@Override
	public void execute() {
		interp.performPickBeeper();
                interp.incrementInstructionCount();
		
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public String toString() {
		return String.format("Line %d: pickbeeper\n", lineNum);
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
