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

package org.example.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

/**
 * JPanel to edit a configuration parameter.
 *
 * @see EditConfigDialog
 *
 * @author Aurélie Chamouleau
 * @file EditConfigParamPanel.java
 * @date 2023-10-20
 * @since 3.0
 */
public class EditConfigParamPanel extends JPanel {
  //constants
  /** JLabel width. */
  private static final int EDIT_CONFIG_PARAM_PANEL_LABEL_WIDTH = 270;
  /** JLabel height. */
  private static final int EDIT_CONFIG_PARAM_PANEL_LABEL_HEIGHT = 20;
  /** JTextField width. */
  private static final int EDIT_CONFIG_PARAM_PANEL_TEXT_FIELD_WIDTH = 160;
  /** JTextField height. */
  private static final int EDIT_CONFIG_PARAM_PANEL_TEXT_FIELD_HEIGHT = 25;
  /** Border top gap. */
  private static final int BORDER_TOP_GAP = 5;
  /** Border bottom gap. */
  private static final int BORDER_BOTTOM_GAP = 5;
  // attributes
  /** JLabel of the parameter name. */
  private final JLabel label;
  /** JTextField of the parameter value. */
  private final JTextField textField;


  /**
   * Constructor of the class.
   * Creates a panel with the parameter name and a text field to edit the value.
   *
   * @param entry the entry of the json parameter to edit
   */
  public EditConfigParamPanel(final Map.Entry<String, Object> entry) {
    // label properties
    this.label = new JLabel(entry.getKey().substring(0, 1).toUpperCase()
      + entry.getKey().substring(1));
    this.label.setPreferredSize(new Dimension(
        EDIT_CONFIG_PARAM_PANEL_LABEL_WIDTH,
        EDIT_CONFIG_PARAM_PANEL_LABEL_HEIGHT));
    this.add(this.label);

    // text field properties
    this.textField = new JTextField(entry.getValue().toString());
    this.textField.setPreferredSize(new Dimension(
        EDIT_CONFIG_PARAM_PANEL_TEXT_FIELD_WIDTH,
        EDIT_CONFIG_PARAM_PANEL_TEXT_FIELD_HEIGHT));
    this.add(this.textField);

    // border properties
    Color buttonBackgroundColor = UIManager.getColor("textInactiveText");
    Border lineBorder = new MatteBorder(0, 0, 1, 0, buttonBackgroundColor);
    Border emptyBorder = new EmptyBorder(BORDER_TOP_GAP, 0,
        BORDER_BOTTOM_GAP, 0);
    this.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
  }

  /**
   * Get the parameter name.
   *
   * @return the parameter name
   */
  public String getParamName() {
    return this.label.getText().toLowerCase();
  }

  /**
   * Get the parameter value.
   *
   * @return the parameter value
   */
  public String getParamValue() {
    return this.textField.getText();
  }
}
