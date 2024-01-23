/*
Package models

File : map.go

Brief : This file contains the Map struct and its methods.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX (quality check)
  - Alexis BONAMY
  - Paul TRÉMOUREUX
  - Marie BORDET

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
	"errors"
	"fmt"
	"log"
	"math"
	"math/rand"
	"network-journey-simulator/src/configs"
	"network-journey-simulator/src/tools"
	"sort"
	"strconv"
	"strings"
)

const (
	/*
		Used to determine how far away are the stations from one another
	*/
	mapScale        = 0.01
	strSC    string = "Station code"
	strSNam  string = "Station name"
	strSNum  string = "Station Number"
	strLin   string = "Line"
	strLN    string = "Line Name"
	strLat   string = "Latitude"
	strLon   string = "Longitude"
)

/*
Map is the structure that manage the map of the metro network.

Attributes :
  - graphTimeBetweenStation [][]int : the graph of the time between stations
  - graphDelay [][]int : the graph of the delay between stations
  - stationsMappingCsv tools.CsvFile : the csv file of the stations mapping
  - stationsCsv tools.CsvFile : the csv file of the stations
  - stationsLinesCsv tools.CsvFile : the csv file of the stations lines
  - isConvex bool : true if the map is convex, false otherwise
  - lines []*MetroLine : the lines of the map
  - stations []*MetroStation : the stations of the map
  - graph [][]*PathStation : the graph of the map
  - EventMultipleStationsClosed []*EventMultipleStationsClosed : the events
    of multiple stations closed
  - eventsAttendancePeak []*EventAttendancePeak : the events of attendance
    peak

Methods :
  - Stations() []MetroStation : return the stations of the map
  - Stations2() []*MetroStation : return the stations of the map
  - Lines() []*MetroLine : return the lines of the map
  - FindStationById(id int) *MetroStation : return the station with the id
    id
  - Lines2() []*MetroLine : return the lines of the map
  - Graph() [][]*PathStation : return the graph of the map
  - IsConvex() bool : return true if the map is convex, false otherwise
  - GraphTimeBetweenStation() [][]int : return the graph of the time between
    stations
  - setGraphTimeBetweenStation(graph [][]int) : set the graph of the time
    between stations
  - GraphDelay() [][]int : return the graph of the delay between stations
  - AddDelay(idStation1 int, idStation2 int, delay int) : add a delay
  - ExportMapToAdConfig() configs.AdvancedConfig : export the map to an
    advanced config
  - generateSingleLineRailway(config configs.ConfigurationType) error :
    generate a single line railway
  - generateDoubleLineRailway(config configs.ConfigurationType) error :
    generate a double line railway
  - checkUnreachable(i, j int, graph [][]*PathStation,
    hasBeenModified bool) ([][]*PathStation, bool) : check if we can reach
    one of our unreachable
  - multiLineRelationLoop(graph [][]*PathStation,
    hasBeenModified bool) [][]*PathStation : loop of the function
    multiLineRelation
  - multiLineRelation(graph [][]*PathStation) [][]*PathStation : create the
    >2-lines relations (with transfer)
  - GenerateGraph() : generate the graph of the map
  - initStationsIdNear(startId int, startStation *MetroStation, graph
    [][]*PathStation, stationModifiedId []int) ([][]*PathStation, []int) :
    init the stations near
  - initStationsIdFarAffectation(startId, endStationId, stationMiddleId int,
    graph [][]*PathStation, stationModifiedId []int, line *MetroLine)
    ([][]*PathStation, []int) : init the stations far
  - initStationsIdFarPreAffectation(startId, stationMiddleId int, graph
    [][]*PathStation, stationModifiedId []int, line *MetroLine, stationMiddle
    *MetroStation) ([][]*PathStation, []int) : init the stations far
  - initStationsIdFar(startId, endStationId, stationMiddleId int, graph
    [][]*PathStation, stationModifiedId []int, line *MetroLine)
    ([][]*PathStation, []int) : init the stations far
  - writeCsv() : write the csv files
  - generateGraphTimeBetweenStation() : generate the graph of the time between
    stations
*/
type Map struct {
	graphTimeBetweenStation     [][]int
	graphDelay                  [][]int
	stationsMappingCsv          tools.CsvFile
	stationsCsv                 tools.CsvFile
	stationsLinesCsv            tools.CsvFile
	isConvex                    bool
	lines                       []*MetroLine
	stations                    []*MetroStation
	graph                       [][]*PathStation
	eventMultipleStationsClosed []*EventMultipleStationsClosed
	eventsAttendancePeak        []*EventAttendancePeak
}

/*
check is used to check if an error is not nil.

Param :
  - e error : the error to check
*/
func check(e error) {
	if e != nil {
		panic(e)
	}
}

/*
Stations returns the stations of the map.

Param :
  - mapPointer *Map : the map

Return :
  - []MetroStation : the stations of the map
*/
func (mapPointer *Map) Stations() []MetroStation {

	clone := make([]MetroStation, len(mapPointer.stations))

	for i := range clone {
		clone[i] = *mapPointer.stations[i]
	}

	return clone
}

/*
Stations2 returns the stations of the map.

Param :
  - mapPointer *Map : the map

Return :
  - []*MetroStation : the stations of the map
*/
func (mapPointer *Map) Stations2() []*MetroStation {
	return mapPointer.stations
}

