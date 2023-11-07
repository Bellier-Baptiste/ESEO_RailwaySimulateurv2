/*
File : timetableReal_test.go

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
	"github.com/stretchr/testify/assert"
	"testing"
	"time"
)

var timetableRealTest TimetableReal
var eventTrainTest *EventTimetableTrain
var startTime time.Time
var timeInStation int
var timeBetweenStation int

// var stopTime time.Time
var trainTest = MetroTrain{id: 1, line: &linesTest[0], direction: "up", capacity: 120}

func initForTimetableRealTest() {
	timetableRealTest = NewTimetableReal()
	startTime = time.Date(2018, 12, 7, 0, 0, 0, 0, time.UTC)
	//stopTime = time.Date(2018, 12, 8, 0, 0, 0, 0, time.UTC)
	timeInStation = 60
	timeBetweenStation = 180
	metroStationTab := [2]MetroStation{stationsTest[0], stationsTest[1]}
	nextStartTime := startTime.Add(time.Duration(timeInStation+timeBetweenStation) * time.Second)
	departureTime := startTime.Add(time.Duration(timeInStation) * time.Second)
	nextDepartureTime := startTime.Add(time.Duration(2*timeInStation+timeBetweenStation) * time.Second)
	timeTab := [4]time.Time{startTime, nextStartTime, departureTime, nextDepartureTime}
	eventTrainTest = NewEventTimetableTrain(&linesTest[0], metroStationTab, &trainTest, "up", timeTab, false, 0)
}

func TestAddEventsTrain(t *testing.T) {
	initForTimetableRealTest()

	timetableRealTest.AddEventsTrain(eventTrainTest)

	assert.NotNil(t, timetableRealTest, "the timetable should not be empty")
	assert.Equal(t, 1, len(timetableRealTest.GetEventsTrain()), "the timetable should have 1 event")
	assert.Equal(t, linesTest[0].Name(), timetableRealTest.GetEventsTrain()[0].Line().Name(), "name of the line in the event")
}

func TestTimetableRealToCSV(t *testing.T) {
	initForTimetableRealTest()

	timetableRealTest.AddEventsTrain(eventTrainTest)
	timetableRealTest.ToCSV()
	//check csv file manually
}
