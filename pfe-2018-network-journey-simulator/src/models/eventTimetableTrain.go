/*
Package models

File : eventTimetableTrain.go

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

/*
NewEventTimetableTrain

Param :
line *MetroLine
station*MetroStation
nextStation*MetroStation
train *MetroTrain
direction string
arrival time.Time
nextArrival time.Time
departure time.Time
nextDeparture time.Time
isRevenue bool
tripNumber int

Return :
event EventTimetableTrain //constructor
*/
func NewEventTimetableTrain(line *MetroLine,
	metroStationTab [2]MetroStation, train *MetroTrain, direction string,
	timeTab [4]time.Time, isRevenue bool,
	tripNumber int) *EventTimetableTrain {
	e := EventTimetableTrain{
		line:          line,
		station:       &(metroStationTab[0]),
		nextStation:   &(metroStationTab[1]),
		train:         train,
		direction:     direction,
		arrival:       timeTab[0],
		nextArrival:   timeTab[1],
		departure:     timeTab[2],
		nextDeparture: timeTab[3],
		isRevenue:     isRevenue,
		tripNumber:    tripNumber,
	}
	return &e
}

func (e *EventTimetableTrain) Test() error {
	return nil
}

/*
Getters
*/

func (e *EventTimetableTrain) Line() *MetroLine {
	return e.line
}

func (e *EventTimetableTrain) Station() *MetroStation {
	return e.station
}

func (e *EventTimetableTrain) NextStation() *MetroStation {
	return e.nextStation
}

func (e *EventTimetableTrain) Train() *MetroTrain {
	return e.train
}

func (e *EventTimetableTrain) Direction() string {
	return e.direction
}

func (e *EventTimetableTrain) Arrival() time.Time {
	return e.arrival
}

func (e *EventTimetableTrain) NextArrival() time.Time {
	return e.nextArrival
}

func (e *EventTimetableTrain) Departure() time.Time {
	return e.departure
}

func (e *EventTimetableTrain) NextDeparture() time.Time {
	return e.nextDeparture
}

func (e *EventTimetableTrain) IsRevenue() bool {
	return e.isRevenue
}

func (e *EventTimetableTrain) TripNumber() int {
	return e.tripNumber
}
