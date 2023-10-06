package models

import (
	"fmt"
	"log"
	"pfe-2018-network-journey-simulator/src/configs"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestMap(t *testing.T) {

	theMap, err := CreateMap()
	if err != nil {
		return
	}

	mapPointer := &theMap

	fmt.Println(computeNextPosition(0, 0, 2))
	fmt.Println(computeNextPosition(0, 0, 6))

	fmt.Println(mapPointer.Stations())
	fmt.Println(mapPointer.Lines())

	//fmt.Println(mapPointer.Lines()[1].stations[2])

	assert.True(t, theMap.isConvex)

}

// Generate a city with 3 stations and one line
func generateMapSimpleLine() Map {

	clearStationsTest()
	clearLinesTest()

	m := Map{
		stations: []*MetroStation{
			&stations_test[0],
			&stations_test[1],
			&stations_test[2],
		},
	}

	m.lines = append(m.lines, &lines_test[0])

	for i := 0; i < len(m.stations); i++ {
		m.stations[i].AddMetroLine(m.lines[0])
		m.lines[0].AddMetroStation(m.stations[i])
	}

	return m
}

func generateMapTwoLine() Map {
	clearStationsTest()
	clearLinesTest()
	m := Map{
		stations: []*MetroStation{
			&stations_test[0],
			&stations_test[1],
			&stations_test[2],
		},
	}

	m.lines = append(m.lines, &lines_test[0], &lines_test[1])

	// 1 <-A-> 2
	m.stations[0].AddMetroLine(m.lines[0])
	m.lines[0].AddMetroStation(m.stations[0])
	m.stations[1].AddMetroLine(m.lines[0])
	m.lines[0].AddMetroStation(m.stations[1])
	//1 <-B->3
	m.stations[0].AddMetroLine(m.lines[1])
	m.lines[1].AddMetroStation(m.stations[0])
	m.stations[2].AddMetroLine(m.lines[1])
	m.lines[1].AddMetroStation(m.stations[2])

	return m
}

func map_printGraph(m Map) {
	var g = m.graph
	if g == nil {
		return
	}
	for i := 0; i < len(g); i++ {
		print("[ ")
		for j := 0; j < len(g[i]); j++ {
			if g[i][j] == nil {
				print(" ø ")
			} else {
				print(g[i][j].toString())
			}
			if j != len(g[i])-1 {
				print(", ")
			}
		}
		println("]")
	}
}

func TestMap_GenerateGraphDirectLine(t *testing.T) {
	m := generateMapSimpleLine()

	m.GenerateGraph()

	assert.Equal(t, 3, len(m.graph))
	assert.Equal(t, 3, len(m.graph[0]))

	map_printGraph(m)
	//all stations can be reached
	assert.True(t, m.isConvexCalc())

	//the size seems coherent
	assert.Nil(t, m.graph[0][0])
	assert.Equal(t, 2, len(m.graph[0][1].stations))
	assert.Equal(t, 3, len(m.graph[0][2].stations))
	assert.Equal(t, 2, len(m.graph[1][0].stations))
	assert.Nil(t, m.graph[1][1])
	assert.Equal(t, 2, len(m.graph[1][2].stations))
	assert.Equal(t, 3, len(m.graph[2][0].stations))
	assert.Equal(t, 2, len(m.graph[2][1].stations))
	assert.Nil(t, m.graph[2][2])
}

func TestMap_GenerateGraphComplex(t *testing.T) {
	clearLinesTest()

	var config = configs.GetInstance()
	config.Reload()

	m := generateMapTwoLine()

	m.GenerateGraph()

	assert.Equal(t, 3, len(m.graph))
	assert.Equal(t, 3, len(m.graph[0]))

	map_printGraph(m)
	//all stations can be reached
	assert.True(t, m.isConvexCalc())

	//the size seems coherent
	assert.Nil(t, m.graph[0][0])
	assert.Equal(t, 2, len(m.graph[0][1].stations)) // 0->1 : (0,1)
	assert.Equal(t, 2, len(m.graph[0][2].stations)) // 0->2 : (0,2)
	assert.Equal(t, 2, len(m.graph[1][0].stations)) // 1->0 : (1,0)
	assert.Nil(t, m.graph[1][1])
	assert.Equal(t, 3, len(m.graph[1][2].stations)) // 1->2 : (1,0,2)
	assert.Equal(t, 2, len(m.graph[2][0].stations)) // 2->0 : (2,0)
	assert.Equal(t, 3, len(m.graph[2][1].stations)) // 2->1 : (2,0,1)
	assert.Nil(t, m.graph[2][2])
}

func TestMap_ExportMapToAdConfig(t *testing.T) {
	var mapObject, err = CreateMap()
	if err != nil {
		log.Fatal("failed to create map", err)
	}

	adConfig := mapObject.ExportMapToAdConfig()

	println("writing...")
	assert.Nil(t, adConfig.SaveXML("test.xml"))
}

func TestAddDelay(t *testing.T) {
	//create a map
	var adConfig = *configs.GetAdvancedConfigInstance()
	theMap := CreateMapAdvanced(adConfig)

	println("number of stations : ", len(theMap.stations))
	println("size of delay graph : ", len(theMap.graphDelay))
	println("size2 of delay graph : ", len(theMap.graphDelay[0]))

	theMap.AddDelay(0, 2, 500)

	assert.Equal(t, 500, theMap.GraphDelay()[0][2], "time between 0 and 2 should be 500")
}

func TestMap_GetNearestStations(t *testing.T) {
	theMap, _ := CreateMap()

	var point = Point{x: 0, y: 0}

	var output = theMap.GetNearestStations(point)
	var minDist = -1.0

	assert.True(t, len(theMap.stations) > 2)

	for _, station := range output {
		assert.True(t, station.position.DistanceTo(point) >= minDist)
		minDist = station.position.DistanceTo(point)
	}

}
