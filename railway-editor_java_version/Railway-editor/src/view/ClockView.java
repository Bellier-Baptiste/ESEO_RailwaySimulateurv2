package view;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class view of the analog clock.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class ClockView {
  private int hour;
  private int minutes;
  private int posX;
  private int posY;
  private JPanel panel;
  private static final int CLOCK_SIZE = 30;
  private static final int CLOCK_MARK_SIZE = 3;
  private static final int MINUTE_SIZE = 20;
  private static final int HOUR_SIZE = 10;

  /**
   * Constructor.
   *
   * @param posX  clock positionX
   * @param posY  clock positionY
   * @param panel clock panel
   */
  public ClockView(int posX, int posY, JPanel panel) {
    this.posX = posX;
    this.posY = posY;
    this.panel = panel;
  }

  /**
   * Create clockShape.
   *
   * @param g2D graphics component
   */
  public void display(Graphics2D g2D) {

    for (int i = 1; i <= 12; i++) {
      g2D.drawLine(posX, posY + CLOCK_SIZE - CLOCK_MARK_SIZE, posX,
          posY + CLOCK_SIZE);
      g2D.rotate(Math.PI / 6, posX, posY);
    }
    g2D.setColor(Color.BLUE);
    g2D.rotate(minutes * Math.PI / 30, posX, posY);
    g2D.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND));
    g2D.drawLine(posX, posY, posX, posY - MINUTE_SIZE);

    g2D.rotate(2 * Math.PI - minutes * Math.PI / 30, posX, posY);
    g2D.rotate(hour * Math.PI / 6, posX, posY);
    g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND));
    g2D.drawLine(posX, posY, posX, posY - HOUR_SIZE);

    g2D.setColor(Color.BLACK);
    g2D.fillOval(posX - 3, posY - 3, 6, 6);
  }

  /**
   * get the hour.
   *
   * @return int hour
   */
  public int getHour() {
    return hour;
  }

  /**
   * set the hour.
   *
   * @param hour hour on the clock
   */
  public void setHour(int hour) {
    this.hour = hour;
  }

  /**
   * get the minutes.
   *
   * @return int minutes
   */
  public int getMinutes() {
    return minutes;
  }

  /**
   * set the minutes.
   *
   * @param minutes on the clock
   */
  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  /**
   * get panel of the clock.
   *
   * @return JPanel panel
   */
  public JPanel getPanel() {
    return panel;
  }

  /**
   * set the panel of the clock.
   *
   * @param panel panel which contain the clock
   */
  public void setPanel(JPanel panel) {
    this.panel = panel;
  }

  /**
   * get Clock posX.
   *
   * @return int posX
   */
  public int getPosX() {
    return posX;
  }

  /**
   * set Clock posX.
   *
   * @param posX clock positionX
   */
  public void setPosX(int posX) {
    this.posX = posX;
  }

  /**
   * get Clock posY.
   *
   * @return int posY
   */
  public int getPosY() {
    return posY;
  }

  /**
   * set Clock posY.
   *
   * @param posY clock positionY
   */
  public void setPosY(int posY) {
    this.posY = posY;
  }


}
