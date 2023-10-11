package controller;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import view.EventRecap;
import view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class ActionThemeMode {
  //private static final String ACTION_NAME = "⬛ Dark Mode";
  public static final String ACTION_NAME = "Light Mode";
  private static ActionThemeMode instance;
  private final JButton themeMode;

  private boolean isDarkMode = true;


  private ActionThemeMode(JButton themeMode) {
    this.themeMode = themeMode;
  }

  /**
   * Create Singleton
   *
   * @return ActionExport instance
   */
  public static ActionThemeMode getInstance(JButton changeThemeBtn) {
    if (instance == null) {
      instance = new ActionThemeMode(changeThemeBtn);
    }
    return instance;
  }


  public void changeTheme() {
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
    EventRecap.getInstance().eventsListRemoveBackground();
    MainWindow.getInstance().requestFocusInWindow();
  }
}
