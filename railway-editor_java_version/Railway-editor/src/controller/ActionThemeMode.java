/**
 * Class part of the controller package of the application.
 */

package controller;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import view.EventRecap;
import view.MainWindow;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.util.Collections;

/**
 * Class to change the application's theme.
 */
public final class ActionThemeMode {
  /** String of the button to change the theme to light mode. */
  public static final String LIGHT_MODE = "Light Mode";
  /** String of the button to change the theme to dark mode. */
  public static final String DARK_MODE = "Dark Mode";
  /** Singleton instance of the class. */
  private static ActionThemeMode instance;
  /** Button to change the theme. */
  private final JButton themeMode;
  /** Boolean to know if the current theme is dark or light. */
  private boolean isDarkMode = true;


  /**
   * Constructor of the class.
   *
   * @param themeModeBtn button to change the theme
   */
  private ActionThemeMode(final JButton themeModeBtn) {
    this.themeMode = themeModeBtn;
  }

  /**
   * Return Singleton.
   *
   * @param changeThemeBtn button to change the theme
   *
   * @return ActionExport instance
   */
  public static ActionThemeMode getInstance(final JButton changeThemeBtn) {
    if (instance == null) {
      instance = new ActionThemeMode(changeThemeBtn);
    }
    return instance;
  }


  /**
   * Change the theme of the application.
   */
  public void changeTheme() {
    // Change the theme according to the current state
    if (this.isDarkMode) {
      FlatLaf.setGlobalExtraDefaults(Collections.singletonMap(
          "@accentColor", "#007aff"));
      FlatArcIJTheme.setup();
      themeMode.setText(DARK_MODE);
    } else {
      FlatLaf.setGlobalExtraDefaults(Collections.singletonMap(
          "@accentColor", "#0a84ff"));
      FlatArcDarkIJTheme.setup();
      themeMode.setText(LIGHT_MODE);
    }
    this.isDarkMode = !this.isDarkMode; // Toggle the state
    // Refresh the components to apply the new theme
    SwingUtilities.updateComponentTreeUI(MainWindow.getInstance());
    EventRecap.getInstance().eventsListRemoveBackground();
    MainWindow.getInstance().requestFocusInWindow();
  }
}
