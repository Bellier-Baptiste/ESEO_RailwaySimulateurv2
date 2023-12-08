/*
 * License : MIT License
 *
 * Copyright (c) 2023 Équipe PFE_2023_16
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.view.EditConfigDialog;
import org.example.view.EditConfigParamPanel;
import org.example.view.MainWindow;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller to read and save the json configuration parameters.
 *
 * @author Aurélie CHAMOULEAU
 * @author Benoît VAVASSEUR
 * @file ActionConfiguration.java
 * @date 2023-10-20
 * @see EditConfigParamPanel
 * @see EditConfigDialog
 * @since 3.0
 */
public class ActionConfiguration {
  /**
   * LinkedHashMap with the parameters of the configuration.
   */
  private LinkedHashMap<String, Object> jsonMap;
  /**
   * Path of the json file.
   */
  private static final String JSON_FILE_PATH = System.getProperty("user.dir")
    + File.separator + "network-journey-simulator" + File.separator + "src"
    + File.separator + "configs" + File.separator + "config.json";

  /**
   * path to the archives folder
   */
  private static final String ARCHIVES_PATH = System.getProperty("user.dir")
    + File.separator + "archives";
  /**
   * EditConfigDialog that calls this class methods.
   */
  private final EditConfigDialog editConfigDialog;

  private static final Logger LOGGER =
    Logger.getLogger(ActionConfiguration.class.getName());


  /**
   * Constructor of the class.
   *
   * @param editConfigDialogToSet the EditConfigDialog that calls this class
   *                              methods
   */
  public ActionConfiguration(final EditConfigDialog editConfigDialogToSet) {
    this.editConfigDialog = editConfigDialogToSet;
  }

  public ActionConfiguration(){
    this.editConfigDialog = null;
  }

  /**
   * Getter of the json map.
   *
   * @return the json map
   */
  public Map<String, Object> getJsonMap() {
    return this.jsonMap;
  }


  /**
   * Read the json file and create the map with the parameters.
   */
  public void readJsonFile() {
    try {
      // Creation of the json file and the json map
      File file = new File(JSON_FILE_PATH);
      this.jsonMap = new LinkedHashMap<>();

      // Create a JSON parser
      JsonFactory jsonFactory = new JsonFactory();
      try (JsonParser jsonParser = jsonFactory.createParser(file)) {
        // Read the JSON file
        while (jsonParser.nextToken() != null) {
          JsonToken token = jsonParser.getCurrentToken();
          if (token == JsonToken.FIELD_NAME) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            String value = jsonParser.getValueAsString();
            this.jsonMap.put(fieldName, value);
          }
        }
      }
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }


  /**
   * Save the json file with the new parameters.
   */
  public void saveJsonFile() {
    // Iterate over the panels to edit the configuration parameters
    // and update the json map and the json file
    this.jsonMap.clear();
    for (EditConfigParamPanel editConfigParamPanel
        : this.editConfigDialog.getEditConfigParamPanelList()) {
      String key = editConfigParamPanel.getParamName();
      String value = editConfigParamPanel.getParamValue();
      // Check if the value is an integer, a double or a boolean and
      // convert it to the right type if it is the case
      try {
        Integer intValue = Integer.parseInt(value);
        this.jsonMap.put(key, intValue);
      } catch (NumberFormatException e) {
        try {
          Double doubleValue = Double.parseDouble(value);
          this.jsonMap.put(key, doubleValue);
        } catch (NumberFormatException e2) {
          if (value.equals("true") || value.equals("false")) {
            this.jsonMap.put(key, Boolean.valueOf(value));
          } else {
            this.jsonMap.put(key, value);
          }
        }
      }
    }

    // Use ObjectMapper to write in the JSON file
    ObjectMapper mapper = new ObjectMapper();
    // Write the map in the JSON file
    try {
      mapper.writeValue(new File(JSON_FILE_PATH), jsonMap);
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Prompts the export dialog to choose the location to export the json config.
   */
  public void showExportDialogJSON() {
    JFileChooser fileChooser = new JFileChooser(ARCHIVES_PATH);
    FileNameExtensionFilter filter =
      new FileNameExtensionFilter("JSON FILES", "json");
    fileChooser.setFileFilter(filter);
    fileChooser.setDialogTitle("Specify a file to save");

    File defaultFile = new File("example.json");
    fileChooser.setSelectedFile(defaultFile);

    int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      if (!fileToSave.getAbsolutePath().endsWith(".json")) {
        fileToSave = new File(fileToSave + ".json");
      }
      this.copyFile(JSON_FILE_PATH, fileToSave.getAbsolutePath());
    }
  }

  /**
   * Prompts the open dialog to select which json file to import.
   */
  public void showOpenDialogJSON() {
    JFileChooser fileChooser = new JFileChooser(ARCHIVES_PATH);
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
      "json files", "json");
    fileChooser.setFileFilter(filter);
    int returnVal = fileChooser.showOpenDialog(MainWindow.getInstance()
      .getMainPanel());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      deleteFile(JSON_FILE_PATH);
      this.copyFile(file.getAbsolutePath(), JSON_FILE_PATH);
    }
  }

  /**
   * Copy a file to the selected location.
   *
   * @param sourcePath the path of the file to copy
   * @param destPath   the path of the destination file
   */
  public void copyFile(String sourcePath, String destPath) {
    try {
      Files.copy(Paths.get(sourcePath), Paths.get(destPath));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error copying file", e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Delete a file.
   *
   * @param fileToDelete the path of the file to delete
   */
  public void deleteFile(String fileToDelete) {
    try {
      Files.deleteIfExists(Paths.get(fileToDelete));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Impossible to delete the existing file: "
        + fileToDelete, e);
    }
  }

}
