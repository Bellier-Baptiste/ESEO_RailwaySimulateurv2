package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import controller.MovingAdapter;

/**Main Panel which cntains all the views
 * @author Arthur Lagarce
 *
 */
public class MainPanel extends JMapViewer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//constants
	public static final int PANEL_WIDTH_DEFAULT = 1100;
	public static final int PANEL_HEIGHT_DEFAULT = 750;
	public static final Color BACKGROUND_COLOR_DEFAULT = Color.WHITE;
	
	//attributes
	//private LineView lineView;
	private List<LineView> lineViews;
	//private StationView stationView;
	//private List<StationView> stationViews;
	private List<AreaView> areaViews;
//	private double zoomFactor;
//	private boolean zoomer;
	private MainPanelHUD mainPanelHUD;
	private boolean hideHud;
	
	/**Constructor
	 * @param width panel width
	 * @param height panel height
	 * @param color panel color
	 */
	public MainPanel(int width, int height, Color color) {
		MovingAdapter ma = new MovingAdapter();
		Dimension dim = new Dimension(width, height);
		this.setPreferredSize(dim);
		this.setBackground(color);
		this.lineViews = new ArrayList<>();
		this.areaViews = new ArrayList<>();
		addMouseMotionListener(ma);
		addMouseWheelListener(ma);
		addMouseListener(ma);
//		this.zoomer = false;
//		this.zoomFactor = 1;
		mainPanelHUD = new MainPanelHUD(0, 650);
		this.hideHud = false;
    Coordinate point = new Coordinate(47.46667, -0.55);
    this.setDisplayPosition(point, 0);		//this.setDisplayPositionByLatLon(31.23, 121.47, 14);


	}
	
	///**
//	 * add a stationView to the list.
//	 * @param stationView stationView to add
//	 */
//	public void addStationView(StationView stationView) {
//		this.stationViews.add(stationView);
//	}
	/** add a lineView to the list.
	 * @param lineView lineView to add
	 */
	public void addLineView(LineView lineView) {
		this.lineViews.add(lineView);
	}
	/**add an AreaView to the list
	 * @param areaView areaView to add
	 */
	public void addAreaView(AreaView areaView) {
		this.areaViews.add(areaView);
	}
	
	
//	
//	public LineView getLineView() {
//		return lineView;
//	}
//
//	public void setLineView(LineView lineView) {
//		this.lineView = lineView;
//	}
	
	/**get lineView list.
	 * @return List lineviews
	 */
	public List<LineView> getLineViews() {
		return lineViews;
	}

	/**set lineview list.
	 * @param lineViews panel lineViews
	 */
	public void setLineViews(List<LineView> lineViews) {
		this.lineViews = lineViews;
	}

//	public StationView getStationView() {
//		return stationView;
//	}
//
//	public void setStationView(StationView stationView) {
//		this.stationView = stationView;
//	}
//
//	public List<StationView> getStationViews() {
//		return stationViews;
//	}
//
//	public void setStationViews(List<StationView> stationViews) {
//		this.stationViews = stationViews;
//	}
	
	
	/**get AreaView List.
	 * @return List Areaview
	 */
	public List<AreaView> getAreaViews() {
		return areaViews;
	}

	/**set AreaViewList.
	 * @param areaViews list of areaView
	 */
	public void setAreaViews(List<AreaView> areaViews) {
		this.areaViews = areaViews;
	}

//	public double getZoomFactor() {
//		return this.zoomFactor;
//	}
//	public void setZoomFactor(double factor){        
//	    if(factor<this.zoomFactor){
//	        this.zoomFactor=this.zoomFactor/1.1;
//	    }
//	    else{
//	        this.zoomFactor=factor;
//	    }
//	    this.zoomer=true;
//	    System.out.println(this.zoomFactor);
//	}
	
	

	/**get the panel which contain the HUD.
	 * @return mainPanelHud
	 */
	public MainPanelHUD getMainPanelHUD() {
		return mainPanelHUD;
	}
	

	/** return if the hud is hide or not.
	 * @return boolean isHideHUD
	 */
	public boolean isHideHud() {
		return hideHud;
	}

	/**set the hud state (hide or present)
	 * @param hideHud HUD panel
	 */
	public void setHideHud(boolean hideHud) {
		this.hideHud = hideHud;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {//display all panel elements
		Graphics2D g2D = (Graphics2D) g.create();
		 g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paintComponent(g);
	
//		//Grid start
//        g2D.setColor(new Color(231,231,231));
//        int sideLength = 20;
//        int nRowCount = getHeight() / sideLength;
//        int currentX = sideLength;
//        for (int i = 0; i < nRowCount; i++) {
//            g2D.drawLine(0, currentX, getWidth(), currentX);
//            currentX = currentX + sideLength;
//        }
//        
//        int nColumnCount = getWidth() / sideLength;
//        int currentY = sideLength;
//        
//        for (int i = 0; i < nColumnCount; i++) {
//            g2D.drawLine(currentY, 0, currentY, getHeight());
//            currentY = currentY + sideLength;
//        }
//        //Grid end
        
        //hud display
        if(!hideHud) {
            mainPanelHUD.affiche(g2D);
        }


    	//LineViews Display
		if (lineViews !=null) {
			for (LineView lineView : lineViews) {
				lineView.affiche(g2D);
			}
		}
		
		//AreaViews Display
		if (this.areaViews !=null) {
			for (AreaView areaView:this.areaViews) {
				areaView.display(g2D);
			}
		}
		

	}

}
