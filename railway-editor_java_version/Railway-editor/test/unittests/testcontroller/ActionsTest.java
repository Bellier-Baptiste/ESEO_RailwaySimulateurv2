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

package unittests.testcontroller;import controller.ActionArea;
import controller.ActionLine;
import controller.ActionStation;
import model.Line;
import org.junit.Test;
import view.MainWindow;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test-case of adding a line, a station an area and switching between lines.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ActionsTest.java
 * @date N/A
 * @since 2.0
 */
public class ActionsTest {


  @Test
  public void testAddLine() {
    ActionLine actionLine = ActionLine.getInstance();
    actionLine.addLine();
    int nbLineView = MainWindow.getInstance().getMainPanel().getLineViews()
        .size();
    Line line = MainWindow.getInstance().getMainPanel().getLineViews().get(0)
        .getLine();
    assertEquals(1, nbLineView);
    assertEquals(0, line.getId());
    actionLine.addLine();
    nbLineView = MainWindow.getInstance().getMainPanel().getLineViews().size();
    Line line2 = MainWindow.getInstance().getMainPanel().getLineViews()
        .get(nbLineView - 1).getLine();
    assertEquals(2, nbLineView);
    assertEquals(1, line2.getId());
    MainWindow.getInstance().getMainPanel().getLineViews().clear();
  }

  @Test
  public void testAddStation() {
    MainWindow.getInstance().getMainPanel().getLineViews().clear();
    ActionLine actionLine = ActionLine.getInstance();
    actionLine.addLine();

    int nbStationViews = MainWindow.getInstance().getMainPanel().getLineViews()
        .get(0).getStationViews().size();
    int nbLineView = MainWindow.getInstance().getMainPanel().getLineViews()
        .size();
    assertEquals(1, nbLineView);
    assertEquals(0, nbStationViews);
    ActionStation actionStation = ActionStation.getInstance();
    actionStation.addStation();
    nbStationViews = MainWindow.getInstance().getMainPanel().getLineViews()
        .get(0).getStationViews().size();
    assertEquals(1, nbStationViews);
    MainWindow.getInstance().getMainPanel().getLineViews().clear();

  }

  @Test
  public void testAddArea() {
    ActionArea actionArea = new ActionArea();
    int nbAreaView = MainWindow.getInstance().getMainPanel().getAreaViews()
        .size();
    assertEquals(0, nbAreaView);
    actionArea.addArea();
    nbAreaView = MainWindow.getInstance().getMainPanel().getAreaViews().size();
    assertEquals(1, nbAreaView);
    MainWindow.getInstance().getMainPanel().getAreaViews().clear();

  }

  @Test
  public void testLineSwitch() {
    ActionLine actionLine = ActionLine.getInstance();
    actionLine.addLine();
    actionLine.addLine();
    actionLine.addLine();
    int nbLineViews = MainWindow.getInstance().getMainPanel().getLineViews()
        .size();
    assertEquals(3, nbLineViews);
    int currentLineId = MainWindow.getInstance().getMainPanel().getLineViews()
        .get(nbLineViews - 1).getLine().getId();
    assertEquals(2, currentLineId);
    actionLine.incrementLine();
    currentLineId = actionLine.getLineToUpdateIndex();
    assertEquals(2, currentLineId);
    actionLine.decrementLine();
    currentLineId = actionLine.getLineToUpdateIndex();
    assertEquals(1, currentLineId);
    actionLine.incrementLine();
    currentLineId = actionLine.getLineToUpdateIndex();
    assertEquals(2, currentLineId);
  }
}