/*
Lines returns the lines of the map.

Param :
  - mapPointer *Map : the map

Return :
  - []*MetroLine : the lines of the map
*/
func (mapPointer *Map) Lines() []*MetroLine {

	clone := make([]*MetroLine, len(mapPointer.lines))

	copy(clone, mapPointer.lines)

	return clone
}

/*
FindStationById returns the station with the id id.

Param :
  - mapPointer *Map : the map
  - id int : the id of the station

Return :
  - *MetroStation : the station with the id id
*/
func (mapPointer *Map) FindStationById(id int) *MetroStation {
	stations := mapPointer.Stations2()
	for i := 0; i < len(stations); i++ {
		if stations[i].Id() == id {
			return stations[i]
		}
	}
	return nil
}

/*
Lines2 returns the lines of the map.

Param :
  - mapPointer *Map : the map

Return :
  - []*MetroLine : the lines of the map
*/
func (mapPointer *Map) Lines2() []*MetroLine {
	return mapPointer.lines
}

/*
Graph returns the graph of the map.

Param :
  - mapPointer *Map : the map

Return :
  - [][]*PathStation : the graph of the map
*/
func (mapPointer *Map) Graph() [][]*PathStation {
	println("is mapPointer.graph nil ? " + strconv.FormatBool(
		mapPointer.graph == nil))
	clone := make([][]*PathStation, len(mapPointer.graph))

	for i := range mapPointer.graph {
		clone[i] = make([]*PathStation, len(mapPointer.graph[i]))
		copy(clone[i], mapPointer.graph[i])
	}

	return clone
}

/*
IsConvex returns true if the map is convex, false otherwise.

Param :
  - mapPointer *Map : the map

Return :
  - bool : true if the map is convex, false otherwise
*/
func (mapPointer *Map) IsConvex() bool {
	return mapPointer.isConvex
}

/*
GraphTimeBetweenStation returns the graph of the time between stations.

Param :
  - mapPointer *Map : the map

Return :
  - [][]int : the graph of the time between stations
*/
func (mapPointer *Map) GraphTimeBetweenStation() [][]int {
	return mapPointer.graphTimeBetweenStation
}

/*
setGraphTimeBetweenStation set the graph of the time between stations.

Param :
  - mapPointer *Map : the map
  - graph [][]int : the graph of the time between stations
*/
func (mapPointer *Map) setGraphTimeBetweenStation(graph [][]int) {
	mapPointer.graphTimeBetweenStation = graph
}

/*
GraphDelay returns the graph of the delay between stations.

Param :
  - mapPointer *Map : the map

Return :
  - [][]int : the graph of the delay between stations
*/
func (mapPointer *Map) GraphDelay() [][]int {
	return mapPointer.graphDelay
}

/*
CreateMap creates a new map.

/!\ In order to create map from scratch and save it only
- use CreateMapAdvanced for the run deprecated - use CreateMapAdvanced now

Return :
  - Map : the new map
  - error : an error if something went wrong
*/
func CreateMap() (Map, error) {

	/**
	THIS FUNCTION IS NOT FOR SIMULATION USE, ONLY TO CREATE RANDOM MAPS.
	*/

	m := Map{
		stations: make([]*MetroStation, 0),
		lines:    make([]*MetroLine, 0),
		stationsCsv: tools.NewFileWithDelimiter("stations",
			[]string{strSC,
				strSNam,
				strLat,
				strLon},
			";"),
		stationsMappingCsv: tools.NewFile("station_numbers",
			[]string{
				strSNum,
				strSC}),
		stationsLinesCsv: tools.NewFile("stations_lines",
			[]string{
				strSC,
				strSNum,
				strLin,
				strLN,
			}),
	}

	config := configs.GetInstance()

	var err error

	rand.New(rand.NewSource(config.Seed()))

	if config.Lines() == 1 {
		if config.InterchangeStations() > 0 {
			panic("there cant be any interchange stations on a single line system")
		}
		err = m.generateSingleLineRailway(config)
	} else if config.Lines() == 2 {
		if config.Stations() < 9 {
			panic("You need at least 9 stations to generate a two lines system.")
		} else if config.InterchangeStations() != 1 {
			panic("You need to have exactly one interchange station " +
				"for a two lines system.")
		}
		err = m.generateDoubleLineRailway(config)
	} else {
		panic("You tried to create a map with an unsupported number of lines")
	}

	check(err)

	m.GenerateGraph()
	m.generateGraphTimeBetweenStation()

	m.graphDelay = make([][]int, len(m.stations)) //initialized with 0
	for i := 0; i < len(m.stations); i++ {
		m.graphDelay[i] = make([]int, len(m.stations))
	}

	m.writeCsv()

	return m, err
}

/*
AddDelay add a delay.

Param :
  - mapPointer *Map : the map
  - idStation1 int : the id of the first station
  - idStation2 int : the id of the second station
  - delay int : the delay to add
*/
func (mapPointer *Map) AddDelay(idStation1 int, idStation2 int, delay int) {
	if len(mapPointer.GraphDelay()) < idStation1 ||
		len(mapPointer.GraphDelay()) < idStation2 {
		log.Fatal("Cannot add a delay : station id not in graph (too high)")
	} else if idStation1 < 0 || idStation2 < 0 {
		log.Fatal("Cannot add a delay : station id cannot be negativ")
	}
	mapPointer.graphDelay[idStation1][idStation2] = delay
	mapPointer.graphDelay[idStation2][idStation1] = delay
}

