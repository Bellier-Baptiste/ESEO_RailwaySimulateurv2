/*
Package models

File : eventStationClosed.go

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

import "time"

type EventStationClosed struct {
	start     time.Time
	end       time.Time
	idStation int
	finished  bool
}

func NewEventStationClosed(idStation int,
	start, end time.Time) EventStationClosed {
	return EventStationClosed{
		end:       end,
		start:     start,
		idStation: idStation,
		finished:  false,
	}
}

func (esc *EventStationClosed) Finished() bool {
	return esc.finished
}

func (esc *EventStationClosed) SetFinished(f bool) {
	esc.finished = f
}

func (esc *EventStationClosed) ConcernStation(id int) bool {
	return esc.idStation == id
}

func (esc *EventStationClosed) IdStation() int {
	return esc.idStation
}

func (esc *EventStationClosed) Start() time.Time {
	return esc.start
}

func (esc *EventStationClosed) End() time.Time {
	return esc.end
}
