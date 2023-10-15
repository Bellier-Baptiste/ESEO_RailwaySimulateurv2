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
  private int posX;
  private int posY;
  private String name;
  private int id;
  private Area area;
  private double latitude;
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
   * @param id   station id
   * @param posX station positionX
   * @param posY station positionY
   * @param name station name
   */
  public Station(int id, int posX, int posY, String name) {
    super();
    this.posX = posX;
    this.posY = posY;
    this.name = name;
    this.id = id;
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
   * @param posX station positionX
   */
  public void setPosX(int posX) {
    this.posX = posX;
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
   * @param posY station positionY
   */
  public void setPosY(int posY) {
    this.posY = posY;
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
   * @param latitude station latitude
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
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
   * @param longitude station longitude
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
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
   * @param name station name
   */
  public void setName(String name) {
    this.name = name;
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
   * @param id station id
   */
  public void setId(int id) {
    this.id = id;
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
   * @param area station area
   */
  public void setAreas(Area area) {
    this.area = area;
  }


  /**
   * Moves the station of dx and dy and update latitudes and longitudes
   * in consequences.
   *
   * @param dx deltaX
   * @param dy deltaY
   */
  public void moveStation(int dx, int dy) {
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
