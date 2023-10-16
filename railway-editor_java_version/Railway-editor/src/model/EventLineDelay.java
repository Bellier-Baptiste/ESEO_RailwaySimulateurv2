/** Class part of the model package of the application. */

package model;

import controller.EventName;

/**
 * Model class extending event which describes a delay between 2 stations.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class EventLineDelay extends EventBetween2Stations {
  /** Delay in minutes for attendance peak events. */
  private int delay;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventLineDelay(final int id, final String startTime,
                        final String endTime, final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.LINE_DELAYED);
  }

  /**
   * get the delay in second.
   *
   * @return int delay
   */
  public int getDelay() {
    return delay;
  }

  /**
   * set the delay.
   *
   * @param eventDelay event delay
   */
  public void setDelay(final int eventDelay) {
    this.delay = eventDelay;
  }
}
