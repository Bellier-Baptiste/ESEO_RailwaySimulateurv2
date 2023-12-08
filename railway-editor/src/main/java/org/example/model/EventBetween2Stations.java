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

/**
 * Model class which describes a generic event between two
 * {@link org.example.model.Station}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventBetween2Stations.java
 * @date N/A
 * @since 2.0
 */
public abstract class EventBetween2Stations extends Event {
  /**
   * Id of the starting station.
   */
  private int idStationStart;
  /**
   * Id of the ending station.
   */
  private int idStationEnd;

  /**
   * EventBetween2Stations constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event type
   * @param name      event name
   */
  protected EventBetween2Stations(final int id, final String startTime,
                                  final String endTime, final EventType type,
                                  final EventName name) {
    super(id, startTime, endTime, type, name);
  }

  /**
   * get the id of the starting station.
   *
   * @return int idStationStart
   */
  public int getIdStationStart() {
    return idStationStart;
  }

  /**
   * get the id of the ending station.
   *
   * @param eventIdStationStart event stationStart Id
   */
  public void setIdStationStart(final int eventIdStationStart) {
    this.idStationStart = eventIdStationStart;
  }

  /**
   * get the id of the ending station.
   *
   * @return idStationEnd
   */
  public int getIdStationEnd() {
    return idStationEnd;
  }

  /**
   * set the id of the ending station.
   *
   * @param eventIdStationEnd event stationEnd Id
   */
  public void setIdStationEnd(final int eventIdStationEnd) {
    this.idStationEnd = eventIdStationEnd;
  }
}
