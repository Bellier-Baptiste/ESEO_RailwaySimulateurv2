/*
Package models

File : timetable.go

Brief : This file contains the Timetable struct and its methods.

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
	"network-journey-simulator/src/configs"
	"network-journey-simulator/src/tools"
	"strconv"
	"time"
)

/*
Timetable is the structure that manage the timetable of the metro.

Attributes :
  - eventsTrain []*EventTimetableTrain : the list of events of the timetable

Methods :
  - getEventsTrain() []*EventTimetableTrain : return the list of events of
    the timetable
  - addEventsTrain(event *EventTimetableTrain) : add an event to the list
    of events of the timetable
  - GenerateTimetable(trains []*MetroTrain, graphTimeBetweenStation [][]int,
    graphDelay [][]int) : generate the timetable for the given trains
  - ToCSV() : generate CSV file for the timetable
*/
type Timetable struct {
	eventsTrain []*EventTimetableTrain
}

/*
getEventsTrain return the list of events of the timetable.

Param :
  - timetable *Timetable : the timetable

Return :
  - []*EventTimetableTrain : the list of events of the timetable
*/
func (timetable *Timetable) getEventsTrain() []*EventTimetableTrain {
	return timetable.eventsTrain
}

/*
addEventsTrain add an event to the list of events of the timetable.

Param :
  - timetable *Timetable : the timetable
  - event *EventTimetableTrain : the event to add
*/
func (timetable *Timetable) addEventsTrain(event *EventTimetableTrain) {
	timetable.eventsTrain = append(timetable.eventsTrain, event)
}

// --- Constructor

/*
NewTimetable creates a new Timetable struct.

Param :
  - m *Map : the map
  - trains []*MetroTrain : the list of trains

Return :
  - Timetable : the new Timetable struct
*/
func NewTimetable(m *Map, trains []*MetroTrain) Timetable {
	timetable := Timetable{}
	timetable.GenerateTimetable(trains, m.GraphTimeBetweenStation(),
		m.GraphDelay())
	//TODO pass this function directly here
	return timetable
}

/*
GenerateTimetable generate the timetable for the given trains.

Param :
  - timetable *Timetable : the timetable
  - trains []*MetroTrain : the list of trains
  - graphTimeBetweenStation [][]int : the graph of time between stations
  - graphDelay [][]int : the graph of delay between stations
*/
func (timetable *Timetable) GenerateTimetable(trains []*MetroTrain,
	graphTimeBetweenStation [][]int, graphDelay [][]int) {
	var event *EventTimetableTrain
	var station *MetroStation
	var direction string
	var arrival time.Time
	var departure time.Time
	var shift int
	var trainsOfLine []*MetroTrain
	linesWithTrains := linesWithTrains(trains)
	var currentTime time.Time
	var indexStationList int
	var nextStation *MetroStation
	var tripNumber int

	//get config parameters
	config := configs.GetInstance()

	startTime := config.TimeStart()
	stopTime := config.TimeEnd()
	timeInStation := config.TimeInStation()

	//algorithm to generate timetable by trains (sorted by lines)
	for i := 0; i < len(linesWithTrains); i++ {
		trainsOfLine = getTrainsPerLine(linesWithTrains[i], trains)
		for j := 0; j < len(trainsOfLine); j++ {
			if j%2 == 0 {
				//peer
				indexStationList = 0
				station = linesWithTrains[i].Stations()[indexStationList]
				direction = "up"
				shift = 0
			} else {
				//odd
				indexStationList = len(linesWithTrains[i].Stations()) - 1
				station = linesWithTrains[i].Stations()[indexStationList]
				direction = "down"
				shift = linesWithTrains[i].DurationOfLine(
					graphTimeBetweenStation, graphDelay) +
					timeInStation*len(linesWithTrains[i].Stations())
			}

			//add all events for this train
			currentTime = startTime.Add(time.Duration(shift) * time.Second)
			for currentTime.Before(stopTime) {
				arrival = currentTime
				currentTime = currentTime.Add(time.Duration(timeInStation) * time.Second)
				departure = currentTime
				//add time to next station
				indexStationList, direction = getNextStation(direction,
					linesWithTrains[i].Stations(), indexStationList)
				nextStation = linesWithTrains[i].Stations()[indexStationList]
				currentTime = currentTime.Add(time.Duration(
					graphTimeBetweenStation[station.Id()][nextStation.Id()]+
						graphDelay[station.Id()][nextStation.Id()]) *
					time.Second)
				//add the event to the timetable, as this timetable is
				//pre-generated isRevenue is set to false
				nextDepartureTime := currentTime.Add(
					time.Duration(timeInStation) * time.Second)
				metroStationTab := [2]MetroStation{*station, *nextStation}
				timeTab := [4]time.Time{arrival, currentTime, departure, nextDepartureTime}
				event = NewEventTimetableTrain(linesWithTrains[i],
					metroStationTab, trainsOfLine[j], direction, timeTab, false, tripNumber)
				timetable.addEventsTrain(event)

				//update current station to nextStation value
				station = nextStation
			}
		}
	}
}

