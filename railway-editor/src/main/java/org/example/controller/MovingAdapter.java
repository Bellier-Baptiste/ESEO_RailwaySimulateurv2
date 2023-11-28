/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.controller;

import org.example.data.Data;
import org.example.model.Area;
import org.example.model.Station;
import org.example.view.AreaSetDistribution;
import org.example.view.AreaView;
import org.example.view.LineView;
import org.example.view.MainWindow;
import org.example.view.Popup;
import org.example.view.StationView;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which manage all actions relative to the {@link org.example.view.MainPanel}
 * using mouse.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file MovingAdapter.java
 * @date N/A
 * @since 2.0
 */
public class MovingAdapter extends MouseAdapter {
  // attributes
  /**
   * x coordinate of the click.
   */
  private int coordX;
  /**
   * y coordinate of the click.
   */
  private int coordY;
  /**
   * view of the station to merge.
   */
  private StationView[] stationsToMergeView;
  /**
   * boolean to know if the station to merge has been selected.
   */
  private boolean selectSecStation;
  /**
   * line of the selected station view.
   */
  private LineView selectedStationLineView;
  /**
   * view of the lines of the stations to merge.
   */
  private LineView[] lineStationToMergeViews;
  /**
   * boolean to know if a station is being dragged.
   */
  private boolean stationDrag;
  /**
   * boolean to know if an area is being dragged.
   */
  private boolean areaDrag;
  /**
   * boolean to know if an area is being extended on the left side.
   */
  private boolean extendLeftSide;
  /**
   * boolean to know if an area is being extended on the right side.
   */
  private boolean extendRightSide;
  /**
   * boolean to know if an area is being extended on the top side.
   */
  private boolean extendTopSide;
  /**
   * boolean to know if an area is being extended on the bottom side.
   */
  private boolean extendBotSide;
  /**
   * boolean to know if the right button of the mouse is pressed.
   */
  private boolean rightPressed;
  /**
   * station being dragged.
   */
  private Station draggedStation;
  /**
   * area being dragged.
   */
  private Area draggedArea;
  /**
   * area being extended.
   */
  private Area extendedArea;
  /**
   * form to set the distribution of the area.
   */
  private final AreaSetDistribution form;

  /**
   * MovingAdapter constructor.
   */
  public MovingAdapter() {
    super();
    this.form = new AreaSetDistribution();
  }


  // Mouse Event

  /**
   * Action when mouse is pressed.
   *
   * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
   */
  @Override
  public void mousePressed(final MouseEvent e) {
    coordX = e.getX();
    coordY = e.getY();

    rightPressed = (e.getButton() == MouseEvent.BUTTON3);

  }

