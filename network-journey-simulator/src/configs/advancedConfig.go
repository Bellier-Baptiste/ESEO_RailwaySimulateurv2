/*
Package configs

File : advancedConfig.go

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
package configs

import (
	"encoding/xml"
	"fmt"
	"io"
	"log"
	"os"
	"path/filepath"
	"strings"
)

type ConfigMap struct {
	XMLName              xml.Name                    `xml:"map"`
	Stations             []ConfigStation             `xml:"stations>station"`
	Lines                []ConfigLine                `xml:"lines>line"`
	EventsStationClosed  []ConfigStationClosedEvent  `xml:"events>stationClosed"`
	EventsLineDelay      []ConfigLineDelayEvent      `xml:"events>lineDelay"`
	EventsLineClosed     []ConfigLineClosedEvent     `xml:"events>lineClosed"`
	EventsAttendancePeak []ConfigAttendancePeakEvent `xml:"events>attendancePeak"`
}

type ConfigStation struct {
	XMLName  xml.Name `xml:"station"`
	Id       int      `xml:"id"`
	Name     string   `xml:"name"`
	Position Pos      `xml:"position"`
	Lines    []Line   `xml:"lines>line"`
}

type Pos struct {
	Latitude  float64 `xml:"latitude"`
	Longitude float64 `xml:"longitude"`
}

type Line struct {
	Id       int    `xml:"id,attr"`
	Platform string `xml:"platform,attr"`
}

type ConfigLine struct {
	XMLName       xml.Name  `xml:"line"`
	Id            int       `xml:"id"`
	Name          string    `xml:"name"`
	NumberOfTrain int       `xml:"numberOfTrain"`
	Stations      []Station `xml:"stations>station"`
}

type Station struct {
	Id    int `xml:"id,attr"`
	Order int `xml:"order,attr"`
}

type ConfigStationClosedEvent struct {
	XMLName     xml.Name `xml:"stationClosed"`
	StartString string   `xml:"start"`
	EndString   string   `xml:"end"`
	IdStation   int      `xml:"idStation"`
}

type ConfigLineDelayEvent struct {
	XMLName        xml.Name `xml:"lineDelay"`
	StartString    string   `xml:"start"`
	EndString      string   `xml:"end"`
	StationIdStart int      `xml:"stationIdStart"`
	StationIdEnd   int      `xml:"stationIdEnd"`
	Delay          int      `xml:"delay"`
}

type ConfigLineClosedEvent struct {
	XMLName        xml.Name `xml:"lineClosed"`
	StartString    string   `xml:"start"`
	EndString      string   `xml:"end"`
	StationIdStart int      `xml:"stationIdStart"`
	StationIdEnd   int      `xml:"stationIdEnd"`
}

type ConfigAttendancePeakEvent struct {
	XMLName    xml.Name `xml:"attendancePeak"`
	TimeString string   `xml:"time"`
	StationId  int      `xml:"stationId"`
	Size       int      `xml:"size"`
}

type AdvancedConfig struct {
	MapC ConfigMap
}

var advancedConfig *AdvancedConfig

func InitAdvancedConfigInstance(filename string) {
	advancedConfig = &AdvancedConfig{}
	var err = advancedConfig.loadXML(filename)
	if err != nil {
		log.Fatal("error in advanced config loading - aborting run")
	}
	err = advancedConfig.CheckRelations()
	if err != nil {
		log.Fatal("error in advanced config checking - ", err)
	}

	println("Here")
	advancedConfig.ReattributeIds()
	println("Here")
}

func GetAdvancedConfigInstance() *AdvancedConfig {
	if advancedConfig == nil {
		InitAdvancedConfigInstance("map_config.xml")
	}

	return advancedConfig
}

// return the total number of trains
func (aConfig *AdvancedConfig) NumberOfTrains() int {
	var output = 0

	for _, line := range aConfig.MapC.Lines {
		output += line.NumberOfTrain
	}

	return output
}

func closeConfigFile(xmlFile *os.File) error {
	err := xmlFile.Close()
	return err
}

/*
*
loadXML
Open the mag configuration file that will be used.
This file is a xml.
*/
func (aConfig *AdvancedConfig) loadXML(filename string) error {
	// open XML file
	fmt.Println("loading advanced config...")
	fmt.Println("base path : ", basePath)
	fmt.Println("current path : ", currentPath)
	fmt.Println("base path : ", basePath)
	if basePath == currentPath && strings.Contains(basePath, "src") &&
		!strings.Contains(basePath, "configs") {
		configPath = filepath.Join(basePath, "/configs/", filename)
	} else if strings.Contains(basePath, "configs") {
		configPath = filepath.Join(basePath, filename)
	} else {
		configPath = filepath.Join(basePath, projectPath, "configs/", filename)
	}

	fmt.Println("config path : ", configPath)
	xmlFile, err := os.Open(configPath)
	if err != nil {
		fmt.Println(err)
		return err
	}
	defer func(xmlFile *os.File) {
		err := closeConfigFile(xmlFile)
		if err != nil {
			fmt.Println(err)
		}
	}(xmlFile)

	// read XML File as aConfig byte array.
	byteValue, _ := io.ReadAll(xmlFile)

	var mapC ConfigMap
	err = xml.Unmarshal(byteValue, &mapC)

	if err != nil {
		fmt.Println(err)
		return err
	}

	aConfig.MapC = mapC

	return nil
}

