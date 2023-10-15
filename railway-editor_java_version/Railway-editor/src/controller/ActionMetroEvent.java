package controller;

import data.Data;
import model.Event;
import model.EventAttendancePeak;
import model.EventBetween2Stations;
import model.EventHour;
import model.EventLineClosed;
import model.EventLineDelay;
import model.EventStationClosed;
import model.Line;
import model.Station;
import view.EventRecap;
import view.EventWindow;
import view.LineView;
import view.ListEventPanel;
import view.MainWindow;
import view.StationView;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.WindowEvent;

/**
 * Controller class which handles the creation of events.
 */
public class ActionMetroEvent {
  /**
   * Add event action name.
   */
  public static final String ADD_EVENT = "ADD_EVENT";
  /**
   * Singleton instance.
   */
  private static ActionMetroEvent instance;
  /**
   * Current event id.
   */
  private int currentId = 0;

  /**
   * Create Singleton.
   *
   * @return ActionMetroEvent instance
   */
  public static ActionMetroEvent getInstance() {
    if (instance == null) {
      instance = new ActionMetroEvent();
    }
    return instance;
  }

  /**
   * check if a station id between the starting and ending station.
   *
   * @param station     station to check
   * @param stationStart starting station
   * @param stationEnd  ending station
   * @param line       line concerned
   *
   * @return true if the station is between the starting and ending station
   */

  private boolean isInDelta(final Station station, final Station stationStart,
                            final Station stationEnd, final Line line) {
    if (line.getStations().contains(station)) {
      return line.getStations().indexOf(station) >= line.getStations()
          .indexOf(stationStart)
          && line.getStations().indexOf(station) <= line.getStations()
          .indexOf(stationEnd);
    } else {
      return false;
    }
  }

  private int getCurrentId() {
    return currentId;
  }

  private void incrementCurrentId() {
    currentId += 1;
  }

  /**
   * Add event action.
   */
  public void addEvent() {
    EventWindow.getInstance().setLocation(MainWindow.getInstance().getX()
        + 200, MainWindow.getInstance().getY() + 20);
    EventWindow.getInstance().setVisible(true);
  }

