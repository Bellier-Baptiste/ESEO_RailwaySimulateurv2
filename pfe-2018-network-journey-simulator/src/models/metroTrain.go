/*
Package models

File : metroTrain.go

Brief : This file contains the MetroTrain struct and its methods.

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
	"fmt"
	"log"
	"pfe-2018-network-journey-simulator/src/configs"
	"time"
)

/*
MetroTrain is the structure that manage the train.

Attributes :
  - id int : the id of the train
  - line *MetroLine : the line on which the train is
  - direction string : the direction of the train
  - directionChanged bool : true if the train changed direction, false
    otherwise
  - tripNumber int : the number of trip of the train
  - capacity int : the capacity of the train
  - currentStation *MetroStation : the current station of the train
  - nextStation *MetroStation : the next station of the train
  - timeArrivalCurrentStation time.Time : the time of arrival of the train
    in the current station
  - timeArrivalNextStation time.Time : the time of arrival of the train
    in the next station

Methods :
  - GetLine() *MetroLine : return the line on which the train is (pointer)
  - GetCurrentStation() *MetroStation : return the current station of the
    train (pointer)
  - GetNextStation() *MetroStation : return the next station of the train
    (pointer)
  - GetDirectionChanged() bool : return the directionChanged attribute of
    the train
  - SetDirectionChanged(boolean bool) : set the directionChanged attribute
    of the train
  - GetTripNumber() int : return the tripNumber attribute of the train
  - SetTripNumber(tripNumber int) : set the tripNumber attribute of the
    train
  - GetNextOpenedStationUD() *MetroStation : return the next opened
    station of the train (pointer) (up to down loop)
  - GetNextOpenedStationDU() *MetroStation : return the next opened
    station of the train (pointer) (down to up loop)
  - GetNextOpenedStation() *MetroStation : return the next opened
    station of the train (pointer)
  - GetDirection() string : return the direction of the train
  - Id() int : return the id of the train
  - SetId(id int) : set the id of the train
  - GetAvailableCapacity(p *Population) int : return the available
    capacity of the train
  - CurrentStation() *MetroStation : return the current station of the
    train
  - TimeArrivalCurrentStation() time.Time : return the time of arrival of
    the train in the current station
  - SetTimeArrivalCurrentStation(timeArrivalCurrentStation time.Time) :
    set the time of arrival of the train in the current station
  - TimeArrivalNextStation() time.Time : return the time of arrival of
    the train in the next station
  - SetTimeArrivalNextStation(timeArrivalNextStation time.Time) : set the
    time of arrival of the train in the next station
  - arriveInStation(station *MetroStation, aTime time.Time) : executed
    everytime the train arrive in a station
  - ArriveInNextStation(aTime time.Time) : executed everytime the train
    arrive in the next station
  - departFromStation(aTime time.Time, timeBetweenStation int) : executed
    everytime the train leave the current station
  - intToDuration(value int) time.Duration : convert an int to a
    time.Duration
  - UpdateBeforeTimeArrivalNextStation(train *MetroTrain, currentTime
    time.Time, delay, timeBetweenStation int) : used when a train is
    delayed. It will update the time of arrival in the next station
  - UpdateAfterTimeArrivalNextStation(train *MetroTrain, currentTime
    time.Time, delay, timeBetweenStation int) : used when a train is
    delayed. It will update the time of arrival in the next station
  - StationsBeforeReturn() []*MetroStation : return a list containing all
    stations between the train and the end of the line (included)
*/
type MetroTrain struct {
	id               int
	line             *MetroLine
	direction        string
	directionChanged bool //detect when end of line is reached
	tripNumber       int
	capacity         int //number of passengers the train can support

	currentStation *MetroStation //should be NiL when moving
	nextStation    *MetroStation //should never be equal to currentStation

	timeArrivalCurrentStation time.Time //used to show when there are delays
	timeArrivalNextStation    time.Time //used to show when there are delays
}

/*
getter
*/

/*
GetLine return the line on which the train is (pointer).

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroLine : the line on which the train is
*/
func (mt *MetroTrain) GetLine() *MetroLine {
	return mt.line
}

/*
GetCurrentStation return the current station of the train (pointer).

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the current station of the train
*/
func (mt *MetroTrain) GetCurrentStation() *MetroStation {
	return mt.currentStation
}

