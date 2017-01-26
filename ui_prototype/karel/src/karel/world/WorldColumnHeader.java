/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world;

import java.awt.Color;
import java.awt.Dimension;
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
public class WorldColumnHeader extends JPanel implements WorldHeader {
    
    private Font font;
    private int cellSize;
    private int headerLength = 1000;     // default length
    private JScrollBar scrollBar;
    
    public WorldColumnHeader() {
        this(new JScrollBar(), new Font("Sans Sarif", Font.BOLD, 14), 50, 1000);
    }
    
    public WorldColumnHeader(JScrollBar horizontalScrollBar, Font font,
            int cellSize, int headerLength) {
        
        this.cellSize = cellSize;
        this.scrollBar = horizontalScrollBar;
        this.font = font;
        this.headerLength = headerLength;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        
        g.setColor(Color.black);
        
        int pWidth = this.getWidth();
        int pHeight = this.getHeight();
        
        scrollBar.setMaximum(headerLength * cellSize - pWidth);
        int colNumber = scrollBar.getValue();
        
        int xOffset = colNumber % cellSize;
        colNumber /= cellSize;
        colNumber++;
        
        // Column heading
        for(int w = -xOffset; w < pWidth; w+=cellSize, colNumber++){
            
            g2.drawLine(w + cellSize, 0, w + cellSize, pHeight);
            
            String colString = String.valueOf(colNumber);
            int stringPixelLength = fontMetrics.stringWidth(colString);
            g2.drawString(colString, w + cellSize/2 - stringPixelLength/2, pHeight/2 + fontHeight/4);
        }
    }
    
    @Override
    public void repaint() {
        super.repaint();
    }
    
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }
}
