package view;

import controller.ActionFile;
import controller.ActionThemeMode;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
  private static JMenuBar instance;

  private MenuBar(MainWindow mainWindow, MainPanel mainPanel) {
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
    JMenuItem export = new JMenuItem("Export");
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
    JMenuItem exportConfig = new JMenuItem("Export");
    exportConfig.setName("Export");
    config.add(exportConfig);

    // Theme menu
    JButton changeThemeBtn = new JButton("Light Mode");
    changeThemeBtn.setName(ActionThemeMode.ACTION_NAME);
    changeThemeBtn.addActionListener(e -> ActionThemeMode.getInstance(changeThemeBtn).changeTheme());
    this.add(changeThemeBtn);
    this.add(config);
    this.add(file);
  }

  public static JMenuBar getInstance(MainWindow mainWindow, MainPanel mainPanel) {
    if (instance == null) {
      instance = new MenuBar(mainWindow, mainPanel);
    }
    return instance;
  }
}
