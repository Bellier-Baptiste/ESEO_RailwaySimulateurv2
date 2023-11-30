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

package org.example.controller;

import org.example.model.Line;
import org.example.view.LineView;
import org.example.view.MainWindow;
import org.example.view.StationView;

import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for performing actions on the lines {@link org.example.model.Line}.
 * Linked to buttons in {@link org.example.view.ToolBarPanel}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file ActionLine.java
 * @date 2023-10-02
 * @since 3.0
 */
public final class ActionLine {
  /** String of the add action name. */
  public static final String ADD_LINE = "ADD_LINE";
  /** String of the increment action name. */
  public static final String INCREMENT_LINE = "INCREMENT_LINE";
  /** String of the decrement action name. */
  public static final String DECREMENT_LINE = "DECREMENT_LINE";
  /** String of the delete action name. */
  public static final String DELETE_LINE = "DELETE_LINE";
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
   * Creates a {@link Line} with its linked {@link LineView} created and
   * add this {@link LineView} to the {@link org.example.view.MainPanel} thanks
   * to {@link LineController}.
   */
  public void addLine() {
    int lineIndex = MainWindow.getInstance().getMainPanel().getLineViews()
        .size(); // find the new line number
    ActionLine.this.setLineToUpdateIndex(lineIndex);
    // create a new line (model)
    Line line = new Line(lineIndex, new ArrayList<>());
    // create a new vie for the new line's stations
    List<StationView> stationsViews = new ArrayList<>();
    LineView lineview = new LineView(line, stationsViews);
    @SuppressWarnings("unused")
    LineController lineController = new LineController(lineview);
    MainWindow.getInstance().getToolBarPanel().getLineId().setText(
        Integer.toString(lineIndex)); // change line id
    // displayed in the
    // toolBar Panel
    MainWindow.getInstance().getMainPanel().repaint();
  }

  /**
   * Function to select the upper line.
   */
  public void incrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanel().getLineId();

    if (!lineId.getText().equals("none")) { // if a line exists
      int currentLineId = Integer.parseInt(lineId.getText());
      if (currentLineId < MainWindow.getInstance().getMainPanel().getLineViews()
          .size() - 1) { // if this is not
        // the last line created
        MainWindow.getInstance().getToolBarPanel().getLineId().setText(
            Integer.toString(currentLineId + 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId + 1);
      }
    }
  }

  /**
   * Function to select the lower line.
   */
  public void decrementLine() {
    JLabel lineId = MainWindow.getInstance().getToolBarPanel().getLineId();

    if (!lineId.getText().equals("none")) { // if a line exists
      int currentLineId = Integer.parseInt(lineId.getText());
      if (currentLineId > 0) { // if this is not the first line created
        MainWindow.getInstance().getToolBarPanel().getLineId().setText(
            Integer.toString(currentLineId - 1));
        ActionLine.this.setLineToUpdateIndex(currentLineId - 1);
      }
    }
  }
}