  /**
   * Action when mouse is dragged.
   *
   * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseDragged(final MouseEvent e) {
    // drag the station and update its position in real time
    int dx = 0;
    int dy = 0;

    if (isInWidth(e.getX())) {
      dx = e.getX() - coordX;
    }
    if (isInHeight(e.getY())) {
      dy = e.getY() - coordY;
    }

    if (rightPressed) {
      moveAllStations(dx, dy);
      moveAllAreas(dx, dy);
    }

    StationView clickedStation = this.getClickedStation(coordX, coordY);
    AreaView clickedArea = this.getClickedArea(coordX, coordY);
    AreaView clickedTopAreaBorder = this.onTopAreaBorder(coordX, coordY);
    AreaView clickedBotAreaBorder = this.onBotAreaBorder(coordX, coordY);
    AreaView clickedLeftAreaBorder = this.onLeftAreaBorder(coordX, coordY);
    AreaView clickedRightAreaBorder = this.onRightAreaBorder(coordX, coordY);
    if (stationDrag) {
      draggedStation.moveStation(dx, dy);
      MainWindow.getInstance().getMainPanel().repaint();
    } else if (areaDrag) {
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
    } else if (clickedStation != null) {
      this.stationDrag = true;
      int stationSize = ActionStation.STATION_SIZE;
      int centerStationSize = ActionStation.CENTER_STATION_SIZE;

      clickedStation.setStationSize(stationSize);
      clickedStation.setCenterStationSize(centerStationSize);
      draggedStation = clickedStation.getStation();

    } else if (clickedArea != null) {
      this.areaDrag = true;
      draggedArea = clickedArea.getArea();
    } else if (clickedTopAreaBorder != null) {
      this.extendTopSide = true;
      this.extendedArea = clickedTopAreaBorder.getArea();
    } else if (clickedBotAreaBorder != null) {
      this.extendBotSide = true;
      this.extendedArea = clickedBotAreaBorder.getArea();
    } else if (clickedLeftAreaBorder != null) {
      this.extendLeftSide = true;
      this.extendedArea = clickedLeftAreaBorder.getArea();
    } else if (clickedRightAreaBorder != null) {
      this.extendRightSide = true;
      this.extendedArea = clickedRightAreaBorder.getArea();
    }

    coordX += dx;
    coordY += dy;

  }

  /**
   * Action when the mouse is clicked.
   *
   * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseClicked(final MouseEvent e) {
    if (selectSecStation) { // if a station to merge has been selected
      this.stationsToMergeView[1] = this.getClickedStation(e.getX(), e.getY());
      this.lineStationToMergeViews[1] = this.selectedStationLineView;
      this.mergeStation(stationsToMergeView);
      MainWindow.getInstance().getMainPanel().repaint();
      this.selectSecStation = false;
    }
    this.getClickedArea(e.getX(), e.getY());
    if (e.getClickCount() == 2) {
      stationZoomUpdatePos();
      StationView clickedStation = this.getClickedStation(e.getX(), e.getY());
      AreaView clickedArea = this.getClickedArea(e.getX(), e.getY());
      if (clickedStation != null) {
        if (Data.getInstance().getSelectType() != null) {
          switch (Data.getInstance().getSelectType()) {
            case Data.STATION_START:
              Data.getInstance()
                  .setStationStartId(clickedStation.getStation().getId());
              Data.getInstance().setSelectType("");
              break;
            case Data.STATION_END:
              Data.getInstance()
                  .setStationEndId(clickedStation.getStation().getId());
              Data.getInstance().setSelectType("");
              break;
            case Data.STATION_CONCERNED:
              Data.getInstance()
                  .setStationConcernedId(clickedStation.getStation().getId());
              Data.getInstance().setSelectType("");
              break;
            default:
              break;
          }
        } else { // if we want to start a merge
          this.stationsToMergeView = new StationView[2];
          this.lineStationToMergeViews = new LineView[2];
          this.stationsToMergeView[0] = this.getClickedStation(e.getX(),
              e.getY());
          this.lineStationToMergeViews[0] = this.selectedStationLineView;
          Popup popup = new Popup();
          popup.setObserver(choice -> this.update((Integer) choice));
          popup.pop();
        }
      } else if (clickedArea != null) {
        form.pop(clickedArea.getArea());
      }
    }

    StationView clickedStation = this.getClickedStation(e.getX(), e.getY());
    // if we want to delete the selected station
    if (e.getButton() == MouseEvent.BUTTON3 && !selectSecStation
      && clickedStation != null) {
      deleteStation(clickedStation);
      MainWindow.getInstance().getMainPanel().repaint();
    }
  }

  /**
   * Action when the mouse is released.
   *
   * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseReleased(final MouseEvent e) {
    // reset
    this.draggedStation = null;
    this.stationDrag = false;
    this.draggedArea = null;
    this.areaDrag = false;
    this.extendBotSide = false;
    this.extendTopSide = false;
    this.extendLeftSide = false;
    this.extendRightSide = false;
    this.extendedArea = null;
    this.rightPressed = false;
  }

  /**
   * Action when the mousewheel is moved.
   *
   * @see MouseAdapter#mouseWheelMoved(MouseWheelEvent)
   */
  @Override
  public void mouseWheelMoved(final MouseWheelEvent e) {
    stationZoomUpdatePos();
    areaZoomUpdatePos();
  }

