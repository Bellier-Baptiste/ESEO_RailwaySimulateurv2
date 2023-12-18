package org.example.unittests.testcontroller;

import org.example.controller.ActionMetroEvent;
import org.example.model.*;
import org.example.model.Event;
import org.example.view.LineView;
import org.example.view.MainPanel;
import org.example.view.MainWindow;
import org.example.view.StationView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * Test-cases of creating events.
 *
 * @author Marie Bordet
 * @file ActionMetroEventTest.java
 * @date 2023-12-12
 * @since 3.0
 */
class ActionMetroEventTest {

  /**
   *  Test the addEventBetween2Stations method when the event is a station closed
   *
   *  @throws Exception if an error occurs
   */
  @Test
  void testAddEventBetween2StationsMultipleStationsClosed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    //Creation of the data
    EventMultipleStationsClosed event = new EventMultipleStationsClosed(0, "2023/12/12-10:00", "2023/12/12-15:00",
        Event.EventType.LINE);
    event.setIdStationStart(0);
    event.setIdStationEnd(1);
    Color color = new Color(255,0,0);
    String[] string = {"2023/12/14", "10:00", "2023/12/14", "15:00", "0", "1"};
    Station station1 = new Station(0, 1, 1, "1");
    Station station2 = new Station(1, 2, 2, "2");
    List<Station> stationList = new ArrayList<>();
    stationList.add(station1);
    stationList.add(station2);
    List<StationView> stationViewList = new ArrayList<>();
    stationViewList.add(new StationView(station1));
    stationViewList.add(new StationView(station2));
    LineView line1 = new LineView(new Line(0, stationList), stationViewList);
    List<LineView> lineViewList = new ArrayList<>();
    lineViewList.add(line1);

    //Introspection of the lineViews
    Field field = MainPanel.class.getDeclaredField("lineViews");
    field.setAccessible(true);
    field.set(MainWindow.getInstance().getMainPanel(), lineViewList);

