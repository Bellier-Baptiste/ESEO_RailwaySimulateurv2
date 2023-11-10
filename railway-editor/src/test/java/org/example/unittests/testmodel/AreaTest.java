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

import org.example.data.Data;
import org.example.model.Area;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test-case of {@link Area} model.
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file AreaTest.java
 * @date N/A
 * @since 2.0
 */
public class AreaTest {

	@Test
	public void testConstructor() {
		Area area = new Area(10,10,20,20);
		assertEquals(0, area.getId());
		assertEquals(10, area.getPosX());
		assertEquals(10, area.getPosY());
		assertEquals(20, area.getWidth());
		assertEquals(20, area.getHeight());
	}
	
	@Test
	public void testSetNewPart() {
		Area area = new Area(10,10,20,20);
		area.setNewPart(Data.AREA_TOURIST, 20);
		area.setNewPart(Data.AREA_STUDENT, 20);
		area.setNewPart(Data.AREA_BUSINESSMAN, 20);
		area.setNewPart(Data.AREA_CHILD, 20);
		area.setNewPart(Data.AREA_WORKER, 60);

		int touristAmount = area.getDistribution().get(Data.AREA_TOURIST);
		int studentAmount = area.getDistribution().get(Data.AREA_STUDENT);
		int businessmannAmount = area.getDistribution().get(Data.AREA_BUSINESSMAN);
		int childAmount = area.getDistribution().get(Data.AREA_CHILD);
		int workerAmount = area.getDistribution().get(Data.AREA_WORKER);
		int unemployedAmount = area.getDistribution().get(Data.AREA_UNEMPLOYED);

		assertEquals(20, touristAmount);
		assertEquals(20, studentAmount);
		assertEquals(20, businessmannAmount);
		assertEquals(20, childAmount);
		assertEquals(20, workerAmount);
		assertEquals(0, unemployedAmount);
	}
	
	@Test
	public void testExtend() {
		Area area = new Area(10,10,20,20);
		area.extendRightSide(10);
		assertEquals(30, area.getWidth());
		area.extendLeftSide(-10);
		assertEquals(0,area.getPosX());
		assertEquals(40, area.getWidth());
		area.extendBotSide(10);
		assertEquals(30, area.getHeight());
		area.extendTopSide(-10);
		assertEquals(0, area.getPosY());
		assertEquals(40, area.getHeight());
	}
}
