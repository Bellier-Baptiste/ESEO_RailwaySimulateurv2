/**
 * Class part of the main package of the application.
 */

package main;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import view.MainWindow;

import javax.swing.UIManager;

/**
 * Main class which runs the program.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
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