/*
CreateMapAdvanced creates a new map from an advanced config.

Param :
  - adConfig configs.AdvancedConfig : the advanced config

Return :
  - Map : the new map
*/
func CreateMapAdvanced(adConfig configs.AdvancedConfig) Map {

	config := configs.GetInstance()

	rand.New(rand.NewSource(config.Seed()))

	m := Map{
		stations: make([]*MetroStation, len(adConfig.MapC.Stations)),
		lines:    make([]*MetroLine, len(adConfig.MapC.Lines)),
		stationsCsv: tools.NewFileWithDelimiter("stations",
			[]string{strSC,
				strSNam,
				strLat,
				strLon},
			";"),
		stationsMappingCsv: tools.NewFile("Stations_mapping",
			[]string{
				strSNum,
				strSC}),
		stationsLinesCsv: tools.NewFile("stations_lines",
			[]string{
				strSC,
				strSNum,
				strLin,
				strLN,
			}),
	}

	//create stations
	for i, station := range adConfig.MapC.Stations {
		if i != station.Id {
			log.Fatalf("map init - station.Id (%d) not equal to i (%d)", station.Id, i)
		}
		m.stations[i] = &MetroStation{
			name:   station.Name,
			id:     station.Id, //should be equal to i
			number: station.Id + 1,
			code:   strings.ToUpper(station.Name[0:3]),
			idArea: station.IdArea,
		}

		m.stations[i].position = Point{
			x: station.Position.Longitude,
			y: station.Position.Latitude,
		}

		//TODO add lines-platform
	}

	//create lines
	for i, line := range adConfig.MapC.Lines {
		if i != line.Id {
			log.Fatalf("map init - line.Id (%d) not equal to i (%d)", line.Id, i)
		}

		m.lines[i] = &MetroLine{
			id:     line.Id,
			number: line.Id + 1,
			code: strings.ToUpper(line.Name[:int(math.Min(3,
				float64(len(line.Name))))]),
			name:                  line.Name,
			trainNumber:           line.NumberOfTrain,
			passengersMaxPerTrain: config.CapacityPerTrain(),
		}

		//m.lines[i].stations = make([]*MetroStation, len(line.Stations))
	}

	//links lines and stations
	for _, station := range adConfig.MapC.Stations {
		for _, line := range station.Lines {
			m.stations[station.Id].AddMetroLine(m.lines[line.Id])
			m.lines[line.Id].AddMetroStation(m.stations[station.Id])
		}
	}

	m.GenerateGraph()
	m.generateGraphTimeBetweenStation()

	m.graphDelay = make([][]int, len(m.stations)) //initialized with 0
	for i := 0; i < len(m.stations); i++ {
		m.graphDelay[i] = make([]int, len(m.stations))
	}

	m.writeCsv()

	return m
}

/*
ExportMapToAdConfig export the map to an advanced config.

Param :
  - mapPointer *Map : the map

Return :
  - configs.AdvancedConfig : the advanced config
*/
func (mapPointer *Map) ExportMapToAdConfig() configs.AdvancedConfig {
	var adConfig configs.AdvancedConfig

	//create map
	var mapC = configs.ConfigMap{}

	//add stations
	var stationC configs.ConfigStation
	for _, station := range mapPointer.stations {
		stationC = configs.ConfigStation{
			Name: station.name,
			Id:   station.id,
			Position: struct {
				Latitude  float64 `xml:"latitude"`
				Longitude float64 `xml:"longitude"`
			}{
				Longitude: station.position.x,
				Latitude:  station.position.y,
			},
		}

		for _, line := range station.metroLines {
			stationC.Lines = append(stationC.Lines, struct {
				Id       int    `xml:"id,attr"`
				Platform string `xml:"platform,attr"`
			}{
				Id: line.id, //TODO platform
			})
		}
		mapC.Stations = append(mapC.Stations, stationC)
	}

	//	println("stations ", len(mapPointer.stations), " -- ", len(mapC.Stations))

	//add lines
	var lineC configs.ConfigLine
	for _, line := range mapPointer.lines {
		lineC = configs.ConfigLine{
			Name:          line.name,
			Id:            line.id,
			NumberOfTrain: line.trainNumber,
		}

		for i, station := range line.stations {
			lineC.Stations = append(lineC.Stations, struct {
				Id    int `xml:"id,attr"`
				Order int `xml:"order,attr"`
			}{
				Id:    station.id,
				Order: i,
			})
		}
		mapC.Lines = append(mapC.Lines, lineC)
	}

	//add events
	var multipleStationsClosedEventsC configs.ConfigMultipleStationsClosedEvent
	for _, eventMultipleStationsClosed := range mapPointer.
		eventMultipleStationsClosed {
		multipleStationsClosedEventsC = configs.ConfigMultipleStationsClosedEvent{
			StartString:    eventMultipleStationsClosed.start.String(),
			EndString:      eventMultipleStationsClosed.end.String(),
			StationIdStart: eventMultipleStationsClosed.idStationStart,
			StationIdEnd:   eventMultipleStationsClosed.idStationEnd,
		}
		mapC.EventsMultipleStationsClosed = append(mapC.EventsMultipleStationsClosed,
			multipleStationsClosedEventsC)
	}

	var attendancePeakEventsC configs.ConfigAttendancePeakEvent
	for _, eventAttendancePeak := range mapPointer.eventsAttendancePeak {
		attendancePeakEventsC = configs.ConfigAttendancePeakEvent{
			StartString: eventAttendancePeak.start.String(),
			EndString:   eventAttendancePeak.end.String(),
			PeakString:  eventAttendancePeak.peak.String(),
			StationId:   eventAttendancePeak.idStation,
			PeakSize:    eventAttendancePeak.peakSize,
		}
		mapC.EventsAttendancePeak = append(mapC.EventsAttendancePeak,
			attendancePeakEventsC)
	}

	adConfig.MapC = mapC

	return adConfig
}

