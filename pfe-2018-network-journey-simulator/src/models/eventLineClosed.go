/*
Package models

File : eventLineClosed.go

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

type EventLineClosed struct {
	start          time.Time
	end            time.Time
	idStationStart int
	idStationEnd   int
	finished       bool
}

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

func (elc *EventLineClosed) Finished() bool {
	return elc.finished
}

func (elc *EventLineClosed) SetFinished(f bool) {
	elc.finished = f
}

func (elc *EventLineClosed) Start() time.Time {
	return elc.start
}

func (elc *EventLineClosed) End() time.Time {
	return elc.end
}

func (elc *EventLineClosed) IdStationStart() int {
	return elc.idStationStart
}

func (elc *EventLineClosed) SetIdStationStart(id int) {
	elc.idStationStart = id
}

func (elc *EventLineClosed) IdStationEnd() int {
	return elc.idStationEnd
}

func (elc *EventLineClosed) SetIdStationEnd(id int) {
	elc.idStationEnd = id
}
