/*
Package configs

File : config.go

Brief : This file contains the configuration object and its methods.
It also contains the functions to load the configuration from the config.json
file.

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

/*
ConfigurationObject is the struct of the configuration object.

Methods :
  - NameStationList() []string : the getter for the nameStationList attribute
  - NameLineList() []string : the getter for the nameLineList attribute
  - Lines() int : the getter for the lines attribute
  - InterchangeStations() int : the getter for the interchange stations
    attribute
  - Population() int : the getter for the population attribute
  - AccelerationTrain() int : the getter for the acceleration train attribute
  - MaxSpeedTrain() int : the getter for the max speed train attribute
  - Seed() int64 : the getter for the seed attribute
  - CsvHeaders() bool : the getter for the csv headers attribute
  - TrainsPerLine() int : the getter for the trains per line attribute
  - BusinessDayStart() string : the getter for the business day start attribute
  - BusinessDayEnd() string : the getter for the business day end attribute
  - PrintDebug() bool : the getter for the print debug attribute
  - Stations() int : the getter for the stations attribute
  - CapacityPerTrain() int : the getter for the capacity per train attribute
  - PopulationCommutersProportion() float64 : the getter for the population
    commuters proportion attribute
  - PopulationRandomProportion() float64 : the getter for the population random
    proportion attribute
  - PopulationAdultProportion() float64 : the getter for the population adult
    proportion attribute
  - PopulationSeniorProportion() float64 : the getter for the population senior
    proportion attribute
  - PopulationDisabledProportion() float64 : the getter for the population
    disabled proportion attribute
  - PopulationStudentProportion() float64 : the getter for the population
    student proportion attribute
  - PopulationChildrenProportion() float64 : the getter for the population
    children proportion attribute
  - MorningCommuteTime() time.Duration : the getter for the morning commute
    time attribute
  - EveningCommuteTime() time.Duration : the getter for the evening commute
    time attribute
  - CommutePeriodDuration() time.Duration : the getter for the commute period
    duration attribute
  - TimeStart() time.Time : the getter for the time start attribute
  - TimeEnd() time.Time : the getter for the time end attribute
  - TimeInStation() int : the getter for the time in station attribute
  - ChangeParam(paramName string, value interface{}) : the setter for the
    parameters
  - GetMaxTimeInStationPassenger() time.Duration : the getter for the max time
    in station passenger attribute
  - getAsInt(paramName string) int : a generic function to convert a parameter
    to int
  - getAsTime(paramName string) time.Time : a generic function to convert a
    parameter to time.Time
  - GetInstance() ConfigurationObject : the getter for the configuration object
  - Load(configPath, nameStationListPath, nameLineListPath string) : a function
    to load the configuration from the config.json file
  - Reload() : a function to reload the configuration from the config.json file
  - Check() (bool, error) : a function to check if the configuration is correct
*/
type ConfigurationObject map[string]interface{}

var config ConfigurationObject

var nameStationList []string

var nameLineList []string

/*
Getters & Setters
*/

/*
NameStationList is the getter for the nameStationList attribute.

Return :
  - []string : the list of the names of the stations
*/
func (c *ConfigurationObject) NameStationList() []string {
	return nameStationList
}

/*
NameLineList is the getter for the nameLineList attribute.

Return :
  - []string : the list of the names of the lines
*/
func (c *ConfigurationObject) NameLineList() []string {
	return nameLineList
}

/*
Lines is the getter for the lines attribute.

Return :
  - int : the number of lines
*/
func (c *ConfigurationObject) Lines() int {
	return c.getAsInt("lines")
}

/*
InterchangeStations is the getter for the interchange stations attribute.

Return :
  - int : the number of interchange stations
*/
func (c *ConfigurationObject) InterchangeStations() int {
	return c.getAsInt("interchange stations")
}

/*
Population is the getter for the population attribute.

Return :
  - int : the number of people in the simulation
*/
func (c *ConfigurationObject) Population() int {
	return c.getAsInt("population")
}

