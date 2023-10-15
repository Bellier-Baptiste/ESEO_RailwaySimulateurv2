/** Class part of the model package of the application. */

package model;

import controller.EventName;

/**
 * Model class extends EventBetween2Lines which describes the closing of a line
 * portion.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class EventLineClosed extends EventBetween2Stations {

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventLineClosed(final int id, final String startTime,
                         final String endTime, final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.LINE_CLOSED);
  }
}
