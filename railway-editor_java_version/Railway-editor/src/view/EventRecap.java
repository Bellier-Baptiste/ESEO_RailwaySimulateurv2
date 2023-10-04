package view;

import Model.Event;
import data.Data;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**panel which contains a short description of created events.
 * @author arthu
 *
 */
public class EventRecap extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constants
	public static final int LARGEUR_PAR_DEFAUT = 200;
	public static final int HAUTEUR_PAR_DEFAUT = 600;
  private static EventRecap instance;

  private JXTaskPaneContainer taskPaneContainer;


	/**
	 * constructor
	 */
	private EventRecap() {
		this.setPreferredSize(new Dimension(LARGEUR_PAR_DEFAUT, HAUTEUR_PAR_DEFAUT));
		TitledBorder eventRecapBorder = new TitledBorder("Events List");
		this.setBorder(eventRecapBorder);
		this.taskPaneContainer = new JXTaskPaneContainer();
    this.eventsListRemoveBackground();
	}

  public static EventRecap getInstance() {
    if (instance == null) {
      instance = new EventRecap();
    }
    return instance;
  }

  public void eventsListRemoveBackground() {
    this.taskPaneContainer.setBackgroundPainter(null);
    this.taskPaneContainer.repaint();
    this.taskPaneContainer.revalidate();
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
		JXTaskPane taskpane = new JXTaskPane();
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
				taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId() == id);
        taskPaneContainer.revalidate();
      }
		});

		// add the task pane to the taskPaneContainer
		this.taskPaneContainer.add(taskpane);
		this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();
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
		JXTaskPane taskpane = new JXTaskPane();
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
				taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId() == id);
        taskPaneContainer.revalidate();
			}
		});

		// add the task pane to the taskPaneContainer
		this.taskPaneContainer.add(taskpane);
		this.setViewportView(taskPaneContainer);
  }

	/** create a recap for event attendancePeak.
		 * @param id event id
	 * @param startDateStr event start date
	 * @param endDateStr event end date
	 * @param stationStr station concerned
	 * @param peakStr peak amount
	 */
	public void createEventAttendancePeak(int id, String startDateStr, String endDateStr, String stationStr, String peakStr) {
		JXTaskPane taskpane = new JXTaskPane();

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
				taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId() == id);
        taskPaneContainer.revalidate();
      }
		});

		// add the task pane to the taskPaneContainer
		this.taskPaneContainer.add(taskpane);
		this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();

  }
	
	/** create a recap for event stationClosed.
	 * @param id event id
 * @param startDateStr event start date
 * @param endDateStr event end date
 * @param stationStr station concerned
// * @param peakStr peak amount
 */
public void createEventStationClosed(int id, String startDateStr, String endDateStr, String stationStr) {
	JXTaskPane taskpane = new JXTaskPane();
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
      taskPaneContainer.remove(taskpane);
      Data.getInstance().getEventList().removeIf(event -> event.getId() == id);
      taskPaneContainer.revalidate();
    }
  });
	// add the task pane to the taskPaneContainer
	this.taskPaneContainer.add(taskpane);
	this.setViewportView(taskPaneContainer);
  taskPaneContainer.revalidate();
}

	/** create a recap for event hour.
	 * @param id event id
	 * @param startTimeStr event startDate
	 * @param endTimeStr event endDate
	 * @param lineStr line concerned
	 * @param trainNbStr trainNumber amount
	 */
	public void createEventHour(int id,String startTimeStr, String endTimeStr, String lineStr, String trainNbStr) {
		JXTaskPane taskpane = new JXTaskPane();
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
				taskPaneContainer.remove(taskpane);
        Data.getInstance().getEventList().removeIf(event -> event.getId() == id);
        taskPaneContainer.revalidate();
      }
		});

		// add the task pane to the taskPaneContainer
		this.taskPaneContainer.add(taskpane);
		this.setViewportView(taskPaneContainer);
    taskPaneContainer.revalidate();
  }
}
