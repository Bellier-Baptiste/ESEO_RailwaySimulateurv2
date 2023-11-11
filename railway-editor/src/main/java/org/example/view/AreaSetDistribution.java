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
  /** Font size. */
  private static final int FONT_SIZE = 14;
  /** Erreur dialog title. */
  private static final String ERROR_DIALOG_TITLE = "Error";
  /** Population distribution type. */
  private static final String POPULATION_DISTRIBUTION_TYPE = "population";
  /** Destination distribution type. */
  private static final String DESTINATION_DISTRIBUTION_TYPE = "destination";
  // Attributes.
  /** Tourist distribution. */
  private int tourist;
  /** Student distribution. */
  private int student;
  /** BusinessMan distribution. */
  private int businessMan;
  /** Worker distribution. */
  private int worker;
  /** Child distribution. */
  private int child;
  /** Retired distribution. */
  private int retired;
  /** Unemployed distribution. */
  private int unemployed;
  /** List of the population distribution. */
  private final JSpinner[] populationDistributionSpinners = new JSpinner[7];

  /** Residential distribution. */
  private int residential;
  /** Commercial distribution. */
  private int commercial;
  /** Office distribution. */
  private int office;
  /** Industrial distribution. */
  private int industrial;
  /** Touristic distribution. */
  private int touristic;
  /** Leisure distribution. */
  private int leisure;
  /** Educational distribution. */
  private int educational;
  private final JSpinner[] destinationDestributionSpinners = new JSpinner[7];

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

  private void display(final Area area) {
    JLabel titlePopulation = new JLabel("Population distribution "
        + "percentages");
    titlePopulation.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

    JSpinner fieldTourist = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldTourist.setValue(area.getDistributionPopulation().get(
        Data.AREA_TOURIST));
    fieldTourist.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    populationDistributionSpinners[0] = fieldTourist;
    JSpinner fieldStudent = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldStudent.setValue(area.getDistributionPopulation().get(
        Data.AREA_STUDENT));
    populationDistributionSpinners[1] = fieldStudent;
    fieldStudent.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    JSpinner fieldBusinessMan = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    populationDistributionSpinners[2] = fieldBusinessMan;
    fieldBusinessMan.setValue(area.getDistributionPopulation().get(
        Data.AREA_BUSINESSMAN));
    fieldBusinessMan.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    JSpinner fieldWorker = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldWorker.setValue(area.getDistributionPopulation().get(
        Data.AREA_WORKER));
    fieldWorker.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    populationDistributionSpinners[3] = fieldWorker;
    JSpinner fieldChild = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldChild.setValue(area.getDistributionPopulation().get(
        Data.AREA_CHILD));
    fieldChild.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    populationDistributionSpinners[4] = fieldChild;
    JSpinner fieldRetired = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldRetired.setValue(area.getDistributionPopulation().get(
        Data.AREA_RETIRED));
    fieldRetired.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    populationDistributionSpinners[5] = fieldRetired;
    JSpinner fieldUnemployed = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    fieldUnemployed.addChangeListener(new DistributionChangeListener(
        POPULATION_DISTRIBUTION_TYPE));
    fieldUnemployed.setValue(area.getDistributionPopulation().get(
        Data.AREA_UNEMPLOYED));
    populationDistributionSpinners[6] = fieldUnemployed;

    JLabel  titleDestination = new JLabel("Destination"
        + " distribution percentages");
    titleDestination.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    JSpinner fieldResidential = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    fieldResidential.setValue(area.getDistributionDestination().get(
        Data.AREA_RESIDENTIAL));
    fieldResidential.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[0] = fieldResidential;
    JSpinner fieldCommercial = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    fieldCommercial.setValue(area.getDistributionDestination().get(
        Data.AREA_COMMERCIAL));
    fieldCommercial.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[1] = fieldCommercial;
    JSpinner fieldOffice = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldOffice.setValue(area.getDistributionDestination().get(
        Data.AREA_OFFICE));
    fieldOffice.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[2] = fieldOffice;
    JSpinner fieldIndustrial = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    fieldIndustrial.setValue(area.getDistributionDestination().get(
        Data.AREA_INDUSTRIAL));
    fieldIndustrial.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[3] = fieldIndustrial;
    JSpinner fieldTouristic = new JSpinner(new SpinnerNumberModel(0, 0, 100,
        1));
    fieldTouristic.setValue(area.getDistributionDestination().get(
        Data.AREA_TOURISTIC));
    fieldTouristic.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[4] = fieldTouristic;
    JSpinner fieldLeisure = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldLeisure.setValue(area.getDistributionDestination().get(
        Data.AREA_LEISURE));
    fieldLeisure.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[5] = fieldLeisure;
    JSpinner fieldSchool = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    fieldSchool.setValue(area.getDistributionDestination().get(
        Data.AREA_EDUCATIONAL));
    fieldSchool.addChangeListener(new DistributionChangeListener(
        DESTINATION_DISTRIBUTION_TYPE));
    this.destinationDestributionSpinners[6] = fieldSchool;

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

    JPanel panelPopulation = new JPanel();
    panelPopulation.setLayout(new BoxLayout(panelPopulation,
        BoxLayout.Y_AXIS));
    JPanel panelPopulationTitle = new JPanel();
    panelPopulationTitle.setLayout(new BorderLayout());
    panelPopulationTitle.add(titlePopulation, BorderLayout.WEST);
    panelPopulation.add(panelPopulationTitle);

    Component verticalPopulationSpace = Box.createVerticalStrut(10);
    panelPopulation.add(verticalPopulationSpace);

    JPanel panelPopulationFieldsContainer = new JPanel();
    panelPopulationFieldsContainer.setLayout(new BorderLayout());
    JPanel panelPopulationFields = new JPanel();
    panelPopulationFields.setLayout(new BoxLayout(panelPopulationFields,
        BoxLayout.Y_AXIS));
    this.addFieldToDistributionPanel(panelPopulationFields, "Tourist",
        fieldTourist, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "Student",
        fieldStudent, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "BusinessMan",
        fieldBusinessMan, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "Worker",
        fieldWorker, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "Child",
        fieldChild, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "Retired ",
        fieldRetired, POPULATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelPopulationFields, "Unemployed ",
        fieldUnemployed, POPULATION_DISTRIBUTION_TYPE);

    // Total of the population distribution
    this.updatePopulationDistributionTotal();
    this.totalPopulationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    this.totalPopulationLabel.setPreferredSize(new Dimension(100, 25));
    this.totalPopulationLabel.setMaximumSize(this.totalPopulationLabel
        .getPreferredSize());
    this.totalPopulationLabel.setMinimumSize(this.totalPopulationLabel
        .getPreferredSize());
    JPanel totalPopulationField = new JPanel();
    totalPopulationField.setLayout(new BorderLayout());
    totalPopulationField.setPreferredSize(new Dimension(200, 25));
    totalPopulationField.setMaximumSize(totalPopulationField
        .getPreferredSize());
    totalPopulationField.setMinimumSize(totalPopulationField
        .getPreferredSize());
    totalPopulationField.add(new JLabel("Total"), BorderLayout.WEST);
    totalPopulationField.add(this.totalPopulationLabel, BorderLayout.EAST);
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

    Component verticalDestinationSpace = Box.createVerticalStrut(10);
    panelDestination.add(verticalDestinationSpace);

    JPanel panelDestinationFieldsContainer = new JPanel();
    panelDestinationFieldsContainer.setLayout(new BorderLayout());
    JPanel panelDestinationFields = new JPanel();
    panelDestinationFields.setLayout(new BoxLayout(panelDestinationFields,
        BoxLayout.Y_AXIS));
    this.addFieldToDistributionPanel(panelDestinationFields, "Residential ",
        fieldResidential, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "Commercial ",
        fieldCommercial, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "Office ",
        fieldOffice, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "Industrial ",
        fieldIndustrial, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "Touristic ",
        fieldTouristic, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "Leisure ",
        fieldLeisure, DESTINATION_DISTRIBUTION_TYPE);
    this.addFieldToDistributionPanel(panelDestinationFields, "School ",
        fieldSchool, DESTINATION_DISTRIBUTION_TYPE);

    // Total of the destination distribution
    this.updateDestinationDistributionTotal();
    this.totalDestinationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    this.totalDestinationLabel.setPreferredSize(new Dimension(100, 25));
    this.totalDestinationLabel.setMaximumSize(this.totalDestinationLabel
        .getPreferredSize());
    this.totalDestinationLabel.setMinimumSize(this.totalDestinationLabel
        .getPreferredSize());
    JPanel totalDestinationField = new JPanel();
    totalDestinationField.setLayout(new BorderLayout());
    totalDestinationField.setPreferredSize(new Dimension(200, 25));
    totalDestinationField.setMaximumSize(totalDestinationField
        .getPreferredSize());
    totalDestinationField.setMinimumSize(totalDestinationField
        .getPreferredSize());
    totalDestinationField.add(new JLabel("Total"), BorderLayout.WEST);
    totalDestinationField.add(this.totalDestinationLabel, BorderLayout.EAST);
    panelDestinationFields.add(totalDestinationField);

    panelDestinationFieldsContainer.add(panelDestinationFields,
        BorderLayout.WEST);
    panelDestination.add(panelDestinationFieldsContainer);




    panel.add(panelPopulation);
    Component horizontalSpace = Box.createHorizontalStrut(30);
    panel.add(horizontalSpace);
    panel.add(panelDestination);


    int result = JOptionPane.showConfirmDialog(MainWindow.getInstance(), panel,
        "Edit distribution", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      tourist = (int) fieldTourist.getValue();
      student = (int) fieldStudent.getValue();
      businessMan = (int) fieldBusinessMan.getValue();
      worker = (int) fieldWorker.getValue();
      child = (int) fieldChild.getValue();
      retired = (int) fieldRetired.getValue();
      unemployed = (int) fieldUnemployed.getValue();


      residential = (int) fieldResidential.getValue();
      commercial = (int) fieldCommercial.getValue();
      office = (int) fieldOffice.getValue();
      industrial = (int) fieldIndustrial.getValue();
      touristic = (int) fieldTouristic.getValue();
      leisure = (int) fieldLeisure.getValue();
      educational = (int) fieldSchool.getValue();
      ok = true;
    }
  }

  private void updatePopulationDistributionTotal() {
    AreaSetDistribution.this.totalPopulationDistribution = 0;
    for (JSpinner jSpinner : populationDistributionSpinners) {
      AreaSetDistribution.this.totalPopulationDistribution += (int)
          jSpinner.getValue();
      AreaSetDistribution.this.totalPopulationLabel.setText(
          AreaSetDistribution.this.totalPopulationDistribution + "%");
    }
    if (AreaSetDistribution.this.totalPopulationDistribution != 100) {
      AreaSetDistribution.this.totalPopulationLabel.setForeground(Color.RED);
    } else {
      AreaSetDistribution.this.totalPopulationLabel.setForeground(Color.GREEN);
    }
  }

  private void updateDestinationDistributionTotal() {
    AreaSetDistribution.this.totalDestinationDistribution = 0;
    for (JSpinner jSpinner : destinationDestributionSpinners) {
      AreaSetDistribution.this.totalDestinationDistribution += (int)
          jSpinner.getValue();
      AreaSetDistribution.this.totalDestinationLabel.setText(
          AreaSetDistribution.this.totalDestinationDistribution + "%");
    }
    if (AreaSetDistribution.this.totalDestinationDistribution != 100) {
      AreaSetDistribution.this.totalDestinationLabel.setForeground(Color.RED);
    } else {
      AreaSetDistribution.this.totalDestinationLabel.setForeground(Color.GREEN);
    }
  }

  /**
   * run and display the popup.
   *
   * @param area area clicked
   */
  public void pop(final Area area) {
    EventQueue.invokeLater(() -> {
      display(area);
      if (ok) {
        // error message if the total of the destination distribution is
        // greater than 100 or if the total of the population distribution is
        // greater than 100
        if (totalPopulationDistribution != 100 && totalDestinationDistribution
            != 100) {
          JOptionPane.showMessageDialog(MainWindow.getInstance(),
              "The total of the population distribution and the total of the "
                  + "destination distribution are not equal to 100",
              ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
        } else if (totalPopulationDistribution != 100) {
          JOptionPane.showMessageDialog(MainWindow.getInstance(),
              "The total of the population distribution is not equal to 100",
              ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
          setDestinationParts(area);
          return;
        } else if (totalDestinationDistribution != 100) {
          JOptionPane.showMessageDialog(MainWindow.getInstance(),
              "The total of the destination distribution is not equal to 100",
              ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
          setPopulationParts(area);
          return;
        }
        setPopulationParts(area);
        setDestinationParts(area);
        ok = false;
      }
    });
  }

  private void setDestinationParts(Area area) {
    area.setNewDestinationPart(Data.AREA_RESIDENTIAL, residential);
    area.setNewDestinationPart(Data.AREA_COMMERCIAL, commercial);
    area.setNewDestinationPart(Data.AREA_OFFICE, office);
    area.setNewDestinationPart(Data.AREA_INDUSTRIAL, industrial);
    area.setNewDestinationPart(Data.AREA_TOURISTIC, touristic);
    area.setNewDestinationPart(Data.AREA_LEISURE, leisure);
    area.setNewDestinationPart(Data.AREA_EDUCATIONAL, educational);
  }

  private void setPopulationParts(Area area) {
    area.setNewPopulationPart(Data.AREA_TOURIST, tourist);
    area.setNewPopulationPart(Data.AREA_STUDENT, student);
    area.setNewPopulationPart(Data.AREA_BUSINESSMAN, businessMan);
    area.setNewPopulationPart(Data.AREA_WORKER, worker);
    area.setNewPopulationPart(Data.AREA_CHILD, child);
    area.setNewPopulationPart(Data.AREA_RETIRED, retired);
    area.setNewPopulationPart(Data.AREA_UNEMPLOYED, unemployed);
  }

  private void addFieldToDistributionPanel(final JPanel panel,
                                           final String label,
                                           final JSpinner field,
                                           final String distributionType) {
    field.setPreferredSize(new Dimension(100, 25));
    field.setMaximumSize(field.getPreferredSize());
    field.setMinimumSize(field.getPreferredSize());

    JPanel panelField = new JPanel();
    panelField.setLayout(new BorderLayout());
    panelField.setPreferredSize(new Dimension(200, 25));
    panelField.setMaximumSize(panelField.getPreferredSize());
    panelField.setMinimumSize(panelField.getPreferredSize());
    field.addChangeListener(new DistributionChangeListener(distributionType));

    panelField.add(new JLabel(label), BorderLayout.WEST);
    panelField.add(field, BorderLayout.EAST);
    panel.add(panelField);
  }

  private class DistributionChangeListener implements ChangeListener {
    private final String distributionType;

    public DistributionChangeListener(String distributionType) {
      this.distributionType = distributionType;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
      if (this.distributionType.equals(POPULATION_DISTRIBUTION_TYPE)) {
        AreaSetDistribution.this.updatePopulationDistributionTotal();
      } else {
        AreaSetDistribution.this.updateDestinationDistributionTotal();
      }
    }
  }
}
