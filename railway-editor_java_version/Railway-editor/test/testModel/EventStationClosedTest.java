package testModel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Model.EventStationClosed;

public class EventStationClosedTest {

    @Test
    public void testConstructor() {
        EventStationClosed event = new EventStationClosed("2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        assertEquals("2018/12/12_12:00", event.getStartTime());
        assertEquals("2018/12/12_13:00", event.getEndTime());
        assertEquals(EventStationClosed.EventType.STATION, event.getType());
        assertEquals("stationClosed", event.getEventName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorException() {
        new EventStationClosed("2018/12/12_13:00", "2018/12/12_12:00", EventStationClosed.EventType.STATION);
    }

    @Test
    public void testSetStartTime() {
        EventStationClosed event = new EventStationClosed("2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setStartTime("2018/12/12_13:00");
        assertEquals("2018/12/12_13:00", event.getStartTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStartTimeException() {
        EventStationClosed event = new EventStationClosed("2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setStartTime("2018/12/12_14:00");
    }

    @Test
    public void testSetEndTime() {
        EventStationClosed event = new EventStationClosed("2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setEndTime("2018/12/12_14:00");
        assertEquals("2018/12/12_14:00", event.getEndTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEndTimeException() {
        EventStationClosed event = new EventStationClosed("2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setEndTime("2018/12/12_11:00");
    }
}
