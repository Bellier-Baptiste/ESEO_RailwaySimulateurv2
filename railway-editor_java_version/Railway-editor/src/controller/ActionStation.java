/**
 * Class part of the controller package of the application.
 */

package controller;

import Model.Station;
import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.LineView;
import view.MainWindow;
import view.StationView;

import java.util.List;
import java.util.Random;

/**
 * Class that handles the action of adding a station to the current line.
 */
public final class ActionStation {
  /** Action name of the add button. */
  public static final String ACTION_NAME = "ADD_STATION";

  /** Singleton instance. */
  private static ActionStation instance;

  /** Current station id. */
  private int stationId;

  /** Random object to generate random numbers. */
  private final Random rand = new Random();


  private ActionStation() {
    this.stationId = 0;
  }

  /** Create Singleton.
   *
   * @return ActionStation instance
   */
  public static ActionStation getInstance() {
    if (instance == null) {
      instance = new ActionStation();
    }
    return instance;
  }

  /**
   * Add a station to the current line.
   */
  public void addStation() {
    int lineToUpdateIndex = ActionLine.getInstance().getLineToUpdateIndex();
    stationId = 0;
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      // count stations number to get a new stationId
      stationId += lineView.getStationViews().size();
    }
    // new first station Location
    int low = 10;
    int high = 350;
    int x = this.rand.nextInt(high - low) + low;
    int y = this.rand.nextInt(high - low) + low;

    // adjust size relative to the zoom level
    List<StationView> stationViews = null;
    try {
      stationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(
          lineToUpdateIndex)
          .getStationViews();
    } catch (IndexOutOfBoundsException ex) {
      // No line existing
      ex.printStackTrace();
    }
    int stationX = 0;
    int stationY = 0;
    // if there are already stations on this line
    if (stationViews != null && !stationViews.isEmpty()) {
      stationX = stationViews.get(stationViews.size() - 1).getStation()
          .getPosX() + 25;
      stationY = stationViews.get(stationViews.size() - 1).getStation()
          .getPosY() + 25;
    } else { // if is the first station for the line
      stationX = x;
      stationY = y;
    }

    // setup station name
    int randomIndex = this.rand.nextInt(Data.getInstance()
        .getAvailableStationNames().size());
    String stationName = Data.getInstance().getAvailableStationNames().get(
        randomIndex);
    Data.getInstance().getAvailableStationNames().remove(randomIndex);
    // create station (model)
    Station station = new Station(stationId, stationX, stationY, stationName);
    Coordinate latLon = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(station.getPosX(), station.getPosY());
    station.setLatitude(latLon.getLat());
    station.setLongitude(latLon.getLon());

    int stationSize = 18;
    int centerStationSize = 14;
    // create stationView relative to this station
    StationView stationView = new StationView(station);
    stationView.setStationSize(stationSize);
    stationView.setCenterStationSize(centerStationSize);
    @SuppressWarnings("unused")
    StationController stationController = new StationController(station,
        stationView, lineToUpdateIndex);
    stationId += 1;
    MainWindow.getInstance().getMainPanel().repaint();
  }

  /**
   * Get the current station id.
   *
   * @return stationId
   */
  public int getStationId() {
    return stationId;
  }
}
