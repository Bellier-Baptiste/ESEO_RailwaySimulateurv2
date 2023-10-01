package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Model.Area;
import Model.Event;
import Model.Event.EventType;
import Model.EventAttendancePeak;
import Model.EventHour;
import Model.EventLineClosed;
import Model.EventLineDelay;
import Model.EventStationClosed;
import Model.Line;
import Model.Station;
import data.Data;
import view.AreaView;
import view.EventWindow;
import view.LineView;
import view.MainWindow;
import view.StationView;

/**
 * Class which execute action linked with buttons in the toolbar panel
 * 
 * @author Arthur Lagarce
 *
 */
public class ActionManager {

	// attributes
	private int lineToUpdateIndex;
	private int stationid;

	private static final int AREA_POSX_DEFAULT = 150;
	private static final int AREA_POSY_DEFAULT = 150;
	private static final int AREA_WIDTH_DEFAULT = 75;
	private static final int AREA_HEIGHT_DEFAULT = 75;
	public EventWindow eventWindow;

	/**
	 * constructor
	 * 
	 */
	public ActionManager() {
		lineToUpdateIndex = 0;
		stationid = 0;

	}

	// accessors
	/**get line to update index.
	 * @return int lineToUpdateIndex
	 */
//	public int getLineToUpdateIndex() {
//		return lineToUpdateIndex;
//	}

	/**set line to update index.
	 * @param lineToUpdateIndex index of the currenLine
	 */
//	public void setLineToUpdateIndex(int lineToUpdateIndex) {
//		this.lineToUpdateIndex = lineToUpdateIndex;
//	}

	// methods

	/**
	 * Add a new line to the map.
	 * 
	 */
//	public void addLine() {
//		int lineIndex = MainWindow.getInstance().getMainPanel().getLineViews().size(); // find the new line number
//		this.setLineToUpdateIndex(lineIndex);
//		Line line = new Line(lineIndex, new ArrayList<Station>()); // create a new line (model)
//		List<StationView> stationsViews = new ArrayList<StationView>(); // create a new vie for the new line's sattions
//		LineView lineview = new LineView(line, stationsViews);
//		@SuppressWarnings("unused")
//		LineController lineController = new LineController(line, lineview);
//		MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(lineIndex));// change line id
//																									// displayed in the
//																									// toolBar Panel
//		MainWindow.getInstance().getMainPanel().repaint();
//	}

	/**
	 * Add a new station to the current Line.
	 * 
	 */
//	public void addStation() {
//		stationid = 0;
//		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
//			stationid += lineView.getStationViews().size();// count stations number to get a new stationId
//		}
//		// new first station Location
//		Random r = new Random();
//		int low = 10;
//		int high = 350;
//		int x = r.nextInt(high - low) + low;
//		int y = r.nextInt(high - low) + low;
//
//		// adjust size relative to the zoom level
//		int stationSize = 18;
//		int centerStationSize = 14;
//    List<StationView> stationViews = null;
//		try {
//      stationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(lineToUpdateIndex)
//          .getStationViews();
//    } catch (IndexOutOfBoundsException e) {
//      e.printStackTrace();
//      System.out.println("No existing line");
//    }
//		int stationX = 0;
//		int stationY = 0;
//		if (!stationViews.isEmpty()) {// if there are already stations on this line
//			stationX = stationViews.get(stationViews.size() - 1).getStation().getPosX() + 25;
//			stationY = stationViews.get(stationViews.size() - 1).getStation().getPosY() + 25;
//		} else {// if is the first station for the line
//			stationX = x;
//			stationY = y;
//		}
//
//		// setup station name
//		Random rand = new Random();
//
//		int randomIndex = rand.nextInt(Data.getInstance().getAvailableStationNames().size());
//		String stationName = Data.getInstance().getAvailableStationNames().get(randomIndex);
//		Data.getInstance().getAvailableStationNames().remove(randomIndex);
//		Station station = new Station(stationid, stationX, stationY, stationName);// create station (model)
//		Coordinate latLon = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(station.getPosX(), station.getPosY());
//		station.setLatitude(latLon.getLat());
//		station.setLongitude(latLon.getLon());
//
//		StationView stationView = new StationView(station);// create stationView relative to this station
//		// System.out.println(stationView.getStation().getId());
//		stationView.setStationSize(stationSize);
//		stationView.setCenterStationSize(centerStationSize);
//		@SuppressWarnings("unused")
//		StationController stationController = new StationController(station, stationView, lineToUpdateIndex);
//		stationid += 1;
//		MainWindow.getInstance().getMainPanel().repaint();
//	}

