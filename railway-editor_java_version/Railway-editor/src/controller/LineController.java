package controller;

import Model.Line;
import view.LineView;
import view.MainWindow;

/**
 * Controlller to add lineView to the mainPanel.
 * @author arthu
 *
 */
public class LineController {
	
	//attributes
	Line line;
	LineView lineview;
	
	//constructor
	/**Construcor add lineView to the MainPanel.
	 * @param line line to add
	 * @param lineview lineView binded to line
	 */
	public LineController(Line line, LineView lineview) {
		this.line = line;
		this.lineview = lineview;
		MainWindow.getInstance().getMainPanel().addLineView(lineview);
	}
	
	
}
