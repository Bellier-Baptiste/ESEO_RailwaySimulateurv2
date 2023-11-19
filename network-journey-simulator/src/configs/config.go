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
  - Paul TRÉMOUREUX

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
ConfigurationObject is the interface of the configuration object.

Methods :
  - Get(key string) interface{} : the getter for the parameters
  - Set(key string, value interface{}) : the setter for the parameters
*/
type ConfigurationObject interface {
	Get(key string) interface{}
	Set(key string, value interface{})
}

/*
ConfigurationType is the type of the configuration object.

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
type ConfigurationType struct {
	data map[string]interface{}
}

var nameStationList []string

var nameLineList []string

func (c *ConfigurationType) Get(key string) interface{} {
	return c.data[key]
}

func (c *ConfigurationType) Set(key string, value interface{}) {
	c.data[key] = value
}

/*
Getters & Setters
*/

/*
NameStationList is the getter for the nameStationList attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - []string : the list of the names of the stations
*/
func (c *ConfigurationType) NameStationList() []string {
	return nameStationList
}

/*
NameLineList is the getter for the nameLineList attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - []string : the list of the names of the lines
*/
func (c *ConfigurationType) NameLineList() []string {
	return nameLineList
}

/*
Lines is the getter for the lines attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the number of lines
*/
func (c *ConfigurationType) Lines() int {
	return c.getAsInt("lines")
}

/*
InterchangeStations is the getter for the interchange stations attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the number of interchange stations
*/
func (c *ConfigurationType) InterchangeStations() int {
	return c.getAsInt("interchange stations")
}

/*
Population is the getter for the population attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the number of people in the simulation
*/
func (c *ConfigurationType) Population() int {
	return c.getAsInt("population")
}

/*
AccelerationTrain is the getter for the acceleration train attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the acceleration of the train
*/
func (c *ConfigurationType) AccelerationTrain() int {
	return c.getAsInt("acceleration train")
}

/*
MaxSpeedTrain is the getter for the max speed train attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the max speed of the train
*/
func (c *ConfigurationType) MaxSpeedTrain() int {
	return c.getAsInt("max speed train")
}

/*
Seed is the getter for the seed attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int64 : the seed of the simulation
*/
func (c *ConfigurationType) Seed() int64 {
	return int64(c.Get("seed").(float64))
}

/*
CsvHeaders is the getter for the csv headers attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - bool : true if the csv headers are enabled, false otherwise
*/
func (c *ConfigurationType) CsvHeaders() bool {
	return c.Get("csv headers").(bool)
}

/*
TrainsPerLine is the getter for the trains per line attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the number of trains per line
*/
func (c *ConfigurationType) TrainsPerLine() int {
	return c.getAsInt("trains per line")
}

/*
BusinessDayStart is the getter for the business day start attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - string : the time of the start of the business day
*/
func (c *ConfigurationType) BusinessDayStart() string {
	return c.Get("business day start").(string)
}

/*
BusinessDayEnd is the getter for the business day end attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - string : the time of the end of the business day
*/
func (c *ConfigurationType) BusinessDayEnd() string {
	return c.Get("business day end").(string)
}

/*
PrintDebug is the getter for the print debug attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - bool : true if the debug is enabled, false otherwise
*/
func (c *ConfigurationType) PrintDebug() bool {
	val := c.Get("print debug")
	return val.(bool)
}

/*
PreTimetable is the getter for the pre timetable attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - bool : true if the pre timetable is enabled, false otherwise
*/
func (c *ConfigurationType) PreTimetable() bool {
	val := c.Get("pre timetable")
	return val.(bool)
}

/*
Stations is the getter for the stations attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the number of stations
*/
func (c *ConfigurationType) Stations() int {
	return c.getAsInt("stations")
}

/*
CapacityPerTrain is the getter for the capacity per train attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the capacity per train
*/
func (c *ConfigurationType) CapacityPerTrain() int {
	return c.getAsInt("capacity per train")
}

