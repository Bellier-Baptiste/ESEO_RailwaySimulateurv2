package models

import (
	"time"
)

type EventAttendancePeak struct {
	time		time.Time
	idStation	int
	size		int
	finished	bool
}

func NewEventAttendancePeak(idStation int, time time.Time, size int) EventAttendancePeak {
	return EventAttendancePeak {
		time :			time,
		idStation : 	idStation,
		size : 			size,
		finished :		false,
	}
}

func (eap *EventAttendancePeak) Finished() bool {
	return eap.finished
}

func (eap *EventAttendancePeak) SetFinished(f bool) {
	eap.finished = f
}

func (eap *EventAttendancePeak) Time() time.Time {
	return eap.time
}

func (eap *EventAttendancePeak) IdStation() int {
	return eap.idStation
}

func (eap *EventAttendancePeak) SetidStation(id int) {
	eap.idStation = id
}

func (eap *EventAttendancePeak) Size() int {
	return eap.size
}

func (eap *EventAttendancePeak) SetSize(s int) {
	eap.size = s
}