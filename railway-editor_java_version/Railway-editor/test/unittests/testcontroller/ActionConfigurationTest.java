/*
 * License : MIT License
 *
 * Copyright (c) 2023 Équipe PFE_2023_16
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

import controller.ActionConfiguration;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import view.EditConfigDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Test-case of editing the configuration of the simulation from the Java HMI.
 *
 * @author Aurélie Chamouleau
 * @file ActionConfigurationTest.java
 * @date 2023-10-23
 * @since 3.0
 */
public class ActionConfigurationTest {

  /**
   * Test the readJsonFile method with the configTest.json file.
   *
   * @throws NoSuchFieldException if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  @Test
  public void testReadJsonFile() throws NoSuchFieldException, IllegalAccessException {
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    Field jsonFilePathField = actionConfiguration.getClass()
        .getDeclaredField("JSON_FILE_PATH");

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(jsonFilePathField, jsonFilePathField.getModifiers()
        & ~Modifier.FINAL);

    jsonFilePathField.setAccessible(true);
    jsonFilePathField.set(actionConfiguration, "test/unittests" +
        "/testcontroller/configTest.json");

    System.out.println(jsonFilePathField.get(actionConfiguration));
    actionConfiguration.readJsonFile();
    Assertions.assertEquals(4, actionConfiguration.getJsonMap().size());


    String key1 =  "parameter 1";
    String key2 =  "parameter 2";
    String key3 =  "parameter 3";
    String key4 =  "parameter 4";
    // Assert that the keys names are correct
    Assertions.assertEquals(key1, actionConfiguration.getJsonMap().keySet()
        .toArray()[0]);
    Assertions.assertEquals(key2, actionConfiguration.getJsonMap().keySet()
        .toArray()[1]);
    Assertions.assertEquals(key3, actionConfiguration.getJsonMap().keySet()
        .toArray()[2]);
    Assertions.assertEquals(key4, actionConfiguration.getJsonMap().keySet()
        .toArray()[3]);
    // Assert that the values are correct
    Assertions.assertEquals("1", actionConfiguration.getJsonMap()
        .get(key1));
    Assertions.assertEquals("2.0", actionConfiguration.getJsonMap()
        .get(key2));
    Assertions.assertEquals("3", actionConfiguration.getJsonMap()
        .get(key3));
    Assertions.assertEquals("true", actionConfiguration.getJsonMap()
        .get(key4));

    // Verify that the methods have been called
    Mockito.verify(actionConfiguration).readJsonFile();
    Mockito.verify(actionConfiguration, Mockito.times(9))
        .getJsonMap();
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }
}
