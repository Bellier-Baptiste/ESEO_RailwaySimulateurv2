package configs

import (
	"encoding/xml"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"path/filepath"
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
	Position struct {
		Latitude  float64 `xml:"latitude"`
		Longitude float64 `xml:"longitude"`
	} `xml:"position"`
	Lines []struct {
		Id       int    `xml:"id,attr"`
		Platform string `xml:"platform,attr"`
	} `xml:"lines>line"`
}

type ConfigLine struct {
	XMLName       xml.Name `xml:"line"`
	Id            int      `xml:"id"`
	Name          string   `xml:"name"`
	NumberOfTrain int      `xml:"numberOfTrain"`
	Stations      []struct {
		Id    int `xml:"id,attr"`
		Order int `xml:"order,attr"`
	} `xml:"stations>station"`
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

	advancedConfig.ReattributeIds()
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

func (aConfig *AdvancedConfig) loadXML(filename string) error {
	// open XML file
	configPath = filepath.Join(basePath, projectPath, "configs/", filename)
	xmlFile, err := os.Open(configPath)
	if err != nil {
		fmt.Println(err)
		return err
	}
	defer xmlFile.Close()

	// read XML File as aConfig byte array.
	byteValue, _ := ioutil.ReadAll(xmlFile)

	var mapC ConfigMap
	err = xml.Unmarshal(byteValue, &mapC)

	if err != nil {
		fmt.Println(err)
		return err
	}

	aConfig.MapC = mapC

	return nil
}

// verify that the relations (stations <-> lines) are good, e.g. they don't refer to a non-existing id
func (aConfig *AdvancedConfig) CheckRelations() error { //TODO verify line € station <=> station € line
	for i, station := range aConfig.MapC.Stations {
		//verify only one station with this id
		for j, station2 := range aConfig.MapC.Stations {
			if i == j {
				continue
			}
			if station.Id == station2.Id {
				return fmt.Errorf("AdvancedConfig : same id for two stations : %d", station.Id)
			}
		}
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
				return fmt.Errorf("AdvancedConfig : line %d referenced in station %d but not found in Lines", line.Id, station.Id)
			}

			//check for double
			for b, line2 := range station.Lines {
				if a == b {
					continue
				}
				if line.Id == line2.Id {
					return fmt.Errorf("AdvancedConfig : line %d referenced more than once in station %d", line.Id, station.Id)
				}
			}
		}
	}

	for i, line := range aConfig.MapC.Lines {
		//verify only one line with this id
		for j, line2 := range aConfig.MapC.Lines {
			if i == j {
				continue
			}
			if line.Id == line2.Id {
				return fmt.Errorf("AdvancedConfig : same id for two lines : %d", line.Id)
			}
		}
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
				return fmt.Errorf("AdvancedConfig : station %d referenced in line %d but not found in Lines", station.Id, line.Id)
			}
			// check for double
			for b, station2 := range line.Stations {
				if a == b {
					continue
				}
				if station.Id == station2.Id {
					return fmt.Errorf("AdvancedConfig : station %d referenced more than once in line %d", station.Id, line.Id)
				}
			}
		}
	}

	return nil
}

// reattribute IDs so they all start at 0 and don't have any missing number. we do this because the run part uses the Ids to search in arrays, but it's a hassle for the user to check in the xml
func (aConfig *AdvancedConfig) ReattributeIds() {
	//--- Stations
	//gives an impossible ID to rearrange them (e.g. avoid to affect twice the same id)
	for a, station := range aConfig.MapC.Stations {
		var previousId = station.Id
		var newId = -9678 - a
		aConfig.MapC.Stations[a].Id = newId
		for i := range aConfig.MapC.Lines {
			for j := range aConfig.MapC.Lines[i].Stations {
				if aConfig.MapC.Lines[i].Stations[j].Id == previousId {
					aConfig.MapC.Lines[i].Stations[j].Id = newId
				}
			}
		}
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
	//gives the good id
	for a, station := range aConfig.MapC.Stations {
		var previousId = station.Id
		var newId = a
		aConfig.MapC.Stations[a].Id = newId
		for i := range aConfig.MapC.Lines {
			for j := range aConfig.MapC.Lines[i].Stations {
				if aConfig.MapC.Lines[i].Stations[j].Id == previousId {
					aConfig.MapC.Lines[i].Stations[j].Id = newId
				}
			}
		}
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

	//--- Lines
	//gives an impossible ID to rearrange them (e.g. avoid to affect twice the same id)
	for a, line := range aConfig.MapC.Lines {
		var previousId = line.Id
		var newId = -9678 - a
		aConfig.MapC.Lines[a].Id = newId
		for i := range aConfig.MapC.Stations {
			for j := range aConfig.MapC.Stations[i].Lines {
				if aConfig.MapC.Stations[i].Lines[j].Id == previousId {
					aConfig.MapC.Stations[i].Lines[j].Id = newId
				}
			}
		}
	}
	//gives the good id
	for a, line := range aConfig.MapC.Lines {
		var previousId = line.Id
		var newId = a
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

func (aConfig AdvancedConfig) SaveXML(path string) error {

	var pathToFile = filepath.Join(basePath, projectPath, "configs/", path)

	if _, err := os.Stat(pathToFile); !os.IsNotExist(err) {
		// pathToFile exists
		err = os.Remove(pathToFile)
		if err != nil {
			println("couldn't remove ", pathToFile, " - continuing")
		}
	}

	var output, _ = xml.MarshalIndent(aConfig.MapC, "", "  ")
	var err = ioutil.WriteFile(pathToFile, output, 777)

	if err != nil {
		log.Fatal("error while writing xml : ", err)
	}

	return nil
}
