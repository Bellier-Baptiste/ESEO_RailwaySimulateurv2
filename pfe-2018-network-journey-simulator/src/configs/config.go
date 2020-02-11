package configs

import (
	"encoding/json"
	"errors"
	"go/build"
	"io/ioutil"
	"log"
	"os"
	"path/filepath"
	"sync"
	"time"
)

var basePath = os.Getenv("GOPATH")
var projectPath string = "src"

var configPath string
var stationsPath string
var linesPath string

type ConfigurationObject map[string]interface{}

var config ConfigurationObject

var nameStationList []string

var nameLineList []string

// Getters & Setters

func (c ConfigurationObject) NameStationList() []string {
	return nameStationList
}

func (c ConfigurationObject) NameLineList() []string {
	return nameLineList
}

func (c ConfigurationObject) Lines() int {
	return c.getAsInt("lines")
}

func (c ConfigurationObject) InterchangeStations() int {
	return c.getAsInt("interchange stations")
}

func (c ConfigurationObject) Population() int {
	return c.getAsInt("population")
}

func (c ConfigurationObject) AccelerationTrain() int{
	return c.getAsInt("acceleration train")
}

func (c ConfigurationObject) MaxSpeedTrain() int{
	return c.getAsInt("max speed train")
}

func (c ConfigurationObject) Seed() int64 {
	return int64(config["seed"].(float64))
}

func (c ConfigurationObject) CsvHeaders() bool {
	return config["csv headers"].(bool)
}

func (c ConfigurationObject) TrainsPerLine() int {
	return c.getAsInt("trains per line")
}

func (c ConfigurationObject) BusinessDayStart() string {
	return string(config["business day start"].(string))
}

func (c ConfigurationObject) BusinessDayEnd() string {
	return string(config["business day end"].(string))
}

func (c ConfigurationObject) PrintDebug() bool {
	if val, ok := c["print debug"]; ok {
		return val.(bool)
	}
	return false
}

func (c ConfigurationObject) PreTimetable() bool{
	if val, ok := c["pre timetable"]; ok {
		return val.(bool)
	}
	return false
}

func (c ConfigurationObject) Stations() int {
	return c.getAsInt("stations")
}

func (c ConfigurationObject) CapacityPerTrain() int {
	return c.getAsInt("capacity per train")
}

func (c ConfigurationObject) PopulationCommutersProportion() float64 {
	return config["population commuters proportion"].(float64)
}

func (c ConfigurationObject) PopulationRandomProportion() float64 {
	return config["population randoms proportion"].(float64)
}

func (c ConfigurationObject) PopulationAdultProportion() float64 {
	return config["population adults proportion"].(float64)
}

func (c ConfigurationObject) PopulationSeniorProportion() float64 {
	return config["population senior proportion"].(float64)
}

func (c ConfigurationObject) PopulationDisabledProportion() float64 {
	return config["population disabled proportion"].(float64)
}

func (c ConfigurationObject) PopulationStudentProportion() float64 {
	return config["population students proportion"].(float64)
}

func (c ConfigurationObject) PopulationChildrenProportion() float64 {
	return config["population children proportion"].(float64)
}


func (c ConfigurationObject) MorningCommuteTime() time.Duration {
	duration, error := time.ParseDuration(string(config["morning commute time"].(string)))
	if error != nil {
		log.Fatal(error, "could not parse morning commute time")
	}
	return duration
}

func (c ConfigurationObject) EveningCommuteTime() time.Duration {
	duration, error := time.ParseDuration(string(config["evening commute time"].(string)))
	if error != nil {
		log.Fatal(error, "could not parse evening commute time")
	}
	return duration
}

func (c ConfigurationObject) CommutePeriodDuration() time.Duration {
	duration, error := time.ParseDuration(string(config["commute period duration"].(string)))
	if error != nil {
		log.Fatal(error, "could not parse commute duration")
	}
	return duration
}

func (c ConfigurationObject) TimeStart() time.Time { //TODO do that
	return c.getAsTime("start time")
}

func (c ConfigurationObject) TimeEnd() time.Time { //TODO do that
	return c.getAsTime("stop time")
}

func (c ConfigurationObject) TimeInStation() int {
	return c.getAsInt("time in station")
}

func (c *ConfigurationObject) ChangeParam(paramName string, value interface{}) {
	config[paramName] = value
}

func (c *ConfigurationObject) GetMaxTimeInStationPassenger() time.Duration {
	return time.Second * time.Duration(c.getAsInt("passenger max waiting time before exit"))
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
		var mutex = &sync.Mutex{}

		mutex.Lock()

		if basePath == "" {
			basePath = build.Default.GOPATH
		}

		configPath = filepath.Join(basePath, projectPath, "configs/config.json")
		stationsPath = filepath.Join(basePath, projectPath, "configs/nameStationList.json")
		linesPath = filepath.Join(basePath, projectPath, "configs/nameLineList.json")

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

func Load(configPath string, nameStationListPath string, nameLineListPath string) {

	configFile, err := os.Open(configPath)
	if err != nil {
		log.Fatal(err, "error while reading config (0) - config open failed")
	}
	configBytes, err := ioutil.ReadAll(configFile)
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
	nameStationListBytes, err := ioutil.ReadAll(nameStationListFile)
	json.Unmarshal(nameStationListBytes, &nameStationList)

	nameLineListFile, err := os.Open(nameLineListPath)
	if err != nil {
		log.Fatal(err, "error while reading config (4) - nameLineListPath open failed")
	}
	nameLineListBytes, err := ioutil.ReadAll(nameLineListFile)
	json.Unmarshal(nameLineListBytes, &nameLineList)
}

func (c *ConfigurationObject) Reload() {
	configPath = filepath.Join(basePath, projectPath, "configs/config.json")
	stationsPath = filepath.Join(basePath, projectPath, "configs/nameStationList.json")
	linesPath = filepath.Join(basePath, projectPath, "configs/nameLineList.json")

	Load(configPath, stationsPath, linesPath)
}

func (c ConfigurationObject) Check() (bool, error) {
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

	if (config.PopulationRandomProportion() + config.PopulationCommutersProportion() ) != 1 {
		errMsg = errMsg + "The population proportions should be equal to one"
		isCorrect = false
	}

	return isCorrect, errors.New(errMsg)

}
