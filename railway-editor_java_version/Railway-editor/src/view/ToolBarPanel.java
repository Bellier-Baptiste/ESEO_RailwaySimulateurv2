package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import controller.ActionManager;
import data.Data;

/**
 * ToolBar panel which contains all the action buttons
 * 
 * @author Arthur Lagarce
 *
 */
public class ToolBarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constants
	public static final int LARGEUR_PAR_DEFAUT = 120;
	public static final int HAUTEUR_PAR_DEFAUT = 750;
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	public static final Color BACKGROUND_COLOR = new Color(50, 90, 254);
	public static final Color BACKGROUND_LABEL_COLOR = new Color(30, 62, 191);
	public static final Color TEXT_COLOR = new Color(231, 231, 231);
	public static final int RED = 50;
	public static final int GREEN = 90;
	public static final int BLUE = 254;

	// attributes
	private ActionManager actionManager;
	private JLabel lineId;
	private FilterComboBox filterComboBox;

	/**
	 * constructor
	 */
	public ToolBarPanel() {
		this.setPreferredSize(new Dimension(LARGEUR_PAR_DEFAUT, HAUTEUR_PAR_DEFAUT));
		this.setBackground(BACKGROUND_COLOR);
		this.actionManager = new ActionManager();
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
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

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 3;
		c.weighty = 0.02;

		// buttonIcon
		try {
			Image img = this.getCroppedImage("/resources/addStationIcPa2.png");

			JButton btnStation = new JButton("add station");
			btnStation.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnStation.setHorizontalTextPosition(SwingConstants.CENTER);
			btnStation.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
			btnStation.setFont(btnStation.getFont().deriveFont(16.0f));
			btnStation.setForeground(TEXT_COLOR);

			Dimension size = btnStation.getSize();
			Insets insets = btnStation.getInsets();
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			if (size.width > size.height) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			Image scaled = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);

			btnStation.setIcon(new ImageIcon(scaled));

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 0;
			c.gridheight = 1;

			btnStation.setBackground(BACKGROUND_COLOR);
			btnStation.setFocusPainted(false);

			btnStation.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					actionManager.addStation();
				}

			});
			this.add(btnStation, c);
		} catch (Exception ex) {
			System.out.println(ex);
		}

		lineId = new JLabel();
		lineId.setText("none");
		lineId.setFont(lineId.getFont().deriveFont(30.0f));
		lineId.setForeground(TEXT_COLOR);
		lineId.setHorizontalAlignment(JLabel.CENTER);
		lineId.setBackground(BACKGROUND_LABEL_COLOR);
		lineId.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

		lineId.setOpaque(true);
		c.ipady = 30;
		c.weighty = 0.0;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;

		this.add(lineId, c);

		JButton btnIncrement = new JButton();
		btnIncrement.setText("↑");
		btnIncrement.setBackground(BACKGROUND_COLOR);
		btnIncrement.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
		btnIncrement.setFont(btnIncrement.getFont().deriveFont(16.0f));
		btnIncrement.setForeground(TEXT_COLOR);
		btnIncrement.setFocusPainted(false);
		btnIncrement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionManager.incrementLine();
			}

		});
		c.ipady = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 0.04;

		c.gridx = 0;
		c.gridy = 2;

		this.add(btnIncrement, c);

		JButton btnDecrement = new JButton();
		btnDecrement.setText("↓");
		btnDecrement.setFont(btnDecrement.getFont().deriveFont(16.0f));
		btnDecrement.setForeground(TEXT_COLOR);
		btnDecrement.setBackground(BACKGROUND_COLOR);
		btnDecrement.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));

		btnDecrement.setFocusPainted(false);
		btnDecrement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionManager.decrementLine();
			}

		});
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.weighty = 0.02;

		c.gridy = 2;
		this.add(btnDecrement, c);
		try {
			Image img = this.getCroppedImage("/resources/addLineIcPa2.png");

			JButton btnLine = new JButton("AddLine");
			btnLine.setBackground(BACKGROUND_COLOR);
			btnLine.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnLine.setHorizontalTextPosition(SwingConstants.CENTER);
			btnLine.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
			btnLine.setFont(btnLine.getFont().deriveFont(16.0f));
			btnLine.setForeground(TEXT_COLOR);
			btnLine.setFocusPainted(false);

			Dimension size = btnLine.getSize();
			Insets insets = btnLine.getInsets();
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			if (size.width > size.height) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			Image scaled = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);

			btnLine.setIcon(new ImageIcon(scaled));

			c.gridwidth = 3;
			c.gridx = 0;
			c.weighty = 0.04;

			c.gridy = 3;

			btnLine.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					actionManager.addLine();
				}

			});
			this.add(btnLine, c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Image img = this.getCroppedImage("/resources/areaIcPA.png");

			JButton btnArea = new JButton("AddArea");
			btnArea.setBackground(BACKGROUND_COLOR);
			btnArea.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnArea.setHorizontalTextPosition(SwingConstants.CENTER);
			btnArea.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
			btnArea.setFont(btnArea.getFont().deriveFont(16.0f));
			btnArea.setForeground(TEXT_COLOR);
			btnArea.setFocusPainted(false);

			Dimension size = btnArea.getSize();
			Insets insets = btnArea.getInsets();
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			if (size.width > size.height) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			Image scaled = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);

			btnArea.setIcon(new ImageIcon(scaled));

			c.gridwidth = 3;
			c.gridx = 0;
			c.weighty = 0.04;

			c.gridy = 4;

			btnArea.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					actionManager.addArea();
				}

			});
			this.add(btnArea, c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Image img = this.getCroppedImage("/resources/eventIcPa.png");

			JButton btnEvent = new JButton("AddEvent");
			btnEvent.setBackground(BACKGROUND_COLOR);
			btnEvent.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnEvent.setHorizontalTextPosition(SwingConstants.CENTER);
			btnEvent.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
			btnEvent.setFont(btnEvent.getFont().deriveFont(16.0f));
			btnEvent.setForeground(TEXT_COLOR);
			btnEvent.setFocusPainted(false);

			Dimension size = btnEvent.getSize();
			Insets insets = btnEvent.getInsets();
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			if (size.width > size.height) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			Image scaled = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);

			btnEvent.setIcon(new ImageIcon(scaled));

			c.gridwidth = 3;
			c.gridx = 0;
			c.weighty = 0.5;
			c.gridy = 5;
			btnEvent.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					actionManager.addEvent();
				}

			});
			this.add(btnEvent, c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Image img = this.getCroppedImage("/resources/exportIcPa2.png");

			JButton btnExport = new JButton("Export");
			btnExport.setBackground(BACKGROUND_COLOR);
			btnExport.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnExport.setHorizontalTextPosition(SwingConstants.CENTER);
			btnExport.setBorder(new LineBorder(BACKGROUND_LABEL_COLOR));
			btnExport.setFont(btnExport.getFont().deriveFont(16.0f));
			btnExport.setForeground(TEXT_COLOR);
			btnExport.setFocusPainted(false);

			Dimension size = btnExport.getSize();
			Insets insets = btnExport.getInsets();
			size.width -= insets.left + insets.right;
			size.height -= insets.top + insets.bottom;
			if (size.width > size.height) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			Image scaled = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);

			btnExport.setIcon(new ImageIcon(scaled));
			c.gridwidth = 3;
			c.gridx = 0;
			c.weighty = 0.5;
			c.gridy = 6;

			btnExport.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					LookAndFeel previousLF = UIManager.getLookAndFeel();
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						JFileChooser fileChooser = new JFileChooser();
						UIManager.setLookAndFeel(previousLF);

						fileChooser.setDialogTitle("Specify a file to save");

						int userSelection = fileChooser.showSaveDialog(MainWindow.getInstance());

						if (userSelection == JFileChooser.APPROVE_OPTION) {
							File fileToSave = fileChooser.getSelectedFile();

							System.out.println("Save as file: " + fileToSave.getAbsolutePath());
							actionManager.export(fileToSave);
						}
					} catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException
							| ClassNotFoundException e1) {
					}

				}

			});
			this.add(btnExport, c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.gridwidth = 3;
		c.gridx = 0;
		c.weighty = 0.3;
		c.gridy = 7;
		JLabel destinationLabel = new JLabel("destination");
		destinationLabel.setFont(lineId.getFont().deriveFont(18.0f));
		destinationLabel.setForeground(TEXT_COLOR);
		lineId.setHorizontalAlignment(JLabel.CENTER);
		
		this.add(destinationLabel,c);
		filterComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Data.getInstance().setCurrentCity(filterComboBox.getSelectedItem().toString());
			}

		});
		c.gridwidth = 3;
		c.gridx = 0;
		c.weighty = 0;
		c.gridy = 8;
		this.add(filterComboBox, c);

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
