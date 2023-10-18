/*
Package models

File : metroStation.go

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

type MetroStation struct {
	id         int
	number     int
	code       string
	name       string
	position   Point
	metroLines []*MetroLine

	status string
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

func (ms *MetroStation) SetStatus(s string) {
	ms.status = s
}

func (ms *MetroStation) StatusIsClosed() bool {
	return ms.status == "closed"
}

func (ms *MetroStation) Id() int {
	return ms.id
}

func (ms *MetroStation) setId(id int) {
	ms.id = id
}

func (ms *MetroStation) Number() int {
	return ms.number
}

func (ms *MetroStation) setNumber(number int) {
	ms.number = number
}

func (ms *MetroStation) Code() string {
	return ms.code
}

func (ms *MetroStation) setCode(code string) {
	ms.code = code
}

func (ms *MetroStation) Name() string {
	return ms.name
}

func (ms *MetroStation) Lines() []*MetroLine {
	return ms.metroLines
}

func (ms *MetroStation) setName(name string) {
	ms.name = name
}

func (ms *MetroStation) Position() Point {
	return ms.position
}

func (ms *MetroStation) setPosition(position Point) {
	ms.position = position
}

func (ms *MetroStation) AddMetroLine(line *MetroLine) {
	//verify if the line is in the station
	for i := 0; i < len(ms.metroLines); i++ {
		if ms.metroLines[i] == line {
			return
		}
	}

	ms.metroLines = append(ms.metroLines, line)
}

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

func (ms *MetroStation) distanceTo(station2 MetroStation) float64 {
	//TODO : calculate distance using the lines
	return ms.position.DistanceTo(station2.position)
}

// verify if the two station can go to each other using only one line
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

func (ms *MetroStation) equalsNoRecurrence(station2 *MetroStation) bool {
	return station2 != nil &&
		ms.id == station2.id &&
		ms.name == station2.name &&
		ms.position.equals(station2.position) &&
		len(ms.metroLines) == len(station2.metroLines)
}
