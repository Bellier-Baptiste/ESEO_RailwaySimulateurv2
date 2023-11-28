/*
File : simulator_test.go

Brief : simulator_test.go contains the tests of the simulator package.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Benoît VAVASSEUR
  - Paul TRÉMOUREUX (quality check)
  - Aurélie CHAMOULEAU

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
	"network-journey-simulator/src/configs"
	"os"
	"path/filepath"
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

/*
beforeAll() is called before all tests.
*/
func beforeAll() {
	var config = configs.GetInstance()
	config.Reload()
}

/*
afterAll() is called after all tests.
*/
func afterAll() {}

/*
TestMain() is called before all tests. It calls beforeAll() and afterAll().

# It tests if the beforeAll() and afterAll() functions work properly

Input : m *testing.M

Expected : The beforeAll() and afterAll() functions work properly
*/
func TestMain(m *testing.M) {
	println("*** simulator_test.go ***")
	beforeAll()
	retCode := m.Run()
	afterAll()
	os.Exit(retCode)
}

/*
TestSimulator_SimulatorInit() tests the Init() method of the Simulator struct.

# It tests if the Init() method works properly

Input : t *testing.T

Expected : The Init() method works properly
*/
func TestSimulator_SimulatorInit(t *testing.T) {
	println("TestSimulator_SimulatorInit")
	var sim = NewSimulator()
	println(sim.tripNumberCounter)
	var output, err = sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
}

/*
TestSimulator_RunOnce() tests the RunOnce() method of the Simulator struct.

# It tests if the RunOnce() method works properly

Input : t *testing.T

Expected : The RunOnce() method works properly
*/
func TestSimulator_RunOnce(t *testing.T) {
	println("TestSimulator_RunOnce")
	//var basePath = os.Getenv("GOPATH")
	var currentPath, _ = os.Getwd()
	var basePath = strings.Replace(currentPath, "src\\main", "", -1)
	var projectPath = "../"
	var configPath = filepath.Join(basePath, projectPath, "configs/config.json")
	var stationsPath = filepath.Join(basePath, projectPath, "configs/nameStationList.json")
	var linesPath = filepath.Join(basePath, projectPath, "configs/nameLineList.json")
	var config = configs.ConfigurationType{}
	config.Load(configPath, stationsPath, linesPath)

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

/*
TestSimulator_RunMultiple() tests the RunMultiple() method of the Simulator struct.

# It tests if the RunMultiple() method works properly

Input : t *testing.T

Expected : The RunMultiple() method works properly
*/
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

/*
TestSimulator_Run() tests the Run() method of the Simulator struct.

# It tests if the Run() method works properly

Input : t *testing.T

Expected : The Run() method works properly
*/
/*
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
*/

/*
TestSimulator_TripNumber() tests the TripNumber() method of the Simulator struct.

# It tests if the TripNumber() method works properly

Input : t *testing.T

Expected : The TripNumber() method works properly
*/
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

/*
TestSimulator_EventLineClosed() tests the GetAllEventsLineClosed() method of the Simulator struct.

# It tests if the GetAllEventsLineClosed() method works properly

Input : t *testing.T

Expected : The GetAllEventsLineClosed() method works properly
*/
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

/*
TestSimulator_EventStationClosed() tests the GetAllEventsStationClosed() method of the Simulator struct.

# It tests if the GetAllEventsStationClosed() method works properly

Input : t *testing.T

Expected : The GetAllEventsStationClosed() method works properly
*/
func TestSimulator_EventStationClosed(t *testing.T) {
	println("TestSimulator_EventStationClosed")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)
	eventsStationClosed := sim.GetAllEventsStationClosed()

	assert.True(t, len(eventsStationClosed) != 0, "Array of events (station closed) not initialized")

	assert.Equal(t, timeStart, eventsStationClosed[0].Start().Format(time.RFC3339), "Bad Start time")
	assert.Equal(t, timeEnd, eventsStationClosed[0].End().Format(time.RFC3339), "Bad End time")
	assert.Equal(t, 2, eventsStationClosed[0].IdStation(), "Bad station id")
}

/*
TestSimulator_EventAttendancePeak() tests the GetAllEventsAttendancePeak() method of the Simulator struct.

# It tests if the GetAllEventsAttendancePeak() method works properly

Input : t *testing.T

Expected : The GetAllEventsAttendancePeak() method works properly
*/
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

