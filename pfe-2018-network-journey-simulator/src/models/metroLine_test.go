/*
File : metroLine_test.go

Brief :

Date : N/A

Author : Team v1, Team v2, Paul TRÉMOUREUX (quality check)

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
	"strconv"
	"testing"

	"github.com/stretchr/testify/assert"
)

var stationsTest = []MetroStation{
	{name: "station1", id: 0},
	{name: "station2", id: 1},
	{name: "station3", id: 2},
}

func clearStationsTest() {
	stationsTest = []MetroStation{
		{name: "station1", id: 0},
		{name: "station2", id: 1},
		{name: "station3", id: 2},
	}
}

func initLine() MetroLine {
	clearStationsTest()
	return MetroLine{
		stations: []*MetroStation{
			&stationsTest[0],
			&stationsTest[1],
			&stationsTest[2],
		},
		name:                  "test",
		passengersMaxPerTrain: 10,
		trainNumber:           1,
	}
}

func TestGetPath(t *testing.T) {
	var line = initLine()

	var path = line.GetPath(&stationsTest[0], &stationsTest[1])

	assert.NotNil(t, path)
	for i := 0; i < len(path); i++ {
		println(path[i].name)
	}
	assert.Equal(t, 2, len(path))
	assert.Equal(t, stationsTest[0], *path[0])
	assert.Equal(t, stationsTest[1], *path[1])
}

func TestEqualsMetroLine(t *testing.T) {
	var line = initLine()
	var line2 = initLine()

	assert.True(t, line.Equals(line2))
}

func TestPositionInLine(t *testing.T) {
	var line = initLine()

	assert.Equal(t, line.PositionInLine(&stationsTest[0]), 0)
	assert.Equal(t, line.PositionInLine(&stationsTest[1]), 1)
	assert.Equal(t, line.PositionInLine(&stationsTest[2]), 2)
}

func TestDurationOfLine(t *testing.T) {
	line := initLine()
	var graph [][]int
	graph = [][]int{
		{-1, 100, -1},
		{100, -1, 100},
		{-1, 100, -1}}
	graphDelay := [][]int{
		{0, 0, 0},
		{0, 0, 0},
		{0, 0, 0}}
	stationsTest[0].setId(0)
	stationsTest[1].setId(1)
	stationsTest[2].setId(2)
	assert.Equal(t, 200, line.DurationOfLine(graph, graphDelay), "duration of the travel time over this line should be 200, actually it's : "+strconv.Itoa(line.DurationOfLine(graph, graphDelay)))
}
