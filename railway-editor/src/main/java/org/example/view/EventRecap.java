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

package org.example.view;

import org.example.data.Data;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

/**
 * Panel which contains a short description of created events.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @author Alexis BONAMY
 * @file ClockView.java
 * @date N/A
 * @since 2.0
 */
public final class EventRecap extends JScrollPane {
  /**
   * EventRecap serial version UID.
   */
  private static final long serialVersionUID = 1L;
  // constants
  /** default width of the panel. */
  public static final int DEFAULT_WIDTH = 220;
  /** default height of the panel. */
  public static final int DEFAULT_HEIGHT = 600;
  /** default font of the panel. */
  private static final String SEGEOE_UI = "Segoe UI";
  /** string of the for the starting date. */
  private static final String START_DATE = "Start date: ";
  /** string of the for the ending date. */
  private static final String END_DATE = "End date: ";
  /** string of the for the line. */
  private static final String LINE = "Line: ";
  /** string of the for the remove action. */
  private static final String REMOVE = "remove";
  /** Main font size. */
  private static final int MAIN_FONT_SIZE = 12;
  /** Details font size. */
  private static final int DETAILS_FONT_SIZE = 11;
  /**
   * Number of minutes in an hour.
   */
  private static final int MINUTES_IN_HOUR = 60;
  /** Singleton instance of the class. */
  private static EventRecap instance;
  /** task pane container. */
  private final JXTaskPaneContainer taskPaneContainer;


  /**
   * Constructor of the class.
   */
  private EventRecap() {
    this.setPreferredSize(new Dimension(DEFAULT_WIDTH,
        DEFAULT_HEIGHT));
    TitledBorder eventRecapBorder = new TitledBorder("Events List");
    this.setBorder(eventRecapBorder);
    this.taskPaneContainer = new JXTaskPaneContainer();
    this.eventsListRemoveBackground();
  }

  /**
   * Return Singleton.
   *
   * @return EventRecap instance
   */
  public static EventRecap getInstance() {
    if (instance == null) {
      instance = new EventRecap();
    }
    return instance;
  }

  /**
   * Function to remove the native background of the task pane container.
   */
  public void eventsListRemoveBackground() {
    this.taskPaneContainer.setBackgroundPainter(null);
    this.taskPaneContainer.repaint();
    this.taskPaneContainer.revalidate();
  }

  /**
   * Create a recap for event line delay.
   *
   * @param id           event id
   * @param startDateStr event startDate
   * @param endDateStr   event endDate
   * @param locationsStr stations concerned
   * @param delayStr     event delay
   * @param lineStr      line concerned
   */
  public void createEventLineDelayed(final int id, final String startDateStr,
                                     final String endDateStr,
                                     final String locationsStr,
                                     final String delayStr,
                                     final String lineStr) {
    JXTaskPane taskpane = new JXTaskPane();
    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Line Delayed");
    String newDelayStr = delayStr;
    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel locations = new JXLabel();
    locations.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    locations.setText(locationsStr);
    locations.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    line.setText(LINE + lineStr);
    line.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel delay = new JXLabel();
    delay.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    if (!delayStr.contains(":")) {
      // if for example delay = 186, then delay should be 3:06
      int delayInt = Integer.parseInt(delayStr);
      int delayHours = delayInt / MINUTES_IN_HOUR;
      int delayMinutes = delayInt % MINUTES_IN_HOUR;
      newDelayStr = delayHours + ":" + delayMinutes;
      newDelayStr = formatTime(newDelayStr);
    }
    delay.setText("Delay: " + newDelayStr);
    delay.setHorizontalAlignment(SwingConstants.LEFT);

    // add various actions and components to the taskpane

    taskpane.add(startDate);
    taskpane.add(endDate);
    taskpane.add(locations);
    taskpane.add(line);
    taskpane.add(delay);
    taskpane.add(new AbstractAction(REMOVE) {
      /**
       * Remove action serial version UID.
       */
      private static final long serialVersionUID = 1L;

      public void actionPerformed(final ActionEvent e) {
        taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId()
            == id);
        taskPaneContainer.revalidate();
      }
    });

