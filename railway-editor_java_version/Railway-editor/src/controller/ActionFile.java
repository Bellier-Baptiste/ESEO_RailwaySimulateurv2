package controller;

import Model.*;
import Model.Event;
import data.Data;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import view.*;

import javax.swing.*;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ActionFile {
  public static final String EXPORT_NAME = "Export";
  public static final String IMPORT_NAME = "Open";
  private static ActionFile instance;

  /**
   * Create Singleton
   *
   * @return ActionExport instance
   */
  public static ActionFile getInstance() {
    if (instance == null) {
      instance = new ActionFile();
    }
    return instance;
  }


  public void showExportDialog() {
    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setDialogTitle("Specify a file to save");

    int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();

      System.out.println("Save as file: " + fileToSave.getAbsolutePath());
      this.export(fileToSave);
    }
  }

  public void showOpenDialog() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
    fileChooser.setFileFilter(filter);
    int returnVal = fileChooser.showOpenDialog(MainWindow.getInstance().getMainPanel());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
      String[] file = fileChooser.getSelectedFile().getName().split("\\.");
      this.importMap(fileChooser.getSelectedFile());
    }
  }

  public void export(File fileToSave) {
    this.assignAreaToStations();
    try {

      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

      Document document = documentBuilder.newDocument();

      // root element
      Element root = document.createElement("map");
      document.appendChild(root);

      // stations of map
      Element stations = document.createElement("stations");

      root.appendChild(stations);
      List<Integer> stationIds;
      stationIds = new ArrayList<>();
      for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
        for (StationView stationView : lineView.getStationViews()) {
          if (!stationIds.contains(stationView.getStation().getId())) {


            // station element
            Element station = document.createElement("station");
            stations.appendChild(station);

            // station id element
            Element stationId = document.createElement("id");
            stationId.appendChild(document.createTextNode(Integer.toString(stationView.getStation().getId())));
            station.appendChild(stationId);

            // station name element
            Element stationName = document.createElement("name");
            stationName.appendChild(document.createTextNode(stationView.getStation().getName()));
            station.appendChild(stationName);

            // station position element
            Element position = document.createElement("position");
            station.appendChild(position);

            // position latitude element
            Element latitude = document.createElement("latitude");
            double latitudeValue = stationView.getStation().getLatitude();
            latitude.appendChild(document.createTextNode(Double.toString(latitudeValue)));
            position.appendChild(latitude);

            // position longitude element
            Element longitude = document.createElement("longitude");
            double longitudeValue = stationView.getStation().getLongitude();
            longitude.appendChild(document.createTextNode(Double.toString(longitudeValue)));
            position.appendChild(longitude);

            // station lines element
            Element lines = document.createElement("lines");
            station.appendChild(lines);

            // Station lines line element
            for (LineView lineview2 : MainWindow.getInstance().getMainPanel().getLineViews()) {
              if (lineview2.getStationViews().contains(stationView)) {
                Element line = document.createElement("line");
                Attr attrLineId = document.createAttribute("id");
                attrLineId.setValue(Integer.toString(lineview2.getLine().getId()));
                line.setAttributeNode(attrLineId);

                Attr attrLinePlatform = document.createAttribute("platform");
                attrLinePlatform.setValue("");
                line.setAttributeNode(attrLinePlatform);
                lines.appendChild(line);
              }

            }
            // Station destination element
            Element destination = document.createElement("destination");
            if (stationView.getStation().getArea() != null) {
              destination.appendChild(
                  document.createTextNode(stationView.getStation().getArea().getDestination()));
              station.appendChild(destination);

              // station distribution element
              Element areaDistribution = document.createElement("areaDistribution");
              // tourist Attribute
              Attr attrTourist = document.createAttribute("tourist");
              attrTourist.setValue(Integer
                  .toString(stationView.getStation().getArea().getDistribution().get(Data.AREA_TOURIST)));
              areaDistribution.setAttributeNode(attrTourist);

              // Student attribute
              Attr attrStudent = document.createAttribute("student");
              attrStudent.setValue(Integer
                  .toString(stationView.getStation().getArea().getDistribution().get(Data.AREA_STUDENT)));
              areaDistribution.setAttributeNode(attrStudent);

              // Businessmann attribute
              Attr attrBusinessmann = document.createAttribute("businessmann");
              attrBusinessmann.setValue(Integer.toString(
                  stationView.getStation().getArea().getDistribution().get(Data.AREA_BUSINESSMAN)));
              areaDistribution.setAttributeNode(attrBusinessmann);

              // worker attribute
              Attr attrWorker = document.createAttribute("worker");
              attrWorker.setValue(Integer
                  .toString(stationView.getStation().getArea().getDistribution().get(Data.AREA_WORKER)));
              areaDistribution.setAttributeNode(attrWorker);

              // child attribute
              Attr attrChild = document.createAttribute("child");
              attrChild.setValue(Integer
                  .toString(stationView.getStation().getArea().getDistribution().get(Data.AREA_CHILD)));
              areaDistribution.setAttributeNode(attrChild);

              // retired attribute
              Attr attrRetired = document.createAttribute("retired");
              attrRetired.setValue(Integer
                  .toString(stationView.getStation().getArea().getDistribution().get(Data.AREA_RETIRED)));
              areaDistribution.setAttributeNode(attrRetired);

              // unemployed attribute
              Attr attrUnemployed = document.createAttribute("unemployed");
              attrUnemployed.setValue(Integer.toString(
                  stationView.getStation().getArea().getDistribution().get(Data.AREA_UNEMPLOYED)));
              areaDistribution.setAttributeNode(attrUnemployed);

              station.appendChild(areaDistribution);
            }

            stationIds.add(stationView.getStation().getId());
          }

        }
      }

      // lines of map
      Element lines = document.createElement("lines");
      root.appendChild(lines);
      for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {

        // lines line element
        Element line = document.createElement("line");
        lines.appendChild(line);

        // line id element
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode(Integer.toString(lineView.getLine().getId())));
        line.appendChild(id);

        // line name element
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(this.toAlphabetic(lineView.getLine().getId())));
        line.appendChild(name);

        // line number of train element

        Element numberOfTrain = document.createElement("numberOfTrain");
        numberOfTrain.appendChild(document.createTextNode("30"));
        line.appendChild(numberOfTrain);

        // line Stations element
        Element stations2 = document.createElement("stations");
        line.appendChild(stations2);
        int order = 0;
        for (Station station : lineView.getLine().getStations()) {
          // line Stations station element
          Element station2 = document.createElement("station");
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

      // Areas of the Map
      Element areas = document.createElement("areas");
      root.appendChild(areas);
      for (AreaView areaView : MainWindow.getInstance().getMainPanel().getAreaViews()) {
        // one area of the map
        Element area = document.createElement("area");
        areas.appendChild(area);

        // id of the area
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode(Integer.toString(areaView.getArea().getId())));
        area.appendChild(id);

        // position of the area
        Element position = document.createElement("position");
        Attr posX = document.createAttribute("posX");
        posX.setValue(Integer.toString(areaView.getArea().getPosX()));
        position.setAttributeNode(posX);
        Attr posY = document.createAttribute("posY");
        posY.setValue(Integer.toString(areaView.getArea().getPosY()));
        position.setAttributeNode(posY);
        area.appendChild(position);

        // size of the area
        Element size = document.createElement("size");
        Attr width = document.createAttribute("width");
        width.setValue(Integer.toString(areaView.getArea().getWidth()));
        size.setAttributeNode(width);
        Attr height = document.createAttribute("height");
        height.setValue(Integer.toString(areaView.getArea().getHeight()));
        size.setAttributeNode(height);
        area.appendChild(size);

        // type of the area (Stations destination)
        Element areaType = document.createElement("type");
        areaType.appendChild(document.createTextNode(areaView.getArea().getDestination()));
        area.appendChild(areaType);

        // distribution of the area
        Element distribution = document.createElement("distribution");
        // tourist Attribute
        Attr attrTourist = document.createAttribute(Data.AREA_TOURIST);
        attrTourist.setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_TOURIST)));
        distribution.setAttributeNode(attrTourist);

        // Student attribute
        Attr attrStudent = document.createAttribute(Data.AREA_STUDENT);
        attrStudent.setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_STUDENT)));
        distribution.setAttributeNode(attrStudent);

        // Businessmann attribute
        Attr attrBusinessmann = document.createAttribute(Data.AREA_BUSINESSMAN);
        attrBusinessmann
            .setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_BUSINESSMAN)));
        distribution.setAttributeNode(attrBusinessmann);

        // worker attribute
        Attr attrWorker = document.createAttribute(Data.AREA_WORKER);
        attrWorker.setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_WORKER)));
        distribution.setAttributeNode(attrWorker);

        // child attribute
        Attr attrChild = document.createAttribute(Data.AREA_CHILD);
        attrChild.setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_CHILD)));
        distribution.setAttributeNode(attrChild);

        // retired attribute
        Attr attrRetired = document.createAttribute(Data.AREA_RETIRED);
        attrRetired.setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_RETIRED)));
        distribution.setAttributeNode(attrRetired);

        // unemployed attribute
        Attr attrUnemployed = document.createAttribute(Data.AREA_UNEMPLOYED);
        attrUnemployed
            .setValue(Integer.toString(areaView.getArea().getDistribution().get(Data.AREA_UNEMPLOYED)));
        distribution.setAttributeNode(attrUnemployed);
        area.appendChild(distribution);
      }

      // Events
      Element events = document.createElement("events");
      root.appendChild(events);
      for (Event event : Data.getInstance().getEventList()) {
        // Name of the event
        String timeStart = event.getStartTime();
        timeStart = timeStart.replaceAll("/", "-");
        timeStart = timeStart.replaceAll("_", "T");
        timeStart = timeStart+":00.000Z";

        String timeEnd = event.getEndTime();
        timeEnd = timeEnd.replace("/", "-");
        timeEnd = timeEnd.replace("_", "T");
        timeEnd = timeEnd+":00.000Z";

        Element eventName = document.createElement(event.EVENT_NAME);
        events.appendChild(eventName);
        Element eventStart = document.createElement("start");
        eventStart.appendChild(document.createTextNode(timeStart));
        eventName.appendChild(eventStart);

        Element eventEnd = document.createElement("end");
        eventEnd.appendChild(document.createTextNode(timeEnd));
        eventName.appendChild(eventEnd);

        switch (event.EVENT_NAME) {
          case "lineDelay":
            EventLineDelay eventLineDelay = (EventLineDelay) event;

            Element stationStart = document.createElement("stationIdStart");
            stationStart
                .appendChild(document.createTextNode(Integer.toString(eventLineDelay.getIdStationStart())));
            eventName.appendChild(stationStart);

            Element stationEnd = document.createElement("stationIdEnd");
            stationEnd.appendChild(document.createTextNode(Integer.toString(eventLineDelay.getIdStationEnd())));
            eventName.appendChild(stationEnd);

            Element delay = document.createElement("delay");
            delay.appendChild(document.createTextNode(Integer.toString(eventLineDelay.getDelay())));
            eventName.appendChild(delay);
            break;

          case "lineClose":
            EventLineClosed eventLineClosed = (EventLineClosed) event;

            Element stationStartClosed = document.createElement("stationIdStart");
            stationStartClosed.appendChild(
                document.createTextNode(Integer.toString(eventLineClosed.getIdStationStart())));
            eventName.appendChild(stationStartClosed);

            Element stationEndClosed = document.createElement("stationIdEnd");
            stationEndClosed
                .appendChild(document.createTextNode(Integer.toString(eventLineClosed.getIdStationEnd())));
            eventName.appendChild(stationEndClosed);
            break;
          case "attendancePeak":
            EventAttendancePeak eventAttendancePeak = (EventAttendancePeak) event;

            Element stationID = document.createElement("stationId");
            stationID
                .appendChild(document.createTextNode(Integer.toString(eventAttendancePeak.getIdStation())));
            eventName.appendChild(stationID);

            Element sizePeak = document.createElement("size");
            sizePeak.appendChild(document.createTextNode(Integer.toString(eventAttendancePeak.getSize())));
            eventName.appendChild(sizePeak);
            break;
          case "stationClosed":
            EventStationClosed eventStationClosed = (EventStationClosed) event;

            Element stationClosedID = document.createElement("idStation");
            stationClosedID
                .appendChild(document.createTextNode(Integer.toString(eventStationClosed.getIdStation())));
            eventName.appendChild(stationClosedID);
            break;
          case "hour":
            EventHour eventHour = (EventHour) event;

            Element idLine = document.createElement("idLine");
            idLine.appendChild(document.createTextNode(Integer.toString(eventHour.getIdLine())));
            eventName.appendChild(idLine);

            Element trainNumber = document.createElement("trainNumber");
            trainNumber.appendChild(document.createTextNode(Integer.toString(eventHour.getTrainNumber())));
            eventName.appendChild(trainNumber);
            break;
        }
      }

      // create the xml file
      // transform the DOM Object to an XML File
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(fileToSave);
      transformer.transform(domSource, streamResult);

      System.out.println("Done creating XML File");

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
  }

  public void importMap(File fileToLoad) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fileToLoad);
      System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
      List<Station> stationsToLoad = new ArrayList<>();
      List<Integer> linesId = new ArrayList<>();
      HashMap<String, String> stationMatchLines = new HashMap<String, String>();
      NodeList stationsList = doc.getElementsByTagName("stations");
      Node nNodeS = (Node) stationsList.item(0);
      if (nNodeS.getNodeType() == Node.ELEMENT_NODE) {
        Element stationsElement = (Element) nNodeS;
        NodeList stationList = stationsElement.getElementsByTagName("station");
        for (int i = 0; i < stationList.getLength(); i++) {
          Node nNode = (Node) stationList.item(i);
          if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element stationElement = (Element) nNode;
            String id = stationElement.getElementsByTagName("id").item(0).getTextContent();
            String name = stationElement.getElementsByTagName("name").item(0).getTextContent();
            Element positions = (Element) stationElement.getElementsByTagName("position").item(0);
            String latitude = positions.getElementsByTagName("latitude").item(0).getTextContent();
            String longitude = positions.getElementsByTagName("longitude").item(0).getTextContent();
            Element lines = (Element) stationElement.getElementsByTagName("lines").item(0);
            Element line = (Element) lines.getElementsByTagName("line").item(0);
            String lineId = line.getAttribute("id");
            System.out.println(latitude+" "+longitude);
            Point pos = MainWindow.getInstance().getMainPanel().getMapPosition(Double.parseDouble(latitude), Double.parseDouble(longitude),false);


            System.out.println(pos.x+" "+pos.y);
            int stationPosX = (int)pos.getX();

            int stationPosY = (int)pos.getY();


            stationsToLoad.add(new Station(Integer.valueOf(id), stationPosX, stationPosY, name));
            linesId.add(Integer.valueOf(lineId));
            stationMatchLines.put(id, lineId);
          }
        }
      }

      HashMap<Integer, String[]> linesMatchStations = new HashMap<Integer, String[]>();
      NodeList linesList = doc.getElementsByTagName("lines");
      List<Integer> linesStationsNumbers = new ArrayList<>();
      Node nNodeL = (Node) linesList.item(linesList.getLength() - 1);

      if (nNodeL.getNodeType() == Node.ELEMENT_NODE) {
        Element linesElement = (Element) nNodeL;
        NodeList lineList = linesElement.getElementsByTagName("line");
        int counter = 0;
        for (int i = 0; i < lineList.getLength(); i++) {
          Node lNode = (Node) lineList.item(i);
          if (lNode.getNodeType() == Node.ELEMENT_NODE) {
            Element lineElement = (Element) lNode;
            String lineId = lineElement.getElementsByTagName("id").item(0).getTextContent();
            System.out.println("lineId " + lineId);
            Element stations = (Element) lineElement.getElementsByTagName("stations").item(0);
            NodeList stationL = stations.getElementsByTagName("station");
            System.out.println(stationL.getLength());
            linesStationsNumbers.add(stationL.getLength());
            for (int j = 0; j < stationL.getLength(); j++) {
              Node sNode = (Node) stationL.item(j);
              if (sNode.getNodeType() == Node.ELEMENT_NODE) {
                Element stationElement = (Element) sNode;
                System.out.println("attributes " + stationElement.getAttributes().item(0));
                String stationId = stationElement.getAttribute("id");
                String[] array = { stationId, lineId };
                linesMatchStations.put(counter, array);
                counter += 1;
              }

            }
          }
        }
      }

      // reading area section
      List<Area> areasToLoad = new ArrayList<>();
      NodeList areasList = doc.getElementsByTagName("areas");
      Node nNodeA = (Node) areasList.item(0);
      if (nNodeA.getNodeType() == Node.ELEMENT_NODE) {
        Element areasElement = (Element) nNodeA;
        NodeList areaList = areasElement.getElementsByTagName("area");
        for (int i = 0; i < areaList.getLength(); i++) {
          Node nNode = (Node) areaList.item(i);
          if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element areaElement = (Element) nNode;
            String id = areaElement.getElementsByTagName("id").item(0).getTextContent();
            String type = areaElement.getElementsByTagName("type").item(0).getTextContent();
            Element positions = (Element) areaElement.getElementsByTagName("position").item(0);
            String posX = positions.getAttribute("posX");
            String posY = positions.getAttribute("posY");
            Element size = (Element) areaElement.getElementsByTagName("size").item(0);
            String width = size.getAttribute("width");
            String height = size.getAttribute("height");
            Element distributions = (Element) areaElement.getElementsByTagName("distribution").item(0);
            String touristAmount = distributions.getAttribute(Data.AREA_TOURIST);
            String studentAmount = distributions.getAttribute(Data.AREA_STUDENT);
            String businessmannAmount = distributions.getAttribute(Data.AREA_BUSINESSMAN);
            String childAmount = distributions.getAttribute(Data.AREA_CHILD);
            String retiredAmount = distributions.getAttribute(Data.AREA_RETIRED);
            String unemployedAmount = distributions.getAttribute(Data.AREA_UNEMPLOYED);

            // format number
            if (touristAmount.isEmpty()) {
              touristAmount = "0";
            }
            if (studentAmount.isEmpty()) {
              studentAmount = "0";
            }
            if (businessmannAmount.isEmpty()) {
              businessmannAmount = "0";
            }
            if (childAmount.isEmpty()) {
              childAmount = "0";
            }
            if (retiredAmount.isEmpty()) {
              retiredAmount = "0";
            }
            if (unemployedAmount.isEmpty()) {
              unemployedAmount = "0";
            }

            Area area = new Area(Integer.valueOf(id), Integer.valueOf(posX), Integer.valueOf(posY),
                Integer.valueOf(width), Integer.valueOf(height));
            area.setNewPart(Data.AREA_TOURIST, Integer.valueOf(touristAmount));
            area.setNewPart(Data.AREA_STUDENT, Integer.valueOf(studentAmount));
            area.setNewPart(Data.AREA_BUSINESSMAN, Integer.valueOf(businessmannAmount));
            area.setNewPart(Data.AREA_CHILD, Integer.valueOf(childAmount));
            area.setNewPart(Data.AREA_RETIRED, Integer.valueOf(retiredAmount));
            area.setNewPart(Data.AREA_UNEMPLOYED, Integer.valueOf(unemployedAmount));
            area.setDestination(type);
            areasToLoad.add(area);
          }
        }
      }

      List<Line> lineModelList = new ArrayList<>();

      int linesNumber = Collections.max(linesId) + 1;
      System.out.println("linesNumber " + linesNumber);
      for (int i = 0; i < linesNumber; i++) {
        Line line = new Line(i, new ArrayList<Station>());
        lineModelList.add(line);
      }

      for (Map.Entry<Integer, String[]> entry : linesMatchStations.entrySet()) {
        int stationId = Integer.valueOf(linesMatchStations.get(entry.getKey())[0]);
        int lineId = Integer.valueOf(linesMatchStations.get(entry.getKey())[1]);
        Station station = null;
        for (Station stationL : stationsToLoad) {
          if (stationL.getId() == stationId) {
            station = stationL;
          }
        }
        if (station != null) {
          lineModelList.get(lineId).addStation(station);

          station = null;
        }
      }

      for (Line line : lineModelList) {
        List<StationView> stationsViews = new ArrayList<>();
        for (Station station : line.getStations()) {
          stationsViews.add(new StationView(station));
        }
        LineView lineView = new LineView(line, stationsViews);
        MainWindow.getInstance().getMainPanel().addLineView(lineView);
      }
      System.out.println("areaSize " + areasToLoad.size());
      for (Area area : areasToLoad) {
        MainWindow.getInstance().getMainPanel().addAreaView(new AreaView(area));
      }

      // reading event section
      NodeList eventList = doc.getElementsByTagName("events");
      NodeList childList = eventList.item(0).getChildNodes();
      for (int j = 0; j < childList.getLength(); j++) {
        Node childNode = childList.item(j);
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {

          Element eventElement = (Element) childNode;

          String startTime = eventElement.getElementsByTagName("start").item(0).getTextContent();
          String endTime = eventElement.getElementsByTagName("end").item(0).getTextContent();
          String dateStart = "";
          String timeStart = "";
          String dateEnd = "";
          String timeEnd = "";
          if (!eventElement.getTagName().equals("hour")) {
            dateStart = startTime.split("_")[0];
            timeStart = startTime.split("_")[1];
            dateEnd = endTime.split("_")[0];
            timeEnd = endTime.split("_")[1];
          }
          if (eventElement.getTagName().equals("lineDelay")) {
            String stationStartId = eventElement.getElementsByTagName("stationIdStart").item(0)
                .getTextContent();
            String stationEndId = eventElement.getElementsByTagName("stationIdEnd").item(0)
                .getTextContent();
            String delay = eventElement.getElementsByTagName("delay").item(0).getTextContent();
            delay = Integer.parseInt(delay) / 60 + ":" + Integer.parseInt(delay) % 60;

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationStartId + "," + stationEndId + "," + delay;
            ActionMetroEvent.getInstance().addLineDelay();
          }

          if (eventElement.getTagName().equals("lineClose")) {
            String stationStartId = eventElement.getElementsByTagName("stationIdStart").item(0)
                .getTextContent();
            String stationEndId = eventElement.getElementsByTagName("stationIdEnd").item(0)
                .getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationStartId + "," + stationEndId;
            ActionMetroEvent.getInstance().addLineClosed();
          }

          if (eventElement.getTagName().equals("attendancePeak")) {
            String stationId = eventElement.getElementsByTagName("stationId").item(0).getTextContent();
            String size = eventElement.getElementsByTagName("size").item(0).getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationId + "," + size;
            ActionMetroEvent.getInstance().addAttendancePeak();
          }

          if (eventElement.getTagName().equals("stationClosed")) {
            String stationId = eventElement.getElementsByTagName("idStation").item(0).getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationId;
            ActionMetroEvent.getInstance().addStationClosed();
          }
          if (eventElement.getTagName().equals("hour")) {
            String lineId = eventElement.getElementsByTagName("idLine").item(0).getTextContent();
            String trainNumber = eventElement.getElementsByTagName("trainNumber").item(0).getTextContent();

            String eventString = startTime + "," + endTime + "," + lineId + "," + trainNumber;
            ActionMetroEvent.getInstance().addTrainHour();
          }
        }
      }

    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * iterate over all stations and area to find which stations are in which area,
   * then assign areas to stations.
   */
  public void assignAreaToStations() {
    for (LineView lineview : MainWindow.getInstance().getMainPanel().getLineViews()) {
      for (StationView stationView : lineview.getStationViews()) {
        for (AreaView areaView : MainWindow.getInstance().getMainPanel().getAreaViews()) {
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
   * @param stationView
   * @param areaView
   * @return
   */
  private boolean isInArea(StationView stationView, AreaView areaView) {
    int x = stationView.getStation().getPosX();
    int ax = areaView.getArea().getPosX();
    int y = stationView.getStation().getPosY();
    int ay = areaView.getArea().getPosY();
    int aWidth = areaView.getArea().getWidth();
    int aHeight = areaView.getArea().getHeight();
    return ((x > ax) && (x < ax + aWidth) && (y > ay) && (y < ay + aHeight));
  }

  /**map number in letters from their index in alphabet.
   * @param i index
   * @return String letter
   */
  public static String toAlphabetic(int i) {
    if( i<0 ) {
      return "-"+toAlphabetic(-i-1);
    }

    int quot = i/26;
    int rem = i%26;
    char letter = (char)((int)'A' + rem);
    if( quot == 0 ) {
      return ""+letter;
    } else {
      return toAlphabetic(quot-1) + letter;
    }
  }

}
