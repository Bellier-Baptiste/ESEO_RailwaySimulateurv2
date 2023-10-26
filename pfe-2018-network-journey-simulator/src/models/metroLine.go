/*
Package models

File : metroLine.go

Brief :

Date : N/A

Author : Team v1, Team v2, Paul TRÉMOUREUX (quality check)

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

import "strconv"

type MetroLine struct {
	id                    int
	number                int
	code, name            string
	stations              []*MetroStation
	trainNumber           int
	passengersMaxPerTrain int
}

/*
Unused function
func NewLine(id, number, trainNumber, passengersMaxPerTrain int,
	code, name string) MetroLine {
	var instance MetroLine
	instance.setName(name)
	instance.setId(id)
	instance.setNumber(number)
	instance.setCode(code)
	instance.setTrainNumber(trainNumber)
	instance.setPassengersMaxPerTrain(passengersMaxPerTrain)
	return instance
}
*/

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

func (ml *MetroLine) AddMetroStation(station *MetroStation) {
	//verify if the line is in the station
	for i := 0; i < len(ml.stations); i++ {
		if ml.stations[i] == station {
			return
		}
	}
	ml.stations = append(ml.stations, station)
}

//TODO: getStationNumber

func (ml *MetroLine) removeMetroStation(station *MetroStation) {
	var index = -1
	//verify if the line is in the station
	for i := 0; i < len(ml.stations); i++ {
		if ml.stations[i] == station {
			index = i
			break
		}
	}

	if index == -1 {
		return
	}

	ml.stations = append(ml.stations[:index], ml.stations[index+1:]...)

	//add the station to the line
	station.removeMetroLine(ml)
}

/*
isReverseOrder

Param :
i int
reverseOrder bool
output []*MetroStation

Return :
[]*MetroStation
*/
func (ml *MetroLine) isReverseOrder(i int, reverseOrder bool,
	output []*MetroStation) []*MetroStation {
	if reverseOrder {
		output = append([]*MetroStation{ml.stations[i]}, output...)
	} else {
		output = append(output, ml.stations[i])
	}
	return output
}

func (ml *MetroLine) GetPath(station1,
	station2 *MetroStation) []*MetroStation {
	var output []*MetroStation
	var foundOne = false
	var reverseOrder = false
	for i := 0; i < len(ml.stations); i++ {
		if ml.stations[i] == station1 || ml.stations[i] == station2 {
			//toggle the foundOne switch
			foundOne = !foundOne

			//if the first match is the second station
			//-> we order the order to be reversed
			if ml.stations[i] == station2 && foundOne {
				reverseOrder = true
			}

			output = ml.isReverseOrder(i, reverseOrder, output)

			//if we had already found one station before finding this one
			//=> finish the work
			if !foundOne {
				break
			}
		} else if foundOne {

			output = ml.isReverseOrder(i, reverseOrder, output)
		} else {
			//println(line.stations[i], &station1, &station2)
		}
	}

	//verify if we have found the start AND the end
	if len(output) == 0 || foundOne == true {
		//len(output) == 0 => found no one |||| foundOne == true => found only one
		println("didn't find both stations", len(output), foundOne)
		return nil
	}

	return output
}

func (ml *MetroLine) Equals(line2 MetroLine) bool {
	if !ml.equalsNoRecurrence(&line2) {
		return false
	}

	for i := 0; i < len(ml.stations); i++ {
		if !ml.stations[i].equalsNoRecurrence(line2.stations[i]) {
			return false
		}
	}

	return true
}

func (ml *MetroLine) equalsNoRecurrence(line2 *MetroLine) bool {
	return line2 != nil &&
		ml.id == line2.id &&
		ml.number == line2.number &&
		ml.name == line2.name &&
		len(ml.stations) == len(line2.stations) &&
		ml.trainNumber == line2.trainNumber &&
		ml.passengersMaxPerTrain == line2.passengersMaxPerTrain
}

/*
PositionInLine
return n+1 /!\
*/
func (ml *MetroLine) PositionInLine(station *MetroStation) int {
	var position = -1
	for i := 0; i < len(ml.stations); i++ {
		if ml.stations[i].id == station.id {
			position = i
		}
	}
	return position
}

func (ml *MetroLine) String() string {
	var out = "[MetroLine"

	out += " id:" + strconv.Itoa(ml.id)
	out += " name:" + ml.name
	out += " stations:["
	for _, s := range ml.stations {
		out += " " + s.name
	}
	out += "]]"

	return out
}

/*
DurationOfLine sum of all timeBetweenStation of the line
*/
func (ml *MetroLine) DurationOfLine(graph [][]int, graphDelay [][]int) int {
	var sum int
	sum = 0
	if len(graph) != len(graphDelay) {
		println("Error in MetroLine.DurationOfLine : " +
			"graphTimeBetweenStation and graphDelay doesn't match")
		println("size of graphTimeBetweenStation : ", len(graph))
		println("size of graphDelay : ", len(graphDelay))
		println("number of stations : ", len(ml.stations))
	}
	for i := 0; i < len(ml.Stations())-1; i++ {
		sum += graph[ml.Stations()[i].Id()][ml.Stations()[i+1].Id()] +
			graphDelay[ml.Stations()[i].Id()][ml.Stations()[i+1].Id()]
	}
	return sum
}
