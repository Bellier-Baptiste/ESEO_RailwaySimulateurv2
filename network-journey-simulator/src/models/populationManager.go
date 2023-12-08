/*
Package models

File : populationManager.go

Brief :	This file contains the Population struct and its methods.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX
  - Alexis BONAMY
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
package models

import (
	"fmt"
	"log"
	"math"
	"math/rand"
	"network-journey-simulator/src/configs"
	"network-journey-simulator/src/tools"
	"reflect"
	"sort"
	"strconv"
	"strings"
	"sync"
	"time"
)

/*
Population is the structure that manage the population of the simulation.

Attributes :
  - outside map[string]*Passenger : the passengers outside
  - outsideSorted []*Passenger : the passengers outside, sorted by the time
    they should enter a station (id only)
  - inStation []map[string]*Passenger : the passengers in stations
  - inTrain []map[string]*Passenger : the passengers in trains
  - output tools.CsvFile : the csv file to write the output
  - inTrainMutex sync.Mutex : the mutex to lock the inTrain map
  - inStationMutex sync.Mutex : the mutex to lock the inStation map
  - printDebug bool : true if the debug mode is activated, false otherwise
  - maxTimeInStationPassenger time.Duration : the max time a passenger can
    stay in a station

Methods :
  - test(popSizeRandoms, popSizeCommuters int, config
    configs.ConfigurationType, aMap Map) : test create a population
  - NewPopulation(popSize int, popCommutersProportion, popRandomsProportion
    float64, aMap Map) : create a population and assign it trips (random and
    commuting)
  - PickAKind() int : pick a kind of passenger (ADL, STD, DIS, CHD, SEN)
    according to the proportions in the config file
  - kindToString(kind int) string : convert a kind of passenger (ADL, STD, DIS,
    CHD, SEN) to a string
  - sortOutsideInit() : sort the outsiders and re-init the list. 2*O(n)
    complexity. DON'T ABUSE OF IT.
  - SortOutside() : sort the outsiders; O(n) complexity, don't abuse of it. Use
    it only when you know that the list is not sorted.
  - OutsideSortedFindIndexByTime(t time.Time) int : find the index of the first
    passenger having a nextTrip after the time t. It uses a binary tree to find
    the last index where p.outsideSorted[index].departureTime <= t.
  - OutsideSortedInsertPassenger(passenger *Passenger) : reinsert a Passenger
    inside the sorted OutsideSorted. 1% chance of relaunching a sort (to make
    sure everything works smoothly and to avoid bugs).
  - OutsideSortedPopAllBefore(t time.Time) []*Passenger : pop all passengers
    having a nextTrip before the time t.
  - OutsideSortedCheckSorted() bool : verify that the OutsideSorted list is
    sorted.
  - FindPassenger(passenger Passenger) string : find where a passenger is. For
    debug purposes only.
  - transferFromPopulationToStation(passenger *Passenger, stationPt
    *MetroStation, aTime time.Time) : transfer a passenger from the general
    population to a station
  - transferFromStationToPopulation(passenger *Passenger, stationPt
    *MetroStation, aTime time.Time) : transfer a passenger from a station to
    the general population
  - transferFromStationToTrain(passenger *Passenger, trainPt *MetroTrain,
    stationPt *MetroStation) : transfer a passenger from a station to a
    train
  - transferFromTrainToStation(passenger *Passenger, trainPt *MetroTrain,
    stationPt *MetroStation) : transfer a passenger from a train to a
    station
  - UpdateAll(aTime time.Time, mapO *Map) : update all passengers
  - UpdateOutsideToStation(aTime time.Time) : update all passengers outside
    to a station
  - UpdateStationToOutside(aTime time.Time, stationPt *MetroStation) : update
    all passengers in a station to outside
  - StationToOutside(aTime time.Time, stationPt *MetroStation) : update all
    passengers in a station to outside
  - UpdateStationToTrain(train *MetroTrain) : update all passengers in a
    station to a train
  - UpdateTrainToStation(train *MetroTrain, aTime time.Time) : update all
    passengers in a train to a station
  - String() string : return the population as a string
  - createColumnsTitles() : create the columns titles of the output csv file
*/
type Population struct {
	outside       map[string]*Passenger
	outsideSorted []*Passenger
	//passengers outside, sorted by the time they should enter a station (id only)
	//inStation map[int]map[string]*Passenger
	//stationId->lineId->up(0)/down(1)->passengers
	inStation []map[string]*Passenger
	//inTrain   map[int]map[string]*Passenger
	inTrain []map[string]*Passenger
	output  tools.CsvFile

	inTrainMutex   sync.Mutex
	inStationMutex sync.Mutex

	printDebug                bool
	maxTimeInStationPassenger time.Duration
}

