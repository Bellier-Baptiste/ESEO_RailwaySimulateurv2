package view;

import controller.*;
import data.Data;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


/**
 * ToolBar panel which contains all the action buttons
 *
 * @author Arthur Lagarce
 *
 */
public class ToolBarPanelIdea2 extends JToolBar {

  private static final long serialVersionUID = 1L;

  // attributes
  private final ActionManager actionManager;
  private JLabel lineId;
  private FilterComboBox filterComboBox;
  private MainPanel mainPanel;
  private EventWindow eventWindow;


  /**
   * constructor
   */
  public ToolBarPanelIdea2(MainPanel mainPanel, MainWindow mainWindow, ActionManager actionManager) {
    this.mainPanel = mainPanel;
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.actionManager = mainWindow.getActionManager();
    this.filterComboBox = new FilterComboBox(FilterComboBox.populateArray());
    this.initComponents();
  }

  // Accessors
  /**get the jLabel which contains the currentLineId.
   * @return JLabel currentLineId
   */
  public JLabel getLineId() {
    return lineId;
  }

  /**the the JLabel currentLineId.
   * @param lineId current line id
   */
  public void setLineId(JLabel lineId) {
    this.lineId = lineId;
  }

  /**get the editable combo box which choose the destination.
   * @return FilterComboBox filterComboBox
   */
  public FilterComboBox getFilterComboBox() {
    return filterComboBox;
  }

  /**set the editable combo box which choose the destination.
   * @param filterComboBox editable combo box to choose destination
   */
  public void setFilterComboBox(FilterComboBox filterComboBox) {
    this.filterComboBox = filterComboBox;
  }

  /**
   * init all the different button in a Grid Layout
   */
  private void initComponents() {
    // ? Station panel init
    JPanel stationPanel = new JPanel();
    stationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    JPanel firstStationRowPanel = new JPanel();
    firstStationRowPanel.setLayout(new BorderLayout());
    firstStationRowPanel.setLayout(new BoxLayout(firstStationRowPanel, BoxLayout.X_AXIS));
    firstStationRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    stationPanel.setLayout(new BoxLayout(stationPanel, BoxLayout.Y_AXIS));
    TitledBorder stationPanelBorder = new TitledBorder("Station");
    stationPanel.setBorder(stationPanelBorder);

    // Station panel components
    NoneSelectedButtonGroup actionButtonGroup = new NoneSelectedButtonGroup();
    JButton addStationBtn = new JButton(new ActionStation(this.mainPanel, ActionLine.getInstance()));
    addStationBtn.setName(ActionStation.ACTION_NAME);
    addStationBtn.setText("ADD");
    addStationBtn.setFocusable(false);

    JButton deleteStationBtn = new JButton("DELETE");
    deleteStationBtn.setFocusable(false);
    JButton mergeStationsBtn = new JButton("MERGE");
    mergeStationsBtn.setFocusable(false);
    firstStationRowPanel.add(addStationBtn);
    firstStationRowPanel.add(deleteStationBtn);
    firstStationRowPanel.add(mergeStationsBtn);

    JPanel secondStationRowPanel = new JPanel();
    secondStationRowPanel.setLayout(new BoxLayout(secondStationRowPanel, BoxLayout.X_AXIS));
    secondStationRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton addFromEndOption = new JRadioButton("Add from the end of the line");
    JRadioButton addFromStartOption = new JRadioButton("Add from the start of the line");
    addFromEndOption.setSelected(true);
    buttonGroup.add(addFromEndOption);
    buttonGroup.add(addFromStartOption);
    secondStationRowPanel.add(addFromEndOption);
    secondStationRowPanel.add(addFromStartOption);

    stationPanel.add(firstStationRowPanel);
    stationPanel.add(secondStationRowPanel);
    stationPanel.add(Box.createVerticalGlue());

    // ? Line panel init
    JPanel linePanel= new JPanel();
    linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.Y_AXIS));
    linePanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder linePanelBorder = new TitledBorder("Line");
    linePanel.setBorder(linePanelBorder);

    // Line panel components
    JPanel firstLineRowPanel = new JPanel();
    firstLineRowPanel.setLayout(new BoxLayout(firstLineRowPanel, BoxLayout.X_AXIS));
    firstLineRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JButton addLineBtn = new JButton(ActionLine.getInstance().new ActionAddLine());
    addLineBtn.setText("ADD");
    addLineBtn.setFocusable(false);

    JToggleButton deleteLineBtn = new JToggleButton("DELETE");
    deleteLineBtn.setFocusable(false);
    actionButtonGroup.add(deleteLineBtn);
    firstLineRowPanel.add(addLineBtn);
    firstLineRowPanel.add(deleteLineBtn);

    JPanel secondLineRowPanel = new JPanel();
    secondLineRowPanel.setLayout(new BoxLayout(secondLineRowPanel, BoxLayout.X_AXIS));
    secondLineRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lineIdLabel = new JLabel(" Selected line id : ");
    this.lineId = new JLabel("None");
    JButton btnIncrement = new JButton(ActionLine.getInstance().new ActionIncrementLine());
    btnIncrement.setText("↑");
    btnIncrement.setFocusable(false);

    JButton btnDecrement = new JButton(ActionLine.getInstance().new ActionDecrementLine());
    btnDecrement.setText("↓");
    btnDecrement.setFocusable(false);

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
    firstAreaRowPanel.setLayout(new BoxLayout(firstAreaRowPanel, BoxLayout.Y_AXIS));
    firstAreaRowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    firstAreaRowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    JButton addAreaBtn = new JButton(new ActionArea());
    addAreaBtn.setName(ActionArea.ACTION_NAME);
    addAreaBtn.setText("ADD");
    addAreaBtn.setFocusable(false);

    JToggleButton deleteAreaBtn = new JToggleButton("DELETE");
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
    firstEventRowPanel.setLayout(new BoxLayout(firstEventRowPanel, BoxLayout.Y_AXIS));
    firstEventRowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firstEventRowPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    JButton addEventBtn = new JButton(ActionMetroEvent.getInstance(). new ActionAddEvent());
    addEventBtn.setText("ADD");
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
    filterComboBox.addActionListener(e -> Data.getInstance().setCurrentCity(filterComboBox.getSelectedItem().toString()));
    destinationPanel.add(this.filterComboBox);
    destinationPanel.add(Box.createVerticalGlue());

    // ? Run simulation panel init
    JPanel runSimulationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    runSimulationPanel.setPreferredSize(new Dimension(100, 60));
    runSimulationPanel.setMaximumSize(new Dimension(100, 90));
    runSimulationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    TitledBorder runSimulationPanelBorder = new TitledBorder("Run Simulation");
    runSimulationPanel.setBorder(runSimulationPanelBorder);
    JButton runSimulationBtn = new JButton(new ActionRunSimulation(this.mainPanel, this.actionManager));
    runSimulationBtn.setName(ActionRunSimulation.ACTION_NAME);
    runSimulationBtn.setText("RUN");
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

  public void hideEventWindow() {
    eventWindow.setVisible(false);
  }
}
