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

package org.example.controller;

import org.example.data.Data;
import org.example.model.Area;
import org.example.model.Event;
import org.example.model.EventAttendancePeak;
import org.example.model.EventHour;
import org.example.model.EventLineClosed;
import org.example.model.EventLineDelay;
import org.example.model.EventStationClosed;
import org.example.model.Line;
import org.example.model.Station;
import org.example.view.AreaView;
import org.example.view.LineView;
import org.example.view.MainWindow;
import org.example.view.StationView;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for performing actions related to the file menu.
 * Linked to menu items in {@link org.example.view.MenuBar}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ActionFile.java
 * @date 2023/09/22
 * @see org.example.data.Data
 * @since 3.0
 */
public class ActionFile {
  /**
   * Name of the export action.
   */
  public static final String EXPORT_NAME = "Export";
  /**
   * Name of the import action.
   */
  public static final String IMPORT_NAME = "Open";
  /**
   * String of the position tag.
   */
  public static final String POSITION = "position";
  /**
   * String of the lines tag.
   */
  public static final String LINES = "lines";
  /**
   * String of the stations tag.
   */
  public static final String STATIONS = "stations";
  /**
   * String of the station tag.
   */
  public static final String STATION = "station";
  /**
   * Number of letters in alphabet.
   */
  private static final int ALPHABET_SIZE = 26;
  /**
   * Singleton instance.
   */
  private static ActionFile instance;

  /**
   * Create Singleton.
   *
   * @return ActionExport instance
   */
  public static ActionFile getInstance() {
    if (instance == null) {
      instance = new ActionFile();
    }
    return instance;
  }

