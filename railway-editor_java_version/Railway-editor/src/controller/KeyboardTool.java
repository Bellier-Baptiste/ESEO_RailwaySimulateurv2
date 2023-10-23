/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package controller;

import view.MainPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class to handle the different keyEvents on the {@link MainPanel}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file KeyboardTool.java
 * @date N/A
 * @since 2.0
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
