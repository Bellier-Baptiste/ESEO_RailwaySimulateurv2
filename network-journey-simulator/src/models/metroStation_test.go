/*
File : metroStation_test.go

Brief : metroStation_test.go runs tests on the metroStation.go file.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX (quality check)

License : MIT License

Copyright (c) 2023 Équipe PFE_2023_16

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package models

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

/*
linesTest is a slice of MetroLine used for tests.

Attributes :
  - name string : the name of the line
  - trainNumber int : the number of trains of the line
  - passengersMaxPerTrain int : the number of passengers max per train
*/
var linesTest = []MetroLine{
	{name: "A", trainNumber: 5, passengersMaxPerTrain: 120},
	{name: "B", trainNumber: 5, passengersMaxPerTrain: 120},
	{name: "C", trainNumber: 5, passengersMaxPerTrain: 120},
}

/*
clearLinesTest clears the linesTest slice.
*/
func clearLinesTest() {
	linesTest = []MetroLine{
		{name: "A", trainNumber: 5, passengersMaxPerTrain: 120},
		{name: "B", trainNumber: 5, passengersMaxPerTrain: 120},
		{name: "C", trainNumber: 5, passengersMaxPerTrain: 120},
	}
}

/*
initStation initializes a MetroStation for tests. It adds the linesTest to the
station.

Return :
  - station MetroStation : the initialized station
*/
func initStation() MetroStation {
	var station = MetroStation{
		id:       0,
		name:     "metroStationTest",
		position: Point{x: 0, y: 0},
	}

	for i := 0; i < len(linesTest); i++ {
		station.AddMetroLine(&linesTest[i])
	}

	return station
}

/*
TestMetroStation_hasDirectLineTo tests the hasDirectLineTo method of the
MetroStation struct.

# It tests if the method works properly

Input : t *testing.T

Expected : The method works properly
*/
func TestMetroStation_hasDirectLineTo(t *testing.T) {
	var station1 = initStation()
	var station2 = initStation()

	station2.removeMetroLine(&linesTest[1])
	station2.removeMetroLine(&linesTest[2])

	assert.True(t, station1.hasDirectLineTo(station2))
	station2.removeMetroLine(&linesTest[0])

	assert.False(t, station1.hasDirectLineTo(station2))
}
