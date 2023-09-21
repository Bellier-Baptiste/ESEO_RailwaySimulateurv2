package view;

import controller.ActionManager;
import controller.KeyboardTool;
import data.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Filter;

/**Main windows Singleton which contain all the different panels
 * @author arthu
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	// constantes
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 600;
	private final String TITLE = "Railway-editor";
  private boolean isDarkMode = true;
	
	// attributes
	private static MainWindow instance;
	private MainPanel mainPanel;
	private ToolBarPanel toolBarPanel;
  private ToolBarPanelIdea2 toolBarPanelIdea2;
	private IntroPanel introPanel;
	private EventRecap eventRecapPanel;
	public boolean intro = true;
	private ActionManager actionManager;
	private JMenuBar menuBar;
//  private FilterComboBox filterComboBox;
//  private JPanel filterComboPanel;

	/**
	 * Constructor, initialize window and panels
	 */
	private MainWindow() {
		this.mainPanel = new MainPanel(MainPanel.PANEL_WIDTH_DEFAULT,MainPanel.PANEL_HEIGHT_DEFAULT);
		this.setLayout(new BorderLayout());
		this.actionManager = new ActionManager();
		this.menuBar = MenuBar.getInstance(this, this.mainPanel, this.actionManager);

		this.setJMenuBar(this.menuBar);
		this.toolBarPanelIdea2 = new ToolBarPanelIdea2(this.mainPanel, this, this.actionManager);
    this.toolBarPanelIdea2.setVisible(true);
		this.getContentPane().add(this.toolBarPanelIdea2, BorderLayout.NORTH);

//    this.filterComboPanel = new JPanel(new FlowLayout());
//    this.filterComboBox = new FilterComboBox(FilterComboBox.populateArray());
//    filterComboBox.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//
//        Data.getInstance().setCurrentCity(filterComboBox.getSelectedItem().toString());
//      }
//
//    });
//    this.filterComboPanel.add(this.filterComboBox);
//    this.filterComboPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//    this.mainPanel.add(filterComboPanel, BorderLayout.SOUTH);


// Ajoute en haut
//		this.toolBarPanel = new ToolBarPanel();
//		this.toolBarPanel.setVisible(true);
//		this.eventRecapPanel = new EventRecap();
		this.setResizable(true);
    this.setLocationRelativeTo(null);
    KeyboardTool  kbt  =new KeyboardTool(this.getMainPanel());
    this.addKeyListener(kbt);

    this.getContentPane().add(this.mainPanel,
        BorderLayout.CENTER);
    //this.getContentPane().add(this.toolBarPanelIdea2,
      //  BorderLayout.NORTH);
//		this.getContentPane().add(this.toolBarPanel, BorderLayout.EAST);
//    this.getContentPane().add(this.eventRecapPanel,BorderLayout.WEST);
    //this.revalidate();
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);

    BufferedImage source;
		try {
			source = ImageIO.read(getClass().getResource("/resources/railwayEditorIcon3.png"));
			ImageIcon img = new ImageIcon(source);
			this.setIconImage(img.getImage());
		} catch (IOException e) {
			e.printStackTrace();
		}

    this.pack();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainPanel.requestFocusInWindow();
  }

	//accessors
	/**get the main panel.
	 * @return MainPanel mainPanel
	 */
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	/**set the MainPanel
	 * @param mainPanel Map panel
	 */
	public void setMainPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**get the intro panel
	 * @return IntroPanel introPanel
	 */
	public IntroPanel getIntroPanel() {
		return introPanel;
	}
	
	/**get the eventRecapPanel.
	 * @return EventRecapPanel eventRecapPanel.
	 */
	public EventRecap getEventRecapPanel() {
		return eventRecapPanel;
	}

	/**set the eventRecapPanel.
	 * @param eventRecapPanel panel which recap the added events
	 */
	public void setEventRecapPanel(EventRecap eventRecapPanel) {
		this.eventRecapPanel = eventRecapPanel;
	}

	/**get the toolbar panel.
	 * @return ToolbarPane toolbarPanel
	 */
	public ToolBarPanel getToolBarPanel() {
		return toolBarPanel;
	}

	/**set the toolBar panel.
	 * @param toolBarPanel panel with the actions buttons
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		this.toolBarPanel = toolBarPanel;
	}
	
	



	/**Create Singleton
	 * @return MainWindos instance
	 */
	public static MainWindow getInstance() {
		if (instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}

  public ToolBarPanelIdea2 getToolBarPanelIdea2() {
    return toolBarPanelIdea2;
  }

	public ActionManager getActionManager() {
		return this.actionManager;
	}
}
