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

func NewEventLineDelay( idStationStart int, idStationEnd int, delay int, start time.Time, end time.Time) EventLineDelay {
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
