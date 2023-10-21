/*
Package models

File : populationManager.go

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
package models

import (
	"log"
	"math"
	"math/rand"
	"pfe-2018-network-journey-simulator/src/configs"
	"pfe-2018-network-journey-simulator/src/tools"
	"sort"
	"strconv"
	"strings"
	"sync"
	"time"
)

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

func (p *Population) test(popSizeRandoms, popSizeCommuters int,
	config configs.ConfigurationObject, aMap Map) {
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
			morningTrip, eveningTrip := CommuntingTrips(commutePeriodDuration,
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
NewPopulation create a population and assign it trips (random and commuting)
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

func kindToString(kind int) string {
	names := [...]string{
		"ADL",
		"STD",
		"DIS",
		"CHD",
		"SEN"}
	return names[kind]
}

// Sort the outsiders and re-init the list; 2*O(n) complexity. DON'T ABUSE OF IT
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
SortOutside
Sort the outsiders; O(n) complexity, don't abuse of it
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
OutsideSortedFindIndexByTime
use a binary tree to find the last index where
p.outsideSorted[index].departureTime <= t
*/
func (p *Population) OutsideSortedFindIndexByTime(t time.Time) int {
	return sort.Search(len(p.outsideSorted), func(i int) bool {
		//f(i) == true => f(i+1) == true
		return p.outsideSorted[i].nextTrip == nil ||
			t.Before(p.outsideSorted[i].nextTrip.departureTime)
	})
}

/*
OutsideSortedInsertPassenger
reinsert a Passenger inside the sorted OutsideSorted.
1% chance of relaunching a sort (to make sure everything works smoothly
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
OutsideSortedPopAllBefore
pop all passengers having a nextTrip before the time
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

// verify that the OutsideSorted list is sorted
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

func (p *Population) Outside() map[string]*Passenger {
	return p.outside
}

func (p *Population) InTrains() []map[string]*Passenger {
	return p.inTrain
}

func (p *Population) InStation() []map[string]*Passenger {
	return p.inStation
}

func (p *Population) InTrain(train MetroTrain) map[string]*Passenger {
	return p.inTrain[train.Id()]
}

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
FindPassenger
find where a passenger is. for debug purposes only
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

// transfer a passenger from the general population to a station
func (p *Population) transferFromPopulationToStation(passenger *Passenger,
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

// transfer a passenger from a station to the general population
func (p *Population) transferFromStationToPopulation(passenger *Passenger,
	stationPt *MetroStation, aTime time.Time) {
	info := prepareCSVline(*passenger, stationPt, aTime, "USE")
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

// transfer a passenger from a train to a station
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
UpdateAll
update the situation in all station (outside <-> station only)
*/
func (p *Population) UpdateAll(aTime time.Time, mapO *Map) {
	// 1. outside > station
	p.UpdateOutsideToStations(aTime)
	// 2. station > outside
	for _, station := range mapO.Stations() {
		p.UpdateStationToOutside(aTime, &station)
	}
}

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
		p.transferFromPopulationToStation(passenger, departureStation, aTime)
	}
}

func (p *Population) UpdateStationToOutside(aTime time.Time,
	station *MetroStation) {
	for i := range p.inStation[station.Id()] {
		passenger := p.inStation[station.Id()][i]
		var trip = passenger.CurrentTrip()
		if trip.Path().EndStation().Id() == station.Id() ||
			passenger.timeArrivalLastStation.Add(
				p.maxTimeInStationPassenger).Before(aTime) {
			//TODO account for time in station --> gate
			trip.SetArrivalTime(aTime)
			p.transferFromStationToPopulation(passenger, station, aTime)

			passenger.ClearCurrentTrip()
			passenger.calculateNextTrip()
			p.OutsideSortedInsertPassenger(passenger)
			//use after the transfer from station to pop
			//println("station->outside: passenger #"+passenger.id)
		}
	}
}

/*
StationToOutside push a passenger outside
*/
func (p *Population) StationToOutside(aTime time.Time, station *MetroStation,
	passenger *Passenger) {
	var trip = passenger.CurrentTrip()
	trip.SetArrivalTime(aTime)
	p.transferFromStationToPopulation(passenger, station, aTime)

	passenger.ClearCurrentTrip()
	passenger.calculateNextTrip()
	p.OutsideSortedInsertPassenger(passenger)
}

func (p *Population) UpdateStationToTrain(train *MetroTrain) {
	station := train.CurrentStation()
	p.inStationMutex.Lock()
	inStation := p.inStation[station.Id()]
	//TODO check above
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