/*
generateLines generate the lines of the map.

Param :
  - mapPointer *Map : the map
  - config configs.ConfigurationType : the configuration object

Return :
  - error : an error if something went wrong
*/
func generateLines(mapPointer *Map, config configs.ConfigurationType) error {

	linesName := config.NameLineList()

	for i := 0; i < config.Lines(); i++ {
		mapPointer.lines = append(mapPointer.lines, &MetroLine{
			number:                i + 1,
			id:                    i,
			name:                  linesName[i],
			code:                  strings.ToUpper(linesName[i][0:3]),
			passengersMaxPerTrain: config.CapacityPerTrain(),
			trainNumber:           config.TrainsPerLine(),
		})
	}

	return nil
}

/*
generateSingleLineRailway generate a single line railway.

Param :
  - mapPointer *Map : the map
  - config configs.ConfigurationType : the configuration object

Return :
  - error : an error if something went wrong
*/
func (mapPointer *Map) generateSingleLineRailway(
	config configs.ConfigurationType) error {

	err := generateLines(mapPointer, config)

	if err != nil {
		return err
	}

	stationsName := config.NameStationList()

	for i := 0; i < config.Stations(); i++ {

		station := &MetroStation{
			name:   stationsName[i],
			id:     i,
			number: i + 1,
			code:   strings.ToUpper(stationsName[i][0:3]),
		}

		mapPointer.stations = append(mapPointer.stations, station)

		mapPointer.stations[i].AddMetroLine(mapPointer.lines[0])
		mapPointer.lines[0].AddMetroStation(station)

	}

	X, Y := float64(0), float64(0)

	/*
		Direction is the direction from which next station position
		will be calculated
		The numbers represents a cardinal direction
		0 being N, 1 NNE, 2 NE, 3 ENE, 4 E and so on
		https://upload.wikimedia.org/wikipedia/commons/1/1a/Brosen_windrose.svg
	*/

	direction := rand.Intn(16)

	for i := 0; i < len(mapPointer.stations); i++ {
		mapPointer.stations[i].position.x = X
		mapPointer.stations[i].position.y = Y
		direction = nextDirection(direction)
		X, Y = computeNextPosition(X, Y, direction)
	}

	return nil
}

/*
generateDoubleLineRailway generate a double line railway.

Param :
  - mapPointer *Map : the map
  - config configs.ConfigurationType : the configuration object

Return :
  - error : an error if something went wrong
*/
func (mapPointer *Map) generateDoubleLineRailway(
	config configs.ConfigurationType) error {
	//TODO that
	err := generateLines(mapPointer, config)

	check(err)

	stationsName := config.NameStationList()

	var interchangeStation *MetroStation

	//create the stations
	for i := 0; i < config.Stations(); i++ {

		station := MetroStation{
			name:   stationsName[i],
			id:     i,
			number: i + 1,
			code:   strings.ToUpper(stationsName[i][0:3]),
		}

		mapPointer.stations = append(mapPointer.stations, &station)

		if i == int(config.Stations()/2) {
			mapPointer.stations[i].AddMetroLine(mapPointer.lines[0])
			mapPointer.lines[0].AddMetroStation(&station)
			mapPointer.stations[i].AddMetroLine(mapPointer.lines[1])
			mapPointer.lines[1].AddMetroStation(&station)

			interchangeStation = &station
		} else if i%2 == 0 {

			mapPointer.stations[i].AddMetroLine(mapPointer.lines[0])
			mapPointer.lines[0].AddMetroStation(&station)

		} else {
			mapPointer.stations[i].AddMetroLine(mapPointer.lines[1])
			mapPointer.lines[1].AddMetroStation(&station)
		}
	}

	//mapPointer.stations[i].AddMetroLine(&mapPointer.lines[0])
	//mapPointer.lines[0].AddMetroStation(&station)

	X, Y := float64(0), float64(0)

	stationsLineOne := mapPointer.lines[0].stations
	stationsLineTwo := mapPointer.lines[1].stations

	/*
		Direction is the direction from which next station position will
		be calculated
		The numbers represents a cardinal direction
		0 being N, 1 NNE, 2 NE, 3 ENE, 4 E and so on
		https://upload.wikimedia.org/wikipedia/commons/1/1a/Brosen_windrose.svg
	*/

	// First we place the stations for line one
	direction := rand.Intn(16)
	var directionInterchangeStation int

	for _, station := range stationsLineOne {

		station.position.x = X
		station.position.y = Y
		if station.id == interchangeStation.id {
			directionInterchangeStation = direction
		}
		direction = nextDirection(direction)
		X, Y = computeNextPosition(X, Y, direction)

	}

	placeLineForInterchangeStation(interchangeStation, stationsLineTwo,
		directionInterchangeStation)

	return nil
}

/*
Unused function
func (mapPointer *Map) fillMultipleLineRailway(
	config configs.ConfigurationType) error {
	return nil
}
*/