/*
test create a population.

Param :
  - p *Population : the population to test
  - popSizeRandoms int : the number of random passengers
  - popSizeCommuters int : the number of commuting passengers
  - config configs.ConfigurationType : the configuration object
  - aMap Map : the map of the network
*/
func (p *Population) test(popSizeRandoms, popSizeCommuters int,
	config configs.ConfigurationType, aMap Map) {
	for i := popSizeRandoms; i < popSizeRandoms+popSizeCommuters; i++ {
		id := strconv.FormatInt(int64(i), 10)
		passenger := NewPassenger(id, PickAKind())
		startTime := config.TimeStart()
		currentDay := startTime
		endTime := config.TimeEnd()
		timeOfMorningCommute := config.MorningCommuteTime()
		timeOfEveningCommute := config.EveningCommuteTime()
		commutePeriodDuration := int64(config.CommutePeriodDuration().Seconds())
		for currentDay.Before(endTime) {
			morningTrip, eveningTrip := CommutingTrips(commutePeriodDuration,
				timeOfMorningCommute, timeOfEveningCommute, currentDay, aMap)
			if morningTrip.IsValid(startTime, endTime) {
				passenger.AddTrip(&morningTrip)
			}
			if eveningTrip.IsValid(startTime, endTime) {
				passenger.AddTrip(&eveningTrip)
			}
			currentDay = currentDay.AddDate(0, 0, 1)
		}
		p.outside[passenger.Id()] = &passenger
	}
}

/*
NewPopulation create a population and assign it trips (random and commuting).

Param :
  - popSize int : the size of the population
  - popCommutersProportion float64 : the proportion of commuters
  - popRandomsProportion float64 : the proportion of random passengers
  - aMap Map : the map of the network

Return :
  - *Population : the population
*/
func NewPopulation(popSize int, popCommutersProportion,
	popRandomsProportion float64, aMap Map) *Population {

	if math.Abs(popCommutersProportion+popRandomsProportion-1) > 0.02 {
		log.Fatal("NewPopulation error : popCommutersProportion + "+
			"popRandomsProportions != 1 (", popCommutersProportion, "+",
			popRandomsProportion, ")")
	}

	config := configs.GetInstance()
	rand.New(rand.NewSource(config.Seed()))
	popSizeRandoms := int(float64(popSize) * popRandomsProportion)
	popSizeCommuters := int(float64(popSize) * popCommutersProportion)
	var population Population
	population.outside = make(map[string]*Passenger)
	population.maxTimeInStationPassenger = config.GetMaxTimeInStationPassenger()

	population.inTrainMutex = sync.Mutex{}
	population.inStationMutex = sync.Mutex{}

	for i := 0; i < popSizeRandoms; i++ {
		id := strconv.FormatInt(int64(i), 10)

		passenger := NewPassenger(id, PickAKind())
		startTime := config.TimeStart()
		endTime := config.TimeEnd()
		currentDay := startTime
		for currentDay.Before(endTime) {
			trip := RandomTrip(currentDay, aMap)
			if trip.IsValid(startTime, endTime) {
				passenger.AddTrip(&trip)
			}
			currentDay = currentDay.AddDate(0, 0, 1)
		}
		population.outside[passenger.Id()] = &passenger
	}

	population.test(popSizeRandoms, popSizeCommuters, config, aMap)

	for i := range population.outside {
		population.outside[i].calculateNextTrip()
	}

	var adConfig = configs.GetAdvancedConfigInstance()

	population.inStation = make([]map[string]*Passenger,
		len(adConfig.MapC.Stations))
	for i := range population.inStation {
		population.inStation[i] = map[string]*Passenger{}
	}
	//population.inTrain = make(map[int]map[string]*Passenger)
	population.inTrain = make([]map[string]*Passenger, adConfig.NumberOfTrains())
	for i := range population.inTrain {
		population.inTrain[i] = map[string]*Passenger{}
	}

	population.createColumnsTitles()

	population.sortOutsideInit()

	population.printDebug = config.PrintDebug()

	return &population
}

