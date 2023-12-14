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

package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.view.EditConfigDialog;
import org.example.view.EditConfigParamPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Test-case of editing the configuration of the simulation from the Java HMI.
 *
 * @author Aurélie Chamouleau
 * @author Baptiste BELLIER
 * @file ActionConfigurationTest.java
 * @date 2023-10-23
 * @since 3.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActionConfigurationTest {

  /**
   * Test the readJsonFile method with the configTest.json file.
   *
   * @throws NoSuchFieldException   if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  @Test
  @Order(1)
  void testReadJsonFile() throws NoSuchFieldException,
      IllegalAccessException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Use introspection to get the JSON_FILE_PATH field
    Field jsonFilePathField = getJsonFilePathField(actionConfiguration);
    jsonFilePathField.set(actionConfiguration, "src/test/java/org/example/controller/"
        + "configTest.json");

    // Running tested method
    actionConfiguration.readJsonFile();

    /* Assertions */
    Assertions.assertEquals(5, actionConfiguration.getJsonMap().size());
    String key1 = "parameter 1";
    String key2 = "parameter 2";
    String key3 = "parameter 3";
    String key4 = "parameter 4";
    String key5 = "parameter 5";
    // Assert that the keys names are correct
    Assertions.assertEquals(key1, actionConfiguration.getJsonMap().keySet()
        .toArray()[0]);
    Assertions.assertEquals(key2, actionConfiguration.getJsonMap().keySet()
        .toArray()[1]);
    Assertions.assertEquals(key3, actionConfiguration.getJsonMap().keySet()
        .toArray()[2]);
    Assertions.assertEquals(key4, actionConfiguration.getJsonMap().keySet()
        .toArray()[3]);
    Assertions.assertEquals(key5, actionConfiguration.getJsonMap().keySet()
        .toArray()[4]);
    // Assert that the values are correct
    Assertions.assertEquals("1", actionConfiguration.getJsonMap()
        .get(key1));
    Assertions.assertEquals("2.0", actionConfiguration.getJsonMap()
        .get(key2));
    Assertions.assertEquals("string", actionConfiguration.getJsonMap()
        .get(key3));
    Assertions.assertEquals("true", actionConfiguration.getJsonMap()
        .get(key4));
    Assertions.assertEquals("false", actionConfiguration.getJsonMap()
        .get(key5));

    // Verify that the methods have been called
    Mockito.verify(actionConfiguration).readJsonFile();
    Mockito.verify(actionConfiguration, Mockito.times(11))
        .getJsonMap();
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }

  /**
   * Test the readJsonFile method when an IOException is thrown.
   *
   * @throws NoSuchFieldException   if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  @Test
  @Order(2)
  void testReadJsonFileIoException() throws NoSuchFieldException,
      IllegalAccessException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Use introspection to get the JSON_FILE_PATH field
    Field jsonFilePathField = getJsonFilePathField(actionConfiguration);
    // Set the JSON_FILE_PATH field to a wrong path to create an IOException
    jsonFilePathField.set(actionConfiguration, "some/wrong/config/path");

    // Use introspection to get jsonMap field
    Field jsonMapField = actionConfiguration.getClass()
        .getDeclaredField("jsonMap");
    jsonMapField.setAccessible(true);
    jsonMapField.set(actionConfiguration, new LinkedHashMap<>());

    // Running tested method
    actionConfiguration.readJsonFile();
    // Check that the thread has been interrupted
    Assertions.assertTrue(Thread.currentThread().isInterrupted());

    // Verify that the methods have been called
    Mockito.verify(actionConfiguration).readJsonFile();
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }

  /**
   * Test the saveJsonFile method with the configTest.json file.
   *
   * <p>Make sure to go check the configTest.json file after the test
   * to see if the changes have been made.
   * This test just checks that the method works and that the jsonMap
   * used to write the file is correct.
   * Testing the file writing is not possible else it would not be a unit
   * test because it would need to be read.</p>
   *
   * @throws NoSuchFieldException   if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  @Test
  @Order(3)
  void testSaveJsonFile() throws NoSuchFieldException, IllegalAccessException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Use introspection to get the JSON_FILE_PATH field
    Field jsonFilePathField = getJsonFilePathField(actionConfiguration);
    jsonFilePathField.set(actionConfiguration, "test/unittests"
        + "/testcontroller/configTest.json");

    // Use introspection to get jsonMap field
    Field jsonMapField = actionConfiguration.getClass()
        .getDeclaredField("jsonMap");
    jsonMapField.setAccessible(true);
    jsonMapField.set(actionConfiguration, new LinkedHashMap<>());

    EditConfigParamPanel editConfigParamPanel1 = Mockito.mock(
        EditConfigParamPanel.class);
    EditConfigParamPanel editConfigParamPanel2 = Mockito.mock(
        EditConfigParamPanel.class);
    EditConfigParamPanel editConfigParamPanel3 = Mockito.mock(
        EditConfigParamPanel.class);
    EditConfigParamPanel editConfigParamPanel4 = Mockito.mock(
        EditConfigParamPanel.class);
    EditConfigParamPanel editConfigParamPanel5 = Mockito.mock(
        EditConfigParamPanel.class);

    // Mocking methods
    Mockito.when(editConfigParamPanel1.getParamName()).thenReturn(
        "parameter 1");
    Mockito.when(editConfigParamPanel1.getParamValue()).thenReturn("string");
    Mockito.when(editConfigParamPanel2.getParamName()).thenReturn(
        "parameter 2");
    Mockito.when(editConfigParamPanel2.getParamValue()).thenReturn("3.14");
    Mockito.when(editConfigParamPanel3.getParamName()).thenReturn(
        "parameter 3");
    Mockito.when(editConfigParamPanel3.getParamValue()).thenReturn("8");
    Mockito.when(editConfigParamPanel4.getParamName()).thenReturn(
        "parameter 4");
    Mockito.when(editConfigParamPanel4.getParamValue()).thenReturn("false");
    Mockito.when(editConfigParamPanel5.getParamName()).thenReturn(
        "parameter 5");
    Mockito.when(editConfigParamPanel5.getParamValue()).thenReturn("true");

    // Add the panels to the list
    List<EditConfigParamPanel> editConfigParamPanelList = new ArrayList<>();
    editConfigParamPanelList.add(editConfigParamPanel1);
    editConfigParamPanelList.add(editConfigParamPanel2);
    editConfigParamPanelList.add(editConfigParamPanel3);
    editConfigParamPanelList.add(editConfigParamPanel4);
    editConfigParamPanelList.add(editConfigParamPanel5);

    Mockito.when(editConfigDialog.getEditConfigParamPanelList())
        .thenReturn(editConfigParamPanelList);

    // Running tested method
    actionConfiguration.saveJsonFile();

    // Assertions
    Assertions.assertEquals(5, actionConfiguration.getJsonMap().size());
    Assertions.assertEquals("string", actionConfiguration.getJsonMap()
        .get("parameter 1"));
    Assertions.assertEquals(3.14, actionConfiguration.getJsonMap()
        .get("parameter 2"));
    Assertions.assertEquals(8, actionConfiguration.getJsonMap()
        .get("parameter 3"));
    Assertions.assertEquals(false, actionConfiguration.getJsonMap()
        .get("parameter 4"));
    Assertions.assertEquals(true, actionConfiguration.getJsonMap()
        .get("parameter 5"));


    /* Verify that the methods have been called */
    // Verification for each mocked panel
    for (EditConfigParamPanel editConfigParamPanel : editConfigParamPanelList) {
      Mockito.verify(editConfigParamPanel).getParamName();
      Mockito.verify(editConfigParamPanel).getParamValue();
      Mockito.verifyNoMoreInteractions(editConfigParamPanel);
    }
    // Verification for the editConfigDialog
    Mockito.verify(editConfigDialog).getEditConfigParamPanelList();
    Mockito.verifyNoMoreInteractions(editConfigDialog);
    // Verification for the actionConfiguration
    Mockito.verify(actionConfiguration, Mockito.times(6))
        .getJsonMap();
    Mockito.verify(actionConfiguration).saveJsonFile();
  }


  /**
   * Test the saveJsonFile method when an IOException is thrown.
   *
   * @throws NoSuchFieldException   if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  @Test
  @Order(4)
  void testSaveJsonFileIoException() throws NoSuchFieldException,
      IllegalAccessException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Use introspection to get the JSON_FILE_PATH field
    Field jsonFilePathField = getJsonFilePathField(actionConfiguration);
    // Set the JSON_FILE_PATH field to a wrong path to create an IOException
    jsonFilePathField.set(actionConfiguration, "some/wrong/config/path");

    // Use introspection to get jsonMap field
    Field jsonMapField = actionConfiguration.getClass()
        .getDeclaredField("jsonMap");
    jsonMapField.setAccessible(true);
    jsonMapField.set(actionConfiguration, new LinkedHashMap<>());

    // Running tested method
    actionConfiguration.saveJsonFile();
    // Check that the thread has been interrupted
    Assertions.assertTrue(Thread.currentThread().isInterrupted());

    // Verify that the methods have been called
    Mockito.verify(actionConfiguration).saveJsonFile();
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }

  /**
   * Test the copyFile method.
   * @throws IOException if the file cannot be created
   */
  @Test
  @Order(5)
  void testCopyFile() throws IOException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
        Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Create a temporary file to copy
    Path source = null;
    Path target = null;
    try {
      source = Files.createTempFile("source", ".tmp");
      target = Files.createTempFile("target", ".tmp");
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }

    // Running tested method
    assert target != null;
    Files.deleteIfExists(target);
    actionConfiguration.copyFile(source.toString(), target.toString());

    // Assertions
    Assertions.assertTrue(Files.exists(target), "Target file should " +
      "exist after copy");
    Assertions.assertTrue(Files.exists(source), "Source file should " +
      "still exist after copy");

    // Verify that copyFile was called
    Mockito.verify(actionConfiguration).copyFile(source.toString(),
      target.toString());
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }

  /**
   * Test the copyFile method when the destination file already exists.
   * @throws IOException if the file cannot be created
   */
  @Test
  @Order(6)
  void testCopyFileDestFileAlreadyExists() throws IOException {
    // Mocking and spying instances
    EditConfigDialog editConfigDialog = Mockito.mock(EditConfigDialog.class);
    ActionConfiguration actionConfiguration =
      Mockito.spy(new ActionConfiguration(editConfigDialog));

    // Create a temporary file to copy
    Path source = null;
    Path target = null;
    try {
      source = Files.createTempFile("source", ".tmp");
      target = Files.createTempFile("target", ".tmp");
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }

    // Make sure the target file exists before calling copyFile
    assert target != null;
    Files.write(target, Collections.singletonList("Test content"));

    // Running tested method
    actionConfiguration.copyFile(source.toString(), target.toString());

    // Assertions
    Assertions.assertTrue(Files.exists(target), "Target file should " +
      "still exist after copy");
    Assertions.assertTrue(Files.exists(source), "Source file should " +
      "still exist after copy");

    // Verify that deleteFile and copyFile were called
    Mockito.verify(actionConfiguration).deleteFile(target.toString());
    Mockito.verify(actionConfiguration).copyFile(source.toString(),
      target.toString());
    Mockito.verifyNoMoreInteractions(actionConfiguration);
  }



  /**
   * Update the configTest.json file because it's modified during
   * the testSaveJsonFile test so the testReadJsonFile method will fail
   * at the next execution if the file is not updated.
   */
  @AfterEach
  public void updateConfigTestFile() {
    try {
      // Map creation to represent the JSON
      Map<String, Object> jsonMap = new LinkedHashMap<>();
      jsonMap.put("parameter 1", 1);
      jsonMap.put("parameter 2", 2.0);
      jsonMap.put("parameter 3", "string");
      jsonMap.put("parameter 4", true);
      jsonMap.put("parameter 5", false);

      // Path of the configTest.json file
      String filePath = "test/unittests/testcontroller/configTest.json";

      // ObjectMapper configuration
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

      // Write in the JSON file
      objectMapper.writeValue(new File(filePath), jsonMap);
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Get the JSON_FILE_PATH field of the ActionConfiguration class.
   *
   * @param actionConfiguration the ActionConfiguration instance
   * @return the JSON_FILE_PATH field
   * @throws NoSuchFieldException   if the field does not exist
   * @throws IllegalAccessException if the field is not accessible
   */
  private static Field getJsonFilePathField(
      final ActionConfiguration actionConfiguration)
      throws NoSuchFieldException, IllegalAccessException {
    Field jsonFilePathField = actionConfiguration.getClass()
        .getDeclaredField("JSON_FILE_PATH");

    // Remove the final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(jsonFilePathField, jsonFilePathField.getModifiers()
        & ~Modifier.FINAL);

    // Set the JSON_FILE_PATH field to the test file
    jsonFilePathField.setAccessible(true);
    return jsonFilePathField;
  }
}
