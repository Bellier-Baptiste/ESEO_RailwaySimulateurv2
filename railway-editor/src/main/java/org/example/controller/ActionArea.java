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

package org.example.controller;

import org.example.model.Area;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.example.view.AreaView;
import org.example.view.MainWindow;

/**
 * A class for managing areas {@link Area} on the map.
 * Linked to a button in {@link org.example.view.ToolBarPanel}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ActionArea.java
 * @date 2023-10-02
 * @since 3.0
 */
public class ActionArea {
  /** Action name. */
  public static final String ACTION_NAME = "ADD_AREA";
  /** Default area x position. */
  private static final int AREA_POS_X_DEFAULT = 150;
  /** Default area y position. */
  private static final int AREA_POS_Y_DEFAULT = 150;
  /** Default area width. */
  private static final int AREA_WIDTH_DEFAULT = 75;
  /** Default area height. */
  private static final int AREA_HEIGHT_DEFAULT = 75;
  /** Singleton instance. */
  private static ActionArea instance;

  /**
   * Create Singleton.
   *
   * @return ActionArea instance
   */
  public static ActionArea getInstance() {
    if (instance == null) {
      instance = new ActionArea();
    }
    return instance;
  }

  /**
   * Creates a {@link Area} with its linked {@link AreaView} created and
   * add this {@link AreaView} to the {@link org.example.view.MainPanel} with
   * {@link org.example.view.MainPanel#addAreaView(AreaView)}.
   */
  public void addArea() {
    Area area = new Area(AREA_POS_X_DEFAULT,
        AREA_POS_Y_DEFAULT, AREA_WIDTH_DEFAULT, AREA_HEIGHT_DEFAULT);

    Coordinate latLonTop = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(area.getPosX(), area.getPosY());
    area.setLatitudeTop(latLonTop.getLat());
    area.setLongitudeTop(latLonTop.getLon());

    Coordinate latLonBot = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(area.getPosX() + area.getWidth(),
        area.getPosY() + area.getHeight());
    area.setLatitudeBot(latLonBot.getLat());
    area.setLongitudeBot(latLonBot.getLon());

    AreaView areaView = new AreaView(area);
    MainWindow.getInstance().getMainPanel().addAreaView(areaView);
    MainWindow.getInstance().getMainPanel().repaint();
  }
}
