/**
 *  Class part of the view package of the application.
 */

package view;

import controller.ActionFile;
import controller.ActionThemeMode;

import java.awt.ComponentOrientation;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * Menu bar of the application.
 */
public final class MenuBar extends JMenuBar {
  /**
   * Singleton instance.
   */
  private static JMenuBar instance;
  /**
   * Text to display on the export menu item.
   */
  private static final String EXPORT_TEXT = "Export";

  /**
   * Private constructor.
   */
  private MenuBar() {
    // Menu bar properties
    this.add(Box.createHorizontalGlue());
    this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    // File menu
    JMenu file = new JMenu("File");
    JMenuItem open = new JMenuItem("Open");
    open.setName(ActionFile.IMPORT_NAME);
    open.addActionListener(e -> ActionFile.getInstance().showOpenDialog());
    file.add(open);
    JMenuItem save = new JMenuItem("Save");
    save.setName("Save");
    file.add(save);
    JMenuItem export = new JMenuItem(EXPORT_TEXT);
    export.addActionListener(e -> ActionFile.getInstance().showExportDialog());
    export.setName(ActionFile.EXPORT_NAME);
    file.add(export);
    this.add(file);

    // Configuration menu
    JMenu config = new JMenu("Configuration");
    JMenuItem newConfig = new JMenuItem("New");
    newConfig.setName("New");
    config.add(newConfig);
    JMenuItem openConfig = new JMenuItem("Open");
    openConfig.setName("Open");
    config.add(openConfig);
    JMenuItem exportConfig = new JMenuItem(EXPORT_TEXT);
    exportConfig.setName(EXPORT_TEXT);
    config.add(exportConfig);

    // Theme menu
    JButton changeThemeBtn = new JButton("Light Mode");
    changeThemeBtn.setName(ActionThemeMode.LIGHT_MODE);
    changeThemeBtn.addActionListener(e -> ActionThemeMode.getInstance(
        changeThemeBtn).changeTheme());
    this.add(changeThemeBtn);
    this.add(config);
    this.add(file);
  }

  /**
   * Create Singleton.
   *
   * @return MenuBar instance
   */
  public static JMenuBar getInstance() {
    if (instance == null) {
      instance = new MenuBar();
    }
    return instance;
  }
}
