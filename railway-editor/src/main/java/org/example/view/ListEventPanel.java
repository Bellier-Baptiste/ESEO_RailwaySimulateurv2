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

import org.example.controller.ActionLine;
import org.example.controller.ActionMetroEvent;
import org.example.data.Data;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

/**
 * Panel of all the {@link org.example.model.Event} edition elements.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ListEventPanel.java
 * @date N/A
 * @since 2.0
 */
public final class ListEventPanel extends JPanel {
  // constants
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  /** Scroll pane width. */
  private static final int SCROLL_PANE_WIDTH = 500;
  /** Scroll pane height. */
  private static final int SCROLL_PANE_HEIGHT = 500;
  /** Table width. */
  private static final int TABLE_WIDTH = 600;
  /** Table height. */
  private static final int TABLE_HEIGHT = 100;
  /** Table type column max width. */
  private static final int TABLE_TYPE_COLUMN_MAX_WIDTH = 50;
  /** Table event description column min width. */
  private static final int TABLE_EVENT_DESCRIPTION_COLUMN_MIN_WIDTH = 200;
  /** GridBagConstraints x position. */
  private static final int GRID_X_POSITION = 0;
  /** GridBagConstraints width. */
  private static final int GRID_WIDTH = 2;
  /** GridBagConstraints weight. */
  private static final double GRID_WEIGHT = 0.1;
  /** GridBagConstraints y position. */
  private static final int GRID_Y_POSITION = 10;
  /** properties text today. */
  public static final String PROPERTIES_TEXT_TODAY = "text.today";
  /** properties text today value. */
  public static final String PROPERTIES_TEXT_TODAY_VALUE = "Today";
  /** properties text month. */
  public static final String PROPERTIES_TEXT_MONTH = "text.month";
  /** properties text month value. */
  public static final String PROPERTIES_TEXT_MONTH_VALUE = "Month";
  /** properties text year. */
  public static final String PROPERTIES_TEXT_YEAR = "text.year";
  /** properties text year value. */
  public static final String PROPERTIES_TEXT_YEAR_VALUE = "Year";
  /** properties text select start time. */
  public static final String START_TIME = "Start Time: ";
  /** properties text select end time. */
  public static final String END_TIME = "End Time: ";
  /** how to format date in the date picker. */
  public static final String FORMAT_DATE = "yyyy/MM/dd";
  /** how to format time in the date picker. */
  public static final String FORMAT_TIME = "HH:mm";
  /** selection png path. */
  @SuppressWarnings("squid:S1075")
  public static final String SELECTION_PNG_PATH = "/images/selection.png";
  /** list event panel column names. */
  private static final String[] COLUMN_NAMES = {"Event Name", "Type",
      "Event Description"};
  /** Select station icon button width. */
  private static final int SELECT_STATION_ICON_BTN_WIDTH = 15;
  /** Select station icon button height. */
  private static final int SELECT_STATION_ICON_BTN_HEIGHT = 15;
  /** Select station button width. */
  private static final int SELECT_STATION_BTN_WIDTH = 50;
  /** Select station button height. */
  private static final int SELECT_STATION_BTN_HEIGHT = 20;
  /** list event panel table data. */
  private static final Object[][] TABLE_DATA = {
    {"LineDelayed", "Line", "configure a delay between 2 stations of a line"},
    {"StationClosed", "Station", "close a station"},
    {"LineClosed", "Line", "close an entire line of the map"},
    {"AttendancePeak", "Station", "configure a big raise of population on a"
        + " defined station"},
    {"TrainHour", "Line", "configure a new train flow on a line"}};
  /** Singleton instance. */
  private static ListEventPanel instance;
  /** view panel. */
  private final JPanel view;
  /** event config panel. */
  private final JScrollPane eventConfig;

  // event attribute
  /** date picker start. */
  private JDatePickerImpl datePickerStart;
  /** date picker end. */
  private JDatePickerImpl datePickerEnd;
  /** clock panel start. */
  private ClockPanel clockPanelStart;
  /** clock panel end. */
  private ClockPanel clockPanelEnd;
  // eventLineDelayAttribute
  /** clock panel delay. */
  private ClockPanel clockPanelDelay;
  /** edit line selected text field. */
  private JTextField editLineSelected;
  /** edit station start text field. */
  private JTextField editStationStart;
  /** edit station end text field. */
  private JTextField editStationEnd;

