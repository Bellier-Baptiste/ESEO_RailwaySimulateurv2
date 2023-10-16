/**
 * Class part of the model package of the application.
 */

package model;

import data.Data;

import java.awt.Color;
import java.util.List;

/**
 * Model Class of a line.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class Line {

  //attributes
  /** Line id. */
  private int id;
  /** Line color. */
  private Color color;
  /** Line stations. */
  private List<Station> stations;

  /**
   * Line constructor.
   *
   * @param lineId       line id
   * @param lineStations stations of the line
   */
  public Line(final int lineId, final List<Station> lineStations) {
    super();
    this.id = lineId;
    this.color = Data.getInstance().getLinesColors()[id];
    this.stations = lineStations;
  }

  //accessors

  /**
   * get the id of the line.
   *
   * @return line Id
   */
  public int getId() {
    return id;
  }

  /**
   * set the id of the line.
   *
   * @param lineId line id
   */
  public void setId(final int lineId) {
    this.id = lineId;
  }

  /**
   * get the color of the line.
   *
   * @return color
   */
  public Color getColor() {
    return color;
  }

  /**
   * set the color of the line.
   *
   * @param lineColor color of the line
   */
  public void setColor(final Color lineColor) {
    this.color = lineColor;
  }

  /**
   * get the stations of the line.
   *
   * @return list of the stations
   */
  public List<Station> getStations() {
    return stations;
  }

  /**
   * set the stations of the line.
   *
   * @param lineStations stations of the line
   */
  public void setStations(final List<Station> lineStations) {
    this.stations = lineStations;
  }

  //method

  /**
   * Add a station to complete the line.
   *
   * @param station station to add on the line
   */
  public void addStation(final Station station) {
    this.stations.add(station);
  }


}
