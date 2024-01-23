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
 * Model class extending {@link Event} which describes a gaussian peak on a
 * Station.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @author Alexis BONAMY
 * @file EventGaussianPeak.java
 * @date N/A
 * @since 2.0
 */
public class EventGaussianPeak extends EventPeak {
  /** Peak width. */
  private int peakWidth;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      eventType
   */
  public EventGaussianPeak(final int id, final String startTime,
                             final String endTime,
                             final EventType type) {
    super(id, startTime, endTime, type, EventName.GAUSSIAN_PEAK);
  }

  /**
   * get the peak width.
   *
   * @return int peakWidth
   */
  public int getPeakWidth() {
    return peakWidth;
  }

  /**
   * set the peak width.
   *
   * @param paramPeakWidth width of the peak
   */
  public void setPeakWidth(final int paramPeakWidth) {
    if (paramPeakWidth < 0) {
      throw new IllegalArgumentException("Peak width is not valid");
    }
    this.peakWidth = paramPeakWidth;
  }
}
