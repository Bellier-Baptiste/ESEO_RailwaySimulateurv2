/**
 * Class part of the view package of the application.
 */

package view;

import controller.KeyboardTool;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Main windows Singleton which contain all the different panels.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
@SuppressWarnings("serial")
public final class MainWindow extends JFrame {

  // constants
  /** Width of the window. */
  public static final int WINDOW_WIDTH = 1000;
  /** Height of the window. */
  public static final int WINDOW_HEIGHT = 600;
  /** Title of the window. */
  private static final String TITLE = "Railway-editor";

  // attributes
  /** Singleton instance of the class. */
  private static MainWindow instance;
  /** Main panel of the application. */
  private MainPanel mainPanel;
  /** ToolBar panel of the application. */
  private ToolBarPanelIdea2 toolBarPanelIdea2;
  /** Event recap panel of the application. */
  private EventRecap eventRecapPanel;
  /** Menu bar of the application. */
  private JMenuBar appMenuBar;

  /**
   * Constructor, initialize window and panels.
   */
  private MainWindow() {
    this.setResizable(true);
    this.setLayout(new BorderLayout());
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setTitle(TITLE);
    this.setLocationRelativeTo(null);
    KeyboardTool kbt = new KeyboardTool(this.getMainPanel());
    this.addKeyListener(kbt);

    this.mainPanel = MainPanel.getInstance();
    this.getContentPane().add(this.mainPanel,
        BorderLayout.CENTER);

    this.appMenuBar = MenuBar.getInstance();
    this.setJMenuBar(this.appMenuBar);

    this.toolBarPanelIdea2 = new ToolBarPanelIdea2();
    this.toolBarPanelIdea2.setVisible(true);
    this.getContentPane().add(this.toolBarPanelIdea2, BorderLayout.NORTH);

    this.eventRecapPanel = EventRecap.getInstance();
    this.getContentPane().add(this.eventRecapPanel, BorderLayout.WEST);

    this.revalidate();

    BufferedImage source;
    try {
      source = ImageIO.read(getClass().getResource(
          "/resources/railwayEditorIcon3.png"));
      ImageIcon img = new ImageIcon(source);
      this.setIconImage(img.getImage());
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.pack();
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.mainPanel.requestFocusInWindow();
  }

  //accessors

  /**
   * get the main panel.
   *
   * @return MainPanel mainPanel
   */
  public MainPanel getMainPanel() {
    return mainPanel;
  }

  /**
   * set the MainPanel.
   *
   * @param mainPanelToSet Map panel
   */
  public void setMainPanel(final MainPanel mainPanelToSet) {
    this.mainPanel = mainPanelToSet;
  }


  /**
   * get the eventRecapPanel.
   *
   * @return EventRecapPanel eventRecapPanel.
   */
  public EventRecap getEventRecapPanel() {
    return eventRecapPanel;
  }

  /**
   * set the eventRecapPanel.
   *
   * @param eventRecapPaneToSet panel which recap the added events
   */
  public void setEventRecapPanel(final EventRecap eventRecapPaneToSet) {
    this.eventRecapPanel = eventRecapPaneToSet;
  }

  /**
   * Return Singleton.
   *
   * @return MainWindos instance
   */
  public static MainWindow getInstance() {
    if (instance == null) {
      instance = new MainWindow();
    }
    return instance;
  }

  /**
   * Get the toolbar panel.
   *
   * @return the toolbar panel
   */
  public ToolBarPanelIdea2 getToolBarPanelIdea2() {
    return toolBarPanelIdea2;
  }

}
