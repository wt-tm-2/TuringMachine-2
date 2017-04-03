
package parser;

/**
 * Defines a state transition for the Turing Machine.
 * A state transition consists of the symbol read, the symbol to write,
 * the direction to move the read/write head, and the state to transition to.
 * 
 * @author Michael Johnson
 */
public class Transition {
    private final String readSymbol;
    private final String writeSymbol;
    private final String direction;
    private final String newStateMnemonic;
    private final String tapeIndex;
    
    /**
     * Creates a new transition object setting all required data.
     * @param readSymbol the current symbol read
     * @param writeSymbol the symbol to write over the read symbol
     * @param direction the direction to move the read/write head after writing
     * @param newStateMnemonic the mnemonic of the new state to transition to
     */
    Transition(String readSymbol, String writeSymbol, String direction, String newStateMnemonic,
            String tapeIndex) {
        this.readSymbol = readSymbol;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
        this.newStateMnemonic = newStateMnemonic;
        this.tapeIndex = tapeIndex;
    }

    /**
     * 
     * @return the read symbol for this transition
     */
    public String getReadSymbol() {
        return readSymbol;
    }

    /**
     * 
     * @return the write symbol for this transition
     */
    public String getWriteSymbol() {
        return writeSymbol;
    }

    /**
     * 
     * @return the direction the read/write head will move after writing
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 
     * @return the mnemonic of the new state to transition into
     */
    public String getNewState() {
        return newStateMnemonic;
    }
    
    /**
     * 
     * @return The index of the tape to switch to for this transition
     */
    public String getTapeIndex() {
        return tapeIndex;
    }
    
}
