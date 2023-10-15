/**
 * Class part of the view package of the application.
 */

package view;

import data.Data;
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
 * panel which contains a short description of created events.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public final class EventRecap extends JScrollPane {
  /**
   * EventRecap serial version UID.
   */
  private static final long serialVersionUID = 1L;
  // constants
  /** default width of the panel. */
  public static final int LARGEUR_PAR_DEFAUT = 220;
  /** default height of the panel. */
  public static final int HAUTEUR_PAR_DEFAUT = 600;
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
  /** Singleton instance of the class. */
  private static EventRecap instance;
  /** task pane container. */
  private final JXTaskPaneContainer taskPaneContainer;


  /**
   * Constructor of the class.
   */
  private EventRecap() {
    this.setPreferredSize(new Dimension(LARGEUR_PAR_DEFAUT,
        HAUTEUR_PAR_DEFAUT));
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

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel locations = new JXLabel();
    locations.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    locations.setText(locationsStr);
    locations.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    line.setText(LINE + lineStr);
    line.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel delay = new JXLabel();
    delay.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    delay.setText("Delay: " + delayStr);
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
   * create a recap for event line closed.
   *
   * @param id           event id
   * @param startDateStr event start date
   * @param endDateStr   event end date
   * @param locationsStr stations concerned
   * @param lineStr      line concerned
   */
  public void createEventLineClosed(final int id, final String startDateStr,
                                    final String endDateStr,
                                    final String locationsStr,
                                    final String lineStr) {
    JXTaskPane taskpane = new JXTaskPane();
    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Line Closed");

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel locations = new JXLabel();
    locations.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    locations.setText(locationsStr);
    locations.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
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
   * @param stationStr   station concerned
   * @param peakStr      peak amount
   */
  public void createEventAttendancePeak(final int id, final String startDateStr,
                                        final String endDateStr,
                                        final String stationStr,
                                        final String peakStr) {
    JXTaskPane taskpane = new JXTaskPane();

    // create a taskpane, and set it's title and icon
    taskpane.setTitle("Attendance Peak");

    JXLabel startDate = new JXLabel();
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel station = new JXLabel();
    station.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    station.setText("Station: " + stationStr);
    station.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel peak = new JXLabel();
    peak.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    peak.setText("Peak amount: " + peakStr);
    peak.setHorizontalAlignment(SwingConstants.LEFT);

    // add various actions and components to the taskpane

    taskpane.add(startDate);
    taskpane.add(endDate);
    taskpane.add(station);
    taskpane.add(peak);
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
    startDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    startDate.setText(START_DATE + startDateStr);
    startDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endDate = new JXLabel();
    endDate.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    endDate.setText(END_DATE + endDateStr);
    endDate.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel station = new JXLabel();
    station.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
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
    startTime.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    startTime.setText("Start Time: " + startTimeStr);
    startTime.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel endTime = new JXLabel();
    endTime.setFont(new Font(SEGEOE_UI, Font.ITALIC, 12));
    endTime.setText("End Time: " + endTimeStr);
    endTime.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel line = new JXLabel();
    line.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
    line.setText(LINE + lineStr);
    line.setHorizontalAlignment(SwingConstants.LEFT);
    JXLabel trainNb = new JXLabel();
    trainNb.setFont(new Font(SEGEOE_UI, Font.ITALIC, 11));
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

}
