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

package org.example.model;

/**
 * Model class extending {@link org.example.model.EventBetween2Stations} which
 * describes a delay between two stations {@link org.example.model.Station}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventLineDelay.java
 * @date N/A
 * @since 2.0
 */
public class EventLineDelay extends EventBetween2Stations {
  /**
   * Delay in minutes for gaussian peak events.
   */
  private int delay;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventLineDelay(final int id, final String startTime,
                        final String endTime, final EventType type) {
    super(id, startTime, endTime, type, EventName.LINE_DELAYED);
  }

  /**
   * get the delay in second.
   *
   * @return int delay
   */
  public int getDelay() {
    return delay;
  }

  /**
   * set the delay.
   *
   * @param eventDelay event delay
   */
  public void setDelay(final int eventDelay) {
    this.delay = eventDelay;
  }
}
