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
var stopTime time.Time
var train_test = MetroTrain{id: 1, line: &linesTest[0], direction: "up", capacity: 120}

func initForTimetableRealTest() {
	timetableRealTest = NewTimetableReal()
	startTime = time.Date(2018, 12, 7, 0, 0, 0, 0, time.UTC)
	stopTime = time.Date(2018, 12, 8, 0, 0, 0, 0, time.UTC)
	timeInStation = 60
	timeBetweenStation = 180
	metroStationTab := [2]MetroStation{stationsTest[0], stationsTest[1]}
	nextStartTime := startTime.Add(time.Duration(timeInStation+timeBetweenStation) * time.Second)
	departureTime := startTime.Add(time.Duration(timeInStation) * time.Second)
	nextDepartureTime := startTime.Add(time.Duration(2*timeInStation+timeBetweenStation) * time.Second)
	timeTab := [4]time.Time{startTime, nextStartTime, departureTime, nextDepartureTime}
	eventTrainTest = NewEventTimetableTrain(&linesTest[0], metroStationTab, &train_test, "up", timeTab, false, 0)
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
