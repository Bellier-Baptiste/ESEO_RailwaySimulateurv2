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

import org.example.model.Station;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Class which draw a {@link Station} on the {@link MainPanel}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file StationView.java
 * @date N/A
 * @since 2.0
 */
public class StationView {
  // constants
  /**
   * Station size.
   */
  private static final int STATION_SIZE = 18;
  /**
   * Center circle station size.
   */
  private static final int CENTER_STATION_SIZE = 14;
  /**
   * Stroke width.
   */
  private static final int STROKE_WIDTH = 5;
  // attributes
  /**
   * Station bound to the view.
   */
  private Station station;
  /**
   * Station size.
   */
  private int stationSize;
  /**
   * Center circle station size.
   */
  private int centerStationSize;
  /**
   * Stroke of the station.
   */
  private Stroke stroke;
  /**
   * Center circle color.
   */
  private Color centerCircleColor;

  /**
   * StationView constructor.
   *
   * @param stationBound station to bind
   */
  public StationView(final Station stationBound) {
    this.station = stationBound;
    this.stationSize = STATION_SIZE;
    this.centerStationSize = CENTER_STATION_SIZE;
    this.stroke = new BasicStroke(STROKE_WIDTH);
    this.centerCircleColor = Color.WHITE;
  }

  /**
   * get the station bound to the view.
   *
   * @return the bound station of the station view
   */
  public Station getStation() {
    return station;
  }

  /**
   * bind a station to the view.
   *
   * @param stationBound station to bind
   */
  public void setStation(final Station stationBound) {
    this.station = stationBound;
  }

  /**
   * get the stationView size.
   *
   * @return int size
   */
  public int getStationSize() {
    return stationSize;
  }

  /**
   * set the stationView size.
   *
   * @param stationSizeToSet external circle radius
   */
  public void setStationSize(final int stationSizeToSet) {
    this.stationSize = stationSizeToSet;
  }

  /**
   * get center circle station size (the white one).
   *
   * @return int size
   */
  public int getCenterStationSize() {
    return centerStationSize;
  }

  /**
   * set the center circle station size.
   *
   * @param centerStationSizeToSet circle radius
   */
  public void setCenterStationSize(final int centerStationSizeToSet) {
    this.centerStationSize = centerStationSizeToSet;
  }

  // methods

  /**
   * draw 2 circles (one with color for the outline and another filled with
   * white to hide the polyline behind).
   *
   * @param g2D   graphics component
   * @param color intern circle color
   */
  public void show(final Graphics2D g2D, final Color color) {
    g2D.setColor(color);
    g2D.setStroke(this.stroke);
    g2D.drawOval(station.getPosX() - stationSize / 2, station.getPosY()
        - stationSize / 2, stationSize, stationSize);
    g2D.setColor(centerCircleColor);
    g2D.fillOval(station.getPosX() - centerStationSize / 2,
        station.getPosY() - centerStationSize / 2, centerStationSize,
        centerStationSize);
  }

  /**
   * set the stroke.
   *
   * @param strokeToSet stroke
   */
  public void setStroke(final Stroke strokeToSet) {
    this.stroke = strokeToSet;
  }

  /**
   * get the center circle color.
   *
   * @return Color centerCircleColor
   */
  public Color getCenterCircleColor() {
    return centerCircleColor;
  }

  /**
   * set the center circle color.
   *
   * @param centerCircleColorToSet intern circle color
   */
  public void setCenterCircleColor(final Color centerCircleColorToSet) {
    this.centerCircleColor = centerCircleColorToSet;
  }
}
