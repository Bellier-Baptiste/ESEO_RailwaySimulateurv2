/** Class part of the model package of the application. */

package model;

/**
 * Model class which describes a generic event between two stations.
 */
public abstract class EventBetween2Stations extends Event {
  /** Id of the starting station. */
  private int idStationStart;
  /** Id of the ending station. */
  private int idStationEnd;

  /**
   * EventBetween2Stations constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event type
   */
  protected EventBetween2Stations(final int id, final String startTime,
                                  final String endTime, final EventType type) {
    super(id, startTime, endTime, type);
  }

  /**
   * get the id of the starting station.
   *
   * @return int idStationStart
   */
  public int getIdStationStart() {
    return idStationStart;
  }

  /**
   * get the id of the ending station.
   *
   * @param eventIdStationStart event stationStart Id
   */
  public void setIdStationStart(final int eventIdStationStart) {
    this.idStationStart = eventIdStationStart;
  }

  /**
   * get the id of the ending station.
   *
   * @return idStationEnd
   */
  public int getIdStationEnd() {
    return idStationEnd;
  }

  /**
   * set the id of the ending station.
   *
   * @param eventIdStationEnd event stationEnd Id
   */
  public void setIdStationEnd(final int eventIdStationEnd) {
    this.idStationEnd = eventIdStationEnd;
  }
}
