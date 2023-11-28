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

import org.example.data.Data;
import org.example.model.Area;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to show a popup windows in order to distribute the population in the
 * {@link Area}.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file AreaSetDistribution.java
 * @date N/A
 * @since 2.0
 */
public class AreaSetDistribution {
  /** Total field dimension. */
  private static final Dimension FIELD_PANEL_DIMENSION = new Dimension(200,
      25);
  /** Total label dimension. */
  private static final Dimension FIELD_DIMENSION = new Dimension(100, 25);
  /** Space between population panel and destination panel. */
  private static final int SPACE_BETWEEN_PANELS = 30;
  /** Vertical spacing between fields. */
  private static final int VERTICAL_SPACING_BETWEEN_FIELDS = 10;
  /** One hundred percent value. */
  private static final int ONE_HUNDRED_PERCENT = 100;
  /** Font size. */
  private static final int FONT_SIZE = 14;
  /** Population distribution type. */
  private static final String POPULATION_DISTRIBUTION_TYPE = "population";
  /** Destination distribution type. */
  private static final String DESTINATION_DISTRIBUTION_TYPE = "destination";
  /** List of the population distribution keys. */
  protected static final Map<String, JSpinner> DISTRIBUTION_POPULATION_FIELDS =
      new HashMap<>();
  /** List of the destination distribution keys. */
  protected static final Map<String, JSpinner> DISTRIBUTION_DESTINATION_FIELDS =
      new HashMap<>();

