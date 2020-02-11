package Model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import data.Data;
import view.MainWindow;

/** Model Class to describe an Area.
 * @author arthu
 *
 */
public class Area {

	private int posX;
	private int posY;
	private int id;
	private int width;
	private int height;
	private HashMap<String,Integer> distribution;
	private List<String> distributionKey;
	private Color color;
	private String destination;
	
	//top left corner
	private double latitudeTop;
	private double longitudeTop;
	
	//bottom right corner
	private double latitudeBot;
	private double longitudeBot;

	
	/**Constructor.
	 * @param id area id
	 * @param posX area positionX
	 * @param posY area positionY
	 * @param width area width
	 * @param height area height
	 */
	public Area(int id,int posX, int posY, int width, int height) {
		super();
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.distribution = new HashMap<String, Integer>();
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
		this.color = new Color(r,g,b,0.5f);
	}

	
	/**getter of id.
	 * @return id
	 */
	public int getId() {
		return id;
	}


	/**setter of id.
	 * @param id areaId
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**getter of posX.
	 * @return top left corner Xposition
	 */
	public int getPosX() {
		return posX;
	}


	/** setter of posX.
	 * @param posX area positionX
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}


	/**getter of posY.
	 * @return top left corner Yposition
	 */
	public int getPosY() {
		return posY;
	}


	/**setter of posY.
	 * @param posY area positionY
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}


	/** getter for width.
	 * @return with of the area rectangle
	 */
	public int getWidth() {
		return width;
	}


	/**setter for width.
	 * @param width area width
	 */
	public void setWidth(int width) {
		this.width = width;
	}


	/**getter for height.
	 * @return height of the area rectangle
	 */
	public int getHeight() {
		return height;
	}


	/** setter for height.
	 * @param height area height
	 */
	public void setHeight(int height) {
		this.height = height;
	}


	/**getter for distribution, the percentage of population type who live in the area.
	 * @return hashmap of distribution
	 */
	public HashMap<String, Integer> getDistribution() {
		return distribution;
	}


	/**setter for distribution.
	 * @param distribution area distribution hashMap
	 */
	public void setDistribution(HashMap<String, Integer> distribution) {
		this.distribution = distribution;
	}
	
	
	
	
	/**getter for area color.
	 * @return color
	 */
	public Color getColor() {
		return color;
	}


	/**setter for area color
	 * @param color area color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	

	/** get the keys of th distribution hashmap (names of population types).
	 * @return String list of  distributionKeys
	 */
	public List<String> getDistributionKey() {
		return distributionKey;
	}


	/** setter for distributionKey.
	 * @param distributionKey area keys of poupulation types list
	 */
	public void setDistributionKey(List<String> distributionKey) {
		this.distributionKey = distributionKey;
	}
	
	/** get the destination of the area.
	 * @return destination
	 */
	public String getDestination() {
		return this.destination;
	}
	
	/** seter for the destination area.
	 * @param destination area main destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	

	/**get latitude of the top left corner.
	 * @return latitudeTop
	 */
	public double getLatitudeTop() {
		return latitudeTop;
	}


	/**set latitude of the top left corner.
	 * @param latitudeTop area top left corner latitude
	 */
	public void setLatitudeTop(double latitudeTop) {
		this.latitudeTop = latitudeTop;
	}


	/**get longitude of the top left corner.
	 * @return longitudeTop.
	 */
	public double getLongitudeTop() {
		return longitudeTop;
	}


	/**set longitude of the top left corner.
	 * @param longitudeTop area top-Left corner longitude
	 */
	public void setLongitudeTop(double longitudeTop) {
		this.longitudeTop = longitudeTop;
	}


	/**get latitude of the bottom right corner.
	 * @return latitudeBot
	 */
	public double getLatitudeBot() {
		return latitudeBot;
	}


	/**set Latitude of the bottom right corner.
	 * @param latitudeBot area bottom-right corner latitude
	 */
	public void setLatitudeBot(double latitudeBot) {
		this.latitudeBot = latitudeBot;
	}


	/** get longitude of the bottom right corner.
	 * @return longitudeBot
	 */
	public double getLongitudeBot() {
		return longitudeBot;
	}


	/**set longitude of the bottom right corner
	 * @param longitudeBot area bottom-right corner longitude
	 */
	public void setLongitudeBot(double longitudeBot) {
		this.longitudeBot = longitudeBot;
	}


	/**Update a distribution value and check if the total is under 100 percent
	 * @param key population type key to update
	 * @param part new percentage
	 */
	public void setNewPart(String key, int part) {
		int total = 0;
		int checkedPart = 0;
		for (String key2: distributionKey) {
			if (key2!=key) {
				total+= distribution.get(key2);
			}
		}
		if (total+part>100) {
			checkedPart = 100-total;
		}else {
			checkedPart = part;
		}
		distribution.put(key, checkedPart);
	}
	
	
	/** same as setNewPart but dont check if the total is under 100 percent.
	 *@param key population type key to update
	 * @param part new percentage
	 */
	public void addNewPart(String key,int part) {
		distribution.put(key, part);
	}
	
	/**Moves the area of dx and dy and update latitude and longitude in consequences.
	 * @param dx deltaX
	 * @param dy deltaY
	 */
	public void moveArea(int dx,int dy) {
		this.setPosX(this.posX+=dx);
		this.setPosY(this.posY+=dy);
		
		Coordinate latLonTop = MainWindow.getInstance().getMainPanel().getPosition(this.posX, this.posY);
		this.setLatitudeTop(latLonTop.getLat());
		this.setLongitudeTop(latLonTop.getLon());
		
		Coordinate latLonBot = MainWindow.getInstance().getMainPanel().getPosition(this.posX+this.width, this.posY+this.height);
		this.setLatitudeBot(latLonBot.getLat());
		this.setLongitudeBot(latLonBot.getLon());

	}
	
	/**extend the left side of the rectangle. 
	 * @param dx deltaX
	 */
	public void extendLeftSide(int dx){
		int rightX = this.posX+this.width;
		this.setPosX(this.posX+dx);
		this.setWidth(rightX-this.posX);
	}
	/**extend the right side of the rectangle. 
	 * @param dx deltaX
	 */
	public void extendRightSide(int dx) {
		this.setWidth(this.width+dx);
	}
	
	/**extend the top side of the rectangle.
	 * @param dy deltaY
	 */
	public void extendTopSide(int dy){
		int botY = this.posY+this.height;
		this.setPosY(this.posY+dy);
		this.setHeight(botY-this.posY);
	}
	/**extend the bottom side of the rectangle. 
	 * @param dY deltaY
	 */
	public void extendBotSide(int dY) {
		this.setHeight(this.height+dY);
	}
	
	
}
