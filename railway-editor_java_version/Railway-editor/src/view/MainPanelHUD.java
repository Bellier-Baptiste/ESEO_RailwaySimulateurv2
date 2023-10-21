package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import model.Station;

/**
 * The panel which contains the hud with the stations names, some instructions and the caption.
 * @author arthu
 *
 */
public class MainPanelHUD {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 70;

	private static final int CIRCLE_SIZE = 25;

	private static final int STATION_CLOSED_X = 10;
	private static final int STATION_CLOSED_Y = 10;

	private static final int STATION_CLOSED_X_STRING = 40;
	private static final int STATION_CLOSED_Y_STRING = 25;

	private static final int STATION_DELAYED_X = 150;
	private static final int STATION_DELAYED_Y = 10;

	private static final int STATION_DELAYED_X_STRING = 180;
	private static final int STATION_DELAYED_Y_STRING = 25;

	private static final int STATION_PEAK_X = 300;
	private static final int STATION_PEAK_Y = 10;

	private static final int STATION_PEAK_X_STRING = 330;
	private static final int STATION_PEAK_Y_STRING = 25;
	
	private static final int STATION_NAME_Y = 60;
	
	private static final int HIDE_TIPS_X = 5;
	private static final int HIDE_TIPS_Y = 65;
	
	private static final int DRAG_TIPS_X = 350;
	private static final int DRAG_TIPS_Y = 65;

	private static final String STATION_CLOSED_STRING = ": Station Closed";
	private static final String STATION_DELAYED_STRING = ": Station Delayed";
	private static final String STATION_PEAK_STRING = ": Station Peak";

	private int posX;
	private int posY;
	private Station station;

	/**Constructor.
	 * @param posX panel positionX
	 * @param posY panel positionY
	 */
	public MainPanelHUD(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;

	}

	// methods
	/**
	 * draw 2 cicrles (one with color for the outline and another filled with white
	 * to hide the polyline behind)
	 * 
	 * @param g2D graphics component
	 */
	public void affiche(Graphics2D g2D) {
		g2D.setColor(Color.BLACK);
		g2D.drawRect(posX, posY, WIDTH, HEIGHT);
		g2D.setColor(Color.WHITE);
		g2D.fillRect(posX+2, posY+2, WIDTH-4, HEIGHT-4);

		// Station Closed description

		g2D.setColor(Color.RED);
		g2D.fillOval(posX + STATION_CLOSED_X , posY + STATION_CLOSED_Y , CIRCLE_SIZE ,
				CIRCLE_SIZE);
		g2D.setColor(Color.BLACK);
		g2D.drawString(STATION_CLOSED_STRING, posX + STATION_CLOSED_X_STRING, posY + STATION_CLOSED_Y_STRING);

		// Station Delayed description
		g2D.setColor(Color.ORANGE);
		g2D.fillOval(posX + STATION_DELAYED_X  , posY + STATION_DELAYED_Y , CIRCLE_SIZE ,
				CIRCLE_SIZE);
		g2D.setColor(Color.BLACK);
		g2D.drawString(STATION_DELAYED_STRING, posX + STATION_DELAYED_X_STRING, posY + STATION_DELAYED_Y_STRING);
		
		// Station AttendancePeak description

		g2D.setColor(Color.YELLOW);
		g2D.fillOval(posX + STATION_PEAK_X , posY + STATION_PEAK_Y , CIRCLE_SIZE ,
				CIRCLE_SIZE );
		g2D.setColor(Color.BLACK);
		g2D.drawString(STATION_PEAK_STRING, posX + STATION_PEAK_X_STRING, posY + STATION_PEAK_Y_STRING);
		
		if (this.station != null) {
			g2D.setColor(Color.BLACK);
			  // Get the FontMetrics
		    FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());
		    // Determine the X coordinate for the text
		    int x = posX + (WIDTH - metrics.stringWidth(this.station.getName())) / 2;
			g2D.drawString(this.station.getName(), x, posY + STATION_NAME_Y);

		}
		
		g2D.setFont(new Font(g2D.getFont().getName(), Font.ITALIC, 10));
		g2D.drawString("press H to hide HUD", posX + HIDE_TIPS_X, posY + HIDE_TIPS_Y);

		g2D.setFont(new Font(g2D.getFont().getName(), Font.ITALIC, 10));
		g2D.drawString("press right click to move the map", posX + DRAG_TIPS_X, posY + DRAG_TIPS_Y);

	}

	/**get the station linked to the panel.
	 * @return Station station
	 */
	public Station getStation() {
		return station;
	}

	/**bind a station to the panel.
	 * @param station current station
	 */
	public void setStation(Station station) {
		this.station = station;
	}
	
	
}
