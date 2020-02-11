package models

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

var lines_test = []MetroLine{
	{name: "A", trainNumber: 5, passengersMaxPerTrain: 120},
	{name: "B", trainNumber: 5, passengersMaxPerTrain: 120},
	{name: "C", trainNumber: 5, passengersMaxPerTrain: 120},
}

func clearLinesTest(){
	lines_test = []MetroLine{
		{name: "A", trainNumber: 5, passengersMaxPerTrain: 120},
		{name: "B", trainNumber: 5, passengersMaxPerTrain: 120},
		{name: "C", trainNumber: 5, passengersMaxPerTrain: 120},
	}
}

func initStation() MetroStation {
	var station = MetroStation{
		id: 0,
		name:     "metroStationTest",
		position: Point{x: 0, y: 0},
	}

	for i := 0; i < len(lines_test); i++ {
		station.AddMetroLine(&lines_test[i])
	}

	return station
}

func TestMetroStation_hasDirectLineTo(t *testing.T) {
	var station1 = initStation()
	var station2 = initStation()

	station2.removeMetroLine(&lines_test[1])
	station2.removeMetroLine(&lines_test[2])

	assert.True(t, station1.hasDirectLineTo(station2))
	station2.removeMetroLine(&lines_test[0])

	assert.False(t, station1.hasDirectLineTo(station2))
}
