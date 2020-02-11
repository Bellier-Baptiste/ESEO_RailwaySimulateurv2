package models

import "time"

type EventTimetableTrain struct {
	line          *MetroLine
	station       *MetroStation
	nextStation   *MetroStation
	train         *MetroTrain
	direction     string
	arrival       time.Time
	nextArrival   time.Time
	departure     time.Time
	nextDeparture time.Time
	isRevenue     bool
	tripNumber    int
}

/*
events are information for 1 line in a timetable
in order to write csv file :
0 line name
1 train number
2 TRS number
3 trip number
4 is revenue
5 direction
6 source station
7 time arrived to the source station
8 time departed from the source station
9 destination station
10 time arrived to the destination station
11 time departed from the destination station
12 depot in/out --- not used in naia
13 car number --- not used in naia
14 train trip --- not used in naia
15 train km --- not used in naia
*/

/**
@param line *MetroLine
@param station*MetroStation
@param nextStation*MetroStation
@param train *MetroTrain
@param direction string
@param arrival time.Time
@param nextArrival time.Time
@param departure time.Time
@param nextDeparture time.Time
@param isRevenue bool
@param tripNumber int
@return event EventTimetableTrain //constructor
*/
func NewEventTimetableTrain(line *MetroLine, station *MetroStation, nextStation *MetroStation, train *MetroTrain, direction string, arrival time.Time, nextArrival time.Time, departure time.Time, nextDeparture time.Time, isRevenue bool, tripNumber int) *EventTimetableTrain {
	return &EventTimetableTrain{line: line, station: station, nextStation: nextStation, train: train, direction: direction, arrival: arrival, nextArrival: nextArrival, departure: departure, nextDeparture: nextDeparture, isRevenue: isRevenue, tripNumber: tripNumber}
}

// Getters
func (e *EventTimetableTrain) Line() *MetroLine { return e.line }

func (e *EventTimetableTrain) Station() *MetroStation { return e.station }

func (e *EventTimetableTrain) NextStation() *MetroStation { return e.nextStation }

func (e *EventTimetableTrain) Train() *MetroTrain { return e.train }

func (e *EventTimetableTrain) Direction() string { return e.direction }

func (e *EventTimetableTrain) Arrival() time.Time { return e.arrival }

func (e *EventTimetableTrain) NextArrival() time.Time { return e.nextArrival }

func (e *EventTimetableTrain) Departure() time.Time { return e.departure }

func (e *EventTimetableTrain) NextDeparture() time.Time { return e.nextDeparture }

func (e *EventTimetableTrain) IsRevenue() bool { return e.isRevenue }

func (e *EventTimetableTrain) TripNumber() int { return e.tripNumber }
