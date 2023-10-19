/*
Package models

File : trip.go

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

import (
	"log"
	"math/rand"
	"pfe-2018-network-journey-simulator/src/configs"
	"time"
)

type Trip struct {
	departureTime time.Time
	arrivalTime   time.Time
	path          PathStation
}

//--- constructors

func NewTrip(departure time.Time, path PathStation) Trip {
	var aTrip Trip
	aTrip.departureTime = departure
	aTrip.path = path
	return aTrip
}

/*
RandomTime
Generate a random hour and minute in the given day within the boundaries
defined by Business day start and end
*/
func RandomTime(currentDay time.Time) time.Time {
	var currentDayMidnight = time.Date(currentDay.Year(), currentDay.Month(),
		currentDay.Day(), 0, 0, 0, 0, currentDay.Location())
	pickedTime := currentDayMidnight

	bDayStart, err := time.ParseDuration(
		configs.GetInstance().BusinessDayStart())
	if err != nil {
		log.Fatal(err, "BusinessDayStart - couldn't parse duration")
	}

	aDay, err := time.ParseDuration("24h")
	if err != nil {
		log.Fatal(err, "a Day- couldn't parse duration")
	}

	bDayEnd, err := time.ParseDuration(configs.GetInstance().BusinessDayEnd())
	if err != nil {
		log.Fatal(err, "BusinessDayEnd - couldn't parse duration")
	}

	pickedTime = pickedTime.Add(bDayStart)
	minutesSpan := bDayEnd.Minutes() + 1440 - bDayStart.Minutes()
	minute := rand.Intn(int(minutesSpan))
	pickedTime = pickedTime.Add(time.Duration(minute) * time.Minute)

	if pickedTime.After(currentDayMidnight.Add(aDay)) {
		pickedTime = pickedTime.Add(-aDay)
	}

	return pickedTime
}

// Generate a normal distributed time with expected time as mean
// and duration as standard deviation (in seconds)
// return the time limited to the boundaries defined by business day start/end
func normalDistributedTime(duration int64, expectedTime time.Time) time.Time {
	config := configs.GetInstance()
	bDayStart, err := time.ParseDuration(config.BusinessDayStart())
	if err != nil {
		log.Fatal(err, "BusinessDayStart - couldn't parse duration")
	}

	aDay, err := time.ParseDuration("24h")
	if err != nil {
		log.Fatal(err, "a Day- couldn't parse duration")
	}

	bDayEnd, err := time.ParseDuration(config.BusinessDayEnd())
	if err != nil {
		log.Fatal(err, "BusinessDayEnd - couldn't parse duration")
	}

	maxWait := config.GetMaxTimeInStationPassenger()

	unixTime := expectedTime.Unix()
	var currentDayMidnight = time.Date(expectedTime.Year(),
		expectedTime.Month(), expectedTime.Day(),
		0, 0, 0, 0, expectedTime.Location())
	unixBusinessDayStart := currentDayMidnight.Add(bDayStart).Unix()
	unixBusinessDayEnd := currentDayMidnight.Add(aDay).Add(bDayEnd).Unix()

	tripUnixTime := int64(rand.NormFloat64()*(float64(duration)) +
		float64(unixTime))
	if tripUnixTime < unixBusinessDayStart {
		tripUnixTime = unixBusinessDayStart
		//The passenger will catch the first train at the beginning of
		//the business day
	}
	if tripUnixTime > unixBusinessDayEnd {
		//the passenger will catch one of the last trains, we subtract some
		//time, so he does enter the station just when it closes
		tripUnixTime = unixBusinessDayEnd - int64(maxWait.Seconds())
	}

	aTime := time.Unix(tripUnixTime, 0)
	return aTime.UTC()
}

// Generate a random path in a map
// return the path.
func randomPath(aMap Map) PathStation {
	stations := aMap.Stations()
	//generating an index to pick a random station
	departureId := rand.Intn(len(stations))
	departureStation := stations[departureId]
	stations = append(stations[:departureId], stations[departureId+1:]...)

	arrivalStation := stations[rand.Intn(len(stations))]
	path, err := aMap.GetPathStation(departureStation, arrivalStation)
	if err != nil {
		log.Fatal(err, "couldn't get path")
	}
	return path
}

/*
RandomTrip
generate a random trip with a random time and a random path
return the trip
*/
func RandomTrip(currentDay time.Time, aMap Map) Trip {
	departure := RandomTime(currentDay)
	path := randomPath(aMap)
	trip := NewTrip(departure, path)
	return trip
}

func CommuntingTrips(commutePeriodDuration int64, morningCommute,
	eveningCommute time.Duration, currentDay time.Time, aMap Map) (Trip, Trip) {
	morningTime := normalDistributedTime(commutePeriodDuration,
		currentDay.Add(morningCommute))
	eveningTime := normalDistributedTime(commutePeriodDuration,
		currentDay.Add(eveningCommute))
	morningPath := randomPath(aMap)

	i := morningPath.StartStation().Id()
	j := morningPath.EndStation().id
	eveningPath := *aMap.graph[i][j]
	morningTrip := NewTrip(morningTime, morningPath)
	eveningTrip := NewTrip(eveningTime, eveningPath)
	return morningTrip, eveningTrip
}

//--- constructors

func (t *Trip) DepartureTime() time.Time {
	return t.departureTime
}

func (t *Trip) ArrivalTime() time.Time {
	return t.arrivalTime
}

func (t *Trip) Path() *PathStation {
	return &t.path
}

func (t *Trip) SetPath(path PathStation) {
	t.path = path
}

func (t *Trip) SetDepartureTime(time time.Time) {
	t.departureTime = time
}

func (t *Trip) SetArrivalTime(time time.Time) {
	t.arrivalTime = time
}

func (t *Trip) IsStarted(currentTime time.Time) bool {
	return t.departureTime.Before(currentTime)
}

func (t *Trip) IsCompleted() bool {
	return !t.arrivalTime.IsZero()
}

func (t *Trip) IsValid(start time.Time, end time.Time) bool {
	return t.departureTime.After(start) && t.departureTime.Before(end)
}

// return a string representation of a trip
func (t *Trip) String() string {
	//station_start\tstation_end\ttime_start\ttime_end
	var output string
	if len(t.path.stations) == 0 {
		return "no path in trip"
	}
	output = "start : " + t.path.stations[0].name + "\t" +
		"end : " + t.path.EndStation().name + "\t" +
		"start time : " + t.departureTime.String() + "\t"

	if t.IsCompleted() {
		output += "end time : " + t.arrivalTime.String()
	} else {
		output += "end time : Not Completed"
	}

	return output
}
