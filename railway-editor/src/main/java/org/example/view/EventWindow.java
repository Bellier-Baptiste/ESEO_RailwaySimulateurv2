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

package org.example.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * JFrame of the {@link org.example.model.Event}'s edition.
 *
 * @see org.example.view.ListEventPanel
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventWindow.java
 * @date N/A
 * @since 2.0
 */
public class EventWindow extends JFrame {
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Singleton instance.
   */
  private static EventWindow instance;

  /**
   * Event window width.
   */
  private static final int WINDOW_WIDTH = 800;

  /**
   * Event window height.
   */
  private static final int WINDOW_HEIGHT = 270;
  /**
   * Event window title.
   */
  private static final String TITLE = "Event-editor";


  // attributes

  /**
   * Constructor, initialize window and panels.
   */
  public EventWindow() {
    ListEventPanel listEventPanel = ListEventPanel.getInstance();
    this.getContentPane().add(listEventPanel);
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setTitle(TITLE);
    try {
      BufferedImage source = ImageIO.read(
          Objects.requireNonNull(getClass().getResource(
          "/images/railwayEditorIcon3.png")));
      ImageIcon img = new ImageIcon(source);
      this.setIconImage(img.getImage());
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.setAlwaysOnTop(true);

    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  /**
   * Create EventWindow Singleton.
   *
   * @return EventWindow instance
   */
  public static EventWindow getInstance() {
    if (instance == null) {
      instance = new EventWindow();
    }
    return instance;
  }
}