/*
placeLineForInterchangeStation is used to place the stations of a line
around the interchange station.

Param :
  - interchangeStation *MetroStation : the interchange station
  - lineStations []*MetroStation : the stations of the line
  - direction int : the direction
*/
func placeLineForInterchangeStation(interchangeStation *MetroStation,
	lineStations []*MetroStation, direction int) {

	var nonInterchangeStations []*MetroStation

	for _, station := range lineStations {
		if station.number != interchangeStation.number {
			nonInterchangeStations = append(nonInterchangeStations, station)
		}
	}

	indexInterchange := int(len(lineStations) / 2)

	j := 0
	for i := range lineStations {
		if i == indexInterchange {
			lineStations[i] = interchangeStation
		} else {
			lineStations[i] = nonInterchangeStations[j]
			j++
		}
	}

	X := interchangeStation.position.x
	Y := interchangeStation.position.y

	// Now that the interchange station is placed around the center of the line
	// We place the stations

	for i := indexInterchange - 1; i >= 0; i-- {
		X, Y = computeNextPosition(X, Y, direction)
		lineStations[i].position.x = X
		lineStations[i].position.y = Y
		direction = nextDirection(direction)
	}

	X = interchangeStation.position.x
	Y = interchangeStation.position.y

	//Reverse the direction for the other half of the line
	direction = direction + 8

	for i := indexInterchange + 1; i < len(lineStations); i++ {
		X, Y = computeNextPosition(X, Y, direction)
		lineStations[i].position.x = X
		lineStations[i].position.y = Y
		direction = nextDirection(direction)
	}
}

/*
nextDirection is used to determine the next direction.

Param :
  - direction int : the direction

Return :
  - int : the next direction
*/
func nextDirection(direction int) int {
	return direction + (rand.Intn(5) - 2)
}

/*
computeNextPosition is used to compute the next position.

Param :
  - X float64 : the x
  - Y float64 : the y
  - direction int : the direction

Return :
  - float64 : the new x
  - float64 : the new y
*/
func computeNextPosition(X, Y float64, direction int) (newX, newY float64) {
	adjustedDirection := (direction + 4) % 16
	circlePosition := (math.Pi * 2) * (float64(adjustedDirection) / 16)
	newX = X - math.Cos(circlePosition)*mapScale
	newY = Y + math.Sin(circlePosition)*mapScale
	return newX, newY
}

/*
checkUnreachable is used for each reachable station to check if we can
reach one of our unreachable.

Param :
  - mapPointer *Map : the map
  - i int : the index of the first station
  - j int : the index of the second station
  - graph [][]*PathStation : the graph
  - hasBeenModified bool : true if the graph has been modified, false
    otherwise

Return :
  - [][]*PathStation : the graph
  - bool : true if the graph has been modified, false otherwise
*/
func (mapPointer *Map) checkUnreachable(i, j int, graph [][]*PathStation,
	hasBeenModified bool) ([][]*PathStation, bool) {
	for k := 0; k < len(mapPointer.stations); k++ {
		if i == k {
			continue
		}

		if mapPointer.stations[k].StatusIsClosed() {
			continue
		}

		if graph[i][k] == nil && graph[j][k] != nil &&
			!graph[j][k].haveSameLineAs(graph[i][j]) {
			//TODO check that the two graph don't use the same lines
			graph[i][k] = graph[i][j].append(graph[j][k])

			if !graph[i][k].checkValidity() {
				println(mapPointer.stations[i].name + " -> " +
					mapPointer.stations[k].name)
				println("graph1:\n" + graph[i][j].String())
				println("graph2:\n" + graph[j][k].String())
				println("result:\n" + graph[i][k].String() + "\n")
			}
			hasBeenModified = true
		}
	}
	return graph, hasBeenModified
}

/*
multiLineRelationLoop is used for the loop of the function multiLineRelation.

Param :
  - mapPointer *Map : the map
  - graph [][]*PathStation : the graph
  - hasBeenModified bool : true if the graph has been modified, false
    otherwise

Return :
  - [][]*PathStation : the graph
*/
func (mapPointer *Map) multiLineRelationLoop(graph [][]*PathStation,
	hasBeenModified bool) [][]*PathStation {
	for i := 0; i < len(mapPointer.stations); i++ {
		//for each reachable station, check if we can reach one of our unreachable
		for j := 0; j < len(mapPointer.stations); j++ {
			if i == j || graph[i][j] == nil {
				// same station || A -> B not possible (so A->B->C not possible)
				continue
			}

			if mapPointer.stations[i].StatusIsClosed() ||
				mapPointer.stations[j].StatusIsClosed() {
				continue
			}

			graph, hasBeenModified = mapPointer.checkUnreachable(i, j,
				graph, hasBeenModified)
		}
	}
	return graph
}

/*
multiLineRelation is used to create the >2-lines relations (with transfer).

Param :
  - mapPointer *Map : the map
  - graph [][]*PathStation : the graph

Return :
  - [][]*PathStation : the graph
*/
func (mapPointer *Map) multiLineRelation(
	graph [][]*PathStation) [][]*PathStation {
	var hasBeenModified = true //true for entering the loop
	for hasBeenModified {
		hasBeenModified = false
		graph = mapPointer.multiLineRelationLoop(graph, hasBeenModified)
	}
	return graph
}

