/*
File : eventAttendancePeak_test.go

Brief :

Date : N/A

Author : Team v2, Paul TRÉMOUREUX (quality check)

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
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestEventAttendancePeak(t *testing.T) {
	println("*** eventAttendancePeak_test.go ***")
	start, _ := time.Parse("15:04", "00:01")

	attendancePeak := NewEventAttendancePeak(1, 10, start)

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
