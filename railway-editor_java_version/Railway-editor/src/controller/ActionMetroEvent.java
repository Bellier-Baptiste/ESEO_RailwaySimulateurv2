package controller;

import Model.*;
import Model.Event;
import data.Data;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ActionMetroEvent {
  public static final String ADD_EVENT = "ADD_EVENT";
  private static ActionMetroEvent instance;
  private int currentId = 0;

  /**Create Singleton
   * @return ActionMetroEvent instance
   */
  public static ActionMetroEvent getInstance() {
    if (instance == null) {
      instance = new ActionMetroEvent();
    }
    return instance;
  }

  private boolean isInDelta(Station station, Station stationStart, Station stationEnd, Line line) {
    if (line.getStations().contains(station)) {
      if (line.getStations().indexOf(station) >= line.getStations().indexOf(stationStart)
          && line.getStations().indexOf(station) <= line.getStations().indexOf(stationEnd)) {
        return true;
      } else {
        return false;
      }
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

  public void addEvent() {
    EventWindow.getInstance().setLocation(MainWindow.getInstance().getX() + 200, MainWindow.getInstance().getY() + 20);
    EventWindow.getInstance().setVisible(true);
  }

  public void addLineDelay() {
    MainWindow.getInstance().toFront();
    String eventString =ListEventPanel.getInstance().eventLineDelayToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventLineDelay eventLineDelay = new EventLineDelay(ActionMetroEvent.this.getCurrentId(),startTime, endTime, Event.EventType.LINE);
    eventLineDelay.setIdStationStart(Integer.valueOf(eventStringTab[4]));
    eventLineDelay.setIdStationEnd(Integer.valueOf(eventStringTab[5]));
    int delay = Integer.valueOf(eventStringTab[6].split(":")[0]) * 60
        + Integer.valueOf(eventStringTab[6].split(":")[1]);
    eventLineDelay.setDelay(delay);
    Data.getInstance().getEventList().add(eventLineDelay);

    Station stationStart = null;
    Station stationEnd = null;
    LineView lineDelayed = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventLineDelay.getIdStationStart()) {
          lineDelayed = lineView;
          stationStart = stationView.getStation();
        } else if (stationView.getStation().getId() == eventLineDelay.getIdStationEnd()) {
          stationEnd = stationView.getStation();
        }
      }
    }
    if (lineDelayed != null && stationStart != null && stationEnd != null) {
      if (stationEnd.getId() < stationStart.getId()) {
        Station aux = stationEnd;
        stationEnd = stationStart;
        stationStart = aux;
      }
      for (StationView stationView : lineDelayed.getStationViews()) {
        if (ActionMetroEvent.this.isInDelta(stationView.getStation(), stationStart, stationEnd, lineDelayed.getLine())) {
          stationView.setCenterCircleColor(Color.ORANGE);
        }
      }
      EventWindow.getInstance().dispatchEvent(new WindowEvent(EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
      MainWindow.getInstance().getMainPanel().repaint();

      String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
//        int id = Data.getInstance().getEventList().size();
      MainWindow.getInstance().getEventRecapPanel().createEventLineDelayed(ActionMetroEvent.this.getCurrentId(),startTime, endTime, locationsStr,
          eventStringTab[6], Integer.toString(lineDelayed.getLine().getId()));
      MainWindow.getInstance().getEventRecapPanel().revalidate();
      ActionMetroEvent.this.incrementCurrentId();
    }
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  public void addLineClosed() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventLineClosedToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventLineClosed eventLineClosed = new EventLineClosed(ActionMetroEvent.this.getCurrentId(), startTime, endTime, Event.EventType.LINE);
    eventLineClosed.setIdStationStart(Integer.valueOf(eventStringTab[4]));
    eventLineClosed.setIdStationEnd(Integer.valueOf(eventStringTab[5]));
    Data.getInstance().getEventList().add(eventLineClosed);

    Station stationStart = null;
    Station stationEnd = null;
    LineView lineDelayed = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventLineClosed.getIdStationStart()) {
          lineDelayed = lineView;
          stationStart = stationView.getStation();
        } else if (stationView.getStation().getId() == eventLineClosed.getIdStationEnd()) {
          stationEnd = stationView.getStation();
        }
      }
    }
    if (lineDelayed != null && stationStart != null && stationEnd != null) {
      if (stationEnd.getId() < stationStart.getId()) {
        Station aux = stationEnd;
        stationEnd = stationStart;
        stationStart = aux;
      }
      for (StationView stationView : lineDelayed.getStationViews()) {
        if (isInDelta(stationView.getStation(), stationStart, stationEnd, lineDelayed.getLine())) {
          stationView.setCenterCircleColor(Color.RED);
        }
      }
      EventWindow.getInstance().dispatchEvent(new WindowEvent(EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    }
    MainWindow.getInstance().getMainPanel().repaint();
    String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
//      int id = Data.getInstance().getEventList().size();
    MainWindow.getInstance().getEventRecapPanel().createEventLineClosed(ActionMetroEvent.this.getCurrentId(),startTime, endTime, locationsStr,
        eventStringTab[5]);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    ActionMetroEvent.this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  public void addAttendancePeak() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventAttendancePeakToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventAttendancePeak eventAttendancePeak = new EventAttendancePeak(ActionMetroEvent.this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventAttendancePeak.setIdStation(Integer.valueOf(eventStringTab[4]));
    eventAttendancePeak.setSize(Integer.parseInt(eventStringTab[5]));
    Data.getInstance().getEventList().add(eventAttendancePeak);

    Station stationConcerned = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventAttendancePeak.getIdStation()) {
          stationConcerned = stationView.getStation();
          stationView.setCenterCircleColor(Color.YELLOW);
        }
      }
    }
    EventWindow.getInstance().dispatchEvent(new WindowEvent(EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    int id = Data.getInstance().getEventList().size();
    MainWindow.getInstance().getEventRecapPanel().createEventAttendancePeak(ActionMetroEvent.this.getCurrentId(), startTime, endTime,
        Integer.toString(stationConcerned.getId()), eventStringTab[5]);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    ActionMetroEvent.this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  public void addTrainHour() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventTrainHourToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0];
    String endTime = eventStringTab[1];
    EventHour eventHour = new EventHour(ActionMetroEvent.this.getCurrentId(),startTime, endTime, Event.EventType.LINE);
    eventHour.setIdLine(Integer.valueOf(eventStringTab[2]));
    eventHour.setTrainNumber(Integer.parseInt(eventStringTab[3]));
    Data.getInstance().getEventList().add(eventHour);
    EventWindow.getInstance().dispatchEvent(new WindowEvent(EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    //int id = Data.getInstance().getEventList().size();
    MainWindow.getInstance().getEventRecapPanel().createEventHour(ActionMetroEvent.this.getCurrentId(),startTime, endTime, eventStringTab[2],
        eventStringTab[3]);
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    ActionMetroEvent.this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }

  public void addStationClosed() {
    MainWindow.getInstance().toFront();
    String eventString = ListEventPanel.getInstance().eventStationClosedToString();
    String[] eventStringTab = eventString.split(",");
    String startTime = eventStringTab[0] + "_" + eventStringTab[1];
    String endTime = eventStringTab[2] + "_" + eventStringTab[3];
    EventStationClosed eventStationClosed = new EventStationClosed(ActionMetroEvent.this.getCurrentId(), startTime, endTime, Event.EventType.STATION);
    eventStationClosed.setIdStation(Integer.valueOf(eventStringTab[4]));
    Data.getInstance().getEventList().add(eventStationClosed);
    Station stationConcerned = null;
    for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (stationView.getStation().getId() == eventStationClosed.getIdStation()) {
          stationConcerned = stationView.getStation();
          stationView.setCenterCircleColor(Color.RED);
        }
      }
    }

    EventWindow.getInstance().dispatchEvent(new WindowEvent(EventWindow.getInstance(), WindowEvent.WINDOW_CLOSING));
    MainWindow.getInstance().getMainPanel().repaint();
    int id = Data.getInstance().getEventList().size();
    MainWindow.getInstance().getEventRecapPanel().createEventStationClosed(ActionMetroEvent.this.getCurrentId(),startTime, endTime,
        Integer.toString(stationConcerned.getId()));
    MainWindow.getInstance().getEventRecapPanel().revalidate();
    ActionMetroEvent.this.incrementCurrentId();
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
  }
}

