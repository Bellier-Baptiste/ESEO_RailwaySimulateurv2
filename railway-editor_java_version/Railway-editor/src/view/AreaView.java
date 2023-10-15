package view;

import java.awt.Graphics2D;

import model.Area;

/**
 * View class which describes Area shape.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class AreaView {
  private Area area;

  /**
   * Constructor.
   *
   * @param area area binded to the view
   */
  public AreaView(Area area) {
    this.area = area;
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
   * @param area area to bind
   */
  public void setArea(Area area) {
    this.area = area;
  }

  /**
   * display the area shape.
   *
   * @param g2D graphics component
   */
  public void display(Graphics2D g2D) {
    g2D.setColor(this.area.getColor());
    g2D.fillRect(this.area.getPosX(), this.area.getPosY(), this.area.getWidth(),
        this.area.getHeight());
  }
}
