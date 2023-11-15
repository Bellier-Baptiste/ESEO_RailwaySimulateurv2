/*
Package models

File : metroStation.go

Brief : This file contains the MetroStation struct and its methods.

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

/*
MetroStation is the structure that manage the metro stations.

Attributes :
  - id int : the id of the station
  - number int : the number of the station
  - code string : the code of the station
  - name string : the name of the station
  - position Point : the position of the station
  - metroLines []*MetroLine : the lines of the station
  - status string : the status of the station

Methods :
  - SetStatus(s string) : set the status of the station to the given string
  - StatusIsClosed() bool : return true if the status of the station is
    "closed", false otherwise
  - Id() int : return the id of the station
  - setId(id int) : set the id of the station
  - Number() int : return the number of the station
  - setNumber(number int) : set the number of the station
  - Code() string : return the code of the station
  - setCode(code string) : set the code of the station
  - Name() string : return the name of the station
  - Lines() []*MetroLine : return the lines of the station
  - setName(name string) : set the name of the station
  - Position() Point : return the position of the station
  - setPosition(position Point) : set the position of the station
  - AddMetroLine(line *MetroLine) : add the given line to the station
  - removeMetroLine(line *MetroLine) : remove the given line from the station
  - distanceTo(station2 MetroStation) float64 : return the distance between
    the two stations
  - hasDirectLineTo(station2 MetroStation) bool : return true if the two
    stations can go to each other using only one line, false otherwise
  - getDirectLinesTo(station2 MetroStation) []*MetroLine : return the lines
    that the two stations can use to go to each other
  - equals(station2 MetroStation) bool : return true if the two stations are
    equals, false otherwise
  - equalsNoRecurrence(station2 *MetroStation) bool : return true if the two
    stations are equals, false otherwise
*/
type MetroStation struct {
	id         int
	number     int
	code       string
	name       string
	position   Point
	metroLines []*MetroLine
	status     string
	idArea     int
}

/*
Unused function
func NewStation(id, number int, code, name string,
	position Point) MetroStation {
	var instance MetroStation
	instance.setId(id)
	instance.setNumber(number)
	instance.setCode(code)
	instance.setName(name)
	instance.setPosition(position)
	instance.status = "open"
	return instance
}
*/

/*
SetStatus sets the status of the station to the given string.

Param :
  - ms *MetroStation : the station to modify
  - s string : the new status of the station
*/
func (ms *MetroStation) SetStatus(s string) {
	ms.status = s
}

/*
StatusIsClosed returns true if the status of the station is "closed", false
otherwise.

Param :
  - ms *MetroStation : the station to check

Return :
  - bool : true if the status of the station is "closed", false otherwise
*/
func (ms *MetroStation) StatusIsClosed() bool {
	return ms.status == "closed"
}

/*
Id returns the id of the station.

Param :
  - ms *MetroStation : the station

Return :
  - int : the id of the station
*/
func (ms *MetroStation) Id() int {
	return ms.id
}

/*
setId sets the id of the station.

Param :
  - ms *MetroStation : the station
  - id int : the new id of the station
*/
func (ms *MetroStation) setId(id int) {
	ms.id = id
}

/*
Number returns the number of the station.

Param :
  - ms *MetroStation : the station

Return :
  - int : the number of the station
*/
func (ms *MetroStation) Number() int {
	return ms.number
}

/*
setNumber sets the number of the station.

Param :
  - ms *MetroStation : the station
  - number int : the new number of the station
*/
func (ms *MetroStation) setNumber(number int) {
	ms.number = number
}

/*
Code returns the code of the station.

Param :
  - ms *MetroStation : the station

Return :
  - string : the code of the station
*/
func (ms *MetroStation) Code() string {
	return ms.code
}

/*
setCode sets the code of the station.

Param :
  - ms *MetroStation : the station
  - code string : the new code of the station
*/
func (ms *MetroStation) setCode(code string) {
	ms.code = code
}

/*
Name returns the name of the station.

Param :
  - ms *MetroStation : the station

Return :
  - string : the name of the station
*/
func (ms *MetroStation) Name() string {
	return ms.name
}

/*
Lines returns the lines of the station.

Param :
  - ms *MetroStation : the station

Return :
  - []*MetroLine : the lines of the station
*/
func (ms *MetroStation) Lines() []*MetroLine {
	return ms.metroLines
}

