/*
File : timetable_test.go

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

/*
Unused variables
var trains_test []*MetroTrain
var times_test []time.Time
var timetable_test = Timetable{}
*/

/**
specific config.json needed :
"lines": 1,
"interchange stations": 0,
"population": 1000,
"stations": 7,
"trains per line": 4,
"capacity per train": 100,
"time between stations min": 100,
"time between stations max": 500,
"time in station": 60,
"start time": "2018-10-12T00:00:00.000Z",
"stop time": "2018-10-13T00:00:00.000Z",
*/

/*//init
func initForTimetable() ([]*MetroTrain, *EventTimetableTrain){
	//init line and station
	lines_test[0].AddMetroStation(&stations_test[0])
	lines_test[0].AddMetroStation(&stations_test[1])
	lines_test[0].AddMetroStation(&stations_test[2])
	stations_test[0].AddMetroLine(&lines_test[0])
	stations_test[1].AddMetroLine(&lines_test[0])
	stations_test[2].AddMetroLine(&lines_test[0])
	//create trains
	var trains_test1 = NewMetroTrain(&lines_test[0], "up")
	var trains_test2 = NewMetroTrain(&lines_test[0], "up")
	var trains_test3 = NewMetroTrain(&lines_test[0], "up")
	var trains_test4 = NewMetroTrain(&lines_test[0], "up")
	trains_test = []*MetroTrain{trains_test1, trains_test2, trains_test3, trains_test4}
	//id for trains
	trains_test1.SetId(0)
	trains_test2.SetId(1)
	trains_test3.SetId(2)
	trains_test4.SetId(3)
	//eventTrain
	event := NewEventTimetableTrain(&lines_test[0], &stations_test[0], &stations_test[1], trains_test1, "up", "", "", "", "", false, 0)
	return trains_test, event
}

//test addToTimetable
func TestAddToTimetable(t *testing.T) {


	assert.NotNil(t, timetable_test)

	//add the same timetableStation, it should not be added


	assert.NotNil(t, timetable_test)

}

//test generation
func TestGenerateTimetable(t *testing.T) {
	trains_test, _ := initForTimetable()
	//create a map
	theMap, err := CreateMap()
	if err != nil {
		return
	}

	mapPointer := &theMap

	//generate the timetable
	timetable_test.GenerateTimetable(mapPointer, trains_test)

	assert.NotNil(t, timetable_test)
	//with 1 line and 7 stations, we should have 12 timetableStations
	assert.Equal(t, len(timetable_test.timetableStationList), 12, "timetableStationList should have 12 elements")
	//the 1st elemetn should be the 1st station of the 1st line
	assert.Equal(t, timetable_test.timetableStationList[0].station.name, "station1", "name of the station of the 1st timetableStation should be the 1st station of the 1st line")
	assert.Equal(t, timetable_test.timetableStationList[0].line.name, "A", "name of the line of the 1st timetableStation should be the 1st station of the 1st line")
}

func TestCreateTimetableCSV(t *testing.T) {
	//create the map
	map_test, _ := CreateMap()
	//create trains
	var trains_test1 = NewMetroTrain(&map_test.Lines()[0], "up")
	trains_test1.SetId(0)
	var trains_test2 = NewMetroTrain(&map_test.Lines()[0], "up")
	trains_test2.SetId(1)
	var trains_test3 = NewMetroTrain(&map_test.Lines()[0], "up")
	trains_test3.SetId(2)
	var trains_test4 = NewMetroTrain(&map_test.Lines()[0], "up")
	trains_test4.SetId(3)
	var trains_test = []*MetroTrain{trains_test1, trains_test2, trains_test3, trains_test4}
	//create the timetable
	var timetable = NewTimetable(&map_test, trains_test)
	//create the gobal timetable CSV
	timetable.ToCSV()
}*/
