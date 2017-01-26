package karel.interpreter.command;

import karel.interpreter.Interpreter;

public class ReturnCommand implements Command {
	
	private Interpreter interp;
	
	public ReturnCommand(Interpreter interp) {
		this.interp = interp;
	}

	@Override
	public void execute() {
		interp.performReturn();
	}
	
	@Override
	public int getLineNum() {
		return -1;
	}
	
	public String toString() {
		return "return\n";
	}
        
        @Override
        public void sleep(int duration) {
        }
}