  //eventAttendancePeakAttribute
  /** edit station concerned text field. */
  private JTextField editStationConcerned;
  /** edit peak number text field. */
  private JTextField editPeakNumber;
  /** edit station closed concerned text field. */

  //eventStationClosedAttribute
  private JTextField editStationClosedConcerned;
  /** edit train number text field. */
  //eventTrainHour
  private JTextField editTrainNumber;


  /**
   * ListEventPanel constructor.
   *
   * @param width  panel width
   * @param height panel height
   */
  private ListEventPanel(final int width, final int height) {
    Data.getInstance().setObserver(stationType -> this.update(
        (String) stationType));
    view = new JPanel(new GridBagLayout());
    eventConfig = new JScrollPane(view);
    eventConfig.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH,
        SCROLL_PANE_HEIGHT));
    Dimension dim = new Dimension(width, height);
    this.setPreferredSize(dim);
    this.setLayout(new GridLayout(0, 2));
    this.initComponent();
    editStationConcerned = new JTextField();
    editLineSelected = new JTextField();
    editPeakNumber = new JTextField();
    editStationEnd = new JTextField();
    editStationStart = new JTextField();
    editTrainNumber = new JTextField();
    editStationClosedConcerned = new JTextField();

  }

  /**
   * ListEventPanel singleton instantiation.
   *
   * @return ListEventPanel instance
   */
  public static ListEventPanel getInstance() {
    if (instance == null) {
      instance = new ListEventPanel(MainPanel.PANEL_WIDTH_DEFAULT,
          MainPanel.PANEL_HEIGHT_DEFAULT);
    }
    return instance;
  }

  /**
   * init available table elements.
   */
  public void initComponent() {
    JXTable table = new JXTable(TABLE_DATA, COLUMN_NAMES);
    table.setDefaultEditor(Object.class, null);
    table.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
    table.getColumn("Type").setMaxWidth(TABLE_TYPE_COLUMN_MAX_WIDTH);
    table.getColumn("Event Description").setMinWidth(
        TABLE_EVENT_DESCRIPTION_COLUMN_MIN_WIDTH);
    table.getColumn(2).setCellRenderer(new PathCellRenderer());
    JScrollPane js = new JScrollPane(table);
    js.setPreferredSize(table.getPreferredSize());
    this.add(js);
    this.add(eventConfig);
    GridBagConstraints c = new GridBagConstraints();
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(final MouseEvent mouseEvent) {
        JTable table = (JTable) mouseEvent.getSource();
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
          String eventSelected = (String) table.getValueAt(
              table.getSelectedRow(), 0);
          JButton confirmEventBtn = new JButton("Done");
          view.removeAll();
          switch (eventSelected) {
            case "LineDelayed":
              initLineDelayed(c);
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = GRID_WIDTH;
              c.gridx = GRID_X_POSITION;
              c.gridy = GRID_Y_POSITION;
              c.weighty = GRID_WEIGHT;
              confirmEventBtn.addActionListener(e ->
                  ActionMetroEvent.getInstance().addLineDelay());
              view.add(confirmEventBtn, c);
              break;
            case "LineClosed":
              initLineClosed(c);
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = GRID_WIDTH;
              c.gridx = GRID_X_POSITION;
              c.gridy = GRID_Y_POSITION;
              c.weighty = GRID_WEIGHT;
              confirmEventBtn.addActionListener(e ->
                  ActionMetroEvent.getInstance().addLineClosed());
              view.add(confirmEventBtn, c);
              break;
            case "AttendancePeak":
              initAttendancePeak(c);
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = GRID_WIDTH;
              c.gridx = GRID_X_POSITION;
              c.gridy = GRID_Y_POSITION;
              c.weighty = GRID_WIDTH;
              confirmEventBtn.addActionListener(e ->
                  ActionMetroEvent.getInstance().addAttendancePeak());
              view.add(confirmEventBtn, c);
              break;
            case "TrainHour":
              initTrainHour(c);
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = GRID_WIDTH;
              c.gridx = GRID_X_POSITION;
              c.gridy = GRID_Y_POSITION;
              c.weighty = GRID_WEIGHT;
              confirmEventBtn.addActionListener(e ->
                  ActionMetroEvent.getInstance().addTrainHour());
              view.add(confirmEventBtn, c);
              break;
            case "StationClosed":
              initStationClosed(c);
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = GRID_WIDTH;
              c.gridx = GRID_X_POSITION;
              c.gridy = GRID_Y_POSITION;
              c.weighty = GRID_WEIGHT;
              confirmEventBtn.addActionListener(e ->
                  ActionMetroEvent.getInstance().addStationClosed());
              view.add(confirmEventBtn, c);
              break;
            default:
              break;
          }
          confirmEventBtn.setVisible(true);
        }
      }
    });
  }

  /**
   * Edition fields for event line delayed.
   *
   * @param c grid bag constraints
   */
  private void initLineDelayed(final GridBagConstraints c) {
    JLabel timeStart = new JLabel(START_TIME);
    JLabel timeEnd = new JLabel(END_TIME);
    Properties p = new Properties();
    p.put(PROPERTIES_TEXT_TODAY, PROPERTIES_TEXT_TODAY_VALUE);
    p.put(PROPERTIES_TEXT_MONTH, PROPERTIES_TEXT_MONTH_VALUE);
    p.put(PROPERTIES_TEXT_YEAR, PROPERTIES_TEXT_YEAR_VALUE);
    JPanel viewDateStart = new JPanel();
    JPanel viewDateEnd = new JPanel();
    viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
    viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

    UtilDateModel model = new UtilDateModel();
    UtilDateModel model2 = new UtilDateModel();
    Properties p2 = new Properties();
    JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
    JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);
    datePickerStart = new JDatePickerImpl(datePanelStart,
        new DateLabelFormatter());
    datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
    viewDateStart.add(datePickerStart, BorderLayout.CENTER);
    viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
    clockPanelStart = new ClockPanel();
    clockPanelEnd = new ClockPanel();
    clockPanelDelay = new ClockPanel();
    JLabel stationStart = new JLabel("id StationStart: ");
    editStationStart = new JTextField();
    try {
      BufferedImage btnImg = ImageIO.read(Objects.requireNonNull(getClass()
          .getResource(SELECTION_PNG_PATH)));
      Image scaled = btnImg.getScaledInstance(SELECT_STATION_ICON_BTN_WIDTH,
          SELECT_STATION_ICON_BTN_HEIGHT, java.awt.Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(scaled);
      JButton stationStartPicker = new JButton(icon);
      stationStartPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().setVisible(false);
        Data.getInstance().setSelectType(Data.STATION_START);
      });
      stationStartPicker.setPreferredSize(new Dimension(
          SELECT_STATION_BTN_WIDTH, SELECT_STATION_BTN_HEIGHT));
      editStationEnd = new JTextField();
      JXButton stationEndPicker = new JXButton(icon);
      stationEndPicker.setPreferredSize(new Dimension(SELECT_STATION_BTN_WIDTH,
          SELECT_STATION_BTN_HEIGHT));
      stationEndPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().setVisible(false);
        Data.getInstance().setSelectType(Data.STATION_END);
      });

      c.anchor = GridBagConstraints.NORTHWEST;
      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 0;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeStart, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 1;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(viewDateStart, c);
      c.gridx = 1;
      c.gridy = 1;
      c.weighty = 0.1;
      view.add(clockPanelStart, c);

      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 2;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeEnd, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 3;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.4;
      view.add(viewDateEnd, c);
      c.gridx = 1;
      c.gridy = 3;
      c.weighty = 0.4;
      view.add(clockPanelEnd, c);

      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 4;
      c.weighty = 0.1;
      c.insets = new Insets(15, 0, 0, 0);
      view.add(stationStart, c);
      c.gridx = 0;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(editStationStart, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(stationStartPicker, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 6;
      c.weighty = 0.1;
      JLabel stationEnd = new JLabel("id StationEnd: ");
      view.add(stationEnd, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 7;
      c.weighty = 0.1;
      view.add(stationEndPicker, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 7;
      c.weighty = 0.1;
      view.add(editStationEnd, c);
      c.gridx = 0;
      c.gridy = 8;
      c.weighty = 0.1;
      JLabel delay = new JLabel("delay: ");
      view.add(delay, c);
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 9;
      c.weighty = 0.5;
      clockPanelDelay.getClockView().setHour(0);
      clockPanelDelay.getClockView().setMinutes(0);
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      Date date = cal.getTime();
      clockPanelDelay.getTimeSpinner().setValue(date);
      view.add(clockPanelDelay, c);

      eventConfig.revalidate();
      eventConfig.setVisible(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Edition fields for event line Closed.
   *
   * @param c grid bag constraints
   */
  private void initLineClosed(final GridBagConstraints c) {
    JLabel timeStart = new JLabel(START_TIME);
    JLabel timeEnd = new JLabel(END_TIME);



    Properties p = new Properties();
    p.put(PROPERTIES_TEXT_TODAY, PROPERTIES_TEXT_TODAY_VALUE);
    p.put(PROPERTIES_TEXT_MONTH, PROPERTIES_TEXT_MONTH_VALUE);
    p.put(PROPERTIES_TEXT_YEAR, PROPERTIES_TEXT_YEAR_VALUE);

    JPanel viewDateStart = new JPanel();
    JPanel viewDateEnd = new JPanel();
    viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
    viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

    UtilDateModel model = new UtilDateModel();
    UtilDateModel model2 = new UtilDateModel();
    JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
    Properties p2 = new Properties();
    JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);
    datePickerStart = new JDatePickerImpl(datePanelStart,
        new DateLabelFormatter());
    datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
    viewDateStart.add(datePickerStart, BorderLayout.CENTER);
    viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
    clockPanelStart = new ClockPanel();
    clockPanelEnd = new ClockPanel();

    JLabel stationStart = new JLabel("id StationStart: ");
    editStationStart = new JTextField();
    try {
      BufferedImage btnImg =
          ImageIO.read(Objects.requireNonNull(getClass()
              .getResource(SELECTION_PNG_PATH)));
      Image scaled = btnImg.getScaledInstance(SELECT_STATION_ICON_BTN_WIDTH,
          SELECT_STATION_ICON_BTN_HEIGHT, java.awt.Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(scaled);
      JButton stationStartPicker = new JButton(icon);
      stationStartPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().setVisible(false);
        Data.getInstance().setSelectType(Data.STATION_START);
      });
      stationStartPicker.setPreferredSize(new Dimension(
          SELECT_STATION_BTN_WIDTH, SELECT_STATION_BTN_HEIGHT));

      editStationEnd = new JTextField();
      JXButton stationEndPicker = new JXButton(icon);
      stationEndPicker.setPreferredSize(new Dimension(
          SELECT_STATION_BTN_WIDTH, SELECT_STATION_BTN_HEIGHT));
      stationEndPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().setVisible(false);
        Data.getInstance().setSelectType(Data.STATION_END);
      });

      c.anchor = GridBagConstraints.NORTHWEST;
      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 0;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeStart, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 1;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(viewDateStart, c);
      c.gridx = 1;
      c.gridy = 1;
      c.weighty = 0.1;
      view.add(clockPanelStart, c);

      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 2;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeEnd, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 3;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.4;
      view.add(viewDateEnd, c);
      c.gridx = 1;
      c.gridy = 3;
      c.weighty = 0.4;
      view.add(clockPanelEnd, c);

      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 4;
      c.weighty = 0.1;
      c.insets = new Insets(15, 0, 0, 0);
      view.add(stationStart, c);
      c.gridx = 0;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(editStationStart, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(stationStartPicker, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 6;
      c.weighty = 0.1;
      JLabel stationEnd = new JLabel("id StationEnd: ");
      view.add(stationEnd, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 7;
      c.weighty = 0.1;
      view.add(stationEndPicker, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 7;
      c.weighty = 0.1;
      view.add(editStationEnd, c);
      eventConfig.revalidate();
      eventConfig.setVisible(true);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Edition fields for event Attendance peak.
   *
   * @param c grid bag constraints
   */
  private void initAttendancePeak(final GridBagConstraints c) {
    JLabel timeStart = new JLabel(START_TIME);
    JLabel timeEnd = new JLabel(END_TIME);


    Properties p = new Properties();
    p.put(PROPERTIES_TEXT_TODAY, PROPERTIES_TEXT_TODAY_VALUE);
    p.put(PROPERTIES_TEXT_MONTH, PROPERTIES_TEXT_MONTH_VALUE);
    p.put(PROPERTIES_TEXT_YEAR, PROPERTIES_TEXT_YEAR_VALUE);

    JPanel viewDateStart = new JPanel();
    JPanel viewDateEnd = new JPanel();
    viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
    viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

    UtilDateModel model = new UtilDateModel();
    UtilDateModel model2 = new UtilDateModel();
    JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
    Properties p2 = new Properties();
    JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);
    datePickerStart = new JDatePickerImpl(datePanelStart,
        new DateLabelFormatter());
    datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
    viewDateStart.add(datePickerStart, BorderLayout.CENTER);
    viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
    clockPanelStart = new ClockPanel();
    clockPanelEnd = new ClockPanel();

    JLabel stationConcerned = new JLabel("Id station concerned: ");
    editStationConcerned = new JTextField();
    try {
      BufferedImage btnImg = ImageIO.read(Objects.requireNonNull(getClass()
          .getResource(SELECTION_PNG_PATH)));
      Image scaled = btnImg.getScaledInstance(SELECT_STATION_ICON_BTN_WIDTH,
          SELECT_STATION_ICON_BTN_HEIGHT, java.awt.Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(scaled);
      JButton stationConcernedPicker = new JButton(icon);
      stationConcernedPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().setVisible(false);
        Data.getInstance().setSelectType(Data.STATION_CONCERNED);
      });
      stationConcernedPicker.setPreferredSize(new Dimension(
          SELECT_STATION_BTN_WIDTH, SELECT_STATION_BTN_HEIGHT));
      editPeakNumber = new JTextField();

      c.anchor = GridBagConstraints.NORTHWEST;
      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 0;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeStart, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 1;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(viewDateStart, c);
      c.gridx = 1;
      c.gridy = 1;
      c.weighty = 0.1;
      view.add(clockPanelStart, c);

      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 2;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeEnd, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 3;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.4;
      view.add(viewDateEnd, c);
      c.gridx = 1;
      c.gridy = 3;
      c.weighty = 0.4;
      view.add(clockPanelEnd, c);

      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 4;
      c.weighty = 0.1;
      c.insets = new Insets(15, 0, 0, 0);
      view.add(stationConcerned, c);
      c.gridx = 0;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(editStationConcerned, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(stationConcernedPicker, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 6;
      c.weighty = 0.1;
      JLabel peakNumber = new JLabel("peakNumber: ");
      view.add(peakNumber, c);
      c.gridx = 0;
      c.gridy = 7;
      c.weighty = 0.2;
      view.add(editPeakNumber, c);


      eventConfig.revalidate();
      eventConfig.setVisible(true);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * edition fields for event Station Closed.
   *
   * @param c grid bag constraints
   */
  private void initStationClosed(final GridBagConstraints c) {
    JLabel timeStart = new JLabel(START_TIME);
    JLabel timeEnd = new JLabel(END_TIME);



    Properties p = new Properties();
    p.put(PROPERTIES_TEXT_TODAY, PROPERTIES_TEXT_TODAY_VALUE);
    p.put(PROPERTIES_TEXT_MONTH, PROPERTIES_TEXT_MONTH_VALUE);
    p.put(PROPERTIES_TEXT_YEAR, PROPERTIES_TEXT_YEAR_VALUE);

    JPanel viewDateStart = new JPanel();
    JPanel viewDateEnd = new JPanel();
    viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
    viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

    UtilDateModel model = new UtilDateModel();
    UtilDateModel model2 = new UtilDateModel();
    JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
    Properties p2 = new Properties();
    JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);
    datePickerStart = new JDatePickerImpl(datePanelStart,
        new DateLabelFormatter());
    datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
    viewDateStart.add(datePickerStart, BorderLayout.CENTER);
    viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
    clockPanelStart = new ClockPanel();
    clockPanelEnd = new ClockPanel();

    JLabel stationConcerned = new JLabel("id StationConcerned: ");
    editStationClosedConcerned = new JTextField();
    try {
      BufferedImage btnImg = ImageIO.read(Objects.requireNonNull(getClass()
          .getResource(SELECTION_PNG_PATH)));
      Image scaled = btnImg.getScaledInstance(SELECT_STATION_ICON_BTN_WIDTH,
          SELECT_STATION_ICON_BTN_HEIGHT, java.awt.Image.SCALE_SMOOTH);
      ImageIcon icon = new ImageIcon(scaled);
      JButton stationConcernedPicker = new JButton(icon);
      stationConcernedPicker.addActionListener(arg0 -> {
        EventWindow.getInstance().dispose();
        Data.getInstance().setSelectType(Data.STATION_CONCERNED);
      });
      stationConcernedPicker.setPreferredSize(new Dimension(
          SELECT_STATION_BTN_WIDTH, SELECT_STATION_BTN_HEIGHT));
      c.anchor = GridBagConstraints.NORTHWEST;
      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 0;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeStart, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 1;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(viewDateStart, c);
      c.gridx = 1;
      c.gridy = 1;
      c.weighty = 0.1;
      view.add(clockPanelStart, c);

      c.fill = GridBagConstraints.VERTICAL;
      c.gridwidth = 3;
      c.gridx = 0;
      c.gridy = 2;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.1;
      view.add(timeEnd, c);

      c.fill = GridBagConstraints.BOTH;
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 3;
      c.gridheight = 1;
      c.weightx = 3;
      c.weighty = 0.4;
      view.add(viewDateEnd, c);
      c.gridx = 1;
      c.gridy = 3;
      c.weighty = 0.4;
      view.add(clockPanelEnd, c);

      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 4;
      c.weighty = 0.1;
      c.insets = new Insets(15, 0, 0, 0);
      view.add(stationConcerned, c);
      c.gridx = 0;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(editStationClosedConcerned, c);
      c.fill = GridBagConstraints.CENTER;
      c.gridx = 1;
      c.gridy = 5;
      c.weighty = 0.2;
      view.add(stationConcernedPicker, c);

      eventConfig.revalidate();
      eventConfig.setVisible(true);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Edition fields for event train hour.
   *
   * @param c grid bag constraints
   */
  private void initTrainHour(final GridBagConstraints c) {
    clockPanelStart = new ClockPanel();
    clockPanelEnd = new ClockPanel();

    editLineSelected = new JTextField();
    JButton currentLineButton = new JButton("Select Current line");
    currentLineButton.addActionListener(e -> editLineSelected.setText(
        Integer.toString(ActionLine.getInstance().getLineToUpdateIndex())));
    editTrainNumber = new JTextField();

    c.anchor = GridBagConstraints.NORTHWEST;
    c.fill = GridBagConstraints.VERTICAL;
    c.gridwidth = 1;
    c.gridx = 0;
    c.gridy = 0;
    c.gridheight = 1;
    c.weightx = 3;
    c.weighty = 0.1;
    JLabel timeStart = new JLabel(START_TIME);
    view.add(timeStart, c);
    c.gridx = 0;
    c.gridy = 1;
    c.weighty = 0.1;
    view.add(clockPanelStart, c);

    c.fill = GridBagConstraints.VERTICAL;
    c.gridwidth = 1;
    c.gridx = 1;
    c.gridy = 0;
    c.gridheight = 1;
    c.weightx = 3;
    c.weighty = 0.1;
    JLabel timeEnd = new JLabel(END_TIME);
    view.add(timeEnd, c);
    c.gridx = 1;
    c.gridy = 1;
    c.weighty = 0.4;
    view.add(clockPanelEnd, c);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 2;
    c.weighty = 0.1;
    JLabel lineSelected = new JLabel("Line to edit: ");
    view.add(lineSelected, c);
    c.gridx = 0;
    c.gridy = 3;
    c.weighty = 0.2;
    view.add(editLineSelected, c);
    c.fill = GridBagConstraints.CENTER;
    c.gridx = 1;
    c.gridy = 3;
    c.weighty = 0.2;
    view.add(currentLineButton, c);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 4;
    c.weighty = 0.2;
    JLabel trainNumber = new JLabel("trainNumber ");
    view.add(trainNumber, c);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 5;
    c.weighty = 0.2;
    view.add(editTrainNumber, c);

    eventConfig.revalidate();
    eventConfig.setVisible(true);
  }

  /**
   * format the date of the JDatePicker.
   *
   * @author arthu
   */
  public static class DateLabelFormatter extends AbstractFormatter {

    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /** The date pattern. */
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    /** The date formatter. */
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
        DATE_PATTERN);

    /** {@inheritDoc} */
    @Override
    public Object stringToValue(final String text) throws ParseException {
      return dateFormatter.parseObject(text);
    }

    /** {@inheritDoc} */
    @Override
    public String valueToString(final Object value) {
      if (value != null) {
        Calendar cal = (Calendar) value;
        return dateFormatter.format(cal.getTime());
      }

      return "";
    }

  }

  /**
   * get the info of the edition fields recap as a String for eventLineDelay.
   *
   * @return String
   */
  public String eventLineDelayToString() {
    DateFormat df = new SimpleDateFormat(FORMAT_DATE);
    DateFormat dfTime = new SimpleDateFormat(FORMAT_TIME);

    String dateStart = df.format((Date) datePickerStart.getModel().getValue());
    String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
    String timeStart = dfTime.format(clockPanelStart.getTimeSpinner()
        .getValue());
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue());
    String stationStart = editStationStart.getText();
    String stationEnd = editStationEnd.getText();
    String delay = dfTime.format(clockPanelDelay.getTimeSpinner().getValue());

    return dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
      + stationStart + "," + stationEnd + "," + delay;
  }

  /**
   * get the info of the edition fields recap as a String for eventLineClosed.
   *
   * @return String
   */
  public String eventLineClosedToString() {
    DateFormat df = new SimpleDateFormat(FORMAT_DATE);
    DateFormat dfTime = new SimpleDateFormat(FORMAT_TIME);

    String dateStart = df.format((Date) datePickerStart.getModel().getValue());
    String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
    String timeStart = dfTime.format(clockPanelStart.getTimeSpinner()
        .getValue());
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue());
    String stationStart = editStationStart.getText();
    String stationEnd = editStationEnd.getText();
    return dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
      + stationStart + "," + stationEnd;
  }

  /**
   * get the info of the edition fields recap as a String for
   * eventAttendancePeak.
   *
   * @return String
   */
  public String eventAttendancePeakToString() {
    DateFormat df = new SimpleDateFormat(FORMAT_DATE);
    DateFormat dfTime = new SimpleDateFormat(FORMAT_TIME);

    String dateStart = df.format((Date) datePickerStart.getModel().getValue());
    String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
    String timeStart = dfTime.format(clockPanelStart.getTimeSpinner()
        .getValue());
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue());
    String stationConcerned = editStationConcerned.getText();
    String peakNb = editPeakNumber.getText();

    return dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
      + stationConcerned + "," + peakNb;
  }

  /**
   * get the info of the edition fields recap as a String for
   * eventStationClosed.
   *
   * @return String
   */
  public String eventStationClosedToString() {
    DateFormat df = new SimpleDateFormat(FORMAT_DATE);
    DateFormat dfTime = new SimpleDateFormat(FORMAT_TIME);
    String dateStart = df.format((Date) datePickerStart.getModel().getValue());
    String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
    String timeStart = dfTime.format(clockPanelStart.getTimeSpinner()
        .getValue());
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue());
    String stationConcerned = editStationConcerned.getText();

    return dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + ","
      + stationConcerned;
  }

  /**
   * get the info of the edition fields recap as a String for event train hour.
   *
   * @return String
   */
  public String eventTrainHourToString() {
    DateFormat dfTime = new SimpleDateFormat(FORMAT_TIME);
    String timeStart =
        dfTime.format(clockPanelStart.getTimeSpinner().getValue());
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue());
    String lineSelected = editLineSelected.getText();
    String trainNumer = editTrainNumber.getText();

    return timeStart + "," + timeEnd + "," + lineSelected + ","
      + trainNumer;
  }

  /**
   * Updates the text fields of the event.
   *
   * @param stationType the type of the station to update
   */
  public void update(final String stationType) {
    EventWindow.getInstance().setVisible(true);
    switch (stationType) {
      case "start":
        editStationStart.setText(Integer.toString(Data.getInstance()
            .getStationStartId()));
        break;
      case "end":
        editStationEnd.setText(Integer.toString(Data.getInstance()
            .getStationEndId()));
        break;
      case "concerned":
        editStationConcerned.setText(Integer.toString(Data.getInstance()
            .getStationConcernedId()));
        editStationClosedConcerned.setText(Integer.toString(Data.getInstance()
            .getStationConcernedId()));
        break;
      default:
        break;
    }
    view.repaint();
  }

  /**
   * display available events tab.
   *
   * @author arthu
   */
  static class PathCellRenderer extends DefaultTableCellRenderer {
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(final JTable table,
                                                   final Object value,
                                                   final boolean isSelected,
                                                   final boolean hasFocus,
                                                   final int row,
                                                   final int column) {
      JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, column);
      String pathValue = value.toString(); // Could be value.toString()
      cellComponent.setToolTipText(pathValue);
      return cellComponent;
    }
  }

}
