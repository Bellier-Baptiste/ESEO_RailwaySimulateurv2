/**
 * Class part of the model package of the application.
 */

package model;

import controller.EventName;

/**
 * Model class which describes a generic event.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
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
