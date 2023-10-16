/** Class part of the model package of the application. */

package model;

import controller.EventName;

/**
 * Model class extending event which describes a station closing.
 */
public class EventStationClosed extends Event {
  /** Station id. */
  private int idStation;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      eventType
   */
  public EventStationClosed(final int id, final String startTime,
                            final String endTime, final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.STATION_CLOSED);
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
}
