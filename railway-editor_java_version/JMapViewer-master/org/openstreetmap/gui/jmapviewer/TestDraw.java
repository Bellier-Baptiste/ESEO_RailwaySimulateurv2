package org.openstreetmap.gui.jmapviewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;

public class TestDraw extends JFrame {
		public TestDraw() {
	        super("JMapViewer Demo");
	        setSize(400, 400);
	        final JMapViewer map = new JMapViewer();
	        setLayout(new BorderLayout());
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setExtendedState(JFrame.MAXIMIZED_BOTH);
	        TestDrawPanel panel = new TestDrawPanel();
	        JPanel helpPanel = new JPanel();
	        add(panel, BorderLayout.CENTER);
	        add(helpPanel, BorderLayout.SOUTH);
	        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
	                + "left double click or mouse wheel to zoom.");
	        helpPanel.add(helpLabel);
	      
	        panel.add(map, BorderLayout.CENTER);
	        panel.repaint();
		}
		
		  /**
	     * @param args
	     */
	    public static void main(String[] args) {
	        // java.util.Properties systemProperties = System.getProperties();
	        // systemProperties.setProperty("http.proxyHost", "localhost");
	        // systemProperties.setProperty("http.proxyPort", "8008");
	        new TestDraw().setVisible(true);
	    }

}
