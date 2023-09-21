package controller;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import main.RailwayEditor;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class ActionThemeMode extends  AbstractAction{
  //private static final String ACTION_NAME = "⬛ Dark Mode";
  private static final String ACTION_NAME = "Light Mode";
  private MainWindow mainWindow;
  private JButton themeMode;

  private boolean isDarkMode = true;


  public ActionThemeMode(MainWindow mainWindow, JButton themeMode) {
    super(ACTION_NAME);
    this.mainWindow = mainWindow;
    this.themeMode = themeMode;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    // Change the theme according to the current state
    if (this.isDarkMode) {
      FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", "#007aff") );
      FlatArcIJTheme.setup();
      themeMode.setText("Dark Mode");
    } else {
      FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", "#0a84ff") );
      FlatArcDarkIJTheme.setup();
      themeMode.setText("Light Mode");
    }
    this.isDarkMode = !this.isDarkMode; // Toggle the state
    // Refresh the components to apply the new theme
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    MainWindow.getInstance().requestFocusInWindow();
  }
}
