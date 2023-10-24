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

package controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import view.EditConfigDialog;
import view.EditConfigParamPanel;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller to read and save the json configuration parameters.
 *
 * @author Aurélie Chamouleau
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
  private static final String JSON_FILE_PATH = System.getProperty("user"
      + ".dir").replace("railway-editor_java_version",
      "pfe-2018-network-journey-simulator\\src" + "\\configs\\config.json");

  /**
   * EditConfigDialog that calls this class methods.
   */
  private final EditConfigDialog editConfigDialog;

  /**
   * Constructor of the class.
   *
   * @param editConfigDialogToSet the EditConfigDialog that calls this class
   *                              methods
   */
  public ActionConfiguration(final EditConfigDialog editConfigDialogToSet) {
    this.editConfigDialog = editConfigDialogToSet;
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
}
