package controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Model.Area;
import Model.Station;
import data.Data;
import view.AreaSetDistribution;
import view.AreaView;
import view.LineView;
import view.MainWindow;
import view.Popup;
import view.StationView;

/**
 * Class which manage all actions relative to the main panel using mouse.
 * 
 * @author Arthur Lagarce
 *
 */
public class MovingAdapter extends MouseAdapter implements Observer {

	// attributes
	private int x;
	private int y;
	private StationView[] stationsToMergeView;
	private StationView stationToDeleteView;
	private boolean selectSecStation;
	private LineView selectedStationLineView;
	private LineView[] lineStationToMergeViews;
	private boolean stationdrag;
	private boolean areadrag;
	private boolean extendLeftSide;
	private boolean extendRightSide;
	private boolean extendTopSide;
	private boolean extendBotSide;
	private boolean rightPressed;
	private Station draggedStation;
	private Area draggedArea;
	private Area extendedArea;
	private AreaSetDistribution form;

	/**
	 * Constructor
	 * 
	 */
	public MovingAdapter() {
		super();
		this.form = new AreaSetDistribution();
	}

	// Mouse Event

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();

		rightPressed = (e.getButton() == MouseEvent.BUTTON3);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		// drag the station and update it's position in real time
		int dx = 0;
		int dy = 0;

		if (isInWidth(e.getX())) {
			dx = e.getX() - x;
		}
		if (isInHeight(e.getY())) {
			dy = e.getY() - y;
		}

