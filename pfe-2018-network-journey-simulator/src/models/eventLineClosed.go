package models

import "time"

type EventLineClosed struct {
	start          time.Time
	end            time.Time
	idStationStart int
	idStationEnd   int
	finished       bool
}

func NewEventLineClosed(idStationStart int, idStationEnd int, start time.Time, end time.Time) EventLineClosed {
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

func (elc *EventLineClosed) SetidStationStart(id int) {
	elc.idStationStart = id
}

func (elc *EventLineClosed) IdStationEnd() int {
	return elc.idStationEnd
}

func (elc *EventLineClosed) SetidStationEnd(id int) {
	elc.idStationEnd = id
}
