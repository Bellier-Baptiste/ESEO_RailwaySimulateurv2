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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Model class extending {@link Event} which describes a ramp peak on a
 * Station.
 *
 * @author Alexis BONAMY
 * @file EventPeak.java
 * @date 08/12/2023
 * @since 3.0
 */
public class EventPeak extends Event {
  /**
   * Station id.
   */
  private int idStation;
  /**
   * Peak size.
   */
  private int size;
  /** Peak time. */
  private String peakTime;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      eventType
   * @param name      eventName
   */
  public EventPeak(final int id, final String startTime,
                       final String endTime,
                       final EventType type,
                       final EventName name) {
    super(id, startTime, endTime, type, name);
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
    if (peakSize < 0) {
      throw new IllegalArgumentException("Peak size is not valid");
    }
    this.size = peakSize;
  }

  /**
   * get the peak time.
   *
   * @return String peakTime
   */
  public String getPeakTime() {
    return peakTime;
  }

  /**
   * set the peak time.
   *
   * @param paramPeakTime time of the peak
   */
  public void setPeakTime(final String paramPeakTime) {
    if (isPeakTimeValid(paramPeakTime)) {
      this.peakTime = paramPeakTime;
    } else {
      throw new IllegalArgumentException("Peak time is not valid");
    }
  }

  /**
   * check if peak time is between start time and end time. The peak time should
   * be equal or after the start time and equal or before the end time.
   *
   * @param paramPeakTime peak time
   * @return boolean true if peak time is valid, false otherwise
   */
  private boolean isPeakTimeValid(final String paramPeakTime) {
    try {
      DateTimeFormatter formatter =
              DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
      LocalDateTime peakTimeDate = LocalDateTime.parse(
              paramPeakTime, formatter);
      LocalDateTime startTimeDate = LocalDateTime.parse(
              getStartTime(), formatter);
      LocalDateTime endTimeDate = LocalDateTime.parse(
              getEndTime(), formatter);

      return (peakTimeDate.isEqual(startTimeDate)
              || peakTimeDate.isAfter(startTimeDate))
              && (peakTimeDate.isEqual(endTimeDate)
              || peakTimeDate.isBefore(endTimeDate));
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}