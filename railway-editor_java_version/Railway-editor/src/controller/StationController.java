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

  //constructor

  /**
   * Constructor.
   *
   * @param station     station to add
   * @param stationView stationView binded to station
   * @param lineIdex    index of the current line
   */
  public StationController(Station station, StationView stationView,
                           int lineIdex) {
    super();
    MainWindow.getInstance().getMainPanel().getLineViews().get(lineIdex)
        .getStationViews().add(stationView);
    MainWindow.getInstance().getMainPanel().getLineViews().get(lineIdex)
        .getLine().addStation(station);
  }

}
