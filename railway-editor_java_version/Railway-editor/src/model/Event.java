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

package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Model Class to describe a generic Event.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @author Benoît Vavasseur
 * @file Event.java
 * @date N/A
 * @since 2.0
 */
public abstract class Event {
  /**
   * Event starting time.
   */
  private String startTime;
  /**
   * Event ending time.
   */
  private String endTime;
  /**
   * Event id.
   */
  private int id;

  /**
   * Enum of the event types.
   */
  public enum EventType { LINE, STATION /*, AREA*/ }

  /**
   * Event type.
   */
  private EventType type;
  /**
   * Event name.
   */
  private EventName eventName;


  /**
   * Event constructor.
   *
   * @param eventId        event id
   * @param eventStartTime event startTime
   * @param eventEndTime   event endTime
   * @param eventType      event type
   */
  protected Event(final int eventId, final String eventStartTime,
                  final String eventEndTime, final EventType eventType) {
    super();
    if (isTimeValid(eventStartTime) && isTimeValid(eventEndTime)
        && isStartTimeAfterEndTime(eventStartTime, eventEndTime)) {
      this.startTime = eventStartTime;
      this.endTime = eventEndTime;
      this.type = eventType;
      this.id = eventId;
    } else {
      throw new IllegalArgumentException("Invalid start time or end time.");
    }
  }


  /**
   * get the startTime of the event.
   *
   * @return String startTime
   */
  public String getStartTime() {
    return startTime;
  }


  /**
   * Set the startTime of the event.
   *
   * @param startTimeToSet event startTime in format "yyyy/MM/dd_HH:mm"
   * @throws IllegalArgumentException if the provided time is invalid or after
   *                                  endTime
   */
  public void setStartTime(final String startTimeToSet) {
    if (isTimeValid(startTimeToSet) && isStartTimeAfterEndTime(startTimeToSet,
        this.endTime)) {
      this.startTime = startTimeToSet;
    } else {
      throw new IllegalArgumentException("Invalid start time.");
    }
  }


  /**
   * get the endTime of the event.
   *
   * @return String endTime
   */
  public String getEndTime() {
    return endTime;
  }


  /**
   * Set the endTime of the event.
   *
   * @param endTimeToSet event endTime in format "yyyy/MM/dd_HH:mm"
   * @throws IllegalArgumentException if the provided time is invalid
   *                                  or before startTime
   */
  public void setEndTime(final String endTimeToSet) {
    if (isTimeValid(endTimeToSet) && isStartTimeAfterEndTime(this.startTime,
        endTimeToSet)) {
      this.endTime = endTimeToSet;
    } else {
      throw new IllegalArgumentException("Invalid end time.");
    }
  }


  /**
   * get the type of event.
   *
   * @return EventType type
   */
  public EventType getType() {
    return type;
  }


  /**
   * set the type of the event.
   *
   * @param eventType event type
   */
  public void setType(final EventType eventType) {
    this.type = eventType;
  }


  /**
   * set the id of the event.
   *
   * @param eventId event id
   */
  public void setId(final int eventId) {
    this.id = eventId;
  }

  /**
   * get the id of the event.
   *
   * @return int id
   */
  public int getId() {
    return this.id;
  }

  /**
   * Set the event name.
   *
   * @param eventNameToSet event name
   */
  public void setEventName(final EventName eventNameToSet) {
    this.eventName = eventNameToSet;
  }

  /**
   * Get the name of the event.
   *
   * @return String eventName
   */
  public EventName getEventName() {
    return eventName;
  }

  /**
    * Parses a given string to a LocalDateTime using the custom format
    * "yyyy/MM/dd_HH:mm".
    *
    * @param time The date-time string to be parsed.
    * @return LocalDateTime representation of the provided string or null if
    *         parsing fails.
    */
  private LocalDateTime parseTime(final String time) {
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
    try {
      return LocalDateTime.parse(time, formatter);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /**
   * Checks if the provided date-time string is valid according to the custom
   * format "yyyy/MM/dd_HH:mm".
   *
   * @param time The date-time string to be validated.
   *
   * @return true if the string is a valid date-time, false otherwise.
   */
  private boolean isTimeValid(final String time) {
    return parseTime(time) != null;
  }

  /**
   * Checks if the provided startTime is after the provided endTime using
   * the custom format "yyyy/MM/dd_HH:mm".
   *
   * @param startTimeToCheck The start date-time string to be compared.
   * @param endTimeToCheck   The end date-time string to be compared.
   *
   * @return true if the startTime is after endTime, false otherwise or if
   *         either time is invalid.
   */
  private boolean isStartTimeAfterEndTime(final String startTimeToCheck,
                                          final String endTimeToCheck) {
    LocalDateTime start = parseTime(startTimeToCheck);
    LocalDateTime end = parseTime(endTimeToCheck);
    return start == null || end == null || !start.isAfter(end);
  }


}
