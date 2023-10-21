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

import controller.ActionConfiguration;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JDialog to edit the configuration of the simulation.
 *
 * @author Aurélie Chamouleau
 * @file EditConfigDialog.java
 * @date 2023-10-20
 * @see EditConfigParamPanel
 * @since 3.0
 */
public class EditConfigDialog extends JDialog {
  // constants
  /**
   * Width of the dialog.
   */
  private static final int EDIT_CONFIG_DIALOG_WIDTH = 500;
  /**
   * Height of the dialog.
   */
  private static final int EDIT_CONFIG_DIALOG_HEIGHT = 600;
  /**
   * Unit increment of the scrollbar.
   */
  private static final int EDIT_CONFIG_DIALOG_SCROLL_PANE_UNIT_INCREMENT = 13;

  // attributes
  /**
   * List of the panels to edit the configuration parameters.
   */
  private final List<EditConfigParamPanel> editConfigParamPanelList =
      new ArrayList<>();
  /**
   * Panel to contain the panels to edit the configuration parameters.
   */
  private final JPanel editConfigParamPanelsContainer;
  /**
   * ActionConfiguration to read and save the json file.
   */
  private final transient ActionConfiguration actionConfiguration;

  /**
   * Constructor of the class.
   * Read the json file and create the panels to edit the configuration
   */
  public EditConfigDialog() {
    this.actionConfiguration = new ActionConfiguration(this);

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
    this.actionConfiguration.readJsonFile();
    EditConfigParamPanel editConfigParamPanel = null;
    for (Map.Entry<String, Object> entry
        : this.actionConfiguration.getJsonMap().entrySet()) {
      editConfigParamPanel =
          new EditConfigParamPanel(entry);
      this.editConfigParamPanelList.add(editConfigParamPanel);
      this.editConfigParamPanelsContainer.add(editConfigParamPanel);
    }
    // Remove the border of the last panel
    if (editConfigParamPanel != null) {
      editConfigParamPanel.setBorder(null);
    }
    // Add the button panel to save or cancel the changes
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    // Button to save the changes
    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> this.actionConfiguration.saveJsonFile());
    okButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(okButton);
    this.editConfigParamPanelsContainer.add(buttonPanel);
    // Button to cancel the changes
    JButton cancelButton = new JButton("CANCEL");
    cancelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
    cancelButton.addActionListener(e -> this.dispose());
    buttonPanel.add(cancelButton);
    this.editConfigParamPanelsContainer.add(buttonPanel);
  }

  /**
   * Get the list of the panels to edit the configuration parameters.
   *
   * @return the list of the panels to edit the configuration parameters
   */
  public List<EditConfigParamPanel> getEditConfigParamPanelList() {
    return this.editConfigParamPanelList;
  }
}
