package model;

/**
 * Model class which describes a generic event between two stations.
 */
public abstract class EventBetween2Stations extends Event {
  private int idStationStart;
  private int idStationEnd;

  /**
   * EventBetween2Stations constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event type
   */
  protected EventBetween2Stations(int id, String startTime, String endTime,
                                  EventType type) {
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
   * @param idStationStart event stationStart Id
   */
  public void setIdStationStart(int idStationStart) {
    this.idStationStart = idStationStart;
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
   * @param idStationEnd event stationEnd Id
   */
  public void setIdStationEnd(int idStationEnd) {
    this.idStationEnd = idStationEnd;
  }
}