/*
PickAKind pick a kind of passenger (ADL, STD, DIS, CHD, SEN) according to the
proportions in the config file.

Return :
  - int : the kind of passenger
*/
func PickAKind() int {
	config := configs.GetInstance()
	adlProp := config.PopulationAdultProportion()
	senProp := config.PopulationSeniorProportion()
	chdProp := config.PopulationChildrenProportion()
	disProp := config.PopulationDisabledProportion()
	stdProp := config.PopulationStudentProportion()
	var kind int
	choice := rand.Float64()
	if choice < adlProp {
		kind = ADL
	} else if (adlProp < choice) && (choice < adlProp+senProp) {
		kind = SEN
	} else if (adlProp+senProp < choice) && (choice < senProp+adlProp+chdProp) {
		kind = CHD
	} else if (adlProp+senProp+chdProp < choice) &&
		(choice < senProp+adlProp+chdProp+disProp) {
		kind = DIS
	} else if (adlProp+senProp+chdProp+disProp < choice) &&
		(choice < senProp+adlProp+chdProp+disProp+stdProp) {
		kind = STD
	}

	return kind
}

/*
kindToString convert a kind of passenger (ADL, STD, DIS, CHD, SEN) to a string.

Param :
  - kind int : the kind of passenger

Return :
  - string : the kind of passenger as a string
*/
func kindToString(kind int) string {
	names := [...]string{
		"ADL",
		"STD",
		"DIS",
		"CHD",
		"SEN"}
	return names[kind]
}

/*
sortOutsideInit sort the outsiders and re-init the list. 2*O(n) complexity.
DON'T ABUSE OF IT.

Param :
  - p *Population : the population
*/
func (p *Population) sortOutsideInit() {
	p.outsideSorted = make([]*Passenger, len(p.outside))
	var j = 0
	for _, passengerOutside := range p.outside {
		p.outsideSorted[j] = passengerOutside
		j++
	}

	p.SortOutside()
}

/*
SortOutside sort the outsiders; O(n) complexity, don't abuse of it. Use it only
when you know that the list is not sorted.

Param :
  - p *Population : the population
*/
func (p *Population) SortOutside() {
	sort.Slice(p.outsideSorted, func(i, j int) bool {
		if p.outsideSorted[i].nextTrip == nil {
			return false
		}

		if p.outsideSorted[j].nextTrip == nil {
			return true
		}

		return p.outsideSorted[i].nextTrip.departureTime.Before(
			p.outsideSorted[j].nextTrip.departureTime)
	})
}

/*
OutsideSortedFindIndexByTime find the index of the first passenger having a
nextTrip after the time t. It uses a binary tree to find the last index where
p.outsideSorted[index].departureTime <= t.

Param :
  - p *Population : the population
  - t time.Time : the time to compare

Return :
  - int : the index of the first passenger having a nextTrip after the time t
*/
func (p *Population) OutsideSortedFindIndexByTime(t time.Time) int {
	return sort.Search(len(p.outsideSorted), func(i int) bool {
		//f(i) == true => f(i+1) == true
		return p.outsideSorted[i].nextTrip == nil ||
			t.Before(p.outsideSorted[i].nextTrip.departureTime)
	})
}

/*
OutsideSortedInsertPassenger reinsert a Passenger inside the sorted
OutsideSorted. 1% chance of relaunching a sort (to make sure everything works
smoothly and to avoid bugs).

Param :
  - p *Population : the population
  - passenger *Passenger : the passenger to insert
*/
func (p *Population) OutsideSortedInsertPassenger(passenger *Passenger) {
	if passenger.nextTrip == nil || passenger.nextTrip.IsCompleted() {
		passenger.calculateNextTrip()
	}

	if passenger.nextTrip == nil {
		//insert at the end
		p.outsideSorted = append(p.outsideSorted, passenger)
		return
	}

	var index = p.OutsideSortedFindIndexByTime(passenger.nextTrip.departureTime)
	if index == len(p.outsideSorted) {
		//not found > insert at the end
		p.outsideSorted = append(p.outsideSorted, passenger)
		return
	}

	p.outsideSorted = append(p.outsideSorted[:index],
		append([]*Passenger{passenger}, p.outsideSorted[index:]...)...)
}