	/**
	 * Create new Area.
	 */
//	public void addArea() {
//		Area area = new Area(Data.getInstance().getNewAreaId(), AREA_POSX_DEFAULT, AREA_POSY_DEFAULT,
//				AREA_WIDTH_DEFAULT, AREA_HEIGHT_DEFAULT);
//
//		Coordinate latLonTop = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(area.getPosX(), area.getPosY());
//		area.setLatitudeTop(latLonTop.getLat());
//		area.setLongitudeTop(latLonTop.getLon());
//
//		Coordinate latLonBot = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(area.getPosX()+area.getWidth(), area.getPosY()+area.getHeight());
//		area.setLatitudeBot(latLonBot.getLat());
//		area.setLongitudeBot(latLonBot.getLon());
//
//		AreaView areaView = new AreaView(area);
//		MainWindow.getInstance().getMainPanel().addAreaView(areaView);
//		MainWindow.getInstance().getMainPanel().repaint();
//	}

	/**
	 * create event editor Windows and display it.
	 */
//	public void addEvent() {
//		eventWindow = new EventWindow(this);
//		eventWindow.setLocation(MainWindow.getInstance().getX() + 200, MainWindow.getInstance().getY() + 20);
//		eventWindow.setVisible(true);
//	}

	/**
	 * set event editor window visible
	 */

	/**
	 * set event editor window not visible
	 */

