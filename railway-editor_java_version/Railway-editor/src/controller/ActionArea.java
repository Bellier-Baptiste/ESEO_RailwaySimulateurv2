/** Class part of the controller package of the application. */

package controller;

import data.Data;
import model.Area;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.AreaView;
import view.MainWindow;

/** Class which describes the action to add an area to the map. */
public class ActionArea {
  /** Action name. */
  public static final String ACTION_NAME = "ADD_AREA";
  /** Default area x position. */
  private static final int AREA_POSX_DEFAULT = 150;
  /** Default area y position. */
  private static final int AREA_POSY_DEFAULT = 150;
  /** Default area width. */
  private static final int AREA_WIDTH_DEFAULT = 75;
  /** Default area height. */
  private static final int AREA_HEIGHT_DEFAULT = 75;
  /** Singleton instance. */
  private static ActionArea instance;

  /**
   * Create Singleton.
   *
   * @return ActionArea instance
   */
  public static ActionArea getInstance() {
    if (instance == null) {
      instance = new ActionArea();
    }
    return instance;
  }

  /**
   * Add an area to the map.
   */
  public void addArea() {
    Area area = new Area(Data.getInstance().getNewAreaId(), AREA_POSX_DEFAULT,
        AREA_POSY_DEFAULT, AREA_WIDTH_DEFAULT, AREA_HEIGHT_DEFAULT);

    Coordinate latLonTop = (Coordinate) MainWindow.getInstance().getMainPanel()
          .getPosition(area.getPosX(), area.getPosY());
    area.setLatitudeTop(latLonTop.getLat());
    area.setLongitudeTop(latLonTop.getLon());

    Coordinate latLonBot = (Coordinate) MainWindow.getInstance().getMainPanel()
          .getPosition(area.getPosX() + area.getWidth(),
            area.getPosY() + area.getHeight());
    area.setLatitudeBot(latLonBot.getLat());
    area.setLongitudeBot(latLonBot.getLon());

    AreaView areaView = new AreaView(area);
    MainWindow.getInstance().getMainPanel().addAreaView(areaView);
    MainWindow.getInstance().getMainPanel().repaint();
  }
}
