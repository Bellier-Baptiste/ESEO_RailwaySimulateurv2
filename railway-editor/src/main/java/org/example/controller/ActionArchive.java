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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.controller;

import org.example.controller.ActionConfiguration;
import org.example.controller.ActionFile;
import org.example.view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.controller.ActionConfiguration.JSON_FILE_PATH;
import static org.example.controller.ActionFile.ARCHIVES_PATH;

/**
 * A class for performing actions related to the archive menu.
 * Linked to menu items in {@link org.example.view.MenuBar}.
 *
 * @author Benoît VAVASSEUR
 * @file ActionArchive.java
 * @date 2023/12/08
 * @see org.example.data.Data
 * @since 3.0
 */
public class ActionArchive {

  /**
   * ActionConfiguration instance.
   */
  private ActionConfiguration actionConfiguration = new ActionConfiguration();

  /**
   * Path to the XML file.
   */
  public static final String XML_FILE_PATH = System.getProperty("user.dir")
    + File.separator + "network-journey-simulator" + File.separator + "src"
    + File.separator + "configs" + File.separator + "runThisSimulation.xml";

  /**
   * Logger.
   */
  private static final Logger LOGGER =
    Logger.getLogger(ActionArchive.class.getName());


  /**
   *  Constructor of the class
   */
  public ActionArchive() {}


  /**
   * showExportDialogJSONandXML
   */
  public void showExportDialogJSONandXML() {
    try {
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
      String baseFolderName = now.format(formatter);
      String folderName = baseFolderName;
      File exportFolder = new File(ARCHIVES_PATH +
        File.separator + folderName);
      int increment = 1;

      while (exportFolder.exists()) {
        folderName = baseFolderName + "(" + increment + ")";
        exportFolder = new File(ARCHIVES_PATH + File.separator
          + folderName);
        increment++;
      }

      exportFolder.mkdirs();

      Files.copy(Paths.get(JSON_FILE_PATH),
        Paths.get(exportFolder.getAbsolutePath() + File.separator
          + "config.json"),
        StandardCopyOption.REPLACE_EXISTING);
      Files.copy(Paths.get(XML_FILE_PATH),
        Paths.get(exportFolder.getAbsolutePath() + File.separator
          + "RunThisSimulation.xml"),
        StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE,
        "Error exporting files", e);
    }
  }

  /**
   * showOpenDialogJSONandXML
   */
  public void showOpenDialogJSONandXML() {
    JFileChooser fileChooser = new JFileChooser(ARCHIVES_PATH);
    fileChooser.setMultiSelectionEnabled(true);
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
      "JSON and XML Files", "json", "xml");
    fileChooser.setFileFilter(filter);

    int returnVal =
      fileChooser.showOpenDialog(MainWindow.getInstance().getMainPanel());

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File[] files = fileChooser.getSelectedFiles();

      for (File file : files) {
        try {
          String fileName = file.getName();
          int i = fileName.lastIndexOf('.');
          if (i > 0) {
            String extension = fileName.substring(i + 1);
            if ("json".equalsIgnoreCase(extension)) {
              actionConfiguration.copyFile(file.getAbsolutePath(),
                JSON_FILE_PATH);
            } else if ("xml".equalsIgnoreCase(extension)) {
              ActionFile.getInstance().importMap(file);
            }
          }
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error processing file: " +
            file.getName(), e);
        }
      }
    }
  }




}
