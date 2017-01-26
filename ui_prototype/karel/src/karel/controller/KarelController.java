package karel.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import karel.interpreter.Interpreter;
import karel.interpreter.InterpreterException;
import karel.view.KarelView;
import karel.world.World;
import karel.world.WorldGrid;
import karel.world.parser.WorldParserException;
import karel.world.parser.WorldParser;

public class KarelController {
	
      
	private Interpreter interp;
        private KarelView karelView;
        private WorldParser worldParser;
	private boolean hasStarted = false;
	
	public KarelController() {
            
            karelView = new KarelView(this);
            interp = new Interpreter(karelView);
            worldParser = new WorldParser(karelView, karelView.getWorld());
            setSimulationSpeed(karelView.getSpeed());
  
	}
	
	public void startSimulation(String code) throws InterpreterException {
           
            interp.start(code);
            hasStarted = true;
	}
	
	public void runSimulation() {
            interp.run();
	}
	
	public void stepSimulation(String code) throws InterpreterException {
            
            if (hasStarted) {
                interp.step();
            }
            else {
                interp.parseAndInitialize(code);
                hasStarted = true;
                
            }
	}
	
	public void stopSimulation() {
            interp.stop();
	}
	
	public void resetSimulation() {
            hasStarted = false;
            //When the world is repainted the robot won't be set to explode
            WorldGrid.explodeFlag = false;
            interp.reset();
	}
	
	public void setSimulationSpeed(int speed) {
            interp.setSpeed(speed);
	}
        
        public void loadWorld(World world, String worldText) throws WorldParserException {
            worldParser.createWorld(worldText);
        }
        
        public void saveFile(File file, String content) {
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(content);
                }
            } catch (FileNotFoundException ex) {
                
            } catch (IOException ex) {
                
            }
        }
        
        public String openFile(File file) {
            StringBuilder sb = new StringBuilder();
            try {
                FileReader fw = new FileReader(file.getAbsoluteFile());
                try (BufferedReader bw = new BufferedReader(fw)) {
 
                    String line;
                    while ((line = bw.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                }
            } catch (FileNotFoundException ex) {
                
            } catch (IOException ex) {
                
            }
            return sb.toString();
        }
        
        public void disposeKarelView() {
            interp.stop();
            karelView.dispose();
        }
}
