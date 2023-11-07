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

import org.example.controller.ActionArea;
import org.example.controller.ActionLine;
import org.example.controller.ActionMetroEvent;
import org.example.controller.ActionRunSimulation;
import org.example.controller.ActionStation;
import org.example.data.Data;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.Objects;

/**
 * ToolBar panel that extends {@link JToolBar} which contains all the action
 * buttons.
 *
 * @author Aurélie Chamouleau
 * @file ToolBarPanel.java
 * @date 2023-09-22
 * @since 3.0
 */
public class ToolBarPanel extends JToolBar {
  // constants
  /** Text of the button with a delete action. */
  public static final String DELETE_TEXT_BTN = "DELETE";
  /** Preferred destination panel width. */
  private static final int DESTINATION_PANEL_WIDTH = 180;
  /** Preferred destination panel height. */
  private static final int DESTINATION_PANEL_HEIGHT = 60;
  /** Maximum destination panel width. */
  private static final int DESTINATION_PANEL_MAX_WIDTH = 180;
  /** Maximum destination panel height. */
  private static final int DESTINATION_PANEL_MAX_HEIGHT = 90;
  /** Filter combo box width. */
  private static final int FILTER_COMBO_BOX_WIDTH = 140;
  /** Filter combo box height. */
  private static final int FILTER_COMBO_BOX_HEIGHT = 30;
  /** Run simulation panel preferred width. */
  private static final int RUN_SIMULATION_PANEL_WIDTH = 100;
  /** Run simulation panel preferred height. */
  private static final int RUN_SIMULATION_PANEL_HEIGHT = 60;
  /** Run simulation panel maximum width. */
  private static final int RUN_SIMULATION_PANEL_MAX_WIDTH = 100;
  /** Run simulation panel maximum height. */
  private static final int RUN_SIMULATION_PANEL_MAX_HEIGHT = 90;
  /** Run simulation button width. */
  private static final int RUN_SIMULATION_BTN_WIDTH = 80;
  /** Run simulation button height. */
  private static final int RUN_SIMULATION_BTN_HEIGHT = 30;
  // attributes
  /** Current line id. */
  private JLabel lineId;

  /** Editable combo box to choose destination. */
  private FilterComboBox filterComboBox;

