package controller;

import view.MainPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class to handle the differents keyEvents on the MainPanel.
 *
* @author Arthur Lagarce, Aurélie Chamouleau
 */
public class KeyboardTool implements KeyListener {

  MainPanel mainPanel;

  /**
   * Constructor.
   *
   * @param mainPanel mainPanel of the app
   */
  public KeyboardTool(MainPanel mainPanel) {
    this.mainPanel = mainPanel;
  }


  @Override
  public void keyPressed(KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_H) {
      mainPanel.setHideHud(!mainPanel.isHideHud());
      mainPanel.repaint();
    }

  }

  /**
   * Invoked when a key has been released.
   *
   * @param e the event to be processed
   */
  @Override
  public void keyReleased(KeyEvent e) {
    // Does nothing cause mandatory to implement but not useful for us
  }

  /**
   * Invoked when a key has been typed.
   *
   * @param e the event to be processed
   */
  @Override
  public void keyTyped(KeyEvent e) {
    // Does nothing cause mandatory to implement but not useful for us
  }
}
