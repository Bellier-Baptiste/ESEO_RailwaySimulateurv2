package view;

import data.Data;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;

/**panel which contains a short description of created events.
 * @author arthu
 *
 */
public class EventRecap extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constants
	public static final int LARGEUR_PAR_DEFAUT = 200;
	public static final int HAUTEUR_PAR_DEFAUT = 750;
	public static final Color BACKGROUND_COLOR = new Color(54, 61, 194);
	public static final Color BACKGROUND_LABEL_COLOR = new Color(30, 62, 191);

	/**
	 * constructor
	 */
	public EventRecap() {
		this.setPreferredSize(new Dimension(LARGEUR_PAR_DEFAUT, HAUTEUR_PAR_DEFAUT));
		this.setBackground(BACKGROUND_COLOR);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

	}

//	/** this painter draws a gradient fill */
//	public Painter getPainter() {
//		int width = 100;
//		int height = 100;
//		Color color1 = Color.WHITE;
//		Color color2 = Color.GRAY;
//		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f, width, height,
//				new float[] { 0.0f, 1.0f }, new Color[] { color1, color2 });
//		MattePainter mattePainter = new MattePainter(gradientPaint);
//		return mattePainter;
//	}

	private void changeUIdefaults() {
		// JXTaskPaneContainer settings (developer defaults)
		/*
		 * These are all the properties that can be set (may change with new version of
		 * SwingX) "TaskPaneContainer.useGradient", "TaskPaneContainer.background",
		 * "TaskPaneContainer.backgroundGradientStart",
		 * "TaskPaneContainer.backgroundGradientEnd", etc.
		 */
		// setting taskpanecontainer defaults
		UIManager.put("TaskPaneContainer.useGradient", Boolean.FALSE);
		UIManager.put("TaskPaneContainer.background", BACKGROUND_COLOR);
		// setting taskpane defaults
		UIManager.put("TaskPane.font", new FontUIResource(new Font("Verdana", Font.BOLD, 16)));
		UIManager.put("TaskPane.titleBackgroundGradientStart", Color.WHITE);
	}

	/**Create a recap for event line delay.
	 * @param id event id
	 * @param startDateStr event startDate
	 * @param endDateStr event endDate
	 * @param LocationsStr stations concerned
	 * @param delayStr event delay
	 * @param lineStr line concerned
	 */
	public void createEventLineDelayed(int id,String startDateStr, String endDateStr, String LocationsStr, String delayStr,
			String lineStr) {
		changeUIdefaults();
		JXTaskPane taskpane = new JXTaskPane();
		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();

		// create a taskpane, and set it's title and icon
		taskpane.setTitle("Line Delayed");

		JXLabel startDate = new JXLabel();
		startDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		startDate.setText("Start date: " + startDateStr);
		startDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel EndDate = new JXLabel();
		EndDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		EndDate.setText("End date: " + endDateStr);
		EndDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel locations = new JXLabel();
		locations.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		locations.setText(LocationsStr);
		locations.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel line = new JXLabel();
		line.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		line.setText("Line: " + lineStr);
		line.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel delay = new JXLabel();
		delay.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		delay.setText("Delay: " + delayStr);
		delay.setHorizontalAlignment(JXLabel.LEFT);

		// add various actions and components to the taskpane

		taskpane.add(startDate);
		taskpane.add(EndDate);
		taskpane.add(locations);
		taskpane.add(line);
		taskpane.add(delay);
		taskpane.add(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				putValue(Action.NAME, "remove");
			}

			public void actionPerformed(ActionEvent e) {
				taskpanecontainer.remove(taskpane);
				Data.getInstance().getEventList().remove(id);
			}
		});

		// add the task pane to the taskpanecontainer
		taskpanecontainer.add(taskpane);
		this.add(taskpanecontainer, BorderLayout.CENTER);
	}

	/**create a recap for event line closed.
	 * @param id event id
	 * @param startDateStr event start date
	 * @param endDateStr event end date
	 * @param LocationsStr stations concerned
	 * @param lineStr line concerned
	 */
	public void createEventLineClosed(int id,String startDateStr, String endDateStr, String LocationsStr,
			String lineStr) {
		changeUIdefaults();
		JXTaskPane taskpane = new JXTaskPane();
		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();

		// create a taskpane, and set it's title and icon
		taskpane.setTitle("Line Closed");

		JXLabel startDate = new JXLabel();
		startDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		startDate.setText("Start date: " + startDateStr);
		startDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel EndDate = new JXLabel();
		EndDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		EndDate.setText("End date: " + endDateStr);
		EndDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel locations = new JXLabel();
		locations.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		locations.setText(LocationsStr);
		locations.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel line = new JXLabel();
		line.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		line.setText("Line: " + lineStr);
		line.setHorizontalAlignment(JXLabel.LEFT);
		

		// add various actions and components to the taskpane

		taskpane.add(startDate);
		taskpane.add(EndDate);
		taskpane.add(locations);
		taskpane.add(line);
		taskpane.add(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				putValue(Action.NAME, "remove");
			}

			public void actionPerformed(ActionEvent e) {
				taskpanecontainer.remove(taskpane);
				Data.getInstance().getEventList().remove(id);

			}
		});

		// add the task pane to the taskpanecontainer
		taskpanecontainer.add(taskpane);
		this.add(taskpanecontainer, BorderLayout.CENTER);
	}

	/** create a recap for event attendancePeak.
		 * @param id event id
	 * @param startDateStr event start date
	 * @param endDateStr event end date
	 * @param stationStr station concerned
	 * @param peakStr peak amount
	 */
	public void createEventAttendancePeak(int id, String startDateStr, String endDateStr, String stationStr, String peakStr) {
		changeUIdefaults();
		JXTaskPane taskpane = new JXTaskPane();
		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();

		// create a taskpane, and set it's title and icon
		taskpane.setTitle("Attendance Peak");

		JXLabel startDate = new JXLabel();
		startDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		startDate.setText("Start date: " + startDateStr);
		startDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel EndDate = new JXLabel();
		EndDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		EndDate.setText("End date: " + endDateStr);
		EndDate.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel station = new JXLabel();
		station.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		station.setText("Station: " + stationStr);
		station.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel peak = new JXLabel();
		peak.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		peak.setText("Peak amount: " + peakStr);
		peak.setHorizontalAlignment(JXLabel.LEFT);

		// add various actions and components to the taskpane

		taskpane.add(startDate);
		taskpane.add(EndDate);
		taskpane.add(station);
		taskpane.add(peak);
		taskpane.add(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				putValue(Action.NAME, "remove");
			}

			public void actionPerformed(ActionEvent e) {
				taskpanecontainer.remove(taskpane);
				Data.getInstance().getEventList().remove(id);
			}
		});

		// add the task pane to the taskpanecontainer
		taskpanecontainer.add(taskpane);
		this.add(taskpanecontainer, BorderLayout.CENTER);
	}
	
	/** create a recap for event stationClosed.
	 * @param id event id
 * @param startDateStr event start date
 * @param endDateStr event end date
 * @param stationStr station concerned
// * @param peakStr peak amount
 */
