package testModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Model.Line;
import Model.Station;

public class LineTest {
	
	public static final String NAME = "eglantine";

	
	
	@Test
	public void testLine() {
		Station station = new Station(0,5,5,NAME);
		List<Station> stations =new ArrayList<>();
		Line line = new Line(0,stations);
		assertEquals(0, line.getId());
		assertTrue(line.getStations().isEmpty());
		line.addStation(station);
		assertEquals(1, line.getStations().size());
	}
	
}