/*
GetNextStation return the next station of the train (pointer).

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the next station of the train
*/
func (mt *MetroTrain) GetNextStation() *MetroStation {
	return mt.nextStation
}

/*
GetDirectionChanged return the directionChanged attribute of the train.

Param :
  - mt *MetroTrain : the train

Return :
  - bool : the directionChanged attribute of the train
*/
func (mt *MetroTrain) GetDirectionChanged() bool {
	return mt.directionChanged
}

/*
SetDirectionChanged set the directionChanged attribute of the train.

Param :
  - mt *MetroTrain : the train
  - boolean bool : the new value of the directionChanged attribute
*/
func (mt *MetroTrain) SetDirectionChanged(boolean bool) {
	mt.directionChanged = boolean
}

/*
GetTripNumber return the tripNumber attribute of the train.

Param :
  - mt *MetroTrain : the train

Return :
  - int : the tripNumber attribute of the train
*/
func (mt *MetroTrain) GetTripNumber() int {
	return mt.tripNumber
}

/*
SetTripNumber set the tripNumber attribute of the train.

Param :
  - mt *MetroTrain : the train
  - tripNumber int : the new value of the tripNumber attribute
*/
func (mt *MetroTrain) SetTripNumber(tripNumber int) {
	mt.tripNumber = tripNumber
}

/*
GetNextOpenedStationUD is up to down loop for GetNextOpenedStation. It's used
when the train is at the end of the line and has to go in reverse.

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the next opened station of the train
*/
func (mt *MetroTrain) GetNextOpenedStationUD() *MetroStation {
	for i := range mt.line.Stations()[mt.line.PositionInLine(
		mt.GetNextStation()):] {
		if !mt.line.Stations()[i].StatusIsClosed() {
			return mt.line.Stations()[i]
		}
	}
	return nil
}

/*
GetNextOpenedStationDU is down to up loop for GetNextOpenedStation. It's used
when the train is at the end of the line and has to go in reverse.

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the next opened station of the train
*/
func (mt *MetroTrain) GetNextOpenedStationDU() *MetroStation {
	for i := mt.line.PositionInLine(mt.GetNextStation()); i > 0; i-- {
		if !mt.line.Stations()[i].StatusIsClosed() {
			return mt.line.Stations()[i]
		}
	}
	return nil
}

/*
GetNextOpenedStation return the next opened station of the train (pointer).

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the next opened station of the train
*/
func (mt *MetroTrain) GetNextOpenedStation() *MetroStation {
	if !mt.GetNextStation().StatusIsClosed() {
		return mt.GetNextStation()
	}
	var ms *MetroStation
	if mt.direction == "up" {
		ms = mt.GetNextOpenedStationUD()
		//go in reverse
		ms = mt.GetNextOpenedStationDU()
	} else {
		ms = mt.GetNextOpenedStationDU()
		//go in reverse
		ms = mt.GetNextOpenedStationUD()
	}
	return ms
}

/*
GetDirection return the direction of the train.

Param :
  - mt *MetroTrain : the train

Return :
  - string : the direction of the train
*/
func (mt *MetroTrain) GetDirection() string {
	return mt.direction
}

//--- constructor

/*
NewMetroTrain creates a train on a line, with a direction.

Param :
  - line *MetroLine : the line on which the train will be created
  - direction string : the direction in which the train will go

Return :
  - *MetroTrain : the new train

("up" -> stations[0]->station[9999]/ "down")
*/
func NewMetroTrain(line *MetroLine, direction string) *MetroTrain {
	//TODO change direction by bool "goReverse" ?
	var config = configs.GetInstance()

	var out = MetroTrain{
		line:             line,
		direction:        direction,
		capacity:         config.CapacityPerTrain(),
		directionChanged: false,
	}

	if direction == "down" {
		out.currentStation = line.stations[len(line.stations)-1]
		out.nextStation = line.stations[len(line.stations)-2]

	} else {
		out.currentStation = line.stations[0]
		out.nextStation = line.stations[1]

	}

	return &out
}

//--- Getters & Setters

/*
Id return the id of the train.

Param :
  - mt *MetroTrain : the train

Return :
  - int : the id of the train
*/
func (mt *MetroTrain) Id() int {
	return mt.id
}

