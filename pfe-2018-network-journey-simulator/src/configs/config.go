/*
Package configs

File : config.go

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
	"encoding/json"
	"errors"
	"io"
	"log"
	"os"
	"path/filepath"
	"strings"
	"sync"
	"time"
)

// var basePath = os.Getenv("GOPATH")
var currentPath, _ = os.Getwd()
var basePath = strings.Replace(currentPath, "src\\main", "", -1)
var projectPath string = "src"

var configPath string
var stationsPath string
var linesPath string

type ConfigurationObject map[string]interface{}

var config ConfigurationObject

var nameStationList []string

var nameLineList []string

// Getters & Setters

func (c *ConfigurationObject) NameStationList() []string {
	return nameStationList
}

func (c *ConfigurationObject) NameLineList() []string {
	return nameLineList
}

func (c *ConfigurationObject) Lines() int {
	return c.getAsInt("lines")
}

func (c *ConfigurationObject) InterchangeStations() int {
	return c.getAsInt("interchange stations")
}

func (c *ConfigurationObject) Population() int {
	return c.getAsInt("population")
}

func (c *ConfigurationObject) AccelerationTrain() int {
	return c.getAsInt("acceleration train")
}

func (c *ConfigurationObject) MaxSpeedTrain() int {
	return c.getAsInt("max speed train")
}

func (c *ConfigurationObject) Seed() int64 {
	return int64(config["seed"].(float64))
}

func (c *ConfigurationObject) CsvHeaders() bool {
	return config["csv headers"].(bool)
}

func (c *ConfigurationObject) TrainsPerLine() int {
	return c.getAsInt("trains per line")
}

func (c *ConfigurationObject) BusinessDayStart() string {
	return string(config["business day start"].(string))
}

func (c *ConfigurationObject) BusinessDayEnd() string {
	return string(config["business day end"].(string))
}

func (c ConfigurationObject) PrintDebug() bool {
	if val, ok := c["print debug"]; ok {
		return val.(bool)
	}
	return false
}

func (c ConfigurationObject) PreTimetable() bool {
	if val, ok := c["pre timetable"]; ok {
		return val.(bool)
	}
	return false
}

func (c *ConfigurationObject) Stations() int {
	return c.getAsInt("stations")
}

func (c *ConfigurationObject) CapacityPerTrain() int {
	return c.getAsInt("capacity per train")
}

func (c *ConfigurationObject) PopulationCommutersProportion() float64 {
	return config["population commuters proportion"].(float64)
}

func (c *ConfigurationObject) PopulationRandomProportion() float64 {
	return config["population randoms proportion"].(float64)
}

func (c *ConfigurationObject) PopulationAdultProportion() float64 {
	return config["population adults proportion"].(float64)
}

func (c *ConfigurationObject) PopulationSeniorProportion() float64 {
	return config["population senior proportion"].(float64)
}

func (c *ConfigurationObject) PopulationDisabledProportion() float64 {
	return config["population disabled proportion"].(float64)
}

func (c *ConfigurationObject) PopulationStudentProportion() float64 {
	return config["population students proportion"].(float64)
}

func (c *ConfigurationObject) PopulationChildrenProportion() float64 {
	return config["population children proportion"].(float64)
}

func (c *ConfigurationObject) MorningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(string(config["morning commute "+
		"time"].(string)))
	if err != nil {
		log.Fatal(err, "could not parse morning commute time")
	}
	return duration
}

func (c *ConfigurationObject) EveningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(string(config["evening commute "+
		"time"].(string)))
	if err != nil {
		log.Fatal(err, "could not parse evening commute time")
	}
	return duration
}

func (c *ConfigurationObject) CommutePeriodDuration() time.Duration {
	duration, err := time.ParseDuration(string(config["commute period "+
		"duration"].(string)))
	if err != nil {
		log.Fatal(err, "could not parse commute duration")
	}
	return duration
}

func (c *ConfigurationObject) TimeStart() time.Time { //TODO do that
	return c.getAsTime("start time")
}

func (c *ConfigurationObject) TimeEnd() time.Time { //TODO do that
	return c.getAsTime("stop time")
}

func (c *ConfigurationObject) TimeInStation() int {
	return c.getAsInt("time in station")
}

func (c *ConfigurationObject) ChangeParam(paramName string, value interface{}) {
	config[paramName] = value
}

func (c *ConfigurationObject) GetMaxTimeInStationPassenger() time.Duration {
	return time.Second * time.Duration(c.getAsInt("passenger max "+
		"waiting time before exit"))
}

// functions & methods
func (c *ConfigurationObject) getAsInt(paramName string) int {
	var param = config[paramName]
	var _, ok = param.(float64)
	if ok {
		return int(config[paramName].(float64))
	}
	_, ok = param.(int)
	if ok {
		return config[paramName].(int)
	}
	log.Fatal("tried to convert non-int value to int", paramName, param)
	return -1
}

func (c *ConfigurationObject) getAsTime(paramName string) time.Time {
	date, _ := time.Parse(time.RFC3339, config[paramName].(string))
	return date
}

func GetInstance() ConfigurationObject {
	if config == nil {
		println("Here 1")
		var mutex = &sync.Mutex{}
		mutex.Lock()
		basePath = strings.Replace(basePath, "src\\configs", "", -1)
		basePath = strings.Replace(basePath, "src\\models", "", -1)
		basePath = strings.Replace(basePath, "src\\simulator", "", -1)
		basePath = strings.Replace(basePath, "src\\tools", "", -1)
		if strings.Contains(basePath, "src") {
			configPath = filepath.Join(basePath, "/configs/config.json")
			stationsPath = filepath.Join(basePath, "/configs/nameStationList.json")
			linesPath = filepath.Join(basePath, "/configs/nameLineList.json")
		} else if strings.Contains(basePath, "configs") {
			configPath = filepath.Join(basePath, "/config.json")
			stationsPath = filepath.Join(basePath, "/nameStationList.json")
			linesPath = filepath.Join(basePath, "/nameLineList.json")
		} else {
			configPath = filepath.Join(basePath, "src/configs/config.json")
			stationsPath = filepath.Join(basePath, "src/configs/nameStationList.json")
			linesPath = filepath.Join(basePath, "src/configs/nameLineList.json")
		}
		println(configPath)

		Load(configPath, stationsPath, linesPath)

		mutex.Unlock()

		correct, err := config.Check()
		if !correct {
			log.Fatal(err, "error while verifying config")
			return nil
		}
	}

	return config
}

func Load(configPath, nameStationListPath, nameLineListPath string) {

	println(configPath)
	configFile, err := os.Open(configPath)
	if err != nil {
		log.Fatal(err, "error while reading config (0) - config open failed")
	}
	configBytes, err := io.ReadAll(configFile)
	if err != nil {
		log.Fatal(err, "error while reading config (1) - config read failed")
	}

	errjson := json.Unmarshal(configBytes, &config)
	if errjson != nil {
		log.Fatal(err, "error while reading config (2) - json from config failed")
	}
	nameStationListFile, err := os.Open(nameStationListPath)
	if err != nil {
		log.Fatal(err, "error while reading config (3) - nameStation open failed")
	}
	nameStationListBytes, err := io.ReadAll(nameStationListFile)
	errUnmarshal := json.Unmarshal(nameStationListBytes, &nameStationList)
	if errUnmarshal != nil {
		return
	}

	nameLineListFile, err := os.Open(nameLineListPath)
	if err != nil {
		log.Fatal(err, "error while reading config (4) - nameLineListPath "+
			"open failed")
	}
	nameLineListBytes, err := io.ReadAll(nameLineListFile)
	errUnmarshal = json.Unmarshal(nameLineListBytes, &nameLineList)
	if errUnmarshal != nil {
		return
	}
}

func (c *ConfigurationObject) Reload() {
	if strings.Contains(basePath, "src") {
		configPath = filepath.Join(basePath, "/configs/config.json")
	} else if strings.Contains(basePath, "configs") {
		configPath = filepath.Join(basePath, "/config.json")
	}
	println(configPath)
	stationsPath = filepath.Join(basePath, "src/configs/nameStationList.json")
	linesPath = filepath.Join(basePath, "src/configs/nameLineList.json")

	Load(configPath, stationsPath, linesPath)
}

func (c *ConfigurationObject) Check() (bool, error) {
	var errMsg string = ""
	isCorrect := true

	if config.InterchangeStations() > config.Stations() {
		errMsg = errMsg + " There are more interchanges than stations"
		isCorrect = false
	}

	if config.Lines() > len(config.NameLineList()) {
		errMsg = errMsg + " Not enough names for lines"
		isCorrect = false
	}

	if config.Stations() > len(config.NameStationList()) {
		errMsg = errMsg + " Not enough names for stations"
		isCorrect = false
	}

	if config.TrainsPerLine() < 1 {
		errMsg = errMsg + " There is zero train per lines"
		isCorrect = false
	}

	if (config.PopulationRandomProportion() +
		config.PopulationCommutersProportion()) != 1 {
		errMsg = errMsg + "The population proportions should be equal to one"
		isCorrect = false
	}

	return isCorrect, errors.New(errMsg)

}