/*
GenerateGraphOld generate the graph enabling a passenger to determine to
which station he should go next to arrive to destination this function needs
the metroLines and metroStation to be finished deprecated.

Param :
  - mapPointer *Map : the map
*/
func (mapPointer *Map) GenerateGraphOld() {

	//attribute new ids to the elements
	mapPointer.attributeIDs()

	//init the graph
	var graph = make([][]*PathStation, len(mapPointer.stations))
	for i := 0; i < len(mapPointer.stations); i++ {
		graph[i] = make([]*PathStation, len(mapPointer.stations))
	}

	// create the 1-line relations (no transfer)
	for lineI := 0; lineI < len(mapPointer.lines); lineI++ {
		var line = mapPointer.lines[lineI]

		for i := 0; i < len(line.stations); i++ {
			for j := i + 1; j < len(line.stations); j++ {
				var stationI = line.stations[i]
				var stationJ = line.stations[j]

				if stationI.StatusIsClosed() || stationJ.StatusIsClosed() {
					continue
				}

				// I -> J
				graph[stationI.id][stationJ.id] = PathStationCreate(line,
					line.stations[i:j+1]...)
				// J -> I (we estimate there is no one-direction-only lines
				graph[stationJ.id][stationI.id] = PathStationInverse(line,
					line.stations[i:j+1]...)
			}
		}
	}

	//create the >2-lines relations (with transfer)
	graph = mapPointer.multiLineRelation(graph)

	//verify the map is goodly formed
	badlyFormed := mapPointer.checkMapValidity(graph)

	if badlyFormed {
		log.Fatal("graph has been badly formed, see error log for more details")
	}

	mapPointer.graph = graph
	mapPointer.isConvex = mapPointer.isConvexCalc()

}

/*
initStationsIdNear is used to initialize the ids of stations that are in
proximity (range = 1).

Param :
  - mapPointer *Map : the map
  - startId int : the id of the start station
  - startStation *MetroStation : the start station
  - graph [][]*PathStation : the graph
  - stationModifiedId []int : the modified ids

Return :
  - [][]*PathStation : the graph
  - []int : the modified ids
*/
func (mapPointer *Map) initStationsIdNear(startId int,
	startStation *MetroStation, graph [][]*PathStation,
	stationModifiedId []int) ([][]*PathStation, []int) {
	for lineId := range mapPointer.stations[startId].Lines() {
		var line = mapPointer.stations[startId].Lines()[lineId]
		positionStation := line.PositionInLine(startStation)
		if positionStation > 0 {
			endStationId := line.stations[positionStation-1].id
			graph[startId][endStationId] = PathStationCreate(line,
				mapPointer.stations[startId], mapPointer.stations[endStationId])
			stationModifiedId = append(stationModifiedId, endStationId)
		}
		if positionStation < len(line.stations)-1 {
			endStationId := line.stations[positionStation+1].id
			graph[startId][endStationId] = PathStationCreate(line,
				mapPointer.stations[startId], mapPointer.stations[endStationId])
			stationModifiedId = append(stationModifiedId, endStationId)
		}
	}
	return graph, stationModifiedId
}

/*
initStationsIdFarPreAffectation is used to affect the ids depending on the
position of the station in the line.

Param :
  - mapPointer *Map : the map
  - startId int : the id of the start station
  - endStationId int : the id of the end station
  - stationMiddleId int : the id of the middle station
  - graph [][]*PathStation : the graph
  - stationModifiedId []int : the modified ids
  - line *MetroLine : the line

Return :
  - [][]*PathStation : the graph
  - []int : the modified ids
*/
func (mapPointer *Map) initStationsIdFarAffectation(startId, endStationId,
	stationMiddleId int, graph [][]*PathStation, stationModifiedId []int,
	line *MetroLine) ([][]*PathStation, []int) {
	if graph[startId][endStationId] == nil ||
		len(graph[startId][endStationId].stations) >
			len(graph[startId][stationMiddleId].stations)+2 {
		//+1 would mean the same size, +2 means we are more efficient
		//means it is the shortest path
		pathStationMiddle := PathStationCreate(line,
			mapPointer.stations[stationMiddleId],
			mapPointer.stations[endStationId])
		graph[startId][endStationId] = graph[startId][stationMiddleId].append(
			pathStationMiddle)
		stationModifiedId = append(stationModifiedId, endStationId)
	}
	return graph, stationModifiedId
}

/*
initStationsIdFarPreAffectation is used to prepare the affectation depending on
the position of the station in the line.

Param :
  - mapPointer *Map : the map
  - startId int : the id of the start station
  - stationMiddleId int : the id of the middle station
  - graph [][]*PathStation : the graph
  - stationModifiedId []int : the modified ids
  - line *MetroLine : the line
  - stationMiddle *MetroStation : the middle station

Return :
  - [][]*PathStation : the graph
  - []int : the modified ids
*/
func (mapPointer *Map) initStationsIdFarPreAffectation(startId,
	stationMiddleId int, graph [][]*PathStation, stationModifiedId []int,
	line *MetroLine, stationMiddle *MetroStation) ([][]*PathStation, []int) {
	positionStation := line.PositionInLine(stationMiddle)
	if positionStation > 0 {
		endStationId := line.stations[positionStation-1].id
		if endStationId != startId {
			graph, stationModifiedId = mapPointer.initStationsIdFarAffectation(
				startId, endStationId, stationMiddleId, graph,
				stationModifiedId, line)
		}
	}
	if positionStation < len(line.stations)-1 {
		endStationId := line.stations[positionStation+1].id
		if endStationId != startId {
			graph, stationModifiedId = mapPointer.initStationsIdFarAffectation(
				startId, endStationId, stationMiddleId, graph,
				stationModifiedId, line)
		}
	}
	return graph, stationModifiedId
}

