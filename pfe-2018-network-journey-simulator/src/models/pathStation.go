/*
Package models

File : pathStation.go

Brief : This file contains the PathStation struct and its methods.

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
PathStation is a struct that represent a path between two stations. It contains
the stations and the lines between the two stations.

Attributes :
  - stations []*MetroStation : the stations between the two stations
  - lines []*MetroLine : the lines between the two stations
  - minDuration int : the min duration of the path
  - maxDuration int : the max duration of the path

Methods :
  - PathStationCreate(line *MetroLine, stations ...*MetroStation) *PathStation
    : create a path between all stations
  - PathStationInverse(line *MetroLine, stations ...*MetroStation) *PathStation
    : create a path between all stations in reverse side
  - Lines() []*MetroLine : return the lines attribute
  - Stations() []*MetroStation : return the stations attribute
  - EndStation() *MetroStation : return the last station of the path
  - Inverse() : inverse the path
  - HasStation(station *MetroStation) bool : return true if the path contains
    the station, false otherwise
  - HaveSameLineAs(ps2 *PathStation) bool : return true if the path contains the
    same line as ps2, false otherwise
  - GetSegment(start *MetroStation, end *MetroStation) *PathStation : return a
    path between the two stations
  - String() string : return a string representation of the path
  - toString() string : return a string representation of the path
  - append(ps2 *PathStation) *PathStation : append a path to the other
  - TakeTrain(train *MetroTrain) bool : return true if the passenger should take
    this train, false otherwise
  - ExitTrain(train *MetroTrain) bool : return true if the passenger should exit
    this train, false otherwise
  - StartStation() *MetroStation : return the first station of the path
  - PositionStation(station MetroStation) int : return the position of the
    station in the path
  - Reroute(ps2 PathStation) *PathStation : reroute the path
*/
type PathStation struct {
	stations []*MetroStation
	//stations between A and B (including A and B)
	lines []*MetroLine
	//line between A and B (line to take to go to n+1 step)
	//ex : A->(1)->b->(1)->c->(2)->d->(3)->E
	//stations = [A,b,c,d,E]
	//lines = [1, 1, 2, 3]

	//these var will be useful when we will need to optimize
	//the transportation time
	minDuration int //min duration of trajectory between A and B (not implemented)
	maxDuration int //max duration of trajectory between A and B (not implemented)
}

//--- Constructors

/*
PathStationCreate creates a path between all stations. They should all use the
same line (use methods to add multi lines). The stations should be in the order
they are in the line.

Param :
  - line *MetroLine : the line used
  - stations ...*MetroStation : the stations used

Return :
  - *PathStation : the path created
*/
func PathStationCreate(line *MetroLine,
	stations ...*MetroStation) *PathStation {
	var out = PathStation{stations: stations}
	for i := 0; i < len(stations)-1; i++ {
		out.lines = append(out.lines, line)
	}
	return &out
}

/*
PathStationInverse creates a path between all stations in reverse side. They
should all use the same line (use methods to add multi lines). The stations
should be in the order they are in the line.

Param :
  - line *MetroLine : the line used
  - stations ...*MetroStation : the stations used

Return :
  - *PathStation : the path created
*/
func PathStationInverse(line *MetroLine,
	stations ...*MetroStation) *PathStation {
	var stations2 []*MetroStation
	for i := len(stations) - 1; i >= 0; i-- {
		stations2 = append(stations2, stations[i])
	}
	return PathStationCreate(line, stations2...)
}

//--- Getters & Setters

/*
Lines returns the lines attribute of the PathStation struct.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - []*MetroLine : the lines attribute of the PathStation struct
*/
func (ps *PathStation) Lines() []*MetroLine {
	return ps.lines
}

/*
Stations returns the stations attribute of the PathStation struct.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - []*MetroStation : the stations attribute of the PathStation struct
*/
func (ps *PathStation) Stations() []*MetroStation {
	return ps.stations
}

/*
EndStation returns the last station of the PathStation struct.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - *MetroStation : the last station of the PathStation struct
*/
func (ps *PathStation) EndStation() *MetroStation {
	if len(ps.stations) == 0 {
		return nil
	}
	return ps.stations[len(ps.stations)-1]
}

// --- Methods

