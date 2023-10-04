package view;

import controller.KeyboardTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
  private ToolBarPanelIdea2 toolBarPanelIdea2;
	private EventRecap eventRecapPanel;
	public boolean intro = true;
	private JMenuBar menuBar;
//  private FilterComboBox filterComboBox;
//  private JPanel filterComboPanel;

	/**
	 * Constructor, initialize window and panels
	 */
	private MainWindow() {
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setTitle(TITLE);
    this.setLocationRelativeTo(null);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    KeyboardTool  kbt  =new KeyboardTool(this.getMainPanel());
    this.addKeyListener(kbt);

		this.mainPanel = new MainPanel(MainPanel.PANEL_WIDTH_DEFAULT,MainPanel.PANEL_HEIGHT_DEFAULT);
    this.getContentPane().add(this.mainPanel,
        BorderLayout.CENTER);

		this.menuBar = MenuBar.getInstance(this, this.mainPanel);
		this.setJMenuBar(this.menuBar);

		this.toolBarPanelIdea2 = new ToolBarPanelIdea2(this.mainPanel, this);
    this.toolBarPanelIdea2.setVisible(true);
		this.getContentPane().add(this.toolBarPanelIdea2, BorderLayout.NORTH);

		this.eventRecapPanel = new EventRecap();
    this.getContentPane().add(this.eventRecapPanel,BorderLayout.WEST);

    this.revalidate();

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

}
