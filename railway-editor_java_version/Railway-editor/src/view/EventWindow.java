package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.ActionManager;

/**
 * JFrame of the events edition.
 * @author arthu
 *
 */
public class EventWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constantes
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 270;
	private final String TITLE = "Event-editor";
	// attributes
	private ListEventPanel listEventPanel;

	
	/**Constructor.
	 * @param actionManager ActionManager
	 */
	public EventWindow(ActionManager actionManager) {
		this.listEventPanel = new ListEventPanel(MainPanel.PANEL_WIDTH_DEFAULT,MainPanel.PANEL_HEIGHT_DEFAULT,MainPanel.BACKGROUND_COLOR_DEFAULT,actionManager);
		this.getContentPane().add(listEventPanel);
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setTitle(TITLE);
		try {
			BufferedImage source = ImageIO.read(getClass().getResource("/resources/railwayEditorIcon3.png"));
			ImageIcon img = new ImageIcon(source);
			this.setIconImage(img.getImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setAlwaysOnTop (true);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}


	/**get the panel of the available elements.
	 * @return JPanel ListEventPanel
	 */
	public ListEventPanel getListEventPanel() {
		return listEventPanel;
	}


	/**set the panel of the available elements.
	 * @param listEventPanel panel of event editor
	 */
	public void setListEventPanel(ListEventPanel listEventPanel) {
		this.listEventPanel = listEventPanel;
	}
	
	
}
