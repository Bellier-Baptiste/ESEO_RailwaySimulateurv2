package controller;

/** Enum of the event names. */
public enum EventName {
  LINE_DELAYED("lineDelay"),
  LINE_CLOSED("lineClosed"),
  ATTENDANCE_PEAK("attendancePeak"),
  TRAIN_HOUR("hour"),
  STATION_CLOSED("stationClosed");

  private final String valeur;

  EventName(String valeur) {
    this.valeur = valeur;
  }

  public String getString() {
    return valeur;
  }
}