/*
OutsideSortedPopAllBefore pop all passengers having a nextTrip before the time
t.

Param :
  - p *Population : the population
  - t time.Time : the time to compare

Return :
  - []*Passenger : the list of passengers having a nextTrip before the time t
*/
func (p *Population) OutsideSortedPopAllBefore(t time.Time) []*Passenger {
	var index = p.OutsideSortedFindIndexByTime(t)

	if index == len(p.outsideSorted) {
		return []*Passenger{}
	}

	var output = p.outsideSorted[:index]
	p.outsideSorted = p.outsideSorted[index:]
	return output
}

/*
OutsideSortedCheckSorted verify that the OutsideSorted list is sorted.

Param :
  - p *Population : the population

Return :
  - bool : true if the list is sorted, false otherwise
*/
func (p *Population) OutsideSortedCheckSorted() bool {
	//assert sorted AFTER
	for i := 0; i < len(p.outsideSorted)-1; i++ {
		if p.outsideSorted[i].nextTrip == nil &&
			p.outsideSorted[i+1].nextTrip != nil {
			println("debug: outstideSorted error : nil before not nil")
			return false
		}
		if p.outsideSorted[i].nextTrip == nil ||
			p.outsideSorted[i+1].nextTrip == nil {
			continue
		}

		if p.outsideSorted[i].nextTrip.departureTime.After(
			p.outsideSorted[i+1].nextTrip.departureTime) {
			println("debug: outstideSorted error : not good order ",
				p.outsideSorted[i].nextTrip.departureTime.String(), " :: ",
				p.outsideSorted[i+1].nextTrip.departureTime.String())
			return false
		}
	}

	return true
}

// Getters (Setters not needed)

/*
Outside return the list of passengers outside.

Param :
  - p *Population : the population

Return :
  - map[string]*Passenger : the list of passengers outside
*/
func (p *Population) Outside() map[string]*Passenger {
	return p.outside
}

/*
InTrains return the list of passengers in trains.

Param :
  - p *Population : the population

Return :
  - []map[string]*Passenger : the list of passengers in trains
*/
func (p *Population) InTrains() []map[string]*Passenger {
	return p.inTrain
}

/*
InStation return the list of passengers in stations.

Param :
  - p *Population : the population

Return :
  - []map[string]*Passenger : the list of passengers in stations
*/
func (p *Population) InStation() []map[string]*Passenger {
	return p.inStation
}

/*
InTrain return the list of passengers in a train.

Param :
  - p *Population : the population
  - train MetroTrain : the train

Return :
  - map[string]*Passenger : the list of passengers in a train
*/
func (p *Population) InTrain(train MetroTrain) map[string]*Passenger {
	return p.inTrain[train.Id()]
}

/*
Passengers return the list of all passengers.

Param :
  - p *Population : the population

Return :
  - map[string]*Passenger : the list of all passengers
*/
func (p *Population) Passengers() map[string]*Passenger {
	var out = map[string]*Passenger{}

	for i, passenger := range p.outside {
		var _, exists = out[i] //check if the key is already stored
		if exists {
			println("Outside - key already in use - error", i, passenger.id)
		}
		out[i] = passenger
	}

	for _, station := range p.inStation {
		for i, passenger := range station {
			var _, exists = out[i] //check if the key is already stored
			if exists {
				print("inStation - key already in use - error")
			}
			out[i] = passenger
		}
	}

	for _, train := range p.inTrain {
		for i, passenger := range train {
			var _, exists = out[i] //check if the key is already stored
			if exists {
				print("inStation - key already in use - error")
			}
			out[i] = passenger
		}
	}

	return out
}

//-------------------------------------------------------- functions and methods

/*
FindPassenger find where a passenger is. For debug purposes only.

Param :
  - p *Population : the population
  - passenger Passenger : the passenger

Return :
  - string : the location of the passenger
*/
func (p *Population) FindPassenger(passenger Passenger) string {
	var exists = false

	_, exists = p.outside[passenger.id]
	if exists {
		return "outside"
	}

	for i, station := range p.inStation {
		_, exists = station[passenger.id]
		if exists {
			return "inStation #" + strconv.Itoa(i)
		}
	}

	for i, train := range p.inTrain {
		_, exists = train[passenger.id]
		if exists {
			return "inTrain #" + strconv.Itoa(i)
		}
	}

	return "not found"
}

