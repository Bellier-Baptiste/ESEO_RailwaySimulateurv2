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
)

/*
TestNewEventRampPeak tests the EventRampPeak struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
func TestNewEventRampPeak(t *testing.T) {
	start := time.Now()
	end := start.Add(time.Hour)
	peak := start.Add(time.Minute)
	idStation := 123
	peakSize := 42

	event := NewEventRampPeak(start, end, peak, idStation, peakSize)

	if event.start != start ||
		event.end != end ||
		event.peak != peak ||
		event.idStation != idStation ||
		event.peakSize != peakSize ||
		event.finished != false {
		t.Errorf("NewEventRampPeak did not initialize the struct correctly.")
	}
}

/*
TestEventRampPeakMethods tests the methods of the EventRampPeak struct.

# It tests if the methods work properly

Input : t *testing.T

Expected : The methods work properly
*/
func TestEventRampPeakMethods(t *testing.T) {
	start := time.Now()
	end := start.Add(time.Hour)
	peak := start.Add(time.Minute)
	idStation := 123
	peakSize := 42

	event := NewEventRampPeak(start, end, peak, idStation, peakSize)

	// Test Getters
	if event.GetStart() != start {
		t.Errorf("GetStart() returned incorrect value.")
	}

	if event.GetEnd() != end {
		t.Errorf("GetEnd() returned incorrect value.")
	}

	if event.GetPeak() != peak {
		t.Errorf("GetPeak() returned incorrect value.")
	}

	if event.GetIdStation() != idStation {
		t.Errorf("GetIdStation() returned incorrect value.")
	}

	if event.GetPeakSize() != peakSize {
		t.Errorf("GetPeakSize() returned incorrect value.")
	}

	if event.IsFinished() != false {
		t.Errorf("IsFinished() returned incorrect value.")
	}

	// Test Setters
	newStart := start.Add(2 * time.Hour)
	event.SetStart(newStart)
	if event.GetStart() != newStart {
		t.Errorf("SetStart() did not set the value correctly.")
	}

	newEnd := end.Add(2 * time.Hour)
	event.SetEnd(newEnd)
	if event.GetEnd() != newEnd {
		t.Errorf("SetEnd() did not set the value correctly.")
	}

	newPeak := peak.Add(2 * time.Hour)
	event.SetPeak(newPeak)
	if event.GetPeak() != newPeak {
		t.Errorf("SetPeak() did not set the value correctly.")
	}

	newIdStation := 456
	event.SetIdStation(newIdStation)
	if event.GetIdStation() != newIdStation {
		t.Errorf("SetIdStation() did not set the value correctly.")
	}

	newPeakSize := 84
	event.SetPeakSize(newPeakSize)
	if event.GetPeakSize() != newPeakSize {
		t.Errorf("SetPeakSize() did not set the value correctly.")
	}

	event.SetFinished(true)
	if event.IsFinished() != true {
		t.Errorf("SetFinished() did not set the value correctly.")
	}
}
