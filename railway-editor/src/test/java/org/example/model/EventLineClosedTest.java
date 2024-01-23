package org.example.model;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Test-case of {@link EventLineClosed} model.
 *
 * @author Marie Bordet
 * @file EventLineClosedTest.java
 * @date 2024-01-09
 * @since 3.0
 */
public class EventLineClosedTest {
  /**
   * Test the constructor.
   */
  @Test
  public void testConstructor() {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    assertEquals(1, event.getId());
    assertEquals("2018/12/12-12:00", event.getStartTime());
    assertEquals("2018/12/12-13:00", event.getEndTime());
    assertEquals(EventLineClosed.EventType.LINE, event.getType());
    assertEquals(0, event.getIdLine());
  }

  /**
   * Test the setStartTime method.
   */
  @Test
  public void testSetStartTime() {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    event.setStartTime("2018/12/12-12:00");
    assertEquals("2018/12/12-12:00", event.getStartTime());
  }

  /**
   * Test the setStartTime method with wrong parameters.
   */
  @Test
  public void testSetStartTimeException() {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    assertThrows(IllegalArgumentException.class, () -> {
      event.setStartTime("test");
    });
  }

  /**
   * Test the setEndTime method.
   */
  @Test
  public void testSetEndTime() {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    event.setEndTime("2018/12/12-13:00");
    assertEquals("2018/12/12-13:00", event.getEndTime());
  }

  /**
   * Test the setEndTime method with wrong parameters.
   */
  @Test
  public void testSetEndTimeException() {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    assertThrows(IllegalArgumentException.class, () -> {
      event.setEndTime("test");
    });
  }

  /**
   * Test the parseTime method.
   *
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  @Test
  public void testParseTime() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    Method method = Event.class.getDeclaredMethod("parseTime", String.class);
    method.setAccessible(true);
    assertEquals(LocalTime.of(12,0), method.invoke(event, "12:00"));
  }

  /**
   * Test the parseTime method with wrong parameters.
   *
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  @Test
  public void testParseTimeException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    EventLineClosed event = new EventLineClosed(1,
        "2018/12/12-12:00", "2018/12/12-13:00",
        EventLineClosed.EventType.LINE);
    Method method = Event.class.getDeclaredMethod("parseTime", String.class);
    method.setAccessible(true);
    assertNull(method.invoke(event, "test"));
  }






}
