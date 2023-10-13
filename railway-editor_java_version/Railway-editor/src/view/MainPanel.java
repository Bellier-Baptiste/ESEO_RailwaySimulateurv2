/**
 * Class part of the view package of the application.
 */

package view;

import controller.MovingAdapter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

/**
 * Main Panel which cntains all the views.
 *
 * @author Arthur Lagarce
 */
public final class MainPanel extends JMapViewer {
  //constants
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * MainPanel default width.
   */
  public static final int PANEL_WIDTH_DEFAULT = 1000;
  /**
   * MainPanel default height.
   */
  public static final int PANEL_HEIGHT_DEFAULT = 600;
  /**
   * MainPanel Singleton instance.
   */
  private static MainPanel instance;
  /** MainPanel HUD. */
  private final transient MainPanelHUD mainPanelHud;
  /** List of lineViews. */
  private transient List<LineView> lineViews;
  /** List of areaViews. */
  private transient List<AreaView> areaViews;
  /** Boolean to know if the HUD is hidden or not. */
  private boolean hideHud;

  /**
   * MainPanel constructor.
   */
  private MainPanel() {
    Dimension dim = new Dimension(MainPanel.PANEL_WIDTH_DEFAULT,
        MainPanel.PANEL_HEIGHT_DEFAULT);
    this.setPreferredSize(dim);
    this.lineViews = new ArrayList<>();
    this.areaViews = new ArrayList<>();
    MovingAdapter ma = new MovingAdapter();
    addMouseMotionListener(ma);
    addMouseWheelListener(ma);
    addMouseListener(ma);
    mainPanelHud = new MainPanelHUD(0, 650);
    this.hideHud = false;
    Coordinate point = new Coordinate(47.46667, -0.55);
    this.setDisplayPosition(point, 0);
    this.setZoom(2);
  }

  /**
   * Create MainPanel Singleton.
   *
   * @return MainPanel instance
   */
  public static MainPanel getInstance() {
    if (instance == null) {
      instance = new MainPanel();
    }
    return instance;
  }

  /**
   * add a lineView to the list.
   *
   * @param lineView lineView to add
   */
  public void addLineView(final LineView lineView) {
    this.lineViews.add(lineView);
  }

  /**
   * Add an AreaView to the list.
   *
   * @param areaView areaView to add
   */
  public void addAreaView(final AreaView areaView) {
    this.areaViews.add(areaView);
  }

  /**
   * get lineView list.
   *
   * @return List lineviews
   */
  public List<LineView> getLineViews() {
    return lineViews;
  }

  /**
   * set lineview list.
   *
   * @param lineViewsToSet panel lineViews
   */
  public void setLineViews(final List<LineView> lineViewsToSet) {
    this.lineViews = lineViewsToSet;
  }

  /**
   * get AreaView List.
   *
   * @return List Areaview
   */
  public List<AreaView> getAreaViews() {
    return areaViews;
  }

  /**
   * set AreaViewList.
   *
   * @param areaViewsToSet list of areaView
   */
  public void setAreaViews(final List<AreaView> areaViewsToSet) {
    this.areaViews = areaViewsToSet;
  }

  /**
   * get the panel which contain the HUD.
   *
   * @return mainPanelHud
   */
  public MainPanelHUD getMainPanelHud() {
    return mainPanelHud;
  }


  /**
   * return if the hud is hide or not.
   *
   * @return boolean isHideHUD
   */
  public boolean isHideHud() {
    return hideHud;
  }

  /**
   * Set the hud state (hide or present).
   *
   * @param hideHudToSet HUD panel
   */
  public void setHideHud(final boolean hideHudToSet) {
    this.hideHud = hideHudToSet;
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paintComponent(final Graphics g) { //display all panel elements
    Graphics2D g2D = (Graphics2D) g.create();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    super.paintComponent(g);
    //hud display
    if (!hideHud) {
      mainPanelHud.affiche(g2D);
    }
    //LineViews Display
    if (lineViews != null) {
      for (LineView lineView : lineViews) {
        lineView.affiche(g2D);
      }
    }
    //AreaViews Display
    if (this.areaViews != null) {
      for (AreaView areaView : this.areaViews) {
        areaView.display(g2D);
      }
    }
  }
}