  static {
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_TOURIST, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_STUDENT, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_BUSINESSMAN, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_WORKER, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_CHILD, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_RETIRED, null);
    DISTRIBUTION_POPULATION_FIELDS.put(Data.AREA_UNEMPLOYED, null);

    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_RESIDENTIAL, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_COMMERCIAL, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_OFFICE, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_INDUSTRIAL, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_TOURISTIC, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_LEISURE, null);
    DISTRIBUTION_DESTINATION_FIELDS.put(Data.AREA_EDUCATIONAL, null);
  }
  // Attributes.
  /** Total of the population distribution. */

  private int totalPopulationDistribution;
  /** Total of the destination distribution. */
  private int totalDestinationDistribution;
  /** Boolean to know if ok button of the popup clicked. */
  private boolean ok = false;
  /** Total population value. */
  private final JLabel totalPopulationLabel = new JLabel();
  /** Total destination value. */
  private final JLabel totalDestinationLabel = new JLabel();
  /** Boolean to know if it is the first entrance in the popup. */
  private boolean notFirstEntrance1 = false;
  /** Boolean to know if it is the first entrance in the popup. */
  private boolean notFirstEntrance2 = false;
  /** Area. */
  private final Area area;

  /**
   * Constructor of the class.
   *
   * @param areaToEditDistributions area to edit distributions
   */
  public AreaSetDistribution(final Area areaToEditDistributions) {
    this.area = areaToEditDistributions;
  }

  /**
   * Display the popup and initialize the fields.
   */
  private void display() {
    JLabel titlePopulation = new JLabel("Population distribution "
        + "percentages");
    titlePopulation.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    // Create the population fields
    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_POPULATION_FIELDS.entrySet()) {
      JSpinner field = this.createSpinner(POPULATION_DISTRIBUTION_TYPE,
          entry.getKey());
      DISTRIBUTION_POPULATION_FIELDS.put(entry.getKey(), field);
    }

    JLabel  titleDestination = new JLabel("Destination"
        + " distribution percentages");
    titleDestination.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    //Create the destination fields
    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_DESTINATION_FIELDS.entrySet()) {
      JSpinner field = this.createSpinner(DESTINATION_DISTRIBUTION_TYPE,
          entry.getKey());
      DISTRIBUTION_DESTINATION_FIELDS.put(entry.getKey(), field);
    }

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    JPanel panelPopulation = new JPanel();
    panelPopulation.setLayout(new BoxLayout(panelPopulation,
        BoxLayout.Y_AXIS));
    JPanel panelPopulationTitle = new JPanel();
    panelPopulationTitle.setLayout(new BorderLayout());
    panelPopulationTitle.add(titlePopulation, BorderLayout.WEST);
    panelPopulation.add(panelPopulationTitle);
    Component verticalPopulationSpace = Box.createVerticalStrut(
        VERTICAL_SPACING_BETWEEN_FIELDS);
    panelPopulation.add(verticalPopulationSpace);
    JPanel panelPopulationFieldsContainer = new JPanel();
    panelPopulationFieldsContainer.setLayout(new BorderLayout());
    JPanel panelPopulationFields = new JPanel();
    panelPopulationFields.setLayout(new BoxLayout(panelPopulationFields,
        BoxLayout.Y_AXIS));

    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_POPULATION_FIELDS.entrySet()) {
      this.addFieldToDistributionPanel(panelPopulationFields, entry.getKey(),
          entry.getValue(), POPULATION_DISTRIBUTION_TYPE);
    }

    this.updatePopulationDistributionTotal();
    this.totalPopulationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    this.totalPopulationLabel.setPreferredSize(FIELD_DIMENSION);
    this.totalPopulationLabel.setMaximumSize(this.totalPopulationLabel
        .getPreferredSize());
    this.totalPopulationLabel.setMinimumSize(this.totalPopulationLabel
        .getPreferredSize());
    JPanel totalPopulationField = this.initTotalField(
        this.totalPopulationLabel);
    panelPopulationFields.add(totalPopulationField);
    panelPopulationFieldsContainer.add(panelPopulationFields,
        BorderLayout.WEST);

    panelPopulation.add(panelPopulationFieldsContainer);
    JPanel panelDestination = new JPanel();
    panelDestination.setLayout(new BoxLayout(panelDestination,
        BoxLayout.Y_AXIS));
    JPanel panelDestinationTitle = new JPanel();
    panelDestinationTitle.setLayout(new BorderLayout());
    panelDestinationTitle.add(titleDestination, BorderLayout.WEST);
    panelDestination.add(panelDestinationTitle);
    Component verticalDestinationSpace = Box.createVerticalStrut(
        VERTICAL_SPACING_BETWEEN_FIELDS);
    panelDestination.add(verticalDestinationSpace);
    JPanel panelDestinationFieldsContainer = new JPanel();
    panelDestinationFieldsContainer.setLayout(new BorderLayout());
    JPanel panelDestinationFields = new JPanel();
    panelDestinationFields.setLayout(new BoxLayout(panelDestinationFields,
        BoxLayout.Y_AXIS));

    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_DESTINATION_FIELDS.entrySet()) {
      this.addFieldToDistributionPanel(panelDestinationFields, entry.getKey(),
          entry.getValue(), DESTINATION_DISTRIBUTION_TYPE);
    }

    this.updateDestinationDistributionTotal();
    this.totalDestinationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    this.totalDestinationLabel.setPreferredSize(FIELD_DIMENSION);
    this.totalDestinationLabel.setMaximumSize(this.totalDestinationLabel
        .getPreferredSize());
    this.totalDestinationLabel.setMinimumSize(this.totalDestinationLabel
        .getPreferredSize());
    JPanel totalDestinationField = this.initTotalField(
        this.totalDestinationLabel);
    panelDestinationFields.add(totalDestinationField);
    panelDestinationFieldsContainer.add(panelDestinationFields,
        BorderLayout.WEST);
    panelDestination.add(panelDestinationFieldsContainer);
    panel.add(panelPopulation);
    Component horizontalSpace = Box.createHorizontalStrut(SPACE_BETWEEN_PANELS);
    panel.add(horizontalSpace);
    panel.add(panelDestination);

    int result = JOptionPane.showConfirmDialog(MainWindow.getInstance(), panel,
        "Edit distribution", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      ok = true;
    }
  }

  private JSpinner createSpinner(final String distributionType,
                                 final String areaName) {
    JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0,
        ONE_HUNDRED_PERCENT, 1));
    if (distributionType.equals(POPULATION_DISTRIBUTION_TYPE)) {
      spinner.setValue(this.area.getDistributionPopulation().get(areaName));
    } else {
      spinner.setValue(this.area.getDistributionDestination().get(areaName));
    }
    spinner.setName(areaName);
    return spinner;
  }

  private JPanel initTotalField(final JLabel totalLabel) {
    JPanel totalField = new JPanel();
    totalField.setLayout(new BorderLayout());
    totalField.setPreferredSize(FIELD_PANEL_DIMENSION);
    totalField.setMaximumSize(totalField
        .getPreferredSize());
    totalField.setMinimumSize(totalField
        .getPreferredSize());
    totalField.add(new JLabel("Total"), BorderLayout.WEST);
    totalField.add(totalLabel, BorderLayout.EAST);
    return totalField;
  }

  private void updatePopulationDistributionTotal() {
    AreaSetDistribution.this.totalPopulationDistribution = 0;
    for (JSpinner spinner : DISTRIBUTION_POPULATION_FIELDS.values()) {
      AreaSetDistribution.this.totalPopulationDistribution += (int)
          spinner.getValue();
      AreaSetDistribution.this.totalPopulationLabel.setText(
          AreaSetDistribution.this.totalPopulationDistribution + "%");
    }
    boolean isValid = true;
    JButton okButton = null;
    JOptionPane pane = null;
    if (this.notFirstEntrance1) {
      pane = this.getOptionPane(AreaSetDistribution.this.totalPopulationLabel);
    }
    if (pane != null) {
      okButton = pane.getRootPane().getDefaultButton();
    }
    if (AreaSetDistribution.this.totalPopulationDistribution
        != ONE_HUNDRED_PERCENT) {
      AreaSetDistribution.this.totalPopulationLabel.setForeground(Color.RED);
      isValid = false;
    } else {
      AreaSetDistribution.this.totalPopulationLabel.setForeground(
          Color.GREEN);
    }
    if (okButton != null) {
      okButton.setEnabled(isValid);
    }
    if (!this.notFirstEntrance1) {
      this.notFirstEntrance1 = true;
    }
  }


  private void updateDestinationDistributionTotal() {
    AreaSetDistribution.this.totalDestinationDistribution = 0;
    for (JSpinner spinner : DISTRIBUTION_DESTINATION_FIELDS.values()) {

      AreaSetDistribution.this.totalDestinationDistribution += (int)
          spinner.getValue();
      AreaSetDistribution.this.totalDestinationLabel.setText(
          AreaSetDistribution.this.totalDestinationDistribution + "%");
    }
    boolean isValid = true;
    JButton okButton = null;
    JOptionPane pane = null;
    if (this.notFirstEntrance2) {
      pane = this.getOptionPane(AreaSetDistribution.this.totalDestinationLabel);
    }
    if (pane != null) {
      okButton = pane.getRootPane().getDefaultButton();
    }
    if (AreaSetDistribution.this.totalDestinationDistribution
        != ONE_HUNDRED_PERCENT) {
      AreaSetDistribution.this.totalDestinationLabel.setForeground(Color.RED);
      isValid = false;
    } else {
      AreaSetDistribution.this.totalDestinationLabel.setForeground(
          Color.GREEN);
    }
    if (okButton != null) {
      okButton.setEnabled(isValid);
    }
    if (!this.notFirstEntrance2) {
      this.notFirstEntrance2 = true;
    }
  }

  /**
   * Get the {@link JOptionPane} of the popup.
   *
   * @param parent parent component
   *
   * @return the {@link JOptionPane} of the popup
   */
  protected JOptionPane getOptionPane(final JComponent parent) {
    JOptionPane pane;
    if (!(parent instanceof JOptionPane)) {
      pane = getOptionPane((JComponent) parent.getParent());
    } else {
      pane = (JOptionPane) parent;
    }
    return pane;
  }

  /**
   * run and display the popup.
   */
  public void pop() {
    EventQueue.invokeLater(() -> {
      this.display();
      if (ok) {
        setPopulationParts();
        setDestinationParts();
        this.notFirstEntrance1 = false;
        this.notFirstEntrance2 = false;
        ok = false;
      }
    });
  }

  private void setDestinationParts() {
    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_DESTINATION_FIELDS.entrySet()) {
      this.area.getDistributionDestination().put(entry.getKey(),
          (int) entry.getValue().getValue());
    }
  }

  private void setPopulationParts() {
    for (Map.Entry<String, JSpinner> entry
        : DISTRIBUTION_POPULATION_FIELDS.entrySet()) {
      this.area.getDistributionPopulation().put(entry.getKey(),
          (int) entry.getValue().getValue());
    }
  }

  private void addFieldToDistributionPanel(final JPanel panel,
                                           final String label,
                                           final JSpinner field,
                                           final String distributionType) {
    field.setPreferredSize(FIELD_DIMENSION);
    field.setMaximumSize(field.getPreferredSize());
    field.setMinimumSize(field.getPreferredSize());

    JPanel panelField = new JPanel();
    panelField.setLayout(new BorderLayout());
    panelField.setPreferredSize(FIELD_PANEL_DIMENSION);
    panelField.setMaximumSize(panelField.getPreferredSize());
    panelField.setMinimumSize(panelField.getPreferredSize());
    field.addChangeListener(new DistributionChangeListener(distributionType));
    panelField.add(new JLabel(label), BorderLayout.WEST);
    panelField.add(field, BorderLayout.EAST);
    panel.add(panelField);
  }

  private class DistributionChangeListener implements ChangeListener {
    /** Distribution type. */
    private final String distributionType;

    /**
     * Constructor of the class.
     *
     * @param distributionTypeToSet distribution type of the spinner
     */
    DistributionChangeListener(final String distributionTypeToSet) {
      this.distributionType = distributionTypeToSet;
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e  a ChangeEvent object
     */
    @Override
    public void stateChanged(final ChangeEvent e) {
      if (AreaSetDistribution.POPULATION_DISTRIBUTION_TYPE.equals(
          this.distributionType)) {
        AreaSetDistribution.this.updatePopulationDistributionTotal();
      } else {
        AreaSetDistribution.this.updateDestinationDistributionTotal();
      }
    }
  }
}



