package models

import "time"

type EventStationClosed struct {
	start     time.Time
	end       time.Time
	idStation int
	finished  bool
}

func NewEventStationClosed(idStation int, start time.Time,
	end time.Time) EventStationClosed {
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

func (esc *EventStationClosed) SetidStation(id int) {
	esc.idStation = id
}
