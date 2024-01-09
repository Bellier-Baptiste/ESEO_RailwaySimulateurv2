package org.example.model;

/**
 * Model class extending {@link Event} which
 * describes a closure of a line {@link org.example.model.Line}.
 *
 * @author Marie Bordet
 * @file EventLineClosed.java
 * @date 2024-01-09
 * @since 3.0
 */

public class EventLineClosed extends Event {
  /**
   * Line id.
   */
  private int idLine;
  /**
   * Type of the line closure.
   */
  private LineClosureType closureType;

  /**
   * Constructor.
   *
   * @param id event id
   * @param startTime event startTime
   * @param endTime event endTime
   * @param type event eventType
   */
  public EventLineClosed(final int id, final String startTime, final String endTime,
      final EventType type) {
    super(id, startTime, endTime, type, EventName.LINE_CLOSED);
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
   * get the type of the line closure.
   *
   * @return LineClosureType type of the line closure
   */
  public LineClosureType getClosureType() {
    return closureType;
  }

  /**
   * set the type of the line closure.
   *
   * @param eventClosureType type of the line closure
   */
  public void setClosureType(final LineClosureType eventClosureType) {
    this.closureType = eventClosureType;
  }

}
