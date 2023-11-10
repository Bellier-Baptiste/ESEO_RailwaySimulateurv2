/*
Package models

File : eventLineDelay.go

Brief : This file contains the EventLineDelay struct and its methods.

Date : 10/02/2020

Author :
  - Team v1
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
EventLineDelay is the structure that manage the event of a delay on a line.

Attributes :
  - delay int : the delay of the line
  - start time.Time : start time of the event
  - end time.Time : end time of the event
  - idStationStart int : id of the start station
  - idStationEnd int : id of the end station
  - finished bool : true if the event is finished, false otherwise

Methods :
  - Finished() bool : return true if the event is finished, false otherwise
  - SetFinished(f bool) : set the finished attribute to f
  - Start() time.Time : return the start time of the event
  - End() time.Time : return the end time of the event
  - IdStationStart() int : return the id of the start station
  - IdStationEnd() int : return the id of the end station
  - Delay() int : return the delay of the line
*/
type EventLineDelay struct {
	delay          int
	start          time.Time
	end            time.Time
	idStationStart int
	idStationEnd   int
	finished       bool
}

/*
NewEventLineDelay is the constructor of the EventLineDelay struct.

Param :
  - idStationStart int : the id of the start station
  - idStationEnd int : the id of the end station
  - delay int : the delay of the line
  - start time.Time : the start time of the delay
  - end time.Time : the end time of the delay

Return :
  - EventLineDelay : the new EventLineDelay struct
*/
func NewEventLineDelay(idStationStart, idStationEnd, delay int,
	start, end time.Time) EventLineDelay {
	return EventLineDelay{
		end:            end,
		start:          start,
		idStationStart: idStationStart,
		idStationEnd:   idStationEnd,
		delay:          delay,
		finished:       false,
	}
}

/*
Finished returns the finished attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - bool : the finished attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) Finished() bool {
	return eld.finished
}

/*
SetFinished sets the finished attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct
  - f bool : the new value of the finished attribute
*/
func (eld *EventLineDelay) SetFinished(f bool) {
	eld.finished = f
}

/*
Start returns the start attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - time.Time : the start attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) Start() time.Time {
	return eld.start
}

/*
End returns the end attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - time.Time : the end attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) End() time.Time {
	return eld.end
}

/*
IdStationStart returns the idStationStart attribute of the EventLineDelay
struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - int : the idStationStart attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) IdStationStart() int {
	return eld.idStationStart
}

/*
IdStationEnd returns the idStationEnd attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - int : the idStationEnd attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) IdStationEnd() int {
	return eld.idStationEnd
}

/*
Delay returns the delay attribute of the EventLineDelay struct.

Param :
  - eld *EventLineDelay : the EventLineDelay struct

Return :
  - int : the delay attribute of the EventLineDelay struct
*/
func (eld *EventLineDelay) Delay() int {
	return eld.delay
}
