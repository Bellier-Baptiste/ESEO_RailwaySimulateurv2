package model;

/**
 * Model class extending event which describes a delay between 2 stations.
 * @author arthu
 *
 */
public class EventLineDelay extends Event {

	private int idStationStart;
	private int idStationEnd;
	private int delay;
	
	/** 
	 * Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type event eventType
	 */
	public EventLineDelay(int id, String startTime, String endTime, EventType type) {
		super(id, startTime, endTime, type);
		eventName = "lineDelay";
	}

	/**
	 * get the id of the starting station.
	 * @return int idStationStart
	 */
	public int getIdStationStart() {
		return idStationStart;
	}

	/**
	 * get the id of the ending station.
	 * @param idStationStart event stationStart Id
	 */
	public void setIdStationStart(int idStationStart) {
		this.idStationStart = idStationStart;
	}

	/** get the id of the ending station.
	 * @return idStationEnd
	 */
	public int getIdStationEnd() {
		return idStationEnd;
	}

	/**set the id of the ending station.
	 * @param idStationEnd event stationEnd Id
	 */
	public void setIdStationEnd(int idStationEnd) {
		this.idStationEnd = idStationEnd;
	}

	/** get the delay in second.
	 * @return int delay
	 */
	public int getDelay() {
		return delay;
	}

	/**set the delay.
	 * @param delay event delay
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	
	

}
