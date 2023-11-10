/*
Package models

File : eventLineClosed.go

Brief : This file contains the EventLineClosed struct and its methods.

Date : 10/02/2020

Author :
  - Team v2
  - Paul TRÉMOUREUX (quality check)

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

import "time"

/*
EventLineClosed is the structure that manage the event of a line closed.

Attributes :
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event
  - idStationStart int : the id of the start station
  - idStationEnd int : the id of the end station
  - finished bool : true if the event is finished, false otherwise

Methods :
  - Finished() bool : return true if the event is finished, false otherwise
  - SetFinished(f bool) : set the finished attribute to f
  - Start() time.Time : return the start time of the event
  - End() time.Time : return the end time of the event
  - IdStationStart() int : return the id of the start station
  - SetIdStationStart(id int) : set the id of the start station
  - IdStationEnd() int : return the id of the end station
  - SetIdStationEnd(id int) : set the id of the end station
*/
type EventLineClosed struct {
	start          time.Time
	end            time.Time
	idStationStart int
	idStationEnd   int
	finished       bool
}

/*
NewEventLineClosed creates a new EventLineClosed struct.

Param :
  - idStationStart int : the id of the start station
  - idStationEnd int : the id of the end station
  - start time.Time : the time of the start of the event
  - end time.Time : the time of the end of the event

Return :
  - EventLineClosed : the new EventLineClosed struct
*/
func NewEventLineClosed(idStationStart, idStationEnd int,
	start, end time.Time) EventLineClosed {
	return EventLineClosed{
		end:            end,
		start:          start,
		idStationStart: idStationStart,
		idStationEnd:   idStationEnd,
		finished:       false,
	}
}

/*
Finished returns the finished attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - bool : the finished attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) Finished() bool {
	return elc.finished
}

/*
SetFinished sets the finished attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - bool : the finished attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) SetFinished(f bool) {
	elc.finished = f
}

/*
Start returns the start attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - time.Time : the start attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) Start() time.Time {
	return elc.start
}

/*
End returns the end attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - time.Time : the end attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) End() time.Time {
	return elc.end
}

/*
IdStationStart returns the idStationStart attribute of the EventLineClosed
struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - int : the idStationStart attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) IdStationStart() int {
	return elc.idStationStart
}

/*
SetIdStationStart sets the idStationStart attribute of the EventLineClosed
struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - int : the idStationStart attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) SetIdStationStart(id int) {
	elc.idStationStart = id
}

/*
IdStationEnd returns the idStationEnd attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - int : the idStationEnd attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) IdStationEnd() int {
	return elc.idStationEnd
}

/*
SetIdStationEnd sets the idStationEnd attribute of the EventLineClosed struct.

Param :
  - elc *EventLineClosed : the EventLineClosed struct

Return :
  - int : the idStationEnd attribute of the EventLineClosed struct
*/
func (elc *EventLineClosed) SetIdStationEnd(id int) {
	elc.idStationEnd = id
}
