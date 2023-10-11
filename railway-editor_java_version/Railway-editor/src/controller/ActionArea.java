package controller;

import Model.Area;
import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.AreaView;
import view.MainWindow;

// TODO : make this class a singleton
public class ActionArea {
  public static final String ACTION_NAME = "ADD_AREA";
  private static final int AREA_POSX_DEFAULT = 150;
  private static final int AREA_POSY_DEFAULT = 150;
  private static final int AREA_WIDTH_DEFAULT = 75;
  private static final int AREA_HEIGHT_DEFAULT = 75;
  private static ActionArea instance;

  /**
   * Create Singleton
   *
   * @return ActionArea instance
   */
  public static ActionArea getInstance() {
    if (instance == null) {
      instance = new ActionArea();
    }
    return instance;
  }

  public void addArea() {
    Area area = new Area(Data.getInstance().getNewAreaId(), AREA_POSX_DEFAULT, AREA_POSY_DEFAULT,
        AREA_WIDTH_DEFAULT, AREA_HEIGHT_DEFAULT);

    Coordinate latLonTop = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(area.getPosX(), area.getPosY());
    area.setLatitudeTop(latLonTop.getLat());
    area.setLongitudeTop(latLonTop.getLon());

    Coordinate latLonBot = (Coordinate) MainWindow.getInstance().getMainPanel().getPosition(area.getPosX()+area.getWidth(), area.getPosY()+area.getHeight());
    area.setLatitudeBot(latLonBot.getLat());
    area.setLongitudeBot(latLonBot.getLon());

    AreaView areaView = new AreaView(area);
    MainWindow.getInstance().getMainPanel().addAreaView(areaView);
    MainWindow.getInstance().getMainPanel().repaint();
  }
}
