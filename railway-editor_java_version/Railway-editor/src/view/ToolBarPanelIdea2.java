package view;

import controller.ActionExport;
import controller.ActionManager;
import controller.ActionOpen;
import controller.ActionThemeMode;
import data.Data;
import main.RailwayEditor;
import org.jdesktop.swingx.JXRadioGroup;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ToolBar panel which contains all the action buttons
 *
 * @author Arthur Lagarce
 *
 */
public class ToolBarPanelIdea2 extends JToolBar {

  private static final Dimension DIMENSIONS_BUTTON = new Dimension(180, 25);

  private static final long serialVersionUID = 1L;
  // constants
  public static final int DEFAULT_WIDTH = 1920;
  public static final int DEFAULT_HEIGHT = 35;
  final static boolean shouldFill = true;
  final static boolean shouldWeightX = true;
  final static boolean RIGHT_TO_LEFT = false;

  public static final Color BACKGROUND_COLOR = new Color(224, 224, 224);


  // attributes
  private ActionManager actionManager;
  private JLabel lineId;
  private FilterComboBox filterComboBox;
  private MainPanel mainPanel;
  private MainWindow mainWindow;

  /**
   * constructor
   */
  public ToolBarPanelIdea2(MainPanel mainPanel, MainWindow mainWindow, ActionManager actionManager) {
    this.mainPanel = mainPanel;
    this.mainWindow = mainWindow;
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    //this.setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, 50));
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
    JButton addStationBtn = new JButton("ADD");
    try {
      addStationBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          actionManager.addStation();
        }
      });
    }
    catch (Exception ex) {
      System.out.println(ex);
    }
    addStationBtn.setFocusable(false);
    JButton deleteStationBtn = new JButton("DELETE");
    deleteStationBtn.setFocusable(false);
    JButton mergeStationsBtn = new JButton("MERGE");
    mergeStationsBtn.setFocusable(false);
//    actionButtonGroup.add(addStationBtn);
//    actionButtonGroup.add(deleteStationBtn);
//    actionButtonGroup.add(mergeStationsBtn);
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
    JButton addLineBtn = new JButton("ADD");
    addLineBtn.setFocusable(false);
    try {
      addLineBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          actionManager.addLine();
        }
      });
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    JToggleButton deleteLineBtn = new JToggleButton("DELETE");
    deleteLineBtn.setFocusable(false);
    //actionButtonGroup.add(addLineBtn);
    actionButtonGroup.add(deleteLineBtn);
    firstLineRowPanel.add(addLineBtn);
    firstLineRowPanel.add(deleteLineBtn);

    JPanel secondLineRowPanel = new JPanel();
    secondLineRowPanel.setLayout(new BoxLayout(secondLineRowPanel, BoxLayout.X_AXIS));
    secondLineRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lineIdLabel = new JLabel(" Selected line id : ");
    this.lineId = new JLabel("None");
    JButton btnIncrement = new JButton();
    btnIncrement.setText("↑");
    btnIncrement.setFocusable(false);
    btnIncrement.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actionManager.incrementLine();
      }
    });
    JButton btnDecrement = new JButton();
    btnDecrement.setText("↓");
    btnDecrement.setFocusable(false);
    btnDecrement.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actionManager.decrementLine();
      }
    });
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
    JButton addAreaBtn = new JButton("ADD");
    addAreaBtn.setFocusable(false);
    try {
      addAreaBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          actionManager.addArea();
        }
      });
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    JToggleButton deleteAreaBtn = new JToggleButton("DELETE");
    deleteAreaBtn.setFocusable(false);
    //actionButtonGroup.add(addAreaBtn);
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
    JButton addEventBtn = new JButton("ADD");
    addEventBtn.setFocusable(false);
    try {
      addEventBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          actionManager.addEvent();
        }
      });
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
    filterComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Data.getInstance().setCurrentCity(filterComboBox.getSelectedItem().toString());
      }

    });
    destinationPanel.add(this.filterComboBox);
    destinationPanel.add(Box.createVerticalGlue());

    // ? Run simulation panel init
    JPanel runSimulationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    runSimulationPanel.setPreferredSize(new Dimension(100, 60));
    runSimulationPanel.setMaximumSize(new Dimension(100, 90));
    runSimulationPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    //runSimulationPanel.setLayout(new BoxLayout(runSimulationPanel, BoxLayout.Y_AXIS));
    TitledBorder runSimulationPanelBorder = new TitledBorder("Run Simulation");
    runSimulationPanel.setBorder(runSimulationPanelBorder);
    JButton runSimulationBtn = new JButton("RUN");
    runSimulationBtn.setFocusable(false);
    runSimulationBtn.setPreferredSize(new Dimension(80, 30));

    runSimulationBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actionManager.runSimulation();
      }
    });
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

  private Image getCroppedImage(String address) throws IOException {
    BufferedImage source = ImageIO.read(getClass().getResource(address));

    boolean flag = false;
    int upperBorder = -1;
    do {
      upperBorder++;
      for (int c1 = 0; c1 < source.getWidth(); c1++) {
        if (source.getRGB(c1, upperBorder) != Color.white.getRGB()) {
          flag = true;
          break;
        }
      }

      if (upperBorder >= source.getHeight())
        flag = true;
    } while (!flag);

    BufferedImage destination = new BufferedImage(source.getWidth(), source.getHeight() - upperBorder,
        BufferedImage.TYPE_INT_ARGB);
    destination.getGraphics().drawImage(source, 0, upperBorder * -1, null);

    return destination;
  }

}
