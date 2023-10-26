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

package view;

import controller.CustomMapController;
import controller.MovingAdapter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

/**
 * Main panel that extends {@link JMapViewer} and contains all the views.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file MainPanel.java
 * @date N/A
 * @since 2.0
 */
public final class MainPanel extends JMapViewer {
  //constants
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  /** MainPanel default width. */
  public static final int PANEL_WIDTH_DEFAULT = 1000;
  /** MainPanel default height. */
  public static final int PANEL_HEIGHT_DEFAULT = 600;
  /** MainPanelHUD default position Y. */
  public static final int MAIN_PANEL_HUD_POSITION_Y = 650;
  /** MainPanelHUD display position lat. */
  public static final double MAIN_PANEL_HUD_POSITION_LAT = 47.46667;
  /** MainPanelHUD display position lon. */
  public static final double MAIN_PANEL_HUD_POSITION_LON = -0.55;
  // attributes
  /** MainPanel Singleton instance. */
  private static MainPanel instance;
  /** MainPanel HUD. */
  private final transient MainPanelHud mainPanelHud;
  /** MovingAdapter. */
  private final transient MovingAdapter movingAdapter;
  /** List of lineViews. */
  private transient List<LineView> lineViews;
  /** List of areaViews. */
  private transient List<AreaView> areaViews;
  /** Boolean to know if the HUD is hidden or not. */
  private boolean hideHud;

  /**
   * MainPanel constructor.
   */
  private MainPanel() {
    super();
    for (MouseListener mouseListener : this.getMouseListeners()) {
      this.removeMouseListener(mouseListener);
    }
    for (MouseMotionListener motionListener : this.getMouseMotionListeners()) {
      this.removeMouseMotionListener(motionListener);
    }
    for (MouseWheelListener wheelListener : this.getMouseWheelListeners()) {
      this.removeMouseWheelListener(wheelListener);
    }
    new CustomMapController(this);
    Dimension dim = new Dimension(MainPanel.PANEL_WIDTH_DEFAULT,
        MainPanel.PANEL_HEIGHT_DEFAULT);
    this.setPreferredSize(dim);
    this.lineViews = new ArrayList<>();
    this.areaViews = new ArrayList<>();
    MovingAdapter ma = new MovingAdapter();
    this.movingAdapter = ma;
    addMouseMotionListener(ma);
    addMouseWheelListener(ma);
    addMouseListener(ma);
    mainPanelHud = new MainPanelHud(0, MAIN_PANEL_HUD_POSITION_Y);
    this.hideHud = false;
    Coordinate point = new Coordinate(MAIN_PANEL_HUD_POSITION_LAT,
        MAIN_PANEL_HUD_POSITION_LON);
    this.setDisplayPosition(point, 0);
    this.setZoom(2);
  }

  /**
   * Create MainPanel Singleton.
   *
   * @return MainPanel instance
   */
  public static MainPanel getInstance() {
    if (instance == null) {
      instance = new MainPanel();
    }
    return instance;
  }

  /**
   * add a lineView to the list.
   *
   * @param lineView lineView to add
   */
  public void addLineView(final LineView lineView) {
    this.lineViews.add(lineView);
  }

  /**
   * Add an AreaView to the list.
   *
   * @param areaView areaView to add
   */
  public void addAreaView(final AreaView areaView) {
    this.areaViews.add(areaView);
  }

  /**
   * get lineView list.
   *
   * @return List lineviews
   */
  public List<LineView> getLineViews() {
    return lineViews;
  }

  /**
   * set lineview list.
   *
   * @param lineViewsToSet panel lineViews
   */
  public void setLineViews(final List<LineView> lineViewsToSet) {
    this.lineViews = lineViewsToSet;
  }

  /**
   * get AreaView List.
   *
   * @return List Areaview
   */
  public List<AreaView> getAreaViews() {
    return areaViews;
  }

  /**
   * set AreaViewList.
   *
   * @param areaViewsToSet list of areaView
   */
  public void setAreaViews(final List<AreaView> areaViewsToSet) {
    this.areaViews = areaViewsToSet;
  }

  /**
   * get the panel which contain the HUD.
   *
   * @return mainPanelHud
   */
  public MainPanelHud getMainPanelHud() {
    return mainPanelHud;
  }

  /**
   * get the movingAdapter.
   *
   * @return movingAdapter
   */
  public MovingAdapter getMovingAdapter() {
    return movingAdapter;
  }


  /**
   * return if the hud is hide or not.
   *
   * @return boolean isHideHUD
   */
  public boolean isHideHud() {
    return hideHud;
  }

  /**
   * Set the hud state (hide or present).
   *
   * @param hideHudToSet HUD panel
   */
  public void setHideHud(final boolean hideHudToSet) {
    this.hideHud = hideHudToSet;
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paintComponent(final Graphics g) { //display all panel elements
    Graphics2D g2D = (Graphics2D) g.create();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    super.paintComponent(g);
    //hud display
    if (!hideHud) {
      mainPanelHud.show(g2D);
    }
    //LineViews Display
    if (lineViews != null) {
      for (LineView lineView : lineViews) {
        lineView.show(g2D);
      }
    }
    //AreaViews Display
    if (this.areaViews != null) {
      for (AreaView areaView : this.areaViews) {
        areaView.display(g2D);
      }
    }
  }
}