/*
TransferFromPopulationToStation transfer a passenger from the general population
to a station.

Param :
  - p *Population : the population
  - passenger *Passenger : the passenger
  - stationPt *MetroStation : the station
  - aTime time.Time : the time of the transfer
*/
func (p *Population) TransferFromPopulationToStation(passenger *Passenger,
	stationPt *MetroStation, aTime time.Time) {
	if p.inStation[stationPt.Id()] == nil {
		p.inStation[stationPt.Id()] = make(map[string]*Passenger)
	}
	info := prepareCSVline(*passenger, stationPt, aTime, "ENT")
	p.output.Write(info)
	passenger.setTimeArrivalLastStation(aTime)
	p.inStation[stationPt.Id()][passenger.Id()] = passenger
	delete(p.outside, passenger.Id())
	if p.printDebug {
		if stationPt.id == 1 {
			println("Outside -> station", stationPt.name, ": passenger", passenger.id)
		}
	}
}

/*
TransferFromStationToPopulation transfer a passenger from a station to the
general population.

Param :
  - p *Population : the population
  - passenger *Passenger : the passenger
  - stationPt *MetroStation : the station
  - aTime time.Time : the time of the transfer
*/
func (p *Population) TransferFromStationToPopulation(passenger *Passenger,
	stationPt *MetroStation, aTime time.Time, typeTicket string) {
	info := prepareCSVline(*passenger, stationPt, aTime, typeTicket)
	p.output.Write(info)
	p.outside[passenger.Id()] = passenger
	delete(p.inStation[stationPt.Id()], passenger.Id())
	if p.printDebug {
		if stationPt.id == 1 {
			println("station", stationPt.name, "-> Outside : passenger", passenger.id)
		}
	}
}

// transfer a passenger from the station to the train

/*
transferFromStationToTrain transfer a passenger from a station to a train.

Param :
  - p *Population : the population
  - passenger *Passenger : the passenger
  - trainPt *MetroTrain : the train
  - stationPt *MetroStation : the station
*/
func (p *Population) transferFromStationToTrain(passenger *Passenger,
	trainPt *MetroTrain, stationPt *MetroStation) {
	p.inTrainMutex.Lock()
	if p.inTrain[trainPt.Id()] == nil {
		p.inTrain[trainPt.Id()] = make(map[string]*Passenger)
	}
	p.inTrain[trainPt.Id()][passenger.Id()] = passenger
	p.inTrainMutex.Unlock()

	p.inStationMutex.Lock()
	delete(p.inStation[stationPt.Id()], passenger.Id())
	p.inStationMutex.Unlock()

	if p.printDebug {
		if stationPt.id == 1 {
			println("station", stationPt.name, " -> train ",
				trainPt.id, " : passenger", passenger.id)
		}
	}
}

/*
transferFromTrainToStation transfer a passenger from a train to a station.

Param :
  - p *Population : the population
  - passenger *Passenger : the passenger
  - trainPt *MetroTrain : the train
  - stationPt *MetroStation : the station
  - aTime time.Time : the time of the transfer
*/
func (p *Population) transferFromTrainToStation(passenger *Passenger,
	trainPt *MetroTrain, stationPt *MetroStation, aTime time.Time) {
	passenger.setTimeArrivalLastStation(aTime)

	p.inStationMutex.Lock()
	if p.inStation[stationPt.Id()] == nil {
		p.inStation[stationPt.Id()] = make(map[string]*Passenger)
	}
	p.inStation[stationPt.Id()][passenger.Id()] = passenger
	p.inStationMutex.Unlock()

	p.inTrainMutex.Lock()
	delete(p.inTrain[trainPt.Id()], passenger.Id())
	p.inTrainMutex.Unlock()

	if p.printDebug {
		if stationPt.id == 1 {
			println("train", trainPt.id, " -> station",
				stationPt.name, ": passenger", passenger.id)
		}
	}
}

/*
UpdateAll update the situation in all station (outside <-> station only) and
update the situation in all trains (station <-> train only).

Param :
  - p *Population : the population
  - aTime time.Time : the time of the update
  - mapO *Map : the map of the network
*/
func (p *Population) UpdateAll(aTime time.Time, mapO *Map) {
	// 1. outside > station
	p.UpdateOutsideToStations(aTime)
	// 2. station > outside
	for _, station := range mapO.Stations() {
		p.UpdateStationToOutside(aTime, &station)
	}
}

