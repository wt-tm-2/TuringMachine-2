/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 *
 * @author Ryan Ball
 */
public class WorldRowHeader extends JPanel implements WorldHeader {
    
    private Font font;
    private int cellSize;
    private int headerLength = 1000;     // default length
    private JScrollBar scrollBar;
    
    public WorldRowHeader() {
        this(new JScrollBar(), new Font("Sans Sarif", Font.BOLD, 14), 50, 1000);
    }
    
    public WorldRowHeader(JScrollBar verticalScrollBar, Font font,
            int cellSize, int headerLength) {
        
        this.cellSize = cellSize;
        this.scrollBar = verticalScrollBar;
        this.font = font;
        this.headerLength = headerLength;
    }
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g = (Graphics2D) g;
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        
        g.setColor(Color.black);
        
        int pHeight = this.getHeight();
        int pWidth = this.getWidth();
        
        int newMax = headerLength * cellSize - pHeight;
        int oldMax = scrollBar.getMaximum();
        int newValue = scrollBar.getValue() + newMax - oldMax;
        scrollBar.setMaximum(newMax);

        if (newMax > oldMax) {
            scrollBar.setValue(newValue);
        }
        else if (newMax < oldMax && newMax > newValue) {
            scrollBar.setValue(newValue);
        }
        
        int rowNumber = scrollBar.getMaximum() - scrollBar.getValue();
        
        int yOffset = rowNumber % cellSize;
        rowNumber /= cellSize;
        rowNumber++;

        // Row heading
        int h;
        for(h = pHeight + yOffset; h > 0; h-= cellSize, rowNumber++){
            
            g.drawLine(0, h - cellSize, pWidth, h - cellSize);
            
            String rowString = String.valueOf(rowNumber);
            int stringPixelLength = fontMetrics.stringWidth(rowString);
            g.drawString(rowString, (pWidth - stringPixelLength)/2, h-cellSize/2 + fontHeight/4);
        }
    }
    
    @Override
    public void repaint() {
        super.repaint();
    }
    
    public int getCellWidth() {
        return cellSize;
    }
    
    public void setCellWidth(int cellSize) {
        this.cellSize = cellSize;
    }
    
    public int getCellHeight() {
        return cellSize;
    }
    
    public void setCellHeight(int cellSize) {
        this.cellSize = cellSize;
    }
    
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }
}
