package model;

public class EventStationClosed extends Event{
	private int idStation;
	
	/**Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type eventType
	 */
	public EventStationClosed(String startTime, String endTime, EventType type) {
		super(startTime, endTime, type);
		setEventName("stationClosed");
	}

	/**get the id of the station concerned by the peak.
	 * @return int id
	 */
	public int getIdStation() {
		return idStation;
	}

	/**set the id of the station concerned.
	 * @param idStation id station concerned by the peak
	 */
	public void setIdStation(int idStation) {
		this.idStation = idStation;
	}


}