/*
UpdateOutsideToStations update the situation in all stations (outside -> station
only).

Param :
  - p *Population : the population
  - aTime time.Time : the time of the update
*/
func (p *Population) UpdateOutsideToStations(aTime time.Time) {
	// 1. transfer from outside to station
	var passengers = p.OutsideSortedPopAllBefore(aTime)

	var trip *Trip
	for i := range passengers {
		passenger := passengers[i]
		trip = passenger.nextTrip
		if trip == nil {
			println("\nsize:" + strconv.Itoa(len(passengers)))
			println("\nindex fail:" + strconv.Itoa(i))
			for j := i - 2; j < i+3; j++ {
				println("index " + strconv.Itoa(i) + " - " + passengers[j].String() + "\n")
			}
			log.Fatal("trip is nil ")
		}
		departureStation := trip.Path().Stations()[0]
		passenger.SetCurrentTrip(trip)
		p.TransferFromPopulationToStation(passenger, departureStation, aTime)
	}
}

/*
UpdateStationToOutside update the situation in all stations (station -> outside
only).

Param :
  - p *Population : the population
  - aTime time.Time : the time of the update
  - station *MetroStation : the station
*/
func (p *Population) UpdateStationToOutside(aTime time.Time,
	station *MetroStation) {
	for i := range p.inStation[station.Id()] {
		passenger := p.inStation[station.Id()][i]
		var trip = passenger.CurrentTrip()
		if trip.Path().EndStation().Id() == station.Id() ||
			(passenger.timeArrivalLastStation.Add(p.maxTimeInStationPassenger).
				Before(aTime)) {
			trip.SetArrivalTime(aTime)
			var typeTicket = "USE"
			p.TransferFromStationToPopulation(passenger, station, aTime, typeTicket)

			passenger.ClearCurrentTrip()
			passenger.calculateNextTrip()
			p.OutsideSortedInsertPassenger(passenger)
			//use after the transfer from station to pop
			//println("station->outside: passenger #"+passenger.id)
		}
	}
}

/*
StationToOutside transfer a passenger from a station to the general population.

Param :
  - p *Population : the population
  - aTime time.Time : the time of the transfer
  - station *MetroStation : the station
  - passenger *Passenger : the passenger
*/
func (p *Population) StationToOutside(aTime time.Time, station *MetroStation,
	passenger *Passenger) {
	var trip = passenger.CurrentTrip()
	trip.SetArrivalTime(aTime)
	var typeTicket = "USE"
	p.TransferFromStationToPopulation(passenger, station, aTime, typeTicket)

	passenger.ClearCurrentTrip()
	passenger.calculateNextTrip()
	p.OutsideSortedInsertPassenger(passenger)
}

/*
UpdateStationToTrain update the situation in all stations (station -> train
only).

Param :
  - p *Population : the population
  - train *MetroTrain : the train
*/
func (p *Population) UpdateStationToTrain(train *MetroTrain) {
	station := train.CurrentStation()
	p.inStationMutex.Lock()
	inStation := p.inStation[station.Id()]
	p.inStationMutex.Unlock()
	for i := range inStation {
		p.inStationMutex.Lock()
		passenger := inStation[i]
		p.inStationMutex.Unlock()
		if passenger == nil { //happens in the thread
			continue
		}
		if train.GetAvailableCapacity(p) <= 0 {
			break
		}

		trip := passenger.CurrentTrip()
		if trip.Path().TakeTrain(train) && train.GetAvailableCapacity(p) > 0 {
			p.transferFromStationToTrain(passenger, train, station)
		}
	}
}

/*
UpdateTrainToStation update the situation in all trains (train -> station
only).

Param :
  - p *Population : the population
  - train *MetroTrain : the train
  - aTime time.Time : the time of the update
*/
func (p *Population) UpdateTrainToStation(train *MetroTrain, aTime time.Time) {
	station := train.CurrentStation()
	p.inTrainMutex.Lock()
	inTrain := p.inTrain[train.id]
	p.inTrainMutex.Unlock()
	for i := range inTrain {
		p.inTrainMutex.Lock()
		passenger := inTrain[i]
		p.inTrainMutex.Unlock()

		if passenger == nil {
			continue
		}

		trip := passenger.CurrentTrip()
		if trip.Path().ExitTrain(train) {
			p.transferFromTrainToStation(passenger, train, station, aTime)
		}
	}
}

