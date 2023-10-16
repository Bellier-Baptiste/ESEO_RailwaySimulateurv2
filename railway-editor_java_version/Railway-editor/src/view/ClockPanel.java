/** Class part of the view package of the application. */

package view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class which creates a time selector and display an analog clock directly
 * related to it.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class ClockPanel extends JPanel {
  /** ClockView width and height. */
  public static final int CLOCK_VIEW_WIDTH_HEIGHT = 10;
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  // constants
  /** Default width of the panel. */
  public static final int PANEL_WIDTH_DEFAULT = 100;
  /** Default height of the panel. */
  public static final int PANEL_HEIGHT_DEFAULT = 100;
  /** ClockView object. */
  private transient ClockView clockView;
  /** Time selector. */
  private JSpinner timeSpinner;

  /**
   * Constructor, initialize an change listener to update the clock graphics.
   */
  public ClockPanel() {
    Dimension dim = new Dimension(PANEL_WIDTH_DEFAULT, PANEL_HEIGHT_DEFAULT);
    this.setPreferredSize(dim);
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

    clockView = new ClockView(this.getPreferredSize().width,
        this.getPreferredSize().height / 2 + CLOCK_VIEW_WIDTH_HEIGHT,
        this);
    timeSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner,
        "HH:mm");
    timeSpinner.setEditor(timeEditor);
    ((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
    timeSpinner.addChangeListener(e -> {
      SimpleDateFormat formater = new SimpleDateFormat("HH/mm");
      String spinnerValue = formater.format(timeSpinner.getValue());

      clockView.setHour(Integer.parseInt(spinnerValue.split("/")[0]));
      clockView.setMinutes(Integer.parseInt(spinnerValue.split("/")[1]));
      clockView.getPanel().repaint();

    });
    timeSpinner.setValue(new Date()); // will only show the current time
    this.add(timeSpinner);

  }


  /**
   * get the analog clockView.
   *
   * @return ClockView clockView
   */
  public ClockView getClockView() {
    return clockView;
  }


  /**
   * set the analog clockView.
   *
   * @param clockViewToSet analog clock view
   */
  public void setClockView(final ClockView clockViewToSet) {
    this.clockView = clockViewToSet;
  }


  /**
   * get the time selector.
   *
   * @return JSpinner timeSelector
   */
  public JSpinner getTimeSpinner() {
    return timeSpinner;
  }


  /**
   * set the time Selector.
   *
   * @param timeSpinnerToSet time selector
   */
  public void setTimeSpinner(final JSpinner timeSpinnerToSet) {
    this.timeSpinner = timeSpinnerToSet;
  }


  /**
   * display the panel.
   *
   * @param g graphics component
   *
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paintComponent(final Graphics g) {
    Graphics2D g2D = (Graphics2D) g.create();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    super.paintComponent(g);
    clockView.setPosX(this.getWidth() / 2);
    clockView.setPosY(this.getHeight() / 2 + CLOCK_VIEW_WIDTH_HEIGHT);
    clockView.display(g2D);
  }
}
