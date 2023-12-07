/*
File : pathStation_test.go

Brief : pathStation_test.go runs tests on the pathStation.go file.

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
	"github.com/stretchr/testify/assert"
	"testing"
)

/*
generateStationsLinesRandomTest generates a random set of stations and lines
for testing purposes.

Return :
  - []*MetroStation : the generated stations
  - []*MetroLine : the generated lines
*/
func generateStationsLinesRandomTest() ([]*MetroStation, []*MetroLine) {
	var stations = []*MetroStation{
		{id: 1},
		{id: 2},
		{id: 3},
	}

	var lines = []*MetroLine{
		{id: 1},
		{id: 2},
	}
	return stations, lines
}

/*
TestPathStation_append tests the PathStation.append method.

# It tests if the method works properly

Input : t *testing.T

Expected : The method works properly
*/
func TestPathStation_append(t *testing.T) {
	var stations, lines = generateStationsLinesRandomTest()

	var path1 = PathStationCreate(lines[0], stations[0], stations[1])
	var path2 = PathStationCreate(lines[1], stations[1], stations[2])

	var path3 = path1.append(path2)

	assert.Equal(t, 3, len(path3.stations))
	assert.Equal(t, 2, len(path3.lines))
	assert.Equal(t, lines[0], path3.lines[0])
	assert.Equal(t, lines[1], path3.lines[1])
}
