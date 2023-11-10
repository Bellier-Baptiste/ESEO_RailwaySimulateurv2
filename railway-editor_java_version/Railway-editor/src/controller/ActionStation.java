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

package controller;

import data.Data;
import model.Station;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.LineView;
import view.MainWindow;
import view.StationView;

import java.util.List;
import java.util.Random;

/**
 * A class for performing actions on the stations views {@link StationView} and
 * the stations {@link Station}.
 * Linked to buttons in {@link view.ToolBarPanel}.
 *
 * @author Aurélie Chamouleau
 * @file ActionStation.java
 * @date 2023-10-02
 * @since 3.0
 */
public final class ActionStation {
  /** Action name of the add button. */
  public static final String ACTION_NAME = "ADD_STATION";
  public static final String DELETE_STATION = "DELETE_STATION";
  /** Low bound for the random position of the station. */
  private static final int LOW_BOUND = 10;
  /** High bound for the random position of the station. */
  private static final int HIGH_BOUND = 350;
  /** X offset position of next station. */
  private static final int X_OFFSET = 25;
  /** Y offset position of next station. */
  private static final int Y_OFFSET = 25;
  /** Station size. */
  public static final int STATION_SIZE = 18;
  /** Center station size. */
  public static final int CENTER_STATION_SIZE = 14;
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
   * Creates a {@link Station} with its linked {@link StationView} created and
   * add this {@link StationView} to the {@link view.MainPanel}
   * thanks to {@link StationController}.
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
    int low = LOW_BOUND;
    int high = HIGH_BOUND;
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
    int stationX;
    int stationY;
    // if there are already stations on this line
    if (stationViews != null && !stationViews.isEmpty()) {
      stationX = stationViews.get(stationViews.size() - 1).getStation()
          .getPosX() + X_OFFSET;
      stationY = stationViews.get(stationViews.size() - 1).getStation()
          .getPosY() + Y_OFFSET;
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

    // create stationView relative to this station
    StationView stationView = new StationView(station);
    stationView.setStationSize(STATION_SIZE);
    stationView.setCenterStationSize(CENTER_STATION_SIZE);
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
