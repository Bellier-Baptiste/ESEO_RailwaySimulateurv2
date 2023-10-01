package controller;

public enum EventName {
  LINE_DELAYED("LINE_DELAYED"),
  LINE_CLOSED("LINE_CLOSED"),
  ATTENDANCE_PEAK("ATTENDANCE_PEAK"),
  TRAIN_HOUR("TRAIN_HOUR"),
  STATION_CLOSED("STATION_CLOSED");

  private final String valeur;

  EventName(String valeur) {
    this.valeur = valeur;
  }

  public String getString() {
    return valeur;
  }
}