  /** Add a line delay event. */
  public void addLineDelay() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventLineDelayToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    int delay = Integer.parseInt(eventStringTab[6].split(":")[0]) * 60
        + Integer.parseInt(eventStringTab[6].split(":")[1]);
    EventLineDelay eventLineDelay = new EventLineDelay(this.getCurrentId(),
        startTime, endTime, Event.EventType.LINE);
    eventLineDelay.setIdStationStart(Integer.parseInt(eventStringTab[4]));
    eventLineDelay.setIdStationEnd(Integer.parseInt(eventStringTab[5]));
    eventLineDelay.setDelay(delay);
    eventLineDelay.setEventName(EventName.LINE_DELAYED);
    Color eventColor = Color.ORANGE;
    this.addEventBetween2Stations(eventLineDelay, eventColor, eventStringTab);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  /** Add a line closed event. */
  public void addLineClosed() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventLineClosedToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventLineClosed eventLineClosed = new EventLineClosed(this.getCurrentId(),
        startTime, endTime, Event.EventType.LINE);
    eventLineClosed.setIdStationStart(Integer.parseInt(eventStringTab[4]));
    eventLineClosed.setIdStationEnd(Integer.parseInt(eventStringTab[5]));
    Color eventColor = Color.RED;
    this.addEventBetween2Stations(eventLineClosed, eventColor, eventStringTab);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  private void addEventBetween2Stations(final EventBetween2Stations event,
                                        final Color eventColor,
                                        final String[] eventStringTab) {
    assert event != null;
    Data.getInstance().getEventList().add(event);
    Station stationStart = null;
    Station stationEnd = null;
    LineView line = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == event
            .getIdStationStart()) {
          line = lineView;
          stationStart = stationView.getStation();
        } else if (stationView.getStation().getId() == event
            .getIdStationEnd()) {
          stationEnd = stationView.getStation();
        }
      }
    }
    if (line != null && stationStart != null && stationEnd != null) {
      if (stationEnd.getId() < stationStart.getId()) {
        Station aux = stationEnd;
        stationEnd = stationStart;
        stationStart = aux;
      }
      this.colorStationViews(line, stationStart, stationEnd,
          eventColor);
      EventWindow.getInstance().dispatchEvent(new WindowEvent(
          EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
      MainWindow.getInstance().getMainPanel().repaint();
      String locationsStr = "from " + stationStart.getName() + " to "
          + stationEnd.getName();
      if (event.getEventName() == EventName.LINE_DELAYED) {
        EventRecap.getInstance().createEventLineDelayed(this.getCurrentId(),
            event.getStartTime(), event.getEndTime(), locationsStr,
            eventStringTab[eventStringTab.length - 1],
            Integer.toString(line.getLine().getId()));
      } else if (event.getEventName() == EventName.LINE_CLOSED) {
        EventRecap.getInstance().createEventLineClosed(this.getCurrentId(),
            event.getStartTime(), event.getEndTime(), locationsStr,
            Integer.toString(line.getLine().getId()));
      }
    }
  }

  private void colorStationViews(final LineView line,
                                 final Station stationStart,
                                 final Station stationEnd,
                                 final Color eventColor) {
    for (StationView stationView : line.getStationViews()) {
      if (this.isInDelta(stationView.getStation(), stationStart, stationEnd,
          line.getLine())) {
        stationView.setCenterCircleColor(eventColor);
      }
    }
  }

  /** Add an attendance peak event. */
  public void addAttendancePeak() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance()
        .eventAttendancePeakToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventAttendancePeak eventAttendancePeak = new EventAttendancePeak(
        this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventAttendancePeak.setIdStation(Integer.parseInt(eventStringTab[4]));
    eventAttendancePeak.setSize(Integer.parseInt(eventStringTab[5]));
    Data.getInstance().getEventList().add(eventAttendancePeak);

    Station stationConcerned = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventAttendancePeak
            .getIdStation()) {
          stationConcerned = stationView.getStation();
          stationView.setCenterCircleColor(Color.YELLOW);
        }
      }
    }
    EventWindow.getInstance().dispatchEvent(new WindowEvent(
        EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    if (stationConcerned != null) {
      MainWindow.getInstance().getEventRecapPanel().createEventAttendancePeak(
          this.getCurrentId(), startTime, endTime,
          Integer.toString(stationConcerned.getId()), eventStringTab[5]);
    }
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  /** Add a train hour event. */
  public void addTrainHour() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventTrainHourToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0];
    String endTime = eventStringTab[1];
    EventHour eventHour = new EventHour(this.getCurrentId(), startTime,
        endTime, Event.EventType.LINE);
    eventHour.setIdLine(Integer.parseInt(eventStringTab[2]));
    eventHour.setTrainNumber(Integer.parseInt(eventStringTab[3]));
    Data.getInstance().getEventList().add(eventHour);
    EventWindow.getInstance().dispatchEvent(new WindowEvent(
        EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    MainWindow.getInstance().getEventRecapPanel().createEventHour(
        this.getCurrentId(), startTime, endTime, eventStringTab[2],
        eventStringTab[3]);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  /** Add a station closed event. */
  public void addStationClosed() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance()
        .eventStationClosedToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventStationClosed eventStationClosed = new EventStationClosed(
        this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventStationClosed.setIdStation(Integer.parseInt(eventStringTab[4]));
    Data.getInstance().getEventList().add(eventStationClosed);
    Station stationConcerned = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventStationClosed
            .getIdStation()) {
          stationConcerned = stationView.getStation();
          stationView.setCenterCircleColor(Color.RED);
        }
      }
    }

    EventWindow.getInstance().dispatchEvent(new WindowEvent(
        EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    if (stationConcerned != null) {
      MainWindow.getInstance().getEventRecapPanel().createEventStationClosed(
          this.getCurrentId(), startTime, endTime,
          Integer.toString(stationConcerned.getId()));
    }
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }
}