/*
CheckValidity verify if the PathStation is not wrongly formed (as in, a station
appears twice).

Param :
  - ps *PathStation : the PathStation struct

Return :
  - bool : true if the PathStation is valid, false otherwise
*/
func (ps *PathStation) checkValidity() bool {
	for i := 0; i < len(ps.stations); i++ {
		for j := i + 1; j < len(ps.stations); j++ {
			if ps.stations[i].id == ps.stations[j].id {
				return false
			}
		}
	}

	return true
}

/*
Inverse the PathStation (eg A->B->C becomes C->B->A). The stations and lines
attributes are reversed. The minDuration and maxDuration attributes are not yet
reversed.
Deprecated : should not be used, try to find the correct pathStation
in Map object instead.

Param :
  - ps *PathStation : the PathStation struct
*/
func (ps *PathStation) Inverse() {
	var stations = ps.Stations()
	var stationsReversed []*MetroStation
	for i := len(stations) - 1; i >= 0; i-- {
		stationsReversed = append(stationsReversed, stations[i])
	}
	ps.stations = stationsReversed
}

/*
HasStation return true if the PathStation contains the station, false otherwise.

Param :
  - ps *PathStation : the PathStation struct
  - station *MetroStation : the station to check

Return :
  - bool : true if the PathStation contains the station, false otherwise
*/
func (ps *PathStation) HasStation(station *MetroStation) bool {
	for i := 0; i < len(ps.stations); i++ {
		if ps.stations[i].id == station.id {
			return true
		}
	}

	return false
}

/*
HaveSameLineAs compare the two PathStation to verify they don't have the same
line in them. Used to prevent errors while doing multi-lines path.

Param :
  - ps *PathStation : the PathStation struct
  - ps2 *PathStation : the PathStation struct

Return :
  - bool : true if the PathStation contains the station, false otherwise
*/
func (ps *PathStation) haveSameLineAs(ps2 *PathStation) bool {
	for i := 0; i < len(ps.lines); i++ {
		for j := 0; j < len(ps2.lines); j++ {
			if ps.lines[i].id == ps2.lines[j].id {
				return true
			}
		}
	}
	return false
}

/*
GetSegment return a PathStation between the two stations. The stations should
be in the PathStation. If the stations are not in the PathStation, the function
will return nil.

Param :
  - ps *PathStation : the PathStation struct
  - start *MetroStation : the start station
  - end *MetroStation : the end station

Return :
  - *PathStation : the PathStation between the two stations
*/
func (ps *PathStation) GetSegment(start *MetroStation,
	end *MetroStation) *PathStation {
	var output = PathStation{}
	for i, station := range ps.stations {
		if station.id == start.id && len(output.stations) == 0 {
			output.stations = append(output.stations, ps.stations[i])
		} else if len(output.stations) != 0 && station.id != end.id {
			output.stations = append(output.stations, ps.stations[i])
			output.lines = append(output.lines, ps.lines[i-1])
		} else if station.id == end.id {
			if len(output.stations) != 0 {
				output.stations = append(output.stations, ps.stations[i])
				output.lines = append(output.lines, ps.lines[i-1])
			}
			break
		}
	}

	return &output
}

/*
String return a string representation of the PathStation struct.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - string : the string representation of the PathStation struct
*/
func (ps *PathStation) String() string {
	var out = ""
	out += "stations=\t["
	for i := 0; i < len(ps.stations); i++ {
		out += ps.stations[i].name + ", "
	}
	out += "]\n"
	out += "lines=\t\t["
	for i := 0; i < len(ps.lines); i++ {
		out += ps.lines[i].name + ", "
	}
	out += "]"

	return out
}

/*
toString return a string representation of the PathStation struct. The string
representation is a list of the stations id.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - string : the string representation of the PathStation struct
*/
func (ps *PathStation) toString() string {
	var out = "("
	for i := 0; i < len(ps.stations); i++ {
		out += strconv.Itoa(ps.stations[i].id)
		if i != len(ps.stations)-1 {
			out += ", "
		}
	}
	return out + ")"
}

