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
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.example.view.MainWindow;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
  /** Area population distribution. */
  private HashMap<String, Integer> distributionPopulation;
  /** Area destination distribution. */
  private HashMap<String, Integer> distributionDestination;
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
   * @param areaPosX   area positionX
   * @param areaPosY   area positionY
   * @param areaWidth  area Width
   * @param areaHeight area height
   */
  public Area(final int areaPosX, final int areaPosY,
              final int areaWidth, final int areaHeight) {
    super();
    this.posX = areaPosX;
    this.posY = areaPosY;
    this.width = areaWidth;
    this.height = areaHeight;
    this.distributionPopulation = new HashMap<>();

    distributionPopulation.put(Data.AREA_TOURIST, 0);
    distributionPopulation.put(Data.AREA_STUDENT, 0);
    distributionPopulation.put(Data.AREA_BUSINESSMAN, 0);
    distributionPopulation.put(Data.AREA_WORKER, 0);
    distributionPopulation.put(Data.AREA_CHILD, 0);
    distributionPopulation.put(Data.AREA_RETIRED, 0);
    distributionPopulation.put(Data.AREA_UNEMPLOYED, 0);

    this.distributionDestination = new HashMap<>();

    distributionDestination.put(Data.AREA_RESIDENTIAL, 0);
    distributionDestination.put(Data.AREA_COMMERCIAL, 0);
    distributionDestination.put(Data.AREA_OFFICE, 0);
    distributionDestination.put(Data.AREA_INDUSTRIAL, 0);
    distributionDestination.put(Data.AREA_TOURISTIC, 0);
    distributionDestination.put(Data.AREA_LEISURE, 0);
    distributionDestination.put(Data.AREA_EDUCATIONAL, 0);
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
   * @return hashmap of population distribution
   */
  public Map<String, Integer> getDistributionPopulation() {
    return distributionPopulation;
  }

  /**
   * getter for distribution, the percentage of destination type where people
   * go to.
   *
   * @return hashmap of destination distribution
   */
  public Map<String, Integer> getDistributionDestination() {
    return distributionDestination;
  }

  /**
   * setter for distribution.
   *
   * @param areaDistribution area distribution hashMap
   */
  public void setDistribution(final Map<String, Integer> areaDistribution) {
    this.distributionPopulation = (HashMap<String, Integer>) areaDistribution;
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
   * Update the population distribution value and check if the total is under
   * 100 percent.
   *
   * @param key  population type key to update
   * @param part new percentage
   */
  public void setNewPopulationPart(final String key, final int part) {
    int total = 0;
    int checkedPart;
    for (Map.Entry<String, Integer> entry : distributionPopulation
        .entrySet()) {
      if (!Objects.equals(entry.getKey(), key)) {
        total += distributionPopulation.get(entry.getKey());
      }
    }
    if (total + part > ONE_HUNDRED) {
      checkedPart = ONE_HUNDRED - total;
    } else {
      checkedPart = part;
    }
    distributionPopulation.put(key, checkedPart);
  }

  /**
   * Update the destination distribution value and check if the total is under
   * 100 percent.
   *
   * @param key  destination type key to update
   * @param part new percentage
   */
  public void setNewDestinationPart(final String key, final int part) {
    int total = 0;
    int checkedPart;
    for (Map.Entry<String, Integer> entry : distributionDestination
        .entrySet()) {
      if (!Objects.equals(entry.getKey(), key)) {
        total += distributionDestination.get(entry.getKey());
      }
    }
    if (total + part > ONE_HUNDRED) {
      checkedPart = ONE_HUNDRED - total;
    } else {
      checkedPart = part;
    }
    distributionDestination.put(key, checkedPart);
  }

  /**
   * same as setNewPart but dont check if the total is under 100 percent.
   *
   * @param key  population type key to update
   * @param part new percentage
   */
  public void addNewPart(final String key, final int part) {
    distributionPopulation.put(key, part);
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
