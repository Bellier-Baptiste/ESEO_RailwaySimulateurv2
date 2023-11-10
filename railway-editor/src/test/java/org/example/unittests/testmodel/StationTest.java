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

package org.example.unittests.testmodel;

import org.example.model.Station;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test-case of {@link Station} model.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file StationTest.java
 * @date N/A
 * @since 2.0
 */
public class StationTest {
    public static final String NAME = "eglantine";


    @Test
    public void testConstructeur() {
        Station station = new Station(0, 5, 5, NAME);
        assertEquals(0, station.getId());
        assertEquals(5, station.getPosX());
        assertEquals(5, station.getPosY());
        assertEquals("eglantine", station.getName());
    }

    @Test
    public void testMoveStation() {
        Station station = new Station(0, 5, 5, NAME);
        station.moveStation(3, 3);
        assertEquals(8, station.getPosX());
        assertEquals(8, station.getPosY());
    }
}
