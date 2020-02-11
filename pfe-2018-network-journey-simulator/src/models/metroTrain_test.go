package models

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func generateTrain() *MetroTrain {
	return &MetroTrain{
		id:       1,
		capacity: 120,
		line:     &MetroLine{id: 1, name: "Trafalgar",},
	}
}

func TestMetroTrain_StationsBeforeReturn(t *testing.T) {
	map_test := generateMapSimpleLine()

	train_test := MetroTrain{
		id:          1,
		line:        map_test.lines[0],
		nextStation: map_test.lines[0].stations[1],
		direction:   "up",
	}

	assert.Equal(t, 2, len(train_test.StationsBeforeReturn()))
	assert.Equal(t, map_test.lines[0].stations[1].id, train_test.StationsBeforeReturn()[0].id)
	assert.Equal(t, map_test.lines[0].stations[2].id, train_test.StationsBeforeReturn()[1].id)

	train_test.direction = "down"
	stations := train_test.StationsBeforeReturn()
	assert.Equal(t, 2, len(stations))
	assert.Equal(t, map_test.lines[0].stations[1].id, stations[0].id)
	assert.Equal(t, map_test.lines[0].stations[0].id, stations[1].id)
}