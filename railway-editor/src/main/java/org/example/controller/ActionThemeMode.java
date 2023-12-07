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

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import org.example.view.EventRecap;
import org.example.view.MainWindow;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.util.Collections;

/**
 * Class to change the application's theme.
 *
 * @author Aurélie Chamouleau
 * @file ActionThemeMode.java
 * @date 2023/09/22
 * @since 3.0
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
