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

package unittests.testcontroller;

import controller.ActionFile;
import controller.ActionRunSimulation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

/**
 * Test-case of running the go simulator from the Java HMI.
 *
 * @file ActionsTest.java
 * @date 2023-10-18
 * @since 3.0
 */
class ActionRunSimulationTest {

  @Test
  void testRunSimulation() throws Exception {
    ActionRunSimulation actionRunSimulation =
        Mockito.spy(ActionRunSimulation.getInstance());


    ActionFile actionFile = Mockito.spy(ActionFile.getInstance());
    java.lang.reflect.Field actionFileField = actionRunSimulation.getClass()
        .getDeclaredField("actionFile");
    actionFileField.setAccessible(true);
     Mockito.spy((ActionFile) actionFileField.get(actionRunSimulation));
    actionFileField.set(actionRunSimulation, actionFile);

    Mockito.when(actionRunSimulation.isSimulatorRunning()).thenReturn(false);
    Mockito.doNothing().when(actionFile).export(Mockito.any(File.class));

    int exitValue = actionRunSimulation.runSimulation();
    Assertions.assertEquals(0, exitValue, "The simulator" +
        " should have been launched");

    Mockito.verify(actionFile).export(Mockito.any(File.class));
    Mockito.verifyNoMoreInteractions(actionFile);
    Mockito.verify(actionRunSimulation).isSimulatorRunning();
    Mockito.verify(actionRunSimulation).runSimulation();
    Mockito.verifyNoMoreInteractions((actionRunSimulation));
  }
}