  /**
   * Prompts the export dialog to choose the location to export the map as xml
   * file.
   */
  public void showExportDialog() {
    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setDialogTitle("Specify a file to save");

    int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      this.export(fileToSave);
    }
  }

  /**
   * Prompts the open dialog to select which xml file to import.
   */
  public void showOpenDialog() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "xml files", "xml");
    fileChooser.setFileFilter(filter);
    int returnVal = fileChooser.showOpenDialog(MainWindow.getInstance()
        .getMainPanel());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      this.importMap(fileChooser.getSelectedFile());
    }
  }

  /**
   * Export the current map as xml file.
   *
   * @param fileToSave the xml file to save
   */
  public void export(final File fileToSave) {
    this.assignAreaToStations();
    try {

      DocumentBuilderFactory documentFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

      Document document = documentBuilder.newDocument();
      // root element
      Element root = document.createElement("map");
      document.appendChild(root);

      this.exportStations(document, root);
      this.exportLines(document, root);
      this.exportAreas(document, root);
      this.exportEvents(document, root);

      // create the xml file
      // transform the DOM Object to an XML File
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      // Disable external entities
      transformerFactory.setAttribute(
          "http://javax.xml.XMLConstants/property/accessExternalDTD", "");
      transformerFactory.setAttribute(
          "http://javax.xml.XMLConstants/property/accessExternalStylesheet",
          "");
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
          "{https://xml.apache.org/xslt}indent-amount", "4");

      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(fileToSave);
      transformer.transform(domSource, streamResult);
    } catch (ParserConfigurationException | TransformerException pce) {
      pce.printStackTrace();
    }
  }

  /**
   * Generate the xml part of the events.
   *
   * @param document the document to add the events
   * @param root     the root element of the document
   */
  private void exportEvents(final Document document, final Element root) {
    // Events
    Element events = document.createElement("events");
    root.appendChild(events);
    List<Event> eventList = Data.getInstance().getEventList();
    for (Event event : eventList) {
      // Name of the event
      String timeStart = event.getStartTime();
      timeStart = timeStart.replace("-", "T");
      timeStart = timeStart.replace("/", "-");
      timeStart = timeStart + ":00.000Z";

      String timeEnd = event.getEndTime();
      timeEnd = timeEnd.replace("-", "T");
      timeEnd = timeEnd.replace("/", "-");
      timeEnd = timeEnd + ":00.000Z";

      Element eventName = document.createElement(event.getEventName()
          .getString());
      events.appendChild(eventName);
      Element eventStart = document.createElement("start");
      eventStart.appendChild(document.createTextNode(timeStart));
      eventName.appendChild(eventStart);

      Element eventEnd = document.createElement("end");
      eventEnd.appendChild(document.createTextNode(timeEnd));
      eventName.appendChild(eventEnd);

      switch (event.getEventName().getString()) {
        case "lineDelay":
          EventLineDelay eventLineDelay = (EventLineDelay) event;

          Element stationStart = document.createElement("stationIdStart");
          stationStart
              .appendChild(document.createTextNode(Integer.toString(
                  eventLineDelay.getIdStationStart())));
          eventName.appendChild(stationStart);

          Element stationEnd = document.createElement("stationIdEnd");
          stationEnd.appendChild(document.createTextNode(Integer.toString(
              eventLineDelay.getIdStationEnd())));
          eventName.appendChild(stationEnd);

          Element delay = document.createElement("delay");
          delay.appendChild(document.createTextNode(Integer.toString(
              eventLineDelay.getDelay())));
          eventName.appendChild(delay);
          break;

        case "lineClosed":
          EventLineClosed eventLineClosed = (EventLineClosed) event;

          Element stationStartClosed = document.createElement("stationIdStart");
          stationStartClosed.appendChild(
              document.createTextNode(Integer.toString(eventLineClosed
                  .getIdStationStart())));
          eventName.appendChild(stationStartClosed);

          Element stationEndClosed = document.createElement("stationIdEnd");
          stationEndClosed
              .appendChild(document.createTextNode(Integer.toString(
                  eventLineClosed.getIdStationEnd())));
          eventName.appendChild(stationEndClosed);
          break;
        case "attendancePeak":
          EventAttendancePeak eventAttendancePeak = (EventAttendancePeak) event;

          Element stationId = document.createElement("stationId");
          stationId
              .appendChild(document.createTextNode(Integer.toString(
                  eventAttendancePeak.getIdStation())));
          eventName.appendChild(stationId);

          Element sizePeak = document.createElement("size");
          sizePeak.appendChild(document.createTextNode(Integer.toString(
              eventAttendancePeak.getSize())));
          eventName.appendChild(sizePeak);
          break;
        case "stationClosed":
          EventStationClosed eventStationClosed = (EventStationClosed) event;

          Element stationClosedId
              = document.createElement("idStation");
          stationClosedId.appendChild(document.createTextNode(Integer.toString(
              eventStationClosed.getIdStation())));
          eventName.appendChild(stationClosedId);
          break;
        case "hour":
          EventHour eventHour = (EventHour) event;

          Element idLine = document.createElement("idLine");
          idLine.appendChild(document.createTextNode(Integer.toString(eventHour
              .getIdLine())));
          eventName.appendChild(idLine);

          Element trainNumber = document.createElement("trainNumber");
          trainNumber.appendChild(document.createTextNode(Integer.toString(
              eventHour.getTrainNumber())));
          eventName.appendChild(trainNumber);
          break;
        default:
          break;
      }
    }
  }

  /**
   * Generates the xml part of the areas.
   *
   * @param document the document to add the areas
   * @param root     the root element of the document
   */
  private void exportAreas(final Document document, final Element root) {
    // Areas of the Map
    Element areas = document.createElement("areas");
    root.appendChild(areas);
    for (AreaView areaView : MainWindow.getInstance().getMainPanel()
        .getAreaViews()) {
      // one area of the map
      Element area = document.createElement("area");
      areas.appendChild(area);

      // id of the area
      Element id = document.createElement("id");
      id.appendChild(document.createTextNode(Integer.toString(areaView.getArea()
          .getId())));
      area.appendChild(id);

      // position of the area
      Element position = document.createElement(POSITION);
      Attr latitudeTop = document.createAttribute("latitudeTop");
      latitudeTop.setValue(Double.toString(areaView.getArea()
          .getLatitudeTop()));
      position.setAttributeNode(latitudeTop);
      Attr longitudeTop = document.createAttribute("longitudeTop");
      longitudeTop.setValue(Double.toString(areaView.getArea()
          .getLongitudeTop()));
      position.setAttributeNode(longitudeTop);
      Attr latitudeBot = document.createAttribute("latitudeBot");
      latitudeBot.setValue(Double.toString(areaView.getArea()
          .getLatitudeBot()));
      position.setAttributeNode(latitudeBot);
      Attr longitudeBot = document.createAttribute("longitudeBot");
      longitudeBot.setValue(Double.toString(areaView.getArea()
          .getLongitudeBot()));
      position.setAttributeNode(longitudeBot);
      area.appendChild(position);

      this.exportDistributions(document, areaView, area);
    }
  }

  /**
   * Export the distributions elements of an area or a station
   * that doesn't have an area.
   *
   * @param document the document to add the distributions
   * @param areaView the area view, null if it's for a station
   * @param element the element to add the distributions
   */
  private void exportDistributions(final Document document,
                                   final AreaView areaView,
                                   final Element element) {
    List<String> distributionPopulationElements = Arrays.asList(
        Data.AREA_TOURIST,
        Data.AREA_STUDENT,
        Data.AREA_BUSINESSMAN,
        Data.AREA_WORKER,
        Data.AREA_CHILD,
        Data.AREA_RETIRED,
        Data.AREA_UNEMPLOYED
    );

    List<String> distributionDestinationElements = Arrays.asList(
        Data.AREA_RESIDENTIAL,
        Data.AREA_COMMERCIAL,
        Data.AREA_OFFICE,
        Data.AREA_INDUSTRIAL,
        Data.AREA_TOURISTIC,
        Data.AREA_LEISURE,
        Data.AREA_EDUCATIONAL
    );

    /* Export population distribution. */
    Element populationDistribution = document.createElement(
        "populationDistribution");
    for (String distributionPopulationElement
        : distributionPopulationElements) {

      Attr attr = document.createAttribute(distributionPopulationElement
          .toLowerCase());
      if (areaView != null) {
        attr.setValue(Integer.toString(areaView.getArea()
            .getDistributionPopulation()
            .get(distributionPopulationElement)));
      } else {
        attr.setValue(String.valueOf(Area.getDefaultPopulationDistribution(
            distributionPopulationElement)));
      }
      populationDistribution.setAttributeNode(attr);
    }
    element.appendChild(populationDistribution);

    /* Export destination distribution. */
    Element destinationDistribution = document.createElement(
        "destinationDistribution");
    for (String distributionDestinationElement
        : distributionDestinationElements) {

      Attr attr = document.createAttribute(distributionDestinationElement
          .toLowerCase());
      if (areaView != null) {
        attr.setValue(Integer.toString(areaView.getArea()
                .getDistributionDestination()
            .get(distributionDestinationElement)));
      } else {
        attr.setValue(String.valueOf(Area.getDefaultDestinationDistribution(
            distributionDestinationElement)));
      }
      destinationDistribution.setAttributeNode(attr);
    }
    element.appendChild(destinationDistribution);
  }

  /**
   * Generates the xml part of the lines.
   *
   * @param document the document to add the lines
   * @param root     the root element of the document
   */
  private void exportLines(final Document document, final Element root) {
    // lines of map
    Element lines = document.createElement(LINES);
    root.appendChild(lines);
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {

      // lines line element
      Element line = document.createElement("line");
      lines.appendChild(line);

      // line id element
      Element id = document.createElement("id");
      id.appendChild(document.createTextNode(Integer.toString(lineView.getLine()
          .getId())));
      line.appendChild(id);

      // line name element
      Element name = document.createElement("name");
      name.appendChild(document.createTextNode(ActionFile.toAlphabetic(lineView
          .getLine().getId())));
      line.appendChild(name);

      // line number of train element

      Element numberOfTrain = document.createElement("numberOfTrain");
      numberOfTrain.appendChild(document.createTextNode("30"));
      line.appendChild(numberOfTrain);

      // line Stations element
      Element stations2 = document.createElement(STATIONS);
      line.appendChild(stations2);
      int order = 0;
      for (Station station : lineView.getLine().getStations()) {
        // line Stations station element
        Element station2 = document.createElement(STATION);
        Attr attrId = document.createAttribute("id");
        attrId.setNodeValue(Integer.toString(station.getId()));
        station2.setAttributeNode(attrId);

        Attr attrOrder = document.createAttribute("order");
        attrOrder.setNodeValue(Integer.toString(order));
        station2.setAttributeNode(attrOrder);

        stations2.appendChild(station2);
        order += 1;
      }
    }
  }

  /**
   * Generates the xml part of the stations.
   *
   * @param document the document to add the stations
   * @param root     the root element of the document
   */
  private void exportStations(final Document document, final Element root) {
    // stations of map
    Element stations = document.createElement(STATIONS);

    root.appendChild(stations);
    List<Integer> stationIds;
    stationIds = new ArrayList<>();
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        if (!stationIds.contains(stationView.getStation().getId())) {
          // station element
          Element station = document.createElement(STATION);
          stations.appendChild(station);

          // station id element
          Element stationId = document.createElement("id");
          stationId.appendChild(document.createTextNode(Integer.toString(
              stationView.getStation().getId())));
          station.appendChild(stationId);

          // station name element
          Element stationName = document.createElement("name");
          stationName.appendChild(document.createTextNode(
              stationView.getStation().getName()));
          station.appendChild(stationName);

          // station position element
          Element position = document.createElement(POSITION);
          station.appendChild(position);

          // position latitude element
          Element latitude = document.createElement("latitude");
          double latitudeValue = stationView.getStation().getLatitude();
          latitude.appendChild(document.createTextNode(Double.toString(
              latitudeValue)));
          position.appendChild(latitude);

          // position longitude element
          Element longitude = document.createElement("longitude");
          double longitudeValue = stationView.getStation().getLongitude();
          longitude.appendChild(document.createTextNode(Double.toString(
              longitudeValue)));
          position.appendChild(longitude);

          this.exportStationsLines(document, station, stationView);

          // id area element
          if (stationView.getStation().getArea() != null) {
            Element idArea = document.createElement("idArea");
            idArea.appendChild(document.createTextNode(Integer.toString(
                stationView.getStation().getArea().getId())));
            station.appendChild(idArea);
          } else {
            this.exportDistributions(document, null, station);
          }
          stationIds.add(stationView.getStation().getId());
        }
      }
    }
  }

  /**
   * Generates the xml part of the stations lines.
   *
   * @param document    the document to add the stations lines
   * @param station     the station element
   * @param stationView the station view
   */
  private void exportStationsLines(final Document document,
                                   final Element station,
                                   final StationView stationView) {
    // station lines element
    Element lines = document.createElement(LINES);
    station.appendChild(lines);

    // Station lines line element
    for (LineView lineView2 : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      if (lineView2.getStationViews().contains(stationView)) {
        Element line = document.createElement("line");
        Attr attrLineId = document.createAttribute("id");
        attrLineId.setValue(Integer.toString(lineView2.getLine()
            .getId()));
        line.setAttributeNode(attrLineId);

        Attr attrLinePlatform = document.createAttribute(
            "platform");
        attrLinePlatform.setValue("");
        line.setAttributeNode(attrLinePlatform);
        lines.appendChild(line);
      }
    }
  }

  /**
   * Load the xml file and generate the views of each element.
   *
   * @param fileToLoad the xml file to load
   */
  public void importMap(final File fileToLoad) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      // Disable external entities
      dbFactory.setFeature(
          "http://apache.org/xml/features/disallow-doctype-decl", true);
      dbFactory.setFeature(
          "http://xml.org/sax/features/external-general-entities", false);
      dbFactory.setFeature(
          "http://xml.org/sax/features/external-parameter-entities", false);
      dbFactory.setXIncludeAware(false);
      dbFactory.setExpandEntityReferences(false);

      dbFactory.setXIncludeAware(false);
      dbFactory.setExpandEntityReferences(false);
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(fileToLoad);
      List<Station> stationsToLoad = new ArrayList<>();
      List<Integer> linesId = new ArrayList<>();
      NodeList linesList = doc.getElementsByTagName(LINES);
      HashMap<Integer, String[]> linesMatchStations = new HashMap<>();
      Node nthNodeL = linesList.item(linesList.getLength() - 1);
      List<Area> areasToLoad = new ArrayList<>();

      this.readStationsSection(stationsToLoad, linesId, doc);
      this.readLinesSection(linesMatchStations, nthNodeL);
      this.readAreasSection(doc, areasToLoad);
      this.readEventsSection(doc);

      List<Line> lineModelList = new ArrayList<>();

      if (!linesId.isEmpty()) {
        int linesNumber = Collections.max(linesId) + 1;
        for (int i = 0; i < linesNumber; i++) {
          Line line = new Line(i, new ArrayList<>());
          lineModelList.add(line);
        }
      }

      this.addStationsToLines(stationsToLoad, linesMatchStations,
          lineModelList);

      for (Line line : lineModelList) {
        List<StationView> stationsViews = new ArrayList<>();
        for (Station station : line.getStations()) {
          stationsViews.add(new StationView(station));
        }
        LineView lineView = new LineView(line, stationsViews);
        MainWindow.getInstance().getMainPanel().addLineView(lineView);
      }
      for (Area area : areasToLoad) {
        MainWindow.getInstance().getMainPanel().addAreaView(new AreaView(area));
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  private void addStationsToLines(final List<Station> stationsToLoad,
                                  final HashMap<Integer, String[]>
                                      linesMatchStations,
                                  final List<Line> lineModelList) {
    for (Map.Entry<Integer, String[]> entry : linesMatchStations.entrySet()) {
      int stationId = Integer.parseInt(linesMatchStations.get(entry
          .getKey())[0]);
      int lineId = Integer.parseInt(linesMatchStations.get(entry
          .getKey())[1]);
      Station station = null;
      for (Station stationL : stationsToLoad) {
        if (stationL.getId() == stationId) {
          station = stationL;
        }
      }
      if (station != null) {
        lineModelList.get(lineId).addStation(station);
      }
    }
  }

  /**
   * Load the event section of the xml file and generate the view for each
   * event.
   *
   * @param doc the document to read
   */
  private void readEventsSection(final Document doc) {
    NodeList eventList = doc.getElementsByTagName("events");
    NodeList childList = eventList.item(0).getChildNodes();
    for (int j = 0; j < childList.getLength(); j++) {
      Node childNode = childList.item(j);
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eventElement = (Element) childNode;
        switch (eventElement.getTagName()) {
          case "lineDelay":
            ActionMetroEvent.getInstance().addLineDelay();
            break;
          case "lineClosed":
            ActionMetroEvent.getInstance().addLineClosed();
            break;
          case "attendancePeak":
            ActionMetroEvent.getInstance().addAttendancePeak();
            break;
          case "stationClosed":
            ActionMetroEvent.getInstance().addStationClosed();
            break;
          case "hour":
            ActionMetroEvent.getInstance().addTrainHour();
            break;
          default:
            break;
        }
      }
    }
  }

  /**
   * Load the areas section of the xml file and generate the view for each area.
   *
   * @param doc         the document to read
   * @param areasToLoad the list of areas to load
   */
  private void readAreasSection(final Document doc,
                                final List<Area> areasToLoad) {
    // ! reading area section
    NodeList areasList = doc.getElementsByTagName("areas");
    Node nthNodeA = areasList.item(0);
    if (nthNodeA.getNodeType() == Node.ELEMENT_NODE) {
      Element areasElement = (Element) nthNodeA;
      NodeList areaList = areasElement.getElementsByTagName("area");
      for (int i = 0; i < areaList.getLength(); i++) {
        Node nthNodeA2 = areaList.item(i);
        if (nthNodeA2.getNodeType() == Node.ELEMENT_NODE) {
          Element areaElement = (Element) nthNodeA2;
          Element populationDistribution = (Element) areaElement
              .getElementsByTagName("populationDistribution").item(0);

          // format number
          String touristAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_TOURIST));
          String studentAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_STUDENT));
          String businessManAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_BUSINESSMAN));
          String childAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_CHILD));
          String retiredAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_RETIRED));
          String unemployedAmount = this.formatNumber(
              populationDistribution.getAttribute(Data.AREA_UNEMPLOYED));
          Element positions = (Element) areaElement.getElementsByTagName(
              POSITION).item(0);
          String latitudeTop = positions.getAttribute("latitudeTop");
          String longitudeTop = positions.getAttribute("longitudeTop");
          String latitudeBot = positions.getAttribute("latitudeBot");
          String longitudeBot = positions.getAttribute("longitudeBot");
          Area area = new Area(Double.parseDouble(latitudeTop),
              Double.parseDouble(longitudeTop), Double.parseDouble(latitudeBot),
              Double.parseDouble(longitudeBot));
          area.setNewPopulationPart(Data.AREA_TOURIST, Integer.parseInt(
              touristAmount));
          area.setNewPopulationPart(Data.AREA_STUDENT, Integer.parseInt(
              studentAmount));
          area.setNewPopulationPart(Data.AREA_BUSINESSMAN,
              Integer.parseInt(businessManAmount));
          area.setNewPopulationPart(Data.AREA_CHILD, Integer.parseInt(
              childAmount));
          area.setNewPopulationPart(Data.AREA_RETIRED, Integer.parseInt(
              retiredAmount));
          area.setNewPopulationPart(Data.AREA_UNEMPLOYED,
              Integer.parseInt(unemployedAmount));
          areasToLoad.add(area);
        }
      }
    }
  }

  /**
   * Format the number to avoid empty string.
   *
   * @param populationAmount the population amount to format
   * @return the formatted number
   */
  private String formatNumber(final String populationAmount) {
    String resultPopulationAmount = populationAmount;
    if (resultPopulationAmount.isEmpty()) {
      resultPopulationAmount = "0";
    }
    return resultPopulationAmount;
  }

  /**
   * Load the stations section of the xml file and generate the view for each.
   *
   * @param stationsToLoad the list of stations to load
   * @param linesId        the list of lines id
   * @param doc            the document to read
   */
  private void readStationsSection(final List<Station> stationsToLoad,
                                   final List<Integer> linesId,
                                   final Document doc) {
    NodeList stationsList = doc.getElementsByTagName(STATIONS);
    Node nthNodeS
        = stationsList.item(0);
    if (nthNodeS
        .getNodeType() == Node.ELEMENT_NODE) {
      Element stationsElement = (Element) nthNodeS;
      NodeList stationList = stationsElement.getElementsByTagName(STATION);
      for (int i = 0; i < stationList.getLength(); i++) {
        Node nthNode = stationList.item(i);
        if (nthNode.getNodeType() == Node.ELEMENT_NODE) {
          Element stationElement = (Element) nthNode;
          String id = stationElement.getElementsByTagName("id")
              .item(0).getTextContent();
          String name = stationElement.getElementsByTagName("name")
              .item(0).getTextContent();
          Element positions = (Element) stationElement
              .getElementsByTagName(POSITION).item(0);
          String latitude = positions.getElementsByTagName("latitude")
              .item(0).getTextContent();
          String longitude = positions.getElementsByTagName("longitude")
              .item(0).getTextContent();
          Element lines = (Element) stationElement.getElementsByTagName(LINES)
              .item(0);
          Element line = (Element) lines.getElementsByTagName("line")
              .item(0);
          String lineId = line.getAttribute("id");
          Point pos = MainWindow.getInstance().getMainPanel()
              .getMapPosition(Double.parseDouble(latitude),
                  Double.parseDouble(longitude), false);
          int stationPosX = (int) pos.getX();

          int stationPosY = (int) pos.getY();


          stationsToLoad.add(new Station(Integer.parseInt(id), stationPosX,
              stationPosY, name));
          linesId.add(Integer.valueOf(lineId));
        }
      }
    }
  }

  /**
   * Load the lines section of the xml file and generate the view for each line.
   *
   * @param linesMatchStations the map of lines match stations
   * @param nthNodeL           the node to read
   */
  public void readLinesSection(final Map<Integer, String[]> linesMatchStations,
                               final Node nthNodeL) {
    if (nthNodeL.getNodeType() == Node.ELEMENT_NODE) {
      Element linesElement = (Element) nthNodeL;
      NodeList lineList = linesElement.getElementsByTagName("line");
      int counter = 0;
      for (int i = 0; i < lineList.getLength(); i++) {
        Node nodeL = lineList.item(i);
        if (nodeL.getNodeType() == Node.ELEMENT_NODE) {
          Element lineElement = (Element) nodeL;
          String lineId = lineElement.getElementsByTagName("id")
              .item(0).getTextContent();
          Element stations = (Element) lineElement
              .getElementsByTagName(STATIONS).item(0);
          NodeList stationL = stations.getElementsByTagName(STATION);
          for (int j = 0; j < stationL.getLength(); j++) {
            Node nodeS = stationL.item(j);
            if (nodeS.getNodeType() == Node.ELEMENT_NODE) {
              Element stationElement = (Element) nodeS;
              String stationId = stationElement.getAttribute("id");
              String[] array = {stationId, lineId};
              linesMatchStations.put(counter, array);
              counter += 1;
            }
          }
        }
      }
    }
  }

  /**
   * iterate over all stations and area to find which stations are in
   * area,
   * then assign areas to stations.
   */
  public void assignAreaToStations() {
    for (LineView lineview : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineview.getStationViews()) {
        for (AreaView areaView : MainWindow.getInstance().getMainPanel()
            .getAreaViews()) {
          if (isInArea(stationView, areaView)) {
            stationView.getStation().setAreas(areaView.getArea());
          }
        }
      }
    }
  }

  /**
   * check if a station is in an Area.
   *
   * @param stationView station to check
   * @param areaView    area to check
   * @return true if the station is in the area
   */
  private boolean isInArea(final StationView stationView,
                           final AreaView areaView) {
    int x = stationView.getStation().getPosX();
    int ax = areaView.getArea().getPosX();
    int y = stationView.getStation().getPosY();
    int ay = areaView.getArea().getPosY();
    int widthA = areaView.getArea().getWidth();
    int heightA
        = areaView.getArea().getHeight();
    return ((x > ax) && (x < ax + widthA) && (y > ay) && (y < ay + heightA));
  }

  /**
   * Map number in letters from their index in alphabet.
   *
   * @param i index
   * @return String letter
   */
  public static String toAlphabetic(final int i) {
    if (i < 0) {
      return "-" + toAlphabetic(-i - 1);
    }

    int quot = i / ALPHABET_SIZE;
    int rem = i % ALPHABET_SIZE;
    char letter = (char) ('A' + rem);
    if (quot == 0) {
      return "" + letter;
    } else {
      return toAlphabetic(quot - 1) + letter;
    }
  }
}