    // add the task pane to the taskPaneContainer
    this.taskPaneContainer.add(taskpane);
    this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();
  }

  /**
   * Format a time string to have two digits for hours and minutes.
   *
   * @param input time string to format
   *
   * @return formatted time string
   */
  private static String formatTime(final String input) {
    String[] parts = input.split(":");

    // Get the parts before and after the ":" (or "0" if they don't exist)
    String hours = (parts.length > 0) ? parts[0] : "0";
    String minutes = (parts.length > 1) ? parts[1] : "0";

    // Format the parts with two digits
    hours = String.format("%02d", Integer.parseInt(hours));
    minutes = String.format("%02d", Integer.parseInt(minutes));

    return hours + ":" + minutes;
  }

  /**
   * create a recap for event line closed.
   *
   * @param id           event id
   * @param startDateStr event start date
   * @param endDateStr   event end date
   * @param locationsStr stations concerned
   * @param lineStr      line concerned
   */
  public void createEventMultipleStationsClosed(final int id,
                                                final String startDateStr,
                                                final String endDateStr,
                                                final String locationsStr,
                                                final String lineStr) {
    JXTaskPane taskpane = new JXTaskPane();
    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Multiple Stations Closed");

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel locations = new JXLabel();
    locations.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    locations.setText(locationsStr);
    locations.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    line.setText(LINE + lineStr);
    line.setHorizontalAlignment(SwingConstants.LEFT);


    // add various actions and components to the taskpane

    taskpane.add(startDate);
    taskpane.add(endDate);
    taskpane.add(locations);
    taskpane.add(line);
    taskpane.add(new AbstractAction(REMOVE
    ) {
      /**
       * Remove action serial version UID.
       */
      private static final long serialVersionUID = 1L;

      public void actionPerformed(final ActionEvent e) {
        taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId()
            == id);
        taskPaneContainer.revalidate();
      }
    });

    // add the task pane to the taskPaneContainer
    this.taskPaneContainer.add(taskpane);
    this.setViewportView(taskPaneContainer);
  }

  /**
   * create a recap for event attendancePeak.
   *
   * @param id           event id
   * @param startDateStr event start date
   * @param endDateStr   event end date
   * @param peakDateStr peak time
   * @param stationStr   station concerned
   * @param peakStr      peak amount
   * @param peakWidthStr peak width
   */
  public void createEventAttendancePeak(final int id, final String startDateStr,
                                        final String endDateStr,
                                        final String peakDateStr,
                                        final String stationStr,
                                        final String peakStr,
                                        final String peakWidthStr) {
    JXTaskPane taskpane = new JXTaskPane();

    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Attendance Peak");

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel peakTime = new JXLabel();
    peakTime.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    peakTime.setText("Peak time: " + peakDateStr);
    peakTime.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel station = new JXLabel();
    station.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    station.setText("Station: " + stationStr);
    station.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel peak = new JXLabel();
    peak.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    peak.setText("Peak amount: " + peakStr);
    peak.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel peakWidth = new JXLabel();
    peakWidth.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    peakWidth.setText("Peak width: " + peakWidthStr);
    peakWidth.setHorizontalAlignment(SwingConstants.LEFT);

    // add various actions and components to the taskpane
    taskpane.add(startDate);
    taskpane.add(endDate);
    taskpane.add(peakTime);
    taskpane.add(station);
    taskpane.add(peak);
    taskpane.add(peakWidth);
    taskpane.add(new AbstractAction(REMOVE
    ) {
      /**
       * Remove action serial version UID.
       */
      private static final long serialVersionUID = 1L;

      public void actionPerformed(final ActionEvent e) {
        taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId()
            == id);
        taskPaneContainer.revalidate();
      }
    });

    // add the task pane to the taskPaneContainer
    this.taskPaneContainer.add(taskpane);
    this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();

  }

  /**
   * create a recap for event stationClosed.
   *
   * @param id           event id
   * @param startDateStr event start date
   * @param endDateStr   event end date
   * @param stationStr   station concerned
   *                     // * @param peakStr peak amount
   */
  public void createEventStationClosed(final int id, final String startDateStr,
                                       final String endDateStr,
                                       final String stationStr) {
    JXTaskPane taskpane = new JXTaskPane();
    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Station Closed");

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel station = new JXLabel();
    station.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    station.setText("Station: " + stationStr);
    station.setHorizontalAlignment(SwingConstants.LEFT);

    // add various actions and components to the taskpane

    taskpane.add(startDate);
    taskpane.add(endDate);
    taskpane.add(station);
    taskpane.add(new AbstractAction(REMOVE
    ) {
      /**
       * Remove action serial version UID.
       */
      private static final long serialVersionUID = 1L;
      public void actionPerformed(final ActionEvent e) {
        taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId()
            == id);
        taskPaneContainer.revalidate();
      }
    });
    // add the task pane to the taskPaneContainer
    this.taskPaneContainer.add(taskpane);
    this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();
  }

  /**
   * create a recap for event hour.
   *
   * @param id           event id
   * @param startTimeStr event startDate
   * @param endTimeStr   event endDate
   * @param lineStr      line concerned
   * @param trainNbStr   trainNumber amount
   */
  public void createEventHour(final int id, final String startTimeStr,
                              final String endTimeStr, final String lineStr,
                              final String trainNbStr) {
    JXTaskPane taskpane = new JXTaskPane();
    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Train Hour");

    JXLabel startTime = new JXLabel();
    startTime.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    startTime.setText("Start Time: " + startTimeStr);
    startTime.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endTime = new JXLabel();
    endTime.setFont(new Font(SEGEOE_UI, Font.ITALIC, MAIN_FONT_SIZE));
    endTime.setText("End Time: " + endTimeStr);
    endTime.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    line.setText(LINE + lineStr);
    line.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel trainNb = new JXLabel();
    trainNb.setFont(new Font(SEGEOE_UI, Font.ITALIC, DETAILS_FONT_SIZE));
    trainNb.setText("Train amount: " + trainNbStr);
    trainNb.setHorizontalAlignment(SwingConstants.LEFT);

    // add various actions and components to the taskpane
    taskpane.add(startTime);
    taskpane.add(endTime);
    taskpane.add(line);
    taskpane.add(trainNb);
    taskpane.add(new AbstractAction(REMOVE) {
      /**
       * Remove action serial version UID.
       */
      private static final long serialVersionUID = 1L;

      public void actionPerformed(final ActionEvent e) {
        taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId()
            == id);
        taskPaneContainer.revalidate();
      }
    });

    // add the task pane to the taskPaneContainer
    this.taskPaneContainer.add(taskpane);
    this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();
  }

  /**
   * Remove all events from the list.
   * Used when loading a new file.
   */
  public void cleanEvents() {
    this.taskPaneContainer.removeAll();
    this.taskPaneContainer.revalidate();
    Data.getInstance().getEventList().clear();
  }
}
