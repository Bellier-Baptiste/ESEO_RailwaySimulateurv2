package org.openstreetmap.gui.jmapviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;




public class TestDrawPanel extends JPanel {

	//constants
		public static final int PANEL_WIDTH_DEFAULT = 1100;
		public static final int PANEL_HEIGHT_DEFAULT = 750;
		public static final Color BACKGROUND_COLOR_DEFAULT = Color.WHITE;
		
		
		public TestDrawPanel() {
			Dimension dim = new Dimension(PANEL_WIDTH_DEFAULT, PANEL_HEIGHT_DEFAULT);
			this.setPreferredSize(dim);
			this.setBackground(BACKGROUND_COLOR_DEFAULT);


		}
		
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {//display all panel elements
			Graphics2D g2D = (Graphics2D) g.create();
			 g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			 g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paintComponent(g);
		
			//Grid start
	        g2D.setColor(new Color(231,231,231));
	        int sideLength = 20;
	        int nRowCount = getHeight() / sideLength;
	        int currentX = sideLength;
	        for (int i = 0; i < nRowCount; i++) {
	            g2D.drawLine(0, currentX, getWidth(), currentX);
	            currentX = currentX + sideLength;
	        }
	        
	        int nColumnCount = getWidth() / sideLength;
	        int currentY = sideLength;
	        
	        for (int i = 0; i < nColumnCount; i++) {
	            g2D.drawLine(currentY, 0, currentY, getHeight());
	            currentY = currentY + sideLength;
	        }
	        //Grid end
	        


			

		}
}
