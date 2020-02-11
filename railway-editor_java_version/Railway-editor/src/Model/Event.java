package Model;


/**Model class which describes a generic event.
 * @author arthu
 *
 */
public abstract class Event {
	
	private String startTime;
	private String endTime;
	public static enum EventType{LINE,STATION,AREA};
	private EventType type;
	public	 String EVENT_NAME; 


	
	/** Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type event type
	 */
	public Event(String startTime, String endTime, EventType type) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}


	/** get the startTime of the event.
	 * @return String startTime
	 */
	public String getStartTime() {
		return startTime;
	}


	/** set the startTime of the event.
	 * @param startTime event startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	/** get the endTime of the event.
	 * @return String endTime
	 */
	public String getEndTime() {
		return endTime;
	}


	/** set the endTime of the event.
	 * @param endTime event endTime
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	/** get the type of event.
	 * @return EventType type
	 */
	public EventType getType() {
		return type;
	}


	/** set the type of the event.
	 * @param type event type
	 */
	public void setType(EventType type) {
		this.type = type;
	}
	
	
	
	
	
}
