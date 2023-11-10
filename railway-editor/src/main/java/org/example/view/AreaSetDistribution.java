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
import java.awt.BorderLayout;
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
  /** Boolean to know if ok button of the popup clicked. */
  private boolean ok = false;

  private void display(final Area area) {
    JLabel titlePopulation = new JLabel("Population distribution "
        + "percentages");
    titlePopulation.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

    JSpinner fieldTourist = new JSpinner();
    fieldTourist.setValue(area.getDistributionPopulation().get(
        Data.AREA_TOURIST));
    JSpinner fieldStudent = new JSpinner();
    fieldStudent.setValue(area.getDistributionPopulation().get(
        Data.AREA_STUDENT));
    JSpinner fieldBusinessMan = new JSpinner();
    fieldBusinessMan.setValue(area.getDistributionPopulation().get(
        Data.AREA_BUSINESSMAN));
    JSpinner fieldWorker = new JSpinner();
    fieldWorker.setValue(area.getDistributionPopulation().get(
        Data.AREA_WORKER));
    JSpinner fieldChild = new JSpinner();
    fieldChild.setValue(area.getDistributionPopulation().get(
        Data.AREA_CHILD));
    JSpinner fieldRetired = new JSpinner();
    fieldRetired.setValue(area.getDistributionPopulation().get(
        Data.AREA_RETIRED));
    JSpinner fieldUnemployed = new JSpinner();
    fieldUnemployed.setValue(area.getDistributionPopulation().get(
        Data.AREA_UNEMPLOYED));


    JLabel  titleDestination = new JLabel("Destination"
        + " distribution percentages");
    titleDestination.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    JSpinner fieldResidential = new JSpinner();
    fieldResidential.setValue(area.getDistributionDestination().get(
        Data.AREA_RESIDENTIAL));
    JSpinner fieldCommercial = new JSpinner();
    fieldCommercial.setValue(area.getDistributionDestination().get(
        Data.AREA_COMMERCIAL));
    JSpinner fieldOffice = new JSpinner();
    fieldOffice.setValue(area.getDistributionDestination().get(
        Data.AREA_OFFICE));
    JSpinner fieldIndustrial = new JSpinner();
    fieldIndustrial.setValue(area.getDistributionDestination().get(
        Data.AREA_INDUSTRIAL));
    JSpinner fieldTouristic = new JSpinner();
    fieldTouristic.setValue(area.getDistributionDestination().get(
        Data.AREA_TOURISTIC));
    JSpinner fieldLeisure = new JSpinner();
    fieldLeisure.setValue(area.getDistributionDestination().get(
        Data.AREA_LEISURE));
    JSpinner fieldSchool = new JSpinner();
    fieldSchool.setValue(area.getDistributionDestination().get(
        Data.AREA_EDUCATIONAL));


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
        fieldTourist);
    this.addFieldToDistributionPanel(panelPopulationFields, "Student",
        fieldStudent);
    this.addFieldToDistributionPanel(panelPopulationFields, "BusinessMan",
        fieldBusinessMan);
    this.addFieldToDistributionPanel(panelPopulationFields, "Worker",
        fieldWorker);
    this.addFieldToDistributionPanel(panelPopulationFields, "Child",
        fieldChild);
    this.addFieldToDistributionPanel(panelPopulationFields, "Retired ",
        fieldRetired);
    this.addFieldToDistributionPanel(panelPopulationFields, "Unemployed ",
        fieldUnemployed);
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
        fieldResidential);
    this.addFieldToDistributionPanel(panelDestinationFields, "Commercial ",
        fieldCommercial);
    this.addFieldToDistributionPanel(panelDestinationFields, "Office ",
        fieldOffice);
    this.addFieldToDistributionPanel(panelDestinationFields, "Industrial ",
        fieldIndustrial);
    this.addFieldToDistributionPanel(panelDestinationFields, "Touristic ",
        fieldTouristic);
    this.addFieldToDistributionPanel(panelDestinationFields, "Leisure ",
        fieldLeisure);
    this.addFieldToDistributionPanel(panelDestinationFields, "School ",
        fieldSchool);
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

  /**
   * run and display the popup.
   *
   * @param area area clicked
   */
  public void pop(final Area area) {
    EventQueue.invokeLater(() -> {
      display(area);
      if (ok) {
        area.setNewPopulationPart(Data.AREA_TOURIST, tourist);
        area.setNewPopulationPart(Data.AREA_STUDENT, student);
        area.setNewPopulationPart(Data.AREA_BUSINESSMAN, businessMan);
        area.setNewPopulationPart(Data.AREA_WORKER, worker);
        area.setNewPopulationPart(Data.AREA_CHILD, child);
        area.setNewPopulationPart(Data.AREA_RETIRED, retired);
        area.setNewPopulationPart(Data.AREA_UNEMPLOYED, unemployed);
        area.setNewDestinationPart(Data.AREA_RESIDENTIAL, residential);
        area.setNewDestinationPart(Data.AREA_COMMERCIAL, commercial);
        area.setNewDestinationPart(Data.AREA_OFFICE, office);
        area.setNewDestinationPart(Data.AREA_INDUSTRIAL, industrial);
        area.setNewDestinationPart(Data.AREA_TOURISTIC, touristic);
        area.setNewDestinationPart(Data.AREA_LEISURE, leisure);
        area.setNewDestinationPart(Data.AREA_EDUCATIONAL, educational);
        ok = false;
      }
    });
  }

  private void addFieldToDistributionPanel(final JPanel panel,
                                           final String label,
                                           final JSpinner field) {
    field.setPreferredSize(new Dimension(100, 25));
    field.setMaximumSize(field.getPreferredSize());
    field.setMinimumSize(field.getPreferredSize());

    JPanel panelField = new JPanel();
    panelField.setLayout(new BorderLayout());
    panelField.setPreferredSize(new Dimension(200, 25));
    panelField.setMaximumSize(panelField.getPreferredSize());
    panelField.setMinimumSize(panelField.getPreferredSize());
    panelField.add(new JLabel(label), BorderLayout.WEST);
    panelField.add(field, BorderLayout.EAST);
    panel.add(panelField);
  }

}
