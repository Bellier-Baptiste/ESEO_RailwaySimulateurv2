/*
Package models

File : passenger.go

Brief : This file contains the Passenger struct and its methods.

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

import (
	"strconv"
	"time"
)

const (
	// ADL ==> Adult
	ADL = iota // 0
	// STD ==> Student
	STD = iota // 1
	// DIS ==> Disabled
	DIS = iota // 2
	// CHD ==> Children
	CHD = iota // 3
	// SEN ==> Senior
	SEN = iota // 4
)

/*
Passenger is the structure that manage the passengers.

Attributes :
  - id string : the id of the passenger
  - trips []*Trip : the trips of the passenger
  - currentTrip *Trip : the trip the passenger is doing. is null when the
    passenger is outside the network (station / trains)
  - nextTrip *Trip : the next trip the passenger will be taking. can be
    equal to currentTrip
  - kind int : the kind of the passenger
  - timeArrivalLastStation time.Time : the timeArrivalLastStation of the
    passenger

Methods :
  - Id() string : returns the id of the passenger
  - SetId(id string) : sets the id of the passenger
  - Trips() []*Trip : returns the trips of the passenger
  - SetTrips(trips []*Trip) : sets the trips of the passenger
  - CurrentTrip() *Trip : returns the current trip of the passenger
  - RemoveTrip(trip *Trip) : removes the given trip from the passenger's
    trips
  - NextTrip() *Trip : returns the next trip of the passenger
  - Kind() int : returns the kind of the passenger
  - getTimeArrivalLastStation() time.Time : returns the
    timeArrivalLastStation of the passenger
  - setTimeArrivalLastStation(t time.Time) : sets the
    timeArrivalLastStation of the passenger
  - calculateNextTrip() : calculates the next trip the passenger should be
    taking
  - AddTrip(trip *Trip) : adds the given trip to the passenger's trips
  - SetCurrentTrip(trip *Trip) : sets the current trip of the passenger.
    The current trip should be in Trips
  - ClearCurrentTrip() : clears the current trip of the passenger
  - String() string : returns a string representation of the passenger
*/
type Passenger struct {
	id    string
	trips []*Trip
	//the trip the passenger is doing. is null when the passenger
	//is outside the network (station / trains)
	currentTrip *Trip
	//the next trip the passenger will be taking. can be equal to currentTrip
	nextTrip *Trip
	kind     int

	timeArrivalLastStation time.Time
}

//--- Constructor

/*
NewPassenger creates a new Passenger with the given id and kind.

Param :
  - id string : the id of the passenger
  - kind int : the kind of the passenger

Return :
  - Passenger : the new passenger
*/
func NewPassenger(id string, kind int) Passenger {
	var aPassenger Passenger
	aPassenger.id = id
	aPassenger.trips = nil
	aPassenger.kind = kind
	return aPassenger
}

//--- Getters & Setters

/*
Id returns the id of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - string : the id of the passenger
*/
func (p *Passenger) Id() string {
	return p.id
}

/*
SetId sets the id of the passenger.

Param :
  - p *Passenger : the passenger
  - id string : the new id of the passenger
*/
func (p *Passenger) SetId(id string) {
	p.id = id
}

/*
Trips returns the trips of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - []*Trip : the trips of the passenger
*/
func (p *Passenger) Trips() []*Trip {
	return p.trips
}

/*
SetTrips sets the trips of the passenger.

Param :
  - p *Passenger : the passenger
  - trips []*Trip : the new trips of the passenger
*/
func (p *Passenger) SetTrips(trips []*Trip) {
	p.trips = trips
}

/*
CurrentTrip returns the current trip of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - *Trip : the current trip of the passenger
*/
func (p *Passenger) CurrentTrip() *Trip {
	return p.currentTrip
}

/*
RemoveTrip removes the given trip from the passenger's trips.

Param :
  - p *Passenger : the passenger
  - trip *Trip : the trip to remove
*/
func (p *Passenger) RemoveTrip(trip *Trip) {
	i := -1
	for j, trip2 := range p.trips {
		if trip2.departureTime.Equal(trip.departureTime) {
			//departureTime is the only parameter not affected
			//by multiple change. we should maybe add an ID to the trips
			i = j
			break
		}
	}

	if i == -1 {
		return
	}

	p.trips = append(p.trips[:i], p.trips[i+1:]...)

	//reevaluate nextTrip
	p.calculateNextTrip()
}

/*
NextTrip returns the next trip of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - *Trip : the next trip of the passenger
*/
func (p *Passenger) NextTrip() *Trip {
	return p.nextTrip
}

/*
Kind returns the kind of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - int : the kind of the passenger
*/
func (p *Passenger) Kind() int {
	return p.kind
}

/*
getTimeArrivalLastStation returns the timeArrivalLastStation of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - time.Time : the timeArrivalLastStation of the passenger
*/
func (p *Passenger) getTimeArrivalLastStation() time.Time {
	return p.timeArrivalLastStation
}

/*
setTimeArrivalLastStation sets the timeArrivalLastStation of the passenger.

Param :
  - p *Passenger : the passenger
  - t time.Time : the new timeArrivalLastStation of the passenger
*/
func (p *Passenger) setTimeArrivalLastStation(t time.Time) {
	p.timeArrivalLastStation = t
}

//--- Functions & Methods

/*
calculateNextTrip calculates the next trip the passenger should be taking.

Param :
  - p *Passenger : the passenger
*/
func (p *Passenger) calculateNextTrip() {
	p.nextTrip = nil
	for i := range p.trips {
		if !p.trips[i].IsCompleted() && (p.nextTrip == nil ||
			p.trips[i].departureTime.Before(p.nextTrip.departureTime)) {
			p.nextTrip = p.trips[i]
		}
	}
}

/*
AddTrip adds the given trip to the passenger's trips.

Param :
  - p *Passenger : the passenger
  - trip *Trip : the trip to add
*/
func (p *Passenger) AddTrip(trip *Trip) {
	p.SetTrips(append(p.Trips(), trip))
}

/*
SetCurrentTrip sets the current trip of the passenger. The current trip should
be in Trips.

Param :
  - p *Passenger : the passenger
  - trip *Trip : the current trip of the passenger
*/
func (p *Passenger) SetCurrentTrip(trip *Trip) {
	p.currentTrip = trip
}

/*
ClearCurrentTrip clears the current trip of the passenger.

Param :
  - p *Passenger : the passenger
*/
func (p *Passenger) ClearCurrentTrip() {
	p.currentTrip = nil
}

/*
String returns a string representation of the passenger.

Param :
  - p *Passenger : the passenger

Return :
  - string : the string representation of the passenger
*/
func (p *Passenger) String() string {
	var output = ""
	output += "id : " + p.id + "\t"
	output += "# trips : " + strconv.Itoa(len(p.trips)) + "\n"
	for i, trip := range p.trips {
		output += "\t" + trip.String()
		if i < len(p.trips)-1 {
			output += "\n"
		}
	}

	return output
}
