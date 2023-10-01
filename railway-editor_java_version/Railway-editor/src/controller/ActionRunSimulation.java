package controller;

import view.MainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ActionRunSimulation extends AbstractAction {
  public static final String ACTION_NAME = "RUN_SIMULATION";
  private MainPanel mainPanel;
  private ActionManager actionManager;

  public ActionRunSimulation(MainPanel mainPanel, ActionManager actionManager) {
    this.mainPanel = mainPanel;
    this.actionManager = actionManager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      System.out.println(System.getProperty("user.dir"));
      String rootJavaProjectPath = System.getProperty("user.dir");
      String rootGoProjectPath = rootJavaProjectPath.replace("railway-editor_java_version", "pfe-2018-network-journey-simulator");
      File runThisSimulation = new File(rootGoProjectPath + "\\src\\configs\\runThisSimulation.xml");
      ActionExport actionExport = new ActionExport(this.mainPanel, this.actionManager);
      actionExport.export(runThisSimulation);

      // create a new list of arguments for our process
      String[] commands = {"cmd", "/C", "start metro_simulator.exe -configname runThisSimulation.xml"};
      // create the process builder
      ProcessBuilder pb = new ProcessBuilder(commands);
      // set the working directory of the process
      pb.directory(new File(rootGoProjectPath));
      Process process = pb.start();
      // wait that the process finish
      int exitCode = process.waitFor();
      // verify the exit code
      if (exitCode == 0) {
        System.out.println("The Go file has been executed successfully !");
      } else {
        System.err.println("Error while executing the Go file. Exit code : " + exitCode);
      }
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
