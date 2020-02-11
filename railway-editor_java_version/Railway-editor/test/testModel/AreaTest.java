package testModel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Model.Area;
import data.Data;

public class AreaTest {

	
	
	@Test
	public void testConstructeur() {
		Area area = new Area(0,10,10,20,20);
		assertEquals(0, area.getId());
		assertEquals(10, area.getPosX());
		assertEquals(10, area.getPosY());
		assertEquals(20, area.getWidth());
		assertEquals(20, area.getHeight());
	}
	
	@Test
	public void testSetNewPart() {
		Area area = new Area(0,10,10,20,20);
		area.setNewPart(Data.AREA_TOURIST, 20);
		area.setNewPart(Data.AREA_STUDENT, 20);
		area.setNewPart(Data.AREA_BUSINESSMAN, 20);
		area.setNewPart(Data.AREA_CHILD, 20);
		area.setNewPart(Data.AREA_WORKER, 60);
		
		
		int touristAmount = area.getDistribution().get(Data.AREA_TOURIST);
		int studentAmount = area.getDistribution().get(Data.AREA_STUDENT);
		int businessmannAmount = area.getDistribution().get(Data.AREA_BUSINESSMAN);
		int childAmount = area.getDistribution().get(Data.AREA_CHILD);
		int workerAmount = area.getDistribution().get(Data.AREA_WORKER);
		int unemployedAmount = area.getDistribution().get(Data.AREA_UNEMPLOYED);

		
		assertEquals(20, touristAmount);
		assertEquals(20, studentAmount);
		assertEquals(20, businessmannAmount);
		assertEquals(20, childAmount);
		assertEquals(20, workerAmount);
		assertEquals(0, unemployedAmount);
	}
	
	@Test
	public void testExtend() {
		Area area = new Area(0,10,10,20,20);
		area.extendRightSide(10);
		assertEquals(30, area.getWidth());
		area.extendLeftSide(-10);
		assertEquals(0,area.getPosX());
		assertEquals(40, area.getWidth());
		area.extendBotSide(10);
		assertEquals(30, area.getHeight());
		area.extendTopSide(-10);
		assertEquals(0, area.getPosY());
		assertEquals(40, area.getHeight());
	}
	
	
	
}