/*
TestSimulator_GetAllPopulationsDistribution() tests the
GetAllPopulationsDistribution() method of the Simulator struct.

# It tests if the GetAllPopulationsDistribution() method works properly

Input : t *testing.T

Expected : The GetAllPopulationsDistribution() method works properly
*/
func TestSimulator_GetAllPopulationsDistribution(t *testing.T) {
	println("TestSimulator_GetAllPopulationsDistribution")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	populationDistribution := sim.GetAllPopulationsDistribution()

	assert.Equal(t, 20, populationDistribution[0].Businessman(),
		"Bad businessman attribut")
	assert.Equal(t, 10, populationDistribution[0].Child(),
		"Bad child attribut")
	assert.Equal(t, 10, populationDistribution[0].Retired(),
		"Bad retired attribut")
	assert.Equal(t, 10, populationDistribution[0].Student(),
		"Bad student attribut")
	assert.Equal(t, 10, populationDistribution[0].Tourist(),
		"Bad tourist attribut")
	assert.Equal(t, 30, populationDistribution[0].Unemployed(),
		"Bad unemployed attribut")
	assert.Equal(t, 10, populationDistribution[0].Worker(),
		"Bad worker attribut")

	assert.Equal(t, 0, populationDistribution[1].Businessman(),
		"Bad businessman attribut")
	assert.Equal(t, 30, populationDistribution[1].Child(),
		"Bad child attribut")
	assert.Equal(t, 10, populationDistribution[1].Retired(),
		"Bad retired attribut")
	assert.Equal(t, 10, populationDistribution[1].Student(),
		"Bad student attribut")
	assert.Equal(t, 10, populationDistribution[1].Tourist(),
		"Bad tourist attribut")
	assert.Equal(t, 30, populationDistribution[1].Unemployed(),
		"Bad unemployed attribut")
	assert.Equal(t, 10, populationDistribution[1].Worker(),
		"Bad worker attribut")
}

/*
TestSimulator_GetAllDestinationDistribution() tests the
GetAllDestinationDistribution() method of the Simulator struct.

# It tests if the GetAllDestinationDistribution() method works properly

Input : t *testing.T

Expected : The GetAllDestinationDistribution() method works properly
*/
func TestSimulator_GetAllDestinationDistribution(t *testing.T) {
	println("TestSimulator_GetAllDestinationDistribution")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	destinationDistribution := sim.GetAllDestinationDistribution()

	assert.Equal(t, 20, destinationDistribution[0].Commercial(),
		"Bad commercial attribut")
	assert.Equal(t, 10, destinationDistribution[0].Educational(),
		"Bad educational attribut")
	assert.Equal(t, 10, destinationDistribution[0].Industrial(),
		"Bad industrial attribut")
	assert.Equal(t, 10, destinationDistribution[0].Leisure(),
		"Bad leisure attribut")
	assert.Equal(t, 10, destinationDistribution[0].Office(),
		"Bad office attribut")
	assert.Equal(t, 30, destinationDistribution[0].Residential(),
		"Bad residential attribut")
	assert.Equal(t, 10, destinationDistribution[0].Touristic(),
		"Bad touristic attribut")

	assert.Equal(t, 0, destinationDistribution[1].Commercial(),
		"Bad commercial attribut")
	assert.Equal(t, 30, destinationDistribution[1].Educational(),
		"Bad educational attribut")
	assert.Equal(t, 10, destinationDistribution[1].Industrial(),
		"Bad industrial attribut")
	assert.Equal(t, 10, destinationDistribution[1].Leisure(),
		"Bad leisure attribut")
	assert.Equal(t, 10, destinationDistribution[1].Office(),
		"Bad office attribut")
	assert.Equal(t, 30, destinationDistribution[1].Residential(),
		"Bad residential attribut")
	assert.Equal(t, 10, destinationDistribution[1].Touristic(),
		"Bad touristic attribut")
}

