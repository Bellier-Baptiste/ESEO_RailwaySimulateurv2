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
 * Model class extending {@link Event} which describes an attendance peak on a
 * Station
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @author Alexis BONAMY
 * @file EventAttendancePeak.java
 * @date 2023/11/12
 * @since 2.0
 */
public class EventAttendancePeak extends Event {
  /** Station id. */
  private int idStation;
  /** Peak size. */
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
   */
  public EventAttendancePeak(final int id, final String startTime,
                             final String endTime,
                             final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.ATTENDANCE_PEAK);
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
   * @param peakTime time of the peak
   */
  public void setPeakTime(final String peakTime) {
    // check if the peaktime is valid and between the start and end time
    if (isPeakTimeValid(peakTime)) {
      this.peakTime = peakTime;
    } else {
      throw new IllegalArgumentException("Peak time is not valid");
    }
  }

  /**
   * check if peak time is between start time and end time. The peak time should
   * be equal or after the start time and equal or before the end time.
   * @param peakTime peak time
   */
  private boolean isPeakTimeValid(final String peakTime) {
    try {
      DateTimeFormatter formatter =
              DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
      LocalDateTime peakTimeDate = LocalDateTime.parse(
              peakTime, formatter);
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
