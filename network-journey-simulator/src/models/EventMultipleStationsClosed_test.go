/*
File : eventMultipleStationsClosed_test.go

Brief : eventMultipleStationsClosed_test.go runs tests on the eventMultipleStationsClosed.go file.

Date : 10/02/2020

Author :
  - Team v2
  - Paul TRÉMOUREUX (quality check)
  - Marie BORDET

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
	"log"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

/*
TestEventMultipleStationsClosed tests the EventMultipleStationsClosed struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
func TestEventMultipleStationsClosed(t *testing.T) {
	println("*** EventMultipleStationsClosed_test.go ***")
	start, err := time.Parse("15:04", "00:01")
	end, err := time.Parse("15:04", "00:02")
	if err != nil {
		log.Fatal("Failed to initialize multiple stations closed times", err)
	}

	multipleStationsClosed := NewEventMultipleStationsClosed(0, 1, start, end)

	assert.Equal(t, 0, multipleStationsClosed.IdStationStart(), "Bad Start station id")
	multipleStationsClosed.SetIdStationStart(2)
	assert.Equal(t, 2, multipleStationsClosed.IdStationStart(), "Bad Start station id")

	assert.Equal(t, 1, multipleStationsClosed.IdStationEnd(), "Bad End station id")
	multipleStationsClosed.SetIdStationEnd(3)
	assert.Equal(t, 3, multipleStationsClosed.IdStationEnd(), "Bad End station id")

	assert.Equal(t, "0000-01-01 00:01:00 +0000 UTC", multipleStationsClosed.Start().String(), "Bad Start time")
	assert.Equal(t, "0000-01-01 00:02:00 +0000 UTC", multipleStationsClosed.End().String(), "Bad Start time")

	assert.False(t, multipleStationsClosed.Finished(), "Bad event status")
	multipleStationsClosed.SetFinished(true)
	assert.True(t, multipleStationsClosed.Finished(), "Bad event status")

}
