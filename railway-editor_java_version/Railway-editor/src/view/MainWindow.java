package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.KeyboardTool;

/**Main windows Singleton which contain all the different panels
 * @author arthu
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	// constantes
	private final int WINDOW_WIDTH = 1350;
	private final int WINDOW_HEIGHT =750;
	private final String TITLE = "Railway-editor";

	
	
	// attributes
	private static MainWindow instance;
	private MainPanel mainPanel;
	private ToolBarPanel toolBarPanel;
	private IntroPanel introPanel;
	private EventRecap eventRecapPanel;
	public boolean intro = true;
	/**
	 * Constructor, initialize window and panels
	 */
	private MainWindow() {
		this.mainPanel = new MainPanel(MainPanel.PANEL_WIDTH_DEFAULT,MainPanel.PANEL_HEIGHT_DEFAULT,MainPanel.BACKGROUND_COLOR_DEFAULT);
		this.toolBarPanel = new ToolBarPanel();
		this.introPanel = new IntroPanel(IntroPanel.PANEL_WIDTH_DEFAULT, IntroPanel.PANEL_HEIGHT_DEFAULT, IntroPanel.BACKGROUND_COLOR_DEFAULT);
		this.eventRecapPanel = new EventRecap();
		this.setResizable(false);
		//this.getContentPane().add(this.toolBarPanel,BorderLayout.EAST);
		//this.getContentPane().add(this.mainPanel,BorderLayout.WEST);
		this.getContentPane().add(introPanel);
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BufferedImage source;
		try {
			source = ImageIO.read(getClass().getResource("/resources/railwayEditorIcon3.png"));
			ImageIcon img = new ImageIcon(source);
			this.setIconImage(img.getImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		this.setLocationRelativeTo(null);
		KeyboardTool  kbt  =new KeyboardTool(this.getMainPanel());
		this.addKeyListener(kbt);

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
}
