package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActionRunSimulation   {
  public static final String ACTION_NAME = "RUN_SIMULATION";
  private static ActionRunSimulation instance;

  private ActionRunSimulation( ) {
  }

  /**
   * Create Singleton
   *
   * @return ActionExport instance
   */
  public static ActionRunSimulation getInstance() {
    if (instance == null) {
      instance = new ActionRunSimulation();
    }
    return instance;
  }

  public void runSimulation() {
    try {
      // Check if simulator.exe is already running
      if (isSimulatorRunning()) {
        return;
      }

      String rootJavaProjectPath = System.getProperty("user.dir");
      String rootGoProjectPath = rootJavaProjectPath.replace("railway-editor_java_version", "pfe-2018-network-journey-simulator");
      File runThisSimulation = new File(rootGoProjectPath + "\\src\\configs\\runThisSimulation.xml");
      ActionFile.getInstance().export(runThisSimulation);

      // create a new list of arguments for our process
      String[] commands = {"cmd", "/C", "start metro_simulator.exe -configname runThisSimulation.xml"};
      // create the process builder
      ProcessBuilder pb = new ProcessBuilder(commands);
      // set the working directory of the process
      pb.directory(new File(rootGoProjectPath));
      Process process = pb.start();
      // wait that the process finish
      process.waitFor();
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }

  // Check if the process is running
  private boolean isSimulatorRunning() {
   try {
     String findProcess = "metro_simulator.exe";
     String filenameFilter = "/nh /fi \"Imagename eq " + findProcess + "\"";
     String tasksCmd = System.getenv("windir") + "/system32/tasklist.exe " + filenameFilter;

     Process p = Runtime.getRuntime().exec(tasksCmd);
     BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

     ArrayList<String> procs = new ArrayList<>();
     String line = null;
     while ((line = input.readLine()) != null)
       procs.add(line);

     input.close();

     return procs.stream().filter(row -> row.indexOf(findProcess) > -1).count() > 0;
   } catch (IOException ex) {
     ex.printStackTrace();
     return false;
   }
  }
}