/*// get the next events on the line
// use simulator.NextEventsTrain, this one is not functional
func (timetable *Timetable) NextEventsTrain(aTime time.Time) []*MetroTrain{
	var output []*MetroTrain

	// V1 : for each timetableStation, store the next event time
	var nextEventsTime []time.Time
	var lowestTime = time.Time{}

	for _, ts := range timetable.timetableStationList{
		for _, timeArr := range ts.arrivals{
			nextEventsTime = append(nextEventsTime, timeArr)
			if lowestTime.IsZero() || lowestTime.After(timeArr) {
				lowestTime = timeArr
			}
		}
	}

	for i, _ := range nextEventsTime {
		if nextEventsTime[i].Equal(lowestTime){
		//TODO am here
		// can't do anything here... it's not linked to an actual train :/
		}
	}

	// V2 : use trains only (pour retards etc)
	// TODO V2

	return output
}*/

/*
getNextStation return the next station and the direction of the train
depending on the current direction and the current station.

Param :
  - direction string : the current direction of the train
  - stationList []*MetroStation : the list of stations of the line
  - indexStationList int : the index of the current station in the list of
    stations

Return :
  - int : the index of the next station in the list of stations
  - string : the direction of the train
*/
func getNextStation(direction string, stationList []*MetroStation,
	indexStationList int) (int, string) {
	if direction == "up" && indexStationList < len(stationList)-1 {
		indexStationList++
	} else if direction == "up" && indexStationList == len(stationList)-1 {
		indexStationList--
		direction = "down"
	} else if direction == "down" && indexStationList > 0 {
		indexStationList--
	} else if direction == "down" && indexStationList == 0 {
		indexStationList++
		direction = "up"
	} else {
		log.Println("Error in the calcul of next station for timetable")
	}
	return indexStationList, direction
}

/*
Unused function
timeBetweenTrain return the time between 2 trains in a station, note that
is time between 2 trains going in the same direction
func timeBetweenTrain(lineTotalTime int, trainsPerLine int) int {
	if trainsPerLine == 0 {
		log.Println("timetableStation / timeBetweenTrain : " +
			"0 trains on line (divide by 0 dodged ! that was close)")
		return -1
	}
	return lineTotalTime / trainsPerLine
}
*/

/*
getTrainsPerLine return the list of trains of a given line.

Param :
  - line *MetroLine : the line
  - trains []*MetroTrain : the list of trains

Return :
  - []*MetroTrain : the list of trains of the given line
*/
func getTrainsPerLine(line *MetroLine, trains []*MetroTrain) []*MetroTrain {
	var listTrains []*MetroTrain
	for i := 0; i < len(trains); i++ {
		if trains[i].GetLine().id == line.id {
			listTrains = append(listTrains, trains[i])
		}
	}
	return listTrains
}

