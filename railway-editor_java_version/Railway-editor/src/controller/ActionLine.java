package controller;

import model.Line;
import model.Station;
import view.LineView;
import view.MainWindow;
import view.StationView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// TODO : make this class a singleton
public class ActionLine {
  public static final String ADD_LINE = "ADD_LINE";
  public static final String INCREMENT_LINE = "INCREMENT_LINE";
  public static final String DECREMENT_LINE = "DECREMENT_LINE";
  private static ActionLine instance;
  private int lineToUpdateIndex;


  private ActionLine() {
    lineToUpdateIndex = 0;
  }

  /**
   * Create Singleton
   *
   * @return ActionLine instance
   */
  public static ActionLine getInstance() {
    if (instance == null) {
      instance = new ActionLine();
    }
    return instance;
  }

  public int getLineToUpdateIndex() {
    return lineToUpdateIndex;
  }

  private void setLineToUpdateIndex(int lineToUpdateIndex) {
    this.lineToUpdateIndex = lineToUpdateIndex;
  }

  public void addLine() {
    int lineIndex = MainWindow.getInstance().getMainPanel().getLineViews().size(); // find the new line number
    ActionLine.this.setLineToUpdateIndex(lineIndex);
    Line line = new Line(lineIndex, new ArrayList<Station>()); // create a new line (model)
    List<StationView> stationsViews = new ArrayList<StationView>(); // create a new vie for the new line's sattions
    LineView lineview = new LineView(line, stationsViews);
    @SuppressWarnings("unused")
    LineController lineController = new LineController(line, lineview);
    MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(lineIndex));// change line id
    // displayed in the
    // toolBar Panel
    MainWindow.getInstance().getMainPanel().repaint();
  }

  public void incrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();

    if (!lineId.getText().equals("none")) { // if a line exists
      int currentLineId = Integer.valueOf(lineId.getText());
      if (currentLineId < MainWindow.getInstance().getMainPanel().getLineViews().size() - 1) {// if this is not
        // the last line created
        MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(currentLineId + 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId + 1);
      }
    }
  }

  public void decrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanelIdea2().getLineId();

    if (!lineId.getText().equals("none")) {// if a line exists
      int currentLineId = Integer.valueOf(lineId.getText());
      if (currentLineId > 0) {// if this is not the first line created
        MainWindow.getInstance().getToolBarPanelIdea2().getLineId().setText(Integer.toString(currentLineId - 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId - 1);
      }
    }
  }
}