/*
PopulationCommutersProportion is the getter for the population commuters
proportion attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of commuters in the population
*/
func (c *ConfigurationType) PopulationCommutersProportion() float64 {
	return c.Get("population commuters proportion").(float64)
}

/*
PopulationRandomProportion is the getter for the population random proportion
attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of randoms in the population
*/
func (c *ConfigurationType) PopulationRandomProportion() float64 {
	return c.Get("population randoms proportion").(float64)
}

/*
PopulationAdultProportion is the getter for the population adults proportion.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of adults in the population
*/
func (c *ConfigurationType) PopulationAdultProportion() float64 {
	return c.Get("population adults proportion").(float64)
}

/*
PopulationSeniorProportion is the getter for the population senior proportion.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of seniors in the population
*/
func (c *ConfigurationType) PopulationSeniorProportion() float64 {
	return c.Get("population senior proportion").(float64)
}

/*
PopulationDisabledProportion is the getter for the population disabled
proportion.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of disabled in the population
*/
func (c *ConfigurationType) PopulationDisabledProportion() float64 {
	return c.Get("population disabled proportion").(float64)
}

/*
PopulationStudentProportion is the getter for the population student proportion.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of students in the population
*/
func (c *ConfigurationType) PopulationStudentProportion() float64 {
	return c.Get("population students proportion").(float64)
}

/*
PopulationChildrenProportion is the getter for the population children
proportion.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - float64 : the proportion of children in the population
*/
func (c *ConfigurationType) PopulationChildrenProportion() float64 {
	return c.Get("population children proportion").(float64)
}

/*
MorningCommuteTime is the getter for the morning commute time attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Duration : the duration of the morning commute
*/
func (c *ConfigurationType) MorningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(c.Get("morning commute " +
		"time").(string))
	if err != nil {
		log.Fatal(err, "could not parse morning commute time")
	}
	return duration
}

/*
EveningCommuteTime is the getter for the evening commute time attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Duration : the duration of the evening commute
*/
func (c *ConfigurationType) EveningCommuteTime() time.Duration {
	duration, err := time.ParseDuration(c.Get("evening commute " +
		"time").(string))
	if err != nil {
		log.Fatal(err, "could not parse evening commute time")
	}
	return duration
}

/*
CommutePeriodDuration is the getter for the commute period duration attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Duration : the duration of the commute period
*/
func (c *ConfigurationType) CommutePeriodDuration() time.Duration {
	duration, err := time.ParseDuration(c.Get("commute period " +
		"duration").(string))
	if err != nil {
		log.Fatal(err, "could not parse commute duration")
	}
	return duration
}

/*
TimeStart is the getter for the time start attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Time : the time of the start of the simulation
*/
func (c *ConfigurationType) TimeStart() time.Time { //TODO do that
	return c.getAsTime("start time")
}

/*
TimeEnd is the getter for the time end attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Time : the time of the end of the simulation
*/
func (c *ConfigurationType) TimeEnd() time.Time { //TODO do that
	return c.getAsTime("stop time")
}

/*
TimeInStation is the getter for the time in station attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - int : the time in station
*/
func (c *ConfigurationType) TimeInStation() int {
	return c.getAsInt("time in station")
}

/*
ChangeParam is the setter for the parameters.

Param :
  - c *ConfigurationType : the configuration object
  - paramName string : the name of the parameter to change
  - value interface{} : the new value of the parameter
*/
func (c *ConfigurationType) ChangeParam(paramName string, value interface{}) {
	c.Set(paramName, value)
}

/*
GetMaxTimeInStationPassenger is the getter for the max time in station passenger
attribute.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - time.Duration : the max time in station for a passenger
*/
func (c *ConfigurationType) GetMaxTimeInStationPassenger() time.Duration {
	return time.Second * time.Duration(c.getAsInt("passenger max "+
		"waiting time before exit"))
}