/*
linesWithTrains return the list of lines linked with trains.

Param :
  - trains []*MetroTrain : the list of trains

Return :
  - []*MetroLine : the list of lines linked with trains
*/
func linesWithTrains(trains []*MetroTrain) []*MetroLine {
	var linesWithTrains []*MetroLine
	for i := 0; i < len(trains); i++ {
		if !trains[i].GetLine().isInLineList(linesWithTrains) {
			linesWithTrains = append(linesWithTrains, trains[i].GetLine())
		}
	}
	return linesWithTrains
}

/*
isInLineList return true if the given line is present in the given list of
lines.

Param :
  - ml *MetroLine : the line
  - lineList []*MetroLine : the list of lines

Return :
  - bool : true if the given line is present in the given list of lines
*/
func (ml *MetroLine) isInLineList(lineList []*MetroLine) bool {
	sol := false
	for i := 0; i < len(lineList); i++ {
		if ml.Id() == lineList[i].Id() {
			sol = true
		}
	}
	return sol
}

/*
LineTimeLength return the duration of the going and coming for a train,
waiting time in the station for passengers to climb in is taken in account
numberStation must be >1 for a real total time of the line (not just the
duration of the line).

Param :
  - line *MetroLine : the line
  - graph [][]int : the graph of time between stations
  - graphDelay [][]int : the graph of delay between stations
  - numberStation int : the number of stations of the line
  - timeInStation int : the time in station

Return :
  - int : the duration of the going and coming for a train
*/
func LineTimeLength(line *MetroLine, graph, graphDelay [][]int,
	numberStation, timeInStation int) int {
	var totalTime int
	if numberStation < 1 {
		log.Fatal("not enough stations on the line to generate a timetable")
		return 0
	}
	totalTime = timeInStation*(2*numberStation-1) +
		2*line.DurationOfLine(graph, graphDelay)
	return totalTime
}

/*
ToCSV generate CSV file for the timetable.

Param :
  - timetable *Timetable : the timetable
*/
func (timetable *Timetable) ToCSV() {
	//TODO check time (>17s for
	var aux string

	var row = make([]string, 16)
	row[0] = "line name"
	row[1] = "train number"
	row[2] = "TRS number"
	row[3] = "trip number"
	row[4] = "is revenue"
	row[5] = "direction"
	row[6] = "source station"
	row[7] = "time arrived to the source station"
	row[8] = "time departed from the source station"
	row[9] = "destination station"
	row[10] = "time arrived to the destination station"
	row[11] = "time departed from the destination station"
	row[12] = "depot in/out" //not used in naia
	row[13] = "car number"   //not used in naia
	row[14] = "train trip"   //not used in naia
	row[15] = "train km"     //not used in naia
	csv := tools.NewFileWithDelimiter("timetable", row, ";")

	var currentEvent *EventTimetableTrain
	//for each timetableStation

	fmt.Println("number of events : ", len(timetable.getEventsTrain()))

	for i := 0; i < len(timetable.getEventsTrain()); i++ {
		currentEvent = timetable.getEventsTrain()[i]
		row[0] = currentEvent.Line().Name()
		row[1] = strconv.Itoa(currentEvent.Train().Id())
		aux = "000" + strconv.Itoa(currentEvent.Train().Id())
		row[2] = aux[len(aux)-3:]
		row[3] = "" //not ready
		row[4] = "" //not ready
		row[5] = currentEvent.Direction()
		row[6] = currentEvent.Station().Name()
		row[7] = currentEvent.Arrival().String()
		row[8] = currentEvent.Departure().String()
		row[9] = currentEvent.NextStation().Name()
		row[10] = currentEvent.NextArrival().String()
		row[11] = currentEvent.NextDeparture().String()
		row[12] = "" //not ready
		row[13] = "" //not ready
		row[14] = "" //not ready
		row[15] = "" //not ready
		csv.Write(row)
	}
}