/*
setName sets the name of the station.

Param :
  - ms *MetroStation : the station
  - name string : the new name of the station
*/
func (ms *MetroStation) setName(name string) {
	ms.name = name
}

/*
Position returns the position of the station.

Param :
  - ms *MetroStation : the station

Return :
  - Point : the position of the station
*/
func (ms *MetroStation) Position() Point {
	return ms.position
}

/*
setPosition sets the position of the station.

Param :
  - ms *MetroStation : the station
  - position Point : the new position of the station
*/
func (ms *MetroStation) setPosition(position Point) {
	ms.position = position
}

/*
IdArea returns the id of the area of the station.
*/
func (ms *MetroStation) IdArea() int {
	return ms.idArea
}

/*
AddMetroLine adds the given line to the station.

Param :
  - ms *MetroStation : the station
  - line *MetroLine : the line to add to the station
*/
func (ms *MetroStation) AddMetroLine(line *MetroLine) {
	//verify if the line is in the station
	for i := 0; i < len(ms.metroLines); i++ {
		if ms.metroLines[i] == line {
			return
		}
	}

	ms.metroLines = append(ms.metroLines, line)
}

/*
RemoveMetroLine removes the given line from the station.

Param :
  - ms *MetroStation : the station
  - line *MetroLine : the line to remove from the station
*/
func (ms *MetroStation) removeMetroLine(line *MetroLine) {
	var index = -1
	//verify if the line is in the ms
	for i := 0; i < len(ms.metroLines); i++ {
		if ms.metroLines[i] == line {
			index = i
			break
		}
	}

	if index == -1 {
		return
	}

	ms.metroLines = append(ms.metroLines[:index], ms.metroLines[index+1:]...)

	//add the ms to the line
	line.removeMetroStation(ms)
}

/*
distanceTo returns the distance between the two stations.

Param :
  - ms *MetroStation : the station
  - station2 MetroStation : the other station

Return :
  - float64 : the distance between the two stations
*/
func (ms *MetroStation) distanceTo(station2 MetroStation) float64 {
	//TODO : calculate distance using the lines
	return ms.position.DistanceTo(station2.position)
}

/*
hasDirectLineTo returns true if the two stations can go to each other using only
one line, false otherwise.

Param :
  - ms *MetroStation : the station
  - station2 MetroStation : the other station

Return :
  - bool : true if the two stations can go to each other using only one line,
    false otherwise
*/
func (ms *MetroStation) hasDirectLineTo(station2 MetroStation) bool {
	//TODO test
	for i := 0; i < len(ms.metroLines); i++ {
		for j := 0; j < len(station2.metroLines); j++ {
			if ms.metroLines[i] == station2.metroLines[j] {
				return true
			}
		}
	}

	return false
}

/*
getDirectLinesTo returns the lines that the two stations can use to go to each
other.

Param :
  - ms *MetroStation : the station
  - station2 MetroStation : the other station

Return :
  - []*MetroLine : the lines that the two stations can use to go to each
    other
*/
func (ms *MetroStation) getDirectLinesTo(station2 MetroStation) []*MetroLine {
	//TODO test
	var output []*MetroLine

	for i := 0; i < len(ms.metroLines); i++ {
		for j := 0; j < len(station2.metroLines); j++ {
			if ms.metroLines[i] == station2.metroLines[j] {
				output = append(output, ms.metroLines[i])
			}
		}
	}

	return output
}

/*
equals returns true if the two stations are equals, false otherwise.

Param :
  - ms *MetroStation : the station
  - station2 MetroStation : the other station

Return :
  - bool : true if the two stations are equals, false otherwise
*/
func (ms *MetroStation) equals(station2 MetroStation) bool {
	if !ms.equalsNoRecurrence(&station2) {
		return false
	}

	//check the MetroLines
	for i := 0; i <= len(ms.metroLines); i++ {
		if !ms.metroLines[i].equalsNoRecurrence(station2.metroLines[i]) {
			return false
		}
	}

	return true
}

/*
equalsNoRecurrence returns true if the two stations are equals, false otherwise.

Param :
  - ms *MetroStation : the station
  - station2 MetroStation : the other station

Return :
  - bool : true if the two stations are equals, false otherwise
*/
func (ms *MetroStation) equalsNoRecurrence(station2 *MetroStation) bool {
	return station2 != nil &&
		ms.id == station2.id &&
		ms.name == station2.name &&
		ms.position.equals(station2.position) &&
		len(ms.metroLines) == len(station2.metroLines)
}