/*
SetId set the id of the train.

Param :
  - mt *MetroTrain : the train
  - id int : the new value of the id attribute
*/
func (mt *MetroTrain) SetId(id int) {
	mt.id = id
}

/*
GetAvailableCapacity return the available capacity of the train.

Param :
  - mt *MetroTrain : the train
  - p *Population : the population

Return :
  - int : the available capacity of the train
*/
func (mt *MetroTrain) GetAvailableCapacity(p *Population) int {
	currentLoad := len(p.InTrains()[mt.id])
	if currentLoad > mt.capacity {
		return 0
	}
	return mt.capacity - currentLoad
}

/*
CurrentStation return the current station of the train.

Param :
  - mt *MetroTrain : the train

Return :
  - *MetroStation : the current station of the train
*/
func (mt *MetroTrain) CurrentStation() *MetroStation {
	return mt.currentStation
}

/*
TimeArrivalCurrentStation return the time of arrival of the train in the
current station.

Param :
  - mt *MetroTrain : the train

Return :
  - time.Time : the time of arrival of the train in the current station
*/
func (mt *MetroTrain) TimeArrivalCurrentStation() time.Time {
	return mt.timeArrivalCurrentStation
}

/*
SetTimeArrivalCurrentStation set the time of arrival of the train in the
current station.

Param :
  - mt *MetroTrain : the train
  - timeArrivalCurrentStation time.Time : the new value of the
    timeArrivalCurrentStation attribute
*/
func (mt *MetroTrain) SetTimeArrivalCurrentStation(
	timeArrivalCurrentStation time.Time) {
	mt.timeArrivalCurrentStation = timeArrivalCurrentStation
}

/*
TimeArrivalNextStation return the time of arrival of the train in the
next station.

Param :
  - mt *MetroTrain : the train

Return :
  - time.Time : the time of arrival of the train in the next station
*/
func (mt *MetroTrain) TimeArrivalNextStation() time.Time {
	return mt.timeArrivalNextStation
}

/*
SetTimeArrivalNextStation set the time of arrival of the train in the
next station.

Param :
  - mt *MetroTrain : the train
  - timeArrivalNextStation time.Time : the new value of the
    timeArrivalNextStation attribute
*/
func (mt *MetroTrain) SetTimeArrivalNextStation(
	timeArrivalNextStation time.Time) {
	mt.timeArrivalNextStation = timeArrivalNextStation
}

//--- Functions & Methods

// ideally, station == train.nextStation. But i know we're prone to errors :P

/*
arriveInStation is executed everytime the train arrive in a station.
Alter its current/next attribute, but don't touch to passengers. It's the
responsability of the simulation to do it.

Param :
  - mt *MetroTrain : the train
  - station *MetroStation : the station in which the train arrive
  - aTime time.Time : the time of arrival of the train in the station
*/
func (mt *MetroTrain) arriveInStation(station *MetroStation, aTime time.Time) {
	mt.currentStation = station
	mt.timeArrivalCurrentStation = aTime

	positionInLine := mt.line.PositionInLine(mt.currentStation)

	//next station
	if mt.direction == "up" && positionInLine == len(mt.line.stations)-1 {
		mt.direction = "down"
		mt.SetDirectionChanged(true)
	}
	if mt.direction == "down" && positionInLine == 0 {
		mt.direction = "up"
		mt.SetDirectionChanged(true)
	}

	if mt.direction == "up" {
		mt.nextStation = mt.line.stations[positionInLine+1]
	} else {
		mt.nextStation = mt.line.stations[positionInLine-1]
	}
}

/*
ArriveInNextStation is executed everytime the train arrive in the next station.

Param :
  - mt *MetroTrain : the train
  - aTime time.Time : the time of arrival of the train in the station
*/
func (mt *MetroTrain) ArriveInNextStation(aTime time.Time) {
	mt.arriveInStation(mt.nextStation, aTime)
}

/*
ArriveInCurrentStation is executed everytime the train leave the current
station.

Param :
  - mt *MetroTrain : the train
  - aTime time.Time : the time of arrival of the train in the station
  - timeBetweenStation int : the time between the current station and the
    next station
*/
func (mt *MetroTrain) departFromStation(aTime time.Time,
	timeBetweenStation int) {
	mt.currentStation = nil
	mt.timeArrivalNextStation = aTime.Add(intToDuration(timeBetweenStation))
}

