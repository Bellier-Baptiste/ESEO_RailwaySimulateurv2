package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Popup to inform an imminent merge
 *
 * @author arthu
 */
public class Popup {
  private static int jp;
  private int choice;
  private Consumer<Object> observer;

  private static void display() {
    JLabel title = new JLabel("Station merge");
    title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(title);
    String messageAide = "you are about to merge station close this dialog box \n" +
      " and select the second station  you want to merge";
    jp = JOptionPane.showConfirmDialog(panel, messageAide, "press cancel to abort the operation", JOptionPane.OK_CANCEL_OPTION);
  }

  /**
   * get the JoptionPane
   *
   * @return JoptionPane jp
   */
  public int getJp() {
    return jp;
  }


  /**
   * get the user's choice
   *
   * @return int choice
   */
  public int getChoice() {
    return choice;
  }

  /**
   * set the user choice
   *
   * @param choice ok or cancel
   */
  public void setChoice(int choice) {
    this.choice = choice;
  }

  public void setObserver(Consumer<Object> observer) { // Setter pour le Consumer
    this.observer = observer;
  }

  /**
   * display the popup and get the JoptionPane choice.
   */
  public void pop() {
    EventQueue.invokeLater(() -> {
      display();
      if (jp == JOptionPane.OK_OPTION) {
        choice = 1;
      } else if (jp == JOptionPane.CANCEL_OPTION) {
        choice = 0;
      }
      observer.accept(choice);
    });
  }
}
