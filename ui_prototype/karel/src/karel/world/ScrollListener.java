/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 *
 * @author Ryan Ball
 */
public class ScrollListener implements AdjustmentListener {
    
    private WorldHeader worldHeader;
    private WorldGrid worldGrid;
    
    public ScrollListener(WorldHeader worldHeader, WorldGrid worldGrid) {
        this.worldHeader = worldHeader;
        this.worldGrid = worldGrid;
    }
    
    @Override
    public synchronized void adjustmentValueChanged(AdjustmentEvent e) {
        worldHeader.repaint();
        worldGrid.repaint();
    }
    
}
