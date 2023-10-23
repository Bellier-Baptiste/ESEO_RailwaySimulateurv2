/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package controller;

/**
 * Enum of the {@link model.Event} names.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventName.java
 * @date 2023-10-02
 * @since 3.0
 */
public enum EventName {
  /**
   * Line delayed event name.
   */
  LINE_DELAYED("lineDelay"),
  /**
   * Line closed event name.
   */
  LINE_CLOSED("lineClosed"),
  /**
   * Attendance peak event name.
   */
  ATTENDANCE_PEAK("attendancePeak"),
  /**
   * Train hour event name.
   */
  TRAIN_HOUR("hour"),
  /**
   * Station closed event name.
   */
  STATION_CLOSED("stationClosed");

  /**
   * String value of the event name.
   */
  private final String value;

  /**
   * Constructor of the enum.
   *
   * @param valueToSet String value to set
   */
  EventName(final String valueToSet) {
    this.value = valueToSet;
  }

  /**
   * Get the string value of the event name.
   *
   * @return String value
   */
  public String getString() {
    return value;
  }
}
