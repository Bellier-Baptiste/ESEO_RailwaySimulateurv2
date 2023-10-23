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

import controller.EventName;

/**
 * Model Class to describe a generic Event.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
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
  public enum EventType { LINE, STATION, AREA }

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
    this.id = eventId;
    this.startTime = eventStartTime;
    this.endTime = eventEndTime;
    this.type = eventType;
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
   * set the startTime of the event.
   *
   * @param eventStartTime event startTime
   */
  public void setStartTime(final String eventStartTime) {
    this.startTime = eventStartTime;
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
   * set the endTime of the event.
   *
   * @param eventEndTime event endTime
   */
  public void setEndTime(final String eventEndTime) {
    this.endTime = eventEndTime;
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

  /** Set the event name.
   *
   * @param eventNameToSet event name
   */
  public void setEventName(final EventName eventNameToSet) {
    this.eventName = eventNameToSet;
  }

  /**
   * Get the event name.
   *
   * @return event name
   */
  public EventName getEventName() {
    return eventName;
  }
}