/*
AccelerationTrain is the getter for the acceleration train attribute.

Return :
  - int : the acceleration of the train
*/
func (c *ConfigurationObject) AccelerationTrain() int {
	return c.getAsInt("acceleration train")
}

/*
MaxSpeedTrain is the getter for the max speed train attribute.

Return :
  - int : the max speed of the train
*/
func (c *ConfigurationObject) MaxSpeedTrain() int {
	return c.getAsInt("max speed train")
}

/*
Seed is the getter for the seed attribute.

Return :
  - int64 : the seed of the simulation
*/
func (c *ConfigurationObject) Seed() int64 {
	return int64(config["seed"].(float64))
}

/*
CsvHeaders is the getter for the csv headers attribute.

Return :
  - bool : true if the csv headers are enabled, false otherwise
*/
func (c *ConfigurationObject) CsvHeaders() bool {
	return config["csv headers"].(bool)
}

/*
TrainsPerLine is the getter for the trains per line attribute.

Return :
  - int : the number of trains per line
*/
func (c *ConfigurationObject) TrainsPerLine() int {
	return c.getAsInt("trains per line")
}

/*
BusinessDayStart is the getter for the business day start attribute.

Return :
  - string : the time of the start of the business day
*/
func (c *ConfigurationObject) BusinessDayStart() string {
	return config["business day start"].(string)
}

/*
BusinessDayEnd is the getter for the business day end attribute.

Return :
  - string : the time of the end of the business day
*/
func (c *ConfigurationObject) BusinessDayEnd() string {
	return config["business day end"].(string)
}

/*
PrintDebug is the getter for the print debug attribute.

Return :
  - bool : true if the debug is enabled, false otherwise
*/
func (c ConfigurationObject) PrintDebug() bool {
	if val, ok := c["print debug"]; ok {
		return val.(bool)
	}
	return false
}

/*
PreTimetable is the getter for the pre timetable attribute.

Return :
  - bool : true if the pre timetable is enabled, false otherwise
*/
func (c ConfigurationObject) PreTimetable() bool {
	if val, ok := c["pre timetable"]; ok {
		return val.(bool)
	}
	return false
}

/*
Stations is the getter for the stations attribute.

Return :
  - int : the number of stations
*/
func (c *ConfigurationObject) Stations() int {
	return c.getAsInt("stations")
}

/*
CapacityPerTrain is the getter for the capacity per train attribute.

Return :
  - int : the capacity per train
*/
func (c *ConfigurationObject) CapacityPerTrain() int {
	return c.getAsInt("capacity per train")
}

/*
PopulationCommutersProportion is the getter for the population commuters
proportion attribute.

Return :
  - float64 : the proportion of commuters in the population
*/
func (c *ConfigurationObject) PopulationCommutersProportion() float64 {
	return config["population commuters proportion"].(float64)
}

/*
PopulationRandomProportion is the getter for the population random proportion
attribute.

Return :
  - float64 : the proportion of randoms in the population
*/
func (c *ConfigurationObject) PopulationRandomProportion() float64 {
	return config["population randoms proportion"].(float64)
}

/*
PopulationAdultProportion is the getter for the population adults proportion.

Return :
  - float64 : the proportion of adults in the population
*/
func (c *ConfigurationObject) PopulationAdultProportion() float64 {
	return config["population adults proportion"].(float64)
}

/*
PopulationSeniorProportion is the getter for the population senior proportion.

Return :
  - float64 : the proportion of seniors in the population
*/
func (c *ConfigurationObject) PopulationSeniorProportion() float64 {
	return config["population senior proportion"].(float64)
}

/*
PopulationDisabledProportion is the getter for the population disabled
proportion.

Return :
  - float64 : the proportion of disabled in the population
*/
func (c *ConfigurationObject) PopulationDisabledProportion() float64 {
	return config["population disabled proportion"].(float64)
}

/*
PopulationStudentProportion is the getter for the population student proportion.

Return :
  - float64 : the proportion of students in the population
*/
func (c *ConfigurationObject) PopulationStudentProportion() float64 {
	return config["population students proportion"].(float64)
}

