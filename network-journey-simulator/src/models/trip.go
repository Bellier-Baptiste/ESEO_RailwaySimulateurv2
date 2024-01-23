/*
Package models

File : trip.go

Brief : This file contains the Trip struct and its methods.

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
	"log"
	"math/rand"
	"network-journey-simulator/src/configs"
	"time"
)

/*
Trip is the structure that manage the trip of a passenger.

Attributes :
  - departureTime time.Time : the departure time of the trip
  - arrivalTime time.Time : the arrival time of the trip
  - path PathStation : the path of the trip

Methods :
  - DepartureTime() time.Time : return the departureTime attribute of the
    Trip struct
  - ArrivalTime() time.Time : return the arrivalTime attribute of the Trip
    struct
  - Path() *PathStation : return the path attribute of the Trip struct
  - SetPath(path PathStation) : set the path attribute of the Trip struct
  - SetDepartureTime(time time.Time) : set the departureTime attribute of
    the Trip struct
  - SetArrivalTime(time time.Time) : set the arrivalTime attribute of the
    Trip struct
  - IsStarted(currentTime time.Time) bool : return true if the trip is
    started at the given time, false otherwise
  - IsCompleted() bool : return true if the trip is completed, false
    otherwise
  - IsValid(start time.Time, end time.Time) bool : return true if the trip
    is valid between the given start and end time, false otherwise
  - String() string : return a string representation of the Trip struct
*/
type Trip struct {
	departureTime time.Time
	arrivalTime   time.Time
	path          PathStation
}

//--- constructors

/*
NewTrip creates a new trip with the given departure time and path and a zero
arrival time (not completed) and return it as a pointer to Trip struct instance.

Param :
  - departure time.Time : the departure time of the trip
  - path PathStation : the path of the trip

Return :
  - Trip : the trip created
*/
func NewTrip(departure time.Time, path PathStation) Trip {
	var aTrip Trip
	aTrip.departureTime = departure
	aTrip.path = path
	return aTrip
}

/*
RandomTime generate a random hour and minute in the given day within the
boundaries defined by Business day start and end in the config file.

Param :
  - currentDay time.Time : the day in which the time will be generated

Return :
  - time.Time : the random time generated
*/
func RandomTime(currentDay time.Time) time.Time {
	var currentDayMidnight = time.Date(currentDay.Year(), currentDay.Month(),
		currentDay.Day(), 0, 0, 0, 0, currentDay.Location())
	pickedTime := currentDayMidnight

	instance := configs.GetInstance()
	bDayStart, err := time.ParseDuration(
		instance.BusinessDayStart())
	if err != nil {
		log.Fatal(err, "BusinessDayStart - couldn't parse duration")
	}

	aDay, err := time.ParseDuration("24h")
	if err != nil {
		log.Fatal(err, "a Day- couldn't parse duration")
	}

	bDayEnd, err := time.ParseDuration(instance.BusinessDayEnd())
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

/*
normalDistributedTime generate a normal distributed time with expected time as
mean and duration as standard deviation (in seconds) and return it as a
time.Time instance.

Param :
  - duration int64 : the standard deviation of the normal distribution
  - expectedTime time.Time : the mean of the normal distribution

Return :
  - time.Time : the time limited to the boundaries defined by business day
    start/end
*/
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

/*
randomPath generate a random path in the given map and return it as a
PathStation instance.

Param :
  - aMap Map : the map in which the path will be generated

Return :
  - PathStation : the path generated
*/
func randomPath(aMap Map) PathStation {
	stations := aMap.Stations()
	//generating an index to pick a random station
	departureId := rand.Intn(len(stations))
	departureStation := stations[departureId]
	stations = append(stations[:departureId], stations[departureId+1:]...)

	arrivalStation := stations[rand.Intn(len(stations))]
	path, err := aMap.GetPathStation(departureStation, arrivalStation)
	if err != nil {
		log.Fatal(err, " couldn't get path")
	}
	return path
}

/*
RandomTrip generate a random trip with a random time and a random path return
the trip as a Trip instance.

Param :
  - currentDay time.Time : the day in which the trip will be generated
  - aMap Map : the map in which the trip will be generated

Return :
  - Trip : the trip generated
*/
func RandomTrip(currentDay time.Time, aMap Map) Trip {
	departure := RandomTime(currentDay)
	path := randomPath(aMap)
	trip := NewTrip(departure, path)
	return trip
}

/*
CommutingTrips generate two commuting trips with a random time and a random
path return the trips as a pair of Trip instances.

Param :
  - commutePeriodDuration int64 : the standard deviation of the normal
    distribution
  - morningCommute time.Duration : the mean of the normal distribution
  - eveningCommute time.Duration : the mean of the normal distribution
  - currentDay time.Time : the day in which the trip will be generated
  - aMap Map : the map in which the trip will be generated

Return :
  - Trip : the first trip generated
  - Trip : the second trip generated
*/
func CommutingTrips(commutePeriodDuration int64, morningCommute,
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

/*
DepartureTime returns the departureTime attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct

Return :
  - time.Time : the departureTime attribute of the Trip struct
*/
func (t *Trip) DepartureTime() time.Time {
	return t.departureTime
}

/*
ArrivalTime returns the arrivalTime attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct

Return :
  - time.Time : the arrivalTime attribute of the Trip struct
*/
func (t *Trip) ArrivalTime() time.Time {
	return t.arrivalTime
}

/*
Path returns the path attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct

Return :
  - *PathStation : the path attribute of the Trip struct
*/
func (t *Trip) Path() *PathStation {
	return &t.path
}

/*
SetPath sets the path attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct
  - path PathStation : the new value of the path attribute
*/
func (t *Trip) SetPath(path PathStation) {
	t.path = path
}

/*
SetDepartureTime sets the departureTime attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct
  - time time.Time : the new value of the departureTime attribute
*/
func (t *Trip) SetDepartureTime(time time.Time) {
	t.departureTime = time
}

/*
SetArrivalTime sets the arrivalTime attribute of the Trip struct.

Param :
  - t *Trip : the Trip struct
  - time time.Time : the new value of the arrivalTime attribute
*/
func (t *Trip) SetArrivalTime(time time.Time) {
	t.arrivalTime = time
}

/*
IsStarted returns true if the trip is started at the given time, false
otherwise.

Param :
  - t *Trip : the Trip struct
  - currentTime time.Time : the time to compare with the departure time

Return :
  - bool : true if the trip is started at the given time, false otherwise
*/
func (t *Trip) IsStarted(currentTime time.Time) bool {
	return t.departureTime.Before(currentTime)
}

/*
IsCompleted returns true if the trip is completed, false otherwise.

Param :
  - t *Trip : the Trip struct

Return :
  - bool : true if the trip is completed, false otherwise
*/
func (t *Trip) IsCompleted() bool {
	return !t.arrivalTime.IsZero()
}

/*
IsValid returns true if the trip is valid between the given start and end
time, false otherwise.

Param :
  - t *Trip : the Trip struct
  - start time.Time : the start time
  - end time.Time : the end time

Return :
  - bool : true if the trip is valid between the given start and end time,
    false otherwise
*/
func (t *Trip) IsValid(start time.Time, end time.Time) bool {
	return t.departureTime.After(start) && t.departureTime.Before(end)
}

/*
String returns a string representation of the Trip struct.

Param :
  - t *Trip : the Trip struct

Return :
  - string : a string representation of the Trip struct
*/
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
