/** Class part of the view package of the application. */

package view;

import model.Station;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Class which draw a station on the main Panel.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class StationView {
  // constants
  /**
   * Station size.
   */
  private static final int STATION_SIZE = 18;
  /**
   * Center circle station size.
   */
  private static final int CENTER_STATION_SIZE = 14;
  /**
   * Stroke width.
   */
  private static final int STROKE_WIDTH = 5;
  // attributes
  /**
   * Station bound to the view.
   */
  private Station station;
  /**
   * Station size.
   */
  private int stationSize;
  /**
   * Center circle station size.
   */
  private int centerStationSize;
  /**
   * Stroke of the station.
   */
  private Stroke stroke;
  /**
   * Center circle color.
   */
  private Color centerCircleColor;

  /**
   * StationView constructor.
   *
   * @param stationBound station to bind
   */
  public StationView(final Station stationBound) {
    this.station = stationBound;
    this.stationSize = STATION_SIZE;
    this.centerStationSize = CENTER_STATION_SIZE;
    this.stroke = new BasicStroke(STROKE_WIDTH);
    this.centerCircleColor = Color.WHITE;
  }

  /**
   * get the station bound to the view.
   *
   * @return the bound station of the station view
   */
  public Station getStation() {
    return station;
  }

  /**
   * bind a station to the view.
   *
   * @param stationBound station to bind
   */
  public void setStation(final Station stationBound) {
    this.station = stationBound;
  }

  /**
   * get the stationView size.
   *
   * @return int size
   */
  public int getStationSize() {
    return stationSize;
  }

  /**
   * set the stationView size.
   *
   * @param stationSizeToSet external circle radius
   */
  public void setStationSize(final int stationSizeToSet) {
    this.stationSize = stationSizeToSet;
  }

  /**
   * get center circle station size (the white one).
   *
   * @return int size
   */
  public int getCenterStationSize() {
    return centerStationSize;
  }

  /**
   * set the center circle station size.
   *
   * @param centerStationSizeToSet circle radius
   */
  public void setCenterStationSize(final int centerStationSizeToSet) {
    this.centerStationSize = centerStationSizeToSet;
  }

  // methods

  /**
   * draw 2 circles (one with color for the outline and another filled with
   * white to hide the polyline behind).
   *
   * @param g2D   graphics component
   * @param color intern circle color
   */
  public void show(final Graphics2D g2D, final Color color) {
    g2D.setColor(color);
    g2D.setStroke(this.stroke);
    g2D.drawOval(station.getPosX() - stationSize / 2, station.getPosY()
        - stationSize / 2, stationSize, stationSize);
    g2D.setColor(centerCircleColor);
    g2D.fillOval(station.getPosX() - centerStationSize / 2,
        station.getPosY() - centerStationSize / 2, centerStationSize,
        centerStationSize);
  }

  /**
   * set the stroke.
   *
   * @param strokeToSet stroke
   */
  public void setStroke(final Stroke strokeToSet) {
    this.stroke = strokeToSet;
  }

  /**
   * get the center circle color.
   *
   * @return Color centerCircleColor
   */
  public Color getCenterCircleColor() {
    return centerCircleColor;
  }

  /**
   * set the center circle color.
   *
   * @param centerCircleColorToSet intern circle color
   */
  public void setCenterCircleColor(final Color centerCircleColorToSet) {
    this.centerCircleColor = centerCircleColorToSet;
  }
}
