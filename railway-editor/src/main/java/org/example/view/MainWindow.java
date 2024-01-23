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

import org.example.controller.KeyboardTool;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Main window's Singleton which contain all the different panels.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file MainWindow.java
 * @date N/A
 * @since 2.0
 */
public final class MainWindow extends JFrame {

  // constants
  /** Width of the window. */
  public static final int WINDOW_WIDTH = 1000;
  /** Height of the window. */
  public static final int WINDOW_HEIGHT = 600;
  /** Title of the window. */
  private static final String TITLE = "Railway-editor";

  // attributes
  /** Singleton instance of the class. */
  private static MainWindow instance;
  /** Main panel of the application. */
  private MainPanel mainPanel;
  /** ToolBar panel of the application. */
  private final ToolBarPanel toolBarPanel;
  /** Event recap panel of the application. */
  private EventRecap eventRecapPanel;

  /**
   * Constructor, initialize window and panels.
   */
  private MainWindow() {
    this.setResizable(true);
    this.setLayout(new BorderLayout());
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setTitle(TITLE);
    this.setLocationRelativeTo(null);
    KeyboardTool kbt = new KeyboardTool(this.getMainPanel());
    this.addKeyListener(kbt);

    this.mainPanel = MainPanel.getInstance();
    this.getContentPane().add(this.mainPanel,
        BorderLayout.CENTER);

    JMenuBar appMenuBar = MenuBar.getInstance();
    this.setJMenuBar(appMenuBar);

    this.toolBarPanel = new ToolBarPanel();
    this.toolBarPanel.setVisible(true);
    this.getContentPane().add(this.toolBarPanel, BorderLayout.NORTH);

    this.eventRecapPanel = EventRecap.getInstance();
    this.getContentPane().add(this.eventRecapPanel, BorderLayout.WEST);

    this.revalidate();

    BufferedImage source;
    try {
      source = ImageIO.read(Objects.requireNonNull(getClass().getResource(
          "/images/flower_hong_kong.png")));
      ImageIcon img = new ImageIcon(source);
      this.setIconImage(img.getImage());
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.pack();
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainPanel.requestFocusInWindow();
  }

  //accessors

  /**
   * get the main panel.
   *
   * @return MainPanel mainPanel
   */
  public MainPanel getMainPanel() {
    return mainPanel;
  }

  /**
   * set the MainPanel.
   *
   * @param mainPanelToSet Map panel
   */
  public void setMainPanel(final MainPanel mainPanelToSet) {
    this.mainPanel = mainPanelToSet;
  }


  /**
   * get the eventRecapPanel.
   *
   * @return EventRecapPanel eventRecapPanel.
   */
  public EventRecap getEventRecapPanel() {
    return eventRecapPanel;
  }

  /**
   * set the eventRecapPanel.
   *
   * @param eventRecapPaneToSet panel which recap the added events
   */
  public void setEventRecapPanel(final EventRecap eventRecapPaneToSet) {
    this.eventRecapPanel = eventRecapPaneToSet;
  }

  /**
   * Return Singleton.
   *
   * @return MainWindow's instance
   */
  public static MainWindow getInstance() {
    if (instance == null) {
      instance = new MainWindow();
    }
    return instance;
  }

  /**
   * Get the toolbar panel.
   *
   * @return the toolbar panel
   */
  public ToolBarPanel getToolBarPanel() {
    return toolBarPanel;
  }

}
