package controller;

import Model.Station;
import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.LineView;
import view.MainPanel;
import view.MainWindow;
import view.StationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

public class ActionStation extends AbstractAction {
  public static final String ACTION_NAME = "ADD_STATION";
  private static ActionStation instance;
  private int stationid;

  private ActionStation() {
    this.stationid = 0;
  }

  /**Create Singleton
   * @return ActionStation instance
   */
  public static ActionStation getInstance() {
    if (instance == null) {
      instance = new ActionStation();
    }
    return instance;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    int lineToUpdateIndex = ActionLine.getInstance().getLineToUpdateIndex();
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
    List<StationView> stationViews = null;
    try {
      stationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(lineToUpdateIndex)
          .getStationViews();
    } catch (IndexOutOfBoundsException ex) {
      ex.printStackTrace();
      System.out.println("No existing line");
    }
    int stationX = 0;
    int stationY = 0;
    if (!stationViews.isEmpty()) {// if there are already stations on this line
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
    Coordinate latLon = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(station.getPosX(), station.getPosY());
    station.setLatitude(latLon.getLat());
    station.setLongitude(latLon.getLon());

    StationView stationView = new StationView(station);// create stationView relative to this station
    stationView.setStationSize(stationSize);
    stationView.setCenterStationSize(centerStationSize);
    @SuppressWarnings("unused")
    StationController stationController = new StationController(station, stationView, lineToUpdateIndex);
    stationid += 1;
    MainWindow.getInstance().getMainPanel().repaint();
  }

  public int getStationId() {
    return stationid;
  }
}
