package models

import (
	"log"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestEventLineClosed(t *testing.T) {
	println("*** eventLineClosed_test.go ***")
	start, err := time.Parse("15:04", "00:01")
	end, err := time.Parse("15:04", "00:02")
	if err != nil {
		log.Fatal("Failed to initialize line closed times", err)
	}

	lineClosed := NewEventLineClosed(0, 1, start, end)

	assert.Equal(t, 0, lineClosed.IdStationStart(), "Bad Start station id")
	lineClosed.SetIdStationStart(2)
	assert.Equal(t, 2, lineClosed.IdStationStart(), "Bad Start station id")

	assert.Equal(t, 1, lineClosed.IdStationEnd(), "Bad End station id")
	lineClosed.SetIdStationEnd(3)
	assert.Equal(t, 3, lineClosed.IdStationEnd(), "Bad End station id")

	assert.Equal(t, "0000-01-01 00:01:00 +0000 UTC", lineClosed.Start().String(), "Bad Start time")
	assert.Equal(t, "0000-01-01 00:02:00 +0000 UTC", lineClosed.End().String(), "Bad Start time")

	assert.False(t, lineClosed.Finished(), "Bad event status")
	lineClosed.SetFinished(true)
	assert.True(t, lineClosed.Finished(), "Bad event status")

}
