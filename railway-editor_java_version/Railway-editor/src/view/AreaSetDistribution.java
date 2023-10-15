package view;

import data.Data;
import model.Area;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Objects;

/**
 * Class to show a popup windows in order to distribute the population in the
 * Area.
 *
 * @author Lagarce Arthur, Aurélie Chamouleau
 */
public class AreaSetDistribution {
  private String tourist;
  private String student;
  private String businessMan;
  private String worker;
  private String child;
  private String retired;
  private String unemployed;
  private String areaDestination;
  protected static final String[] DESTINATIONS = {"Affaire", "Commerciale",
      "Loisir", "Industrielle", "Scolaire",
      "Touristique", "Universitaire"};
  private boolean ok = false;
  private final JComboBox<String> destinationList = new JComboBox<>(
      DESTINATIONS);

  private void display(Area area) {
    LookAndFeel previousLf = UIManager.getLookAndFeel();
    destinationList.setSelectedItem(area.getDestination());
    JLabel title = new JLabel("choose Area distribution (%)");
    title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    JTextField fieldTourist = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_TOURIST)));
    JTextField fieldStudent = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_STUDENT)));
    JTextField fieldBusinessMan = new JTextField(
        Integer.toString(area.getDistribution().get(Data.AREA_BUSINESSMAN)));
    JTextField fieldWorker = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_WORKER)));
    JTextField fieldChild = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_CHILD)));
    JTextField fieldRetired = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_RETIRED)));
    JTextField fieldUnemployed = new JTextField(Integer.toString(
        area.getDistribution().get(Data.AREA_UNEMPLOYED)));

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

    int result = JOptionPane.showConfirmDialog(null, panel,
        "Edit distribution", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      tourist = fieldTourist.getText();
      student = fieldStudent.getText();
      businessMan = fieldBusinessMan.getText();
      worker = fieldWorker.getText();
      child = fieldChild.getText();
      retired = fieldRetired.getText();
      unemployed = fieldUnemployed.getText();
      areaDestination = Objects.requireNonNull(
          destinationList.getSelectedItem()).toString();
      ok = true;
    }
    try {
      UIManager.setLookAndFeel(previousLf);
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

  }

  /**
   * run and display the popup.
   *
   * @param area area clicked
   */
  public void pop(Area area) {
    EventQueue.invokeLater(() -> {
      display(area);
      if (ok) {
        area.setNewPart(Data.AREA_TOURIST, Integer.parseInt(tourist));
        area.setNewPart(Data.AREA_STUDENT, Integer.parseInt(student));
        area.setNewPart(Data.AREA_BUSINESSMAN, Integer.parseInt(businessMan));
        area.setNewPart(Data.AREA_WORKER, Integer.parseInt(worker));
        area.setNewPart(Data.AREA_CHILD, Integer.parseInt(child));
        area.setNewPart(Data.AREA_RETIRED, Integer.parseInt(retired));
        area.setNewPart(Data.AREA_UNEMPLOYED, Integer.parseInt(unemployed));
        area.setDestination(areaDestination);
        ok = false;
      }
    });
  }
}
