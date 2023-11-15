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

package models

type PopulationDistribution struct {
	businessman int
	child       int
	retired     int
	student     int
	tourist     int
	unemployed  int
	worker      int
}

func NewPopulationDistribution(businessman, child, retired, student, tourist,
	unemployed, worker int) PopulationDistribution {
	return PopulationDistribution{
		businessman: businessman,
		child:       child,
		retired:     retired,
		student:     student,
		tourist:     tourist,
		unemployed:  unemployed,
		worker:      worker,
	}
}

func (ad *PopulationDistribution) Businessman() int {
	return ad.businessman
}

func (ad *PopulationDistribution) Child() int {
	return ad.child
}

func (ad *PopulationDistribution) Retired() int {
	return ad.retired
}

func (ad *PopulationDistribution) Student() int {
	return ad.student
}

func (ad *PopulationDistribution) Tourist() int {
	return ad.tourist
}

func (ad *PopulationDistribution) Unemployed() int {
	return ad.unemployed
}

func (ad *PopulationDistribution) Worker() int {
	return ad.worker
}
