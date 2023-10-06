package controller;

import Model.*;
import data.Data;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import view.*;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActionExport extends AbstractAction {
  public static final String ACTION_NAME = "Export";
  private MainPanel mainPanel;
  private ActionManager actionManager;

public ActionExport(MainPanel mainPanel, ActionManager actionManager) {
    super(ACTION_NAME);
    this.mainPanel = mainPanel;
    this.actionManager = actionManager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();

      fileChooser.setDialogTitle("Specify a file to save");

      int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());

      if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        actionManager.export(fileToSave);
      }


  }


  public void export(File fileToSave) {
    this.actionManager.assignAreaToStations();
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
        name.appendChild(document.createTextNode(this.actionManager.toAlphabetic(lineView.getLine().getId())));
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
      Element events = document.createElement("org/openstreetmap/gui/jmapviewer/events");
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


}
