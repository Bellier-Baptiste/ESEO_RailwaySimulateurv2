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
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.unittests.testcontroller;

import org.example.controller.ActionFile;
import org.example.model.Area;
import org.example.view.AreaView;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test-cases of exporting and importing metro networks map files (xml).
 *
 * @author Aurélie Chamouleau
 * @file ActionFile.java
 * @date 2023-11-16
 * @since 3.0
 */
class ActionFileTest {

  private static Stream<Arguments> areaViewStream()
      throws ParserConfigurationException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document1 = documentBuilder.newDocument();
    Element station = document1.createElement("station");
    document1.appendChild(station);

    Document document2 = documentBuilder.newDocument();
    Element area = document2.createElement("area");
    document2.appendChild(area);

    Area areaParis = new Area(48.92430395329745, 2.23846435546875,
        48.78877122776646, 2.4444580078125);
    AreaView areaView = new AreaView(areaParis);
    return Stream.of(
        Arguments.of(document1, null, station),
        Arguments.of(document2, areaView, area)
    );
  }

  @ParameterizedTest
  @MethodSource("areaViewStream")
  void testExportDistributions(final Document document, final AreaView areaView,
                               final Element element)
      throws NoSuchMethodException, InvocationTargetException,
      IllegalAccessException {

    Method method = ActionFile.class.getDeclaredMethod(
        "exportDistributions", Document.class, AreaView.class, Element.class);
    method.setAccessible(true);

    method.invoke(ActionFile.getInstance(), document, areaView, element);

    // Population distribution check
    NamedNodeMap populationAttributes = element.getFirstChild().getAttributes();
    for (int i = 0; i < populationAttributes.getLength(); i++) {
      Node populationAttribute = populationAttributes.item(i);
      String attrName = populationAttribute.getNodeName();
      String dataFieldName = Character.toUpperCase(attrName.charAt(0))
          + attrName.substring(1);
      int expectedValue = Area.getDefaultPopulationDistribution(dataFieldName);
      assertEquals(expectedValue,
          Integer.parseInt(populationAttribute.getNodeValue()), "The "
              + dataFieldName + "population distribution should be "
              + expectedValue);
    }

    NamedNodeMap destinationAttributes =
        element.getFirstChild().getAttributes();
    // Destination distribution check
    for (int i = 0; i < destinationAttributes.getLength(); i++) {
      Node destinationAttribute = destinationAttributes.item(i);
      String attrName = destinationAttribute.getNodeName();
      String dataFieldName = Character.toUpperCase(attrName.charAt(0))
          + attrName.substring(1);
      int expectedValue = Area.getDefaultPopulationDistribution(dataFieldName);
      assertEquals(expectedValue,
          Integer.parseInt(destinationAttribute.getNodeValue()),
          "The " + dataFieldName
              + "population distribution should be " + expectedValue);
    }
  }
}