/*
initStationsIdFar is used to initialize the ids of stations that are > 1 range.

Param :
  - mapPointer *Map : the map
  - startId int : the id of the start station
  - graph [][]*PathStation : the graph
  - stationModifiedId []int : the modified ids

Return :
  - [][]*PathStation : the graph
  - []int : the modified ids
*/
func (mapPointer *Map) initStationsIdFar(startId int, graph [][]*PathStation,
	stationModifiedId []int) ([][]*PathStation, []int) {
	for len(stationModifiedId) > 0 {
		stationModifiedIdCopy := stationModifiedId
		stationModifiedId = make([]int, 0)
		for _, stationMiddleId := range stationModifiedIdCopy {
			var stationMiddle = mapPointer.stations[stationMiddleId]
			for lineId := range mapPointer.stations[stationMiddleId].Lines() {
				var line = mapPointer.stations[stationMiddleId].Lines()[lineId]
				graph, stationModifiedId = mapPointer.initStationsIdFarPreAffectation(
					startId, stationMiddleId, graph,
					stationModifiedId, line, stationMiddle)
			}
		}
	}
	return graph, stationModifiedId
}

/*
checkMapValidity is used to verify if the map is goodly formed

Param :
  - mapPointer *Map : the map
  - graph [][]*PathStation : the graph

Return :
  - bool : is the map badly formed (badlyFormed)
    -- true if badly formed
    -- false if goodly formed
*/
func (mapPointer *Map) checkMapValidity(graph [][]*PathStation) bool {
	var badlyFormed = false
	for i := 0; i < len(graph); i++ {
		for j := 0; j < len(graph); j++ {
			if graph[i][j] != nil && !graph[i][j].checkValidity() {
				if badlyFormed == false {
					println("BADLY FORMED : ")
				}
				badlyFormed = true
				println(mapPointer.stations[i].name + " -> " + mapPointer.stations[j].name)
				println(graph[i][j].String() + "\n")
			}
		}
	}
	return badlyFormed
}

/*
GenerateGraph generate the graph enabling a passenger to determine to which
station he should go next to arrive to destination this function needs the
metroLines and metroStation to be finished.

Param :
  - mapPointer *Map : the map
*/
func (mapPointer *Map) GenerateGraph() {
	//attribute new ids to the elements
	mapPointer.attributeIDs()

	//init the graph
	var graph = make([][]*PathStation, len(mapPointer.stations))
	for i := 0; i < len(mapPointer.stations); i++ {
		graph[i] = make([]*PathStation, len(mapPointer.stations))
	}

	//create the graph
	for startId, startStation := range mapPointer.stations {

		var stationModifiedId = make([]int, 0)

		//stations that are in proximity (range = 1)
		graph, stationModifiedId = mapPointer.initStationsIdNear(startId,
			startStation, graph, stationModifiedId)

		//stations that are > 1 range
		graph, stationModifiedId = mapPointer.initStationsIdFar(startId,
			graph, stationModifiedId)
	}

	//verify the map is goodly formed
	badlyFormed := mapPointer.checkMapValidity(graph)

	if badlyFormed {
		log.Fatal("graph has been badly formed, see error log for more details")
	}

	mapPointer.graph = graph
	mapPointer.isConvex = mapPointer.isConvexCalc()
}

/*
attributeIDs is used to reattribute the ID for the stations and the lines.

Param :
  - mapPointer *Map : the map
*/
func (mapPointer *Map) attributeIDs() {
	for i := 0; i < len(mapPointer.stations); i++ {
		mapPointer.stations[i].id = i
	}
	for i := 0; i < len(mapPointer.lines); i++ {
		mapPointer.lines[i].id = i
	}
}

/*
isConvexCalc is used to calculate if the city is convex.

Param :
  - mapPointer *Map : the map

Return :
  - bool : is the city convex (isConvex)
*/
func (mapPointer *Map) isConvexCalc() bool {
	for i := 0; i < len(mapPointer.stations); i++ {
		for j := i + 1; j < len(mapPointer.stations); j++ {
			if mapPointer.graph[i][j] == nil {
				return false
			}
		}
	}
	return true
}

/*
GetPathStation is used to get the path needed to go from s1 to s2.

Param :
  - mapPointer *Map : the map
  - s1 MetroStation : the start station
  - s2 MetroStation : the end station

Return :
  - PathStation : the path
  - error : an error if something went wrong
*/
func (mapPointer *Map) GetPathStation(s1,
	s2 MetroStation) (PathStation, error) {
	var sOut PathStation
	if len(mapPointer.graph) > s1.id && len(mapPointer.graph) > s2.id {
		if s1.id == s2.id {
			return sOut, errors.New("same id for the two station")
		}
		if mapPointer.graph[s1.id][s2.id] == nil {
			return sOut, errors.New("no existing path between " +
				s1.name + "-" + s2.name)
		}
		return *mapPointer.graph[s1.id][s2.id], nil
	}

	return sOut, errors.New("badly constructed graph")
}

/*
writeCsv is used to write the csv files.

Param :
  - mapPointer *Map : the map
*/
func (mapPointer *Map) writeCsv() {
	for _, station := range mapPointer.stations {
		mapPointer.stationsMappingCsv.Write([]string{
			strconv.Itoa(station.id),
			station.code,
		})
		mapPointer.stationsCsv.Write([]string{
			station.code,
			station.name,
			fmt.Sprintf("%f", station.position.x),
			fmt.Sprintf("%f", station.position.y),
		})
		if len(station.metroLines) > 1 {
			for _, line := range station.metroLines {
				mapPointer.stationsLinesCsv.Write([]string{
					station.code,
					strconv.Itoa(station.id),
					strconv.Itoa(line.id),
					line.name,
				})
			}
		} else {
			mapPointer.stationsLinesCsv.Write([]string{
				station.code,
				strconv.Itoa(station.id),
				strconv.Itoa(station.metroLines[0].id),
				station.metroLines[0].name,
			})
		}

	}
}