func (aConfig *AdvancedConfig) CheckRelationsStationsId(i int,
	station ConfigStation) error {
	for j, station2 := range aConfig.MapC.Stations {
		if i == j {
			continue
		}
		if station.Id == station2.Id {
			return fmt.Errorf("AdvancedConfig : same id for two "+
				"stations : %d", station.Id)
		}
	}
	return nil
}

func (aConfig *AdvancedConfig) CheckRelationsStationsLinesDouble(a int,
	line Line, station ConfigStation) error {
	for b, line2 := range station.Lines {
		if a == b {
			continue
		}
		if line.Id == line2.Id {
			return fmt.Errorf("AdvancedConfig : line %d "+
				"referenced more than once in station %d",
				line.Id, station.Id)
		}
	}
	return nil
}

func (aConfig *AdvancedConfig) CheckRelationsStations() error {
	var err error = nil
	for i, station := range aConfig.MapC.Stations {
		//verify that only one station with this id exist
		err = aConfig.CheckRelationsStationsId(i, station)
		//verify all lines from station exists and aren't in double
		for a, line := range station.Lines {
			var found = false
			for _, line2 := range aConfig.MapC.Lines {
				if line2.Id == line.Id {
					found = true
					break
				}
			}
			if !found {
				return fmt.Errorf("AdvancedConfig : line %d referenced "+
					"in station %d but not found in Lines", line.Id, station.Id)
			}

			//check for double
			err = aConfig.CheckRelationsStationsLinesDouble(a, line, station)
		}
	}
	return err
}

func (aConfig *AdvancedConfig) CheckRelationsLinesId(i int,
	line ConfigLine) error {
	for j, line2 := range aConfig.MapC.Lines {
		if i == j {
			continue
		}
		if line.Id == line2.Id {
			return fmt.Errorf("AdvancedConfig : same id for two "+
				"lines : %d", line.Id)
		}
	}
	return nil
}

func (aConfig *AdvancedConfig) CheckRelationsLinesStationsDouble(a int,
	station Station, line ConfigLine) error {
	for b, station2 := range line.Stations {
		if a == b {
			continue
		}
		if station.Id == station2.Id {
			return fmt.Errorf("AdvancedConfig : station %d "+
				"referenced more than once in line %d",
				station.Id, line.Id)
		}
	}
	return nil
}

func (aConfig *AdvancedConfig) CheckRelationsLines() error {
	var err error = nil
	for i, line := range aConfig.MapC.Lines {
		//verify only one line with this id
		err = aConfig.CheckRelationsLinesId(i, line)
		//verify all stations from line exists and aren't in double
		for a, station := range line.Stations {
			var found = false
			for _, station2 := range aConfig.MapC.Stations {
				if station.Id == station2.Id {
					found = true
					break
				}
			}
			if !found {
				return fmt.Errorf("AdvancedConfig : station %d "+
					"referenced in line %d but not found in Lines",
					station.Id, line.Id)
			}
			// check for double
			err = aConfig.CheckRelationsLinesStationsDouble(a, station, line)
		}
	}
	return err
}

