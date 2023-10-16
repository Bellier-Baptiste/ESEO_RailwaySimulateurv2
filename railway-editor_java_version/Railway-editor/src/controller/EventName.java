/**
 * Class part of the controller package of the application.
 */

package controller;

/**
 * Enum of the event names.
 */
public enum EventName {
  /**
   * Line delayed event name.
   */
  LINE_DELAYED("lineDelay"),
  /**
   * Line closed event name.
   */
  LINE_CLOSED("lineClosed"),
  /**
   * Attendance peak event name.
   */
  ATTENDANCE_PEAK("attendancePeak"),
  /**
   * Train hour event name.
   */
  TRAIN_HOUR("hour"),
  /**
   * Station closed event name.
   */
  STATION_CLOSED("stationClosed");

  /**
   * String value of the event name.
   */
  private final String value;

  /**
   * Constructor of the enum.
   *
   * @param valueToSet String value to set
   */
  EventName(final String valueToSet) {
    this.value = valueToSet;
  }

  /**
   * Get the string value of the event name.
   *
   * @return String value
   */
  public String getString() {
    return value;
  }
}