/*
timeBetweenDirectStations is used to calculate the time to travel between two
directly lined stations (no stations between them on a direct line).

Param :
  - station1 *MetroStation : the first station
  - station2 *MetroStation : the second station

Return :
  - int : the time to travel between the two stations
*/
func timeBetweenDirectStations(station1, station2 *MetroStation) int {
	conf := configs.GetInstance()

	var accelerationTrain = conf.AccelerationTrain()
	//m.s-2
	var maxSpeedTrain = conf.MaxSpeedTrain()
	//m.s-1
	var distanceBetweenStations = int(station1.distanceTo(*station2))
	//m

	var timeToMaxSpeed = maxSpeedTrain / accelerationTrain
	//s
	var maxDistanceAcceleration = accelerationTrain *
		timeToMaxSpeed * timeToMaxSpeed / 2
	//m

	var output = -1

	if distanceBetweenStations > 2*maxDistanceAcceleration {
		output = int(2*timeToMaxSpeed +
			(distanceBetweenStations-2*maxDistanceAcceleration)/maxSpeedTrain)
		//s
	} else {
		output = int(2 *
			math.Sqrt(float64(2*(distanceBetweenStations/2)/accelerationTrain)))
		//checked for distanceBetweenStations == 2 * maxDistanceAcceleration
	}

	return output
}

/*
generateGraphTimeBetweenStation is used to generate the time between each
station using some maths : T(a,b) = D(a,b) if Distance(a,b).

Param :
  - mapPointer *Map : the map
*/
func (mapPointer *Map) generateGraphTimeBetweenStation() {

	//create the graph
	var graph = make([][]int, len(mapPointer.stations))

	//init the graph
	for i := 0; i < len(mapPointer.stations); i++ {
		graph[i] = make([]int, len(mapPointer.stations))
	}

	for i := 0; i < len(mapPointer.stations)-1; i++ {
		s1 := mapPointer.stations[i]
		for j := i + 1; j < len(mapPointer.stations)-1; j++ {
			s2 := mapPointer.stations[j]
			var timeBetweenStations = timeBetweenDirectStations(s1, s2)
			graph[s1.id][s2.id] = timeBetweenStations
			graph[s2.id][s1.id] = timeBetweenStations
		}
	}

	mapPointer.setGraphTimeBetweenStation(graph)
}

/*
GetNearestStations is used to return the stations sorted by how near they are
from the point.

Param :
  - mapPointer *Map : the map
  - point Point : the point

Return :
  - []*MetroStation : the stations sorted by how near they are from the point
*/
func (mapPointer *Map) GetNearestStations(point Point) []*MetroStation {
	var output = make([]*MetroStation, len(mapPointer.stations))
	for i := range mapPointer.stations {
		output[i] = mapPointer.stations[i]
	}

	sort.Slice(output, func(i, j int) bool {
		return output[i].position.DistanceTo(point) <
			output[j].position.DistanceTo(point)
	})

	return output
}

/*
GetNearestStationOpened is used to return the nearest station that is opened.

Param :
  - mapPointer *Map : the map
  - point Point : the point

Return :
  - *MetroStation : the nearest station that is opened
*/
func (mapPointer *Map) GetNearestStationOpened(point Point) *MetroStation {
	var nearestStations = mapPointer.GetNearestStations(point)
	//divert all passengers to the closest non-closed station
	var nearestStation = nearestStations[0]
	for i := range nearestStations {
		if !nearestStation.StatusIsClosed() {
			break
		} else {
			nearestStation = nearestStations[i]
		}
	}
	return nearestStation
}

/*
GetNewPathStationMiddleClose is used when start->end cannot be done. It try to
find a path that can be done from the start to the closest point possible of
end.

Param :
  - mapPointer *Map : the map
  - start *MetroStation : the start station
  - end *MetroStation : the end station

Return :
  - *PathStation : the path
*/
func (mapPointer *Map) GetNewPathStationMiddleClose(start,
	end *MetroStation) *PathStation {

	if mapPointer.graph[start.id][end.id] != nil {
		return mapPointer.graph[start.id][end.id]
	}

	var distanceStartEnd = start.distanceTo(*end)

	var candidatesEnd []*MetroStation
	for i, middleS := range mapPointer.stations {
		if !middleS.StatusIsClosed() && //station opened
			middleS.id != start.id && //not start
			middleS.id != end.id && //not end
			middleS.distanceTo(*end) < distanceStartEnd &&
			//this station doesn't put us farther than the original path
			mapPointer.graph[start.id][middleS.id] != nil {
			//we can actually go to the station
			candidatesEnd = append(candidatesEnd, mapPointer.stations[i])
		}
	}

	if len(candidatesEnd) == 0 {
		return nil
	}

	if len(candidatesEnd) == 1 {
		return mapPointer.graph[start.id][candidatesEnd[0].id]
	}

	sort.Slice(candidatesEnd, func(i, j int) bool {
		return candidatesEnd[i].position.DistanceTo(end.position) <
			candidatesEnd[j].position.DistanceTo(end.position)
	})

	return mapPointer.graph[start.id][candidatesEnd[0].id]
}