  /**
   * Action when the mouse is moved.
   *
   * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
   */
  @Override
  public void mouseMoved(final MouseEvent e) {
    if (this.onLeftAreaBorder(e.getX(), e.getY()) != null
        || this.onRightAreaBorder(e.getX(), e.getY()) != null) {
      MainWindow.getInstance().getMainPanel().setCursor(new Cursor(
          Cursor.E_RESIZE_CURSOR));
    } else if (this.onTopAreaBorder(e.getX(), e.getY()) != null
        || this.onBotAreaBorder(e.getX(), e.getY()) != null) {
      MainWindow.getInstance().getMainPanel().setCursor(new Cursor(
          Cursor.S_RESIZE_CURSOR));
    } else {
      MainWindow.getInstance().getMainPanel().setCursor(new Cursor(
          Cursor.DEFAULT_CURSOR));
    }
    Station station;
    StationView clickedStationView = this.getClickedStation(e.getX(), e.getY());
    if (clickedStationView != null) {
      Station clickedStation = clickedStationView.getStation();
      if (clickedStation == null) {
        throw new NullPointerException();
      } else {
        station = clickedStation;
      }
      MainWindow.getInstance().getMainPanel().getMainPanelHud().setStation(
          station);
      MainWindow.getInstance().getMainPanel().repaint();
    } else {
      MainWindow.getInstance().getMainPanel().getMainPanelHud().setStation(
          null);
      MainWindow.getInstance().getMainPanel().repaint();
    }
  }

  /**
   * If a click is in a circle (if we clicked on a station).
   *
   * @param otherCoordX x click coordinates
   * @param otherCoordY y click coordinates
   * @param a           circle center x coordinates
   * @param b           circle center y coordinates
   * @param size        circle diameter
   * @return true if the click is in the circle, false otherwise
   */
  private boolean contains(final int otherCoordX, final int otherCoordY,
                           final int a,
                           final int b,
                           final int size) {
    return (Math.pow(otherCoordX - (a - size / 2f), 2)
        + Math.pow(otherCoordY - (b - size / 2f), 2) < Math.pow(size, 2));
  }

  /**
   * If a click is in an area.
   *
   * @param otherCoordX x click coordinates
   * @param otherCoordY y click coordinates
   * @param a           area top left corner x coordinates
   * @param b           area top left corner y coordinates
   * @param width       area width
   * @param height      area height
   * @return true if the click is in the area, false otherwise
   */
  private boolean containsArea(final int otherCoordX, final int otherCoordY,
                               final int a, final int b, final int width,
                               final int height) {
    return (otherCoordX > a + 2 && otherCoordX < a + width - 2
        && otherCoordY > b + 2 && otherCoordY < b + height - 2);
  }

  /**
   * Find the station we selected with our click.
   *
   * @param clickedCoordX x click coordinates
   * @param clickedCoordY y click coordinates
   * @return the station we clicked on
   */
  protected StationView getClickedStation(final int clickedCoordX,
                                        final int clickedCoordY) {
    List<LineView> lineViews = MainWindow.getInstance().getMainPanel()
        .getLineViews();
    StationView returnedStation = null;
    List<StationView> stationviews;
    for (LineView lineview : lineViews) {
      stationviews = lineview.getStationViews();

      for (StationView stationView : stationviews) {
        if (contains(clickedCoordX, clickedCoordY,
            stationView.getStation().getPosX(),
            stationView.getStation().getPosY(), stationView.getStationSize())) {
          returnedStation = stationView;
          this.selectedStationLineView = lineview;
        }
      }
    }
    return returnedStation;
  }

  /**
   * Finds the area we selected with our click.
   *
   * @param clickedCoordX x click coordinates
   * @param clickedCoordY y click coordinates
   * @return the area we clicked on
   */
  protected AreaView getClickedArea(final int clickedCoordX,
                                  final int clickedCoordY) {

    List<AreaView> areaViews = MainWindow.getInstance().getMainPanel()
        .getAreaViews();
    AreaView returnedArea = null;
    for (AreaView areaView : areaViews) {

      if (containsArea(clickedCoordX, clickedCoordY,
          areaView.getArea().getPosX(), areaView.getArea().getPosY(),
          areaView.getArea().getWidth(), areaView.getArea().getHeight())) {
        returnedArea = areaView;
      }
    }
    return returnedArea;
  }

