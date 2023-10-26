package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.KeyboardTool;
import data.Data;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 * Class whiche creates a custom comboBox with integrated research function.
 * @author arthu
 *
 */
@SuppressWarnings("rawtypes")
public class FilterComboBox extends JComboBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> array;

	/**Constructor.
	 * @param array array of character entered by the user
	 */
	@SuppressWarnings("unchecked")
	public FilterComboBox(List<String> array) {
		super(array.toArray());
		this.array = array;
		this.setEditable(true);
		final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
		textfield.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if ((int) ke.getKeyCode() != KeyEvent.VK_ENTER) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							comboFilter(textfield.getText());
						}
					});
				}else {
					if (!Data.getInstance().getCurrentCity().equals("")) {
			    		MainWindow.getInstance().getMainPanel().requestFocusInWindow();
			    		MainWindow.getInstance().getMainPanel().addKeyListener(new KeyboardTool(MainWindow.getInstance().getMainPanel()));
						double[] coords = findCoordinates(Data.getInstance().getCurrentCity());
						MainWindow.getInstance().getMainPanel().getLineViews().clear();
						MainWindow.getInstance().getMainPanel().getAreaViews().clear();
            Coordinate point = new Coordinate(coords[1], coords[0]);
            MainWindow.getInstance().getMainPanel().setDisplayPosition(point, 13);
					}
				}
			}
		});

	}

	/**
	 * Filter of the combo box from the enteredText.
	 * @param enteredText text entered
	 */
	@SuppressWarnings("unchecked")
	public void comboFilter(String enteredText) {
		List<String> filterArray = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).toLowerCase().contains(enteredText.toLowerCase())) {
				filterArray.add(array.get(i));
			}
		}
		if (filterArray.size() > 0) {
			this.setModel(new DefaultComboBoxModel(filterArray.toArray()));
			this.setSelectedItem(enteredText);
			this.showPopup();
		} else {
			this.hidePopup();
		}
	}

	/**
	 * fill the combo box options.
	 * @return all the tows starting with the enteredText
	 */
	public static List<String> populateArray() {
		List<String> test = new ArrayList<String>();
		test.add("");

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream file = classLoader.getResourceAsStream("resources/cities.txt");
		//File file = new File("src/resources/cities.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
			for (String line; (line = br.readLine()) != null;) {
				test.add(line.split(";")[0]);
			}
			// line is not visible here.
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return test;
	}
	
	/**
	 * find the city coordinates after combo box validation.
	 * @param name
	 * @return
	 */
	private double[] findCoordinates(String name) {
		
		 ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	        InputStream file = classLoader.getResourceAsStream(("resources/cities.txt"));
      	double [] coords = new double[2];

	        try(BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
	            String line = br.readLine();
	        	while(line!= null && !line.contains(name)) {
	        		line = br.readLine();
	        	}
	        	coords[0] = Double.valueOf(line.split(";")[1]);
	        	coords[1] = Double.valueOf(line.split(";")[2]);

	            // line is not visible here.
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  		return coords;
	}
}
