/*
Package models

File : eventAttendancePeak.go

Brief : This file contains the EventAttendancePeak struct and its methods.

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
	"time"
)

/*
EventAttendancePeak is the structure that manage the event of a peak of
attendance.

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
type EventAttendancePeak struct {
	start     time.Time
	end       time.Time
	peak      time.Time
	idStation int
	size      int
	finished  bool
}

/*
NewEventAttendancePeak creates a new EventAttendancePeak struct.

Param :
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event
  - peak time.Time : the time of the peak
  - idStation int : the id of the station
  - size int : the size of the peak

Return :
  - EventAttendancePeak : the new EventAttendancePeak struct
*/
func NewEventAttendancePeak(start time.Time, end time.Time, peak time.Time,
	idStation int, size int) EventAttendancePeak {
	return EventAttendancePeak{
		start:     start,
		end:       end,
		peak:      peak,
		idStation: idStation,
		size:      size,
		finished:  false,
	}
}

/*
GetStart returns the start attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct

Return :
  - time.Time : the start attribute of the EventAttendancePeak struct
*/
func (eap *EventAttendancePeak) GetStart() time.Time {
	return eap.start
}

/*
SetStart sets the start attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - start time.Time : the new value of the start attribute
*/
func (eap *EventAttendancePeak) SetStart(start time.Time) {
	eap.start = start
}

/*
GetEnd returns the end attribute of the EventAttendancePeak struct.
*/
func (eap *EventAttendancePeak) GetEnd() time.Time {
	return eap.end
}

/*
SetEnd sets the end attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - end time.Time : the new value of the end attribute
*/
func (eap *EventAttendancePeak) SetEnd(end time.Time) {
	eap.end = end
}

/*
GetPeak returns the peak attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct

Return :
  - time.Time : the peak attribute of the EventAttendancePeak struct
*/
func (eap *EventAttendancePeak) GetPeak() time.Time {
	return eap.peak
}

/*
SetPeak sets the peak attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - peak time.Time : the new value of the peak attribute
*/
func (eap *EventAttendancePeak) SetPeak(peak time.Time) {
	eap.peak = peak
}

/*
GetIdStation returns the idStation attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct

Return :
  - int : the idStation attribute of the EventAttendancePeak struct
*/
func (eap *EventAttendancePeak) GetIdStation() int {
	return eap.idStation
}

/*
SetIdStation sets the idStation attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - id int : the new value of the idStation attribute
*/
func (eap *EventAttendancePeak) SetIdStation(id int) {
	eap.idStation = id
}

/*
GetSize returns the size attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct

Return :
  - int : the size attribute of the EventAttendancePeak struct
*/
func (eap *EventAttendancePeak) GetSize() int {
	return eap.size
}

/*
SetSize sets the size attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - s int : the new value of the size attribute
*/
func (eap *EventAttendancePeak) SetSize(s int) {
	eap.size = s
}

/*
IsFinished returns the finished attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct

Return :
  - bool : the finished attribute of the EventAttendancePeak struct
*/
func (eap *EventAttendancePeak) IsFinished() bool {
	return eap.finished
}

/*
SetFinished sets the finished attribute of the EventAttendancePeak struct.

Param :
  - eap *EventAttendancePeak : the EventAttendancePeak struct
  - f bool : the new value of the finished attribute
*/
func (eap *EventAttendancePeak) SetFinished(f bool) {
	eap.finished = f
}
