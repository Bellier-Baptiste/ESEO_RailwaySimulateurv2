/*
File : populationManager_test.go

Brief :

Date : N/A

Author : Team v1, Team v2, Paul TRÉMOUREUX (quality check)

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
	"testing"
	"time"
)

func TestCreatePopulation(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0.5, 0.5, aMap)
	assert.Equal(t, 10, len(population.Outside()))
	fmt.Println(population)

}

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

func TestPopulation_SortOutside(t *testing.T) {
	aMap, _ := CreateMap()
	population := NewPopulation(10, 0, 1, aMap)

	assert.Equal(t, 10, len(population.outsideSorted))
	assert.NotNil(t, population.outsideSorted[0].nextTrip)

	for i := 0; i < 9; i++ {
		assert.True(t, population.outsideSorted[i].nextTrip.departureTime.Before(population.outsideSorted[i+1].nextTrip.departureTime))
	}
}

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
