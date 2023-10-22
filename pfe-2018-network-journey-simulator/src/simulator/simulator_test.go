/*
File : simulator_test.go

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
package simulator

import (
	"os"
	"path/filepath"
	"pfe-2018-network-journey-simulator/src/configs"
	"strings"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

const (
	timeStart        = "2018-10-26T10:01:00Z"
	timeEnd          = "2018-10-26T10:02:00Z"
	elc_stationStart = 1
	elc_stationEnd   = 19
	eap_stationStart = 2
	eap_size         = 10
)

func beforeAll() {
	var config = configs.GetInstance()
	config.Reload()
}

func afterAll() {}

func TestMain(m *testing.M) {
	println("*** simulator_test.go ***")
	beforeAll()
	retCode := m.Run()
	afterAll()
	os.Exit(retCode)
}

func TestSimulator_SimulatorInit(t *testing.T) {
	println("TestSimulator_SimulatorInit")
	var sim = NewSimulator()
	println(sim.tripNumberCounter)
	var output, err = sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
}

func TestSimulator_RunOnce(t *testing.T) {
	println("TestSimulator_RunOnce")
	//var basePath = os.Getenv("GOPATH")
	var currentPath, _ = os.Getwd()
	var basePath = strings.Replace(currentPath, "src\\main", "", -1)
	var projectPath = "../"
	var configPath = filepath.Join(basePath, projectPath, "configs/config.json")
	var stationsPath = filepath.Join(basePath, projectPath, "configs/nameStationList.json")
	var linesPath = filepath.Join(basePath, projectPath, "configs/nameLineList.json")
	configs.Load(configPath, stationsPath, linesPath)

	sim := NewSimulator()

	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
	//TODO only works while modifying config.json; make it so it doesn't

	//println(simulator.mapObject.Lines()[0].String(),"\n")

	//println(simulator.population.String(),"\n")

	var passenger0 = sim.Population().Passengers()["0"]
	assert.NotNil(t, passenger0)

	sim.RunOnce()
}

func TestSimulator_RunMultiple(t *testing.T) {
	println("TestSimulator_RunMultiple")
	sim := NewSimulator()

	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
	//TODO only works while modifying config.json; make it so it doesn't

	//println(simulator.mapObject.Lines()[0].String(),"\n")

	//println(simulator.population.String(),"\n")

	var passenger0 = sim.Population().Passengers()["0"]
	assert.NotNil(t, passenger0)

	for i := 0; i < 100; i++ {
		sim.RunOnce()
		if i%10 == 0 {
			//assert sorted
			assert.True(t, sim.Population().OutsideSortedCheckSorted())
		}
	}
}

func TestSimulator_Run(t *testing.T) {

	var config = configs.GetInstance()
	config.Reload()

	sim := NewSimulator()

	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
	//TODO only works while modifying config.json; make it so it doesn't

	//println(simulator.mapObject.Lines()[0].String(),"\n")

	//println(simulator.population.String(),"\n")

	var passenger0 = sim.Population().Passengers()["0"]
	assert.NotNil(t, passenger0)
	//	println("passenger0 : ", passenger0.String())
	//	println("trips passenger0")
	//	for _, trip := range passenger0.Trips() {
	//		println(trip.Path().String() + "\n")
	//	}

	sim.Run(-1)

	var tripsNotFinished = 0
	var tripsFinished = 0
	for _, passenger := range sim.Population().Passengers() {
		for _, trip := range passenger.Trips() {
			if !trip.IsCompleted() {
				tripsNotFinished++
			} else {
				tripsFinished++
			}
		}
	}

	//	println("tripFinished : " + strconv.Itoa(tripsFinished) + " -+- " + strconv.Itoa(tripsNotFinished) + " : tripNotFinished")
	//	println("passenger0 : ", passenger0.String())
	assert.True(t, tripsFinished > tripsNotFinished)
	//println(simulator.population.String())
}

func TestSimulator_TripNumber(t *testing.T) {
	println("\r")
	println("TestSimulator_TripNumber")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	//	println("number of trains : ", len(sim.GetTrains()))

	//	println("tripNumber 1 : ", sim.GetTrains()[0].GetTripNumber())
	//	println("tripNumber 2 : ", sim.GetTrains()[1].GetTripNumber())

	assert.True(t, sim.GetTrains()[0].GetTripNumber() != sim.GetTrains()[1].GetTripNumber(), "trip number can't be the same for the first two trains")
}

func TestSimulator_EventLineClosed(t *testing.T) {
	println("TestSimulator_EventLineClosed")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
	eventsLineClosed := sim.GetAllEventsLineClosed()

	//	println("len : ", len(eventsLineClosed))

	assert.True(t, (len(eventsLineClosed) != 0), "Array of events (line closed) not initialized")

	assert.Equal(t, timeStart, eventsLineClosed[0].Start().Format(time.RFC3339), "Bad Start time")
	assert.Equal(t, timeEnd, eventsLineClosed[0].End().Format(time.RFC3339), "Bad End time")
	assert.Equal(t, elc_stationStart, eventsLineClosed[0].IdStationStart(), "Bad Start station id")
	assert.Equal(t, elc_stationEnd, eventsLineClosed[0].IdStationEnd(), "Bad End station id")
}

func TestSimulator_EventStationClosed(t *testing.T) {
	println("TestSimulator_EventStationClosed")
	sim := NewSimulator()
	sim.Init("working day")
	eventsStationClosed := sim.GetAllEventsStationClosed()

	//	println("len : ", len(eventsStationClosed))

	assert.True(t, (len(eventsStationClosed) != 0), "Array of events (station closed) not initialized")

	assert.Equal(t, timeStart, eventsStationClosed[0].Start().Format(time.RFC3339), "Bad Start time")
	assert.Equal(t, timeEnd, eventsStationClosed[0].End().Format(time.RFC3339), "Bad End time")
	assert.Equal(t, 2, eventsStationClosed[0].IdStation(), "Bad station id")
}

func TestSimulator_EventAttendancePeak(t *testing.T) {
	println("TestSimulator_EventAttendancePeak")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	eventsAttendancePeak := sim.GetAllEventsAttendancePeak()

	println("len : ", len(eventsAttendancePeak))

	assert.True(t, len(eventsAttendancePeak) != 0, "Array of events (attendance peak) not initialized")

	assert.Equal(t, timeStart, eventsAttendancePeak[0].Time().Format(time.RFC3339), "Bad Time attribut")
	assert.Equal(t, eap_stationStart, eventsAttendancePeak[0].IdStation(), "Bad station id attribut")
	assert.Equal(t, eap_size, eventsAttendancePeak[0].Size(), "Bad size attribut")
}
