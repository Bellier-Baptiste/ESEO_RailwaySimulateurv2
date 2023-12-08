/*
File : populationManager_test.go

Brief :	This file runs tests on the populationManager.go file.

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
package models

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"strconv"
	"testing"
	"time"
)

/*
TestCreatePopulation tests the NewPopulation function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestCreatePopulation(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0.5, 0.5, aMap)
	assert.Equal(t, 10, len(population.Outside()))
	fmt.Println(population)

}

/*
TestPopulation_GetPassengersWaitingForTrain tests the GetPassengersWaitingForTrain function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_GetPassengersWaitingForTrain(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(3000, 0, 1, aMap)
	stations := aMap.Stations()
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)
	population.UpdateOutsideToStations(trainDeparture)
	waiting := population.InStation()[stations[0].Id()]
	//fmt.Print(waiting)
	assert.NotNil(t, waiting)
}

/*
TestPopulation_SortOutside tests the SortOutside function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_SortOutside(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0, 1, aMap)

	assert.Equal(t, 10, len(population.outsideSorted))
	assert.NotNil(t, population.outsideSorted[0].nextTrip)

	for i := 0; i < 9; i++ {
		assert.True(t, population.outsideSorted[i].nextTrip.departureTime.Before(population.outsideSorted[i+1].nextTrip.departureTime))
	}
}

/*
TestPopulation_OutsideSortedInsertPassenger tests the OutsideSortedInsertPassenger function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_OutsideSortedInsertPassenger(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0, 1, aMap)

	//assert sorted BEFORE
	assert.True(t, population.OutsideSortedCheckSorted())

	var timeMiddle = population.outsideSorted[5].nextTrip.departureTime.Add(time.Second)
	var timeEnd = population.outsideSorted[9].nextTrip.departureTime.Add(time.Hour)

	someone := NewPassenger("abcd", ADL)
	var n1 = NewTrip(timeEnd, PathStation{})
	someone.AddTrip(&n1)
	population.OutsideSortedInsertPassenger(&someone)

	someone2 := NewPassenger("cdef", ADL)
	var n2 = NewTrip(timeMiddle, PathStation{})
	someone2.AddTrip(&n2)
	population.OutsideSortedInsertPassenger(&someone2)

	someoneNil := NewPassenger("cdef", ADL)
	population.OutsideSortedInsertPassenger(&someoneNil)

	//assert sorted AFTER
	assert.True(t, population.OutsideSortedCheckSorted())
}

/*
TestPopulation_OutsideSortedPopAllBefore tests the OutsideSortedPopAllBefore function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_OutsideSortedPopAllBefore(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0, 1, aMap)

	//assert sorted BEFORE
	for i := 0; i < len(population.outsideSorted)-1; i++ {
		assert.True(t, population.outsideSorted[i].nextTrip.departureTime.Before(population.outsideSorted[i+1].nextTrip.departureTime))
	}

	var timeMiddle = population.outsideSorted[4].nextTrip.departureTime.Add(time.Second)

	var passengers = population.OutsideSortedPopAllBefore(timeMiddle)

	for i := range passengers {
		assert.True(t, passengers[i].nextTrip.departureTime.Before(timeMiddle))
	}

	assert.Equal(t, 5, len(passengers))
	assert.Equal(t, 5, len(population.outsideSorted))
}

/*
TestPopulation_StationExitPop_1Passenger tests the StationExitPop function with only one passenger in the station.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_StationExitPop_1Passenger(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(1, 0, 1, aMap)
	stations := aMap.Stations()
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)

	waitingEmpty := map[string]*Passenger{}
	myPop := population.Passengers()

	/*
		Get the passenger and check if he is outside
	*/
	testString := population.FindPassenger(*myPop["0"])
	assert.Equal(t, "outside", testString)

	/*
		Set the current trip of the passenger to the next trip
	*/
	myPop["0"].SetCurrentTrip(myPop["0"].NextTrip())
	trip := myPop["0"].CurrentTrip()
	fmt.Println(trip)

	/*
		Transfer the passenger to the station and check if he is in the station
	*/
	population.TransferFromPopulationToStation(myPop["0"], aMap.Stations2()[0], trainDeparture)
	testString = population.FindPassenger(*myPop["0"])
	assert.Equal(t, "inStation #0", testString)

	/*
		Check if the station is not empty
	*/
	waiting := population.InStation()[stations[0].Id()]
	assert.NotNil(t, waiting)

	/*
		Transfer the passenger to the outside and check if he is outside
	*/
	population.StationExitPop(trainDeparture, aMap.Stations2()[0])
	testString = population.FindPassenger(*myPop["0"])
	assert.Equal(t, "outside", testString)

	/*
		Check if the station is empty
	*/
	waiting = population.InStation()[stations[0].Id()]
	assert.Equal(t, waitingEmpty, waiting)
}

