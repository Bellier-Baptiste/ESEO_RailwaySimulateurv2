/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 * A class for creating events {@link model.Event} on the map.
 *
 * @see view.EventWindow
 * @see view.EventRecap
 * @see ListEventPanel
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ActionMetroEvent.java
 * @date 2023-10-02
 * @since 3.0
 */
public class ActionMetroEvent {
  /**
   * Add event action name.
   */
  public static final String ADD_EVENT = "ADD_EVENT";
  /** Event window x offset. */
  private static final int EVENT_WINDOW_X_OFFSET = 200;
  /** Event window y offset. */
  private static final int EVENT_WINDOW_Y_OFFSET = 20;
  /** Starting date index. */
  private static final int STARTING_DATE_INDEX = 0;
  /** Starting time index for train hour. */
  private static final int STARTING_TIME_INDEX_TRAIN_HOUR = 0;
  /** Ending time index for train hour. */
  private static final int ENDING_TIME_INDEX_TRAIN_HOUR = 1;
  /** Starting time index. */
  private static final int STARTING_TIME_INDEX = 1;
  /** Ending date index. */
  private static final int ENDING_DATE_INDEX = 2;
  /** Train hour line index. */
  private static final int TRAIN_HOUR_LINE_INDEX = 2;
  /** Ending time index. */
  private static final int ENDING_TIME_INDEX = 3;
  /** Train hour train number index. */
  private static final int TRAIN_HOUR_TRAIN_NUMBER_INDEX = 3;
  /** Starting station index. */
  private static final int STARTING_STATION_INDEX = 4;
  /** Station concerned index. */
  private static final int STATION_CONCERNED_INDEX = 4;
  /** Ending station index. */
  private static final int ENDING_STATION_INDEX = 5;
  /** Size index. */
  private static final int PEAK_NUMBER_INDEX = 5;
  /** Delay index. */
  private static final int DELAY_INDEX = 6;
  /** Number of minutes in an hour. */
  private static final int MINUTES_IN_HOUR = 60;
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
    EventWindow.getInstance().setLocation(
        MainWindow.getInstance().getX() + EVENT_WINDOW_X_OFFSET,
        MainWindow.getInstance().getY() + EVENT_WINDOW_Y_OFFSET);
    EventWindow.getInstance().setVisible(true);
  }

  /** Add a line delay event. */
  public void addLineDelay() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventLineDelayToString();
    String[] eventStringTab = eventString.split(",");
    String startTime =
        eventStringTab[STARTING_DATE_INDEX] + "-"
            + eventStringTab[STARTING_TIME_INDEX];
    String endTime = eventStringTab[ENDING_DATE_INDEX] + "-"
            + eventStringTab[ENDING_TIME_INDEX];
    int delay = Integer.parseInt(eventStringTab[DELAY_INDEX].split(":")[0])
        * MINUTES_IN_HOUR + Integer.parseInt(eventStringTab[DELAY_INDEX]
        .split(":")[1]);
    EventLineDelay eventLineDelay = new EventLineDelay(this.getCurrentId(),
        startTime, endTime, Event.EventType.LINE);
    eventLineDelay.setIdStationStart(Integer.parseInt(eventStringTab[
        STARTING_STATION_INDEX]));
    eventLineDelay.setIdStationEnd(Integer.parseInt(eventStringTab[
        ENDING_STATION_INDEX]));
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
    String startTime =
        eventStringTab[STARTING_DATE_INDEX] + "-" + eventStringTab[
            STARTING_TIME_INDEX];
    String endTime =
        eventStringTab[ENDING_DATE_INDEX] + "-" + eventStringTab[
            ENDING_TIME_INDEX];
    EventLineClosed eventLineClosed = new EventLineClosed(this.getCurrentId(),
        startTime, endTime, Event.EventType.LINE);
    eventLineClosed.setIdStationStart(Integer.parseInt(eventStringTab[
        STARTING_STATION_INDEX]));
    eventLineClosed.setIdStationEnd(Integer.parseInt(eventStringTab[
        ENDING_STATION_INDEX]));
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
    String startTime =
        eventStringTab[STARTING_DATE_INDEX] + "-" + eventStringTab[
            STARTING_TIME_INDEX];
    String endTime =
        eventStringTab[ENDING_DATE_INDEX] + "-" + eventStringTab[
            ENDING_TIME_INDEX];
    EventAttendancePeak eventAttendancePeak = new EventAttendancePeak(
        this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventAttendancePeak.setIdStation(Integer.parseInt(eventStringTab[
        STATION_CONCERNED_INDEX]));
    eventAttendancePeak.setSize(Integer.parseInt(eventStringTab[
        PEAK_NUMBER_INDEX]));
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
          Integer.toString(stationConcerned.getId()),
          eventStringTab[PEAK_NUMBER_INDEX]);
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
    String startTime = eventStringTab[STARTING_TIME_INDEX_TRAIN_HOUR];
    String endTime = eventStringTab[ENDING_TIME_INDEX_TRAIN_HOUR];
    EventHour eventHour = new EventHour(this.getCurrentId(), startTime,
        endTime, Event.EventType.LINE);
    eventHour.setIdLine(Integer.parseInt(eventStringTab[
        TRAIN_HOUR_LINE_INDEX]));
    eventHour.setTrainNumber(Integer.parseInt(eventStringTab[
        TRAIN_HOUR_TRAIN_NUMBER_INDEX]));
    Data.getInstance().getEventList().add(eventHour);
    EventWindow.getInstance().dispatchEvent(new WindowEvent(
        EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    MainWindow.getInstance().getEventRecapPanel().createEventHour(
        this.getCurrentId(), startTime, endTime, eventStringTab[2],
        eventStringTab[TRAIN_HOUR_TRAIN_NUMBER_INDEX]);
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
    String startTime =
        eventStringTab[STARTING_DATE_INDEX] + "-"
            + eventStringTab[STARTING_TIME_INDEX];
    String endTime =
        eventStringTab[ENDING_DATE_INDEX] + "-"
            + eventStringTab[ENDING_TIME_INDEX];
    EventStationClosed eventStationClosed = new EventStationClosed(
        this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventStationClosed.setIdStation(Integer.parseInt(eventStringTab[
        STATION_CONCERNED_INDEX]));
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

