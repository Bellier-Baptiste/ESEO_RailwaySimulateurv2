package models

import (
	"strconv"
)

type MetroLine struct {
	id                    int
	number                int
	code, name            string
	stations              []*MetroStation
	trainNumber           int
	passengersMaxPerTrain int
}

func NewLine(id int, number int, code string, name string, trainNumber int, passengersMaxPerTrain int) MetroLine {
	var instance MetroLine
	instance.setName(name)
	instance.setId(id)
	instance.setNumber(number)
	instance.setCode(code)
	instance.setTrainNumber(trainNumber)
	instance.setPassengersMaxPerTrain(passengersMaxPerTrain)
	return instance
}

func (ml *MetroLine) PassengersMaxPerTrain() int {
	return ml.passengersMaxPerTrain
}

func (ml *MetroLine) setPassengersMaxPerTrain(passengersMaxPerTrain int) {
	ml.passengersMaxPerTrain = passengersMaxPerTrain
}

func (ml *MetroLine) TrainNumber() int {
	return ml.trainNumber
}

func (ml *MetroLine) setTrainNumber(trainNumber int) {
	ml.trainNumber = trainNumber
}

func (ml *MetroLine) Code() string {
	return ml.code
}

func (ml *MetroLine) Stations() []*MetroStation {
	return ml.stations
}

func (ml *MetroLine) setCode(code string) {
	ml.code = code
}

func (ml *MetroLine) Number() int {
	return ml.number
}

func (ml *MetroLine) setNumber(number int) {
	ml.number = number
}

func (ml *MetroLine) Id() int {
	return ml.id
}

func (ml *MetroLine) setId(id int) {
	ml.id = id
}

func (ml *MetroLine) Name() string {
	return ml.name
}

func (ml *MetroLine) setName(name string) {
	ml.name = name
}

func (line *MetroLine) AddMetroStation(station *MetroStation) {
	//verify if the line is in the station
	for i := 0; i < len(line.stations); i++ {
		if line.stations[i] == station {
			return
		}
	}
	line.stations = append(line.stations, station)
}

//TODO: getStationNumber

func (line *MetroLine) removeMetroStation(station *MetroStation) {
	var index = -1
	//verify if the line is in the station
	for i := 0; i < len(line.stations); i++ {
		if line.stations[i] == station {
			index = i
			break
		}
	}

	if index == -1 {
		return
	}

	line.stations = append(line.stations[:index], line.stations[index+1:]...)

	//add the station to the line
	station.removeMetroLine(line)
}

func (line *MetroLine) GetPath(station1 *MetroStation, station2 *MetroStation) []*MetroStation {
	var output = []*MetroStation{}
	var foundOne = false
	var reverseOrder = false
	for i := 0; i < len(line.stations); i++ {
		if line.stations[i] == station1 || line.stations[i] == station2 {
			//toggle the foundOne switch
			foundOne = !foundOne

			//if the first match is the second station -> we order the order to be reversed
			if line.stations[i] == station2 && foundOne {
				reverseOrder = true
			}

			if reverseOrder {
				output = append([]*MetroStation{line.stations[i]}, output...)
			} else {
				output = append(output, line.stations[i])
			}

			//if we had already found one station before finding this one => finish the work
			if !foundOne {
				break
			}
		} else if foundOne {

			if reverseOrder {
				output = append([]*MetroStation{line.stations[i]}, output...)
			} else {
				output = append(output, line.stations[i])
			}
		} else {
			//println(line.stations[i], &station1, &station2)
		}
	}

	//verify if we have found the start AND the end
	if len(output) == 0 || foundOne == true { //len(output) == 0 => found no one |||| foundOne == true => found only one
		println("didn't find both stations", len(output), foundOne)
		return nil
	}

	return output
}

func (line *MetroLine) Equals(line2 MetroLine) bool {
	if !line.equalsNoRecurrence(&line2) {
		return false
	}

	for i := 0; i < len(line.stations); i++ {
		if !line.stations[i].equalsNoRecurrence(line2.stations[i]) {
			return false
		}
	}

	return true
}

func (line *MetroLine) equalsNoRecurrence(line2 *MetroLine) bool {
	return line2 != nil &&
		line.id == line2.id &&
		line.number == line2.number &&
		line.name == line2.name &&
		len(line.stations) == len(line2.stations) &&
		line.trainNumber == line2.trainNumber &&
		line.passengersMaxPerTrain == line2.passengersMaxPerTrain
}

//return n+1 /!\
func (line *MetroLine) PositionInLine(station *MetroStation) int {
	var position = -1
	for i := 0; i < len(line.stations); i++ {
		if line.stations[i].id == station.id {
			position = i
		}
	}
	return position
}

func (line MetroLine) String() string {
	var out = "[MetroLine"

	out += " id:" + strconv.Itoa(line.id)
	out += " name:" + line.name
	out += " stations:["
	for _, s := range line.stations {
		out += " " + s.name
	}
	out += "]]"

	return out
}

//sum of all timeBetweenStation of the line
func (line MetroLine) DurationOfLine(graph [][]int, graphDelay [][]int) int {
	var sum int
	sum = 0
	if len(graph) != len(graphDelay) {
		println("Error in MetroLine.DurationOfLine : graphTimeBetweenStation and graphDelay doesn't match")
		println("size of graphTimeBetweenStation : ", len(graph))
		println("size of graphDelay : ", len(graphDelay))
		println("number of stations : ", len(line.stations))
	}
	for i := 0; i < len(line.Stations())-1; i++ {
		sum += graph[line.Stations()[i].Id()][line.Stations()[i+1].Id()] + graphDelay[line.Stations()[i].Id()][line.Stations()[i+1].Id()]
	}
	return sum
}
