/** Class part of the model package of the application. */

package model;

import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import view.MainWindow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Model Class to describe an Area.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class Area {
  /** Area opacity. */
  public static final float AREA_OPACITY = 0.5f;
  /** One hundred percent. */
  public static final int ONE_HUNDRED = 100;
  /** Area X position. */
  private int posX;
  /** Area Y position. */
  private int posY;
  /** Area id. */
  private int id;
  /** Area width. */
  private int width;
  /** Area height. */
  private int height;
  /** Area distribution. */
  private HashMap<String, Integer> distribution;
  /** Area distribution keys. */
  private List<String> distributionKey;
  /** Area color. */
  private Color color;
  /** Area destination. */
  private String destination;

  //top left corner
  /** Area top left corner latitude. */
  private double latitudeTop;
  /** Area top left corner longitude. */
  private double longitudeTop;

  //bottom right corner
  /** Area bottom right corner latitude. */
  private double latitudeBot;
  /** Area bottom right corner longitude. */
  private double longitudeBot;


  /**
   * Area constructor.
   *
   * @param idToSet     area id
   * @param areaPosX   area positionX
   * @param areaPosY   area positionY
   * @param areaWidth  area Width
   * @param areaHeight area height
   */
  public Area(final int idToSet, final int areaPosX, final int areaPosY,
              final int areaWidth, final int areaHeight) {
    super();
    this.posX = areaPosX;
    this.posY = areaPosY;
    this.width = areaWidth;
    this.height = areaHeight;
    this.distribution = new HashMap<>();
    this.distributionKey = new ArrayList<>();

    distribution.put(Data.AREA_TOURIST, 0);
    distribution.put(Data.AREA_STUDENT, 0);
    distribution.put(Data.AREA_BUSINESSMAN, 0);
    distribution.put(Data.AREA_WORKER, 0);
    distribution.put(Data.AREA_CHILD, 0);
    distribution.put(Data.AREA_RETIRED, 0);
    distribution.put(Data.AREA_UNEMPLOYED, 0);
    distributionKey.add(Data.AREA_TOURIST);
    distributionKey.add(Data.AREA_STUDENT);
    distributionKey.add(Data.AREA_BUSINESSMAN);
    distributionKey.add(Data.AREA_WORKER);
    distributionKey.add(Data.AREA_CHILD);
    distributionKey.add(Data.AREA_RETIRED);
    distributionKey.add(Data.AREA_RETIRED);
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    this.color = new Color(r, g, b, AREA_OPACITY);
  }


  /**
   * getter of id.
   *
   * @return id
   */
  public int getId() {
    return id;
  }


  /**
   * setter of id.
   *
   * @param idToSet areaId
   */
  public void setId(final int idToSet) {
    this.id = idToSet;
  }


  /**
   * getter of posX.
   *
   * @return top left corner X position
   */
  public int getPosX() {
    return posX;
  }


  /**
   * setter of posX.
   *
   * @param areaPosX area positionX
   */
  public void setPosX(final int areaPosX) {
    this.posX = areaPosX;
  }


  /**
   * getter of posY.
   *
   * @return top left corner Y position
   */
  public int getPosY() {
    return posY;
  }


  /**
   * setter of posY.
   *
   * @param areaPosY area positionY
   */
  public void setPosY(final int areaPosY) {
    this.posY = areaPosY;
  }


  /**
   * getter for width.
   *
   * @return with of the area rectangle
   */
  public int getWidth() {
    return width;
  }


  /**
   * setter for width.
   *
   * @param areaWidth area width
   */
  public void setWidth(final int areaWidth) {
    this.width = areaWidth;
  }


  /**
   * getter for height.
   *
   * @return height of the area rectangle
   */
  public int getHeight() {
    return height;
  }


  /**
   * setter for height.
   *
   * @param areaHeight area height
   */
  public void setHeight(final int areaHeight) {
    this.height = areaHeight;
  }


  /**
   * getter for distribution, the percentage of population type who live in
   * the area.
   *
   * @return hashmap of distribution
   */
  public Map<String, Integer> getDistribution() {
    return distribution;
  }


  /**
   * setter for distribution.
   *
   * @param areaDistribution area distribution hashMap
   */
  public void setDistribution(final Map<String, Integer> areaDistribution) {
    this.distribution = (HashMap<String, Integer>) areaDistribution;
  }


  /**
   * getter for area color.
   *
   * @return color
   */
  public Color getColor() {
    return color;
  }


  /**
   * Setter for area color.
   *
   * @param areaColor area color
   */
  public void setColor(final Color areaColor) {
    this.color = areaColor;
  }


  /**
   * get the keys of th distribution hashmap (names of population types).
   *
   * @return String list of  distributionKeys
   */
  public List<String> getDistributionKey() {
    return distributionKey;
  }


  /**
   * setter for distributionKey.
   *
   * @param areaDistributionKey area keys of population types list
   */
  public void setDistributionKey(final List<String> areaDistributionKey) {
    this.distributionKey = areaDistributionKey;
  }

  /**
   * get the destination of the area.
   *
   * @return destination
   */
  public String getDestination() {
    return this.destination;
  }

  /**
   * setter for the destination area.
   *
   * @param areaDestination area main destination
   */
  public void setDestination(final String areaDestination) {
    this.destination = areaDestination;
  }


  /**
   * get latitude of the top left corner.
   *
   * @return latitudeTop
   */
  public double getLatitudeTop() {
    return latitudeTop;
  }


  /**
   * set latitude of the top left corner.
   *
   * @param areaLatitudeTop area top left corner latitude
   */
  public void setLatitudeTop(final double areaLatitudeTop) {
    this.latitudeTop = areaLatitudeTop;
  }


  /**
   * get longitude of the top left corner.
   *
   * @return longitudeTop.
   */
  public double getLongitudeTop() {
    return longitudeTop;
  }


  /**
   * set longitude of the top left corner.
   *
   * @param areaLongitudeTop area top-Left corner longitude
   */
  public void setLongitudeTop(final double areaLongitudeTop) {
    this.longitudeTop = areaLongitudeTop;
  }


  /**
   * get latitude of the bottom right corner.
   *
   * @return latitudeBot
   */
  public double getLatitudeBot() {
    return latitudeBot;
  }


  /**
   * set Latitude of the bottom right corner.
   *
   * @param areaLatitudeBot area bottom-right corner latitude
   */
  public void setLatitudeBot(final double areaLatitudeBot) {
    this.latitudeBot = areaLatitudeBot;
  }


  /**
   * get longitude of the bottom right corner.
   *
   * @return longitudeBot
   */
  public double getLongitudeBot() {
    return longitudeBot;
  }


  /**
   * Set longitude of the bottom right corner.
   *
   * @param areaLongitudeBot area bottom-right corner longitude
   */
  public void setLongitudeBot(final double areaLongitudeBot) {
    this.longitudeBot = areaLongitudeBot;
  }


  /**
   * Update a distribution value and check if the total is under 100 percent.
   *
   * @param key  population type key to update
   * @param part new percentage
   */
  public void setNewPart(final String key, final int part) {
    int total = 0;
    int checkedPart;
    for (String key2 : distributionKey) {
      if (!Objects.equals(key2, key)) {
        total += distribution.get(key2);
      }
    }
    if (total + part > ONE_HUNDRED) {
      checkedPart = ONE_HUNDRED - total;
    } else {
      checkedPart = part;
    }
    distribution.put(key, checkedPart);
  }


  /**
   * same as setNewPart but dont check if the total is under 100 percent.
   *
   * @param key  population type key to update
   * @param part new percentage
   */
  public void addNewPart(final String key, final int part) {
    distribution.put(key, part);
  }

  /**
   * Moves the area of dx and dy and update latitude and longitude in
   * consequences.
   *
   * @param dx deltaX
   * @param dy deltaY
   */
  public void moveArea(final int dx, final int dy) {
    this.posX += dx;
    this.posY += dy;
    this.setPosX(this.posX);
    this.setPosY(this.posY);

    Coordinate latLonTop = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(this.posX, this.posY);
    this.setLatitudeTop(latLonTop.getLat());
    this.setLongitudeTop(latLonTop.getLon());

    Coordinate latLonBot = (Coordinate) MainWindow.getInstance().getMainPanel()
        .getPosition(this.posX + this.width,
            this.posY + this.height);
    this.setLatitudeBot(latLonBot.getLat());
    this.setLongitudeBot(latLonBot.getLon());

  }

  /**
   * extend the left side of the rectangle.
   *
   * @param dx deltaX
   */
  public void extendLeftSide(final int dx) {
    int rightX = this.posX + this.width;
    this.setPosX(this.posX + dx);
    this.setWidth(rightX - this.posX);
  }

  /**
   * extend the right side of the rectangle.
   *
   * @param dx deltaX
   */
  public void extendRightSide(final int dx) {
    this.setWidth(this.width + dx);
  }

  /**
   * extend the top side of the rectangle.
   *
   * @param dy deltaY
   */
  public void extendTopSide(final int dy) {
    int botY = this.posY + this.height;
    this.setPosY(this.posY + dy);
    this.setHeight(botY - this.posY);
  }

  /**
   * extend the bottom side of the rectangle.
   *
   * @param dy delta y
   */
  public void extendBotSide(final int dy) {
    this.setHeight(this.height + dy);
  }


}
