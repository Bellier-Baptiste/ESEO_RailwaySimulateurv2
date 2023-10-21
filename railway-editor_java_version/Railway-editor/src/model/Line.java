package model;

import java.awt.Color;
import java.util.List;

import data.Data;

/**Model Class of a line.
 * @author Arthur Lagarce
 *
 */
public class Line {

	//attributes
	private int id;
	private Color color;
	private List<Station> stations;
	
	/**Constructor
	 * @param id line id
	 * @param stations stations of the line
	 */
	public Line(int id, List<Station> stations) {
		super();
		this.id = id;
		Data.getInstance();
		this.color = Data.getLinesColors()[id];
		this.stations = stations;
	}
	
	//accessors
	/**get the id of the line.
	 * @return line Id
	 */
	public int getId() {
		return id;
	}
	/**set the id of the line.
	 * @param id line id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**get the color of the line.
	 * @return color
	 */
	public Color getColor() {
		return color;
	}
	/**set the color of the line.
	 * @param color color of the line
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**get the stations of the line
	 * @return list of the stations
	 */
	public List<Station> getStations() {
		return stations;
	}
	/**set the stations of the line.
	 * @param stations stations of the line
	 */
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
	
	//method
	
	/** Add a station to complete the line.
	 * @param station station to add on the line
	 */
	public void addStation(Station station) {
		this.stations.add(station);
	}
	
	
	
}
