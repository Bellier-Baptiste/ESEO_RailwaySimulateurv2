package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import view.MainPanel;

/**
 * Class to handle the differents keyEvents on the MainPanel.
 * @author arthu
 *
 */
public class KeyboardTool implements KeyListener {
	
	MainPanel mainPanel;	
	/**
	 * Constructor.
	 * @param mainPanel mainPanel of the app
	 */
	public KeyboardTool(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	
	@Override
	public void keyPressed(KeyEvent e) {

		if ((int) e.getKeyCode() == KeyEvent.VK_H) {
			mainPanel.setHideHud(!mainPanel.isHideHud());
			mainPanel.repaint();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}	

	@Override
	public void keyTyped(KeyEvent e) {


	}
	


}
