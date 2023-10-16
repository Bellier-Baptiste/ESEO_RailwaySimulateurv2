/** Class part of the view package of the application. */

package view;

import controller.KeyboardTool;
import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which creates a custom comboBox with integrated research function.
 *
 * @author Arthur Lagarce, Aurélie Chamouleau
 */
@SuppressWarnings("rawtypes")
public class FilterComboBox extends JComboBox {
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  /** Zoom to set on the destination. */
  private static final int ZOOM = 13;
  /** Array of character entered by the user. */
  private List<String> array;

  /**
   * Constructor.
   *
   * @param filterComboBoArray array of character entered by the user
   */
  @SuppressWarnings("unchecked")
  public FilterComboBox(final List<String> filterComboBoArray) {
    super(filterComboBoArray.toArray());
    this.array = filterComboBoArray;
    this.setEditable(true);
    final JTextField textField = (JTextField) this.getEditor()
        .getEditorComponent();
    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(final KeyEvent ke) {
        if (ke.getKeyCode() != KeyEvent.VK_ENTER) {
          SwingUtilities.invokeLater(() -> comboFilter(textField.getText()));
        } else {
          if (!Data.getInstance().getCurrentCity().isEmpty()) {
            MainWindow.getInstance().getMainPanel().requestFocusInWindow();
            MainWindow.getInstance().getMainPanel().addKeyListener(
                new KeyboardTool(MainWindow.getInstance().getMainPanel()));
            double[] coords = findCoordinates(Data.getInstance()
                .getCurrentCity());
            MainWindow.getInstance().getMainPanel().getLineViews().clear();
            MainWindow.getInstance().getMainPanel().getAreaViews().clear();
            Coordinate point = new Coordinate(coords[1], coords[0]);
            MainWindow.getInstance().getMainPanel().setDisplayPosition(point,
                ZOOM);

          }
        }
      }
    });

  }

  /**
   * Filter of the combo box from the enteredText.
   *
   * @param enteredText text entered
   */
  @SuppressWarnings("unchecked")
  public void comboFilter(final String enteredText) {
    List<String> filterArray = new ArrayList<>();
    for (String s : array) {
      if (s.toLowerCase().contains(enteredText.toLowerCase())) {
        filterArray.add(s);
      }
    }
    if (!filterArray.isEmpty()) {
      this.setModel(new DefaultComboBoxModel(filterArray.toArray()));
      this.setSelectedItem(enteredText);
      this.showPopup();
    } else {
      this.hidePopup();
    }
  }

  /**
   * fill the combo box options.
   *
   * @return all the tows starting with the enteredText
   */
  public static List<String> populateArray() {
    List<String> test = new ArrayList<>();
    test.add("");

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    InputStream file = classLoader.getResourceAsStream("resources/cities.txt");
    try {
      assert file != null;
      try (BufferedReader br = new BufferedReader(new InputStreamReader(
          file))) {
        for (String line; (line = br.readLine()) != null;) {
          test.add(line.split(";")[0]);
        }
        // line is not visible here.
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return test;
  }

  /**
   * find the city coordinates after combo box validation.
   *
   * @param name city name
   *
   * @return double[] coordinates
   */
  private double[] findCoordinates(final String name) {

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    InputStream file = classLoader.getResourceAsStream(
        ("resources/cities.txt"));
    double[] coords = new double[2];

    try {
      assert file != null;
      try (BufferedReader br = new BufferedReader(new InputStreamReader(
          file))) {
        String line = br.readLine();
        while (line != null && !line.contains(name)) {
          line = br.readLine();
        }
        assert line != null;
        coords[0] = Double.parseDouble(line.split(";")[1]);
        coords[1] = Double.parseDouble(line.split(";")[2]);

        // line is not visible here.
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return coords;
  }
}
