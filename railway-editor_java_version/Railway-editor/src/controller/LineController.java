/**
 * Class part of the controller package of the application.
 */

package controller;

import view.LineView;
import view.MainWindow;

/**
 * Controller to add lineView to the mainPanel.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
public class LineController {
  //attributes

  /**
   * Constructor add lineView to the MainPanel.
   *
   * @param lineViewToAdd lineView bound to line
   */
  public LineController(final LineView lineViewToAdd) {
    MainWindow.getInstance().getMainPanel().addLineView(lineViewToAdd);
  }
}
