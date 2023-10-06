package view;

import controller.ActionExport;
import controller.ActionManager;
import controller.ActionOpen;
import controller.ActionThemeMode;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
  private static JMenuBar instance;

  private MenuBar(MainWindow mainWindow, MainPanel mainPanel, ActionManager actionManager) {
    // Menu bar properties
    this.add(Box.createHorizontalGlue());
    this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    // File menu
    JMenu file = new JMenu("File");
    JMenuItem open = new JMenuItem(new ActionOpen(mainPanel, actionManager));
    open.setName(ActionOpen.ACTION_NAME);
    file.add(open);
    JMenuItem save = new JMenuItem(new ActionExport(mainPanel, actionManager));
    save.setName("Save");
    save.setText("Save");
    file.add(save);
    JMenuItem export = new JMenuItem(new ActionExport(mainPanel, actionManager));
    export.setName(ActionExport.ACTION_NAME);
    file.add(export);
    this.add(file);

    // Configuration menu
    JMenu config = new JMenu("Configuration");
    JMenuItem newConfig = new JMenuItem(new ActionExport(mainPanel, actionManager));
    newConfig.setName("New");
    newConfig.setText("New");
    config.add(newConfig);
    JMenuItem openConfig = new JMenuItem(new ActionExport(mainPanel, actionManager));
    openConfig.setName("Open");
    openConfig.setText("Open");
    config.add(openConfig);
    JMenuItem exportConfig = new JMenuItem(new ActionExport(mainPanel, actionManager));
    exportConfig.setName("Export");
    exportConfig.setText("Export");
    config.add(exportConfig);

    // Theme menu
    JButton themeMode = new JButton();
    themeMode.setAction(new ActionThemeMode(mainWindow, themeMode));
    this.add(themeMode);
    this.add(config);
    this.add(file);
  }

  public static JMenuBar getInstance(MainWindow mainWindow, MainPanel mainPanel, ActionManager actionManager) {
    if (instance == null) {
      instance = new MenuBar(mainWindow, mainPanel, actionManager);
    }
    return instance;
  }
}
