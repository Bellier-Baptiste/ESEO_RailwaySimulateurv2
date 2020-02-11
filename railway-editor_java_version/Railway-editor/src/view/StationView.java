package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Model.Station;

/**
 * Class which draw a station on the main Panel
 * 
 * @author Arthur Lagarce
 *
 */
public class StationView {
	// attributes
	private Station station;
	private int stationSize;
	private int centerStationSize;
	private Stroke stroke;
	private Color centerCircleColor;

	/**
	 * Constructor.
	 * 
	 * @param station station to bind
	 */
	public StationView(Station station) {
		this.station = station;
		this.stationSize = 18;
		this.centerStationSize = 14;
		this.stroke = new BasicStroke(5);
		this.centerCircleColor = Color.WHITE;
	}

	/**
	 * get the station binded to the view.
	 * 
	 * @return Station station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * bind a station to the view.
	 * 
	 * @param station station to bind
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	/**get the stationView size.
	 * @return int size
	 */
	public int getStationSize() {
		return stationSize;
	}

	/**set the stationView size.
	 * @param stationSize extrnal circle radius
	 */
	public void setStationSize(int stationSize) {
		this.stationSize = stationSize;
	}

	/**get center circle station size (the white one).
	 * @return int size
	 */
	public int getCenterStationSize() {
		return centerStationSize;
	}

	/** set the center circle station size.
	 * @param centerStationSize circle radius
	 */
	public void setCenterStationSize(int centerStationSize) {
		this.centerStationSize = centerStationSize;
	}

	// methods
	/**
	 * draw 2 cicrles (one with color for the outline and another filled with white
	 * to hide the polyline behind)
	 * 
	 * @param g2D graphics component
	 * @param color intern circle color
	 */
	public void affiche(Graphics2D g2D, Color color) {
		g2D.setColor(color);
		g2D.setStroke(this.stroke);
		g2D.drawOval(station.getPosX() - stationSize / 2, station.getPosY() - stationSize / 2, stationSize,
				stationSize);
		g2D.setColor(centerCircleColor);
		g2D.fillOval(station.getPosX() - centerStationSize / 2, station.getPosY() - centerStationSize / 2,
				centerStationSize, centerStationSize);
	}
	
	/**set the stroke.
	 * @param stroke stroke
	 */
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	/**get the center circle color.
	 * @return Color centerCircleColor
	 */
	public Color getCenterCircleColor() {
		return centerCircleColor;
	}

	/**set the center circle color
	 * @param centerCircleColor intern circle color
	 */
	public void setCenterCircleColor(Color centerCircleColor) {
		this.centerCircleColor = centerCircleColor;
	}
	

}
