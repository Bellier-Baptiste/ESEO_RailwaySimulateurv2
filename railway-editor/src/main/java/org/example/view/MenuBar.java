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

package org.example.view;

import org.example.controller.ActionArchive;
import org.example.controller.ActionConfiguration;
import org.example.controller.ActionFile;
import org.example.controller.ActionThemeMode;

import java.awt.ComponentOrientation;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * {@link JMenuBar} of the application with file menu and configuration menu.
 *
 * @author Aurélie CHAMOULEAU
 * @author Benoît VAVASSEUR
 * @file MenuBar.java
 * @date 2023-09-22
 * @since 3.0
 */
public final class MenuBar extends JMenuBar {
  /**
   * Singleton instance.
   */
  private static JMenuBar instance;
  /**
   * Text to display on the export menu item.
   */
  private static final String EXPORT_TEXT = "Export";

  /**
   * Private constructor.
   */
  private MenuBar() {
    // Menu bar properties
    this.add(Box.createHorizontalGlue());
    this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    // File menu
    JMenu file = new JMenu("File");
    JMenuItem openMap = new JMenuItem("Open");
    openMap.setName(ActionFile.IMPORT_NAME);
    openMap.addActionListener(e ->
      ActionFile.getInstance().showOpenDialogXML());
    file.add(openMap);
    JMenuItem save = new JMenuItem("Save");
    save.setName("Save");
    file.add(save);
    JMenuItem export = new JMenuItem(EXPORT_TEXT);
    export.addActionListener(e ->
      ActionFile.getInstance().showExportDialogXML());
    export.setName(ActionFile.EXPORT_NAME);
    file.add(export);
    this.add(file);

    // Configuration menu
    JMenu config = new JMenu("Configuration");
    JMenuItem editConfig = new JMenuItem("Edit");
    editConfig.setName("Edit");
    editConfig.addActionListener(e -> new EditConfigDialog());
    config.add(editConfig);
    JMenuItem openConfig = new JMenuItem("Open");
    openConfig.setName("Open");
    openConfig.addActionListener(e -> new ActionConfiguration()
        .showOpenDialogJSON());
    config.add(openConfig);
    JMenuItem exportConfig = new JMenuItem(EXPORT_TEXT);
    exportConfig.setName(EXPORT_TEXT);
    exportConfig.addActionListener(e ->
      new ActionConfiguration().showExportDialogJSON());
    config.add(exportConfig);

    // Archives menu
    JMenu archives = new JMenu("Archives");
    JMenuItem openArchives = new JMenuItem("Open");
    openArchives.setName("Open");
    openArchives.addActionListener(e ->
      new ActionArchive().showOpenDialogJSONandXML());
    archives.add(openArchives);
    JMenuItem exportArchives = new JMenuItem(EXPORT_TEXT);
    exportArchives.setName(EXPORT_TEXT);
    exportArchives.addActionListener(e ->
      new ActionArchive().showExportDialogJSONandXML());
    archives.add(exportArchives);

    // Theme menu
    JButton changeThemeBtn = new JButton("Light Mode");
    changeThemeBtn.setName(ActionThemeMode.LIGHT_MODE);
    changeThemeBtn.addActionListener(e -> ActionThemeMode.getInstance(
        changeThemeBtn).changeTheme());
    this.add(changeThemeBtn);
    this.add(archives);
    this.add(config);
    this.add(file);
  }

  /**
   * Create Singleton.
   *
   * @return MenuBar instance
   */
  public static JMenuBar getInstance() {
    if (instance == null) {
      instance = new MenuBar();
    }
    return instance;
  }
}
