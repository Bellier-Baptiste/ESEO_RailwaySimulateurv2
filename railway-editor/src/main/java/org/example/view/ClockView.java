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

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class view of the analog clock.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ClockView.java
 * @date N/A
 * @since 2.0
 */
public class ClockView {
  /** Hour of the clock. */
  private int hour;
  /** Minutes of the clock. */
  private int minutes;
  /** Position X of the clock. */
  private int posX;
  /** Position Y of the clock. */
  private int posY;
  /** Panel of the clock. */
  private JPanel panel;
  /** Size of the clock. */
  private static final int CLOCK_SIZE = 30;
  /** Size of the clock mark. */
  private static final int CLOCK_MARK_SIZE = 3;
  /** Size of the minutes. */
  private static final int MINUTE_SIZE = 20;
  /** Size of the hour. */
  private static final int HOUR_SIZE = 10;
  /** Number hours on clock. */
  private static final int NUMBER_HOURS = 12;
  /** Number of hours in PI range. */
  private static final int PI_HOURS = 6;
  /** Number of minutes in PI range. */
  private static final int PI_MINUTES = 30;
  /** Stroke width. */
  private static final int STROKE_WIDTH = 3;
  /** Clock width. */
  private static final int CLOCK_WIDTH_HEIGHT = 6;

  /**
   * ClockView constructor.
   *
   * @param clockPosX  clock positionX
   * @param clockPosY  clock positionY
   * @param clockPanel clock panel
   */
  public ClockView(final int clockPosX, final int clockPosY,
                   final JPanel clockPanel) {
    this.posX = clockPosX;
    this.posY = clockPosY;
    this.panel = clockPanel;
  }

  /**
   * Create clockShape.
   *
   * @param g2D graphics component
   */
  public void display(final Graphics2D g2D) {

    for (int i = 1; i <= NUMBER_HOURS; i++) {
      g2D.drawLine(posX, posY + CLOCK_SIZE - CLOCK_MARK_SIZE, posX,
          posY + CLOCK_SIZE);
      g2D.rotate(Math.PI / PI_HOURS, posX, posY);
    }
    g2D.setColor(Color.BLUE);
    g2D.rotate(minutes * Math.PI / PI_MINUTES, posX, posY);
    g2D.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND));
    g2D.drawLine(posX, posY, posX, posY - MINUTE_SIZE);

    g2D.rotate(2 * Math.PI - minutes * Math.PI / PI_MINUTES, posX, posY);
    g2D.rotate(hour * Math.PI / PI_HOURS, posX, posY);
    g2D.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND));
    g2D.drawLine(posX, posY, posX, posY - HOUR_SIZE);

    g2D.setColor(Color.BLACK);
    g2D.fillOval(posX - STROKE_WIDTH, posY - STROKE_WIDTH,
        CLOCK_WIDTH_HEIGHT, CLOCK_SIZE);
  }

  /**
   * get the hour.
   *
   * @return int hour
   */
  public int getHour() {
    return hour;
  }

  /**
   * set the hour.
   *
   * @param hourToSet hour on the clock
   */
  public void setHour(final int hourToSet) {
    this.hour = hourToSet;
  }

  /**
   * get the minutes.
   *
   * @return int minutes
   */
  public int getMinutes() {
    return minutes;
  }

  /**
   * set the minutes.
   *
   * @param minutesToSet on the clock
   */
  public void setMinutes(final int minutesToSet) {
    this.minutes = minutesToSet;
  }

  /**
   * get panel of the clock.
   *
   * @return JPanel panel
   */
  public JPanel getPanel() {
    return panel;
  }

  /**
   * set the panel of the clock.
   *
   * @param panelToSet panel which contain the clock
   */
  public void setPanel(final JPanel panelToSet) {
    this.panel = panelToSet;
  }

  /**
   * get Clock posX.
   *
   * @return int posX
   */
  public int getPosX() {
    return posX;
  }

  /**
   * set Clock posX.
   *
   * @param clockPosX clock positionX
   */
  public void setPosX(final int clockPosX) {
    this.posX = clockPosX;
  }

  /**
   * get Clock posY.
   *
   * @return int posY
   */
  public int getPosY() {
    return posY;
  }

  /**
   * set Clock posY.
   *
   * @param clockPosY clock positionY
   */
  public void setPosY(final int clockPosY) {
    this.posY = clockPosY;
  }
}
