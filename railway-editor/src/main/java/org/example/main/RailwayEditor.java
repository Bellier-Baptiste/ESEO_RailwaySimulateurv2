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

package org.example.main;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import org.example.view.MainWindow;

import javax.swing.UIManager;

/**
 * Main class which runs the program.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file RailwayEditor.java
 * @date N/A
 * @since 2.0
 */
public final class RailwayEditor {

  // Private constructor to prevent instantiation
  private RailwayEditor() {
    // throw an exception if this ever *is* called
    throw new AssertionError("Instantiating utility class.");
  }

  /**
   * Main function.
   *
   * @param args arguments
   */
  public static void main(final String[] args) {
    try {
      FlatArcDarkIJTheme.setup();
      UIManager.put("JXTaskPaneUI", new com.formdev.flatlaf.swingx.ui
          .FlatTaskPaneUI());
      UIManager.put("JXMonthView", new com.formdev.flatlaf.swingx.ui
          .FlatMonthViewUI());
      UIManager.put("JXDatePicker", new com.formdev.flatlaf.swingx.ui
          .FlatDatePickerUI());

    } catch (Exception e) {
      e.printStackTrace();
    }
    java.awt.EventQueue.invokeLater(() -> MainWindow.getInstance()
        .setVisible(true));
  }
}
