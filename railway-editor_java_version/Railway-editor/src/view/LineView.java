package view;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;

import Model.Line;

/**Represent the Line view.
 * @author Arthur Lagarce
 *
 */
public class LineView {

	// attributes
	private Line line;
	private List<StationView>  stationViews;
	private int[] xPoints;
	private int[] yPoints;
	private Stroke stroke;

	/** Constructor.
	 * @param line line to bind to the view
	 * @param stationViews stationsViews of the lineView
	 */
	public LineView(Line line, List<StationView> stationViews) {
		this.line = line;
		this.stationViews = stationViews;
		this.stroke = new BasicStroke(5,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 5);

	}

	// accessors
	/**get the model line linked to this view.
	 * @return Line line
	 */
	public Line getLine() {
		return line;
	}

	/** line a model line to this view.
	 * @param line line to bind
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	
	public List<StationView> getStationViews() {
		return stationViews;
	}

	public void setStationViews(List<StationView> stationViews) {
		this.stationViews = stationViews;
	}


	// methods
	/**Display the line shape (poyline).
	 * @param g2D graphics component
	 */
	public void affiche(Graphics2D g2D) {
		//x and y point array creation
		xPoints = new int[this.stationViews.size()];
		yPoints = new int[this.stationViews.size()];
		for (int i=0;i<this.stationViews.size();i++) {
			//fill arrays with the center point of circles representing stations
			xPoints[i] = this.stationViews.get(i).getStation().getPosX();
			yPoints[i] = this.stationViews.get(i).getStation().getPosY();
		}
		
		//setting color
		g2D.setColor(this.line.getColor());
		g2D.setStroke(this.stroke);

		
		
		//draw the polyline
		if (this.stationViews.size() >1) {
		g2D.drawPolyline(xPoints, yPoints, this.stationViews.size());
		}
		//draw the station (g2D.drawCircle)
		for (StationView stationView : stationViews) {
			stationView.affiche(g2D,this.line.getColor());
		}
		


	}
	/**set line Stroke.
	 * @param stroke stroke 
	 */
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

}
