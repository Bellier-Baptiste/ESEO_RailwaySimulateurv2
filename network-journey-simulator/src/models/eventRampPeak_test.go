/*
File : eventRampPeak_test.go

Brief : eventRampPeak_test.go runs tests on the eventGaussianPeak.go
file.

Date : 26/12/2023

Author :
  - Alexis BONAMY

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

/*
TestEventGaussianPeak tests the EventRampPeak struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
func TestEventRampPeakModel(t *testing.T) {
	// create a new eventGaussianPeak arguments
	start := time.Now()
	end := time.Now()
	peak := time.Now()
	idStation := 1
	size := 10

	// create a new eventRampPeak
	eventRampPeak := NewEventRampPeak(start, end, peak,
		idStation, size)

	// check if the eventRampPeak is created
	assert.NotNil(t, eventRampPeak, "eventRampPeak should not be nil")

	// check if the eventRampPeak is created with the right values
	assert.Equal(t, start, eventRampPeak.GetStart(), "Bad Start time")
	assert.Equal(t, end, eventRampPeak.GetEnd(), "Bad End time")
	assert.Equal(t, peak, eventRampPeak.GetEnd(), "Bad Peak time")
	assert.Equal(t, idStation, eventRampPeak.GetIdStation(), "Bad Start station id")
	assert.Equal(t, size, eventRampPeak.GetPeakSize(), "Bad population size")
	assert.False(t, eventRampPeak.IsFinished(), "Bad event status")
}
