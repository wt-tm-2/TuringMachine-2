package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class JumpCommand implements Command {

	private int goToIndex;
	private Interpreter interp;
	
	public JumpCommand(Interpreter interp) {
            this.interp = interp;
	}
        
        public JumpCommand(Interpreter interp, int goToIndex) {
            this.interp = interp;
            this.goToIndex = goToIndex;
        }
	
	@Override
	public void execute() {
		interp.setControlPointer(goToIndex);
	}
	
	@Override
	public int getLineNum() {
		return -1;
        }
        
        public void setGoTo(int goToIndex) {
            this.goToIndex = goToIndex;
        }
	
	public String toString() {
		return String.format("jump to %d\n", goToIndex);
	}
        
        @Override
        public void sleep(int duration) {

        }



}
