package model;

/**
 * Model class extending event which describes an attendance peak on a station. 
 * @author arthu
 *
 */
public class EventAttendancePeak extends Event {

	private int idStation;
	private int size;
	
	/**Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type eventType
	 */
	public EventAttendancePeak(String startTime, String endTime, EventType type) {
		super(startTime, endTime, type);
		setEventName("attendancePeak");
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

	/** get the peak size.
	 * @return int size
	 */
	public int getSize() {
		return size;
	}

	/** set the peak size.
	 * @param size size of the peak
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	
}
