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
 * Model class extending {@link Event} which describes an attendance peak on a
 * {@link org.example.model.Station}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventAttendancePeak.java
 * @date N/A
 * @since 2.0
 */
public class EventAttendancePeak extends Event {
  /**
   * Station id.
   */
  private int idStation;
  /**
   * Peak size.
   */
  private int size;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      eventType
   */
  public EventAttendancePeak(final int id, final String startTime,
                             final String endTime,
                             final EventType type) {
    super(id, startTime, endTime, type, EventName.ATTENDANCE_PEAK);
  }

  /**
   * get the id of the station concerned by the peak.
   *
   * @return int id
   */
  public int getIdStation() {
    return idStation;
  }

  /**
   * set the id of the station concerned.
   *
   * @param eventIdStation id station concerned by the peak
   */
  public void setIdStation(final int eventIdStation) {
    this.idStation = eventIdStation;
  }

  /**
   * get the peak size.
   *
   * @return int size
   */
  public int getSize() {
    return size;
  }

  /**
   * set the peak size.
   *
   * @param peakSize size of the peak
   */
  public void setSize(final int peakSize) {
    this.size = peakSize;
  }
}
