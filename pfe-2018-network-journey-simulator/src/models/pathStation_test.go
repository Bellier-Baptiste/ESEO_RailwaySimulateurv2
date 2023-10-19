package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

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
