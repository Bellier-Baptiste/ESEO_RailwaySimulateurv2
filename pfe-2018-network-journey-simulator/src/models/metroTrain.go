package models

import (
	"fmt"
	"log"
	"pfe-2018-network-journey-simulator/src/configs"
	"time"
)

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

// getter
func (train *MetroTrain) GetLine() *MetroLine {
	return train.line
}

func (train *MetroTrain) GetCurrentStation() *MetroStation {
	return train.currentStation
}

func (train *MetroTrain) GetNextStation() *MetroStation {
	return train.nextStation
}

func (train *MetroTrain) GetDirectionChanged() bool {
	return train.directionChanged
}

func (train *MetroTrain) SetDirectionChanged(boolean bool) {
	train.directionChanged = boolean
}

func (train *MetroTrain) GetTripNumber() int {
	return train.tripNumber
}

func (train *MetroTrain) SetTripNumber(tripNumber int) {
	train.tripNumber = tripNumber
}

func (train *MetroTrain) GetNextOpenedStation() *MetroStation {
	if !train.GetNextStation().StatusIsClosed() {
		return train.GetNextStation()
	}
	if train.direction == "up" {
		for i := range train.line.Stations()[train.line.PositionInLine(train.GetNextStation()):] {
			if !train.line.Stations()[i].StatusIsClosed() {
				return train.line.Stations()[i]
			}
		}
		//go in reverse
		for i := train.line.PositionInLine(train.GetNextStation()); i > 0; i-- {
			if !train.line.Stations()[i].StatusIsClosed() {
				return train.line.Stations()[i]
			}
		}
	} else {
		for i := train.line.PositionInLine(train.GetNextStation()); i > 0; i-- {
			if !train.line.Stations()[i].StatusIsClosed() {
				return train.line.Stations()[i]
			}
		}
		//go in reverse

		for i := range train.line.Stations()[train.line.PositionInLine(train.GetNextStation()):] {
			if !train.line.Stations()[i].StatusIsClosed() {
				return train.line.Stations()[i]
			}
		}
	}
	return nil
}

func (train *MetroTrain) GetDirection() string {
	return train.direction
}

//--- constructor

