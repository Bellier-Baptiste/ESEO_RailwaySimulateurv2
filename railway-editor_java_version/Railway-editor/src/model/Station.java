/** Class part of the model package of the application. */

package model;

import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.MainWindow;

/**
 * Model class for a station.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class Station {

  //attributes
  /** Station posX in pixel. */
  private int posX;
  /** Station posY in pixel. */
  private int posY;
  /** Station name. */
  private String name;
  /** Station id. */
  private int id;
  /** Station area. */
  private Area area;
  /** Station latitude. */
  private double latitude;
  /** Station longitude. */
  private double longitude;


  /**
   * Constructor for a station.
   */
  public Station() {
    this.posX = 0;
    this.posY = 0;
    this.name = Data.getStationsNames()[0];
  }

  /**
   * Constructor with its parameters.
   *
   * @param stationId   station id
   * @param stationPosX station positionX
   * @param stationPosY station positionY
   * @param stationName station name
   */
  public Station(final int stationId, final int stationPosX,
                 final int stationPosY, final String stationName) {
    super();
    this.id = stationId;
    this.posX = stationPosX;
    this.posY = stationPosY;
    this.name = stationName;
  }

  //accessors

  /**
   * get station posX in pixel.
   *
   * @return posX
   */
  public int getPosX() {
    return posX;
  }


  /**
   * set station posX in pixel.
   *
   * @param stationPosX station positionX
   */
  public void setPosX(final int stationPosX) {
    this.posX = stationPosX;
  }


  /**
   * get station posY in pixel.
   *
   * @return posY
   */
  public int getPosY() {
    return posY;
  }


  /**
   * set stationPosY in pixel.
   *
   * @param stationPosY station positionY
   */
  public void setPosY(final int stationPosY) {
    this.posY = stationPosY;
  }


  /**
   * get station Latitude.
   *
   * @return double  latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * set station latitude.
   *
   * @param stationLatitude station latitude
   */
  public void setLatitude(final double stationLatitude) {
    this.latitude = stationLatitude;
  }

  /**
   * get station Longitude.
   *
   * @return double longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * set station longitude.
   *
   * @param stationLongitude station longitude
   */
  public void setLongitude(final double stationLongitude) {
    this.longitude = stationLongitude;
  }

  /**
   * get station name.
   *
   * @return string name
   */
  public String getName() {
    return name;
  }


  /**
   * set station name.
   *
   * @param stationName station name
   */
  public void setName(final String stationName) {
    this.name = stationName;
  }


  /**
   * get station Id.
   *
   * @return int id
   */
  public int getId() {
    return id;
  }

  /**
   * set station id.
   *
   * @param stationId station id
   */
  public void setId(final int stationId) {
    this.id = stationId;
  }


  /**
   * get station area.
   *
   * @return Area
   */
  public Area getArea() {
    return area;
  }

  /**
   * set station Area.
   *
   * @param stationArea station area
   */
  public void setAreas(final Area stationArea) {
    this.area = stationArea;
  }


  /**
   * Moves the station of dx and dy and update latitudes and longitudes
   * in consequences.
   *
   * @param dx deltaX
   * @param dy deltaY
   */
  public void moveStation(final int dx, final int dy) {
    this.posX += dx;
    this.posY += dy;
    this.setPosX(this.posX);
    this.setPosY(this.posY);
    Coordinate latLon = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(this.posX, this.posY);
    this.setLatitude(latLon.getLat());
    this.setLongitude(latLon.getLon());
  }
}
