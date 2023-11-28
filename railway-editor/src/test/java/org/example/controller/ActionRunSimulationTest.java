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

package org.example.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

/**
 * Test-case of running the go simulator from the Java HMI.
 *
 * @author Aurélie Chamouleau
 * @file ActionsTest.java
 * @date 2023-10-18
 * @since 3.0
 */
class ActionRunSimulationTest {

  /**
   * Test the runSimulation method when no other simulator is already running.
   *
   * @throws Exception if an error occurs
   */
  @Test
  void testRunSimulationNoSimulatorRunning() throws Exception {
    // Spy the ActionRunSimulation instance
    ActionRunSimulation actionRunSimulation =
        Mockito.spy(ActionRunSimulation.getInstance());

    // Spy the ActionFile instance
    ActionFile actionFile = Mockito.spy(ActionFile.getInstance());

    // Use introspection to synchronize the two instances
    java.lang.reflect.Field actionFileField = actionRunSimulation.getClass()
        .getDeclaredField("actionFile");
    actionFileField.setAccessible(true);
    actionFileField.set(actionRunSimulation, actionFile);

    // Mocking methods
    // No simulator already running so isSimulatorRunning should return false
    Mockito.when(actionRunSimulation.isSimulatorRunning()).thenReturn(false);
    Mockito.doNothing().when(actionFile).export(Mockito.any(File.class));

    // Run the simulation
    int exitValue = actionRunSimulation.runSimulation();

    // Check that the simulator has been launched
    Assertions.assertEquals(0, exitValue, "The simulator" +
        " should have been launched");

    // Check that the methods have been called
    Mockito.verify(actionFile).export(Mockito.any(File.class));
    Mockito.verifyNoMoreInteractions(actionFile);
    Mockito.verify(actionRunSimulation).isSimulatorRunning();
    Mockito.verify(actionRunSimulation).runSimulation();
    Mockito.verifyNoMoreInteractions((actionRunSimulation));
  }

  /**
   * Test the runSimulation method when no other simulator is already running.
   *
   * @throws Exception if an error occurs
   */
  @Test
  void testRunSimulationSomeSimulatorRunning() throws Exception {
    // Spy the ActionRunSimulation instance
    ActionRunSimulation actionRunSimulation =
        Mockito.spy(ActionRunSimulation.getInstance());

    // Spy the ActionFile instance
    ActionFile actionFile = Mockito.spy(ActionFile.getInstance());

    // Use introspection to synchronize the two instances
    java.lang.reflect.Field actionFileField = actionRunSimulation.getClass()
        .getDeclaredField("actionFile");
    actionFileField.setAccessible(true);
    actionFileField.set(actionRunSimulation, actionFile);

    // Mocking methods
    // A simulator is already running so isSimulatorRunning should return true
    Mockito.when(actionRunSimulation.isSimulatorRunning()).thenReturn(true);
    Mockito.doNothing().when(actionFile).export(Mockito.any(File.class));

    // Run the simulation
    int exitValue = actionRunSimulation.runSimulation();

    // Check that the simulator has been launched
    Assertions.assertEquals(-1, exitValue, "The simulator" +
        " should have been launched");

    // Check that the methods have been called
    Mockito.verifyNoInteractions(actionFile);
    Mockito.verify(actionRunSimulation).isSimulatorRunning();
    Mockito.verify(actionRunSimulation).runSimulation();
    Mockito.verifyNoMoreInteractions((actionRunSimulation));
  }
}