  /**
   * ToolBarPanelIdea2's constructor.
   */
  public ToolBarPanel() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.filterComboBox = new FilterComboBox(FilterComboBox.populateArray());
    this.initComponents();
  }

  // Accessors
  /** Get the jLabel which contains the currentLineId.
   *
   * @return JLabel currentLineId
   */
  public JLabel getLineId() {
    return lineId;
  }

  /** The JLabel currentLineId.
   *
   * @param lineIdToSet current line id
   */
  public void setLineId(final JLabel lineIdToSet) {
    this.lineId = lineIdToSet;
  }

  /** Get the editable combo box which choose the destination.
   *
   * @return FilterComboBox filterComboBox
   */
  public FilterComboBox getFilterComboBox() {
    return filterComboBox;
  }

  /** Set the editable combo box which choose the destination.
   *
   * @param filterComboBoxToSet editable combo box to choose destination
   */
  public void setFilterComboBox(final FilterComboBox filterComboBoxToSet) {
    this.filterComboBox = filterComboBoxToSet;
  }

  /**
   * Init all the different buttons.
   */
  private void initComponents() {
    NoneSelectedButtonGroup actionButtonGroup = new NoneSelectedButtonGroup();

    // Adding panels to the toolbar
    this.add(this.initStationPanel());
    this.add(this.initLinePanel(actionButtonGroup));
    this.add(this.initAreaPanel(actionButtonGroup));
    this.add(this.initEventPanel());
    this.add(this.initDestinationPanel());
    this.add(this.initRunSimulationPanel());
  }

  private JPanel initRunSimulationPanel() {
    // Run simulation panel init
    JPanel runSimulationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    runSimulationPanel.setPreferredSize(new Dimension(
        RUN_SIMULATION_PANEL_WIDTH, RUN_SIMULATION_PANEL_HEIGHT));
    runSimulationPanel.setMaximumSize(new Dimension(
        RUN_SIMULATION_PANEL_MAX_WIDTH, RUN_SIMULATION_PANEL_MAX_HEIGHT));
    runSimulationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder runSimulationPanelBorder = new TitledBorder("Run Simulation");
    runSimulationPanel.setBorder(runSimulationPanelBorder);
    JButton runSimulationBtn = new JButton("RUN");
    runSimulationBtn.setName(ActionRunSimulation.ACTION_NAME);
    runSimulationBtn.addActionListener(e -> {
      try {
        ActionRunSimulation.getInstance()
            .runSimulation();
      } catch (InterruptedException | IOException ex) {
        Thread.currentThread().interrupt();
      }
    });
    runSimulationBtn.setFocusable(false);
    runSimulationBtn.setPreferredSize(new Dimension(RUN_SIMULATION_BTN_WIDTH,
        RUN_SIMULATION_BTN_HEIGHT));

    runSimulationPanel.add(runSimulationBtn, BorderLayout.CENTER);
    runSimulationPanel.add(Box.createVerticalGlue());
    return runSimulationPanel;
  }

  private JPanel initDestinationPanel() {
    // Destination panel init
    JPanel destinationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    destinationPanel.setPreferredSize(new Dimension(DESTINATION_PANEL_WIDTH,
        DESTINATION_PANEL_HEIGHT));
    destinationPanel.setMaximumSize(new Dimension(DESTINATION_PANEL_MAX_WIDTH,
        DESTINATION_PANEL_MAX_HEIGHT));
    destinationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder destinationPanelBorder = new TitledBorder("Destination");
    destinationPanel.setBorder(destinationPanelBorder);
    filterComboBox.setPreferredSize(new Dimension(FILTER_COMBO_BOX_WIDTH,
        FILTER_COMBO_BOX_HEIGHT));
    filterComboBox.addActionListener(e -> Data.getInstance().setCurrentCity(
        Objects.requireNonNull(filterComboBox.getSelectedItem()).toString()));
    destinationPanel.add(this.filterComboBox);
    destinationPanel.add(Box.createVerticalGlue());
    return destinationPanel;
  }

  /**
   * Init the event panel.
   *
   * @return JPanel eventPanel
   */
  private JPanel initEventPanel() {
    // Event panel init
    JPanel eventPanel = new JPanel();
    eventPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
    TitledBorder eventPanelBorder = new TitledBorder("Event");
    eventPanel.setBorder(eventPanelBorder);

    // Event panel components
    JPanel firstEventRowPanel = new JPanel();
    firstEventRowPanel.setLayout(new BoxLayout(firstEventRowPanel,
        BoxLayout.Y_AXIS));
    firstEventRowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firstEventRowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    JButton addEventBtn = new JButton("ADD");
    addEventBtn.setName(ActionMetroEvent.ADD_EVENT);
    addEventBtn.addActionListener(e -> ActionMetroEvent.getInstance()
        .addEvent());
    addEventBtn.setFocusable(false);
    JButton showEventsBtn = new JButton("SHOW LIST");
    showEventsBtn.setFocusable(false);

    firstEventRowPanel.add(addEventBtn);
    firstEventRowPanel.add(showEventsBtn);
    eventPanel.add(firstEventRowPanel);
    eventPanel.add(Box.createVerticalGlue());
    return eventPanel;
  }

  /**
   * Init the area panel.
   *
   * @param actionButtonGroup action button group shared with other panels
   *
   * @return JPanel areaPanel
   */
  private JPanel initAreaPanel(final NoneSelectedButtonGroup
                                 actionButtonGroup) {
    // ? Area panel init
    JPanel areaPanel = new JPanel();
    areaPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    areaPanel.setLayout(new BoxLayout(areaPanel, BoxLayout.Y_AXIS));
    TitledBorder areaPanelBorder = new TitledBorder("Area");
    areaPanel.setBorder(areaPanelBorder);

    // Area panel components
    JPanel firstAreaRowPanel = new JPanel();
    firstAreaRowPanel.setLayout(new BoxLayout(firstAreaRowPanel,
        BoxLayout.Y_AXIS));
    firstAreaRowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    firstAreaRowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    JButton addAreaBtn = new JButton("ADD");
    addAreaBtn.setName(ActionArea.ACTION_NAME);
    addAreaBtn.addActionListener(e -> ActionArea.getInstance().addArea());
    addAreaBtn.setFocusable(false);

    JToggleButton deleteAreaBtn = new JToggleButton(ToolBarPanel
        .DELETE_TEXT_BTN);
    deleteAreaBtn.setFocusable(false);
    actionButtonGroup.add(deleteAreaBtn);
    firstAreaRowPanel.add(addAreaBtn);
    firstAreaRowPanel.add(deleteAreaBtn);
    areaPanel.add(firstAreaRowPanel);
    // Fill the height gap
    areaPanel.add(Box.createVerticalGlue());
    return areaPanel;
  }


  /**
   * Init the line panel.
   *
   * @param actionButtonGroup action button group shared with other panels
   *
   * @return JPanel linePanel
   */
  private JPanel initLinePanel(final NoneSelectedButtonGroup
                                 actionButtonGroup) {
    // Line panel init
    JPanel linePanel = new JPanel();
    linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.Y_AXIS));
    linePanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder linePanelBorder = new TitledBorder("Line");
    linePanel.setBorder(linePanelBorder);

    // Line panel components
    JPanel firstLineRowPanel = new JPanel();
    firstLineRowPanel.setLayout(new BoxLayout(firstLineRowPanel,
        BoxLayout.X_AXIS));
    firstLineRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JButton addLineBtn = new JButton("ADD");
    addLineBtn.setName(ActionLine.ADD_LINE);
    addLineBtn.addActionListener(e -> ActionLine.getInstance().addLine());
    addLineBtn.setFocusable(false);

    JToggleButton deleteLineBtn = new JToggleButton(ToolBarPanel
        .DELETE_TEXT_BTN);
    deleteLineBtn.setFocusable(false);
    actionButtonGroup.add(deleteLineBtn);
    firstLineRowPanel.add(addLineBtn);
    firstLineRowPanel.add(deleteLineBtn);

    JPanel secondLineRowPanel = new JPanel();
    secondLineRowPanel.setLayout(new BoxLayout(secondLineRowPanel,
        BoxLayout.X_AXIS));
    secondLineRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.lineId = new JLabel("None");
    JButton btnIncrement = new JButton("↑");
    btnIncrement.setName(ActionLine.INCREMENT_LINE);
    btnIncrement.addActionListener(e -> ActionLine.getInstance()
        .incrementLine());
    btnIncrement.setFocusable(false);

    JButton btnDecrement = new JButton("↓");
    btnDecrement.setName(ActionLine.DECREMENT_LINE);
    btnDecrement.addActionListener(e -> ActionLine.getInstance()
        .decrementLine());
    btnDecrement.setFocusable(false);

    JLabel lineIdLabel = new JLabel(" Selected line id : ");
    JLabel whiteSpace = new JLabel("    ");
    secondLineRowPanel.add(lineIdLabel);
    secondLineRowPanel.add(this.lineId);
    secondLineRowPanel.add(whiteSpace);
    secondLineRowPanel.add(btnIncrement);
    secondLineRowPanel.add(btnDecrement);
    linePanel.add(firstLineRowPanel);
    linePanel.add(secondLineRowPanel);
    // Fill the height gap
    linePanel.add(Box.createVerticalGlue());
    return linePanel;
  }

  /**
   * Init the station panel.
   *
   * @return JPanel stationPanel
   */
  private JPanel initStationPanel() {
    // Station panel init
    JPanel stationPanel = new JPanel();
    stationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    JPanel firstStationRowPanel = new JPanel();
    firstStationRowPanel.setLayout(new BorderLayout());
    firstStationRowPanel.setLayout(new BoxLayout(firstStationRowPanel,
        BoxLayout.X_AXIS));
    firstStationRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.Y_AXIS));
    TitledBorder stationPanelBorder = new TitledBorder("Station");
    stationPanel.setBorder(stationPanelBorder);

    // Station panel components
    JButton addStationBtn = new JButton("ADD");
    addStationBtn.addActionListener(e -> ActionStation.getInstance()
        .addStation());
    addStationBtn.setName(ActionStation.ACTION_NAME);
    addStationBtn.setFocusable(false);

    JButton deleteStationBtn = new JButton(ToolBarPanel.DELETE_TEXT_BTN);
    deleteStationBtn.setFocusable(false);
    JButton mergeStationsBtn = new JButton("MERGE");
    mergeStationsBtn.setFocusable(false);
    firstStationRowPanel.add(addStationBtn);
    firstStationRowPanel.add(deleteStationBtn);
    firstStationRowPanel.add(mergeStationsBtn);
    JPanel secondStationRowPanel = new JPanel();
    secondStationRowPanel.setLayout(new BoxLayout(secondStationRowPanel,
        BoxLayout.X_AXIS));
    secondStationRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton addFromEndOption = new JRadioButton("Add from the end"
        + " of the line");
    JRadioButton addFromStartOption = new JRadioButton("Add from the"
        + " start of the line");
    addFromEndOption.setSelected(true);
    buttonGroup.add(addFromEndOption);
    buttonGroup.add(addFromStartOption);
    secondStationRowPanel.add(addFromEndOption);
    secondStationRowPanel.add(addFromStartOption);

    stationPanel.add(firstStationRowPanel);
    stationPanel.add(secondStationRowPanel);
    stationPanel.add(Box.createVerticalGlue());
    return stationPanel;
  }
}
