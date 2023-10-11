package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

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


import controller.ActionLine;
import controller.ActionMetroEvent;
import controller.EventName;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

import data.Data;

/**
 * Panel of all the event edition elements.
 * @author arthu
 *
 */
public class ListEventPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constants
	public static final int PANEL_WIDTH_DEFAULT = 750;
	public static final int PANEL_HEIGHT_DEFAULT = 750;
	public static final Color BACKGROUND_COLOR_DEFAULT = Color.WHITE;
	private static final String[] COLUNMN_NAMES = { "Event Name", "Type", "Event Description" };

	private static final Object[][] TABLE_DATA = {
			{ "LineDelayed", "Line", "configure a delay between 2 stations of a line" },
			{ "StationClosed", "Station", "close a station" },
			{ "LineClosed", "Line", "close an entire line of the map" },
			{ "AttendancePeak", "Station", "configure a big raise of population on a defined station" },
			{ "TrainHour", "Line", "configure a new train flow on a line" } };
  private static ListEventPanel instance;

	private JPanel view;
	private JScrollPane eventConfig;


	// event attribute
	private JDatePickerImpl datePickerStart;
	private JDatePickerImpl datePickerEnd;
	private ClockPanel clockPanelStart;
	private ClockPanel clockPanelEnd;
	// eventLineDelayAttribute
	private ClockPanel clockPanelDelay;
	private EventName eventName;
	private JTextField editLineSelected;
	private JTextField editStationStart;
	private JTextField editStationEnd;
	
	//eventAttendancePeakAttribute
	private JTextField editStationConcerned;
	private JTextField editPeakNumber;
	
	//eventStationClosedAttribute
	private JTextField editStationClosedConcerned;

	//eventTrainHour
	private JTextField editTrainNumber;


	/**
	 * Constructor
	 * 
	 * @param width panel width
	 * @param height panel heigth
//	 * @param actionManager ActionManager
	 */
	private ListEventPanel(int width, int height) {
		Data.getInstance().addObserver(this);
		Dimension dim = new Dimension(width, height);
		view = new JPanel(new GridBagLayout());
		//view.setBackground(Color.WHITE);
		eventConfig = new JScrollPane(view);
		eventConfig.setPreferredSize(new Dimension(500, 500));
		this.setPreferredSize(dim);
//		this.setBackground(color);
		this.setLayout(new GridLayout(0, 2));
		this.initComponent();
		editStationConcerned = new JTextField();
		editLineSelected = new JTextField();
		editPeakNumber = new JTextField();
		editStationEnd = new JTextField();
		editStationStart = new JTextField();
		editTrainNumber  = new JTextField();
		editStationClosedConcerned = new JTextField();

	}

  public static ListEventPanel getInstance() {
    if (instance == null) {
      instance = new ListEventPanel(MainPanel.PANEL_WIDTH_DEFAULT,MainPanel.PANEL_HEIGHT_DEFAULT);
    }
    return instance;
  }

  /**
	 * init available table elements.
	 */
	public void initComponent() {
		GridBagConstraints c = new GridBagConstraints();
		JXTable table = new JXTable(TABLE_DATA, COLUNMN_NAMES);
		table.setDefaultEditor(Object.class, null);
		table.setPreferredSize(new Dimension(600, 100));
		table.getColumn("Type").setMaxWidth(50);
		table.getColumn("Event Description").setMinWidth(200);
		table.getColumn(2).setCellRenderer(new PathCellRenderer());
		JScrollPane js = new JScrollPane(table);

		//js.getViewport().setBackground(Color.WHITE);
		js.setPreferredSize(table.getPreferredSize());
		this.add(js);
		this.add(eventConfig);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					String eventSelected = (String) table.getValueAt(table.getSelectedRow(), 0);
          JButton confirmEventBtn = new JButton("Done");
          switch (eventSelected) {
            case "LineDelayed":
              initLineDelayed(c);
              eventName = EventName.LINE_DELAYED;
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = 2;
              c.gridx = 0;
              c.gridy = 10;
              c.weighty = 0.1;
              confirmEventBtn.addActionListener(e -> ActionMetroEvent.getInstance().addLineDelay());
              view.add(confirmEventBtn, c);
              break;
            case "LineClosed":
              initLineClosed(c);
              eventName = EventName.LINE_CLOSED;
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = 2;
              c.gridx = 0;
              c.gridy = 10;
              c.weighty = 0.1;
              confirmEventBtn.addActionListener(e -> ActionMetroEvent.getInstance().addLineClosed());
              view.add(confirmEventBtn, c);
              break;
            case "AttendancePeak":
              initAttendancePeak(c);
              eventName = EventName.ATTENDANCE_PEAK;
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = 2;
              c.gridx = 0;
              c.gridy = 10;
              c.weighty = 0.1;
              confirmEventBtn.addActionListener(e -> ActionMetroEvent.getInstance().addAttendancePeak());
              view.add(confirmEventBtn, c);
              break;
            case "TrainHour":
              initTrainHour(c);
              eventName = EventName.TRAIN_HOUR;
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = 2;
              c.gridx = 0;
              c.gridy = 10;
              c.weighty = 0.1;
              confirmEventBtn.addActionListener(e -> ActionMetroEvent.getInstance().addTrainHour());
              view.add(confirmEventBtn, c);
              break;
            case "StationClosed":
              initStationClosed(c);
              eventName = EventName.STATION_CLOSED;
              c.fill = GridBagConstraints.HORIZONTAL;
              c.gridwidth = 2;
              c.gridx = 0;
              c.gridy = 10;
              c.weighty = 0.1;
              confirmEventBtn.addActionListener(e -> ActionMetroEvent.getInstance().addStationClosed());
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
	 * edition fields for event line delayed.
	 * @param c
	 */
	private void initLineDelayed(GridBagConstraints c) {
		view.removeAll();
		JLabel timeStart = new JLabel("Start Time: ");
		JLabel timeEnd = new JLabel("End Time: ");

		UtilDateModel model = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties p = new Properties();
		Properties p2 = new Properties();

		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
		JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);

		JPanel viewDateStart = new JPanel();
		JPanel viewDateEnd = new JPanel();
		//viewDateStart.setBackground(Color.WHITE);
		//viewDateEnd.setBackground(Color.WHITE);
		viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
		viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

		datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
		datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
		viewDateStart.add(datePickerStart, BorderLayout.CENTER);
		viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
		clockPanelStart = new ClockPanel();
		clockPanelEnd = new ClockPanel();
		clockPanelDelay = new ClockPanel();

		JLabel StationStart = new JLabel("id StationStart: ");
		editStationStart = new JTextField();
		try {
			BufferedImage btnImg = ImageIO.read(getClass().getResource("/resources/selection.png"));
			Image scaled = btnImg.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaled);
			JButton stationStartPicker = new JButton(icon);
			stationStartPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().setVisible(false);
					Data.getInstance().setSelectType(Data.STATION_START);
				}

			});
			stationStartPicker.setPreferredSize(new Dimension(50, 20));

			JLabel StationEnd = new JLabel("id StationEnd: ");
			editStationEnd = new JTextField();
			JXButton stationEndPicker = new JXButton(icon);
			stationEndPicker.setPreferredSize(new Dimension(50, 20));
			stationEndPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().setVisible(false);
					Data.getInstance().setSelectType(Data.STATION_END);
				}

			});
			JLabel delay = new JLabel("delay: ");

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
			view.add(StationStart, c);
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
			view.add(StationEnd, c);
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
			view.add(delay, c);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 9;
			c.weighty = 0.5;
			clockPanelDelay.getClockView().setHour(0);
			clockPanelDelay.getClockView().setMinutes(0);
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			date = cal.getTime();
			clockPanelDelay.getTimeSpinner().setValue(date);
			view.add(clockPanelDelay, c);

			eventConfig.revalidate();
			eventConfig.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * edition fields for event line Closed.
	 * @param c
	 */
	private void initLineClosed(GridBagConstraints c) {
		view.removeAll();
		JLabel timeStart = new JLabel("Start Time: ");
		JLabel timeEnd = new JLabel("End Time: ");

		UtilDateModel model = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties p = new Properties();
		Properties p2 = new Properties();

		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
		JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);

		JPanel viewDateStart = new JPanel();
		JPanel viewDateEnd = new JPanel();
		//viewDateStart.setBackground(Color.WHITE);
		//viewDateEnd.setBackground(Color.WHITE);
		viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
		viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

		datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
		datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
		viewDateStart.add(datePickerStart, BorderLayout.CENTER);
		viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
		clockPanelStart = new ClockPanel();
		clockPanelEnd = new ClockPanel();

		JLabel StationStart = new JLabel("id StationStart: ");
		editStationStart = new JTextField();
		try {
			BufferedImage btnImg = ImageIO.read(getClass().getResource("/resources/selection.png"));
			Image scaled = btnImg.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaled);
			JButton stationStartPicker = new JButton(icon);
			stationStartPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().setVisible(false);
					Data.getInstance().setSelectType(Data.STATION_START);
				}

			});
			stationStartPicker.setPreferredSize(new Dimension(50, 20));

			JLabel StationEnd = new JLabel("id StationEnd: ");
			editStationEnd = new JTextField();
			JXButton stationEndPicker = new JXButton(icon);
			stationEndPicker.setPreferredSize(new Dimension(50, 20));
			stationEndPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().setVisible(false);
					Data.getInstance().setSelectType(Data.STATION_END);
				}

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
			view.add(StationStart, c);
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
			view.add(StationEnd, c);
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
	 * edition fields for event Attendance peak.
	 * @param c
	 */
	private void initAttendancePeak(GridBagConstraints c) {
		view.removeAll();
		JLabel timeStart = new JLabel("Start Time: ");
		JLabel timeEnd = new JLabel("End Time: ");

		UtilDateModel model = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties p = new Properties();
		Properties p2 = new Properties();

		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
		JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);

		JPanel viewDateStart = new JPanel();
		JPanel viewDateEnd = new JPanel();
		//viewDateStart.setBackground(Color.WHITE);
		//viewDateEnd.setBackground(Color.WHITE);
		viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
		viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

		datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
		datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
		viewDateStart.add(datePickerStart, BorderLayout.CENTER);
		viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
		clockPanelStart = new ClockPanel();
		clockPanelEnd = new ClockPanel();

		JLabel StationConcerned = new JLabel("id StationConcerned: ");
		editStationConcerned = new JTextField();
		try {
			BufferedImage btnImg = ImageIO.read(getClass().getResource("/resources/selection.png"));
			Image scaled = btnImg.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaled);
			JButton stationConcernedPicker = new JButton(icon);
			stationConcernedPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().setVisible(false);
					Data.getInstance().setSelectType(Data.STATION_CONCERNED);
				}

			});
			stationConcernedPicker.setPreferredSize(new Dimension(50, 20));

		
			JLabel peakNumber = new JLabel("peakNumber: ");
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
			view.add(StationConcerned, c);
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
			view.add(peakNumber,c);
			c.gridx = 0;
			c.gridy = 7;
			c.weighty = 0.2;
			view.add(editPeakNumber,c);
			

			eventConfig.revalidate();
			eventConfig.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * edition fields for event Station Closed.
	 * @param c
	 */
	private void initStationClosed(GridBagConstraints c) {
		view.removeAll();
		JLabel timeStart = new JLabel("Start Time: ");
		JLabel timeEnd = new JLabel("End Time: ");

		UtilDateModel model = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();

		Properties p = new Properties();
		Properties p2 = new Properties();

		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanelStart = new JDatePanelImpl(model, p);
		JDatePanelImpl datePanelEnd = new JDatePanelImpl(model2, p2);

		JPanel viewDateStart = new JPanel();
		JPanel viewDateEnd = new JPanel();
		//viewDateStart.setBackground(Color.WHITE);
		//viewDateEnd.setBackground(Color.WHITE);
		viewDateStart.setBorder(new BevelBorder(BevelBorder.RAISED));
		viewDateEnd.setBorder(new BevelBorder(BevelBorder.RAISED));

		datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
		datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
		viewDateStart.add(datePickerStart, BorderLayout.CENTER);
		viewDateEnd.add(datePickerEnd, BorderLayout.CENTER);
		clockPanelStart = new ClockPanel();
		clockPanelEnd = new ClockPanel();

		JLabel StationConcerned = new JLabel("id StationConcerned: ");
		editStationClosedConcerned = new JTextField();
		try {
			BufferedImage btnImg = ImageIO.read(getClass().getResource("/resources/selection.png"));
			Image scaled = btnImg.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaled);
			JButton stationConcernedPicker = new JButton(icon);
			stationConcernedPicker.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
          EventWindow.getInstance().hide();
					Data.getInstance().setSelectType(Data.STATION_CONCERNED);
				}

			});
			stationConcernedPicker.setPreferredSize(new Dimension(50, 20));

		
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
			view.add(StationConcerned, c);
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
	 * edition fields for event train hour.
	 * @param c
	 */
	private void initTrainHour(GridBagConstraints c) {
		view.removeAll();
		JLabel timeStart = new JLabel("Start Time: ");
		JLabel timeEnd = new JLabel("End Time: ");

		clockPanelStart = new ClockPanel();
		clockPanelEnd = new ClockPanel();

		JLabel lineSelected = new JLabel("Line to edit: ");
		editLineSelected = new JTextField();
		JButton currentLineButton = new JButton("Select Current line");
		currentLineButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editLineSelected.setText(Integer.toString(ActionLine.getInstance().getLineToUpdateIndex()));
			}
		});
		JLabel trainNumber = new JLabel("trainNumber ");
		editTrainNumber = new JTextField();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.weightx = 3;
		c.weighty = 0.1;
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
		view.add(timeEnd, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0.4;
		view.add(clockPanelEnd, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.1;
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
		view.add(trainNumber, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.weighty = 0.2;
		view.add(editTrainNumber, c);

		eventConfig.revalidate();
		eventConfig.setVisible(true);	}

	/**format the date of the JDatePicker.
	 * @author arthu
	 *
	 */
	public class DateLabelFormatter extends AbstractFormatter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String datePattern = "yyyy-MM-dd";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

	}

	/**get the info of the edition fields recap as a String for eventLineDelay.
	 * @return String 
	 */
	public String eventLineDelayToString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat dfTime = new SimpleDateFormat("HH:mm");

		String dateStart = df.format((Date) datePickerStart.getModel().getValue());
		String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
		String timeStart = dfTime.format(clockPanelStart.getTimeSpinner().getValue()).toString();
		String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue()).toString();
		String stationStart = editStationStart.getText();
		String stationEnd = editStationEnd.getText();
		String delay = dfTime.format(clockPanelDelay.getTimeSpinner().getValue());

		String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + "," + stationStart + ","
				+ stationEnd + "," + delay;
		return eventString;
	}
	
	/**get the info of the edition fields recap as a String for eventLineClosed.
	 * @return String 
	 */
	public String eventLineClosedToString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat dfTime = new SimpleDateFormat("HH:mm");

		String dateStart = df.format((Date) datePickerStart.getModel().getValue());
		String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
		String timeStart = dfTime.format(clockPanelStart.getTimeSpinner().getValue()).toString();
		String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue()).toString();
		String stationStart = editStationStart.getText();
		String stationEnd = editStationEnd.getText();
		String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + "," + stationStart + ","
				+ stationEnd;
		return eventString;
	}
	/**get the info of the edition fields recap as a String for eventAttendancePeak.
	 * @return String 
	 */
	public String eventAttendancePeakToString() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat dfTime = new SimpleDateFormat("HH:mm");

		String dateStart = df.format((Date) datePickerStart.getModel().getValue());
		String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
		String timeStart = dfTime.format(clockPanelStart.getTimeSpinner().getValue()).toString();
		String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue()).toString();
		String stationConcerned = editStationConcerned.getText();
		String peakNb = editPeakNumber.getText();

		String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + "," + stationConcerned + ","
				+ peakNb;
		return eventString;
	}
	
	/**get the info of the edition fields recap as a String for eventStationClosed.
	 * @return String 
	 */
	public String eventStationClosedToString() {
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat dfTime = new SimpleDateFormat("HH:mm");
    System.out.println(datePickerStart.getModel().getValue());
    String dateStart = df.format((Date) datePickerStart.getModel().getValue());
    String dateEnd = df.format((Date) datePickerStart.getModel().getValue());
    String timeStart = dfTime.format(clockPanelStart.getTimeSpinner().getValue()).toString();
    String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue()).toString();
    String stationConcerned = editStationConcerned.getText();

    String eventString = dateStart + "," + timeStart + "," + dateEnd + "," + timeEnd + "," + stationConcerned;

    return eventString;

	}
	/**get the info of the edition fields recap as a String for event train hour.
	 * @return String 
	 */
	public String eventTrainHourToString() {
		DateFormat dfTime = new SimpleDateFormat("HH:mm");
		String timeStart = dfTime.format(clockPanelStart.getTimeSpinner().getValue()).toString();
		String timeEnd = dfTime.format(clockPanelEnd.getTimeSpinner().getValue()).toString();
		String lineSelected = editLineSelected.getText();
		String trainNumer = editTrainNumber.getText();

		String eventString = timeStart + "," +timeEnd + "," + lineSelected + ","
				+ trainNumer;
		return eventString;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
    EventWindow.getInstance().setVisible(true);
		if (arg.equals("start")) {
			editStationStart.setText(Integer.toString(Data.getInstance().getStationStartId()));
		} else if (arg.equals("end")) {
			editStationEnd.setText(Integer.toString(Data.getInstance().getStationEndId()));
		}else if (arg.equals("concerned")){
			System.out.println(editStationConcerned);
			editStationConcerned.setText(Integer.toString(Data.getInstance().getStationConcernedId()));
			editStationClosedConcerned.setText(Integer.toString(Data.getInstance().getStationConcernedId()));

		}
		view.repaint();
	}

	/**
	 * display available events tab.
	 * @author arthu
	 *
	 */
	class PathCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			String pathValue = value.toString(); // Could be value.toString()
			cellComponent.setToolTipText(pathValue);
			return cellComponent;
		}
	}

}
