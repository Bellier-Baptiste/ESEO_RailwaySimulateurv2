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

package org.example.model;

import org.example.controller.ActionFile;
import org.example.data.Data;

import java.awt.Color;
import java.util.List;

/**
 * Model Class to describe a line.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file Line.java
 * @date N/A
 * @since 2.0
 */
public class Line {
  /** Line name. */
  private final String name;
  //attributes
  /** Line id. */
  private int id;
  /** Line color. */
  private Color color;
  /** Line stations. */
  private List<Station> stations;

  /**
   * Line constructor.
   *
   * @param lineId       line id
   * @param lineStations stations of the line
   */
  public Line(final int lineId, final List<Station> lineStations) {
    super();
    this.id = lineId;
    this.color = Data.getInstance().getLinesColors()[id];
    this.stations = lineStations;
    this.name = ActionFile.toAlphabetic(this.id);
  }

  //accessors

  /**
   * get the id of the line.
   *
   * @return line Id
   */
  public int getId() {
    return id;
  }

  /**
   * set the id of the line.
   *
   * @param lineId line id
   */
  public void setId(final int lineId) {
    this.id = lineId;
  }

  /**
   * get the color of the line.
   *
   * @return color
   */
  public Color getColor() {
    return color;
  }

  /**
   * set the color of the line.
   *
   * @param lineColor color of the line
   */
  public void setColor(final Color lineColor) {
    this.color = lineColor;
  }

  /**
   * get the name of the line.
   *
   * @return name of the line
   */
  public String getName() {
    return name;
  }

  /**
   * get the stations of the line.
   *
   * @return list of the stations
   */
  public List<Station> getStations() {
    return stations;
  }

  /**
   * set the stations of the line.
   *
   * @param lineStations stations of the line
   */
  public void setStations(final List<Station> lineStations) {
    this.stations = lineStations;
  }

  //method

  /**
   * Add a station to complete the line.
   *
   * @param station station to add on the line
   */
  public void addStation(final Station station) {
    this.stations.add(station);
  }


}