/*
String return a string representation of the population.

Param :
  - p *Population : the population

Return :
  - string : the string representation of the population
*/
func (p *Population) String() string {

	var passengers = p.Passengers()

	var out = "Total Passengers : " + strconv.Itoa(len(passengers))
	out += "\n\tOutside : " + strconv.Itoa(len(p.outside))

	var passengersString = ""
	for _, passenger := range passengers {
		passengersString += passenger.String() + "\n"
	}

	out += "\n\t" + strings.Replace(passengersString, "\n", "\n\t", -1)

	return out
}

/*
prepareCSVline prepare a line for the CSV file.

Param :
  - passenger Passenger : the passenger
  - station *MetroStation : the station
  - aTime time.Time : the time of the transfer
  - transactionType string : the type of transaction

Return :
  - []string : the line for the CSV file
*/
func prepareCSVline(passenger Passenger, station *MetroStation,
	aTime time.Time, transactionType string) []string {
	var line []string
	businessDay := GetBusinessDay(aTime).Format("02/01/2006")
	transactionDate := aTime.Format("02/01/2006 15:04:05")
	currentStation := strconv.Itoa(station.Id())
	departureStation := strconv.Itoa(
		passenger.CurrentTrip().Path().Stations()[0].Id())
	line = append(line, passenger.Id(), businessDay, transactionDate,
		transactionType, kindToString(passenger.Kind()), departureStation,
		currentStation, "", "", "")
	return line
}

/*
createColumnsTitles create the columns titles for the CSV file.

Param :
  - p *Population : the population
*/
func (p *Population) createColumnsTitles() {
	columnsNames := []string{
		"card_id",
		"business_day",
		"transaction_datetime",
		"transaction_type_code",
		"transaction_sub_type",
		"entry_station",
		"transaction_location",
		"transaction_number",
		"machine_id",
		"is_2nd_leg_intermodel"}
	p.output = tools.NewFile("tickets", columnsNames)
}

/*
StationExitPop transfer all passengers in a station to the general population.

Param :
  - p *Population : the population
  - aTime time.Time : the time of the transfer
  - station *MetroStation : the station
*/
func (p *Population) StationExitPop(aTime time.Time, station *MetroStation) {
	/*
		fmt.Println(station.name)
	*/
	for i := range p.inStation[station.Id()] {
		passenger := p.inStation[station.Id()][i]
		var trip = passenger.CurrentTrip()
		trip.SetArrivalTime(aTime)
		var typeTicket = "USE"
		p.TransferFromStationToPopulation(passenger, station, aTime, typeTicket)

		passenger.ClearCurrentTrip()
		passenger.calculateNextTrip()
		p.OutsideSortedInsertPassenger(passenger)
		/*
			use after the transfer from station to pop
			fmt.Println("station->outside: passenger #" + passenger.id)
		*/
	}
}

/*
AllStationsExitPop transfer all passengers in all stations to the general
population.

Param :
  - p *Population : the population
  - aTime time.Time : the time of the transfer
  - mapO *Map : the map of the network
*/
func (p *Population) AllStationsExitPop(aTime time.Time, mapO *Map) {

	fmt.Println("\rTest end of simulation")
	fmt.Println(p.InStation())
	fmt.Println(p.InTrains())

	/*
		Call StationExitPop for each station
	*/
	for _, station := range mapO.Stations() {
		if !p.IsStationEmpty(&station) {
			p.StationExitPop(aTime, &station)
		}
	}
	/*
		fmt.Println(p.InStation())
	*/
}

/*
IsStationEmpty return true if the station is empty, false otherwise.

Param :
  - p *Population : the population
  - station *MetroStation : the station

Return :
  - bool : true if the station is empty, false otherwise
*/
func (p *Population) IsStationEmpty(station *MetroStation) bool {
	emptyStation := map[string]*Passenger{}
	if reflect.DeepEqual(p.inStation[station.Id()], emptyStation) {
		return true
	}
	return false
}

/*
IsTrainEmpty return true if the train is empty, false otherwise.

Param :
  - p *Population : the population
  - train *MetroTrain : the station

Return :
  - bool : true if the station is empty, false otherwise
*/
func (p *Population) IsTrainEmpty(train *MetroTrain) bool {
	emptyTrain := map[string]*Passenger{}
	if reflect.DeepEqual(p.inTrain[train.Id()], emptyTrain) {
		return true
	}
	return false
}
