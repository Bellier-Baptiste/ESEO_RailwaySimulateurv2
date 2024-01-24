/*
Package models

File : eventRampPeak.go

Brief : This file contains the EventRampPeak struct and its methods.

Date : 08/12/2023

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
	"time"
)

/*
EventRampPeak is the structure that manage the event of a peak of
ramp.

Attributes :
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event
  - peak time.Time : the time of the peak
  - idStation int : the id of the station concerned by the peak
  - size int : the size of the peak
  - finished bool : true if the event is finished, false otherwise

Methods :
  - Finished() bool : return true if the event is finished, false otherwise
  - SetFinished(f bool) : set the finished attribute to f
  - Time() time.Time : return the time of the peak
  - IdStation() int : return the id of the station concerned by the peak
  - SetIdStation(id int) : set the id of the station concerned by the peak
  - Size() int : return the size of the peak
  - SetSize(s int) : set the size of the peak
*/
type EventRampPeak struct {
	start     time.Time
	end       time.Time
	peak      time.Time
	idStation int
	peakSize  int
	finished  bool
}

/*
NewEventRampPeak creates a new EventRampPeak struct.

Param :
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event
  - peak time.Time : the time of the peak
  - idStation int : the id of the station
  - size int : the size of the peak

Return :
  - EventRampPeak : the new EventRampPeak struct
*/
func NewEventRampPeak(start time.Time, end time.Time, peak time.Time,
	idStation int, size int) EventRampPeak {
	return EventRampPeak{
		start:     start,
		end:       end,
		peak:      peak,
		idStation: idStation,
		peakSize:  size,
		finished:  false,
	}
}

/*
GetStart returns the start attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct

Return :
  - time.Time : the start attribute of the EventRampPeak struct
*/
func (erp *EventRampPeak) GetStart() time.Time {
	return erp.start
}

/*
SetStart sets the start attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - start time.Time : the new value of the start attribute
*/
func (erp *EventRampPeak) SetStart(start time.Time) {
	erp.start = start
}

/*
GetEnd returns the end attribute of the EventRampPeak struct.
*/
func (erp *EventRampPeak) GetEnd() time.Time {
	return erp.end
}

/*
SetEnd sets the end attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - end time.Time : the new value of the end attribute
*/
func (erp *EventRampPeak) SetEnd(end time.Time) {
	erp.end = end
}

/*
GetPeak returns the peak attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct

Return :
  - time.Time : the peak attribute of the EventRampPeak struct
*/
func (erp *EventRampPeak) GetPeak() time.Time {
	return erp.peak
}

/*
SetPeak sets the peak attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - peak time.Time : the new value of the peak attribute
*/
func (erp *EventRampPeak) SetPeak(peak time.Time) {
	erp.peak = peak
}

/*
GetIdStation returns the idStation attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct

Return :
  - int : the idStation attribute of the EventRampPeak struct
*/
func (erp *EventRampPeak) GetIdStation() int {
	return erp.idStation
}

/*
SetIdStation sets the idStation attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - id int : the new value of the idStation attribute
*/
func (erp *EventRampPeak) SetIdStation(id int) {
	erp.idStation = id
}

/*
GetPeakSize returns the size attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct

Return :
  - int : the peakSize attribute of the EventRampPeak struct
*/
func (erp *EventRampPeak) GetPeakSize() int {
	return erp.peakSize
}

/*
SetPeakSize sets the size attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - s int : the new value of the size attribute
*/
func (erp *EventRampPeak) SetPeakSize(s int) {
	erp.peakSize = s
}

/*
IsFinished returns the finished attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct

Return :
  - bool : the finished attribute of the EventRampPeak struct
*/
func (erp *EventRampPeak) IsFinished() bool {
	return erp.finished
}

/*
SetFinished sets the finished attribute of the EventRampPeak struct.

Param :
  - erp *EventRampPeak : the EventRampPeak struct
  - f bool : the new value of the finished attribute
*/
func (erp *EventRampPeak) SetFinished(f bool) {
	erp.finished = f
}
