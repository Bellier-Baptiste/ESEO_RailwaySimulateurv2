package model;

import controller.EventName;

/**
 * Model class extending event which describes a delay between 2 stations.
 *
 * @author arthu
 */
public class EventLineDelay extends EventBetween2Stations {
  private int delay;

  /**
   * Constructor.
   *
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventLineDelay(int id, String startTime, String endTime,
                        EventType type) {
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
   * @param delay event delay
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }
}
