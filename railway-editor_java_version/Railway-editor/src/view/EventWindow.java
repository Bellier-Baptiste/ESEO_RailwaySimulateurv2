/**
 * Class part of the view package of the application.
 */

package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
/**
 * JFrame of the event's edition.
 *
 * @author arthur
 */

public class EventWindow extends JFrame {
  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Singleton instance.
   */
  private static EventWindow instance;

  /**
   * Event window width.
   */
  private static final int WINDOW_WIDTH = 800;

  /**
   * Event window height.
   */
  private static final int WINDOW_HEIGHT = 270;
  /**
   * Event window title.
   */
  private static final String TITLE = "Event-editor";


  // attributes
  /**
   * Panel of the list of events available.
   */
  private ListEventPanel listEventPanel;

  /**
   * Constructor, initialize window and panels.
   */
  public EventWindow() {
    this.listEventPanel = ListEventPanel.getInstance();
    this.getContentPane().add(listEventPanel);
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setTitle(TITLE);
    try {
      BufferedImage source = ImageIO.read(
          Objects.requireNonNull(getClass().getResource(
          "/resources/railwayEditorIcon3.png")));
      ImageIcon img = new ImageIcon(source);
      this.setIconImage(img.getImage());
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.setAlwaysOnTop(true);

    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  /**
   * Create EventWindow Singleton.
   *
   * @return EventWindow instance
   */
  public static EventWindow getInstance() {
    if (instance == null) {
      instance = new EventWindow();
    }
    return instance;
  }
}
