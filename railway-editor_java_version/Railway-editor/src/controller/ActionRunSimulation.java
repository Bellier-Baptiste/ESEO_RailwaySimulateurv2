/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class for running the simulation from the Java HMI.
 * Linked to buttons in {@link view.ToolBarPanel}.
 *
 * @author Aurélie Chamouleau
 * @file ActionRunSimulation.java
 * @date 2023-10-02
 * @since 3.0
 */
public final class ActionRunSimulation {
  /**
   * String of the run simulation action name.
   */
  public static final String ACTION_NAME = "RUN_SIMULATION";
  /**
   * Singleton instance of the class.
   */
  private static ActionRunSimulation instance;

  /** ActionFile instance. */
  private final ActionFile actionFile;

  /**
   * Constructor of the class.
   */
  private ActionRunSimulation() {
    this.actionFile = ActionFile.getInstance();
  }

  /**
   * Return Singleton.
   *
   * @return ActionExport instance
   */
  public static ActionRunSimulation getInstance() {
    if (instance == null) {
      instance = new ActionRunSimulation();
    }
    return instance;
  }

  /**
   * Run the simulation.
   *
   * @return the exit value of the process
   */
  public int runSimulation() {
    try {
      // Check if simulator.exe is already running
      if (this.isSimulatorRunning()) {
        return 0;
      }

      String rootJavaProjectPath = System.getProperty("user.dir");
      String rootGoProjectPath = rootJavaProjectPath.replace(
          "railway-editor_java_version", "pfe-2018-network-journey-simulator");

      // Useful when running the unit test
      rootGoProjectPath = rootGoProjectPath.replace(
          "\\Railway-editor", "");

      File runThisSimulation = new File(
          rootGoProjectPath + "\\src\\configs\\runThisSimulation.xml");
      this.actionFile.export(runThisSimulation);

      // create a new list of arguments for our process
      String[] commands = {"cmd", "/C",
          "start metro_simulator.exe -configname runThisSimulation.xml"};
      // create the process builder
      ProcessBuilder pb = new ProcessBuilder(commands);
      // set the working directory of the process
      pb.directory(new File(rootGoProjectPath));
      Process process = pb.start();
      // wait that the process finish
      process.waitFor();
      return process.exitValue();
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
      Thread.currentThread().interrupt();
    }
    return -1;
  }

  /**
   * Check if the process is running.
   *
   * @return true if the process is running, false otherwise
   */
  public boolean isSimulatorRunning() {
    try {
      String findProcess = "metro_simulator.exe";
      String filenameFilter = "/nh /fi \"Imagename eq " + findProcess + "\"";
      String tasksCmd = System.getenv("windir") + "/system32/tasklist.exe "
          + filenameFilter;

      Process p = Runtime.getRuntime().exec(tasksCmd);
      BufferedReader input = new BufferedReader(new InputStreamReader(
          p.getInputStream()));

      ArrayList<String> procs = new ArrayList<>();
      String line;
      while ((line = input.readLine()) != null) {
        procs.add(line);
      }
      input.close();

      return procs.stream().anyMatch(row -> row.contains(findProcess));
    } catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
