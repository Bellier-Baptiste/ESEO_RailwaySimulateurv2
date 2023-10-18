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
