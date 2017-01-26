package karel.interpreter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import karel.interpreter.command.Command;
import karel.view.KarelView;
import karel.view.TextEditor;
import karel.world.BeeperException;
import karel.world.RobotMoveException;
import karel.world.World;

/** Karel interpreter */
public class Interpreter {
	
	private final String ROBOT_NOT_TURNED_OFF_ERROR = "Error: The end of execution was reached and the robot was not turned off.";

	private Tokenizer tokenizer;
	private Parser parser;
	
	private boolean notInterrupted = true;
	private boolean turnedOff = false;
	private boolean hasReset = false;
	
	private Stack<Integer> callStack = new Stack<Integer>();
	private ArrayList<Command> commands;
	private int controlPointer = 0;
	private int speed;
        private int instructionCount = 0;
        private int conditionalCount = 0;
	
	private final KarelView view;
        private final World world;
	private InterpreterThread interpThread;
	private final Object monitor = new Object();   // used to prevent turnoff code from executing
	                                         // after reset code
	
	public Interpreter(KarelView view) {
            this.world = view.getWorld();
            this.view = view;
	}
	
	/** Parses the code and starts the interpreter. */
	public void start(String code) throws InterpreterException {

            tokenizer = new Tokenizer();
            parser = new Parser(this);

            parser.parse(tokenizer.tokenize(code));
            commands = parser.getCommands();
            
            hasReset = false;
            turnedOff = false;

            if (commands.isEmpty()) {
                    view.setStatusMessage("Error: Code editor is empty.");
                    view.disableStopButton();
            }
            else {
                    view.setStartState();
                    
                    notInterrupted = true;
                    callStack = new Stack<>();
                    instructionCount = 0;
                    conditionalCount = 0;
                    
                    interpThread = new InterpreterThread();
                    interpThread.start();
            }
	}
	
	/** Runs the interpreter starting at the next command to be run. */
	public void run() {
		
            // don't create an interpreter thread if one is already running
            if (interpThread!= null && !interpThread.isAlive()) {
                view.setRunState();
                notInterrupted = true;
                interpThread = new InterpreterThread();
                interpThread.start();
            }
	}
	
	/** Steps through the next command and any call or return commands. */
	public void step() {
            // don't create an interpreter thread if one is already running
            if (interpThread!= null && !interpThread.isAlive()) {
                view.setStepState(); 
                notInterrupted = false;
                interpThread = new InterpreterThread();
                interpThread.start();
            }
	}
	
	/** Parses the code and initializes the simulation.
        * @param code
        * @throws karel.interpreter.InterpreterException */
	public void parseAndInitialize(String code) throws InterpreterException {
            
            tokenizer = new Tokenizer();
            parser = new Parser(this);

            parser.parse(tokenizer.tokenize(code));
            commands = parser.getCommands();
            
            hasReset = false;
            turnedOff = false;

            if (commands.isEmpty()) {
                    view.setStatusMessage("Error: Code editor is empty.");
                    view.disableStartButton();
                    view.disableStepButton();
            }
            else {
                    view.setStepState();
                    instructionCount = 0;
                    conditionalCount = 0;
                    callStack = new Stack<>();
                    interpThread = new InterpreterThread();
                    try {
                        Command command = commands.get(controlPointer);
                        view.highlightLine(KarelView.CODE_TAB_INDEX,
                            command.getLineNum(), TextEditor.EXECUTION_COLOR);
                        view.setStatusMessage(world.getRobotStatus() 
                            + "\nInstruction count: " + instructionCount);
                    } catch (IndexOutOfBoundsException e) {
                        view.setStatusMessage("Error: No instructions were "
                                + "entered.");
                        view.disableStartButton();
                        view.disableStepButton();
                    }
            }
	}
	
	public void stop() {
		notInterrupted = false;
                
                synchronized(monitor) {
                    view.setStoppedState();
                }
	}
	
	public void reset() {
		
		notInterrupted = false;
		hasReset = true;
		
		synchronized(monitor) {
			view.reset();
		}
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setControlPointer(int index) {
		controlPointer = index;
	}
	
	public void incrementControlPointer() {
		controlPointer++;
	}
	
	public void performMove() {

            try {
                world.moveRobot(speed);
                
            } catch (RobotMoveException ex) {
                performTurnOff(ex.getMessage());
            }
		controlPointer++;
	}
	
	public void performTurnLeft() {
		
                world.turnRobot(speed);
		
		controlPointer++;
	}
	
	public void performPutBeeper() {
		
                try {
                    world.putBeeper();
                } catch (BeeperException ex) {
                    performTurnOff(ex.getMessage());
                }
		
		controlPointer++;
	}
	
	public void performPickBeeper() {
		
		try {
                    world.pickBeeper();
                } catch (BeeperException ex) {
                    performTurnOff(ex.getMessage());
                }
		
		controlPointer++;
	}
	
	public void performTurnOff(String message) {

		turnedOff = true;
		notInterrupted = false;
		
                if (!hasReset) {
                    view.disableStartButton();
                    view.disableStepButton();
                    view.disableStopButton();

                    view.showDialog(message);
                }
	}
	
	public void performReturn() {
		controlPointer = callStack.pop();
	}
	
	public void performCall(int callIndex, int returnIndex) {
		controlPointer = callIndex;
		callStack.push(returnIndex);
	}
	
	public boolean performCheckTest(Test test) {
		return world.checkTest(test);
	}
        
        public void incrementInstructionCount() {
            instructionCount++;
        }
        
        public void incrementConditionalCount() {
            conditionalCount++;
        }
	
	private class InterpreterThread extends Thread {
                
            
                @Override
                public void run() {
                    // step once
                    synchronized(monitor) {
                            step();
                    }

                    // continue to step while not interrupted
                    while (notInterrupted) {
                            synchronized(monitor) {
                                step();
                            }
                    }
                    hasReset = false;
                }
                
                public void step() {
                    
                    Command command = null;
                    try {
                        command = commands.get(controlPointer);
                    } catch (IndexOutOfBoundsException e) {
                        view.setStatusMessage("Error: No instructions were "
                                + "entered.");
                        view.disableStartButton();
                        view.disableStepButton();
                    }

                    view.highlightLine(KarelView.CODE_TAB_INDEX,
                            command.getLineNum(), TextEditor.EXECUTION_COLOR);
                    view.setStatusMessage(world.getRobotStatus() 
                            + "\nInstruction count: " + instructionCount
                            + "\nConditional count: " + conditionalCount);
                    
                    command.sleep(speed);
                    command.execute();
                    
                    // step until next command in source is reached
                    while (controlPointer < commands.size() && (command = commands.get(controlPointer)).getLineNum() == -1) {
                            command.execute();
                    }

                    if (controlPointer >= commands.size() && !turnedOff) {

                        performTurnOff("End of execution was reached and the robot was not turned off.");
                    }
                    else {
                        view.highlightLine(KarelView.CODE_TAB_INDEX,
                            command.getLineNum(), TextEditor.EXECUTION_COLOR);
                        view.setStatusMessage(world.getRobotStatus() 
                            + "\nInstruction count: " + instructionCount
                            + "\nConditional count: " + conditionalCount);
                    }
                }
	}
}