/*
PopulationChildrenProportion is the getter for the population children
proportion.

Return :
  - float64 : the proportion of children in the population
*/
func (c *ConfigurationObject) PopulationChildrenProportion() float64 {
	return config["population children proportion"].(float64)
}

/*
MorningCommuteTime is the getter for the morning commute time attribute.

Return :
  - time.Duration : the duration of the morning commute
*/
func (c *ConfigurationObject) MorningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(config["morning commute "+
		"time"].(string))
	if err != nil {
		log.Fatal(err, "could not parse morning commute time")
	}
	return duration
}

/*
EveningCommuteTime is the getter for the evening commute time attribute.

Return :
  - time.Duration : the duration of the evening commute
*/
func (c *ConfigurationObject) EveningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(config["evening commute "+
		"time"].(string))
	if err != nil {
		log.Fatal(err, "could not parse evening commute time")
	}
	return duration
}

/*
CommutePeriodDuration is the getter for the commute period duration attribute.

Return :
  - time.Duration : the duration of the commute period
*/
func (c *ConfigurationObject) CommutePeriodDuration() time.Duration {
	duration, err := time.ParseDuration(config["commute period "+
		"duration"].(string))
	if err != nil {
		log.Fatal(err, "could not parse commute duration")
	}
	return duration
}

/*
TimeStart is the getter for the time start attribute.

Return :
  - time.Time : the time of the start of the simulation
*/
func (c *ConfigurationObject) TimeStart() time.Time { //TODO do that
	return c.getAsTime("start time")
}

/*
TimeEnd is the getter for the time end attribute.

Return :
  - time.Time : the time of the end of the simulation
*/
func (c *ConfigurationObject) TimeEnd() time.Time { //TODO do that
	return c.getAsTime("stop time")
}

/*
TimeInStation is the getter for the time in station attribute.

Return :
  - int : the time in station
*/
func (c *ConfigurationObject) TimeInStation() int {
	return c.getAsInt("time in station")
}

/*
ChangeParam is the setter for the parameters.

Param :
  - paramName string : the name of the parameter to change
  - value interface{} : the new value of the parameter
*/
func (c *ConfigurationObject) ChangeParam(paramName string, value interface{}) {
	config[paramName] = value
}

/*
GetMaxTimeInStationPassenger is the getter for the max time in station passenger
attribute.

Return :
  - time.Duration : the max time in station for a passenger
*/
func (c *ConfigurationObject) GetMaxTimeInStationPassenger() time.Duration {
	return time.Second * time.Duration(c.getAsInt("passenger max "+
		"waiting time before exit"))
}

/*
getAsInt is a generic function to convert a parameter to int.

Param :
  - paramName string : the name of the parameter to convert

Return :
  - int : the value of the parameter
*/
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

/*
getAsTime is a generic function to convert a parameter to time.Time.

Param :
  - paramName string : the name of the parameter to convert

Return :
  - time.Time : the value of the parameter
*/
func (c *ConfigurationObject) getAsTime(paramName string) time.Time {
	date, _ := time.Parse(time.RFC3339, config[paramName].(string))
	return date
}

/*
GetInstance is the getter for the configuration object.

Return :
  - ConfigurationObject : the instance of the configuration object
*/
func GetInstance() ConfigurationObject {
	if config == nil {
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

/*
Load is a function to load the configuration from the config.json file.

Param :
  - configPath string : the path to the config.json file
  - nameStationListPath string : the path to the nameStationList.json file
  - nameLineListPath string : the path to the nameLineList.json file
*/
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

	errJson := json.Unmarshal(configBytes, &config)
	if errJson != nil {
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

/*
Reload is a function to reload the configuration from the config.json file.
*/
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

/*
Check is a function to check if the configuration is correct.

Return :
  - bool : true if the configuration is correct, false otherwise
  - error : the error message if the configuration is not correct
*/
func (c *ConfigurationObject) Check() (bool, error) {
	var errMsg = ""
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
