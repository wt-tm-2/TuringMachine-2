package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class DoCommand implements Command {
	
	private Interpreter interp;
	private int numberOfTimes;
	private int currentCount = 0;
	private int goToIndex;
	private int lineNum;

	
	public DoCommand(Interpreter interp, int numberOfTimes, int lineNum) {
		this.interp = interp;
		this.numberOfTimes = numberOfTimes;
		this.lineNum = lineNum;
	}

	@Override
	public void execute() {
		if (numberOfTimes > currentCount) {
			currentCount++;
			interp.incrementControlPointer();
		}
		else {
			currentCount = 0;
			interp.setControlPointer(goToIndex);
		}
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
	
	public void setGoTo(int index) {
		goToIndex = index;
	}
	
	public String toString() {
		return String.format("Line %d: do %d times, current count is %d\n", lineNum, numberOfTimes, currentCount);
	}
}
