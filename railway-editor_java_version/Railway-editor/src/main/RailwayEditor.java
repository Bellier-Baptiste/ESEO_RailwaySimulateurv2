package main;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import view.MainWindow;

import javax.swing.*;

/**Main class which runs the program.
 * @author arthu
 *
 */
public class RailwayEditor {

	public static void main(String[] args) {
    try {
      FlatArcDarkIJTheme.setup();
      UIManager.put("JXTaskPaneUI", new com.formdev.flatlaf.swingx.ui.FlatTaskPaneUI());
      UIManager.put("JXMonthView", new com.formdev.flatlaf.swingx.ui.FlatMonthViewUI());
      UIManager.put("JXDatePicker", new com.formdev.flatlaf.swingx.ui.FlatDatePickerUI());

    } catch (Exception e) {
      e.printStackTrace();
    }
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainWindow.getInstance().setVisible(true);
      }
    });
	}
}
