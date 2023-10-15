/**
 * Class part of the view package of the application.
 */

package view;

import controller.ActionArea;
import controller.ActionLine;
import controller.ActionMetroEvent;
import controller.ActionRunSimulation;
import controller.ActionStation;
import data.Data;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Objects;
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

/**
 * ToolBar panel which contains all the action buttons.
 *
 * @author Arthur Lagarce
 *
 */
public class ToolBarPanelIdea2 extends JToolBar {

  /**
   * Text of the button with a delete action.
   */
  public static final String DELETE_TEXT_BTN = "DELETE";

  // attributes

  /**
   * Current line id.
   */
  private JLabel lineId;

  /**
   * Editable combo box to choose destination.
   */
  private FilterComboBox filterComboBox;

  /**
   * ToolBarPanelIdea2's constructor.
   */
  public ToolBarPanelIdea2() {
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
    // ? Station panel init
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

    JButton deleteStationBtn = new JButton(ToolBarPanelIdea2.DELETE_TEXT_BTN);
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

    // ? Line panel init
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

    NoneSelectedButtonGroup actionButtonGroup = new NoneSelectedButtonGroup();
    JToggleButton deleteLineBtn = new JToggleButton(ToolBarPanelIdea2
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

    JToggleButton deleteAreaBtn = new JToggleButton(ToolBarPanelIdea2
        .DELETE_TEXT_BTN);
    deleteAreaBtn.setFocusable(false);
    actionButtonGroup.add(deleteAreaBtn);
    firstAreaRowPanel.add(addAreaBtn);
    firstAreaRowPanel.add(deleteAreaBtn);
    areaPanel.add(firstAreaRowPanel);
    // Fill the height gap
    areaPanel.add(Box.createVerticalGlue());


    // ? Event panel init
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


    // ? Destination panel init
    JPanel destinationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    destinationPanel.setPreferredSize(new Dimension(180, 60));
    destinationPanel.setMaximumSize(new Dimension(180, 90));
    destinationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder destinationPanelBorder = new TitledBorder("Destination");
    destinationPanel.setBorder(destinationPanelBorder);
    filterComboBox.setPreferredSize(new Dimension(140, 30));
    filterComboBox.addActionListener(e -> Data.getInstance().setCurrentCity(
        Objects.requireNonNull(filterComboBox.getSelectedItem()).toString()));
    destinationPanel.add(this.filterComboBox);
    destinationPanel.add(Box.createVerticalGlue());

    // ? Run simulation panel init
    JPanel runSimulationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    runSimulationPanel.setPreferredSize(new Dimension(100, 60));
    runSimulationPanel.setMaximumSize(new Dimension(100, 90));
    runSimulationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder runSimulationPanelBorder = new TitledBorder("Run Simulation");
    runSimulationPanel.setBorder(runSimulationPanelBorder);
    JButton runSimulationBtn = new JButton("RUN");
    runSimulationBtn.setName(ActionRunSimulation.ACTION_NAME);
    runSimulationBtn.addActionListener(e -> ActionRunSimulation.getInstance()
        .runSimulation());
    runSimulationBtn.setFocusable(false);
    runSimulationBtn.setPreferredSize(new Dimension(80, 30));

    runSimulationPanel.add(runSimulationBtn, BorderLayout.CENTER);
    runSimulationPanel.add(Box.createVerticalGlue());

    // Adding panels to the toolbar
    this.add(stationPanel);
    this.add(linePanel);
    this.add(areaPanel);
    this.add(eventPanel);
    this.add(destinationPanel);
    this.add(runSimulationPanel);
  }
}
