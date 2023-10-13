package model;

/**
 *Model class extendig event which describes the closing of a line portion.
 * @author arthu
 *
 */
public class EventLineClosed extends Event {
	

	private int idStationStart;
	private int idStationEnd;
	
	
	/** Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type event eventType
	 */
	public EventLineClosed(int id, String startTime, String endTime, EventType type) {
		super(id, startTime, endTime, type);
		this.eventName = "lineClosed";
	}


	/** get the id of the starting station.
	 * @return int idStationStart
	 */
	public int getIdStationStart() {
		return idStationStart;
	}

	/** set the if of the starting station.
	 * @param idStationStart event stationStart id
	 */
	public void setIdStationStart(int idStationStart) {
		this.idStationStart = idStationStart;
	}

	/**
	 * get the id of the ending station.
	 * @return the id of the ending station
	 */
	public int getIdStationEnd() {
		return idStationEnd;
	}

	/**
	 * set the id of the ending station.
	 * @param idStationEnd event stationEnd id
	 */
	public void setIdStationEnd(int idStationEnd) {
		this.idStationEnd = idStationEnd;
	}
	
	
	



}