/*
CheckRelations verify that the relations (stations <-> lines) are good, e.g.
they don't refer to a non-existing id

Param :
aConfig *AdvancedConfig

Return :
err error
*/
func (aConfig *AdvancedConfig) CheckRelations() error {
	//TODO verify line € station <=> station € line
	var err error = nil

	err = aConfig.CheckRelationsStations()
	if err != nil {
		fmt.Println(err)
	}

	err = aConfig.CheckRelationsLines()
	if err != nil {
		fmt.Println(err)
	}
	return err
}

/*
ReattributeIdsStationGoodEvents

IDs so they all start at 0 and don't have any missing number.
we do this because the run part uses the Ids to search in arrays, but it's a
hassle for the user to check in the xml
*/
func (aConfig *AdvancedConfig) ReattributeIdsStationGoodEvents(previousId,
	newId int) {
	for i := range aConfig.MapC.EventsStationClosed {
		if aConfig.MapC.EventsStationClosed[i].IdStation == previousId {
			aConfig.MapC.EventsStationClosed[i].IdStation = newId
		}
	}
	for i := range aConfig.MapC.EventsLineDelay {
		if aConfig.MapC.EventsLineDelay[i].StationIdEnd == previousId {
			aConfig.MapC.EventsLineDelay[i].StationIdEnd = previousId
		}
		if aConfig.MapC.EventsLineDelay[i].StationIdStart == previousId {
			aConfig.MapC.EventsLineDelay[i].StationIdStart = previousId
		}
	}
}

func (aConfig *AdvancedConfig) ReattributeIdsStationGood(init bool) {
	for a, station := range aConfig.MapC.Stations {
		var previousId = station.Id
		var newId = -9678 - a
		if !init {
			newId = a
		}
		aConfig.MapC.Stations[a].Id = newId
		for i := range aConfig.MapC.Lines {
			for j := range aConfig.MapC.Lines[i].Stations {
				if aConfig.MapC.Lines[i].Stations[j].Id == previousId {
					aConfig.MapC.Lines[i].Stations[j].Id = newId
				}
			}
		}
		aConfig.ReattributeIdsStationGoodEvents(previousId, newId)
	}
}

func (aConfig *AdvancedConfig) ReattributeIdsLineGood(init bool) {
	for a, line := range aConfig.MapC.Lines {
		var previousId = line.Id
		var newId = -9678 - a
		if !init {
			newId = a
		}
		aConfig.MapC.Lines[a].Id = newId
		for i := range aConfig.MapC.Stations {
			for j := range aConfig.MapC.Stations[i].Lines {
				if aConfig.MapC.Stations[i].Lines[j].Id == previousId {
					aConfig.MapC.Stations[i].Lines[j].Id = newId
				}
			}
		}
	}
}

func (aConfig *AdvancedConfig) ReattributeIds() {
	/*
		--- Stations
		Gives an impossible ID to rearrange them
		(e.g. avoid to affect twice the same id)
	*/
	aConfig.ReattributeIdsStationGood(true)
	//gives the good id
	aConfig.ReattributeIdsStationGood(false)

	/*
		---- Lines
		gives an impossible ID to rearrange them (e.g. avoid to affect twice the
		same id)
	*/
	aConfig.ReattributeIdsLineGood(true)
	//gives the good id
	aConfig.ReattributeIdsLineGood(false)
}

func (aConfig *AdvancedConfig) SaveXML(path string) error {
	basePath = strings.Replace(currentPath, "src\\models", "", -1)

	var pathToFile = filepath.Join(basePath, projectPath, "configs/", path)

	if _, err := os.Stat(pathToFile); !os.IsNotExist(err) {
		// pathToFile exists
		err = os.Remove(pathToFile)
		if err != nil {
			println("couldn't remove ", pathToFile, " - continuing")
		}
	}

	var output, _ = xml.MarshalIndent(aConfig.MapC, "", "  ")
	var err = os.WriteFile(pathToFile, output, 777)

	if err != nil {
		log.Fatal("error while writing xml : ", err)
	}

	return nil
}