/*
TestPopulation_StationExitPop_2Passengers tests the StationExitPop function with two passengers in the station.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_StationExitPop_2Passengers(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(2, 0, 1, aMap)
	stations := aMap.Stations()
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)

	waitingEmpty := map[string]*Passenger{}
	myPop := population.Passengers()

	i := 0
	for i < 2 {
		/*
			Get the passenger and check if he is outside
		*/
		testString := population.FindPassenger(*myPop[strconv.Itoa(i)])
		assert.Equal(t, "outside", testString)

		/*
			Set the current trip of the passenger to the next trip
		*/
		myPop[strconv.Itoa(i)].SetCurrentTrip(myPop[strconv.Itoa(i)].NextTrip())
		trip := myPop[strconv.Itoa(i)].CurrentTrip()
		fmt.Println(trip)

		/*
			Transfer the passenger to the station and check if he is in the station
		*/
		population.TransferFromPopulationToStation(myPop[strconv.Itoa(i)], aMap.Stations2()[0], trainDeparture)
		testString = population.FindPassenger(*myPop[strconv.Itoa(i)])
		assert.Equal(t, "inStation #0", testString)

		/*
			Check if the station is not empty
		*/
		waiting := population.InStation()[stations[0].Id()]
		assert.NotNil(t, waiting)

		i++
	}

	/*
		Transfer the passengers to the outside
	*/
	population.StationExitPop(trainDeparture, aMap.Stations2()[0])

	i = 0
	for i < 2 {
		/*
			Check if the passengers are outside
		*/
		testString := population.FindPassenger(*myPop[strconv.Itoa(i)])
		assert.Equal(t, "outside", testString)

		i++
	}

	/*
		Check if the station is empty
	*/
	waiting := population.InStation()[stations[0].Id()]
	assert.Equal(t, waitingEmpty, waiting)
}

/*
TestPopulation_AllStationExitPop tests the AllStationExitPop function.

# It tests if the function works properly

Input : t *testing.T

Expected : The function works properly
*/
func TestPopulation_AllStationExitPop(t *testing.T) {
	/*
		Set the population and the map
		Size should be inferior or equal to the number of stations
	*/
	size := 2
	aMap, _ := CreateMap()
	population := NewPopulation(size, 0, 1, aMap)
	stations := aMap.Stations()
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)

	waitingEmpty := map[string]*Passenger{}
	myPop := population.Passengers()

	i := 0
	for i < size {
		/*
			Get the passenger and check if he is outside
		*/
		testString := population.FindPassenger(*myPop[strconv.Itoa(i)])
		assert.Equal(t, "outside", testString)

		/*
			Set the current trip of the passenger to the next trip
		*/
		myPop[strconv.Itoa(i)].SetCurrentTrip(myPop[strconv.Itoa(i)].NextTrip())
		trip := myPop[strconv.Itoa(i)].CurrentTrip()
		fmt.Println(trip)

		/*
			Transfer the passenger to the station and check if he is in the station
		*/
		population.TransferFromPopulationToStation(myPop[strconv.Itoa(i)], aMap.Stations2()[i], trainDeparture)
		testString = population.FindPassenger(*myPop[strconv.Itoa(i)])
		strTest := "inStation #" + strconv.Itoa(i)
		assert.Equal(t, strTest, testString)

		/*
			Check if the station is not empty
		*/
		waiting := population.InStation()[stations[i].Id()]
		assert.NotNil(t, waiting)

		i++
	}

	/*
		Transfer all the passengers to the outside and check if they are outside
	*/
	population.AllStationsExitPop(trainDeparture, &aMap)

	i = 0
	for i < size {

		testString := population.FindPassenger(*myPop[strconv.Itoa(i)])
		assert.Equal(t, "outside", testString)

		/*
			Check if the station is empty
		*/
		waiting := population.InStation()[stations[i].Id()]
		assert.Equal(t, waitingEmpty, waiting)
		i++
	}
}

func TestPopulation_IsStationEmpty(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(1, 0, 1, aMap)
	station := &aMap.Stations()[0]
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)

	myPop := population.Passengers()

	/*
		Check if the station is empty
	*/
	isStationEmpty := population.IsStationEmpty(station)
	assert.True(t, isStationEmpty)

	/*
		Set the current trip of the passenger to the next trip
	*/
	myPop["0"].SetCurrentTrip(myPop["0"].NextTrip())
	trip := myPop["0"].CurrentTrip()
	fmt.Println(trip)

	/*
		Transfer the passenger to the station
	*/
	population.TransferFromPopulationToStation(myPop["0"], aMap.Stations2()[0], trainDeparture)

	/*
		Check if the station is not empty
	*/
	isStationEmpty = population.IsStationEmpty(station)
	assert.False(t, isStationEmpty)
}

func TestPopulation_IsTrainEmpty(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(1, 0, 1, aMap)
	train := NewMetroTrain(aMap.Lines()[0], "up")
	trainDeparture := time.Date(2018, 10, 12, 20, 00, 0, 0, time.UTC)

	myPop := population.Passengers()

	/*
		Check if the train is empty
	*/
	isTrainEmpty := population.IsTrainEmpty(train)
	assert.True(t, isTrainEmpty)

	/*
		Set the current trip of the passenger to the next trip
	*/
	myPop["0"].SetCurrentTrip(myPop["0"].NextTrip())
	trip := myPop["0"].CurrentTrip()
	fmt.Println(trip)

	/*
		Transfer the passenger to the train
	*/
	population.TransferFromPopulationToStation(myPop["0"], aMap.Stations2()[0], trainDeparture)
	population.transferFromStationToTrain(myPop["0"], train, aMap.Stations2()[0])

	/*
		Check if the train is not empty
	*/
	isTrainEmpty = population.IsTrainEmpty(train)
	assert.False(t, isTrainEmpty)
}
