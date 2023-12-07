/*
Package models

File : metroLine.go

Brief : This file contains the MetroLine struct and its methods.

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

import "strconv"

/*
MetroLine is the structure that manage the metro lines.

Attributes :
  - id int : the id of the line
  - number int : the number of the line
  - code string : the code of the line
  - name string : the name of the line
  - stations []*MetroStation : the stations of the line
  - trainNumber int : the number of train of the line
  - passengersMaxPerTrain int : the number of passengers max per train of
    the line

Methods :
  - PassengersMaxPerTrain() int : return the number of passengers max per
    train of the line
  - setPassengersMaxPerTrain(passengersMaxPerTrain int) : set the number of
    passengers max per train of the line
  - TrainNumber() int : return the number of train of the line
  - setTrainNumber(trainNumber int) : set the number of train of the line
  - Code() string : return the code of the line
  - Stations() []*MetroStation : return the stations of the line
  - setCode(code string) : set the code of the line
  - Number() int : return the number of the line
  - setNumber(number int) : set the number of the line
  - Id() int : return the id of the line
  - setId(id int) : set the id of the line
  - Name() string : return the name of the line
  - setName(name string) : set the name of the line
  - AddMetroStation(station *MetroStation) : add a station to the line
  - removeMetroStation(station *MetroStation) : remove a station to the line
  - GetPath(station1, station2 *MetroStation) []*MetroStation : return the
    path between two stations of the line
  - isReverseOrder(i int, reverseOrder bool, output []*MetroStation)
    []*MetroStation : return the path between two stations of the line
  - Equals(line2 MetroLine) bool : return true if the line is equal to
    line2, false otherwise
  - equalsNoRecurrence(line2 *MetroLine) bool : return true if the line is
    equal to line2, false otherwise
  - PositionInLine(station *MetroStation) int : return the position of the
    station in the line
  - String() string : return the string representation of the line
  - DurationOfLine(graph [][]int, graphDelay [][]int) int : return the
    duration of the line
*/
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

/*
PassengersMaxPerTrain return the number of passengers max per train of the
line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - int : the number of passengers max per train of the line
*/
func (ml *MetroLine) PassengersMaxPerTrain() int {
	return ml.passengersMaxPerTrain
}

/*
setPassengersMaxPerTrain set the number of passengers max per train of the
line.

Param :
  - ml *MetroLine : the MetroLine struct
  - passengersMaxPerTrain int : the number of passengers max per train
    of the line
*/
func (ml *MetroLine) setPassengersMaxPerTrain(passengersMaxPerTrain int) {
	ml.passengersMaxPerTrain = passengersMaxPerTrain
}

/*
TrainNumber return the number of train of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - int : the number of train of the line
*/
func (ml *MetroLine) TrainNumber() int {
	return ml.trainNumber
}

/*
setTrainNumber set the number of train of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - trainNumber int : the number of train of the line
*/
func (ml *MetroLine) setTrainNumber(trainNumber int) {
	ml.trainNumber = trainNumber
}

/*
Code return the code of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - string : the code of the line
*/
func (ml *MetroLine) Code() string {
	return ml.code
}

/*
Stations return the stations of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - []*MetroStation : the stations of the line
*/
func (ml *MetroLine) Stations() []*MetroStation {
	return ml.stations
}

/*
setCode set the code of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - code string : the code of the line
*/
func (ml *MetroLine) setCode(code string) {
	ml.code = code
}

/*
Number return the number of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - int : the number of the line
*/
func (ml *MetroLine) Number() int {
	return ml.number
}

/*
setNumber set the number of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - number int : the number of the line
*/
func (ml *MetroLine) setNumber(number int) {
	ml.number = number
}

/*
Id return the id of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - int : the id of the line
*/
func (ml *MetroLine) Id() int {
	return ml.id
}

/*
setId set the id of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - id int : the id of the line
*/
func (ml *MetroLine) setId(id int) {
	ml.id = id
}

/*
Name return the name of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - string : the name of the line
*/
func (ml *MetroLine) Name() string {
	return ml.name
}

/*
setName set the name of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - name string : the name of the line
*/
func (ml *MetroLine) setName(name string) {
	ml.name = name
}

/*
AddMetroStation add a station to the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - station *MetroStation : the station to add to the line
*/
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

/*
removeMetroStation remove a station to the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - station *MetroStation : the station to remove to the line
*/
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
isReverseOrder return the path between two stations of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - i int : the index of the station
  - reverseOrder bool : true if the order is reversed, false otherwise
  - output []*MetroStation : the output

Return :
  - []*MetroStation : the path between two stations of the line
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

/*
GetPath return the path between two stations of the line.

Param :
  - ml *MetroLine : the MetroLine struct
  - station1 *MetroStation : the first station
  - station2 *MetroStation : the second station

Return :
  - []*MetroStation : the path between two stations of the line
*/
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

/*
Equals return true if the line is equal to line2, false otherwise.

Param :
  - ml *MetroLine : the MetroLine struct
  - line2 MetroLine : the line to compare with

Return :
  - bool : true if the line is equal to line2, false otherwise
*/
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

/*
equalsNoRecurrence return true if the line is equal to line2, false otherwise.

Param :
  - ml *MetroLine : the MetroLine struct
  - line2 *MetroLine : the line to compare with

Return :
  - bool : true if the line is equal to line2, false otherwise
*/
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
PositionInLine return the position of the station in the line.
return n+1 /!\

Param :
  - ml *MetroLine : the MetroLine struct
  - station *MetroStation : the station

Return :
  - int : the position of the station in the line
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

/*
String return the string representation of the line.

Param :
  - ml *MetroLine : the MetroLine struct

Return :
  - string : the string representation of the line
*/
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
DurationOfLine sum of all timeBetweenStation of the line (in minutes).

Param :
  - ml *MetroLine : the MetroLine struct
  - graph [][]int : the graph of the metro
  - graphDelay [][]int : the graph of the metro with the delay

Return :
  - int : the duration of the line
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
