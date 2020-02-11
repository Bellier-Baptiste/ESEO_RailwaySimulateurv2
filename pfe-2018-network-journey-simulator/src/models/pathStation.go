package models

import "strconv"

type pathStation struct {
	stations []*MetroStation //stations between A and B (including A and B)
	lines    []*MetroLine    //line between A and B (line to take to go to n+1 step)
	//ex : A->(1)->b->(1)->c->(2)->d->(3)->E
	//stations = [A,b,c,d,E]
	//lines = [1, 1, 2, 3]

	//theses var will be useful when we will need to optimize the transportation time
	minDuration int //min duration of traject between A and B (not implemented)
	maxDuration int //max duration of traject between A and B (not implemented)
}

//--- Constructors

//creates a path between all stations. they should all use the same line (use methods to add multi lignes)
func PathStation(line *MetroLine, stations ...*MetroStation) *pathStation {
	var out = pathStation{stations: stations}
	for i := 0; i < len(stations)-1; i++ {
		out.lines = append(out.lines, line)
	}
	return &out
}

func PathStationInverse(line *MetroLine, stations ...*MetroStation) *pathStation {
	var stations2 = []*MetroStation{}
	for i := len(stations) - 1; i >= 0; i-- {
		stations2 = append(stations2, stations[i])
	}
	return PathStation(line, stations2...)
}

//--- Getters & Setters

func (p pathStation) Lines() []*MetroLine {
	return p.lines
}

func (p pathStation) Stations() []*MetroStation {
	return p.stations
}

func (ps pathStation) EndStation() *MetroStation {
	if len(ps.stations) == 0 {
		return nil
	}
	return ps.stations[len(ps.stations)-1]
}

//--- Methods
//verify if the pathStation is not wrongly formed (as in, a station appears twice)
func (ps *pathStation) checkValidity() bool {
	for i := 0; i < len(ps.stations); i++ {
		for j := i + 1; j < len(ps.stations); j++ {
			if ps.stations[i].id == ps.stations[j].id {
				return false
			}
		}
	}

	return true
}

// Deprecated : should not be used, try to find the correct pathStation in Map object instead
func (ps *pathStation) Inverse() {
	var stations = ps.Stations()
	var stationsReversed = []*MetroStation{}
	for i := len(stations) - 1; i >= 0; i-- {
		stationsReversed = append(stationsReversed, stations[i])
	}
	ps.stations = stationsReversed;
}

func (ps pathStation) HasStation(station *MetroStation) bool {
	for i := 0; i < len(ps.stations); i++ {
		if ps.stations[i].id == station.id {
			return true
		}
	}

	return false
}

//compare the two pathStation to verify they don't have the same line in them. used to prevent errors while doing multi-lines path
func (ps *pathStation) haveSameLineAs(ps2 *pathStation) bool {
	for i := 0; i < len(ps.lines); i++ {
		for j := 0; j < len(ps2.lines); j++ {
			if ps.lines[i].id == ps2.lines[j].id {
				return true
			}
		}
	}
	return false
}

func (ps *pathStation) GetSegment(start *MetroStation, end *MetroStation) *pathStation {
	var output = pathStation{}
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

func (ps pathStation) String() string {
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

func (ps pathStation) toString() string {
	var out = "("
	for i := 0; i < len(ps.stations); i++ {
		out += strconv.Itoa(ps.stations[i].id)
		if i != len(ps.stations)-1 {
			out += ", "
		}
	}
	return out + ")"
}

// append a pathStation to the other and return a third object (note: the two objects won't be altered by this function)
// if ps.station[-1] == ps2.stations[0] (ps.append(ps2)) then the station in double is removed
func (ps pathStation) append(ps2 *pathStation) *pathStation {
	var output = pathStation{}
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
	output.maxDuration = ps.maxDuration + ps2.maxDuration //TODO add delay in station

	return &output
}

//TODO test
// return if the passenger should take this train. the passenger should be in the train.currentStation
func (ps pathStation) TakeTrain(train *MetroTrain) bool {
	currentStation := train.currentStation
	index := ps.PositionStation(*currentStation)

	if index == -1 || index == len(ps.stations)-1 { //TODO raise error as it should not happen
		return false
	}

	//shortest path (eg same train as PathStation)
	if train.nextStation.id == ps.stations[index+1].id && train.line.id == ps.lines[index].id {
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

//TODO test
func (ps pathStation) ExitTrain(train *MetroTrain) bool {
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

func (ps pathStation) StartStation() *MetroStation {
	if len(ps.stations) == 0 {
		return nil
	}
	return ps.stations[0]
}

func (ps pathStation) PositionStation(station MetroStation) int {
	for i := range ps.stations {
		if ps.stations[i].id == station.id {
			return i
		}
	}
	return -1
}

//reroute the pathStation, meaning that the start of ps2 will be added at the similar station in ps
func (ps *pathStation) Reroute(ps2 pathStation) *pathStation {
	var pos = ps.PositionStation(*ps2.StartStation())
	if pos == -1 {
		return ps.append(&ps2)
	}
	return ps.GetSegment(ps.StartStation(), ps.stations[pos]).append(&ps2)
}
