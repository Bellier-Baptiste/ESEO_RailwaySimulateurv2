/*
Package models

File : eventStationClosed.go

Brief : This file contains the EventStationClosed struct and its methods.

Date : 24/01/2019

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
EventStationClosed is the structure that manage the event of a station closed.

Attributes :
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event
  - idStation int : the id of the station concerned by the event
  - finished bool : true if the event is finished, false otherwise

Methods :
  - Finished() bool : return true if the event is finished, false otherwise
  - SetFinished(f bool) : set the finished attribute to f
  - ConcernStation(id int) bool : return true if the event concern the station
  - IdStation() int	: return the id of the station concerned by the event
  - Start() time.Time : return the start time of the event
  - End() time.Time : return the end time of the event
*/
type EventStationClosed struct {
	start     time.Time
	end       time.Time
	idStation int
	finished  bool
}

/*
NewEventStationClosed is a function that creates a new EventStationClosed.

Param :
  - idStation int : the id of the station concerned by the event
  - start time.Time : the start time of the event
  - end time.Time : the end time of the event

Return :
  - EventStationClosed : the new EventStationClosed
*/
func NewEventStationClosed(idStation int,
	start, end time.Time) EventStationClosed {
	return EventStationClosed{
		end:       end,
		start:     start,
		idStation: idStation,
		finished:  false,
	}
}

/*
Finished is a method that return true if the event is finished, false otherwise.

Param :
  - esc *EventStationClosed : the EventStationClosed

Return :
  - bool : true if the event is finished, false otherwise
*/
func (esc *EventStationClosed) Finished() bool {
	return esc.finished
}

/*
SetFinished is a method that set the finished attribute to f.

Param :
  - esc *EventStationClosed : the EventStationClosed
  - f bool : the new value of the finished attribute
*/
func (esc *EventStationClosed) SetFinished(f bool) {
	esc.finished = f
}

/*
ConcernStation is a method that return true if the event concern the station.

Param :
  - esc *EventStationClosed : the EventStationClosed
  - id int : the id of the station

Return :
  - bool : true if the event concern the station, false otherwise
*/
func (esc *EventStationClosed) ConcernStation(id int) bool {
	return esc.idStation == id
}

/*
IdStation is a method that return the id of the station concerned by the event.

Param :
  - esc *EventStationClosed : the EventStationClosed

Return :
  - int : the id of the station concerned by the event
*/
func (esc *EventStationClosed) IdStation() int {
	return esc.idStation
}

/*
Start is a method that return the start time of the event.

Param :
  - esc *EventStationClosed : the EventStationClosed

Return :
  - time.Time : the start time of the event
*/
func (esc *EventStationClosed) Start() time.Time {
	return esc.start
}

/*
End is a method that return the end time of the event.

Param :
  - esc *EventStationClosed : the EventStationClosed

Return :
  - time.Time : the end time of the event
*/
func (esc *EventStationClosed) End() time.Time {
	return esc.end
}
