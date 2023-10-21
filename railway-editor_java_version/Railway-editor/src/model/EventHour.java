package model;

/**
 * Model class extending event which describes a change of trains number on a line at a certain hour of the day.
 * @author arthu
 *
 */
public class EventHour extends Event{

	private int idLine;
	private int trainNumber;
	/**Constructor.
	 * @param startTime event startTime
	 * @param endTime event endTime
	 * @param type event eventType
	 */
	public EventHour(String startTime, String endTime, EventType type) {
		super(startTime, endTime, type);
		setEventName("hour");

	}
	/**get the id of the line concerned.
	 * @return int idLine
	 */
	public int getIdLine() {
		return idLine;
	}
	/**set the id of the line concerned.
	 * @param idLine id of the line concerned
	 */
	public void setIdLine(int idLine) {
		this.idLine = idLine;
	}
	/**get the new train number.
	 * @return int train number
	 */
	public int getTrainNumber() {
		return trainNumber;
	}
	/**set the new train number.
	 * @param trainNumber new number of train
	 */
	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}
	

}
