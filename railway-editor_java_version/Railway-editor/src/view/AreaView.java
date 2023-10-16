/** Class part of the view package of the application. */

package view;

import java.awt.Graphics2D;

import model.Area;

/**
 * View class which describes Area shape.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class AreaView {
  /** Area bound to the view. */
  private Area area;

  /**
   * Constructor.
   *
   * @param areaOfTheView area bound to the view
   */
  public AreaView(final Area areaOfTheView) {
    this.area = areaOfTheView;
  }

  /**
   * get the area model linked to the view.
   *
   * @return Area area binded to the view
   */
  public Area getArea() {
    return area;
  }

  /**
   * link and area model to the view.
   *
   * @param areaOfTheView area to bind
   */
  public void setArea(final Area areaOfTheView) {
    this.area = areaOfTheView;
  }

  /**
   * display the area shape.
   *
   * @param g2D graphics component
   */
  public void display(final Graphics2D g2D) {
    g2D.setColor(this.area.getColor());
    g2D.fillRect(this.area.getPosX(), this.area.getPosY(), this.area.getWidth(),
        this.area.getHeight());
  }
}
