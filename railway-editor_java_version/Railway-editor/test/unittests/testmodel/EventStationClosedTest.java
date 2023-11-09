/*
 * License : MIT License
 *
 *  Copyright (c) 2023 Team PFE_2023_16
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package unittests.testmodel;

import static org.junit.Assert.assertEquals;

import model.Line;
import org.junit.Test;
import model.EventStationClosed;

/**
 * Test-case of {@link Line} model.
 *
 * @author Benoît Vavasseur
 * @file EventStationClosedTest.java
 * @date N/A
 * @since 2.0
 */

public class EventStationClosedTest {

    @Test
    public void testConstructor() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12-12:00", "2018/12/12-13:00", EventStationClosed.EventType.STATION);
        assertEquals(1, event.getId());
        assertEquals("2018/12/12-12:00", event.getStartTime());
        assertEquals("2018/12/12-13:00", event.getEndTime());
        assertEquals(EventStationClosed.EventType.STATION, event.getType());
        assertEquals(0, event.getIdStation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStartTimeAfterEndTimeException() {
        new EventStationClosed(1, "2018/12/12_13:00", "2018/12/12_12:00", EventStationClosed.EventType.STATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStartTimeException() {
        new EventStationClosed(1, "test", "2018/12/12_12:00", EventStationClosed.EventType.STATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEndTimeException() {
        new EventStationClosed(1, "2018/12/12_13:00", "test", EventStationClosed.EventType.STATION);
    }

    @Test
    public void testSetStartTime() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12-12:00", "2018/12/12-13:00", EventStationClosed.EventType.STATION);
        event.setStartTime("2018/12/12-13:00");
        assertEquals("2018/12/12-13:00", event.getStartTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStartTimeException() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setStartTime("2018/12/12_14:00");
    }

    @Test
    public void testSetEndTime() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12-12:00", "2018/12/12-13:00", EventStationClosed.EventType.STATION);
        event.setEndTime("2018/12/12-14:00");
        assertEquals("2018/12/12-14:00", event.getEndTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEndTimeException() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12_12:00", "2018/12/12_13:00", EventStationClosed.EventType.STATION);
        event.setEndTime("2018/12/12_11:00");
    }

    @Test
    public void testSetAndGetIdStation() {
        EventStationClosed event = new EventStationClosed(1, "2018/12/12-12:00", "2018/12/12-13:00", EventStationClosed.EventType.STATION);
        event.setIdStation(5);
        assertEquals(5, event.getIdStation());
    }
}