public void createEventStationClosed(int id, String startDateStr, String endDateStr, String stationStr) {
	changeUIdefaults();
	JXTaskPane taskpane = new JXTaskPane();
	// create a taskpanecontainer
	JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();

	// create a taskpane, and set it's title and icon
	taskpane.setTitle("Station Closed");

	JXLabel startDate = new JXLabel();
	startDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
	startDate.setText("Start date: " + startDateStr);
	startDate.setHorizontalAlignment(JXLabel.LEFT);
	JXLabel EndDate = new JXLabel();
	EndDate.setFont(new Font("Segoe UI", Font.ITALIC, 12));
	EndDate.setText("End date: " + endDateStr);
	EndDate.setHorizontalAlignment(JXLabel.LEFT);
	JXLabel station = new JXLabel();
	station.setFont(new Font("Segoe UI", Font.ITALIC, 11));
	station.setText("Station: " + stationStr);
	station.setHorizontalAlignment(JXLabel.LEFT);
	
	// add various actions and components to the taskpane

	taskpane.add(startDate);
	taskpane.add(EndDate);
	taskpane.add(station);
	taskpane.add(new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.NAME, "remove");
		}

		public void actionPerformed(ActionEvent e) {
			taskpanecontainer.remove(taskpane);
			Data.getInstance().getEventList().remove(id);
		}
	});

	// add the task pane to the taskpanecontainer
	taskpanecontainer.add(taskpane);
	this.add(taskpanecontainer, BorderLayout.CENTER);
}

	/** create a recap for event hour.
	 * @param id event id
	 * @param startTimeStr event startDate
	 * @param endTimeStr event endDate
	 * @param lineStr line concerned
	 * @param trainNbStr trainNumber amount
	 */
	public void createEventHour(int id,String startTimeStr, String endTimeStr, String lineStr, String trainNbStr) {
		changeUIdefaults();
		JXTaskPane taskpane = new JXTaskPane();
		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();

		// create a taskpane, and set it's title and icon
		taskpane.setTitle("Train Hour");

		JXLabel startTime = new JXLabel();
		startTime.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		startTime.setText("Start Time: " + startTimeStr);
		startTime.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel endTime = new JXLabel();
		endTime.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		endTime.setText("End Time: " + endTimeStr);
		endTime.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel line = new JXLabel();
		line.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		line.setText("Line: " + lineStr);
		line.setHorizontalAlignment(JXLabel.LEFT);
		JXLabel trainNb = new JXLabel();
		trainNb.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		trainNb.setText("Train amount: " + trainNbStr);
		trainNb.setHorizontalAlignment(JXLabel.LEFT);
		System.out.println(startTimeStr + " " + endTimeStr + " " + lineStr + " " + trainNbStr);

		// add various actions and components to the taskpane

		taskpane.add(startTime);
		taskpane.add(endTime);
		taskpane.add(line);
		taskpane.add(trainNb);
		taskpane.add(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				putValue(Action.NAME, "remove");
			}

			public void actionPerformed(ActionEvent e) {
				taskpanecontainer.remove(taskpane);
				Data.getInstance().getEventList().remove(id);
			}
		});

		// add the task pane to the taskpanecontainer
		taskpanecontainer.add(taskpane);
		this.add(taskpanecontainer, BorderLayout.CENTER);
	}
}
