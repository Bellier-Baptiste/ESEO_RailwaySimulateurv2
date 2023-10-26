package models

import (
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestEventAttendancePeak(t *testing.T) {
	println("*** eventAttendancePeak_test.go ***")
	start, _ := time.Parse("15:04", "00:01")

	attendancePeak := NewEventAttendancePeak(1, start, 10)

	assert.Equal(t, 1, attendancePeak.IdStation(), "Bad Start station id")
	attendancePeak.SetidStation(2)
	assert.Equal(t, 2, attendancePeak.IdStation(), "Bad Start station id")

	assert.Equal(t, 10, attendancePeak.Size(), "Bad population size")
	attendancePeak.SetSize(30)
	assert.Equal(t, 30, attendancePeak.Size(), "Bad population size")

	assert.Equal(t, "0000-01-01 00:01:00 +0000 UTC", attendancePeak.Time().String(), "Bad Start time")

	assert.False(t, attendancePeak.Finished(), "Bad event status")
	attendancePeak.SetFinished(true)
	assert.True(t, attendancePeak.Finished(), "Bad event status")

}