/*
intToDuration convert an int to a time.Duration.

Param :
  - value int : the int to convert

Return :
  - time.Duration : the time.Duration
*/
func intToDuration(value int) time.Duration {
	return time.Duration(value) * time.Second
}

/*
UpdateBeforeTimeArrivalNextStation is used when a train is delayed. It will
update the time of arrival in the next station.

Param :
  - train *MetroTrain : the train
  - currentTime time.Time : the current time of the simulation
  - delay int : the delay of the train
  - timeBetweenStation int : the time between the current station and the
    next station
*/
func UpdateBeforeTimeArrivalNextStation(train *MetroTrain,
	currentTime time.Time, delay, timeBetweenStation int) {
	if delay < 0 {
		log.Fatal("bad entry in "+
			"MetroTrain.UpdateBeforeTimeArrivalNextStation : "+
			"delay can't be negative ", delay)
	}
	timeLeft := train.TimeArrivalNextStation().Sub(currentTime)
	//timeLeft is a duration
	timeLeftInt := timeLeft.Seconds()
	if timeLeftInt < 0 {
		timeLeftInt = timeLeftInt * -1
	}
	percentOFLine := timeLeftInt / float64(delay)
	newTimeLeft := float64(timeBetweenStation+delay) * percentOFLine
	newTimeLeftDuration := time.Duration(newTimeLeft) * time.Second
	if newTimeLeftDuration.Seconds() < 0 {
		log.Fatal("bad duration in "+
			"MetroTrain.UpdateBeforeTimeArrivalNextStation : "+
			"nextTimeArrival can't be before current time of simulation ",
			newTimeLeftDuration.Seconds())
	}
	train.SetTimeArrivalNextStation(currentTime.Add(newTimeLeftDuration))
}

/*
UpdateAfterTimeArrivalNextStation is used when a train is delayed. It will
update the time of arrival in the next station.

Param :
  - train *MetroTrain : the train
  - currentTime time.Time : the current time of the simulation
  - delay int : the delay of the train
  - timeBetweenStation int : the time between the current station and the
    next station
*/
func UpdateAfterTimeArrivalNextStation(train *MetroTrain,
	currentTime time.Time, delay, timeBetweenStation int) {
	if delay < 0 {
		log.Fatal("bad entry in "+
			"MetroTrain.UpdateAfterTimeArrivalNextStation : "+
			"delay can't be negative ", delay)
	}
	timeLeft := train.TimeArrivalNextStation().Sub(currentTime)
	//timeLeft is a duration
	timeLeftInt := timeLeft.Seconds()
	if timeLeftInt < 0 {
		timeLeftInt = timeLeftInt * -1
	}
	percentOFLine := timeLeftInt / float64(delay)
	newTimeLeft := float64(timeBetweenStation) * percentOFLine
	newTimeLeftDuration := time.Duration(newTimeLeft) * time.Second
	if newTimeLeftDuration.Seconds() < 0 {
		log.Fatal("bad duration in "+
			"MetroTrain.UpdateAfterTimeArrivalNextStation : "+
			"nextTimeArrival can't be before current time of simulation ",
			newTimeLeftDuration.Seconds())
	}
	fmt.Println("newTimeLeftDuration : ", newTimeLeftDuration)
	train.SetTimeArrivalNextStation(currentTime.Add(newTimeLeftDuration))
}

/*
StationsBeforeReturn return a list containing all stations between
the train and the end of the line (included).

Param :
  - mt *MetroTrain : the train

Return :
  - []*MetroStation : the list of stations
*/
func (mt *MetroTrain) StationsBeforeReturn() []*MetroStation {
	var output []*MetroStation
	output = append(output, mt.nextStation)
	position := mt.line.PositionInLine(mt.nextStation)
	if mt.direction == "up" {
		output = mt.line.stations[position:]
	} else {
		tmp := mt.line.stations[:position]
		for i := len(tmp) - 1; i >= 0; i-- {
			output = append(output, tmp[i])
		}
	}

	return output
}
