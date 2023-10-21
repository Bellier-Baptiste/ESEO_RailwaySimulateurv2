package controller;

import model.Area;
import model.Line;
import model.Station;
import data.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import view.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ActionOpen extends AbstractAction {
  public static final String ACTION_NAME = "Open";
  private MainPanel mainPanel;
  private ActionManager actionManager;

  public ActionOpen(MainPanel mainPanel, ActionManager actionManager) {
    super(ACTION_NAME);
    this.mainPanel = mainPanel;
    this.actionManager = actionManager;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
    fileChooser.setFileFilter(filter);
    int returnVal = fileChooser.showOpenDialog(this.mainPanel);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
      String[] file = fileChooser.getSelectedFile().getName().split("\\.");
      this.importMap(fileChooser.getSelectedFile());
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
            this.actionManager.addEventDelay(eventString);
          }

          if (eventElement.getTagName().equals("lineClose")) {
            String stationStartId = eventElement.getElementsByTagName("stationIdStart").item(0)
                .getTextContent();
            String stationEndId = eventElement.getElementsByTagName("stationIdEnd").item(0)
                .getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationStartId + "," + stationEndId;
            this.actionManager.addEventClosed(eventString);
          }

          if (eventElement.getTagName().equals("attendancePeak")) {
            String stationId = eventElement.getElementsByTagName("stationId").item(0).getTextContent();
            String size = eventElement.getElementsByTagName("size").item(0).getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationId + "," + size;
            this.actionManager.addEventAttendancePeak(eventString);
          }

          if (eventElement.getTagName().equals("stationClosed")) {
            String stationId = eventElement.getElementsByTagName("idStation").item(0).getTextContent();

            String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
                + stationId;
            this.actionManager.addEventStationClosed(eventString);
          }
          if (eventElement.getTagName().equals("hour")) {
            String lineId = eventElement.getElementsByTagName("idLine").item(0).getTextContent();
            String trainNumber = eventElement.getElementsByTagName("trainNumber").item(0).getTextContent();

            String eventString = startTime + "," + endTime + "," + lineId + "," + trainNumber;
            this.actionManager.addEventTrainHour(eventString);
          }
        }
      }

    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

}
