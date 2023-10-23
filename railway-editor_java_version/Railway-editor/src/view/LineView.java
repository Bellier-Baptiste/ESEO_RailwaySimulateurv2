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

import model.Line;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;

/**
 * View for a {@link model.Line}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file LineView.java
 * @date N/A
 * @since 2.0
 */
public class LineView {
  // constants
  /** Stroke width. */
  private static final int STROKE_WIDTH = 5;
  /** Stroke miterlimit. */
  private static final int STROKE_MITERLIMIT = 5;
  // attributes
  /** Line bound to the view. */
  private Line line;
  /** StationsViews of the lineView. */
  private List<StationView> stationViews;
  /** Stroke of the line. */
  private Stroke stroke;

  /**
   * Constructor.
   *
   * @param lineBound         line to bind to the view
   * @param lineStationViews stationsViews of the lineView
   */
  public LineView(final Line lineBound,
                  final List<StationView> lineStationViews) {
    this.line = lineBound;
    this.stationViews = lineStationViews;
    this.stroke = new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE,
        BasicStroke.JOIN_ROUND, STROKE_MITERLIMIT);
  }

  // accessors

  /**
   * get the model line linked to this view.
   *
   * @return Line line
   */
  public Line getLine() {
    return line;
  }

  /**
   * line a model line to this view.
   *
   * @param lineBound line to bind
   */
  public void setLine(final Line lineBound) {
    this.line = lineBound;
  }

  /**
   * get the stationsViews of the lineView.
   *
   * @return List of stationsViews of the lineView
   */
  public List<StationView> getStationViews() {
    return this.stationViews;
  }

  /**
   * set the stationsViews of the lineView.
   *
   * @param lineStationViews stationsViews of the line
   */
  public void setStationViews(final List<StationView> lineStationViews) {
    this.stationViews = lineStationViews;
  }


  // methods

  /**
   * Display the line shape (poyline).
   *
   * @param g2D graphics component
   */
  public void show(final Graphics2D g2D) {
    //x and y point array creation
    int[] pointsX = new int[this.stationViews.size()];
    int[] pointsY = new int[this.stationViews.size()];
    for (int i = 0; i < this.stationViews.size(); i++) {
      //fill arrays with the center point of circles representing stations
      pointsX[i] = this.stationViews.get(i).getStation().getPosX();
      pointsY[i] = this.stationViews.get(i).getStation().getPosY();
    }

    //setting color
    g2D.setColor(this.line.getColor());
    g2D.setStroke(this.stroke);


    //draw the polyline
    if (this.stationViews.size() > 1) {
      g2D.drawPolyline(pointsX, pointsY, this.stationViews.size());
    }
    //draw the station (g2D.drawCircle)
    for (StationView stationView : stationViews) {
      stationView.show(g2D, this.line.getColor());
    }
  }

  /**
   * set line Stroke.
   *
   * @param lineStroke stroke
   */
  public void setStroke(final Stroke lineStroke) {
    this.stroke = lineStroke;
  }

}
