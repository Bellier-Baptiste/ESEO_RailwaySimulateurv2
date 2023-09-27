package main;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import view.MainWindow;

/**Main class which runs the program.
 * @author arthu
 *
 */
public class RailwayEditor {

 // public static final String accentColor = "#ff007aff";
//  public static final String lightAccentColor = "#007aff";
//  public static final String darkAccentColor = "#0a84ff";
 // public static final String accentColor = "#f00";

//  public void setAccentColor(String accentColor) {
//    this.accentColor = accentColor;
//  }
//
//  public String getAccentColor() {
//    return accentColor;
//  }

	public static void main(String[] args) {
    try {
      FlatArcDarkIJTheme.setup();
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
