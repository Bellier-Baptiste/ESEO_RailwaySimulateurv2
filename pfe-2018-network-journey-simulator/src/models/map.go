package models

import (
	"errors"
	"fmt"
	"log"
	"math"
	"math/rand"
	"configs"
	"tools"
	"sort"
	"strconv"
	"strings"
)

// Used to determine how far away are the stations from one another
const mapScale = 0.01

type Map struct {
	graphTimeBetweenStation [][]int
	graphDelay              [][]int
	stationsMappingCsv      tools.CsvFile
	stationsCsv             tools.CsvFile
	stationsLinesCsv        tools.CsvFile
	isConvex                bool
	lines                   []*MetroLine
	stations                []*MetroStation
	graph                   [][]*pathStation
	eventsLineClosed		[]*EventLineClosed
	eventsAttendancePeak	[]*EventAttendancePeak
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func (mapPointer *Map) Stations() []MetroStation {

	clone := make([]MetroStation, len(mapPointer.stations))

	for i := range clone {
		clone[i] = *mapPointer.stations[i]
	}

	return clone
}

func (mapPointer *Map) Stations2() []*MetroStation {
	return mapPointer.stations
}

func (mapPointer *Map) Lines() []*MetroLine {

	clone := make([]*MetroLine, len(mapPointer.lines))

	copy(clone, mapPointer.lines)

	return clone
}

func (mapPointer *Map) FindStationById(id int) *MetroStation {
	stations := mapPointer.Stations2()
	for i := 0; i < len(stations); i++ {
		if stations[i].Id() == id {
			return stations[i]
		}
	}
	return nil
}

func (mapPointer *Map) Lines2() []*MetroLine {
	return mapPointer.lines
}

func (mapPointer *Map) Graph() [][]*pathStation {

	clone := make([][]*pathStation, len(mapPointer.graph))

	for i := range mapPointer.graph {
		clone[i] = make([]*pathStation, len(mapPointer.graph[i]))
		copy(clone[i], mapPointer.graph[i])
	}

	return clone
}

func (mapPointer *Map) IsConvex() bool {
	return mapPointer.isConvex
}

func (mapPointer *Map) GraphTimeBetweenStation() [][]int {
	return mapPointer.graphTimeBetweenStation
}

func (mapPointer *Map) setGraphTimeBetweenStation(graph [][]int) {
	mapPointer.graphTimeBetweenStation = graph
}

func (mapPointer *Map) GraphDelay() [][]int {
	return mapPointer.graphDelay
}

// /!\ for create map from scratch and save it only - use CreateMapAdvanced for the run
//deprecated - use CreateMapAdvanced now
func CreateMap() (Map, error) {

	/**
	THIS FUNCTION IS NOT FOR SIMULATION USE, ONLY TO CREATE RANDOM MAPS.
	*/

	m := Map{
		stations: make([]*MetroStation, 0),
		lines:    make([]*MetroLine, 0),
		stationsCsv: tools.NewFileWithDelimiter("stations",
			[]string{"Station code",
				"Station name",
				"Latitude",
				"Longitude"},
			";"),
		stationsMappingCsv: tools.NewFile("station_numbers",
			[]string{
				"Station Number",
				"Station code"}),
		stationsLinesCsv: tools.NewFile("stations_lines",
			[]string{
				"Station code",
				"Station Number",
				"Line",
				"Line Name",
			}),
	}

	config := configs.GetInstance()

	var err error

	rand.Seed(config.Seed())

	if config.Lines() == 1 {
		if config.InterchangeStations() > 0 {
			panic("there cant be any interchange stations on a single line system")
		}
		err = m.generateSingleLineRailway(config)
	} else if config.Lines() == 2 {
		if config.Stations() < 9 {
			panic("You need at least 9 stations to generate a two lines system.")
		} else if config.InterchangeStations() != 1 {
			panic("You need to have exactly one interchange station for a two lines system.")
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

func (mapPointer *Map) AddDelay(idStation1 int, idStation2 int, delay int) {
	if len(mapPointer.GraphDelay()) < idStation1 || len(mapPointer.GraphDelay()) < idStation2 {
		log.Fatal("Cannot add a delay : station id not in graph (too high)")
	} else if idStation1 < 0 || idStation2 < 0 {
		log.Fatal("Cannot add a delay : station id cannot be negativ")
	}
	mapPointer.graphDelay[idStation1][idStation2] = delay
	mapPointer.graphDelay[idStation2][idStation1] = delay
}

func CreateMapAdvanced(adConfig configs.AdvancedConfig) Map {

	config := configs.GetInstance()

	rand.Seed(config.Seed())

	m := Map{
		stations: make([]*MetroStation, len(adConfig.MapC.Stations)),
		lines:    make([]*MetroLine, len(adConfig.MapC.Lines)),
		stationsCsv: tools.NewFileWithDelimiter("stations",
			[]string{"Station code",
				"Station name",
				"Latitude",
				"Longitude"},
			";"),
		stationsMappingCsv: tools.NewFile("Stations_mapping",
			[]string{
				"Station Number",
				"Station code"}),
		stationsLinesCsv: tools.NewFile("stations_lines",
			[]string{
				"Station code",
				"Station Number",
				"Line",
				"Line Name",
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
			id:                    line.Id,
			number:                line.Id + 1,
			code:                  strings.ToUpper(line.Name[:int(math.Min(3, float64(len(line.Name))))]),
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
	var lineclosedeventsC configs.ConfigLineClosedEvent
	for _, eventLineClosed := range mapPointer.eventsLineClosed {
		lineclosedeventsC = configs.ConfigLineClosedEvent {
			StartString:		eventLineClosed.start.String(),
			EndString:			eventLineClosed.end.String(),
			StationIdStart:		eventLineClosed.idStationStart,
			StationIdEnd:		eventLineClosed.idStationEnd,
		}
		mapC.EventsLineClosed = append(mapC.EventsLineClosed, lineclosedeventsC)
	}
	
	var attendancepeakeventsC configs.ConfigAttendancePeakEvent
	for _, eventAttendancePeak := range mapPointer.eventsAttendancePeak {
		attendancepeakeventsC = configs.ConfigAttendancePeakEvent {
			TimeString:			eventAttendancePeak.time.String(),
			StationId:			eventAttendancePeak.idStation,
			Size:				eventAttendancePeak.size,
		}
		mapC.EventsAttendancePeak = append(mapC.EventsAttendancePeak, attendancepeakeventsC)
	}

	adConfig.MapC = mapC

	return adConfig
}

func generateLines(mapPointer *Map, config configs.ConfigurationObject) error {

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

func (mapPointer *Map) generateSingleLineRailway(config configs.ConfigurationObject) error {

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

	// Direction is the direction from which next station position will be calculated
	// The numbers represents a cardinal direction
	// 0 being N, 1 NNE, 2 NE, 3 ENE, 4 E and so on
	// https://upload.wikimedia.org/wikipedia/commons/1/1a/Brosen_windrose.svg

	direction := rand.Intn(16)

	for i := 0; i < len(mapPointer.stations); i++ {
		mapPointer.stations[i].position.x = X
		mapPointer.stations[i].position.y = Y
		direction = nextDirection(direction)
		X, Y = computeNextPosition(X, Y, direction)
	}

	return nil
}

func (mapPointer *Map) generateDoubleLineRailway(config configs.ConfigurationObject) error { //TODO that
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

	// Direction is the direction from which next station position will be calculated
	// The numbers represents a cardinal direction
	// 0 being N, 1 NNE, 2 NE, 3 ENE, 4 E and so on
	// https://upload.wikimedia.org/wikipedia/commons/1/1a/Brosen_windrose.svg

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

	placeLineForInterchangeStation(interchangeStation, stationsLineTwo, directionInterchangeStation)

	return nil
}

func (mapPointer *Map) fillMultipleLineRailway(config configs.ConfigurationObject) error {
	return nil
}

func placeLineForInterchangeStation(interchangeStation *MetroStation, lineStations []*MetroStation, direction int) {

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
		lineStations[i].position.x = float64(X)
		lineStations[i].position.y = float64(Y)
		direction = nextDirection(direction)
	}

	X = interchangeStation.position.x
	Y = interchangeStation.position.y

	//We inverse the direction for the other half of the line
	direction = direction + 8

	for i := indexInterchange + 1; i < len(lineStations); i++ {
		X, Y = computeNextPosition(X, Y, direction)
		lineStations[i].position.x = float64(X)
		lineStations[i].position.y = float64(Y)
		direction = nextDirection(direction)
	}
}

func nextDirection(direction int) int {
	return direction + (rand.Intn(5) - 2)
}

func computeNextPosition(X float64, Y float64, direction int) (newX, newY float64) {
	adjustedDirection := (direction + 4) % 16
	circlePosition := (math.Pi * 2) * (float64(adjustedDirection) / 16)
	newX = X - math.Cos(circlePosition)*mapScale
	newY = Y + math.Sin(circlePosition)*mapScale
	return newX, newY
}

//generate the graph enabling a passenger to determine to which station he should go next to arrive to destination
//this function needs the metroLines and metroStation to be finished
//deprecated
func (mapPointer *Map) GenerateGraphOld() {

	//attribute new ids to the elements
	mapPointer.attributeIDs()

	//init the graph
	var graph = make([][]*pathStation, len(mapPointer.stations))
	for i := 0; i < len(mapPointer.stations); i++ {
		graph[i] = make([]*pathStation, len(mapPointer.stations))
	}

	// create the 1-line relations (no transfert)
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
				graph[stationI.id][stationJ.id] = PathStation(line, line.stations[i:j+1]...)
				// J -> I (we estimate there is no one-direction-only lines
				graph[stationJ.id][stationI.id] = PathStationInverse(line, line.stations[i:j+1]...)
			}
		}
	}

	//create the >2-lines relations (with transfert)
	var hasBeenModified = true //true for entering the loop
	for hasBeenModified {
		hasBeenModified = false
		for i := 0; i < len(mapPointer.stations); i++ {
			//for each reachable station, check if we can reach one of our unreachable
			for j := 0; j < len(mapPointer.stations); j++ {
				if i == j || graph[i][j] == nil { // same station || A -> B not possible (so A->B->C not possible)
					continue
				}

				if mapPointer.stations[i].StatusIsClosed() || mapPointer.stations[j].StatusIsClosed() {
					continue
				}

				for k := 0; k < len(mapPointer.stations); k++ {
					if i == k {
						continue
					}

					if mapPointer.stations[k].StatusIsClosed() {
						continue
					}

					if graph[i][k] == nil && graph[j][k] != nil && !graph[j][k].haveSameLineAs(graph[i][j]) { //TODO cehck that the two graph don't use the same lines
						graph[i][k] = graph[i][j].append(graph[j][k])

						if !graph[i][k].checkValidity() {
							println(mapPointer.stations[i].name + " -> " + mapPointer.stations[k].name)
							println("graph1:\n" + graph[i][j].String())
							println("graph2:\n" + graph[j][k].String())
							println("result:\n" + graph[i][k].String() + "\n")
						}
						hasBeenModified = true
					}
				}
			}
		}
	}

	//verify the map is goodly formed
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

	if badlyFormed {
		log.Fatal("graph has been badly formed, see error log for more details")
	}

	mapPointer.graph = graph
	mapPointer.isConvex = mapPointer.isConvexCalc()

}

//generate the graph enabling a passenger to determine to which station he should go next to arrive to destination
//this function needs the metroLines and metroStation to be finished
func (mapPointer *Map) GenerateGraph() {
	//attribute new ids to the elements
	mapPointer.attributeIDs()

	//init the graph
	var graph = make([][]*pathStation, len(mapPointer.stations))
	for i := 0; i < len(mapPointer.stations); i++ {
		graph[i] = make([]*pathStation, len(mapPointer.stations))
	}

	//create the graph
	for startId, startStation := range mapPointer.stations {

		var stationModifiedId = make([]int, 0)

		//stations that are in close proximity (range = 1)
		for lineId := range mapPointer.stations[startId].Lines() {
			var line = mapPointer.stations[startId].Lines()[lineId]
			positionStation := line.PositionInLine(startStation)
			if positionStation > 0 {
				endStationId := line.stations[positionStation-1].id
				graph[startId][endStationId] = PathStation(line, mapPointer.stations[startId], mapPointer.stations[endStationId])
				stationModifiedId = append(stationModifiedId, endStationId)
			}
			if positionStation < len(line.stations)-1 {
				endStationId := line.stations[positionStation+1].id
				graph[startId][endStationId] = PathStation(line, mapPointer.stations[startId], mapPointer.stations[endStationId])
				stationModifiedId = append(stationModifiedId, endStationId)
			}
		}

		//stations that are > 1 range
		for len(stationModifiedId) > 0 {
			stationModifiedIdCopy := stationModifiedId
			stationModifiedId = make([]int, 0)
			for _, stationMiddleId := range stationModifiedIdCopy {
				var stationMiddle = mapPointer.stations[stationMiddleId]
				for lineId := range mapPointer.stations[stationMiddleId].Lines() {
					var line = mapPointer.stations[stationMiddleId].Lines()[lineId]
					positionStation := line.PositionInLine(stationMiddle)
					if positionStation > 0 {
						endStationId := line.stations[positionStation-1].id
						if endStationId != startId {
							if graph[startId][endStationId] == nil || len(graph[startId][endStationId].stations) > len(graph[startId][stationMiddleId].stations)+2 { //+1 would mean the same size, +2 means we are more efficient
								//means it is the shortest path
								pathStationMiddle := PathStation(line, mapPointer.stations[stationMiddleId], mapPointer.stations[endStationId])
								graph[startId][endStationId] = graph[startId][stationMiddleId].append(pathStationMiddle)
								stationModifiedId = append(stationModifiedId, endStationId)
							}
						}
					}
					if positionStation < len(line.stations)-1 {
						endStationId := line.stations[positionStation+1].id
						if endStationId != startId {
							if graph[startId][endStationId] == nil || len(graph[startId][endStationId].stations) > len(graph[startId][stationMiddleId].stations)+2 { //+1 would mean the same size, +2 means we are more efficient
								//means it is the shortest path
								pathStationMiddle := PathStation(line, mapPointer.stations[stationMiddleId], mapPointer.stations[endStationId])
								graph[startId][endStationId] = graph[startId][stationMiddleId].append(pathStationMiddle)
								stationModifiedId = append(stationModifiedId, endStationId)
							}
						}
					}
				}
			}
		}
	}

	//verify the map is goodly formed
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

	if badlyFormed {
		log.Fatal("graph has been badly formed, see error log for more details")
	}

	mapPointer.graph = graph
	mapPointer.isConvex = mapPointer.isConvexCalc()
}

// reattribute the ID for the stations and the lines
func (mapPointer *Map) attributeIDs() {
	for i := 0; i < len(mapPointer.stations); i++ {
		mapPointer.stations[i].id = i
	}
	for i := 0; i < len(mapPointer.lines); i++ {
		mapPointer.lines[i].id = i
	}
}

// calculate if the city is convex
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

// get the path needed to go from s1 to s2
func (mapPointer *Map) GetPathStation(s1 MetroStation, s2 MetroStation) (pathStation, error) {
	var sout pathStation
	if len(mapPointer.graph) > s1.id && len(mapPointer.graph) > s2.id {
		if s1.id == s2.id {
			return sout, errors.New("same id for the two station")
		}
		if mapPointer.graph[s1.id][s2.id] == nil {
			return sout, errors.New("no existing path between " + s1.name + "-" + s2.name)
		}
		return *mapPointer.graph[s1.id][s2.id], nil
	}

	return sout, errors.New("badly constructed graph")
}

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

//calculate the time to travel between two directly lined stations (no stations between them on a direct line)
func timeBetweenDirectStations(station1 *MetroStation, station2 *MetroStation) int {
	conf := configs.GetInstance()

	var accelerationTrain = conf.AccelerationTrain()                  //m.s-2
	var maxSpeedTrain = conf.MaxSpeedTrain()                          //m.s-1
	var distanceBetweenStations = int(station1.distanceTo(*station2)) //m

	var timeToMaxSpeed = maxSpeedTrain / accelerationTrain                                //s
	var maxDistanceAcceleration = accelerationTrain * timeToMaxSpeed * timeToMaxSpeed / 2 //m

	var output = -1

	if distanceBetweenStations > 2*maxDistanceAcceleration {
		output = int(2*timeToMaxSpeed + (distanceBetweenStations-2*maxDistanceAcceleration)/maxSpeedTrain) //s
	} else {
		output = int(2 * math.Sqrt(float64(2*(distanceBetweenStations/2)/accelerationTrain))) //checked for distanceBetweenStations == 2 * maxDistanceAcceleration
	}

	return output
}

//generate the time between each station using some maths
// T(a,b) = D(a,b) if Distance(a,b) *
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

//return the stations sorted by how near they are from the point.
func (mapPointer *Map) GetNearestStations(point Point) []*MetroStation {
	var output = make([]*MetroStation, len(mapPointer.stations))
	for i := range mapPointer.stations {
		output[i] = mapPointer.stations[i]
	}

	sort.Slice(output, func(i, j int) bool {
		return output[i].position.DistanceTo(point) < output[j].position.DistanceTo(point)
	})

	return output
}

func (mapPointer *Map) GetNearestStationOpened(point Point) *MetroStation {
	var nearestStations = mapPointer.GetNearestStations(point) //divert all passengers to the closest non-closed station
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

//when start->end cannot be done, try to find a path that can be done from the start to the closest point possible of end
func (mapPointer *Map) GetNewPathStationMiddleClose(start *MetroStation, end *MetroStation) *pathStation {

	if mapPointer.graph[start.id][end.id] != nil {
		return mapPointer.graph[start.id][end.id]
	}

	var distanceStartEnd = start.distanceTo(*end)

	var candidatesEnd []*MetroStation
	for i, middleS := range mapPointer.stations {
		if !middleS.StatusIsClosed() && //station opened
			middleS.id != start.id && //not start
			middleS.id != end.id && //not end
			middleS.distanceTo(*end) < distanceStartEnd && //this station doesn't put us farther than the original path
			mapPointer.graph[start.id][middleS.id] != nil { //we can actually go to the station
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
		return candidatesEnd[i].position.DistanceTo(end.position) < candidatesEnd[j].position.DistanceTo(end.position)
	})

	return mapPointer.graph[start.id][candidatesEnd[0].id]
}