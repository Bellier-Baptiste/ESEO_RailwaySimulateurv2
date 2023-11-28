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

package org.example.model;

import org.example.data.Data;
import org.example.view.MainPanel;
import org.example.view.MainWindow;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Model Class to describe an Area.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file Area.java
 * @date N/A
 * @since 2.0
 */
public class Area {
  /** Area opacity. */
  public static final float AREA_OPACITY = 0.5f;
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

  /** Area population distribution. */
  private HashMap<String, Integer> distributionPopulation;
  /** Area destination distribution. */
  private HashMap<String, Integer> distributionDestination;
  // Init the value at 100 / number of fields for the distribution (7 here)
  /** Default value 14 of some fields. */
  private static final int DEFAULT_VALUE_14 = 14;
  /** Default value 15 of some fields. */
  private static final int DEFAULT_VALUE_15 = 15;
  /** Area population destination distribution default values. */
  protected static final Map<String, Integer> DEFAULT_DISTRIBUTION_DESTINATION =
      new HashMap<>();
  /** Area population population distribution default values. */
  protected static final Map<String, Integer> DEFAULT_DISTRIBUTION_POPULATION =
      new HashMap<>();

  static {
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_WORKER, DEFAULT_VALUE_15);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_STUDENT, DEFAULT_VALUE_15);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_TOURIST, DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_BUSINESSMAN,
        DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_CHILD, DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_RETIRED, DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_POPULATION.put(Data.AREA_UNEMPLOYED, DEFAULT_VALUE_14);

    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_OFFICE, DEFAULT_VALUE_15);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_COMMERCIAL,
        DEFAULT_VALUE_15);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_RESIDENTIAL,
        DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_INDUSTRIAL,
        DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_TOURISTIC, DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_LEISURE, DEFAULT_VALUE_14);
    DEFAULT_DISTRIBUTION_DESTINATION.put(Data.AREA_EDUCATIONAL,
        DEFAULT_VALUE_14);
  }

  /** Area color. */
  private Color color;
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
  /** Random generator. */
  private final Random rand = new Random();


  /**
   * Area constructor when creating a new area.
   *
   * @param areaPosX   area positionX
   * @param areaPosY   area positionY
   * @param areaWidth  area Width
   * @param areaHeight area height
   */
  public Area(final int areaPosX, final int areaPosY,
              final int areaWidth, final int areaHeight) {
    this.posX = areaPosX;
    this.posY = areaPosY;
    this.width = areaWidth;
    this.height = areaHeight;
    ICoordinate geoPosTopLeft = MainPanel.getInstance().getPosition(this.posX,
        this.posY);
    ICoordinate geoPosBotRight = MainPanel.getInstance().getPosition(this.posX
        + this.width, this.posY + this.height);
    this.latitudeTop = geoPosTopLeft.getLat();
    this.longitudeTop = geoPosTopLeft.getLon();
    this.latitudeBot = geoPosBotRight.getLat();
    this.longitudeBot = geoPosBotRight.getLon();
    this.initDistributions();
    this.initColor();
  }

  /**
   * Area constructor when importing xml file.
   *
   * @param latitudeTopToSet latitude of the top left corner
   * @param longitudeTopToSet longitude of the top left corner
   * @param latitudeBotToSet latitude of the bottom right corner
   * @param longitudeBotToSet longitude of the bottom right corner
   */
  public Area(final double latitudeTopToSet, final double longitudeTopToSet,
              final double latitudeBotToSet, final double longitudeBotToSet) {
    this.initDistributions();
    this.initColor();
    this.latitudeBot = latitudeBotToSet;
    this.latitudeTop = latitudeTopToSet;
    this.longitudeBot = longitudeBotToSet;
    this.longitudeTop = longitudeTopToSet;
    Point posTopLeft = MainPanel.getInstance().getMapPosition(latitudeTop,
        longitudeTop);
    Point posBotRight = MainPanel.getInstance().getMapPosition(latitudeBot,
        longitudeBot);
    if (posTopLeft == null || posBotRight == null) {
      return;
    }
    this.posX = posTopLeft.x;
    this.posY = posTopLeft.y;
    this.width = posBotRight.x - posTopLeft.x;
    this.height = posBotRight.y - posTopLeft.y;
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
   * getter for distribution, the percentage of population type who live in the
   * area.
   *
   * @return hashmap of population distribution
   */
  public Map<String, Integer> getDistributionPopulation() {
    return distributionPopulation;
  }

  /**
   * getter for distribution, the percentage of destination type where people go
   * to.
   *
   * @return hashmap of destination distribution
   */
  public Map<String, Integer> getDistributionDestination() {
    return distributionDestination;
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
   * Update the population distribution value and check if the total is under
   * 100 percent.
   *
   * @param key  population type key to update
   * @param part new percentage
   */
  public void setNewPopulationPart(final String key, final int part) {
    this.distributionPopulation.put(key, part);
  }

  /**
   * Update the destination distribution value and check if the total is under
   * 100 percent.
   *
   * @param key  destination type key to update
   * @param part new percentage
   */
  public void setNewDestinationPart(final String key, final int part) {
    this.distributionDestination.put(key, part);
  }

  /**
   * Get the default value of the destination distribution key.
   *
   * @param key destination distribution key
   *
   * @return default value for the given key
   */
  public static int getDefaultDestinationDistribution(final String key) {
    return DEFAULT_DISTRIBUTION_DESTINATION.get(key);
  }

  /**
   * Get the default value of the population distribution key.
   *
   * @param key population distribution key
   *
   * @return default value for the given key
   */
  public static int getDefaultPopulationDistribution(final String key) {
    return DEFAULT_DISTRIBUTION_POPULATION.get(key);
  }

  private void initDistributions() {
    this.distributionPopulation = new HashMap<>();
    distributionPopulation.put(Data.AREA_WORKER,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_WORKER));
    distributionPopulation.put(Data.AREA_STUDENT,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_STUDENT));
    distributionPopulation.put(Data.AREA_TOURIST,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_TOURIST));
    distributionPopulation.put(Data.AREA_BUSINESSMAN,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_BUSINESSMAN));
    distributionPopulation.put(Data.AREA_CHILD,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_CHILD));
    distributionPopulation.put(Data.AREA_RETIRED,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_RETIRED));
    distributionPopulation.put(Data.AREA_UNEMPLOYED,
        DEFAULT_DISTRIBUTION_POPULATION.get(Data.AREA_UNEMPLOYED));

    this.distributionDestination = new HashMap<>();

    distributionDestination.put(Data.AREA_OFFICE,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_OFFICE));
    distributionDestination.put(Data.AREA_COMMERCIAL,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_COMMERCIAL));
    distributionDestination.put(Data.AREA_RESIDENTIAL,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_RESIDENTIAL));
    distributionDestination.put(Data.AREA_INDUSTRIAL,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_INDUSTRIAL));
    distributionDestination.put(Data.AREA_TOURISTIC,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_TOURISTIC));
    distributionDestination.put(Data.AREA_LEISURE,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_LEISURE));
    distributionDestination.put(Data.AREA_EDUCATIONAL,
        DEFAULT_DISTRIBUTION_DESTINATION.get(Data.AREA_EDUCATIONAL));
  }

  private void initColor() {
    float r = this.rand.nextFloat();
    float g = this.rand.nextFloat();
    float b = this.rand.nextFloat();
    this.color = new Color(r, g, b, AREA_OPACITY);
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
