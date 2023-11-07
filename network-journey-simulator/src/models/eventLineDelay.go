/*
Package models

File : eventLineDelay.go

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

type EventLineDelay struct {
	delay          int
	start          time.Time
	end            time.Time
	idStationStart int
	idStationEnd   int
	finished       bool
}

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

func (eld *EventLineDelay) Finished() bool {
	return eld.finished
}

func (eld *EventLineDelay) SetFinished(f bool) {
	eld.finished = f
}

func (eld *EventLineDelay) Start() time.Time {
	return eld.start
}

func (eld *EventLineDelay) End() time.Time {
	return eld.end
}

func (eld *EventLineDelay) IdStationStart() int {
	return eld.idStationStart
}

func (eld *EventLineDelay) IdStationEnd() int {
	return eld.idStationEnd
}

func (eld *EventLineDelay) Delay() int {
	return eld.delay
}
