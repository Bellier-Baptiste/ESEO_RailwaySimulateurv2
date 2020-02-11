package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
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
	public int getLineToUpdateIndex() {
		return lineToUpdateIndex;
	}

	/**set line to update index.
	 * @param lineToUpdateIndex index of the currenLine
	 */
	public void setLineToUpdateIndex(int lineToUpdateIndex) {
		this.lineToUpdateIndex = lineToUpdateIndex;
	}

	// methods

	/**
	 * Add a new line to the map.
	 * 
	 */
	public void addLine() {
		int lineIndex = MainWindow.getInstance().getMainPanel().getLineViews().size(); // find the new line number
		this.setLineToUpdateIndex(lineIndex);
		Line line = new Line(lineIndex, new ArrayList<Station>()); // create a new line (model)
		List<StationView> stationsViews = new ArrayList<StationView>(); // create a new vie for the new line's sattions
		LineView lineview = new LineView(line, stationsViews);
		@SuppressWarnings("unused")
		LineController lineController = new LineController(line, lineview);
		MainWindow.getInstance().getToolBarPanel().getLineId().setText(Integer.toString(lineIndex));// change line id
																									// displayed in the
																									// toolBar Panel
		MainWindow.getInstance().getMainPanel().repaint();
	}

	/**
	 * Add a new station to the current Line.
	 * 
	 */
	public void addStation() {
		stationid = 0;
		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
			stationid += lineView.getStationViews().size();// count stations number to get a new stationId
		}
		// new first station Location
		Random r = new Random();
		int low = 10;
		int high = 350;
		int x = r.nextInt(high - low) + low;
		int y = r.nextInt(high - low) + low;

		// adjust size relative to the zoom level
		int stationSize = 18;
		int centerStationSize = 14;

		List<StationView> stationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(lineToUpdateIndex)
				.getStationViews();
		int stationX = 0;
		int stationY = 0;
		if (stationViews.size() > 0) {// if there are already stations on this line
			stationX = stationViews.get(stationViews.size() - 1).getStation().getPosX() + 25;
			stationY = stationViews.get(stationViews.size() - 1).getStation().getPosY() + 25;
		} else {// if is the first station for the line
			stationX = x;
			stationY = y;
		}

		// setup station name
		Random rand = new Random();

		int randomIndex = rand.nextInt(Data.getInstance().getAvailableStationNames().size());
		String stationName = Data.getInstance().getAvailableStationNames().get(randomIndex);
		Data.getInstance().getAvailableStationNames().remove(randomIndex);
		Station station = new Station(stationid, stationX, stationY, stationName);// create station (model)
		Coordinate latLon = MainWindow.getInstance().getMainPanel().getPosition(station.getPosX(), station.getPosY());
		station.setLatitude(latLon.getLat());
		station.setLongitude(latLon.getLon());

		StationView stationView = new StationView(station);// create stationView relative to this station
		// System.out.println(stationView.getStation().getId());
		stationView.setStationSize(stationSize);
		stationView.setCenterStationSize(centerStationSize);
		@SuppressWarnings("unused")
		StationController stationController = new StationController(station, stationView, lineToUpdateIndex);
		stationid += 1;
		MainWindow.getInstance().getMainPanel().repaint();
	}

	/**
	 * Create new Area.
	 */
	public void addArea() {
		Area area = new Area(Data.getInstance().getNewAreaId(), AREA_POSX_DEFAULT, AREA_POSY_DEFAULT,
				AREA_WIDTH_DEFAULT, AREA_HEIGHT_DEFAULT);
		
		Coordinate latLonTop = MainWindow.getInstance().getMainPanel().getPosition(area.getPosX(), area.getPosY());
		area.setLatitudeTop(latLonTop.getLat());
		area.setLongitudeTop(latLonTop.getLon());
		
		Coordinate latLonBot = MainWindow.getInstance().getMainPanel().getPosition(area.getPosX()+area.getWidth(), area.getPosY()+area.getHeight());
		area.setLatitudeBot(latLonBot.getLat());
		area.setLongitudeBot(latLonBot.getLon());

		AreaView areaView = new AreaView(area);
		MainWindow.getInstance().getMainPanel().addAreaView(areaView);
		MainWindow.getInstance().getMainPanel().repaint();
	}

	/**
	 * create event editor Windows and display it.
	 */
	public void addEvent() {
		eventWindow = new EventWindow(this);
		eventWindow.setLocation(MainWindow.getInstance().getX() + 200, MainWindow.getInstance().getY() + 20);
		eventWindow.setVisible(true);
	}

	/**
	 * set event editor window visible
	 */
	public void displayEventWindow() {
		eventWindow.setVisible(true);
	}
	/**
	 * set event editor window not visible
	 */
	public void hideEventWindow() {
		eventWindow.setVisible(false);
	}

	/**create LineEventDelay add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
	public void addEventDelay(String eventString) {
		String[] eventStringTab = eventString.split(",");
		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
		EventLineDelay eventLineDelay = new EventLineDelay(startTime, endTime, EventType.LINE);
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
				if (isInDelta(stationView.getStation(), stationStart, stationEnd, lineDelayed.getLine())) {
					stationView.setCenterCircleColor(Color.ORANGE);
				}
			}
			if (eventWindow != null) {
				eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
			}
		}

		MainWindow.getInstance().getMainPanel().repaint();

		String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
		int id = Data.getInstance().getEventList().size();
		MainWindow.getInstance().getEventRecapPanel().createEventLineDelayed(id,startTime, endTime, locationsStr,
				eventStringTab[6], Integer.toString(lineDelayed.getLine().getId()));
		MainWindow.getInstance().getEventRecapPanel().revalidate();
	}
	
	/**create LineEventClosed add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
	public void addEventClosed(String eventString) {
		String[] eventStringTab = eventString.split(",");
		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
		EventLineClosed eventLineClosed = new EventLineClosed(startTime, endTime, EventType.LINE);
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
			if (eventWindow != null) {
				eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
			}

		}
		MainWindow.getInstance().getMainPanel().repaint();
		String locationsStr = "from " + stationStart.getName() + " to " + stationEnd.getName();
		int id = Data.getInstance().getEventList().size();
		MainWindow.getInstance().getEventRecapPanel().createEventLineClosed(id,startTime, endTime, locationsStr,
				eventStringTab[5]);
		MainWindow.getInstance().getEventRecapPanel().revalidate();
	}

	/**create AttendancePeakEvent add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
	public void addEventAttendancePeak(String eventString) {
		String[] eventStringTab = eventString.split(",");
		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
		EventAttendancePeak eventAttendancePeak = new EventAttendancePeak(startTime, endTime, EventType.STATION);
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

		if (eventWindow != null) {
			eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
		}
		MainWindow.getInstance().getMainPanel().repaint();
		int id = Data.getInstance().getEventList().size();
		MainWindow.getInstance().getEventRecapPanel().createEventAttendancePeak(id,startTime, endTime,
				Integer.toString(stationConcerned.getId()), eventStringTab[5]);
		MainWindow.getInstance().getEventRecapPanel().revalidate();
	}
	
	/**create StationClosed Event add color to the concerned stations and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
	public void addEventStationClosed(String eventString) {
		String[] eventStringTab = eventString.split(",");
		String startTime = eventStringTab[0] + "_" + eventStringTab[1];
		String endTime = eventStringTab[2] + "_" + eventStringTab[3];
		EventStationClosed eventStationClosed = new EventStationClosed(startTime, endTime, EventType.STATION);
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

		if (eventWindow != null) {
			eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
		}
		MainWindow.getInstance().getMainPanel().repaint();
		int id = Data.getInstance().getEventList().size();
		MainWindow.getInstance().getEventRecapPanel().createEventStationClosed(id,startTime, endTime,
				Integer.toString(stationConcerned.getId()));
		MainWindow.getInstance().getEventRecapPanel().revalidate();
	}
	/**create TrainHourEvent and create a recap in eventRecapPanel.
	 * @param eventString String with all the event Elements
	 */
	public void addEventTrainHour(String eventString) {
		String[] eventStringTab = eventString.split(",");
		String startTime = eventStringTab[0];
		String endTime = eventStringTab[1];
		EventHour eventHour = new EventHour(startTime, endTime, EventType.LINE);
		eventHour.setIdLine(Integer.valueOf(eventStringTab[2]));
		eventHour.setTrainNumber(Integer.parseInt(eventStringTab[3]));
		Data.getInstance().getEventList().add(eventHour);

		if (eventWindow != null) {
			eventWindow.dispatchEvent(new WindowEvent(eventWindow, WindowEvent.WINDOW_CLOSING));
		}
		MainWindow.getInstance().getMainPanel().repaint();
		int id = Data.getInstance().getEventList().size();
		MainWindow.getInstance().getEventRecapPanel().createEventHour(id,startTime, endTime, eventStringTab[2],
				eventStringTab[3]);
		MainWindow.getInstance().getEventRecapPanel().revalidate();
	}



	/**
	 * check if a station id between the starting and ending station.
	 * @param station
	 * @param stationStart
	 * @param stationEnd
	 * @param line
	 * @return
	 */
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

	/**
	 * iterate over all stations and area to find which stations are in which area,
	 * then assign areas to stations.
	 */
	private void assignAreaToStations() {
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
	public void incrementLine() {
		JLabel lineId = MainWindow.getInstance().getToolBarPanel().getLineId();

		if (!lineId.getText().equals("none")) { // if a line exists
			int currentLineId = Integer.valueOf(lineId.getText());
			if (currentLineId < MainWindow.getInstance().getMainPanel().getLineViews().size() - 1) {// if this is not
																									// the last line
																									// created
				System.out.println("increment");
				MainWindow.getInstance().getToolBarPanel().getLineId().setText(Integer.toString(currentLineId + 1));
				this.setLineToUpdateIndex(currentLineId + 1);
			}
		}
	}

	/**
	 * Navigates through existing lines
	 * 
	 */
	public void decrementLine() {
		JLabel lineId = MainWindow.getInstance().getToolBarPanel().getLineId();

		if (!lineId.getText().equals("none")) {// if a line exists
			int currentLineId = Integer.valueOf(lineId.getText());
			if (currentLineId > 0) {// if this is not the first line created
				System.out.println("decrement");
				MainWindow.getInstance().getToolBarPanel().getLineId().setText(Integer.toString(currentLineId - 1));
				this.setLineToUpdateIndex(currentLineId - 1);
			}
		}
	}

	/**
	 * save an XML file which describes all the map
	 * 
	 * @param fileToSave xml file
	 */
	public void export(File fileToSave) {
		assignAreaToStations();
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
				name.appendChild(document.createTextNode(toAlphabetic(lineView.getLine().getId())));
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
						this.addEventDelay(eventString);
					}

					if (eventElement.getTagName().equals("lineClose")) {
						String stationStartId = eventElement.getElementsByTagName("stationIdStart").item(0)
								.getTextContent();
						String stationEndId = eventElement.getElementsByTagName("stationIdEnd").item(0)
								.getTextContent();

						String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
								+ stationStartId + "," + stationEndId;
						this.addEventClosed(eventString);
					}

					if (eventElement.getTagName().equals("attendancePeak")) {
						String stationId = eventElement.getElementsByTagName("stationId").item(0).getTextContent();
						String size = eventElement.getElementsByTagName("size").item(0).getTextContent();

						String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
								+ stationId + "," + size;
						this.addEventAttendancePeak(eventString);
					}
					
					if (eventElement.getTagName().equals("stationClosed")) {
						String stationId = eventElement.getElementsByTagName("idStation").item(0).getTextContent();

						String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
								+ stationId;
						this.addEventStationClosed(eventString);
					}
					if (eventElement.getTagName().equals("hour")) {
						String lineId = eventElement.getElementsByTagName("idLine").item(0).getTextContent();
						String trainNumber = eventElement.getElementsByTagName("trainNumber").item(0).getTextContent();

						String eventString = startTime + "," + endTime + "," + lineId + "," + trainNumber;
						this.addEventTrainHour(eventString);
					}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
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
