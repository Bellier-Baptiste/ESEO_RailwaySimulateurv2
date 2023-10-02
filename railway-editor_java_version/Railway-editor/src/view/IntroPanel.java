//package view;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Image;
//import java.awt.Insets;
//import java.awt.RenderingHints;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFileChooser;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.LookAndFeel;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//
//import controller.ActionManager;
//
///**
// * Class which show the intro panel in order to choose to load a map or to crete a new one.
// * @author arthu
// *
// */
//public class IntroPanel extends JPanel {
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//	// constants
//	public static final int PANEL_WIDTH_DEFAULT = 1100;
//	public static final int PANEL_HEIGHT_DEFAULT = 750;
//	public static final Color BACKGROUND_COLOR_DEFAULT = Color.WHITE;
//
//	// attributes
//	private JLabel newMapLabel;
//	private JLabel loadMapLabel;
//	private ActionManager actionManager;
//
//	/**
//	 * Constructor
//	 *
//	 * @param width panel width
//	 * @param height panel height
//	 * @param color panel color
//	 */
//	public IntroPanel(int width, int height, Color color) {
//		Dimension dim = new Dimension(width, height);
//		this.setPreferredSize(dim);
//		this.setBackground(color);
//		this.setLayout(new GridBagLayout());
//		actionManager = new ActionManager();
//		initComponents();
//	}
//
//	/**
//	 * init the buttons.
//	 */
//	private void initComponents() {
//
//
//		GridBagConstraints c = new GridBagConstraints();
//
//
//		try {
//			BufferedImage sourceNewMap = ImageIO.read(getClass().getResource("/resources/newMap.png"));
//			Image scaledNewMap = sourceNewMap.getScaledInstance((int)(PANEL_WIDTH_DEFAULT/2.42),(int) (PANEL_HEIGHT_DEFAULT/10.2),Image.SCALE_SMOOTH);
//			BufferedImage sourceLoadMap = ImageIO.read(getClass().getResource("/resources/LoadMap.png"));
//			Image scaledLoadMap = sourceLoadMap.getScaledInstance((int)(PANEL_WIDTH_DEFAULT/2.23),(int) (PANEL_HEIGHT_DEFAULT/10.2),Image.SCALE_SMOOTH);
//			ImageIcon newMapIcon = new ImageIcon(scaledNewMap);
//			ImageIcon loadMapIcon = new ImageIcon(scaledLoadMap);
//
//
//			newMapLabel = new JLabel(newMapIcon);
//
//
//			loadMapLabel = new JLabel(loadMapIcon);
//
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}
//
//		newMapLabel.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				MainWindow.getInstance().remove(MainWindow.getInstance().getIntroPanel());
//				MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getMainPanel(),
//						BorderLayout.CENTER);
//				MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getToolBarPanelIdea2(),
//						BorderLayout.NORTH);
//				MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getEventRecapPanel(),BorderLayout.WEST);
//				MainWindow.getInstance().revalidate();
//			}
//		});
//
//		loadMapLabel.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				LookAndFeel previousLF = UIManager.getLookAndFeel();
//				try {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//					JFileChooser fileChooser = new JFileChooser();
//					UIManager.setLookAndFeel(previousLF);
//
//					fileChooser.setDialogTitle("Specify a file to Load");
//
//					int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());
//
//					if (userSelection == JFileChooser.APPROVE_OPTION) {
//						File fileToLoad = fileChooser.getSelectedFile();
//
//						System.out.println("Load File: " + fileToLoad.getAbsolutePath());
//						MainWindow.getInstance().remove(MainWindow.getInstance().getIntroPanel());
//						MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getMainPanel(),
//								BorderLayout.CENTER);
//						MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getToolBarPanel(),
//								BorderLayout.EAST);
//						MainWindow.getInstance().getContentPane().add(MainWindow.getInstance().getEventRecapPanel(),BorderLayout.WEST);
//						MainWindow.getInstance().revalidate();
////						actionManager.importMap(fileToLoad);
//
//					}
//				} catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException
//						| ClassNotFoundException e1) {
//				}
//
//			}
//		});
//		c.anchor = GridBagConstraints.SOUTHWEST;
//		c.gridwidth = 1;
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridheight = 1;
//		c.weighty=0.8;
//		c.weightx=0.2;
//		c.insets = new Insets(0, 100, 0,0);
//		this.add(newMapLabel,c);
//		c.gridwidth = 1;
//		c.gridx = 0;
//		c.gridy = 1;
//		c.gridheight = 1;
//		c.weighty=0.2;
//		c.insets = new Insets(0, 150, 30,0);
//		this.add(loadMapLabel,c);
//	}
//
//	/* (non-Javadoc)
//	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
//	 */
//	public void paintComponent(Graphics g) {// display all panel elements
//		Graphics2D g2D = (Graphics2D) g.create();
//		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//		super.paintComponent(g);
//		try {
//			BufferedImage source = ImageIO.read(getClass().getResource("/resources/RailywayEditorIntroPanelBg.png"));
//			Image scaledImage = source.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
//			g2D.drawImage(scaledImage, 0, 0, null);
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}
//
//	}
//}
