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

import org.example.controller.KeyboardTool;
import org.example.data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which creates a custom {@link JComboBox} with integrated research
 * function.
 *
 * @see org.example.view.ListEventPanel
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file FilterComboBox.java
 * @date N/A
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public class FilterComboBox extends JComboBox {
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  /** Zoom to set on the destination. */
  private static final int ZOOM = 13;
  /** Array of character entered by the user. */
  private final List<String> array;

  /**
   * Constructor.
   *
   * @param filterComboBoArray array of character entered by the user
   */
  @SuppressWarnings("unchecked")
  public FilterComboBox(final List<String> filterComboBoArray) {
    super(filterComboBoArray.toArray());
    this.array = filterComboBoArray;
    this.setEditable(true);
    final JTextField textField = (JTextField) this.getEditor()
        .getEditorComponent();
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(final KeyEvent ke) {
        if (ke.getKeyCode() != KeyEvent.VK_ENTER) {
          SwingUtilities.invokeLater(() -> comboFilter(textField.getText()));
        } else {
          if (!Data.getInstance().getCurrentCity().isEmpty()) {
            MainWindow.getInstance().getMainPanel().requestFocusInWindow();
            MainWindow.getInstance().getMainPanel().addKeyListener(
                new KeyboardTool(MainWindow.getInstance().getMainPanel()));
            double[] coords = findCoordinates(Data.getInstance()
                .getCurrentCity());
            MainWindow.getInstance().getMainPanel().getLineViews().clear();
            MainWindow.getInstance().getMainPanel().getAreaViews().clear();
            Coordinate point = new Coordinate(coords[1], coords[0]);
            MainWindow.getInstance().getMainPanel().setDisplayPosition(point,
                ZOOM);

          }
        }
      }
    });

  }

  /**
   * Filter of the combo box from the enteredText.
   *
   * @param enteredText text entered
   */
  @SuppressWarnings("unchecked")
  public void comboFilter(final String enteredText) {
    List<String> filterArray = new ArrayList<>();
    for (String s : array) {
      if (s.toLowerCase().contains(enteredText.toLowerCase())) {
        filterArray.add(s);
      }
    }
    if (!filterArray.isEmpty()) {
      this.setModel(new DefaultComboBoxModel(filterArray.toArray()));
      this.setSelectedItem(enteredText);
      this.showPopup();
    } else {
      this.hidePopup();
    }
  }

  /**
   * fill the combo box options.
   *
   * @return all the tows starting with the enteredText
   */
  public static List<String> populateArray() {
    List<String> test = new ArrayList<>();
    test.add("");

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    InputStream file = classLoader.getResourceAsStream("cities.txt");
    try {
      if (file != null) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            file))) {
          for (String line; (line = br.readLine()) != null;) {
            test.add(line.split(";")[0]);
          }
          // line is not visible here.
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return test;
  }

  /**
   * find the city coordinates after combo box validation.
   *
   * @param name city name
   *
   * @return double[] coordinates
   */
  private double[] findCoordinates(final String name) {

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    InputStream file = classLoader.getResourceAsStream(
        ("cities.txt"));
    double[] coords = new double[2];

    try {
      assert file != null;
      try (BufferedReader br = new BufferedReader(new InputStreamReader(
          file))) {
        String line = br.readLine();
        while (line != null && !line.contains(name)) {
          line = br.readLine();
        }
        assert line != null;
        coords[0] = Double.parseDouble(line.split(";")[1]);
        coords[1] = Double.parseDouble(line.split(";")[2]);

        // line is not visible here.
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return coords;
  }
}
