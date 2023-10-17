package testmodel;

import model.Line;
import model.Station;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test-case of {@link Line} model.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file LineTest.java
 * @date N/A
 * @since 2.0
 */
public class LineTest {

  public static final String NAME = "eglantine";

  @Test
  public void testLine() {
    Station station = new Station(0, 5, 5, NAME);
    List<Station> stations = new ArrayList<>();
    Line line = new Line(0, stations);
    assertEquals(0, line.getId());
    assertTrue(line.getStations().isEmpty());
    line.addStation(station);
    assertEquals(1, line.getStations().size());
  }
}