    //Introspection of the method addEventBetween2Stations
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "addEventBetween2Stations", EventBetween2Stations.class, Color.class, String[].class);
    method.setAccessible(true);
    method.invoke(ActionMetroEvent.getInstance(), event, color, string);
    Assertions.assertEquals(color, MainWindow.getInstance().getMainPanel().getLineViews().get(0).getStationViews().get(0).getCenterCircleColor());

  }

  /**
  * Test the addEventBetween2Stations method when the event is a station closed and the station start
  * and the station end are on the same line
  *
  * @throws Exception if an error occurs
   */
  @Test
  void testAddEventBetween2StationsMultipleStationsClosedError() throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
    //Creation of the data
    EventMultipleStationsClosed event = new EventMultipleStationsClosed(1, "2023/12/12-10:00", "2023/12/12-15:00",
        Event.EventType.LINE);
    event.setIdStationStart(1);
    event.setIdStationEnd(2);
    Color color = new Color(255,0,0);
    String[] string = {"2023/12/14", "10:00", "2023/12/14", "15:00", "1", "2"};
    Station station1 = new Station(1, 1, 1, "1");
    Station station2 = new Station(2, 2, 2, "2");
    List<Station> stationList1 = new ArrayList<>();
    stationList1.add(station1);
    List<StationView> stationViewList1 = new ArrayList<>();
    stationViewList1.add(new StationView(station1));
    LineView line1 = new LineView(new Line(1, stationList1), stationViewList1);
    List<Station> stationList2 = new ArrayList<>();
    stationList2.add(station2);
    List<StationView> stationViewList2 = new ArrayList<>();
    stationViewList2.add(new StationView(station2));
    LineView line2 = new LineView(new Line(2, stationList2), stationViewList2);
    List<LineView> lineViewList = new ArrayList<>();
    lineViewList.add(line1);
    lineViewList.add(line2);

    //Introspection of the lineViews
    Field field = MainPanel.class.getDeclaredField("lineViews");
    field.setAccessible(true);
    field.set(MainWindow.getInstance().getMainPanel(), lineViewList);

    //Introspection of the method addEventBetween2Stations
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "addEventBetween2Stations", EventBetween2Stations.class, Color.class, String[].class);
    method.setAccessible(true);
    InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () ->
    {method.invoke(ActionMetroEvent.getInstance(), event, color, string);});
    Assertions.assertEquals("The stations must be on the same line", exception.getCause().getMessage());

  }


  /**
   * Test the addEventBetween2Stations method when the event is a lineDelay
   *
   * @throws Exception if an error occurs
   */
  @Test
  void testAddEventBetween2StationsLineDelay() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    //Creation of the data
    EventLineDelay event = new EventLineDelay(0, "2023/12/12-10:00", "2023/12/12-15:00",
        Event.EventType.LINE);
    event.setIdStationStart(0);
    event.setIdStationEnd(1);
    Color color = new Color(255,0,0);
    String[] string = {"2023/12/14", "10:00", "2023/12/14", "15:00", "0", "1"};
    Station station1 = new Station(0, 1, 1, "1");
    Station station2 = new Station(1, 2, 2, "2");
    List<Station> stationList = new ArrayList<>();
    stationList.add(station1);
    stationList.add(station2);
    List<StationView> stationViewList = new ArrayList<>();
    stationViewList.add(new StationView(station1));
    stationViewList.add(new StationView(station2));
    LineView line1 = new LineView(new Line(0, stationList), stationViewList);
    List<LineView> lineViewList = new ArrayList<>();
    lineViewList.add(line1);

    //Introspection of the lineViews
    Field field = MainPanel.class.getDeclaredField("lineViews");
    field.setAccessible(true);
    field.set(MainWindow.getInstance().getMainPanel(), lineViewList);

    //Introspection of the method addEventBetween2Stations
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "addEventBetween2Stations", EventBetween2Stations.class, Color.class, String[].class);
    method.setAccessible(true);
    method.invoke(ActionMetroEvent.getInstance(), event, color, string);
    Assertions.assertEquals(color, MainWindow.getInstance().getMainPanel().getLineViews().get(0).getStationViews().get(0).getCenterCircleColor());

  }

  /**
   * Test the addEventBetween2Stations method when the event is a lineDelay and the station start and the station end
   * are not on the same line
   *
   * @throws Exception if an error occurs
   */
  @Test
  void testAddEventBetween2StationsLineDelayError() throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
    //Creation of the data
    EventLineDelay event = new EventLineDelay(1, "2023/12/12-10:00", "2023/12/12-15:00",
        Event.EventType.LINE);
    event.setIdStationStart(1);
    event.setIdStationEnd(2);
    Color color = new Color(255,0,0);
    String[] string = {"2023/12/14", "10:00", "2023/12/14", "15:00", "1", "2"};
    Station station1 = new Station(1, 1, 1, "1");
    Station station2 = new Station(2, 2, 2, "2");
    List<Station> stationList1 = new ArrayList<>();
    stationList1.add(station1);
    List<StationView> stationViewList1 = new ArrayList<>();
    stationViewList1.add(new StationView(station1));
    LineView line1 = new LineView(new Line(1, stationList1), stationViewList1);
    List<Station> stationList2 = new ArrayList<>();
    stationList2.add(station2);
    List<StationView> stationViewList2 = new ArrayList<>();
    stationViewList2.add(new StationView(station2));
    LineView line2 = new LineView(new Line(2, stationList2), stationViewList2);
    List<LineView> lineViewList = new ArrayList<>();
    lineViewList.add(line1);
    lineViewList.add(line2);

    //Introspection of the lineViews
    Field field = MainPanel.class.getDeclaredField("lineViews");
    field.setAccessible(true);
    field.set(MainWindow.getInstance().getMainPanel(), lineViewList);

    //Introspection of the method addEventBetween2Stations
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "addEventBetween2Stations", EventBetween2Stations.class, Color.class, String[].class);
    method.setAccessible(true);
    InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () ->
    {method.invoke(ActionMetroEvent.getInstance(), event, color, string);});
    Assertions.assertEquals("The stations must be on the same line", exception.getCause().getMessage());

  }

  /**
   * Test the checkLinesAndStations method in nominal case
   */
  @Test
  void testCheckLinesAndStations() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Station station1 = new Station(0, 1, 1, "0");
    Station station2 = new Station(1, 2, 2, "1");
    List<Station> stationList = new ArrayList<>();
    stationList.add(station1);
    stationList.add(station2);
    List<StationView> stationViewList = new ArrayList<>();
    stationViewList.add(new StationView(station1));
    stationViewList.add(new StationView(station2));
    LineView lineView = new LineView(new Line(0, stationList), stationViewList);
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "checkLinesAndStations", LineView.class, LineView.class, Station.class, Station.class);
    method.setAccessible(true);
    Station[] returnFunction = (Station[]) method.invoke(ActionMetroEvent.getInstance(), lineView, lineView, station1, station2);
    Assertions.assertEquals(station1.getId(), returnFunction[0].getId());
    Assertions.assertEquals(station2.getId(), returnFunction[1].getId());
  }

  /**
   * Test the checkLinesAndStations method when the station start is null
   */
  @Test
  void testCheckLinesAndStationsNullStation() throws NoSuchMethodException {
    Station station1 = null;
    Station station2 = new Station(1, 2, 2, "1");
    List<Station> stationList = new ArrayList<>();
    stationList.add(station1);
    stationList.add(station2);
    List<StationView> stationViewList = new ArrayList<>();
    stationViewList.add(new StationView(station1));
    stationViewList.add(new StationView(station2));
    LineView lineView = new LineView(new Line(0, stationList), stationViewList);
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "checkLinesAndStations", LineView.class, LineView.class, Station.class, Station.class);
    method.setAccessible(true);
    InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () ->
        {method.invoke(ActionMetroEvent.getInstance(), lineView, lineView, station1, station2);});
        Assertions.assertEquals("The stations or the line are empty", exception.getCause().getMessage());
    }

  /**
   * Test the checkLinesAndStations method when the line start and line end are null
   */
  @Test
  void testCheckLinesAndStationsNullLine() throws NoSuchMethodException {
    Station station1 = new Station(1, 2, 2, "0");
    Station station2 = new Station(1, 2, 2, "1");
    LineView lineView = null;
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "checkLinesAndStations", LineView.class, LineView.class, Station.class, Station.class);
    method.setAccessible(true);
    InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () ->
    {method.invoke(ActionMetroEvent.getInstance(), lineView, lineView, station1, station2);});
    Assertions.assertEquals("The stations or the line are empty", exception.getCause().getMessage());

  }

  /**
   * Test the checkLinesAndStations method when the station start and station end are not on the same line
   */
  @Test
  void testCheckLinesAndStationsStationsNotOnSameLine() throws NoSuchMethodException {
    Station station1 = new Station(0, 2, 2, "0");
    Station station2 = new Station(1, 2, 2, "1");
    List<Station> stationList1 = new ArrayList<>();
    stationList1.add(station1);
    List<StationView> stationViewList1 = new ArrayList<>();
    stationViewList1.add(new StationView(station1));
    LineView line1 = new LineView(new Line(1, stationList1), stationViewList1);
    List<Station> stationList2 = new ArrayList<>();
    stationList2.add(station2);
    List<StationView> stationViewList2 = new ArrayList<>();
    stationViewList2.add(new StationView(station2));
    LineView line2 = new LineView(new Line(2, stationList2), stationViewList2);
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "checkLinesAndStations", LineView.class, LineView.class, Station.class, Station.class);
    method.setAccessible(true);
    InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class, () ->
    {method.invoke(ActionMetroEvent.getInstance(), line1, line2, station1, station2);});
    Assertions.assertEquals("The stations must be on the same line", exception.getCause().getMessage());

  }

  /**
   * Test the checkLinesAndStations method when the station start and station end are inversed
   */
  @Test
  void testCheckLinesAndStationsInversedStations() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Station station1 = new Station(1, 1, 1, "0");
    Station station2 = new Station(0, 2, 2, "1");
    List<Station> stationList = new ArrayList<>();
    stationList.add(station1);
    stationList.add(station2);
    List<StationView> stationViewList = new ArrayList<>();
    stationViewList.add(new StationView(station1));
    stationViewList.add(new StationView(station2));
    LineView lineView = new LineView(new Line(0, stationList), stationViewList);
    Method method = ActionMetroEvent.class.getDeclaredMethod(
        "checkLinesAndStations", LineView.class, LineView.class, Station.class, Station.class);
    method.setAccessible(true);
    Station[] returnFunction = (Station[]) method.invoke(ActionMetroEvent.getInstance(), lineView, lineView, station1, station2);
    Assertions.assertEquals(station2.getId(), returnFunction[0].getId());
    Assertions.assertEquals(station1.getId(), returnFunction[1].getId());
  }


}