		if (rightPressed) {
			moveAllStations(dx, dy);
			moveAllAreas(dx, dy);
		}
		if (stationdrag) {
			draggedStation.moveStation(dx, dy);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (areadrag) {
			draggedArea.moveArea(dx, dy);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (extendBotSide) {
			extendedArea.extendBotSide(dy);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (extendTopSide) {
			extendedArea.extendTopSide(dy);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (extendLeftSide) {
			extendedArea.extendLeftSide(dx);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (extendRightSide) {
			extendedArea.extendRightSide(dx);
			MainWindow.getInstance().getMainPanel().repaint();
		} else if (this.getClickedStation(x, y) != null) {
			this.stationdrag = true;
			int stationSize = 18;
			int centerStationSize = 14;

			this.getClickedStation(x, y).setStationSize(stationSize);
			this.getClickedStation(x, y).setCenterStationSize(centerStationSize);
			draggedStation = this.getClickedStation(x, y).getStation();

		} else if (this.getClickedArea(x, y) != null) {
			this.areadrag = true;
			draggedArea = this.getClickedArea(x, y).getArea();
		} else if (this.onTopAreaBorder(x, y) != null) {
			this.extendTopSide = true;
			this.extendedArea = this.onTopAreaBorder(x, y).getArea();
		} else if (this.onBotAreaBorder(x, y) != null) {
			this.extendBotSide = true;
			this.extendedArea = this.onBotAreaBorder(x, y).getArea();
		} else if (this.onLeftAreaBorder(x, y) != null) {
			this.extendLeftSide = true;
			this.extendedArea = this.onLeftAreaBorder(x, y).getArea();
		} else if (this.onRightAreaBorder(x, y) != null) {
			this.extendRightSide = true;
			this.extendedArea = this.onRightAreaBorder(x, y).getArea();
		}

		x += dx;
		y += dy;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (selectSecStation) {// if a station to merge has been selected
			this.stationsToMergeView[1] = this.getClickedStation(e.getX(), e.getY());
			this.lineStationToMergeViews[1] = this.selectedStationLineView;
			this.mergeStation(stationsToMergeView);
			MainWindow.getInstance().getMainPanel().repaint();
			this.selectSecStation = false;
		}
		this.getClickedArea(e.getX(), e.getY());
		if (e.getClickCount() == 2) {
			stationZoomUpdatePos(1);
			if (this.getClickedStation(e.getX(), e.getY()) != null) {
				if (Data.getInstance().getSelectType() != null) {
					if (Data.getInstance().getSelectType().equals(Data.STATION_START)) {
						Data.getInstance()
								.setStationStartId(this.getClickedStation(e.getX(), e.getY()).getStation().getId());
						Data.getInstance().setSelectType("");
					} else if (Data.getInstance().getSelectType().equals(Data.STATION_END)) {
						Data.getInstance()
								.setStationEndId(this.getClickedStation(e.getX(), e.getY()).getStation().getId());
						Data.getInstance().setSelectType("");
					} else if (Data.getInstance().getSelectType().equals(Data.STATION_CONCERNED)) {
						Data.getInstance()
								.setStationConcernedId(this.getClickedStation(e.getX(), e.getY()).getStation().getId());
						Data.getInstance().setSelectType("");
					}
				} else {// if we want to start a merge
					this.stationsToMergeView = new StationView[2];
					this.lineStationToMergeViews = new LineView[2];
					this.stationsToMergeView[0] = this.getClickedStation(e.getX(), e.getY());
					this.lineStationToMergeViews[0] = this.selectedStationLineView;
					Popup popup = new Popup();
					popup.addObserver(this);
					popup.pop();
					
				}

			} else if (this.getClickedArea(e.getX(), e.getY()) != null) {
				form.pop(this.getClickedArea(e.getX(), e.getY()).getArea());

			}
		}

		if (e.getButton() == MouseEvent.BUTTON3 && !selectSecStation
				&& this.getClickedStation(e.getX(), e.getY()) != null) {// if we want to delete the selected station
			this.stationToDeleteView = this.getClickedStation(e.getX(), e.getY());
			deleteStation(this.stationToDeleteView);
			MainWindow.getInstance().getMainPanel().repaint();

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// reset
		this.draggedStation = null;
		this.stationdrag = false;
		this.draggedArea = null;
		this.areadrag = false;
		this.extendBotSide = false;
		this.extendTopSide = false;
		this.extendLeftSide = false;
		this.extendRightSide = false;
		this.extendedArea = null;
		this.rightPressed = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseAdapter#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		stationZoomUpdatePos(e.getWheelRotation());
		AreaZoomUpdatePos(e.getWheelRotation());
	}

	public void mouseMoved(MouseEvent e) {
		if (this.onLeftAreaBorder(e.getX(), e.getY()) != null || this.onRightAreaBorder(e.getX(), e.getY()) != null) {
			MainWindow.getInstance().getMainPanel().setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		} else if (this.onTopAreaBorder(e.getX(), e.getY()) != null
				|| this.onBotAreaBorder(e.getX(), e.getY()) != null) {
			MainWindow.getInstance().getMainPanel().setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		} else {
			MainWindow.getInstance().getMainPanel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		Station station = null;
		if (getClickedStation(e.getX(), e.getY()) != null) {
			station = getClickedStation(e.getX(), e.getY()).getStation();
			MainWindow.getInstance().getMainPanel().getMainPanelHUD().setStation(station);
			MainWindow.getInstance().getMainPanel().repaint();
		} else {
			MainWindow.getInstance().getMainPanel().getMainPanelHUD().setStation(null);
			MainWindow.getInstance().getMainPanel().repaint();
		}
	}

	/**
	 * if a click is in a circle (if we clicked on a station)
	 * 
	 * @param x    x click coordinates
	 * @param y    y click coordinates
	 * @param a    circle center x coordinates
	 * @param b    circle center y coordinates
	 * @param size circle diameter
	 * @return
	 */
	private boolean contains(int x, int y, int a, int b, int size) {
		return (Math.pow(x - (a - size / 2), 2) + Math.pow(y - (b - size / 2), 2) < Math.pow(size, 2));
	}

	/**
	 * if a click is in an area 
	 * @param x
	 * @param y
	 * @param a
	 * @param b
	 * @param width
	 * @param height
	 * @return
	 */
	private boolean containsArea(int x, int y, int a, int b, int width, int height) {
		return (x > a + 2 && x < a + width - 2 && y > b + 2 && y < b + height - 2);
	}

	/**
	 * find the station we selected with our click
	 * 
	 * @param x x click coordinates
	 * @param y y click coordinates
	 * @return
	 */
	private StationView getClickedStation(int x, int y) {
		List<LineView> lineViews = MainWindow.getInstance().getMainPanel().getLineViews();
		StationView returnedStation = null;
		List<StationView> stationviews;
		for (LineView lineview : lineViews) {
			stationviews = lineview.getStationViews();

			for (StationView stationView : stationviews) {
				if (contains(x, y, stationView.getStation().getPosX(), stationView.getStation().getPosY(),
						stationView.getStationSize())) {
					returnedStation = stationView;
					this.selectedStationLineView = lineview;
				}
			}
		}
		return returnedStation;
	}

	/**
	 *  find the area we selected with our click
	 * @param x
	 * @param y
	 * @return
	 */
	private AreaView getClickedArea(int x, int y) {

		List<AreaView> areaViews = MainWindow.getInstance().getMainPanel().getAreaViews();
		AreaView returnedArea = null;
		for (AreaView areaView : areaViews) {

			if (containsArea(x, y, areaView.getArea().getPosX(), areaView.getArea().getPosY(),
					areaView.getArea().getWidth(), areaView.getArea().getHeight())) {
				returnedArea = areaView;
			}
		}
		return returnedArea;
	}

	/**
	 * Merge 2 stations (basically replace one with the other in the correct line
	 * stations list
	 * 
	 * @param stationToMergeViews
	 */
	private void mergeStation(StationView[] stationToMergeViews) {
		int stationIndex = this.lineStationToMergeViews[1].getLine().getStations()
				.indexOf(stationToMergeViews[1].getStation());
		int stationViewIndex = this.lineStationToMergeViews[1].getStationViews().indexOf(stationToMergeViews[1]);

		// replace the station to merge in stations list
		this.lineStationToMergeViews[1].getLine().getStations().set(stationIndex, stationToMergeViews[0].getStation());
		// replace station to merge in stationView list
		this.lineStationToMergeViews[1].getStationViews().set(stationViewIndex, stationToMergeViews[0]);
	}

	/**
	 * Delete selected station (remove it in the correct line stations list)
	 * 
	 * @param stationToDeleteView
	 */
	private void deleteStation(StationView stationToDeleteView) {
		int stationIndex = this.selectedStationLineView.getLine().getStations()
				.indexOf(stationToDeleteView.getStation());
		int stationViewIndex = this.selectedStationLineView.getStationViews().indexOf(stationToDeleteView);
		this.selectedStationLineView.getLine().getStations().remove(stationIndex);// remove in station list
		this.selectedStationLineView.getStationViews().remove(stationViewIndex);// remove in stationView list
	}

	/**
	 * check if the station is not out of the Panel horizontally
	 * 
	 * @param x
	 * @return
	 */
	private boolean isInWidth(int x) {
		return (x > 0 && x < MainWindow.getInstance().getMainPanel().getSize().width);
	}

	/**
	 * check if the station is not out of the Panel vertically
	 * 
	 * @param y
	 * @return
	 */
	private boolean isInHeight(int y) {
		return (y > 0 && y < MainWindow.getInstance().getMainPanel().getSize().height);
	}

	/**
	 * check if cursor is on a left area border.
	 * 
	 * @param x
	 * @param y
	 * @return the area where the cursor is in.
	 */
	private AreaView onLeftAreaBorder(int x, int y) {
		List<AreaView> areaViews = MainWindow.getInstance().getMainPanel().getAreaViews();
		AreaView returnedArea = null;
		for (AreaView areaView : areaViews) {

			if ((x >= areaView.getArea().getPosX() - 2 && x <= areaView.getArea().getPosX() + 2)
					&& y >= areaView.getArea().getPosY()
					&& y < areaView.getArea().getPosY() + areaView.getArea().getHeight()) {
				returnedArea = areaView;
			}
		}
		return returnedArea;
	}

	/**
	 * check if cursor is on a top area border.
	 * 
	 * @param x
	 * @param y
	 * @return the area where the cursor is in.
	 */
	private AreaView onTopAreaBorder(int x, int y) {
		List<AreaView> areaViews = MainWindow.getInstance().getMainPanel().getAreaViews();
		AreaView returnedArea = null;
		for (AreaView areaView : areaViews) {
			if ((y >= areaView.getArea().getPosY() - 2 && y <= areaView.getArea().getPosY() + 2)
					&& x >= areaView.getArea().getPosX()
					&& x < areaView.getArea().getPosX() + areaView.getArea().getWidth()) {
				returnedArea = areaView;
			}
		}
		return returnedArea;
	}

	/**
	 * check if cursor is on a right area border.
	 * 
	 * @param x
	 * @param y
	 * @return the area where the cursor is in.
	 */
	private AreaView onRightAreaBorder(int x, int y) {
		List<AreaView> areaViews = MainWindow.getInstance().getMainPanel().getAreaViews();
		AreaView returnedArea = null;
		for (AreaView areaView : areaViews) {

			if (((x >= areaView.getArea().getPosX() + areaView.getArea().getWidth() - 2)
					&& x <= areaView.getArea().getPosX() + areaView.getArea().getWidth() + 2)
					&& y >= areaView.getArea().getPosY()
					&& y < areaView.getArea().getPosY() + areaView.getArea().getHeight()) {
				returnedArea = areaView;
			}
		}
		return returnedArea;
	}

	/**
	 * check if cursor is on a bottom area border.
	 * 
	 * @param x
	 * @param y
	 * @return the area where the cursor is in.
	 */
	private AreaView onBotAreaBorder(int x, int y) {
		List<AreaView> areaViews = MainWindow.getInstance().getMainPanel().getAreaViews();
		AreaView returnedArea = null;
		for (AreaView areaView : areaViews) {
			if (((y >= areaView.getArea().getPosY() + areaView.getArea().getHeight() - 2
					&& y <= areaView.getArea().getPosY() + areaView.getArea().getHeight() + 2))
					&& x >= areaView.getArea().getPosX()
					&& x < areaView.getArea().getPosX() + areaView.getArea().getWidth()) {
				returnedArea = areaView;
			}
		}
		return returnedArea;
	}

	/**
	 * move all the stations in the same direction.
	 * @param dx
	 * @param dy
	 */
	private void moveAllStations(int dx, int dy) {
		List<Integer> idStations;
		idStations = new ArrayList<>();
		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
			for (StationView stationView : lineView.getStationViews()) {
				if (!idStations.contains(stationView.getStation().getId())) {
					stationView.getStation().moveStation(dx, dy);
					idStations.add(stationView.getStation().getId());
				}
			}
		}
		MainWindow.getInstance().getMainPanel().repaint();
	}

	/**
	 * move all the areas in the same direction.
	 * @param dx
	 * @param dy
	 */
	private void moveAllAreas(int dx, int dy) {
		for (AreaView areaView : MainWindow.getInstance().getMainPanel().getAreaViews()) {
			areaView.getArea().moveArea(dx, dy);
		}
		MainWindow.getInstance().getMainPanel().repaint();
	}

	/**
	 * update the stations positions on the screen to fit with their latitude and longitude param.
	 * @param zoomDirection
	 */
	private void stationZoomUpdatePos(int zoomDirection) {
		for (LineView lineView : MainWindow.getInstance().getMainPanel().getLineViews()) {
			for (StationView stationView : lineView.getStationViews()) {
				Point pos = MainWindow.getInstance().getMainPanel().getMapPosition(
						stationView.getStation().getLatitude(), stationView.getStation().getLongitude(), false);
				stationView.getStation().setPosX((int) pos.getX());
				stationView.getStation().setPosY((int) pos.getY());
			}
		}
		MainWindow.getInstance().getMainPanel().repaint();

	}
	
	/**
	 * update the areas positions on the screen to fit with their latitude and longitude param.
	 * @param zoomDirection
	 */
	private void AreaZoomUpdatePos(int zoomDirection) {
		for (AreaView areaView : MainWindow.getInstance().getMainPanel().getAreaViews()) {
				Point pos1 = MainWindow.getInstance().getMainPanel().getMapPosition(
						areaView.getArea().getLatitudeTop(), areaView.getArea().getLongitudeTop(), false);
				
				Point pos2 = MainWindow.getInstance().getMainPanel().getMapPosition(
						areaView.getArea().getLatitudeBot(), areaView.getArea().getLongitudeBot(), false);
				areaView.getArea().setPosX((int) pos1.getX());
				areaView.getArea().setPosY((int) pos1.getY());
				areaView.getArea().setWidth((int)Math.abs(pos2.x-pos1.x));
				areaView.getArea().setHeight((int)Math.abs(pos2.y-pos1.y));

			}
		
		MainWindow.getInstance().getMainPanel().repaint();

	}

	@Override
	public void update(Observable o, Object arg) {
		if ((int) arg==1) {
			this.selectSecStation = true;
		}else {
			this.selectSecStation = false;
		}
	}
}
