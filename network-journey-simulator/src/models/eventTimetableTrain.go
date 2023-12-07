/*
Package models

File : eventTimetableTrain.go

Brief : This file contains the EventTimetableTrain struct and its methods.

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
EventTimetableTrain is the structure that manage the event of a peak of
attendance.

Attributes :
  - line *MetroLine : the line of the train
  - station *MetroStation : the station where the train is
  - nextStation *MetroStation : the next station of the train
  - train *MetroTrain : the train
  - direction string : the direction of the train
  - arrival time.Time : the time of the arrival of the train
  - nextArrival time.Time : the time of the next arrival of the train
  - departure time.Time : the time of the departure of the train
  - nextDeparture time.Time : the time of the next departure of the train
  - isRevenue bool : true if the train is a revenue train, false otherwise
  - tripNumber int : the number of the trip of the train

Methods :
  - Test() error : test the EventTimetableTrain struct
  - Line() *MetroLine : return the line of the train
  - Station() *MetroStation : return the station where the train is
  - NextStation() *MetroStation : return the next station of the train
  - Train() *MetroTrain : return the train
  - Direction() string : return the direction of the train
  - Arrival() time.Time : return the time of the arrival of the train
  - NextArrival() time.Time : return the time of the next arrival of the train
  - Departure() time.Time : return the time of the departure of the train
  - NextDeparture() time.Time : return the time of the next departure of the
    train
  - IsRevenue() bool : return true if the train is a revenue train, false
    otherwise
  - TripNumber() int : return the number of the trip of the train
*/
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
NewEventTimetableTrain creates a new EventTimetableTrain struct.

Param :
  - line *MetroLine : the line of the train
  - metroStationTab [2]MetroStation : the station where the train is and the
    next station of the train
  - train *MetroTrain : the train
  - direction string : the direction of the train
  - timeTab [4]time.Time : the time of the arrival of the train, the time of
    the next arrival of the train, the time of the departure of the train and
    the time of the next departure of the train
  - isRevenue bool : true if the train is a revenue train, false otherwise
  - tripNumber int : the number of the trip of the train

Return :
  - *EventTimetableTrain : the new EventTimetableTrain struct
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

/*
Test tests the EventTimetableTrain struct.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - error : nil if the test is successful, an error otherwise
*/
func (e *EventTimetableTrain) Test() error {
	return nil
}

/*
Getters
*/

/*
Line returns the line of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - *MetroLine : the line of the train
*/
func (e *EventTimetableTrain) Line() *MetroLine {
	return e.line
}

/*
Station returns the station where the train is.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - *MetroStation : the station where the train is
*/
func (e *EventTimetableTrain) Station() *MetroStation {
	return e.station
}

/*
NextStation returns the next station of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - *MetroStation : the next station of the train
*/
func (e *EventTimetableTrain) NextStation() *MetroStation {
	return e.nextStation
}

/*
Train returns the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - *MetroTrain : the train
*/
func (e *EventTimetableTrain) Train() *MetroTrain {
	return e.train
}

/*
Direction returns the direction of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - string : the direction of the train
*/
func (e *EventTimetableTrain) Direction() string {
	return e.direction
}

/*
Arrival returns the time of the arrival of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - time.Time : the time of the arrival of the train
*/
func (e *EventTimetableTrain) Arrival() time.Time {
	return e.arrival
}

/*
NextArrival returns the time of the next arrival of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - time.Time : the time of the next arrival of the train
*/
func (e *EventTimetableTrain) NextArrival() time.Time {
	return e.nextArrival
}

/*
Departure returns the time of the departure of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - time.Time : the time of the departure of the train
*/
func (e *EventTimetableTrain) Departure() time.Time {
	return e.departure
}

/*
NextDeparture returns the time of the next departure of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - time.Time : the time of the next departure of the train
*/
func (e *EventTimetableTrain) NextDeparture() time.Time {
	return e.nextDeparture
}

/*
IsRevenue returns true if the train is a revenue train, false otherwise.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - bool : true if the train is a revenue train, false otherwise
*/
func (e *EventTimetableTrain) IsRevenue() bool {
	return e.isRevenue
}

/*
TripNumber returns the number of the trip of the train.

Param :
  - e *EventTimetableTrain : the EventTimetableTrain struct

Return :
  - int : the number of the trip of the train
*/
func (e *EventTimetableTrain) TripNumber() int {
	return e.tripNumber
}
