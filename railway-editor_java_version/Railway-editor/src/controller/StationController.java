/**
 * Class part of the controller package of the application.
 */

package controller;

import model.Station;
import view.MainWindow;
import view.StationView;

/**
 * Controller to add stationView to the mainPanel.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class StationController {

  /**
   * StationController constructor.
   *
   * @param station     station to add
   * @param stationView stationView bound to station
   * @param lineIndex   index of the current line
   */
  public StationController(final Station station, final StationView stationView,
                           final int lineIndex) {
    super();
    MainWindow.getInstance().getMainPanel().getLineViews().get(lineIndex)
        .getStationViews().add(stationView);
    MainWindow.getInstance().getMainPanel().getLineViews().get(lineIndex)
        .getLine().addStation(station);
  }
}