  /**
   * Merge 2 stations (basically replace one with the other in the correct line
   * stations list).
   *
   * @param stationToMergeViews station views to merge
   */
  private void mergeStation(final StationView[] stationToMergeViews) {
    int stationIndex = this.lineStationToMergeViews[1].getLine().getStations()
        .indexOf(stationToMergeViews[1].getStation());
    int stationViewIndex = this.lineStationToMergeViews[1].getStationViews()
        .indexOf(stationToMergeViews[1]);

    // replace the station to merge in stations list
    this.lineStationToMergeViews[1].getLine().getStations().set(stationIndex,
        stationToMergeViews[0].getStation());
    // replace station to merge in stationView list
    this.lineStationToMergeViews[1].getStationViews().set(stationViewIndex,
        stationToMergeViews[0]);
  }

  /**
   * Delete selected station (remove it in the correct line stations list).
   *
   * @param stationToDeleteView station to delete
   */
  private void deleteStation(final StationView stationToDeleteView) {
    int stationIndex = this.selectedStationLineView.getLine().getStations()
        .indexOf(stationToDeleteView.getStation());
    int stationViewIndex = this.selectedStationLineView.getStationViews()
        .indexOf(stationToDeleteView);
    // remove in station list
    this.selectedStationLineView.getLine().getStations().remove(stationIndex);
    // remove in stationView list
    this.selectedStationLineView.getStationViews().remove(stationViewIndex);
  }

  /**
   * Checks if the station is not out of the Panel horizontally.
   *
   * @param otherCoordX x coordinate of the station
   * @return true if the station is in the panel, false otherwise
   */
  private boolean isInWidth(final int otherCoordX) {
    return (otherCoordX > 0 && otherCoordX < MainWindow.getInstance()
        .getMainPanel().getSize().width);
  }

  /**
   * Checks if the station is not out of the Panel vertically.
   *
   * @param otherCoordY y coordinate of the station
   * @return true if the station is in the panel, false otherwise
   */
  private boolean isInHeight(final int otherCoordY) {
    return (otherCoordY > 0 && otherCoordY < MainWindow.getInstance()
        .getMainPanel().getSize().height);
  }

  /**
   * Checks if cursor is on a left area border.
   *
   * @param otherCoordX x coordinate of the cursor
   * @param otherCoordY y coordinate of the cursor
   * @return the area where the cursor is in.
   */
  private AreaView onLeftAreaBorder(final int otherCoordX,
                                    final int otherCoordY) {
    List<AreaView> areaViews = MainWindow.getInstance().getMainPanel()
        .getAreaViews();
    AreaView returnedArea = null;
    for (AreaView areaView : areaViews) {

      if ((otherCoordX >= areaView.getArea().getPosX() - 2
          && otherCoordX <= areaView.getArea().getPosX() + 2)
          && otherCoordY >= areaView.getArea().getPosY()
          && otherCoordY < areaView.getArea().getPosY()
          + areaView.getArea().getHeight()) {
        returnedArea = areaView;
      }
    }
    return returnedArea;
  }

  /**
   * check if cursor is on a top area border.
   *
   * @param otherCoordX x coordinate of the cursor
   * @param otherCoordY y coordinate of the cursor
   * @return the area where the cursor is in.
   */
  private AreaView onTopAreaBorder(final int otherCoordX,
                                   final int otherCoordY) {
    List<AreaView> areaViews = MainWindow.getInstance().getMainPanel()
        .getAreaViews();
    AreaView returnedArea = null;
    for (AreaView areaView : areaViews) {
      if ((otherCoordY >= areaView.getArea().getPosY() - 2
          && otherCoordY <= areaView.getArea().getPosY() + 2)
          && otherCoordX >= areaView.getArea().getPosX()
          && otherCoordX < areaView.getArea().getPosX()
          + areaView.getArea().getWidth()) {
        returnedArea = areaView;
      }
    }
    return returnedArea;
  }

