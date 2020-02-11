package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class which creates a time selector and display an analog clock directly related to it.
 * @author arthu
 *
 */
public class ClockPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constants
	public static final int PANEL_WIDTH_DEFAULT = 100;
	public static final int PANEL_HEIGHT_DEFAULT = 100;
	public static final Color BACKGROUND_COLOR_DEFAULT = Color.WHITE;

	private ClockView clockView;
	private JSpinner timeSpinner;

	/**
	 * Constructor, initialize an change listener to update the clock graphics.
	 */
	public ClockPanel() {
		Dimension dim = new Dimension(PANEL_WIDTH_DEFAULT, PANEL_HEIGHT_DEFAULT);
		this.setPreferredSize(dim);
		this.setBackground(BACKGROUND_COLOR_DEFAULT);
	    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); 

		clockView = new ClockView(this.getPreferredSize().width, this.getPreferredSize().height/2+10,this);
		timeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		timeSpinner.setEditor(timeEditor);
		((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
		timeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SimpleDateFormat formater = new SimpleDateFormat("HH/mm");
				String spinnerValue = formater.format(timeSpinner.getValue());
				
				clockView.setHour(Integer.valueOf(spinnerValue.split("/")[0]));
				clockView.setMinutes(Integer.valueOf(spinnerValue.split("/")[1]));
				clockView.getPanel().repaint();

			}
		});
		timeSpinner.setValue(new Date()); // will only show the current time
		this.add(timeSpinner);

	}
	
	

	/**get the analog clockView.
	 * @return ClockView clockView
	 */
	public ClockView getClockView() {
		return clockView;
	}



	/**set the analog clockView
	 * @param clockView analog clock view
	 */
	public void setClockView(ClockView clockView) {
		this.clockView = clockView;
	}

	


	/**get the time selector.
	 * @return JSpinner timeSelector
	 */
	public JSpinner getTimeSpinner() {
		return timeSpinner;
	}



	/**set the time Selector
	 * @param timeSpinner time selector
	 */
	public void setTimeSpinner(JSpinner timeSpinner) {
		this.timeSpinner = timeSpinner;
	}



	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {// display all panel elements
		Graphics2D g2D = (Graphics2D) g.create();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paintComponent(g);
		clockView.setPosX((int)this.getWidth()/2);
		clockView.setPosY((int)this.getHeight()/2+10);
		clockView.display(g2D);

	}
}