/*
append a PathStation to the other and return a third object (note: the two
objects won't be altered by this function). If ps.station[-1] == ps2.stations[0]
(ps.append(ps2)) then the station in double is removed.

Param :
  - ps *PathStation : the PathStation struct
  - ps2 *PathStation : the PathStation struct

Return :
  - *PathStation : the PathStation struct
*/
func (ps *PathStation) append(ps2 *PathStation) *PathStation {
	var output = PathStation{}
	//stations from ps1
	for i := 0; i < len(ps.stations); i++ {
		output.stations = append(output.stations, ps.stations[i])
	}
	for i := 0; i < len(ps.lines); i++ {
		output.lines = append(output.lines, ps.lines[i])
	}

	//stations from ps2
	if ps.stations[len(ps.stations)-1] == ps2.stations[0] {
		output.stations = append(output.stations, ps2.stations[1:]...)
	} else {
		output.stations = append(output.stations, ps2.stations...)
	}

	for i := 0; i < len(ps2.lines); i++ {
		output.lines = append(output.lines, ps2.lines[i])
	}

	//time
	output.minDuration = ps.minDuration + ps2.minDuration
	output.maxDuration = ps.maxDuration + ps2.maxDuration
	//TODO add delay in station

	return &output
}

/*
TakeTrain return if the passenger should take this train. The passenger should
be in the train.currentStation. The train should be in the same line as the
PathStation. The train should go to one of the stations of the PathStation.

Param :
  - ps *PathStation : the PathStation struct
  - train *MetroTrain : the train

Return :
  - bool : true if the passenger should take this train, false otherwise

TODO test
*/
func (ps *PathStation) TakeTrain(train *MetroTrain) bool {
	currentStation := train.currentStation
	index := ps.PositionStation(*currentStation)

	if index == -1 || index == len(ps.stations)-1 {
		//TODO raise error as it should not happen
		return false
	}

	//shortest path (eg same train as PathStation)
	if train.nextStation.id == ps.stations[index+1].id &&
		train.line.id == ps.lines[index].id {
		return true
	}

	//if the train go to one of the stations of the PathStation
	var takeTrain = false
	var stationsTrains = train.StationsBeforeReturn()
	for i := len(ps.stations) - 1; i > index; i-- {
		for j := range stationsTrains {
			if ps.stations[i].id == stationsTrains[j].id {
				takeTrain = true
			}
		}
	}

	return takeTrain
}

/*
ExitTrain return if the passenger should exit this train. The passenger should
be in the train.currentStation. The train should be in the same line as the
PathStation. The train should go to one of the stations of the PathStation.

Param :
  - ps *PathStation : the PathStation struct
  - train *MetroTrain : the train

Return :
  - bool : true if the passenger should exit this train, false otherwise

TODO test
*/
func (ps *PathStation) ExitTrain(train *MetroTrain) bool {
	index := ps.PositionStation(*train.currentStation)

	//verify if the train can take us closer to the end
	var exitTrain = true
	var stationsTrains = train.StationsBeforeReturn()
	for i := len(ps.stations) - 1; i > index; i-- {
		for j := range stationsTrains {
			if ps.stations[i].id == stationsTrains[j].id {
				exitTrain = false
			}
		}
	}

	if exitTrain == false {
		return false
	}

	if index == -1 {
		return false
	}
	// final station
	if index == len(ps.stations)-1 {
		return true
	}

	//verify that the

	//line change
	return ps.lines[index].id != train.line.id
}

/*
StartStation return the first station of the PathStation struct.

Param :
  - ps *PathStation : the PathStation struct

Return :
  - *MetroStation : the first station of the PathStation struct
*/
func (ps *PathStation) StartStation() *MetroStation {
	if len(ps.stations) == 0 {
		return nil
	}
	return ps.stations[0]
}

/*
PositionStation return the position of the station in the PathStation struct.
If the station is not in the PathStation, the function will return -1.

Param :
  - ps *PathStation : the PathStation struct
  - station MetroStation : the station

Return :
  - int : the position of the station in the PathStation struct
*/
func (ps *PathStation) PositionStation(station MetroStation) int {
	for i := range ps.stations {
		if ps.stations[i].id == station.id {
			return i
		}
	}
	return -1
}

/*
Reroute the pathStation, meaning that the start of ps2 will be added at the
similar station in ps.

Param :
  - ps *PathStation : the PathStation struct
  - ps2 *PathStation : the PathStation struct

Return :
  - *PathStation : the PathStation struct
*/
func (ps *PathStation) Reroute(ps2 PathStation) *PathStation {
	var pos = ps.PositionStation(*ps2.StartStation())
	if pos == -1 {
		return ps.append(&ps2)
	}
	return ps.GetSegment(ps.StartStation(), ps.stations[pos]).append(&ps2)
}
