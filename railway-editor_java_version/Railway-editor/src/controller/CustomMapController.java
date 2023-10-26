/*
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

import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import view.MainPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 * Class which extends {@link DefaultMapController} in order to override the
 * {@link #mouseClicked(java.awt.event.MouseEvent)} method.
 * This class is used to manage the mouse events on the map. It is used to
 * detect if the user clicked on a station or an area. If not, the map zooms in.
 *
 * @author Aurélie Chamouleau
 * @file CustomMapController.java
 * @date 2023-10-26
 * @since 3.0
 */
public class CustomMapController  extends DefaultMapController implements
    MouseListener, MouseMotionListener, MouseWheelListener {

  /**
   * Constructs a new {@code DefaultMapController}.
   *
   * @param map map panel
   */
  public CustomMapController(final JMapViewer map) {
    super(map);
  }


  /**
   * Process mouse click events.
   * Check if the user clicked on a station and an area. If not, zoom in.
   *
   * @param e the event to be processed
   */
  @Override
  public void mouseClicked(final java.awt.event.MouseEvent e) {
    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
        && MainPanel.getInstance().getMovingAdapter().getClickedStation(
          e.getX(), e.getY()) == null && MainPanel.getInstance()
        .getMovingAdapter().getClickedArea(e.getX(), e.getY()) == null) {
      map.zoomIn(e.getPoint());
    }
  }
}
