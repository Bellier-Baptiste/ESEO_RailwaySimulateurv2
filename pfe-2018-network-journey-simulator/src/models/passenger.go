package models

import (
	"strconv"
	"time"
)

const (
	ADL = iota // 0
	STD = iota // 1
	DIS = iota // 2
	CHD = iota // 3
	SEN = iota // 4
)

type Passenger struct {
	id    string
	trips []*Trip
	//the trip the passenger is doing. is null when the passenger is outside the network (station / trains)
	currentTrip *Trip
	//the next trip the passenger will be taking. can be equal to currentTrip
	nextTrip *Trip
	kind     int

	timeArrivalLastStation time.Time
}

//--- Constructor

func NewPassenger(id string, kind int) Passenger {
	var aPassenger Passenger
	aPassenger.id = id
	aPassenger.trips = nil
	aPassenger.kind = kind
	return aPassenger
}

//--- Getters & Setters

func (p *Passenger) Id() string {
	return p.id
}

func (p *Passenger) SetId(id string) {
	p.id = id
}

func (p *Passenger) Trips() []*Trip {
	return p.trips
}

func (p *Passenger) SetTrips(trips []*Trip) {
	p.trips = trips
}

func (p *Passenger) CurrentTrip() *Trip {
	return p.currentTrip
}

func (p *Passenger) RemoveTrip(trip *Trip) {
	i := -1
	for j, trip2 := range p.trips {
		if trip2.departureTime.Equal(trip.departureTime) {
			//departureTime is the only parameter not affected by multiple change. we should maybe add an ID to the trips
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

func (p *Passenger) NextTrip() *Trip {
	return p.nextTrip
}

func (p *Passenger) Kind() int {
	return p.kind
}

func (p *Passenger) getTimeArrivalLastStation() time.Time {
	return p.timeArrivalLastStation
}

func (p *Passenger) setTimeArrivalLastStation(t time.Time) {
	p.timeArrivalLastStation = t
}

//--- Functions & Methods

//calculate the next trip the passenger should be taking
func (p *Passenger) calculateNextTrip() {
	p.nextTrip = nil
	for i := range p.trips {
		if !p.trips[i].IsCompleted() && (p.nextTrip == nil || p.trips[i].departureTime.Before(p.nextTrip.departureTime)) {
			p.nextTrip = p.trips[i]
		}
	}
}

func (p *Passenger) AddTrip(trip *Trip) {
	p.SetTrips(append(p.Trips(), trip))
}

//trip should be in Trips
func (p *Passenger) SetCurrentTrip(trip *Trip) {
	p.currentTrip = trip
}

func (p *Passenger) ClearCurrentTrip() {
	p.currentTrip = nil
}

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
