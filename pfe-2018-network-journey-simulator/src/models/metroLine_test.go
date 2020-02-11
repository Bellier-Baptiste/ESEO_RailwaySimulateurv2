package models

import (
	"strconv"
	"testing"

	"github.com/stretchr/testify/assert"
)

var stations_test = []MetroStation{
	{name: "station1", id: 0},
	{name: "station2", id: 1},
	{name: "station3", id: 2},
}

func clearStationsTest() {
	stations_test = []MetroStation{
		{name: "station1", id: 0},
		{name: "station2", id: 1},
		{name: "station3", id: 2},
	}
}

func initLine() MetroLine {
	clearStationsTest()
	return MetroLine{
		stations: []*MetroStation{
			&stations_test[0],
			&stations_test[1],
			&stations_test[2],
		},
		name:                  "test",
		passengersMaxPerTrain: 10,
		trainNumber:           1,
	}
}

func TestGetPath(t *testing.T) {
	var line = initLine()

	var path = line.GetPath(&stations_test[0], &stations_test[1])

	assert.NotNil(t, path)
	for i := 0; i < len(path); i++ {
		println(path[i].name)
	}
	assert.Equal(t, 2, len(path))
	assert.Equal(t, stations_test[0], *path[0])
	assert.Equal(t, stations_test[1], *path[1])
}

func TestEqualsMetroLine(t *testing.T) {
	var line = initLine()
	var line2 = initLine()

	assert.True(t, line.Equals(line2))
}

func TestPositionInLine(t *testing.T) {
	var line = initLine()

	assert.Equal(t, line.PositionInLine(&stations_test[0]), 0)
	assert.Equal(t, line.PositionInLine(&stations_test[1]), 1)
	assert.Equal(t, line.PositionInLine(&stations_test[2]), 2)
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
	stations_test[0].setId(0)
	stations_test[1].setId(1)
	stations_test[2].setId(2)
	assert.Equal(t, 200, line.DurationOfLine(graph, graphDelay), "duration of the travel time over this line should be 200, actually it's : "+strconv.Itoa(line.DurationOfLine(graph, graphDelay)))
}
