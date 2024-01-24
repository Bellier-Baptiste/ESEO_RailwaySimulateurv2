package org.example.model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test-case of {@link EventRampPeak} model.
 *
 * @file EventRampPeakTest.java
 * @date 2018-12-12
 * @since 0.0.1
 */

public class EventRampPeakTest {
  /**
   * Test the constructor.
   */
  @Test
  public void testConstructor() {
    EventRampPeak event = new EventRampPeak(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventRampPeak.EventType.STATION);
    assertEquals(1, event.getId());
    assertEquals("2018/12/12-12:00", event.getStartTime());
    assertEquals("2018/12/12-13:00", event.getEndTime());
    assertEquals(EventRampPeak.EventType.STATION, event.getType());
    assertEquals(0, event.getIdStation());
  }

  /**
   * Test the constructor with wrong parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorStartTimeAfterEndTimeException() {
    new EventRampPeak(1, "2018/12/12_13:00",
      "2018/12/12_12:00", EventRampPeak.EventType.STATION);
  }

  /**
   * Test the constructor with wrong parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorStartTimeException() {
    new EventRampPeak(1, "test",
      "2018/12/12_12:00", EventRampPeak.EventType.STATION);
  }

  /**
   * Test the constructor with wrong parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorEndTimeException() {
    new EventRampPeak(1, "2018/12/12_13:00",
      "test", EventRampPeak.EventType.STATION);
  }
}
