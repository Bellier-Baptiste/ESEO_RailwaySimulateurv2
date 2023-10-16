/** Class part of the controller package of the application. */

package controller;

import view.MainPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class to handle the different keyEvents on the MainPanel.
 *
* @author Arthur Lagarce, Aurélie Chamouleau
 */
public class KeyboardTool implements KeyListener {
  /** Main panel of the application. */
  private final MainPanel mainPanel;

  /**
   * Constructor.
   *
   * @param mainPanelToSet mainPanel of the app
   */
  public KeyboardTool(final MainPanel mainPanelToSet) {
    this.mainPanel = mainPanelToSet;
  }


  /**
   * Invoked when a key has been pressed.
   *
   * @param e the event to be processed
   *
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
  @Override
  public void keyPressed(final KeyEvent e) {
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
  public void keyReleased(final KeyEvent e) {
    // Does nothing cause mandatory to implement but not useful for us
  }

  /**
   * Invoked when a key has been typed.
   *
   * @param e the event to be processed
   */
  @Override
  public void keyTyped(final KeyEvent e) {
    // Does nothing cause mandatory to implement but not useful for us
  }
}
