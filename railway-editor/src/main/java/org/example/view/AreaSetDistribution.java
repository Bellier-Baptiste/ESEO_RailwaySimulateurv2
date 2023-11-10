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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

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
  private String tourist;
  /** Student distribution. */
  private String student;
  /** BusinessMan distribution. */
  private String businessMan;
  /** Worker distribution. */
  private String worker;
  /** Child distribution. */
  private String child;
  /** Retired distribution. */
  private String retired;
  /** Unemployed distribution. */
  private String unemployed;
  /** Residential distribution. */
  private String residential;
  /** Commercial distribution. */
  private String commercial;
  /** Office distribution. */
  private String office;
  /** Industrial distribution. */
  private String industrial;
  /** Touristic distribution. */
  private String touristic;
  /** Leisure distribution. */
  private String leisure;
  /** Educational distribution. */
  private String educational;
  /** Boolean to know if ok button of the popup clicked. */
  private boolean ok = false;

  private void display(final Area area) {
    LookAndFeel previousLf = UIManager.getLookAndFeel();
    JLabel titlePopulation = new JLabel("Choose population distribution "
        + "percentage");
    titlePopulation.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

    JTextField fieldTourist = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_TOURIST)));
    JTextField fieldStudent = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_STUDENT)));
    JTextField fieldBusinessMan = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_BUSINESSMAN)));
    JTextField fieldWorker = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_WORKER)));
    JTextField fieldChild = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_CHILD)));
    JTextField fieldRetired = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_RETIRED)));
    JTextField fieldUnemployed = new JTextField(Integer.toString(
        area.getDistributionPopulation().get(Data.AREA_UNEMPLOYED)));

    JLabel  titleDestination = new JLabel("Choose destination"
        + " distribution percentage");
    titleDestination.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
    JTextField fieldResidential = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_RESIDENTIAL)));
    JTextField fieldCommercial = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_COMMERCIAL)));
    JTextField fieldOffice = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_OFFICE)));
    JTextField fieldIndustrial = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_INDUSTRIAL)));
    JTextField fieldTouristic = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_TOURISTIC)));
    JTextField fieldLeisure = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_LEISURE)));
    JTextField fieldEducational = new JTextField(Integer.toString(
        area.getDistributionDestination().get(Data.AREA_EDUCATIONAL)));

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(titlePopulation);
    panel.add(new JLabel("Tourist: "));
    panel.add(fieldTourist);
    panel.add(new JLabel("Student: "));
    panel.add(fieldStudent);
    panel.add(new JLabel("BusinessMan: "));
    panel.add(fieldBusinessMan);
    panel.add(new JLabel("Worker: "));
    panel.add(fieldWorker);
    panel.add(new JLabel("Child: "));
    panel.add(fieldChild);
    panel.add(new JLabel("Retired: "));
    panel.add(fieldRetired);
    panel.add(new JLabel("Unemployed: "));
    panel.add(fieldUnemployed);

    panel.add(new JLabel("Destination: "));
    panel.add(titleDestination);
    panel.add(new JLabel("Residential: "));
    panel.add(fieldResidential);
    panel.add(new JLabel("Commercial: "));
    panel.add(fieldCommercial);
    panel.add(new JLabel("Office: "));
    panel.add(fieldOffice);
    panel.add(new JLabel("Industrial: "));
    panel.add(fieldIndustrial);
    panel.add(new JLabel("Touristic: "));
    panel.add(fieldTouristic);
    panel.add(new JLabel("Leisure: "));
    panel.add(fieldLeisure);
    panel.add(new JLabel("Educational: "));
    panel.add(fieldEducational);

    int result = JOptionPane.showConfirmDialog(null, panel,
        "Edit distribution", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      tourist = fieldTourist.getText();
      student = fieldStudent.getText();
      businessMan = fieldBusinessMan.getText();
      worker = fieldWorker.getText();
      child = fieldChild.getText();
      retired = fieldRetired.getText();
      unemployed = fieldUnemployed.getText();

      residential = fieldResidential.getText();
      commercial = fieldCommercial.getText();
      office = fieldOffice.getText();
      industrial = fieldIndustrial.getText();
      touristic = fieldTouristic.getText();
      leisure = fieldLeisure.getText();
      educational = fieldEducational.getText();

      ok = true;
    }
    try {
      UIManager.setLookAndFeel(previousLf);
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
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
        area.setNewPopulationPart(Data.AREA_TOURIST, Integer.parseInt(tourist));
        area.setNewPopulationPart(Data.AREA_STUDENT, Integer.parseInt(student));
        area.setNewPopulationPart(Data.AREA_BUSINESSMAN, Integer.parseInt(
            businessMan));
        area.setNewPopulationPart(Data.AREA_WORKER, Integer.parseInt(worker));
        area.setNewPopulationPart(Data.AREA_CHILD, Integer.parseInt(child));
        area.setNewPopulationPart(Data.AREA_RETIRED, Integer.parseInt(retired));
        area.setNewPopulationPart(Data.AREA_UNEMPLOYED, Integer.parseInt(
            unemployed));
        area.setNewDestinationPart(Data.AREA_RESIDENTIAL, Integer.parseInt(
            residential));
        area.setNewDestinationPart(Data.AREA_COMMERCIAL, Integer.parseInt(
            commercial));
        area.setNewDestinationPart(Data.AREA_OFFICE, Integer.parseInt(office));
        area.setNewDestinationPart(Data.AREA_INDUSTRIAL, Integer.parseInt(
            industrial));
        area.setNewDestinationPart(Data.AREA_TOURISTIC, Integer.parseInt(
            touristic));
        area.setNewDestinationPart(Data.AREA_LEISURE, Integer.parseInt(
            leisure));
        area.setNewDestinationPart(Data.AREA_EDUCATIONAL, Integer.parseInt(
            educational));
        ok = false;
      }
    });
  }
}
