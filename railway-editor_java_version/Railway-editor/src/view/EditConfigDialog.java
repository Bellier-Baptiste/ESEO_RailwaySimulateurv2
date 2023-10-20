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

package view;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JDialog to edit the configuration of the simulation.
 *
 * @see EditConfigParamPanel
 *
 * @author Aurélie Chamouleau
 * @file EditConfigDialog.java
 * @date 2023-10-20
 * @since 3.0
 */
public class EditConfigDialog extends JDialog {
  // constants
  /** Width of the dialog. */
  private static final int EDIT_CONFIG_DIALOG_WIDTH = 500;
  /** Height of the dialog. */
  private static final int EDIT_CONFIG_DIALOG_HEIGHT = 600;
  /** Unit increment of the scrollbar. */
  private static final int EDIT_CONFIG_DIALOG_SCROLL_PANE_UNIT_INCREMENT = 13;
  /** Path of the json file. */
  private static final String JSON_FILE_PATH = System.getProperty("user" +
      ".dir").replace(      "railway-editor_java_version",
      "pfe-2018-network-journey-simulator\\src" + "\\configs\\config.json");
  /** List of the panels to edit the configuration parameters. */
  private final List<EditConfigParamPanel> editConfigParamPanelList =
      new ArrayList<>();
  /** Panel to contain the panels to edit the configuration parameters. */
  private final JPanel editConfigParamPanelsContainer;
  /** Map of the json file. */
  private transient LinkedHashMap<String, Object> jsonMap;

  /**
   * Constructor of the class.
   * Read the json file and create the panels to edit the configuration
   */
  public EditConfigDialog() {
    // Dialog properties
    this.setTitle("Edit Configuration");
    this.setSize(EDIT_CONFIG_DIALOG_WIDTH, EDIT_CONFIG_DIALOG_HEIGHT);
    this.setLocationRelativeTo(MainWindow.getInstance());
    this.setResizable(false);
    this.setModal(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Panel properties
    this.editConfigParamPanelsContainer = new JPanel();
    this.editConfigParamPanelsContainer.setLayout(new BoxLayout(
        this.editConfigParamPanelsContainer, BoxLayout.PAGE_AXIS));
    this.editConfigParamPanelsContainer.setAlignmentY(Component.TOP_ALIGNMENT);

    // Scroll pane properties
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.getVerticalScrollBar().setUnitIncrement(
        EDIT_CONFIG_DIALOG_SCROLL_PANE_UNIT_INCREMENT);
    scrollPane.setViewportView(this.editConfigParamPanelsContainer);

    // Add the scroll pane to the dialog
    this.add(scrollPane);
    this.initComponents();
    this.setVisible(true);
  }

  /**
   * Initialize the components of the dialog.
   */
  private void initComponents() {
    this.readJsonFile();
    for (Map.Entry<String, Object> entry : this.jsonMap.entrySet()) {
      EditConfigParamPanel editConfigParamPanel =
          new EditConfigParamPanel(entry);
      this.editConfigParamPanelList.add(editConfigParamPanel);
      this.editConfigParamPanelsContainer.add(editConfigParamPanel);
    }
  }

  /**
   * Read the json file and create the map with the parameters.
   */
  private void readJsonFile() {
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
}