/*
getAsInt is a generic function to convert a parameter to int.

Param :
  - c *ConfigurationType : the configuration object
  - paramName string : the name of the parameter to convert

Return :
  - int : the value of the parameter
*/
func (c *ConfigurationType) getAsInt(paramName string) int {
	var param = c.Get(paramName)
	var _, ok = param.(float64)
	if ok {
		return int(c.Get(paramName).(float64))
	}
	_, ok = param.(int)
	if ok {
		return c.Get(paramName).(int)
	}
	log.Fatal("tried to convert non-int value to int: ", paramName, " ", param)
	return -1
}

/*
getAsTime is a generic function to convert a parameter to time.Time.

Param :
  - c *ConfigurationType : the configuration object
  - paramName string : the name of the parameter to convert

Return :
  - time.Time : the value of the parameter
*/
func (c *ConfigurationType) getAsTime(paramName string) time.Time {
	date, _ := time.Parse(time.RFC3339, c.Get(paramName).(string))
	return date
}

/*
GetInstance is the getter for the configuration object.

Return :
  - ConfigurationType : the configuration object
*/
func GetInstance() ConfigurationType {
	var config = ConfigurationType{}
	if config.data == nil {
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

		config.Load(configPath, stationsPath, linesPath)

		mutex.Unlock()

		correct, err := config.Check()
		if !correct {
			log.Fatal(err, "error while verifying config")
		}
	}

	return config
}

/*
Load is a function to load the configuration from the config.json file.

Param :
  - c *ConfigurationType : the configuration object
  - configPath string : the path to the config.json file
  - nameStationListPath string : the path to the nameStationList.json file
  - nameLineListPath string : the path to the nameLineList.json file
*/
func (c *ConfigurationType) Load(configPath, nameStationListPath,
	nameLineListPath string) {

	configFile, err := os.Open(configPath)
	if err != nil {
		log.Fatal(err, "error while reading config (0) - config open failed")
	}
	configBytes, err := io.ReadAll(configFile)
	if err != nil {
		log.Fatal(err, "error while reading config (1) - config read failed")
	}

	errJson := json.Unmarshal(configBytes, &c.data)
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

Param :
  - c *ConfigurationType : the configuration object
*/
func (c *ConfigurationType) Reload() {
	if strings.Contains(basePath, "src") {
		configPath = filepath.Join(basePath, "/configs/config.json")
	} else if strings.Contains(basePath, "configs") {
		configPath = filepath.Join(basePath, "/config.json")
	}
	stationsPath = filepath.Join(basePath, "src/configs/nameStationList.json")
	linesPath = filepath.Join(basePath, "src/configs/nameLineList.json")

	c.Load(configPath, stationsPath, linesPath)
}

/*
Check is a function to check if the configuration is correct.

Param :
  - c *ConfigurationType : the configuration object

Return :
  - bool : true if the configuration is correct, false otherwise
  - error : the error message if the configuration is not correct
*/
func (c *ConfigurationType) Check() (bool, error) {
	var errMsg = ""
	isCorrect := true

	if c.InterchangeStations() > c.Stations() {
		errMsg = errMsg + " There are more interchanges than stations"
		isCorrect = false
	}

	if c.Lines() > len(c.NameLineList()) {
		errMsg = errMsg + " Not enough names for lines"
		isCorrect = false
	}

	if c.Stations() > len(c.NameStationList()) {
		errMsg = errMsg + " Not enough names for stations"
		isCorrect = false
	}

	if c.TrainsPerLine() < 1 {
		errMsg = errMsg + " There is zero train per lines"
		isCorrect = false
	}

	if (c.PopulationRandomProportion() +
		c.PopulationCommutersProportion()) != 1 {
		errMsg = errMsg + " The population proportions should be equal to one"
		isCorrect = false
	}

	return isCorrect, errors.New(errMsg)
}
