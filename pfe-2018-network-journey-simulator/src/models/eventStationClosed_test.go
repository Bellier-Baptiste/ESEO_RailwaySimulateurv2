package models

import (
	"log"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestEventStationClosed(t *testing.T) {
	println("*** eventStationClosed_test.go ***")
	start, err := time.Parse("15:04", "00:01")
	end, err := time.Parse("15:04", "00:02")
	if err != nil {
		log.Fatal("Failed to initialize station closed times", err)
	}

	stationClosed := NewEventStationClosed(0, start, end)

	assert.Equal(t, 0, stationClosed.IdStation(), "Bad station id")
	stationClosed.SetidStation(2)
	assert.Equal(t, 2, stationClosed.IdStation(), "Bad station id")

	assert.Equal(t, "0000-01-01 00:01:00 +0000 UTC", stationClosed.Start().String(), "Bad Start time")
	assert.Equal(t, "0000-01-01 00:02:00 +0000 UTC", stationClosed.End().String(), "Bad Start time")

	assert.False(t, stationClosed.Finished(), "Bad event status")
	stationClosed.SetFinished(true)
	assert.True(t, stationClosed.Finished(), "Bad event status")
}
