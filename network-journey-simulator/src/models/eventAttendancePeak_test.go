/*
File : eventGaussianPeak_test.go

Brief : eventGaussianPeak_test.go runs tests on the eventGaussianPeak.go
file.

Date : 10/02/2020

Author :
  - Team v2
  - Paul TRÉMOUREUX (quality check)
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
TestEventGaussianPeak tests the EventGaussianPeak struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
func TestEventGaussianPeakModel(t *testing.T) {
	// create a new eventGaussianPeak arguments
	start := time.Now()
	end := time.Now()
	peak := time.Now()
	idStation := 1
	size := 10
	peakWidth := 10

	// create a new eventGaussianPeak
	eventGaussianPeak := NewEventGaussianPeak(start, end, peak,
		idStation, size, peakWidth)

	// check if the eventGaussianPeak is created
	assert.NotNil(t, eventGaussianPeak, "eventGaussianPeak should not be nil")

	// check if the eventGaussianPeak is created with the right values
	assert.Equal(t, start, eventGaussianPeak.GetStart(), "Bad Start time")
	assert.Equal(t, end, eventGaussianPeak.GetEnd(), "Bad End time")
	assert.Equal(t, peak, eventGaussianPeak.GetEnd(), "Bad Peak time")
	assert.Equal(t, idStation, eventGaussianPeak.GetIdStation(), "Bad Start station id")
	assert.Equal(t, size, eventGaussianPeak.GetPeakSize(), "Bad population size")
	assert.Equal(t, peakWidth, eventGaussianPeak.GetPeakWidth(), "Bad peak width")
	assert.False(t, eventGaussianPeak.IsFinished(), "Bad event status")
}
