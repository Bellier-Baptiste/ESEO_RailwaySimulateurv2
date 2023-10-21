package testModel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Station;

public class StationTest {
	public static final String NAME = "eglantine";


	@Test
	public void testConstructeur() {
		Station station = new Station(0,5,5,NAME);
		assertEquals(0, station.getId());
		assertEquals(5, station.getPosX());
		assertEquals(5, station.getPosY());
		assertEquals("eglantine", station.getName());
	}
	
	@Test
	public void testMoveStation() {
		Station station = new Station(0,5,5,NAME);
		station.moveStation(3, 3);
		assertEquals(8, station.getPosX());
		assertEquals(8, station.getPosY());	}
}