/*
TestSimulator_GetPopulationDistributionArea() tests the
GetPopulationDistributionArea() method of the Simulator struct.

# It tests if the GetPopulationDistributionArea() method works properly

Input : t *testing.T

Expected : The GetPopulationDistributionArea() method works properly
*/
func TestSimulator_GetPopulationDistributionArea(t *testing.T) {
	println("TestSimulator_GetPopulationDistributionArea")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	populationDistribution := sim.GetPopulationDistributionArea(0)

	assert.Equal(t, 20, populationDistribution.Businessman(),
		"Bad businessman attribut")
	assert.Equal(t, 10, populationDistribution.Child(),
		"Bad child attribut")
	assert.Equal(t, 10, populationDistribution.Retired(),
		"Bad retired attribut")
	assert.Equal(t, 10, populationDistribution.Student(),
		"Bad student attribut")
	assert.Equal(t, 10, populationDistribution.Tourist(),
		"Bad tourist attribut")
	assert.Equal(t, 30, populationDistribution.Unemployed(),
		"Bad unemployed attribut")
	assert.Equal(t, 10, populationDistribution.Worker(),
		"Bad worker attribut")
}

/*
TestSimulator_GetDestinationDistributionArea() tests the
GetDestinationDistributionArea() method of the Simulator struct.

# It tests if the GetDestinationDistributionArea() method works properly

Input : t *testing.T

Expected : The GetDestinationDistributionArea() method works properly
*/
func TestSimulator_GetDestinationDistributionArea(t *testing.T) {
	println("TestSimulator_GetDestinationDistributionArea")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	destinationDistribution := sim.GetDestinationDistributionArea(0)

	assert.Equal(t, 20, destinationDistribution.Commercial(),
		"Bad commercial attribut")
	assert.Equal(t, 10, destinationDistribution.Educational(),
		"Bad educational attribut")
	assert.Equal(t, 10, destinationDistribution.Industrial(),
		"Bad industrial attribut")
	assert.Equal(t, 10, destinationDistribution.Leisure(),
		"Bad leisure attribut")
	assert.Equal(t, 10, destinationDistribution.Office(),
		"Bad office attribut")
	assert.Equal(t, 30, destinationDistribution.Residential(),
		"Bad residential attribut")
	assert.Equal(t, 10, destinationDistribution.Touristic(),
		"Bad touristic attribut")
}

/*
TestSimulator_GetIdAreaStations tests the GetIdAreaStations() method of the
Simulator struct.

# It tests if the GetIdAreaStations() method works properly

Input : t *testing.T

Expected : The GetIdAreaStations() method works properly
*/
func TestSimulator_GetIdAreaStations(t *testing.T) {
	println("TestSimulator_GetIdAreaMetroStations")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	idAreas := sim.GetIdAreaStations()

	assert.Equal(t, -1, idAreas[0],
		"Bad id attribut")
	assert.Equal(t, -1, idAreas[1],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[2],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[3],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[4],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[5],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[6],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[7],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[8],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[9],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[10],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[11],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[12],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[13],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[14],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[15],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[16],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[17],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[18],
		"Bad id attribut")
	assert.Equal(t, 0, idAreas[19],
		"Bad id attribut")
}

/*
TestSimulator_CreatePopulationsDistribution tests the
CreatePopulationsDistribution() method of the Simulator struct.

# It tests if the CreatePopulationsDistribution() method works properly

Input : t *testing.T

Expected : The CreatePopulationsDistribution() method works properly
*/
func TestSimulator_CreatePopulationsDistribution(t *testing.T) {
	println("TestSimulator_createAreas")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	sim.CreatePopulationsDistribution()

	assert.Equal(t, 2, len(sim.populationsDistributions),
		"Bad number of populationsDistributions")
}

/*
TestSimulator_CreateDestinationDistribution tests the
CreateDestinationDistribution() method of the Simulator struct.

# It tests if the CreateDestinationDistribution() method works properly

Input : t *testing.T

Expected : The CreateDestinationDistribution() method works properly
*/
func TestSimulator_CreateDestinationDistribution(t *testing.T) {
	println("TestSimulator_createAreas")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	sim.CreateDestinationDistribution()

	assert.Equal(t, 2, len(sim.destinationDistributions),
		"Bad number of destinationDistributions")
}

