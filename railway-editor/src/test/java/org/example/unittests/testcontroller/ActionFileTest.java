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
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.unittests.testcontroller;

import org.example.controller.ActionFile;
import org.example.data.Data;
import org.example.main.RailwayEditor;
import org.example.model.Area;
import org.example.model.Event;
import org.example.model.EventAttendancePeak;
import org.example.model.EventHour;
import org.example.model.EventMultipleStationsClosed;
import org.example.model.EventLineDelay;
import org.example.model.EventStationClosed;
import org.example.view.AreaView;
import org.example.view.MainWindow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test-cases of exporting and importing metro networks map files (xml).
 *
 * @author Aurélie Chamouleau
 * @file ActionFile.java
 * @date 2023-11-16
 * @since 3.0
 */
class ActionFileTest {

  private static Stream<Arguments> areaViewStream()
      throws ParserConfigurationException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document1 = documentBuilder.newDocument();
    Element station = document1.createElement("station");
    document1.appendChild(station);

    Document document2 = documentBuilder.newDocument();
    Element area = document2.createElement("area");
    document2.appendChild(area);

    Area areaParis = new Area(48.92430395329745, 2.23846435546875,
        48.78877122776646, 2.4444580078125);
    AreaView areaView = new AreaView(areaParis);
    return Stream.of(
        Arguments.of(document1, null, station),
        Arguments.of(document2, areaView, area)
    );
  }

  @ParameterizedTest
  @MethodSource("areaViewStream")
  void testExportDistributions(final Document document, final AreaView areaView,
                               final Element element)
      throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {

    Method method = ActionFile.class.getDeclaredMethod(
        "exportDistributions", Document.class, AreaView.class, Element.class);
    method.setAccessible(true);

    method.invoke(ActionFile.getInstance(), document, areaView, element);

    // Population distribution check
    NamedNodeMap populationAttributes = element.getFirstChild().getAttributes();
    for (int i = 0; i < populationAttributes.getLength(); i++) {
      Node populationAttribute = populationAttributes.item(i);
      String attrName = populationAttribute.getNodeName();
      String dataFieldName = Character.toUpperCase(attrName.charAt(0))
          + attrName.substring(1);
      int expectedValue = Area.getDefaultPopulationDistribution(dataFieldName);
      assertEquals(expectedValue,
          Integer.parseInt(populationAttribute.getNodeValue()), "The "
              + dataFieldName + "population distribution should be "
              + expectedValue);
    }

    NamedNodeMap destinationAttributes =
        element.getFirstChild().getAttributes();
    // Destination distribution check
    for (int i = 0; i < destinationAttributes.getLength(); i++) {
      Node destinationAttribute = destinationAttributes.item(i);
      String attrName = destinationAttribute.getNodeName();
      String dataFieldName = Character.toUpperCase(attrName.charAt(0))
          + attrName.substring(1);
      int expectedValue = Area.getDefaultPopulationDistribution(dataFieldName);
      assertEquals(expectedValue,
          Integer.parseInt(destinationAttribute.getNodeValue()),
          "The " + dataFieldName
              + "population distribution should be " + expectedValue);
    }
  }

  @Test
  void testImportMap() {
    RailwayEditor.main(new String[]{});
    File fileToImport = new File("src/test/java/org/example/"
        + "unittests/testcontroller/angers.xml");
    ActionFile.getInstance().importMap(fileToImport);

    // Check network location and zoom
    ICoordinate centerCoordinate =
        MainWindow.getInstance().getMainPanel().getPosition();
    int zoom = MainWindow.getInstance().getMainPanel().getZoom();

    assertEquals(47.47776807779759, centerCoordinate.getLat(),
        "The center of the map should be at latitude"
            + "47.47776807779759");
    assertEquals(-0.5544662475585938, centerCoordinate.getLon(),
        "The center of the map should be at longitude"
            + "-0.5544662475585938");
    assertEquals(13, zoom, "The zoom of the map should be 13");

    // Check number of elements
    assertEquals(4, MainWindow.getInstance().getMainPanel()
        .getLineViews().size(), "There should be 4 lineViews");
    assertEquals(3, MainWindow.getInstance().getMainPanel()
        .getAreaViews().size(), "There should be 3 areaViews");
    assertEquals(5, Data.getInstance().getEventList().size(),
        "There should be 5 events");
    int numberOfStations = MainWindow.getInstance().getMainPanel()
        .getLineViews().stream().mapToInt(lineView -> lineView.getStationViews()
            .size()).sum();
    assertEquals(45, numberOfStations,
        "There should be 45 stations");

    // Check number of station per line
    assertEquals(25, MainWindow.getInstance().getMainPanel()
            .getLineViews().get(0).getStationViews().size(),
        "There should be 25 stations in line 1");
    assertEquals(10, MainWindow.getInstance().getMainPanel()
            .getLineViews().get(1).getStationViews().size(),
        "There should be 10 stations in line 2");
    assertEquals(2, MainWindow.getInstance().getMainPanel()
            .getLineViews().get(2).getStationViews().size(),
        "There should be 2 stations in line 3");
    assertEquals(8, MainWindow.getInstance().getMainPanel()
            .getLineViews().get(3).getStationViews().size(),
        "There should be 8 stations in line 4");

    // Dates expected for the events
    List<List<String>> expectedArguments = new ArrayList<>();
    expectedArguments.add(Arrays.asList("2023/11/01-15:37",
        "2023/11/01-19:37"));
    expectedArguments.add(Arrays.asList("2023/11/01-21:56",
        "2023/11/01-21:56"));
    expectedArguments.add(Arrays.asList("2023/11/01-20:00",
        "2023/11/01-23:00"));
    expectedArguments.add(Arrays.asList("19:46", "22:46"));
    expectedArguments.add(Arrays.asList("2023/11/01-18:51",
        "2023/11/01-22:51"));

    // Check events imported
    for (int i = 0; i < Data.getInstance().getEventList().size(); i++) {
      Event event = Data.getInstance().getEventList().get(i);
      assertEquals(expectedArguments.get(i).get(0), event.getStartTime(),
          "The starting time of the event station closed should be "
              + expectedArguments.get(i).get(0));
      assertEquals(expectedArguments.get(i).get(1), event.getEndTime(),
          "The ending time of the event station closed should be "
              + expectedArguments.get(i).get(1));
      switch (event.getEventName()) {
        case STATION_CLOSED:
          assertEquals(12, ((EventStationClosed) event)
                  .getIdStation(), "The station concerned by this event should"
              + " be the number 12");
          break;
        case MULTIPLE_STATIONS_CLOSED:
          assertEquals(39, ((EventMultipleStationsClosed) event)
                  .getIdStationStart(), "The starting station concerned by "
              + "this event should be the number 39");
          assertEquals(41, ((EventMultipleStationsClosed) event)
                  .getIdStationEnd(), "The ending station concerned by this "
              + "event should be the number 41");
          break;
        case LINE_DELAYED:
          assertEquals(4, ((EventLineDelay) event)
                  .getIdStationStart(), "The starting station concerned by "
              + "this event should be the number 4");
          assertEquals(7, ((EventLineDelay) event)
                  .getIdStationEnd(), "The ending station concerned by this "
              + "event should be the number 7");
          assertEquals(60, ((EventLineDelay) event)
                  .getDelay(), "The delay of this event should be 60 minutes");
          break;
        case TRAIN_HOUR:
          assertEquals(0, ((EventHour) event)
                  .getIdLine(), "The line concerned by this event should"
              + " be the number 0");
          assertEquals(13, ((EventHour) event)
                  .getTrainNumber(), "The number of trains added to this line "
              + "should be the number 13");
          break;
        case ATTENDANCE_PEAK:
          assertEquals(30, ((EventAttendancePeak) event)
                  .getIdStation(), "The station concerned by this event should"
              + " be the number 30");
          assertEquals(200, ((EventAttendancePeak) event)
                  .getSize(), "The attendance peak in this station should be "
              + "of 200 people");
          break;
        default:
          break;
      }
    }
  }
}
