package controller;

import Model.Line;
import Model.Station;
import view.LineView;
import view.MainWindow;
import view.StationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

// TODO : make this class a singleton
public class ActionLine {
  public static final String ADD_LINE = "ADD_LINE";
  public static final String INCREMENT_LINE = "INCREMENT_LINE";
  public static final String DECREMENT_LINE = "DECREMENT_LINE";
  private static ActionLine instance;
  private int lineToUpdateIndex;


  public ActionLine() {
    lineToUpdateIndex = 0;
  }

  /**Create Singleton
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

  public class ActionAddLine extends AbstractAction {

    public ActionAddLine() {
      super(ADD_LINE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
  }

  public class ActionIncrementLine extends AbstractAction {

    public ActionIncrementLine() {
      super(INCREMENT_LINE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
  }

  public class ActionDecrementLine extends AbstractAction {

    public ActionDecrementLine() {
      super(DECREMENT_LINE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
}
