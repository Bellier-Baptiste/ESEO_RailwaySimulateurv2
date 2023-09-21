package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Model.Area;
import data.Data;

/**
 * Class to show a popup windows in order to distribute the population in the
 * Area.
 * 
 * @author Lagarce Arthur
 *
 */
public class AreaSetDistribution {
	public static String tourist;
	public static String student;
	public static String businessMan;
	public static String worker;
	public static String child;
	public static String retired;
	public static String unemployed;
	public static String areaDestination;
	public static final String[] DESTINATIONS = { "Affaire", "Commerciale", "Loisir", "Industrielle", "Scolaire",
			"Touristique", "Universitaire" };
	public static boolean ok = false;
	public static JComboBox<String> destinationList = new JComboBox<String>(DESTINATIONS);

	private static void display(Area area) {
		LookAndFeel previousLF = UIManager.getLookAndFeel();
		destinationList.setSelectedItem(area.getDestination());
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}

		JLabel title = new JLabel("choose Area distribution (%)");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		JTextField fieldTourist = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_TOURIST)));
		JTextField fieldStudent = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_STUDENT)));
		JTextField fieldBusinessMan = new JTextField(
				Integer.toString(area.getDistribution().get(Data.AREA_BUSINESSMAN)));
		JTextField fieldWorker = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_WORKER)));
		JTextField fieldChild = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_CHILD)));
		JTextField fieldRetired = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_RETIRED)));
		JTextField fieldUnemployed = new JTextField(Integer.toString(area.getDistribution().get(Data.AREA_UNEMPLOYED)));

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(title);
		panel.add(new JLabel("Tourist: "));
		panel.add(fieldTourist);
		panel.add(new JLabel("Student: "));
		panel.add(fieldStudent);
		panel.add(new JLabel("BusinessMan: "));
		panel.add(fieldBusinessMan);
		panel.add(new JLabel("Worker: "));
		panel.add(fieldWorker);
		panel.add(new JLabel("Child: "));
		panel.add(fieldChild);
		panel.add(new JLabel("Retired: "));
		panel.add(fieldRetired);
		panel.add(new JLabel("Unemployed: "));
		panel.add(fieldUnemployed);
		panel.add(new JLabel("Destination: "));
		panel.add(destinationList);

		int result = JOptionPane.showConfirmDialog(null, panel, "Edit distribution", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			tourist = fieldTourist.getText();
			student = fieldStudent.getText();
			businessMan = fieldBusinessMan.getText();
			worker = fieldWorker.getText();
			child = fieldChild.getText();
			retired = fieldRetired.getText();
			unemployed = fieldUnemployed.getText();
			areaDestination = destinationList.getSelectedItem().toString();
			ok = true;
		} else {
			System.out.println("Cancelled");
		}
		try {
			UIManager.setLookAndFeel(previousLF);
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**run and display the popup.
	 * @param area area clicked
	 */
	public void pop(Area area) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				display(area);
				if (ok) {
					area.setNewPart(Data.AREA_TOURIST, Integer.valueOf(tourist));
					area.setNewPart(Data.AREA_STUDENT, Integer.valueOf(student));
					area.setNewPart(Data.AREA_BUSINESSMAN, Integer.valueOf(businessMan));
					area.setNewPart(Data.AREA_WORKER, Integer.valueOf(worker));
					area.setNewPart(Data.AREA_CHILD, Integer.valueOf(child));
					area.setNewPart(Data.AREA_RETIRED, Integer.valueOf(retired));
					area.setNewPart(Data.AREA_UNEMPLOYED, Integer.valueOf(unemployed));
					area.setDestination(areaDestination);
					ok = false;
				}
			}
		});
	}

}
