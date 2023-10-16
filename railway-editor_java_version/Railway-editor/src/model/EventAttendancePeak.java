/** Class part of the model package of the application. */

package model;

import controller.EventName;

/**
 * Model class extending event which describes an attendance peak on a station.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class EventAttendancePeak extends Event {
  /** Station id. */
  private int idStation;
  /** Peak size. */
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
}
