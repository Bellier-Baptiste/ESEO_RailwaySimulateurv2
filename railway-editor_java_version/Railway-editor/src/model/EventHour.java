/** Class part of the model package of the application. */

package model;

import controller.EventName;

/**
 * Model class extending event which describes a change of trains number on a
 * line at a certain hour of the day.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class EventHour extends Event {
  /** Line id. */
  private int idLine;
  /** Number of trains to add. */
  private int trainNumber;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventHour(final int id, final String startTime, final String endTime,
                   final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.TRAIN_HOUR);
  }

  /**
   * get the id of the line concerned.
   *
   * @return int idLine
   */
  public int getIdLine() {
    return idLine;
  }

  /**
   * set the id of the line concerned.
   *
   * @param eventIdLine id of the line concerned
   */
  public void setIdLine(final int eventIdLine) {
    this.idLine = eventIdLine;
  }

  /**
   * get the new train number.
   *
   * @return int train number
   */
  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * set the new train number.
   *
   * @param eventTrainNumber new number of train
   */
  public void setTrainNumber(final int eventTrainNumber) {
    this.trainNumber = eventTrainNumber;
  }


}