  /**
   * check if cursor is on a right area border.
   *
   * @param otherCoordX x coordinate of the cursor
   * @param otherCoordY y coordinate of the cursor
   * @return the area where the cursor is in.
   */
  private AreaView onRightAreaBorder(final int otherCoordX,
                                     final int otherCoordY) {
    List<AreaView> areaViews = MainWindow.getInstance().getMainPanel()
        .getAreaViews();
    AreaView returnedArea = null;
    for (AreaView areaView : areaViews) {

      if (((otherCoordX >= areaView.getArea().getPosX()
          + areaView.getArea().getWidth() - 2)
          && otherCoordX <= areaView.getArea().getPosX()
          + areaView.getArea().getWidth() + 2)
          && otherCoordY >= areaView.getArea().getPosY()
          && otherCoordY < areaView.getArea().getPosY()
          + areaView.getArea().getHeight()) {
        returnedArea = areaView;
      }
    }
    return returnedArea;
  }

  /**
   * check if cursor is on a bottom area border.
   *
   * @param otherCoordX x coordinate of the cursor
   * @param otherCoordY y coordinate of the cursor
   * @return the area where the cursor is in.
   */
  private AreaView onBotAreaBorder(final int otherCoordX,
                                   final int otherCoordY) {
    List<AreaView> areaViews = MainWindow.getInstance().getMainPanel()
        .getAreaViews();
    AreaView returnedArea = null;
    for (AreaView areaView : areaViews) {
      if ((otherCoordY >= areaView.getArea().getPosY()
          + areaView.getArea().getHeight() - 2
          && otherCoordY <= areaView.getArea().getPosY()
          + areaView.getArea().getHeight() + 2)
          && otherCoordX >= areaView.getArea().getPosX()
          && otherCoordX < areaView.getArea().getPosX()
          + areaView.getArea().getWidth()) {
        returnedArea = areaView;
      }
    }
    return returnedArea;
  }

  /**
   * move all the stations in the same direction.
   *
   * @param dx x direction
   * @param dy y direction
   */
  private void moveAllStations(final int dx, final int dy) {
    List<Integer> idStations;
    idStations = new ArrayList<>();
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
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
   * Moves all the areas in the same direction.
   *
   * @param dx x direction
   * @param dy y direction
   */
  private void moveAllAreas(final int dx, final int dy) {
    for (AreaView areaView : MainWindow.getInstance().getMainPanel()
        .getAreaViews()) {
      areaView.getArea().moveArea(dx, dy);
    }
    MainWindow.getInstance().getMainPanel().repaint();
  }

  /**
   * update the stations positions on the screen to fit with their latitude
   * and longitude param.
   */
  private void stationZoomUpdatePos() {
    for (LineView lineView : MainWindow.getInstance().getMainPanel()
        .getLineViews()) {
      for (StationView stationView : lineView.getStationViews()) {
        Point pos = MainWindow.getInstance().getMainPanel().getMapPosition(
            stationView.getStation().getLatitude(), stationView.getStation()
                .getLongitude(), false);
        stationView.getStation().setPosX((int) pos.getX());
        stationView.getStation().setPosY((int) pos.getY());
      }
    }
    MainWindow.getInstance().getMainPanel().repaint();

  }

  /**
   * update the areas positions on the screen to fit with their latitude
   * and longitude param.
   */
  private void areaZoomUpdatePos() {
    for (AreaView areaView : MainWindow.getInstance().getMainPanel()
        .getAreaViews()) {
      Point pos1 = MainWindow.getInstance().getMainPanel().getMapPosition(
          areaView.getArea().getLatitudeTop(), areaView.getArea()
              .getLongitudeTop(), false);

      Point pos2 = MainWindow.getInstance().getMainPanel().getMapPosition(
          areaView.getArea().getLatitudeBot(), areaView.getArea()
              .getLongitudeBot(), false);
      areaView.getArea().setPosX((int) pos1.getX());
      areaView.getArea().setPosY((int) pos1.getY());
      areaView.getArea().setWidth(Math.abs(pos2.x - pos1.x));
      areaView.getArea().setHeight(Math.abs(pos2.y - pos1.y));

    }

    MainWindow.getInstance().getMainPanel().repaint();

  }

  /**
   * Updates the boolean selectSecStation.
   *
   * @param choice ok or cancel
   */
  public void update(final int choice) {
    this.selectSecStation = choice == 1;
  }
}
