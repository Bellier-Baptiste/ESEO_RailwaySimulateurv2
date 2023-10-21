package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Model class which describes a generic event.
 *
 * @author arthu, Benoît VAVASSEUR
 */
public abstract class Event {

    private String startTime;
    private String endTime;

    public enum EventType {LINE, STATION, AREA}

    private EventType type;

    private String eventName;

    /**
     * Constructor.
     *
     * @param startTime event startTime in format "yyyy/MM/dd_HH:mm"
     * @param endTime   event endTime in format "yyyy/MM/dd_HH:mm"
     * @param type      event type
     */
    protected Event(String startTime, String endTime, EventType type) {
        if (isTimeValid(startTime) && isTimeValid(endTime)
                && !isStartTimeAfterEndTime(startTime, endTime)) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.type = type;
        } else {
            throw new IllegalArgumentException("Invalid start time or end " +
                    "time.");
        }
    }

    /**
     * Get the startTime of the event.
     *
     * @return String startTime in format "yyyy/MM/dd_HH:mm"
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Set the startTime of the event.
     *
     * @param startTime event startTime in format "yyyy/MM/dd_HH:mm"
     * @throws IllegalArgumentException if the provided time is invalid or after
     *                                  endTime
     */
    public void setStartTime(String startTime) {
        if (isTimeValid(startTime) && !isStartTimeAfterEndTime(startTime,
                this.endTime)) {
            this.startTime = startTime;
        } else {
            throw new IllegalArgumentException("Invalid start time.");
        }
    }

    /**
     * Get the endTime of the event.
     *
     * @return String endTime in format "yyyy/MM/dd_HH:mm"
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Set the endTime of the event.
     *
     * @param endTime event endTime in format "yyyy/MM/dd_HH:mm"
     * @throws IllegalArgumentException if the provided time is invalid
     *                                  or before startTime
     */
    public void setEndTime(String endTime) {
        if (isTimeValid(endTime) && !isStartTimeAfterEndTime(this.startTime,
                endTime)) {
            this.endTime = endTime;
        } else {
            throw new IllegalArgumentException("Invalid end time.");
        }
    }

    /**
     * Get the type of event.
     *
     * @return EventType type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Set the type of the event.
     *
     * @param type event type
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * Parses a given string to a LocalDateTime using the custom format
     * "yyyy/MM/dd_HH:mm".
     *
     * @param time The date-time string to be parsed.
     * @return LocalDateTime representation of the provided string or null if
     * parsing fails.
     */
    private LocalDateTime parseTime(String time) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm");
        try {
            return LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checks if the provided date-time string is valid according to the custom
     * format "yyyy/MM/dd_HH:mm".
     *
     * @param time The date-time string to be validated.
     * @return true if the string is a valid date-time, false otherwise.
     */
    private boolean isTimeValid(String time) {
        return parseTime(time) != null;
    }

    /**
     * Checks if the provided startTime is after the provided endTime using
     * the custom format "yyyy/MM/dd_HH:mm".
     *
     * @param startTime The start date-time string to be compared.
     * @param endTime   The end date-time string to be compared.
     * @return true if the startTime is after endTime, false otherwise or if
     * either time is invalid.
     */
    private boolean isStartTimeAfterEndTime(String startTime, String endTime) {
        LocalDateTime start = parseTime(startTime);
        LocalDateTime end = parseTime(endTime);
        return start != null && end != null && start.isAfter(end);
    }
    /**
     * Get the name of the event.
     *
     * @return String eventName
     */
    public String getEventName() {
        return eventName;
    }
    /**
     * Set the name of the event.
     *
     * @param eventName event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