/*
TestSimulator_GetPopulationDistributionStation() tests the
GetPopulationDistributionStation(idArea int) method of the Simulator struct.

# It tests if the GetPopulationDistributionStation(idArea int) method works
properly

Input : t *testing.T

Expected : The GetPopulationDistributionStation(idArea int) method works
properly
*/
func TestSimulator_GetPopulationDistributionStation(t *testing.T) {
	println("TestSimulator_GetPopulationDistributionStation")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	// Station with id 3 is part of the area with id 0
	populationDistribution := sim.GetPopulationDistributionStation(3)

	assert.Equal(t, 20, populationDistribution.Businessman(),
		"Bad businessman attribut")
	assert.Equal(t, 10, populationDistribution.Child(),
		"Bad child attribut")
	assert.Equal(t, 10, populationDistribution.Retired(),
		"Bad retired attribut")
	assert.Equal(t, 10, populationDistribution.Student(),
		"Bad student attribut")
	assert.Equal(t, 10, populationDistribution.Tourist(),
		"Bad tourist attribut")
	assert.Equal(t, 30, populationDistribution.Unemployed(),
		"Bad unemployed attribut")
	assert.Equal(t, 10, populationDistribution.Worker(),
		"Bad worker attribut")

	// Station with id 0 is not part of any area, so it should return a
	// PopulationDistribution with all default attributes
	populationDistribution = sim.GetPopulationDistributionStation(0)

	assert.Equal(t, 14, populationDistribution.Businessman(),
		"Bad businessman attribut")
	assert.Equal(t, 14, populationDistribution.Child(),
		"Bad child attribut")
	assert.Equal(t, 14, populationDistribution.Retired(),
		"Bad retired attribut")
	assert.Equal(t, 15, populationDistribution.Student(),
		"Bad student attribut")
	assert.Equal(t, 14, populationDistribution.Tourist(),
		"Bad tourist attribut")
	assert.Equal(t, 14, populationDistribution.Unemployed(),
		"Bad unemployed attribut")
	assert.Equal(t, 15, populationDistribution.Worker(),
		"Bad worker attribut")
}

/*
TestSimulator_GetDestinationDistributionStation() tests the
GetDestinationDistributionStation(idArea int) method of the Simulator struct.

# It tests if the GetDestinationDistributionStation(idArea int) method works
properly

Input : t *testing.T

Expected : The GetDestinationDistributionStation(idArea int) method works
*/
func TestSimulator_GetDestinationDistributionStation(t *testing.T) {
	println("TestSimulator_GetDestinationDistributionStation")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	// Station of id 3 is in the area of id 0 so it should return its
	// distribution
	destinationDistribution := sim.GetDestinationDistributionStation(3)

	assert.Equal(t, 20, destinationDistribution.Commercial(),
		"Bad commercial attribut")
	assert.Equal(t, 10, destinationDistribution.Educational(),
		"Bad educational attribut")
	assert.Equal(t, 10, destinationDistribution.Industrial(),
		"Bad industrial attribut")
	assert.Equal(t, 10, destinationDistribution.Leisure(),
		"Bad leisure attribut")
	assert.Equal(t, 10, destinationDistribution.Office(),
		"Bad office attribut")
	assert.Equal(t, 30, destinationDistribution.Residential(),
		"Bad residential attribut")
	assert.Equal(t, 10, destinationDistribution.Touristic(),
		"Bad touristic attribut")

	// Station of id 0 is not in any area so it should return a distribution
	// with all default values
	destinationDistribution = sim.GetDestinationDistributionStation(0)

	assert.Equal(t, 15, destinationDistribution.Commercial(),
		"Bad commercial attribut")
	assert.Equal(t, 14, destinationDistribution.Educational(),
		"Bad educational attribut")
	assert.Equal(t, 14, destinationDistribution.Industrial(),
		"Bad industrial attribut")
	assert.Equal(t, 14, destinationDistribution.Leisure(),
		"Bad leisure attribut")
	assert.Equal(t, 15, destinationDistribution.Office(),
		"Bad office attribut")
	assert.Equal(t, 14, destinationDistribution.Residential(),
		"Bad residential attribut")
	assert.Equal(t, 14, destinationDistribution.Touristic(),
		"Bad touristic attribut")
}

/*
TestSimulator_GetIdAreaStation() tests the GetIdAreaStation(idStation int)
method of the Simulator struct.

# It tests if the GetIdAreaStation(idStation int) method works properly

Input : t *testing.T

Expected : The GetIdAreaStation(idStation int) method works properly
*/

func TestSimulator_GetIdAreaStation(t *testing.T) {
	println("TestSimulator_GetIdAreaStation")
	sim := NewSimulator()
	output, err := sim.Init("working day")
	assert.Nil(t, err)
	assert.True(t, output)

	idArea1 := sim.GetIdAreaStation(0)
	idArea2 := sim.GetIdAreaStation(3)

	assert.Equal(t, -1, idArea1,
		"Bad idArea attribut")
	assert.Equal(t, 0, idArea2,
		"Bad idArea attribut")
}
