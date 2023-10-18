package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func generateTrain() *MetroTrain {
	return &MetroTrain{
		id:       1,
		capacity: 120,
		line:     &MetroLine{id: 1, name: "Trafalgar"},
	}
}

func TestMetroTrain_StationsBeforeReturn(t *testing.T) {
	mapTest := generateMapSimpleLine()

	trainTest := MetroTrain{
		id:          1,
		line:        mapTest.lines[0],
		nextStation: mapTest.lines[0].stations[1],
		direction:   "up",
	}

	assert.Equal(t, 2, len(trainTest.StationsBeforeReturn()))
	assert.Equal(t, mapTest.lines[0].stations[1].id, trainTest.StationsBeforeReturn()[0].id)
	assert.Equal(t, mapTest.lines[0].stations[2].id, trainTest.StationsBeforeReturn()[1].id)

	trainTest.direction = "down"
	stations := trainTest.StationsBeforeReturn()
	assert.Equal(t, 2, len(stations))
	assert.Equal(t, mapTest.lines[0].stations[1].id, stations[0].id)
	assert.Equal(t, mapTest.lines[0].stations[0].id, stations[1].id)
}
