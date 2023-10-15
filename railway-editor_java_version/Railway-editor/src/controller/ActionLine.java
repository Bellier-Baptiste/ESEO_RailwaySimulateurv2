/**
 * Class part of the controller package of the application.
 */

package controller;

import model.Line;
import view.LineView;
import view.MainWindow;
import view.StationView;

import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage the actions on the lines.
 */
public final class ActionLine {
  /** String of the add action name. */
  public static final String ADD_LINE = "ADD_LINE";
  /** String of the increment action name. */
  public static final String INCREMENT_LINE = "INCREMENT_LINE";
  /** String of the decrement action name. */
  public static final String DECREMENT_LINE = "DECREMENT_LINE";
  /** Singleton instance of the class. */
  private static ActionLine instance;
  /** Index of the line to update. */
  private int lineToUpdateIndex;


  /**
   * Constructor of the class.
   */
  private ActionLine() {
    lineToUpdateIndex = 0;
  }

  /**
   * Return Singleton.
   *
   * @return ActionLine instance
   */
  public static ActionLine getInstance() {
    if (instance == null) {
      instance = new ActionLine();
    }
    return instance;
  }

  /**
   * Getter of the line to update index.
   *
   * @return line to update index
   */
  public int getLineToUpdateIndex() {
    return lineToUpdateIndex;
  }

  /**
   * Setter of the line to update index.
   *
   * @param lineToUpdateIndexToSet line to update index
   */
  private void setLineToUpdateIndex(final int lineToUpdateIndexToSet) {
    this.lineToUpdateIndex = lineToUpdateIndexToSet;
  }

  /**
   * Function to add a new line.
   */
  public void addLine() {
    int lineIndex = MainWindow.getInstance().getMainPanel().getLineViews()
        .size(); // find the new line number
    ActionLine.this.setLineToUpdateIndex(lineIndex);
    // create a new line (model)
    Line line = new Line(lineIndex, new ArrayList<>());
    // create a new vie for the new line's sattions
    List<StationView> stationsViews = new ArrayList<>();
    LineView lineview = new LineView(line, stationsViews);
    @SuppressWarnings("unused")
    LineController lineController = new LineController(line, lineview);
    MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(
        Integer.toString(lineIndex)); // change line id
    // displayed in the
    // toolBar Panel
    MainWindow.getInstance().getMainPanel().repaint();
  }

  /**
   * Function to select the upper line.
   */
  public void incrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();

    if (!lineId.getText().equals("none")) { // if a line exists
      int currentLineId = Integer.parseInt(lineId.getText());
      if (currentLineId < MainWindow.getInstance().getMainPanel().getLineViews()
          .size() - 1) { // if this is not
        // the last line created
        MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(
            Integer.toString(currentLineId + 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId + 1);
      }
    }
  }

  /**
   * Function to select the lower line.
   */
  public void decrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();

    if (!lineId.getText().equals("none")) { // if a line exists
      int currentLineId = Integer.parseInt(lineId.getText());
      if (currentLineId > 0) { // if this is not the first line created
        MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(
            Integer.toString(currentLineId - 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId - 1);
      }
    }
  }
}


