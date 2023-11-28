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
 * Model class extending {@link Event} which describes a change of trains
 * number on a {@link org.example.model.Line} at a certain hour of the day.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file EventHour.java
 * @date N/A
 * @since 2.0
 */
public class EventHour extends Event {
  /**
   * Line id.
   */
  private int idLine;
  /**
   * Number of trains to add.
   */
  private int trainNumber;

  /**
   * Constructor.
   *
   * @param id        event id
   * @param startTime event startTime
   * @param endTime   event endTime
   * @param type      event eventType
   */
  public EventHour(final int id, final String startTime, final String endTime,
                   final EventType type) {
    super(id, startTime, endTime, type);
    super.setEventName(EventName.TRAIN_HOUR);
  }

  /**
   * get the id of the line concerned.
   *
   * @return int idLine
   */
  public int getIdLine() {
    return idLine;
  }

  /**
   * set the id of the line concerned.
   *
   * @param eventIdLine id of the line concerned
   */
  public void setIdLine(final int eventIdLine) {
    this.idLine = eventIdLine;
  }

  /**
   * get the new train number.
   *
   * @return int train number
   */
  public int getTrainNumber() {
    return trainNumber;
  }

  /**
   * set the new train number.
   *
   * @param eventTrainNumber new number of train
   */
  public void setTrainNumber(final int eventTrainNumber) {
    this.trainNumber = eventTrainNumber;
  }


}
