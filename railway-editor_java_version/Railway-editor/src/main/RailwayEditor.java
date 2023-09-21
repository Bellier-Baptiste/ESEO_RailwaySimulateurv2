package main;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import view.MainWindow;

import java.util.Collections;

/**Main class which runs the program.
 * @author arthu
 *
 */
public class RailwayEditor {

 // public static final String accentColor = "#ff007aff";
  public static final String lightAccentColor = "#007aff";
  public static final String darkAccentColor = "#0a84ff";
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
