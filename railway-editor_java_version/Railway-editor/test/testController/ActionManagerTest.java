package testController;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import Model.Line;
import controller.ActionManager;
import view.MainWindow;

public class ActionManagerTest {

	@Test
	public void testAddLine() {
		ActionManager actionManager = new ActionManager();
		actionManager.addLine();
		int nbLineView = MainWindow.getInstance().getMainPanel().getLineViews().size();
		Line line  = MainWindow.getInstance().getMainPanel().getLineViews().get(0).getLine();	
		assertEquals(1, nbLineView);
		assertEquals(0, line.getId());
		actionManager.addLine();
		nbLineView = MainWindow.getInstance().getMainPanel().getLineViews().size();
		Line line2  = MainWindow.getInstance().getMainPanel().getLineViews().get(nbLineView-1).getLine();
		assertEquals(2, nbLineView);
		assertEquals(1, line2.getId());
		MainWindow.getInstance().getMainPanel().getLineViews().clear();
	}
	
	@Test
	public void testAddStation() {
		MainWindow.getInstance().getMainPanel().getLineViews().clear();
		ActionManager actionManager = new ActionManager();
		actionManager.addLine();

		int nbStationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(0).getStationViews().size();
		int nbLineView = MainWindow.getInstance().getMainPanel().getLineViews().size();
		assertEquals(1,nbLineView);
		assertEquals(0, nbStationViews);
		actionManager.addStation();
		nbStationViews = MainWindow.getInstance().getMainPanel().getLineViews().get(0).getStationViews().size();
		assertEquals(1, nbStationViews);
		MainWindow.getInstance().getMainPanel().getLineViews().clear();

	}
	
	@Test
	public void testAddArea() {
		ActionManager actionManager = new ActionManager();
		int nbAreaView = MainWindow.getInstance().getMainPanel().getAreaViews().size();
		assertEquals(0, nbAreaView);
		actionManager.addArea();
		nbAreaView = MainWindow.getInstance().getMainPanel().getAreaViews().size();
		assertEquals(1, nbAreaView);
		 MainWindow.getInstance().getMainPanel().getAreaViews().clear();

	}
	
	@Test
	public void testLineSwitch() {
		ActionManager actionManager = new ActionManager();
		actionManager.addLine();
		actionManager.addLine();
		actionManager.addLine();
		int nbLineViews = MainWindow.getInstance().getMainPanel().getLineViews().size();
		assertEquals(3, nbLineViews);
		int currentLineId =  MainWindow.getInstance().getMainPanel().getLineViews().get(nbLineViews-1).getLine().getId();
		assertEquals(2,currentLineId);
		actionManager.incrementLine();
		currentLineId = actionManager.getLineToUpdateIndex();
		assertEquals(2,currentLineId);
		actionManager.decrementLine();
		currentLineId =  actionManager.getLineToUpdateIndex();
		assertEquals(1,currentLineId);
		actionManager.incrementLine();
		currentLineId = actionManager.getLineToUpdateIndex();
		assertEquals(2,currentLineId);
	}
}