	/**create LineEventDelay add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
//	public void addEventDelay(String eventString) {
//		String[] eventStringTab = eventString.split(",");
//		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
//		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
//		EventLineDelay eventLineDelay = new EventLineDelay(startTime, endTime, EventType.LINE);
//		eventLineDelay.setIdStationStart(Integer.valueOf(eventStringTab[4]));
//		eventLineDelay.setIdStationEnd(Integer.valueOf(eventStringTab[5]));
//		int delay = Integer.valueOf(eventStringTab[6].split(":")[0]) * 60
//				+ Integer.valueOf(eventStringTab[6].split(":")[1]);
//		eventLineDelay.setDelay(delay);
//		Data.getInstance().getEventList().add(eventLineDelay);
//
//		Station stationStart = null;
//		Station stationEnd = null;
//		LineView lineDelayed = null;
//		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
//			for (StationView stationView : lineView.getStationViews()) {
//				if (stationView.getStation().getId() == eventLineDelay.getIdStationStart()) {
//					lineDelayed = lineView;
//					stationStart = stationView.getStation();
//				} else if (stationView.getStation().getId() == eventLineDelay.getIdStationEnd()) {
//					stationEnd = stationView.getStation();
//				}
//			}
//		}
//		if (lineDelayed != null && stationStart != null && stationEnd != null) {
//			if (stationEnd.getId() < stationStart.getId()) {
//				Station aux = stationEnd;
//				stationEnd = stationStart;
//				stationStart = aux;
//			}
//			for (StationView stationView : lineDelayed.getStationViews()) {
//				if (isInDelta(stationView.getStation(), stationStart, stationEnd, lineDelayed.getLine())) {
//					stationView.setCenterCircleColor(Color.ORANGE);
//				}
//			}
//			if (eventWindow != null) {
//				eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
//			}
//		}
//
//		MainWindow.getInstance().getMainPanel().repaint();
//
//		String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
//		int id = Data.getInstance().getEventList().size();
//		MainWindow.getInstance().getEventRecapPanel().createEventLineDelayed(id,startTime, endTime, locationsStr,
//				eventStringTab[6], Integer.toString(lineDelayed.getLine().getId()));
//		MainWindow.getInstance().getEventRecapPanel().revalidate();
//	}
	
	/**create LineEventClosed add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
//	public void addEventClosed(String eventString) {
//		String[] eventStringTab = eventString.split(",");
//		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
//		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
//		EventLineClosed eventLineClosed = new EventLineClosed(startTime, endTime, EventType.LINE);
//		eventLineClosed.setIdStationStart(Integer.valueOf(eventStringTab[4]));
//		eventLineClosed.setIdStationEnd(Integer.valueOf(eventStringTab[5]));
//		Data.getInstance().getEventList().add(eventLineClosed);
//
//		Station stationStart = null;
//		Station stationEnd = null;
//		LineView lineDelayed = null;
//		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
//			for (StationView stationView : lineView.getStationViews()) {
//				if (stationView.getStation().getId() == eventLineClosed.getIdStationStart()) {
//					lineDelayed = lineView;
//					stationStart = stationView.getStation();
//				} else if (stationView.getStation().getId() == eventLineClosed.getIdStationEnd()) {
//					stationEnd = stationView.getStation();
//				}
//			}
//		}
//		if (lineDelayed != null && stationStart != null && stationEnd != null) {
//			if (stationEnd.getId() < stationStart.getId()) {
//				Station aux = stationEnd;
//				stationEnd = stationStart;
//				stationStart = aux;
//			}
//			for (StationView stationView : lineDelayed.getStationViews()) {
//				if (isInDelta(stationView.getStation(), stationStart, stationEnd, lineDelayed.getLine())) {
//					stationView.setCenterCircleColor(Color.RED);
//				}
//			}
//			if (eventWindow != null) {
//				eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
//			}
//
//		}
//		MainWindow.getInstance().getMainPanel().repaint();
//		String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
//		int id = Data.getInstance().getEventList().size();
//		MainWindow.getInstance().getEventRecapPanel().createEventLineClosed(id,startTime, endTime, locationsStr,
//				eventStringTab[5]);
//		MainWindow.getInstance().getEventRecapPanel().revalidate();
//	}

	/**create AttendancePeakEvent add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
//	public void addEventAttendancePeak(String eventString) {
////		String[] eventStringTab = eventString.split(",");
////		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
////		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
////		EventAttendancePeak eventAttendancePeak = new EventAttendancePeak(startTime, endTime, EventType.STATION);
////		eventAttendancePeak.setIdStation(Integer.valueOf(eventStringTab[4]));
////		eventAttendancePeak.setSize(Integer.parseInt(eventStringTab[5]));
////		Data.getInstance().getEventList().add(eventAttendancePeak);
////
////		Station stationConcerned = null;
////		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
////			for (StationView stationView : lineView.getStationViews()) {
////				if (stationView.getStation().getId() == eventAttendancePeak.getIdStation()) {
////					stationConcerned = stationView.getStation();
////					stationView.setCenterCircleColor(Color.YELLOW);
////				}
////			}
////		}
////
////		if (eventWindow != null) {
////			eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
////		}
////		MainWindow.getInstance().getMainPanel().repaint();
////		int id = Data.getInstance().getEventList().size();
////		MainWindow.getInstance().getEventRecapPanel().createEventAttendancePeak(id,startTime, endTime,
////				Integer.toString(stationConcerned.getId()), eventStringTab[5]);
////		MainWindow.getInstance().getEventRecapPanel().revalidate();
//	}
	
	/**create StationClosed Event add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
//	public void addEventStationClosed(String eventString) {
//		String[] eventStringTab = eventString.split(",");
//		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
//		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
//		EventStationClosed eventStationClosed = new EventStationClosed(startTime, endTime, EventType.STATION);
//		eventStationClosed.setIdStation(Integer.valueOf(eventStringTab[4]));
//		Data.getInstance().getEventList().add(eventStationClosed);
//
//		Station stationConcerned = null;
//		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
//			for (StationView stationView : lineView.getStationViews()) {
//				if (stationView.getStation().getId() == eventStationClosed.getIdStation()) {
//					stationConcerned = stationView.getStation();
//					stationView.setCenterCircleColor(Color.RED);
//				}
//			}
//		}
//
//		EventWindow.getInstance().dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
//		MainWindow.getInstance().getMainPanel().repaint();
//		int id = Data.getInstance().getEventList().size();
//		MainWindow.getInstance().getEventRecapPanel().createEventStationClosed(id,startTime, endTime,
//				Integer.toString(stationConcerned.getId()));
//		MainWindow.getInstance().getEventRecapPanel().revalidate();
//	}
	/**create TrainHourEvent and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
//	public void addEventTrainHour(String eventString) {
//		String[] eventStringTab = eventString.split(",");
//		String startTime = eventStringTab[0];
//		String endTime = eventStringTab[1];
//		EventHour eventHour = new EventHour(startTime, endTime, EventType.LINE);
//		eventHour.setIdLine(Integer.valueOf(eventStringTab[2]));
//		eventHour.setTrainNumber(Integer.parseInt(eventStringTab[3]));
//		Data.getInstance().getEventList().add(eventHour);
//
//		if (eventWindow != null) {
//			eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
//		}
//		MainWindow.getInstance().getMainPanel().repaint();
//		int id = Data.getInstance().getEventList().size();
//		MainWindow.getInstance().getEventRecapPanel().createEventHour(id,startTime, endTime, eventStringTab[2],
//				eventStringTab[3]);
//		MainWindow.getInstance().getEventRecapPanel().revalidate();
//	}



	/**
	 * check if a station id between the starting and ending station.
	 * @param station
	 * @param stationStart
	 * @param stationEnd
	 * @param line
	 * @return
	 */
//	private boolean isInDelta(Station station, Station stationStart, Station stationEnd, Line line) {
//		if (line.getStations().contains(station)) {
//			if (line.getStations().indexOf(station) >= line.getStations().indexOf(stationStart)
//					&& line.getStations().indexOf(station) <= line.getStations().indexOf(stationEnd)) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}

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

