/**
 * Class part of the view package of the application.
 */

package view;

import model.Station;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * The panel which contains the hud with the stations names, some instructions
 * and the caption.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class MainPanelHud {
  /**
   * Width of the panel.
   */
  private static final int WIDTH = 500;
  /**
   * Height of the panel.
   */
  private static final int HEIGHT = 70;
  /**
   * Size of the circle.
   */
  private static final int CIRCLE_SIZE = 25;
  /**
   * X position of the red circle for station closed.
   */
  private static final int STATION_CLOSED_X = 10;
  /**
   * Y position of the red circle for station closed.
   */
  private static final int STATION_CLOSED_Y = 10;
  /**
   * X position of the string station closed.
   */
  private static final int STATION_CLOSED_X_STRING = 40;
  /**
   * Y position of the string station closed.
   */
  private static final int STATION_CLOSED_Y_STRING = 25;
  /**
   * X position of the orange circle for station delayed.
   */
  private static final int STATION_DELAYED_X = 150;
  /**
   * Y position of the orange circle for station delayed.
   */
  private static final int STATION_DELAYED_Y = 10;
  /**
   * X position of the string station delayed.
   */
  private static final int STATION_DELAYED_X_STRING = 180;
  /**
   * Y position of the string station delayed.
   */
  private static final int STATION_DELAYED_Y_STRING = 25;
  /**
   * X position of the yellow circle for station peak.
   */
  private static final int STATION_PEAK_X = 300;
  /**
   * Y position of the yellow circle for station peak.
   */
  private static final int STATION_PEAK_Y = 10;
  /**
   * X position of the string station peak.
   */
  private static final int STATION_PEAK_X_STRING = 330;
  /**
   * Y position of the string station peak.
   */
  private static final int STATION_PEAK_Y_STRING = 25;
  /**
   * Y position of the string for the station name.
   */
  private static final int STATION_NAME_Y = 60;
  /**
   * X position of the string for the hiding tip.
   */
  private static final int HIDE_TIPS_X = 5;
  /**
   * Y position of the string for the hiding tip.
   */
  private static final int HIDE_TIPS_Y = 65;
  /**
   * X position of the string for the dragging tip.
   */
  private static final int DRAG_TIPS_X = 350;
  /**
   * Y position of the string for the dragging tip.
   */
  private static final int DRAG_TIPS_Y = 65;
  /**
   * String for the station closed.
   */
  private static final String STATION_CLOSED_STRING = ": Station Closed";
  /**
   * String for the station delayed.
   */
  private static final String STATION_DELAYED_STRING = ": Station Delayed";
  /**
   * String for the station peak.
   */
  private static final String STATION_PEAK_STRING = ": Station Peak";
  /**
   * Hud stroke width.
   */
  private static final int STROKE_WIDTH = 4;
  /**
   * Font size.
   */
  private static final int FONT_SIZE = 10;
  // attributes
  /**
   * Position X of the panel.
   */
  private final int posX;
  /**
   * Position Y of the panel.
   */
  private final int posY;
  /**
   * Station when mouse's cursor currently on it.
   */
  private Station station;

  /**
   * MainPanelHud constructor.
   *
   * @param hudPosX panel positionX
   * @param hudPosY panel positionY
   */
  public MainPanelHud(final int hudPosX, final int hudPosY) {
    this.posX = hudPosX;
    this.posY = hudPosY;
  }


  /**
   * draw 2 cicrles (one with color for the outline and another filled with
   * white to hide the polyline behind).
   *
   * @param g2D graphics component
   */
  public void show(final Graphics2D g2D) {
    g2D.setColor(Color.BLACK);
    g2D.drawRect(posX, posY, WIDTH, HEIGHT);
    g2D.setColor(Color.WHITE);
    g2D.fillRect(posX + 2, posY + 2, WIDTH - STROKE_WIDTH,
      HEIGHT - STROKE_WIDTH);

    // Station Closed description

    g2D.setColor(Color.RED);
    g2D.fillOval(posX + STATION_CLOSED_X, posY + STATION_CLOSED_Y, CIRCLE_SIZE,
      CIRCLE_SIZE);
    g2D.setColor(Color.BLACK);
    g2D.drawString(STATION_CLOSED_STRING, posX + STATION_CLOSED_X_STRING,
      posY + STATION_CLOSED_Y_STRING);

    // Station Delayed description
    g2D.setColor(Color.ORANGE);
    g2D.fillOval(posX + STATION_DELAYED_X, posY + STATION_DELAYED_Y,
      CIRCLE_SIZE, CIRCLE_SIZE);
    g2D.setColor(Color.BLACK);
    g2D.drawString(STATION_DELAYED_STRING, posX + STATION_DELAYED_X_STRING,
      posY + STATION_DELAYED_Y_STRING);

    // Station AttendancePeak description

    g2D.setColor(Color.YELLOW);
    g2D.fillOval(posX + STATION_PEAK_X, posY + STATION_PEAK_Y, CIRCLE_SIZE,
      CIRCLE_SIZE);
    g2D.setColor(Color.BLACK);
    g2D.drawString(STATION_PEAK_STRING, posX + STATION_PEAK_X_STRING,
      posY + STATION_PEAK_Y_STRING);

    if (this.station != null) {
      g2D.setColor(Color.BLACK);
      // Get the FontMetrics
      FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());
      // Determine the X coordinate for the text
      int x = posX + (WIDTH - metrics.stringWidth(this.station.getName())) / 2;
      g2D.drawString(this.station.getName(), x, posY + STATION_NAME_Y);
    }

    g2D.setFont(new Font(g2D.getFont().getName(), Font.ITALIC, FONT_SIZE));
    g2D.drawString("press H to hide HUD", posX + HIDE_TIPS_X,
      posY + HIDE_TIPS_Y);

    g2D.setFont(new Font(g2D.getFont().getName(), Font.ITALIC, FONT_SIZE));
    g2D.drawString("press right click to move the map",
      posX + DRAG_TIPS_X, posY + DRAG_TIPS_Y);
  }

  /**
   * get the station linked to the panel.
   *
   * @return Station station
   */
  public Station getStation() {
    return station;
  }

  /**
   * bind a station to the panel.
   *
   * @param currentStationHovered current station
   */
  public void setStation(final Station currentStationHovered) {
    this.station = currentStationHovered;
  }
}
