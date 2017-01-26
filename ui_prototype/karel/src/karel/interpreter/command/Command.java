package karel.interpreter.command;

public interface Command {

	public void execute();
        public void sleep(int duration);
	public int getLineNum();
}