	/**
	 * Navigates through the existing lines
	 * 
	 */
//	public void incrementLine() {
//		JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();
//
//		if (!lineId.getText().equals("none")) { // if a line exists
//			int currentLineId = Integer.valueOf(lineId.getText());
//			if (currentLineId < MainWindow.getInstance().getMainPanel().getLineViews().size() - 1) {// if this is not
//																									// the last line
//																									// created
//				System.out.println("increment");
//				MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(currentLineId + 1));
//				this.setLineToUpdateIndex(currentLineId + 1);
//			}
//		}
//	}

	/**
	 * Navigates through existing lines
	 * 
	 */
//	public void decrementLine() {
//		JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();
//
//		if (!lineId.getText().equals("none")) {// if a line exists
//			int currentLineId = Integer.valueOf(lineId.getText());
//			if (currentLineId > 0) {// if this is not the first line created
//				System.out.println("decrement");
//				MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(currentLineId - 1));
//				this.setLineToUpdateIndex(currentLineId - 1);
//			}
//		}
//	}

	/**
	 * save an XML file which describes all the map
	 * 
	 * @param fileToSave xml file
	 */



	
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


//  public void runSimulation() {
//    try {
//			System.out.println(System.getProperty("user.dir"));
//			String rootJavaProjectPath = System.getProperty("user.dir");
//			String rootGoProjectPath = rootJavaProjectPath.replace("railway-editor_java_version", "pfe-2018-network-journey-simulator");
//			File runThisSimulation = new File(rootGoProjectPath + "\\src\\configs\\runThisSimulation.xml");
//			this.export(runThisSimulation);
//
//			// create a new list of arguments for our process
//      String[] commands = {"cmd", "/C", "start metro_simulator.exe -configname runThisSimulation.xml"};
//      //String[] commands = {"cmd", "/C", "start metro_simulator.exe"};
//      // create the process builder
//      ProcessBuilder pb = new ProcessBuilder(commands);
//      // set the working directory of the process
//      pb.directory(new File(rootGoProjectPath));
//      Process process = pb.start();
//      // wait that the process finish
//      int exitCode = process.waitFor();
//      // verify the exit code
//      if (exitCode == 0) {
//        System.out.println("The Go file has been executed successfully !");
//      } else {
//        System.err.println("Error while executing the Go file. Exit code : " + exitCode);
//      }
//    } catch (IOException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
}
