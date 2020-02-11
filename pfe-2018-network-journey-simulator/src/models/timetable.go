package models

import (
	"fmt"
	"log"
	"configs"
	"tools"
	"strconv"
	"time"
)

type Timetable struct {
	eventsTrain []*EventTimetableTrain
}

func (timetable *Timetable) getEventsTrain() []*EventTimetableTrain {
	return timetable.eventsTrain
}

func (timetable *Timetable) addEventsTrain(event *EventTimetableTrain) {
	timetable.eventsTrain = append(timetable.eventsTrain, event)
}

//--- Constructor
func NewTimetable(m *Map, trains []*MetroTrain) Timetable {
	timetable := Timetable{}
	timetable.GenerateTimetable(m, trains, m.GraphTimeBetweenStation(), m.GraphDelay()) //TODO pass this function directly here
	return timetable
}

func (timetable *Timetable) GenerateTimetable(mapPointer *Map, trains []*MetroTrain, graphTimeBetweenStation [][]int, graphDelay [][]int) {
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
				shift = linesWithTrains[i].DurationOfLine(graphTimeBetweenStation, graphDelay) + timeInStation*len(linesWithTrains[i].Stations())
			}

			//add all events for this train
			currentTime = startTime.Add(time.Duration(shift) * time.Second)
			for currentTime.Before(stopTime) {
				arrival = currentTime
				currentTime = currentTime.Add(time.Duration(timeInStation) * time.Second)
				departure = currentTime
				//add time to next station
				indexStationList, direction = getNextStation(direction, linesWithTrains[i].Stations(), station, indexStationList)
				nextStation = linesWithTrains[i].Stations()[indexStationList]
				currentTime = currentTime.Add(time.Duration(graphTimeBetweenStation[station.Id()][nextStation.Id()]+graphDelay[station.Id()][nextStation.Id()]) * time.Second)
				//add the event to the timetable, as this timetable is pre-generated isRevenue is set to false
				event = NewEventTimetableTrain(linesWithTrains[i], station, nextStation, trainsOfLine[j], direction, arrival, currentTime, departure, currentTime.Add(time.Duration(timeInStation)*time.Second), false, tripNumber)
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

func getNextStation(direction string, stationList []*MetroStation, actualStation *MetroStation, indexStationList int) (int, string) {
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

//return the time between 2 trains in a station, note that is time between 2 trains going in the same direction
func timeBetweenTrain(lineTotalTime int, trainsPerLine int) int {
	if trainsPerLine == 0 {
		log.Println("timetableStation / timeBetweenTrain : 0 trains on line (divide by 0 dodged ! that was close)")
		return -1
	}
	return lineTotalTime / trainsPerLine
}

//get all trains of a given line
func getTrainsPerLine(line *MetroLine, trains []*MetroTrain) []*MetroTrain {
	var listTrains []*MetroTrain
	for i := 0; i < len(trains); i++ {
		if trains[i].GetLine().id == line.id {
			listTrains = append(listTrains, trains[i])
		}
	}
	return listTrains
}

//from a list of trains, return all the lines linked with trains
func linesWithTrains(trains []*MetroTrain) []*MetroLine {
	var linesWithTrains []*MetroLine
	for i := 0; i < len(trains); i++ {
		if !trains[i].GetLine().isInLineList(linesWithTrains) {
			linesWithTrains = append(linesWithTrains, trains[i].GetLine())
		}
	}
	return linesWithTrains
}

//search by id if the given line is present in a list of lines
func (line *MetroLine) isInLineList(lineList []*MetroLine) bool {
	sol := false
	for i := 0; i < len(lineList); i++ {
		if line.Id() == lineList[i].Id() {
			sol = true
		}
	}
	return sol
}

//return the duration of the going and coming for a train, waiting time in the station for passengers to climb in is taken in account
//numberStation must be >1 for a real total time of the line
func LineTimeLength(line *MetroLine, graph [][]int, graphDelay [][]int, numberStation int, timeInStation int) int {
	var totalTime int
	if numberStation < 1 {
		log.Fatal("not enough stations on the line to generate a timetable")
		return 0
	}
	totalTime = timeInStation*(2*numberStation-1) + 2*line.DurationOfLine(graph, graphDelay)
	return totalTime
}

//generate CSV file
func (timetable Timetable) ToCSV() {//TODO check time (>17s for
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
