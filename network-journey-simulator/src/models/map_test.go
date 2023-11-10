/*
File : map_test.go

Brief : map_test.go runs tests on the map.go file.

Date : 24/01/2019

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

import (
	"fmt"
	"log"
	"network-journey-simulator/src/configs"
	"testing"

	"github.com/stretchr/testify/assert"
)

/*
TestMap tests the Map struct and its methods.

# It tests if the struct and its methods work properly

Input : t *testing.T

Expected : The struct and its methods work properly
*/
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

/*
generateMapSimpleLine test the generation of a city with 3 stations and one line.

Expected : The city is generated properly
*/
func generateMapSimpleLine() Map {

	clearStationsTest()
	clearLinesTest()

	m := Map{
		stations: []*MetroStation{
			&stationsTest[0],
			&stationsTest[1],
			&stationsTest[2],
		},
	}

	m.lines = append(m.lines, &linesTest[0])

	for i := 0; i < len(m.stations); i++ {
		m.stations[i].AddMetroLine(m.lines[0])
		m.lines[0].AddMetroStation(m.stations[i])
	}

	return m
}

/*
generateMapTwoLine test the generation of a city with 3 stations and two lines.

Expected : The city is generated properly
*/
func generateMapTwoLine() Map {
	clearStationsTest()
	clearLinesTest()
	m := Map{
		stations: []*MetroStation{
			&stationsTest[0],
			&stationsTest[1],
			&stationsTest[2],
		},
	}

	m.lines = append(m.lines, &linesTest[0], &linesTest[1])

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

/*
TestMap_GenerateGraph tests the generation of the graph of the map.

# It tests if the graph is generated properly

Input : m Map

Expected : The graph is generated properly
*/
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

/*
TestMap_GenerateGraph tests the generation of the graph of the map.

# It tests if the graph is generated properly

Input : t *testing.T

Expected : The graph is generated properly
*/
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

/*
TestMap_GenerateGraphComplex tests the generation of the graph of the map.

# It tests if the graph is generated properly

Input : t *testing.T

Expected : The graph is generated properly
*/
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

/*
TestMap_ExportMapToAdConfig tests the export of a map to an AdConfig.

# It tests if the export is done properly

Input : t *testing.T

Expected : The export is done properly
*/
func TestMap_ExportMapToAdConfig(t *testing.T) {
	var mapObject, err = CreateMap()
	if err != nil {
		log.Fatal("failed to create map", err)
	}

	adConfig := mapObject.ExportMapToAdConfig()

	println("writing...")
	assert.Nil(t, adConfig.SaveXML("test.xml"))
}

/*
TestAddDelay tests the AddDelay method of the Map struct.

# It tests if the delay is added properly

Input : t *testing.T

Expected : The delay is added properly
*/
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

/*
TestMap_GetNearestStations tests the GetNearestStations method of the Map struct.

# It tests if the nearest stations are returned properly

Input : t *testing.T

Expected : The nearest stations are returned properly
*/
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