// NewMetroTrain creates a train
// :param line *MetroLine the line on which the train will be
// :param direction the direction in which the train will go ("up" -> stations[0]->station[9999]/ "down")
func NewMetroTrain(line *MetroLine, direction string) *MetroTrain { //TODO change direction by bool "goReverse" ?
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

func (m *MetroTrain) Id() int {
	return m.id
}

func (m *MetroTrain) SetId(id int) {
	m.id = id
}

func (train *MetroTrain) GetAvailableCapacity(p *Population) int {
	currentLoad := len(p.InTrains()[train.id])
	if currentLoad > train.capacity {
		return 0
	}
	return train.capacity - currentLoad
}

func (train *MetroTrain) CurrentStation() *MetroStation {
	return train.currentStation
}

func (train *MetroTrain) TimeArrivalCurrentStation() time.Time {
	return train.timeArrivalCurrentStation
}

func (train *MetroTrain) SetTimeArrivalCurrentStation(timeArrivalCurrentStation time.Time) {
	train.timeArrivalCurrentStation = timeArrivalCurrentStation
}

func (train *MetroTrain) TimeArrivalNextStation() time.Time {
	return train.timeArrivalNextStation
}

func (train *MetroTrain) SetTimeArrivalNextStation(timeArrivalNextStation time.Time) {
	train.timeArrivalNextStation = timeArrivalNextStation
}

//--- Functions & Methods

// should be executed everytime the train arrive in a station. alter its current/next attribute, but don't touch to passengers.
// idealy, station == train.nextStation. But i know we're prone to errors :P
func (train *MetroTrain) arriveInStation(station *MetroStation, aTime time.Time) {
	train.currentStation = station
	train.timeArrivalCurrentStation = aTime

	positionInLine := train.line.PositionInLine(train.currentStation)

	//next station
	if train.direction == "up" && positionInLine == len(train.line.stations)-1 {
		train.direction = "down"
		train.SetDirectionChanged(true)
	}
	if train.direction == "down" && positionInLine == 0 {
		train.direction = "up"
		train.SetDirectionChanged(true)
	}

	if train.direction == "up" {
		train.nextStation = train.line.stations[positionInLine+1]
	} else {
		train.nextStation = train.line.stations[positionInLine-1]
	}
}

func (train *MetroTrain) ArriveInNextStation(aTime time.Time) {
	train.arriveInStation(train.nextStation, aTime)
}

func (train *MetroTrain) departFromStation(station *MetroStation, aTime time.Time, timeBetweenStation int) {
	train.currentStation = nil
	train.timeArrivalNextStation = aTime.Add(intToDuration(timeBetweenStation))
}

func intToDuration(value int) time.Duration {
	return time.Duration(value) * time.Second
}

// before a delay, calculate the new time to the next station
func UpdateBeforeTimeArrivalNextStation(train *MetroTrain, currentTime time.Time, delay int, timeBetweenStation int) {
	if delay < 0 {
		log.Fatal("bad entry in MetroTrain.UpdateBeforeTimeArrivalNextStation : delay can't be negativ ", delay)
	}
	timeLeft := train.TimeArrivalNextStation().Sub(currentTime) //timeLeft is a duration
	timeLeftInt := float64(timeLeft.Seconds())
	if timeLeftInt < 0 {
		timeLeftInt = timeLeftInt * -1
	}
	percentOFLine := timeLeftInt / float64(delay)
	newTimeLeft := float64(timeBetweenStation+delay) * percentOFLine
	newTimeLeftDuration := time.Duration(newTimeLeft) * time.Second
	if newTimeLeftDuration.Seconds() < 0 {
		log.Fatal("bad duration in MetroTrain.UpdateBeforeTimeArrivalNextStation : nextTimeArrival can't be before current time of simulation ", newTimeLeftDuration.Seconds())
	}
	train.SetTimeArrivalNextStation(currentTime.Add(newTimeLeftDuration))
}

// after a delay, calculate the new time to the next station
func UpdateAfterTimeArrivalNextStation(train *MetroTrain, currentTime time.Time, delay int, timeBetweenStation int) {
	if delay < 0 {
		log.Fatal("bad entry in MetroTrain.UpdateAfterTimeArrivalNextStation : delay can't be negativ ", delay)
	}
	timeLeft := train.TimeArrivalNextStation().Sub(currentTime) //timeLeft is a duration
	timeLeftInt := float64(timeLeft.Seconds())
	if timeLeftInt < 0 {
		timeLeftInt = timeLeftInt * -1
	}
	percentOFLine := timeLeftInt / float64(delay)
	newTimeLeft := float64(timeBetweenStation) * percentOFLine
	newTimeLeftDuration := time.Duration(newTimeLeft) * time.Second
	if newTimeLeftDuration.Seconds() < 0 {
		log.Fatal("bad duration in MetroTrain.UpdateAfterTimeArrivalNextStation : nextTimeArrival can't be before current time of simulation ", newTimeLeftDuration.Seconds())
	}
	log.Fatal("newTimeLeftDuration : ", newTimeLeftDuration)
	fmt.Println("newTimeLeftDuration : ", newTimeLeftDuration)
	train.SetTimeArrivalNextStation(currentTime.Add(newTimeLeftDuration))
}

// return a list containing all stations between the train and the end of the line
func (train *MetroTrain) StationsBeforeReturn() []*MetroStation {
	var output []*MetroStation
	output = append(output, train.nextStation)
	position := train.line.PositionInLine(train.nextStation)
	if train.direction == "up" {
		output = train.line.stations[position:]
	} else {
		tmp := train.line.stations[:position]
		for i := len(tmp) - 1; i >= 0; i-- {
			output = append(output, tmp[i])
		}
	}

	return output
}
