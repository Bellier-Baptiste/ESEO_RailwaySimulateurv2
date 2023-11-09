/*
Package simulator

File : simulator.go

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
	"fmt"
	"math/rand"
	"pfe-2018-network-journey-simulator/src/configs"
	"pfe-2018-network-journey-simulator/src/models"
	"strconv"
	"sync"
	"time"
)

type Simulator struct {
	config               configs.ConfigurationObject
	adConfig             *configs.AdvancedConfig
	mapObject            models.Map
	population           *models.Population
	trains               []*models.MetroTrain
	currentTime          time.Time
	timetable            models.Timetable
	timetableReal        models.TimetableReal
	eventsStationClosed  []models.EventStationClosed
	eventsLineDelay      []models.EventLineDelay
	eventsLineClosed     []models.EventLineClosed
	eventsAttendancePeak []models.EventAttendancePeak
	tripNumberCounter    int
}

const (
	strErr = " error : "
)

func (s *Simulator) Config() configs.ConfigurationObject {
	return s.config
}

func (s *Simulator) GetTrains() []*models.MetroTrain {
	return s.trains
}

func NewSimulator() *Simulator {
	simulator := &Simulator{
		config:               configs.GetInstance(),
		adConfig:             nil,
		mapObject:            models.Map{},
		population:           nil,
		trains:               make([]*models.MetroTrain, 0),
		currentTime:          time.Now(),
		timetable:            models.Timetable{},
		timetableReal:        models.TimetableReal{},
		eventsStationClosed:  make([]models.EventStationClosed, 0),
		eventsLineDelay:      make([]models.EventLineDelay, 0),
		eventsLineClosed:     make([]models.EventLineClosed, 0),
		eventsAttendancePeak: make([]models.EventAttendancePeak, 0),
		tripNumberCounter:    0,
	}
	return simulator
}

func (s *Simulator) Population() *models.Population {
	return s.population
}

func (s *Simulator) GetAllEventsLineClosed() []models.EventLineClosed {
	return s.eventsLineClosed
}

func (s *Simulator) GetAllEventsAttendancePeak() []models.EventAttendancePeak {
	return s.eventsAttendancePeak
}

func (s *Simulator) GetAllEventsStationClosed() []models.EventStationClosed {
	return s.eventsStationClosed
}

/*
CreateEventsStationClose is used to create "station close" event
*/
func (s *Simulator) CreateEventsStationClose() {
	s.eventsStationClosed = make([]models.EventStationClosed,
		len(s.adConfig.MapC.EventsStationClosed))
	for i, ev := range s.adConfig.MapC.EventsStationClosed {
		start, err := time.Parse(time.RFC3339, ev.StartString)
		if err != nil {
			fmt.Print("EventStationClosed : couldn't parse date : ",
				start, strErr, err)
			continue
		}
		end, err := time.Parse(time.RFC3339, ev.EndString)
		if err != nil {
			fmt.Print("EventStationClosed : couldn't parse date : ",
				end, strErr, err)
			continue
		}

		s.eventsStationClosed[i] = models.NewEventStationClosed(ev.IdStation,
			start, end)
	}
}

/*
CreateEventsLineDelay is used to create "line delay" event
*/
func (s *Simulator) CreateEventsLineDelay() {
	s.eventsLineDelay = make([]models.EventLineDelay,
		len(s.adConfig.MapC.EventsLineDelay))
	for i, ev := range s.adConfig.MapC.EventsLineDelay {
		start, err := time.Parse(time.RFC3339, ev.StartString)
		if err != nil {
			fmt.Print("EventLineDelay : couldn't parse date : ",
				start, strErr, err)
			continue
		}
		end, err := time.Parse(time.RFC3339, ev.EndString)
		if err != nil {
			fmt.Print("EventLineDelay : couldn't parse date : ",
				end, strErr, err)
			continue
		}
		fmt.Println("EventLineDelay")
		s.eventsLineDelay[i] = models.NewEventLineDelay(ev.StationIdStart,
			ev.StationIdEnd, ev.Delay, start, end)
		fmt.Println("EventLineDelay : ", s.eventsLineDelay[i])
	}
}

/*
CreateEventsLineClose is used to create "line close" event
*/
func (s *Simulator) CreateEventsLineClose() {
	s.eventsLineClosed = make([]models.EventLineClosed,
		len(s.adConfig.MapC.EventsLineClosed))
	for i, ev := range s.adConfig.MapC.EventsLineClosed {
		start, err := time.Parse(time.RFC3339, ev.StartString)
		if err != nil {
			fmt.Print("EventLineClosed : couldn't parse date : ",
				ev.StartString, strErr, err)
			continue
		}
		end, err := time.Parse(time.RFC3339, ev.EndString)
		if err != nil {
			fmt.Print("EventLineClosed : couldn't parse date : ",
				ev.EndString, strErr, err)
			continue
		}

		s.eventsLineClosed[i] = models.NewEventLineClosed(ev.StationIdStart,
			ev.StationIdEnd, start, end)
	}
}

/*
CreateEventsAttendancePeak is used to create "attendance peak" event
*/
func (s *Simulator) CreateEventsAttendancePeak() {
	s.eventsAttendancePeak = make([]models.EventAttendancePeak,
		len(s.adConfig.MapC.EventsAttendancePeak))
	for i, ev := range s.adConfig.MapC.EventsAttendancePeak {
		timeEv, err := time.Parse(time.RFC3339, ev.TimeString)
		if err != nil {
			fmt.Print("EventAttendancePeak : couldn't parse date : ",
				ev.TimeString, strErr, err)
			continue
		}
		s.eventsAttendancePeak[i] = models.NewEventAttendancePeak(ev.StationId,
			ev.Size, timeEv)
	}
}

/*
AddTrainLinePeer is used to add train to line (peer train number)

Param :
line *models.MetroLine
shift int
count int
aux1 int
aux2 int

Return :
*models.MetroLine : line
int : shift
int : count
int : aux1
int : aux2
*/
func (s *Simulator) AddTrainLinePeer(line *models.MetroLine,
	shift, count, aux1, aux2 int) (*models.MetroLine, int, int, int, int) {
	for k := 0; k < line.TrainNumber()/2; k++ {
		shift = k * (models.LineTimeLength(line,
			s.mapObject.GraphTimeBetweenStation(),
			s.mapObject.GraphDelay(),
			len(line.Stations()),
			s.config.TimeInStation()) / len(s.trains))
		//count in base N
		s.trains[count] = models.NewMetroTrain(line, "up")
		s.trains[count].SetTripNumber(s.tripNumberCounter)
		s.tripNumberCounter++
		s.trains[count].SetTimeArrivalCurrentStation(
			s.config.TimeStart().Add(time.Duration(
				shift+s.config.TimeInStation()) * time.Second))
		aux1 = line.Stations()[0].Id()
		aux2 = line.Stations()[1].Id()
		s.trains[count].SetTimeArrivalNextStation(
			s.trains[count].TimeArrivalCurrentStation().Add(
				time.Duration(
					s.mapObject.GraphTimeBetweenStation()[aux1][aux2]+
						s.mapObject.GraphDelay()[aux1][aux2]) * time.Second))
		s.trains[count].SetId(count)
		count++
		s.trains[count] = models.NewMetroTrain(line, "down")
		s.trains[count].SetTripNumber(s.tripNumberCounter)
		s.tripNumberCounter++
		s.trains[count].SetTimeArrivalCurrentStation(
			s.config.TimeStart().Add(time.Duration(
				shift+s.config.TimeInStation()) * time.Second))
		aux1 = line.Stations()[len(line.Stations())-1].Id()
		aux2 = line.Stations()[len(line.Stations())-2].Id()
		s.trains[count].SetTimeArrivalNextStation(
			s.trains[count].TimeArrivalCurrentStation().Add(
				time.Duration(
					s.mapObject.GraphTimeBetweenStation()[aux1][aux2]+
						s.mapObject.GraphDelay()[aux1][aux2]) * time.Second))
		s.trains[count].SetId(count)
		count++

	}
	return line, shift, count, aux1, aux2
}

/*
AddTrainLineOdd is used to add train to line (peer train number)

Param :
line *models.MetroLine
shift int
count int
aux1 int
aux2 int
j int

Return :
*models.MetroLine : line
int : shift
int : count
int : aux1
int : aux2
*/
func (s *Simulator) AddTrainLineOdd(line *models.MetroLine,
	shift, count, aux1, aux2, j int) (*models.MetroLine, int, int, int, int) {
	for l := 0; l < line.TrainNumber(); l++ {
		shift = l * (models.LineTimeLength(s.mapObject.Lines()[j],
			s.mapObject.GraphTimeBetweenStation(),
			s.mapObject.GraphDelay(),
			len(s.mapObject.Lines()[j].Stations()),
			s.config.TimeInStation()) / (2 * len(s.trains)))
		if l%2 == 0 {
			s.trains[count] = models.NewMetroTrain(line, "up")
			s.trains[count].SetTripNumber(s.tripNumberCounter)
			s.tripNumberCounter++
			s.trains[count].SetTimeArrivalCurrentStation(
				s.config.TimeStart().Add(time.Duration(
					shift+s.config.TimeInStation()) * time.Second))
			aux1 = line.Stations()[0].Id()
			aux2 = line.Stations()[1].Id()
			s.trains[count].SetTimeArrivalNextStation(
				s.trains[count].TimeArrivalCurrentStation().Add(
					time.Duration(
						s.mapObject.GraphTimeBetweenStation()[aux1][aux2]+
							s.mapObject.GraphDelay()[aux1][aux2]) * time.Second))
			s.trains[count].SetId(count)
		} else {
			s.trains[count] = models.NewMetroTrain(line, "down")
			s.trains[count].SetTripNumber(s.tripNumberCounter)
			s.tripNumberCounter++
			s.trains[count].SetTimeArrivalCurrentStation(
				s.config.TimeStart().Add(time.Duration(
					shift+s.config.TimeInStation()) * time.Second))
			aux1 = line.Stations()[len(line.Stations())-1].Id()
			aux2 = line.Stations()[len(line.Stations())-2].Id()
			s.trains[count].SetTimeArrivalNextStation(
				s.trains[count].TimeArrivalCurrentStation().Add(
					time.Duration(
						s.mapObject.GraphTimeBetweenStation()[aux1][aux2]+
							s.mapObject.GraphDelay()[aux1][aux2]) * time.Second))
			s.trains[count].SetId(count)
		}
		s.trains[count].SetTimeArrivalCurrentStation(
			s.config.TimeStart().Add(time.Duration(
				shift+s.config.TimeInStation()) * time.Second))
		s.trains[count].SetTimeArrivalNextStation(
			s.trains[count].TimeArrivalCurrentStation().Add(
				time.Duration(
					s.mapObject.GraphTimeBetweenStation()[aux1][aux2]) * time.Second))
		s.trains[count].SetId(count)

		count++
	}
	return line, shift, count, aux1, aux2
}

func (s *Simulator) Init(dayType string) (bool, error) {

	// load config
	s.config = configs.GetInstance()
	s.adConfig = configs.GetAdvancedConfigInstance()

	// initialisation of the day type in the config
	fmt.Println("dayType changed in : ", dayType)
	s.config.ChangeParam("day type", dayType)
	fmt.Println("dayType changed in : ", s.config["day type"], "-> ok !")

	println("Here")
	//generate events
	s.CreateEventsStationClose()

	s.CreateEventsLineDelay()

	s.CreateEventsLineClose()

	s.CreateEventsAttendancePeak()

	// create map
	s.mapObject = models.CreateMapAdvanced(*s.adConfig)

	// create passengers
	s.population = models.NewPopulation(s.config.Population(),
		s.config.PopulationCommutersProportion(),
		s.config.PopulationRandomProportion(), s.mapObject)

	// create trains
	var totalNumberOfTrains = 0
	for i := 0; i < len(s.adConfig.MapC.Lines); i++ {
		totalNumberOfTrains = totalNumberOfTrains +
			s.mapObject.Lines()[i].TrainNumber()
	}
	s.trains = make([]*models.MetroTrain, totalNumberOfTrains)

	// assign trains to lines, direction and station_start
	//trains starts from both side of the line and not at the same time :
	//time between 2 trains should be a constant
	var shift int
	var count = 0
	var aux1 = 0
	var aux2 = 0
	for j := 0; j < len(s.mapObject.Lines()); j++ {
		var line = s.mapObject.Lines()[j]
		if line.TrainNumber()%2 == 0 {
			//peer
			line, shift, count, aux1, aux2 = s.AddTrainLinePeer(line,
				shift, count, aux1, aux2)
		} else {
			//odd
			line, shift, count, aux1, aux2 = s.AddTrainLineOdd(line,
				shift, count, aux1, aux2, j)
		}
	}

	// assign current time
	s.currentTime = s.config.TimeStart()

	if s.Config().PreTimetable() {
		// create timetables
		s.timetable = models.NewTimetable(&s.mapObject, s.GetTrains())
		// save timetable and timetableStations as CSV
		s.timetable.ToCSV()
	}
	// create timetableReal
	s.timetableReal = models.NewTimetableReal()

	//init tripNumberCounter
	s.tripNumberCounter = 0

	return true, nil
}

// -------- FUNCTIONS & METHODS

/*
Run	is used to run n loops (one loop == one event).
put n < 0 in order to run until the end of the period defined in config.json
*/
func (s *Simulator) Run(n int) {
	var conf = configs.GetInstance()

	//run until end of the simulation
	var startTime = conf.TimeStart()
	var endTime = conf.TimeEnd()

	var totalDuration = int(endTime.Sub(startTime).Seconds())

	var timeNotReached = conf.TimeStart().Before(endTime)

	for timeNotReached {
		s.RunOnce()
		n--
		timeNotReached = s.currentTime.Before(endTime) && n != 0 && n != -1
		if !conf.PrintDebug() {
			var percent = int(100*s.currentTime.Sub(startTime).Seconds()) /
				totalDuration
			fmt.Print("\r" + strconv.Itoa(percent) + "% done - " +
				s.currentTime.String())
		}
	}

	s.ToCSV()
}

// run to the next event and execute it. if multiple events occur at the same
// time, they will be done.

/*
RunOnce

Run to the next event and execute it. if multiple events occur at the same
time, they will be done.
*/
func (s *Simulator) RunOnce() {

	// this way with a delay in config.json
	//s.updateGraphDelay()

	// get the next event
	// eventsStationClosed = [train,...];
	//each train should be updated on its current position / next position
	var trainEvents, newCurrentTime = s.NextEventsTrain()
	var oldTime = s.currentTime

	//check if user eventsStationClosed (line / station closed) should be called
	var eventsStationClosed = s.getEventsStationClosed(oldTime, newCurrentTime)
	s.executeEventsStationClosed(eventsStationClosed, oldTime, newCurrentTime)

	var eventsLineDelay = s.getEventsLineDelay(oldTime, newCurrentTime)
	var strELDT string = "eventsLineDelay test : "
	fmt.Println()
	fmt.Println("BEGIN")
	fmt.Println(strELDT, oldTime)
	fmt.Println(strELDT, newCurrentTime)
	fmt.Println(strELDT, len(eventsLineDelay))
	fmt.Println(strELDT, eventsLineDelay)
	eventsLineDelay = s.executeEventsLineDelay(eventsLineDelay,
		oldTime, newCurrentTime)

	var eventsLineClosed = s.getEventsLineClosed(oldTime, newCurrentTime)
	eventsLineClosed = s.executeEventsLineClosed(eventsLineClosed,
		oldTime, newCurrentTime)

	var eventsAttendancePeak = s.getEventsAttendancePeak(oldTime, newCurrentTime)
	s.executeEventsAttendancePeak(eventsAttendancePeak, oldTime, newCurrentTime)

	//var lastTime = s.currentTime
	s.currentTime = newCurrentTime

	// Check if passengers arrived / got out of a station.
	s.population.UpdateAll(s.currentTime, &s.mapObject)

	if s.config.PrintDebug() {
		println("\n\n", s.currentTime.String(), ": ", len(trainEvents), " events")
	}

	/*if s.config.PrintDebug() {
		for _, train := range trainEvents {
			println("\ttrain #", train.Id(), " of line ",
			train.GetLine().Name(), " arrived at station ",
			train.CurrentStation().Name())
		}
	}*/

	var threadWaitGroup sync.WaitGroup
	var stationsMutexes map[int]*sync.Mutex
	var mapStationsMutexesLock = sync.RWMutex{}
	var writeInTimetableMutex = sync.Mutex{}

	stationsMutexes = make(map[int]*sync.Mutex)
	//generate mutexes
	for i := range trainEvents {
		if _, ok := stationsMutexes[trainEvents[i].Id()]; !ok {
			stationsMutexes[trainEvents[i].Id()] = &sync.Mutex{}
		}
	}

	//for each train event :
	for i, train := range trainEvents {
		aTime := trainEvents[i].TimeArrivalNextStation()
		trainEvents[i].ArriveInNextStation(aTime)
		//update trip number
		if trainEvents[i].GetDirectionChanged() {
			trainEvents[i].SetTripNumber(s.tripNumberCounter)
			s.tripNumberCounter++
			trainEvents[i].SetDirectionChanged(false)
		}
		//verify the mutex
		if _, ok := stationsMutexes[trainEvents[i].Id()]; !ok {
			mapStationsMutexesLock.Lock()
			stationsMutexes[trainEvents[i].Id()] = &sync.Mutex{}
			mapStationsMutexesLock.Unlock()
		}
		if trainEvents[i].CurrentStation().StatusIsClosed() {
			var j = trainEvents[i].GetCurrentStation().Id()
			var k = trainEvents[i].GetNextStation().Id()
			trainEvents[i].SetTimeArrivalNextStation(aTime.Add(time.Duration(
				s.mapObject.GraphTimeBetweenStation()[j][k]+
					s.mapObject.GraphDelay()[j][k]) * time.Second))
			continue
		} else {
			threadWaitGroup.Add(1)
			func() {
				//TODO add go
				defer threadWaitGroup.Done()

				mapStationsMutexesLock.RLock()
				var mutex = stationsMutexes[trainEvents[i].Id()]
				mapStationsMutexesLock.RUnlock()

				var j = trainEvents[i].GetCurrentStation().Id()
				var k = trainEvents[i].GetNextStation().Id()
				trainEvents[i].SetTimeArrivalNextStation(aTime.Add(
					time.Duration(s.config.TimeInStation()+
						s.mapObject.GraphTimeBetweenStation()[j][k]+
						s.mapObject.GraphDelay()[j][k]) * time.Second))

				writeInTimetableMutex.Lock()
				s.WriteInTimetableReal(trainEvents[i], aTime)
				writeInTimetableMutex.Unlock()

				//start mutex
				mutex.Lock()

				// > get passengers out of the train(s)
				s.population.UpdateTrainToStation(train, aTime)
				// > get passengers in the train(s)
				s.population.UpdateStationToTrain(train)

				//end mutex
				mutex.Unlock()
			}()
		}
	}
	threadWaitGroup.Wait()
}

/*
executeESCStartEventROSetPassengerStart is used to set the path

Param :
trip *models.Trip
nearestStation *models.MetroStation
i string
*/
func (s *Simulator) executeESCStartEventROSetPassengerStart(trip *models.Trip,
	nearestStation *models.MetroStation, i string) {
	if trip.Path().StartStation().StatusIsClosed() {
		//passenger starts at a closed station
		if len(trip.Path().Stations()) > 2 {
			var newStartingStation = trip.Path().Stations()[1]
			if newStartingStation.StatusIsClosed() {
				newStartingStation = nearestStation
			}
			var j = newStartingStation.Id()
			var k = trip.Path().EndStation().Id()
			s.population.Outside()[i].NextTrip().SetPath(
				*s.mapObject.Graph()[j][k])
			if s.config.PrintDebug() {
				fmt.Println(" - new start : ", newStartingStation.Name())
			}
		} else {
			//remove the trip totally
			s.population.Outside()[i].RemoveTrip(trip)
			if s.config.PrintDebug() {
				fmt.Println(" - deleted path")
			}
			return
		}
		//TODO test
	}
}

/*
executeESCStartEventROSetPath is used to set the path

Param :
trip *models.Trip
pass *models.Passenger
i string

Return :
*models.Trip
*/
func (s *Simulator) executeESCStartEventROSetPath(trip *models.Trip,
	pass *models.Passenger, i string) *models.Trip {
	trip = pass.NextTrip()
	if trip == nil {
		return nil
	}
	var j = trip.Path().StartStation().Id()
	var k = trip.Path().EndStation().Id()
	path := s.mapObject.Graph()[j][k]
	if path == nil {
		//means there is no possible pathways between the start and the end.
		//try to find an opened station close to the start & end
		path2 := s.mapObject.GetNewPathStationMiddleClose(
			trip.Path().StartStation(),
			trip.Path().EndStation())
		if path2 == nil {
			//the trip is a fail, the passenger will not take it.
			//we remove it.
			s.population.Outside()[i].RemoveTrip(trip)
			if s.config.PrintDebug() {
				fmt.Println(" - deleted path (2)")
			}
		} else {
			trip.SetPath(*path2)
			if s.config.PrintDebug() {
				fmt.Println(" - new path : ", path2.String())
			}
		}
	} else {
		trip.SetPath(*path)
		if s.config.PrintDebug() {
			fmt.Println(" - new path (2) : ", path.String())
		}
	}
	return trip
}

/*
executeESCStartEventRerouteOutside is used to reroute passengers
in population outside

Param :
event *models.EventStationClosed
stationEvent models.MetroStation
nearestStation *models.MetroStation
*/
func (s *Simulator) executeESCStartEventRerouteOutside(
	event *models.EventStationClosed, stationEvent models.MetroStation,
	nearestStation *models.MetroStation) {
	for i, pass := range s.population.Outside() {
		trip := pass.NextTrip()
		if trip == nil {
			continue
		}
		if trip.DepartureTime().After(event.Start()) &&
			trip.DepartureTime().Before(event.End()) &&
			trip.Path().HasStation(&stationEvent) {
			//trip has to be modified
			if s.config.PrintDebug() {
				fmt.Print("rerouting passenger from outside : "+
					"old path:", trip.Path().String())
			}
			s.executeESCStartEventROSetPassengerStart(trip, nearestStation, i)

			//reassign trip if any changes have been made
			trip = s.executeESCStartEventROSetPath(trip, pass, i)
		}
	}
}

/*
executeESCStartEventRerouteCloseStation is used to reroute passengers
in closed station

Param :
event *models.EventStationClosed
stationEvent models.MetroStation
nearestStation *models.MetroStation
currentTime time.Time
*/
func (s *Simulator) executeESCStartEventRerouteCloseStation(
	event *models.EventStationClosed, stationEvent models.MetroStation,
	nearestStation *models.MetroStation, currentTime time.Time) {
	for i := range s.population.InStation()[event.IdStation()] {
		pass := s.population.InStation()[event.IdStation()][i]
		//put them out and to the closest station
		var curTrip = pass.CurrentTrip()
		//if the passenger isn't near his last station
		//-> needs to create another trip from a neighbour station
		if curTrip.Path().EndStation().Id() != event.IdStation() {
			//get the number of stations left
			var positionInPath = curTrip.Path().PositionStation(stationEvent)
			if positionInPath < len(curTrip.Path().Stations())-3 {
				//at least 3 stations left (-1 : end, -2: 2 stations
				//( in & out), -3 : 3 stations (in & transit & out))
				//because no one will reenter the subway for one stop
				var nextStation = curTrip.Path().Stations()[positionInPath+1]
				if nextStation.StatusIsClosed() {
					nextStation = nearestStation
				}
				var nextStationPosition = nextStation.Position()
				var walkingTime = nextStationPosition.DistanceTo(
					stationEvent.Position()) / (6 / 3.6)
				// 6 kmh

				var newTrip = models.NewTrip(currentTime.Add(
					time.Duration(walkingTime)*time.Second),
					*s.mapObject.Graph()[nextStation.Id()][curTrip.Path().EndStation().Id()])
				s.population.InStation()[event.IdStation()][i].AddTrip(&newTrip)

				if s.config.PrintDebug() {
					println("rerouted passenger", pass.Id(),
						"\t newtrip : \t ", newTrip.String())
				}
				//TODO test
			}
		}

		//push all passengers out
		s.population.StationToOutside(currentTime, &stationEvent,
			s.population.InStation()[event.IdStation()][i])
		//TODO test
	}
}

/*
executeESCStartEventRerouteWaitingLoop is used to reroute passengers
in stations waiting for train (loop)

Param :
stationId int
stationEvent *models.MetroStation
station map[string]*models.Passenger
*/
func (s *Simulator) executeESCStartEventRerouteWaitingLoop(stationId int,
	stationEvent models.MetroStation, station map[string]*models.Passenger) {
	for passengerId := range station {
		trip := s.population.InStation()[stationId][passengerId].CurrentTrip()
		if trip.Path().HasStation(&stationEvent) {
			path := s.mapObject.Graph()[stationId][trip.Path().EndStation().Id()]
			if path == nil {
				//means there is no possible pathways between the start and the end.
				//try to find an opened station close to the start & end
				path2 := s.mapObject.GetNewPathStationMiddleClose(
					&s.mapObject.Stations()[stationId],
					trip.Path().EndStation())
				if path2 == nil {
					//the trip is a fail, the passenger will not take it.
					//we cut it short (the passenger will be flushed out at the next event).
					path2 = trip.Path().GetSegment(
						trip.Path().StartStation(), &stationEvent)
					trip.SetPath(*path2)
				}
			} else {
				//modify the trip path
				path = trip.Path().Reroute(*path)
				trip.SetPath(*path)
			}
		}

	}
}

/*
executeESCStartEventRerouteWaiting is used to reroute passengers
in stations waiting for train

Param :
event *models.EventStationClosed
stationEvent models.MetroStation
*/
func (s *Simulator) executeESCStartEventRerouteWaiting(
	event *models.EventStationClosed, stationEvent models.MetroStation) {
	for stationId, station := range s.population.InStation() {
		if stationId == event.IdStation() {
			continue
		}
		s.executeESCStartEventRerouteWaitingLoop(stationId,
			stationEvent, station)
	}
}

/*
executeESCStartEventRIEndStationClosed is used to reroute passengers
in train (end station closed)

Param :
trip *models.Trip
nearestStation *models.MetroStation
nextStationTrain *models.MetroStation
nextStationOpenedTrain *models.MetroStation

Return :
*models.Trip : trip
*/
func (s *Simulator) executeESCStartEventRIEndStationClosed(trip *models.Trip,
	nearestStation, nextStationTrain,
	nextStationOpenedTrain *models.MetroStation) *models.Trip {
	if trip.Path().EndStation().StatusIsClosed() {
		var l = len(trip.Path().Stations()) - 2
		if !trip.Path().Stations()[l].StatusIsClosed() {
			var j = nextStationTrain.Id()
			var k = trip.Path().Stations()[l].Id()
			newPath := s.mapObject.Graph()[j][k]
			if newPath == nil {
				//put the user out
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(),
					nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		} else {
			var j = nextStationTrain.Id()
			var k = nearestStation.Id()
			newPath := s.mapObject.Graph()[j][k]
			if newPath == nil {
				//put the user out
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(),
					nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		}
	}
	return trip
}

/*
executeESCStartEventRIEventStation is used to reroute passengers
in train (event station on path)

Param :
trip *models.Trip
stationEvent models.MetroStation
nextStationTrain *models.MetroStation
nextStationOpenedTrain *models.MetroStation

Return :
*models.Trip : trip
*/
func (s *Simulator) executeESCStartEventRIEventStation(trip *models.Trip,
	stationEvent models.MetroStation, nextStationTrain,
	nextStationOpenedTrain *models.MetroStation) *models.Trip {
	if trip.Path().HasStation(&stationEvent) {
		var j = nextStationTrain.Id()
		var k = trip.Path().EndStation().Id()
		newPath := s.mapObject.Graph()[j][k]
		if newPath == nil {
			newPath = s.mapObject.GetNewPathStationMiddleClose(
				nextStationTrain, trip.Path().EndStation())
			if newPath == nil {
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(),
					nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		} else {
			trip.SetPath(*trip.Path().Reroute(*newPath))
		}
	}
	return trip
}

/*
executeESCStartEventRerouteInside is used to reroute passengers
in train

Param :
stationEvent models.MetroStation
nearestStation *models.MetroStation
*/
func (s *Simulator) executeESCStartEventRerouteInside(
	stationEvent models.MetroStation, nearestStation *models.MetroStation) {
	for trainId, train := range s.population.InTrains() {
		var nextStationTrain = s.trains[trainId].GetNextStation()
		var nextStationOpenedTrain = s.trains[trainId].GetNextOpenedStation()
		if nextStationOpenedTrain == nil {
			// the line is totally cut off from everything
			continue
		}
		for passengerId := range train {
			var pass = s.population.InTrains()[trainId][passengerId]
			var trip = pass.CurrentTrip()
			if trip.Path().HasStation(&stationEvent) {
				//verify end is good
				trip = s.executeESCStartEventRIEndStationClosed(trip,
					nearestStation, nextStationTrain, nextStationOpenedTrain)

				//verify path is good
				trip = s.executeESCStartEventRIEventStation(trip,
					stationEvent, nextStationTrain, nextStationOpenedTrain)
			}
		}
	}
}

/*
executeESCStartEvent is used to start the event "line close"

Param :
lineEvent []*models.MetroStation
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeESCStartEvent(stationEvent models.MetroStation,
	event *models.EventStationClosed, oldTime, currentTime time.Time) {
	if event.Start().After(oldTime) && event.Start().Before(currentTime) {
		//event : station closed
		if s.config.PrintDebug() {
			println("event activated : closed station ",
				stationEvent.Name(), " at ", event.Start().String(),
				" - statusIsClosed :", stationEvent.StatusIsClosed())
		}

		//get the nearest opened station in order to reroute efficiently passengers
		var nearestStation = s.mapObject.GetNearestStationOpened(
			stationEvent.Position())
		//divert all passengers to the closest non-closed station

		//reroute passengers in population outside
		s.executeESCStartEventRerouteOutside(event, stationEvent, nearestStation)

		//due to the rerouting, force the sort of the trips of outsiders
		s.population.SortOutside()

		//reroute passengers in closed station
		s.executeESCStartEventRerouteCloseStation(event, stationEvent,
			nearestStation, currentTime)

		//reroute passengers in stations, waiting for train
		//(the passengers descend one station before)
		s.executeESCStartEventRerouteWaiting(event, stationEvent)

		//reroute passengers in train (the passengers descend one station before)
		s.executeESCStartEventRerouteInside(stationEvent, nearestStation)
	}
}

/*
executeESCEndEvent is used to start the event "line close"

Param :
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeESCEndEvent(event *models.EventStationClosed,
	oldTime, currentTime time.Time) {
	if event.End().After(oldTime) && event.End().Before(currentTime) {
		if s.config.PrintDebug() {
			println("event activated : reopened station ",
				s.mapObject.Stations()[event.IdStation()].Name(), " at ",
				event.End().String())
		}
		s.mapObject.Stations2()[event.IdStation()].SetStatus("open")
	}
}

/*
executeESCReroutePassenger is used to reroute passenger

Param :
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeESCReroutePassenger(
	events []*models.EventStationClosed,
	oldTime, currentTime time.Time) []*models.EventStationClosed {
	for _, event := range events {
		var stationEvent = s.mapObject.Stations()[event.IdStation()]

		//start the event
		s.executeESCStartEvent(stationEvent, event, oldTime, currentTime)

		//finish the event
		s.executeESCEndEvent(event, oldTime, currentTime)

	}
	return events
}

func (s *Simulator) executeEventsStationClosed(
	events []*models.EventStationClosed, oldTime, currentTime time.Time) {

	//apply the change to the map
	for _, event := range events {
		if event.Start().After(oldTime) && event.Start().Before(currentTime) {
			//activate the event
			s.mapObject.Stations2()[event.IdStation()].SetStatus("closed")
		}
		//don't use if/else as it would risk to not reopen the station.
		if event.End().After(oldTime) && event.End().Before(currentTime) &&
			!event.Finished() {
			//deactivate the event
			event.SetFinished(true)
			s.mapObject.Stations2()[event.IdStation()].SetStatus("open")
		}
	}

	//remake the graph
	s.mapObject.GenerateGraph()

	//reroute passengers
	events = s.executeESCReroutePassenger(events, oldTime, currentTime)
}

/*
timeArrivalUpdate is used to update time arrival next station for trains
on this part of line

Param :
event *models.EventLineDelay
lineDelay *models.MetroLine

Return :
*models.MetroLine : lineDelay
*/
func (s *Simulator) timeArrivalUpdate(event *models.EventLineDelay,
	lineDelay *models.MetroLine) *models.MetroLine {
	for i := 0; i < len(
		s.mapObject.Stations()[event.IdStationStart()].Lines()); i++ {
		for j := 0; j < len(
			s.mapObject.Stations()[event.IdStationEnd()].Lines()); j++ {
			if s.mapObject.Stations()[event.IdStationStart()].Lines()[i].Id() ==
				s.mapObject.Stations()[event.IdStationEnd()].Lines()[j].Id() {
				lineDelay = s.mapObject.Stations()[event.IdStationStart()].Lines()[i]
			}
		}
	}
	return lineDelay
}

/*
eventsLineDelayUpdateBeforeStation is used to update time arrival next station
for trains on this part of line

Param :
event *models.EventLineDelay
lineDelay *models.MetroLine
direction string
currentTime time.Time

Return :
*models.MetroLine : lineDelay
*/
func (s *Simulator) eventsLineDelayUpdateBeforeStation(
	event *models.EventLineDelay, lineDelay *models.MetroLine,
	direction string, currentTime time.Time) {
	for k := 0; k < len(s.GetTrains()); k++ {
		if s.GetTrains()[k].GetLine().Id() == lineDelay.Id() {
			fmt.Println("exe eventsLineDelay  : update ?")
			if direction == "up" &&
				s.GetTrains()[k].CurrentStation().Id() ==
					event.IdStationStart() {
				fmt.Println("exe eventsLineDelay  : update if")
				var l = event.IdStationStart()
				var m = event.IdStationEnd()
				models.UpdateBeforeTimeArrivalNextStation(s.GetTrains()[k],
					currentTime, event.Delay(),
					s.mapObject.GraphTimeBetweenStation()[l][m])
			} else if direction == "down" &&
				s.GetTrains()[k].CurrentStation().Id() == event.IdStationEnd() {
				fmt.Println("exe eventsLineDelay  : update elsif")
				var l = event.IdStationStart()
				var m = event.IdStationEnd()
				models.UpdateBeforeTimeArrivalNextStation(s.GetTrains()[k],
					currentTime, event.Delay(),
					s.mapObject.GraphTimeBetweenStation()[l][m])
			}
		}
	}
}

/*
eventsLineDelayUpdateAfterStation is used to update time arrival next station
for trains on this part of line

Param :
event *models.EventLineDelay
lineDelay *models.MetroLine
direction string
currentTime time.Time

Return :
*models.MetroLine : lineDelay
*/
func (s *Simulator) eventsLineDelayUpdateAfterStation(
	event *models.EventLineDelay, lineDelay *models.MetroLine,
	direction string, currentTime time.Time) {
	for k := 0; k < len(s.GetTrains()); k++ {
		if s.GetTrains()[k].GetLine().Id() == lineDelay.Id() {
			fmt.Println("exe eventsLineDelay  : update ?")
			fmt.Println("station id : ", s.GetTrains()[k].CurrentStation().Id())
			fmt.Println("event station id : ", event.IdStationStart())
			if direction == "up" && s.GetTrains()[k].CurrentStation().Id() ==
				event.IdStationStart() {
				fmt.Println("exe eventsLineDelay  : update if")
				var l = event.IdStationStart()
				var m = event.IdStationEnd()
				models.UpdateAfterTimeArrivalNextStation(s.GetTrains()[k],
					currentTime, event.Delay(),
					s.mapObject.GraphTimeBetweenStation()[l][m])
			} else if direction == "down" &&
				s.GetTrains()[k].CurrentStation().Id() == event.IdStationEnd() {
				fmt.Println("exe eventsLineDelay  : update elsif")
				var l = event.IdStationStart()
				var m = event.IdStationEnd()
				models.UpdateAfterTimeArrivalNextStation(s.GetTrains()[k],
					currentTime, event.Delay(),
					s.mapObject.GraphTimeBetweenStation()[l][m])
			}
		}
	}
}

/*
checkDirection is used to affect the direction

Param :
event *models.EventLineDelay
direction string

Return :
string : direction
*/
func (s *Simulator) checkDirection(event *models.EventLineDelay,
	direction string) string {
	if event.IdStationStart() < event.IdStationEnd() {
		direction = "up"
	} else {
		direction = "down"
	}
	return direction
}

func (s *Simulator) executeEventsLineDelay(events []*models.EventLineDelay,
	oldTime, currentTime time.Time) []*models.EventLineDelay {
	fmt.Println("exeEventsLineDelay")
	for i, event := range events {
		if event.Start().After(oldTime) && (event.Start().Before(currentTime) ||
			event.Start().Equal(currentTime)) && !event.Finished() {
			fmt.Println("exe eventsLineDelay  : if")
			//execute start of event
			var lineDelay *models.MetroLine
			var direction string
			s.mapObject.AddDelay(event.IdStationStart(), event.IdStationEnd(),
				event.Delay())
			//must be changed later to accept a list of delays
			//update time arrival next station for trains on this part of line
			lineDelay = s.timeArrivalUpdate(event, lineDelay)

			direction = s.checkDirection(event, direction)

			s.eventsLineDelayUpdateBeforeStation(event, lineDelay,
				direction, currentTime)
		}

		if event.End().After(oldTime) && (event.End().Before(currentTime) ||
			event.End().Equal(currentTime)) {
			fmt.Println("exe eventsLineDelay  : if 2")
			//execute end of event
			var lineDelay *models.MetroLine
			var direction string
			s.mapObject.AddDelay(event.IdStationStart(), event.IdStationEnd(), 0)
			//not working yet with a list of delays
			//update time arrival next station for trains on this part of line
			lineDelay = s.timeArrivalUpdate(event, lineDelay)

			direction = s.checkDirection(event, direction)
			fmt.Println("direction", direction)

			s.eventsLineDelayUpdateAfterStation(event, lineDelay,
				direction, currentTime)
			events[i].SetFinished(true)
		}
	}
	return events
}

/*
executeELCStartEventROSetPassengerStart is used to set the path

Param :
trip *models.Trip
nearestStation *models.MetroStation
i string
*/
func (s *Simulator) executeELCStartEventROSetPassengerStart(trip *models.Trip,
	nearestStation *models.MetroStation, i string) {
	if trip.Path().StartStation().StatusIsClosed() {
		//passenger starts at a closed station
		if len(trip.Path().Stations()) > 2 {
			var newStartingStation = trip.Path().Stations()[1]
			if newStartingStation.StatusIsClosed() {
				newStartingStation = nearestStation
			}
			var j = newStartingStation.Id()
			var k = trip.Path().EndStation().Id()
			s.population.Outside()[i].NextTrip().SetPath(
				*s.mapObject.Graph()[j][k])
			if s.config.PrintDebug() {
				fmt.Println(" - new start : ", newStartingStation.Name())
			}
		} else {
			//delete the trip
			s.population.Outside()[i].RemoveTrip(trip)
			if s.config.PrintDebug() {
				fmt.Println(" - deleted path")
			}
			return
		}
	}
}

/*
executeELCStartEventROSetPath is used to set the path

Param :
trip *models.Trip
pass *models.Passenger
i string

Return :
*models.Trip
*/
func (s *Simulator) executeELCStartEventROSetPath(trip *models.Trip,
	pass *models.Passenger, i string) *models.Trip {
	trip = pass.NextTrip()
	var j = trip.Path().StartStation().Id()
	var k = trip.Path().EndStation().Id()
	path := s.mapObject.Graph()[j][k]
	if path == nil {
		newPath := s.mapObject.GetNewPathStationMiddleClose(
			trip.Path().StartStation(), trip.Path().EndStation())
		if newPath == nil {
			s.population.Outside()[i].RemoveTrip(trip)
			if s.config.PrintDebug() {
				fmt.Println(" - deleted path (2)")
			}
		} else {
			trip.SetPath(*newPath)
			if s.config.PrintDebug() {
				fmt.Println(" - new path : ", newPath.String())
			}
		}
	} else {
		trip.SetPath(*path)
		if s.config.PrintDebug() {
			fmt.Println(" - new path (2) : ", path.String())
		}
	}
	return trip
}

/*
executeELCStartEventRerouteOutside is used to reroute passengers
in population outside

Param :
event *models.EventLineClosed
stationEvent *models.MetroStation
nearestStation *models.MetroStation
*/
func (s *Simulator) executeELCStartEventRerouteOutside(
	event *models.EventLineClosed,
	stationEvent, nearestStation *models.MetroStation) {
	for i, pass := range s.population.Outside() {
		trip := pass.NextTrip()
		if trip == nil {
			continue
		}
		if (trip.DepartureTime().After(event.Start())) &&
			(trip.DepartureTime().Before(event.End())) &&
			(trip.Path().HasStation(stationEvent)) {
			//trip has to be recalculated
			if s.config.PrintDebug() {
				fmt.Print("rerouting passenger from outside : "+
					"old path:", trip.Path().String())
			}
			s.executeELCStartEventROSetPassengerStart(trip, nearestStation, i)

			trip = s.executeELCStartEventROSetPath(trip, pass, i)
		}
	}
}

/*
executeELCStartEventRerouteCloseLine is used to reroute passengers
in closed line

Param :
currentTime time.Time
stationEvent *models.MetroStation
nearestStation *models.MetroStation
*/
func (s *Simulator) executeELCStartEventRerouteCloseLine(currentTime time.Time,
	stationEvent, nearestStation *models.MetroStation) {
	for i := range s.population.InStation()[stationEvent.Id()] {
		pass := s.population.InStation()[stationEvent.Id()][i]
		currentPath := pass.CurrentTrip().Path()
		if currentPath.EndStation().Id() != stationEvent.Id() {
			pathPosition := currentPath.PositionStation(*stationEvent)
			if pathPosition < len(currentPath.Stations())-3 {
				nextStation := currentPath.Stations()[pathPosition+1]
				if nextStation.StatusIsClosed() {
					nextStation = nearestStation
				}
				var nextStationPosition = nextStation.Position()
				deltaTime := nextStationPosition.DistanceTo(
					stationEvent.Position()) / (6 / 3.6)
				newTrip := models.NewTrip(currentTime.Add(
					time.Duration(deltaTime)*time.Second),
					*s.mapObject.Graph()[nextStation.Id()][currentPath.EndStation().Id()])
				s.population.InStation()[stationEvent.Id()][i].AddTrip(&newTrip)
				if s.config.PrintDebug() {
					println("rerouted passenger", pass.Id(),
						"\t newtrip : \t ", newTrip.String())
				}
			}
		}

		s.population.StationToOutside(currentTime, stationEvent,
			s.population.InStation()[stationEvent.Id()][i])

	}
}

/*
executeELCStartEventRerouteWaitingLoop is used to reroute passengers
in stations waiting for train (loop)

Param :
id int
stationEvent *models.MetroStation
station map[string]*models.Passenger
*/
func (s *Simulator) executeELCStartEventRerouteWaitingLoop(id int,
	stationEvent *models.MetroStation, station map[string]*models.Passenger) {
	for passId := range station {
		trip := s.population.InStation()[id][passId].CurrentTrip()
		if trip.Path().HasStation(stationEvent) {
			path := s.mapObject.Graph()[id][trip.Path().EndStation().Id()]
			if path == nil {
				newPath := s.mapObject.GetNewPathStationMiddleClose(
					&s.mapObject.Stations()[id], trip.Path().EndStation())
				if newPath == nil {
					newPath = trip.Path().GetSegment(
						trip.Path().StartStation(), stationEvent)
					trip.SetPath(*newPath)
				}
			} else {
				path = trip.Path().Reroute(*path)
				trip.SetPath(*path)
			}
		}
	}
}

/*
executeELCStartEventRerouteWaiting is used to reroute passengers
in stations waiting for train

Param :
stationEvent *models.MetroStation
*/
func (s *Simulator) executeELCStartEventRerouteWaiting(
	stationEvent *models.MetroStation) {
	for id, station := range s.population.InStation() {
		if id == stationEvent.Id() {
			continue
		}
		s.executeELCStartEventRerouteWaitingLoop(id, stationEvent, station)
	}
}

/*
executeELCStartEventRIEndStationClosed is used to reroute passengers
in train (end station closed)

Param :
trip *models.Trip
nearestStation *models.MetroStation
nextStationTrain *models.MetroStation
nextStationOpenedTrain *models.MetroStation

Return :
*models.Trip : trip
*/
func (s *Simulator) executeELCStartEventRIEndStationClosed(trip *models.Trip,
	nearestStation, nextStationTrain,
	nextStationOpenedTrain *models.MetroStation) *models.Trip {
	if trip.Path().EndStation().StatusIsClosed() {
		var j = nextStationTrain.Id()
		var k = trip.Path().Stations()[len(trip.Path().Stations())-2].Id()
		if !trip.Path().Stations()[len(
			trip.Path().Stations())-2].StatusIsClosed() {
			newPath := s.mapObject.Graph()[j][k]
			if newPath == nil {
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(), nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		} else {
			var l = nextStationTrain.Id()
			var m = nearestStation.Id()
			newPath := s.mapObject.Graph()[l][m]
			if newPath == nil {
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(), nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		}
	}
	return trip
}

/*
executeELCStartEventRIEventStation is used to reroute passengers
in train (event station on path)

Param :
trip *models.Trip
stationEvent *models.MetroStation
nextStationTrain *models.MetroStation
nextStationOpenedTrain *models.MetroStation

Return :
*models.Trip : trip
*/
func (s *Simulator) executeELCStartEventRIEventStation(trip *models.Trip,
	stationEvent, nextStationTrain,
	nextStationOpenedTrain *models.MetroStation) *models.Trip {
	if trip.Path().HasStation(stationEvent) {
		var j = nextStationTrain.Id()
		var k = trip.Path().EndStation().Id()
		newPath := s.mapObject.Graph()[j][k]
		if newPath == nil {
			newPath = s.mapObject.GetNewPathStationMiddleClose(
				nextStationTrain, trip.Path().EndStation())
			if newPath == nil {
				trip.SetPath(*trip.Path().GetSegment(
					trip.Path().StartStation(), nextStationOpenedTrain))
			} else {
				trip.SetPath(*trip.Path().Reroute(*newPath))
			}
		} else {
			trip.SetPath(*trip.Path().Reroute(*newPath))
		}
	}
	return trip
}

/*
executeELCStartEventRerouteInside is used to reroute passengers
in train

Param :
stationEvent *models.MetroStation
nearestStation *models.MetroStation
*/
func (s *Simulator) executeELCStartEventRerouteInside(
	stationEvent, nearestStation *models.MetroStation) {
	for id, train := range s.population.InTrains() {
		nextStationTrain := s.trains[id].GetNextStation()
		nextStationOpenedTrain := s.trains[id].GetNextOpenedStation()
		if nextStationOpenedTrain == nil {
			continue
		}
		for passId := range train {
			pass := s.population.InTrains()[id][passId]
			trip := pass.CurrentTrip()

			trip = s.executeELCStartEventRIEndStationClosed(trip,
				nearestStation, nextStationTrain, nextStationOpenedTrain)

			trip = s.executeELCStartEventRIEventStation(trip,
				stationEvent, nextStationTrain, nextStationOpenedTrain)
		}
	}
}

/*
executeELCStartEvent is used to start the event "line close"

Param :
lineEvent []*models.MetroStation
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeELCStartEvent(lineEvent []*models.MetroStation,
	event *models.EventLineClosed,
	oldTime, currentTime time.Time) []*models.MetroStation {
	if (lineEvent != nil) &&
		event.Start().After(oldTime) &&
		event.Start().Before(currentTime) {
		//event : line closed
		if s.config.PrintDebug() {
			for _, stationEvent := range lineEvent {
				println("event activated : closed station ",
					stationEvent.Name(), " at ", event.Start().String(),
					" - statusIsClosed :", stationEvent.StatusIsClosed())
			}
		}
		// get the nearest opened station to reroute passengers
		for _, stationEvent := range lineEvent {
			var nearestStation = s.mapObject.GetNearestStationOpened(
				stationEvent.Position())

			//reroute passengers in population outside
			s.executeELCStartEventRerouteOutside(event, stationEvent,
				nearestStation)

			//sort trips of outsiders
			s.population.SortOutside()

			//reroute passengers in closed line
			s.executeELCStartEventRerouteCloseLine(currentTime, stationEvent,
				nearestStation)

			//reroute passengers in stations waiting for train
			s.executeELCStartEventRerouteWaiting(stationEvent)

			//reroute passengers in train
			s.executeELCStartEventRerouteInside(stationEvent, nearestStation)
		}
	}
	return lineEvent
}

/*
executeELCEndEvent is used to start the event "line close"

Param :
lineEvent []*models.MetroStation
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeELCEndEvent(lineEvent []*models.MetroStation,
	event *models.EventLineClosed, oldTime, currentTime time.Time) {
	if event.End().After(oldTime) && event.End().Before(currentTime) {
		for _, stationEvent := range lineEvent {
			if s.config.PrintDebug() {
				println("event activated : reopened station ",
					s.mapObject.Stations()[stationEvent.Id()].Name(),
					" at ", event.End().String())
			}
			s.mapObject.Stations2()[stationEvent.Id()].SetStatus("open")
		}
	}
}

/*
executeELCReroutePassenger is used to reroute passenger

Param :
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeELCReroutePassenger(events []*models.EventLineClosed,
	oldTime, currentTime time.Time) []*models.EventLineClosed {
	for _, event := range events {
		var lineEvent []*models.MetroStation
		lineEvent = nil
		for _, line := range s.mapObject.Lines() {
			if line.GetPath(s.mapObject.FindStationById(event.IdStationStart()),
				s.mapObject.FindStationById(event.IdStationEnd())) != nil {
				lineEvent = line.GetPath(s.mapObject.FindStationById(
					event.IdStationStart()), s.mapObject.FindStationById(event.IdStationEnd()))
			}
		}

		//start the event
		lineEvent = s.executeELCStartEvent(lineEvent, event, oldTime, currentTime)

		//finish the event
		s.executeELCEndEvent(lineEvent, event, oldTime, currentTime)
	}
	return events
}

/*
executeELCAffectES is used to affect "close" or "open" to station

Param :
event *models.EventLineClosed
oldTime time.Time
currentTime time.Time
*/
func (s *Simulator) executeELCAffectES(eventStations []*models.MetroStation,
	event *models.EventLineClosed, oldTime, currentTime time.Time) {
	if (eventStations != nil) &&
		event.Start().After(oldTime) &&
		event.Start().Before(currentTime) {
		//activate the event
		for _, es := range eventStations {
			es.SetStatus("closed")
		}
	}
	if (eventStations != nil) &&
		event.End().After(oldTime) &&
		event.End().Before(currentTime) &&
		!event.Finished() {
		//deactivate the event
		for _, es := range eventStations {
			es.SetStatus("open")
		}
	}
}

func (s *Simulator) executeEventsLineClosed(events []*models.EventLineClosed,
	oldTime, currentTime time.Time) []*models.EventLineClosed {
	//apply the change to the map
	for _, event := range events {
		var eventStations []*models.MetroStation
		eventStations = nil
		for _, line := range s.mapObject.Lines() {
			if line.GetPath(s.mapObject.FindStationById(event.IdStationStart()),
				s.mapObject.FindStationById(event.IdStationEnd())) != nil {
				eventStations = line.GetPath(s.mapObject.FindStationById(
					event.IdStationStart()), s.mapObject.FindStationById(event.IdStationEnd()))
			}
		}
		s.executeELCAffectES(eventStations, event, oldTime, currentTime)
	}
	//remake the graph
	s.mapObject.GenerateGraph()

	//reroute passengers
	events = s.executeELCReroutePassenger(events, oldTime, currentTime)

	return events
}

func (s *Simulator) executeEventsAttendancePeak(
	events []*models.EventAttendancePeak, oldTime, currentTime time.Time) {
	var totalPopulation = len(s.Population().Outside()) +
		len(s.Population().InStation()) + len(s.Population().InTrains())
	for _, event := range events {
		if event.Time().After(oldTime) && (event.Time().Before(currentTime) ||
			event.Time().Equal(currentTime)) && !event.Finished() {
			//execute the event
			for i := 0; i < event.Size(); i++ {
				departureStation := s.mapObject.FindStationById(event.IdStation())
				arrivalStation := s.mapObject.FindStationById(rand.Intn(
					len(s.mapObject.Stations())))
				ps, _ := s.mapObject.GetPathStation(*departureStation, *arrivalStation)
				trip := models.NewTrip(event.Time(), ps)

				passenger := models.NewPassenger(strconv.Itoa(totalPopulation+i), 0)
				passenger.SetCurrentTrip(&trip)

				s.Population().InStation()[event.IdStation()][passenger.Id()] = &passenger
			}
		}
	}
}

func (s *Simulator) getEventsStationClosed(start,
	end time.Time) []*models.EventStationClosed {
	var output = make([]*models.EventStationClosed, 0)

	for i := range s.eventsStationClosed {
		if (s.eventsStationClosed[i].Start().Before(end) &&
			s.eventsStationClosed[i].Start().After(start)) ||
			(s.eventsStationClosed[i].End().Before(end) &&
				s.eventsStationClosed[i].End().After(start)) {
			output = append(output, &s.eventsStationClosed[i])
		}
	}

	return output
}

func (s *Simulator) getEventsLineDelay(start,
	end time.Time) []*models.EventLineDelay {
	var output []*models.EventLineDelay

	for i := range s.eventsLineDelay {
		if (s.eventsLineDelay[i].Start().Before(end) &&
			s.eventsLineDelay[i].Start().After(start)) ||
			(s.eventsLineDelay[i].End().Before(end) &&
				s.eventsLineDelay[i].End().After(start)) {
			output = append(output, &s.eventsLineDelay[i])
		}
	}

	return output
}

func (s *Simulator) getEventsLineClosed(start,
	end time.Time) []*models.EventLineClosed {
	var output []*models.EventLineClosed

	for i := range s.eventsLineClosed {
		if (s.eventsLineClosed[i].Start().Before(end) &&
			s.eventsStationClosed[i].Start().After(start)) ||
			(s.eventsLineClosed[i].End().Before(end) &&
				s.eventsStationClosed[i].End().After(start)) {
			output = append(output, &s.eventsLineClosed[i])
		}
	}

	return output
}

func (s *Simulator) getEventsAttendancePeak(start,
	end time.Time) []*models.EventAttendancePeak {
	var output []*models.EventAttendancePeak

	for i := range s.eventsAttendancePeak {
		if s.eventsAttendancePeak[i].Time().Before(end) &&
			s.eventsAttendancePeak[i].Time().After(start) {
			output = append(output, &s.eventsAttendancePeak[i])
		}
	}
	return output
}

/*
NextEventsTrain call the next events.
*/
func (s *Simulator) NextEventsTrain() ([]*models.MetroTrain, time.Time) {
	var output []*models.MetroTrain

	var nextEventsTime []time.Time
	var lowestTime = time.Time{}
	//maxAuthorizedTime = lowestTime + timeInStation OR lowestTime +
	//maxTimeBeforeTrainInSameStation-1
	//(we avoid having two train in the same station)
	var maxAuthorizedTime time.Time

	for _, train := range s.trains {
		nextEventsTime = append(nextEventsTime, train.TimeArrivalNextStation())
		if lowestTime.IsZero() || lowestTime.After(train.TimeArrivalNextStation()) {
			lowestTime = train.TimeArrivalNextStation()
		}
	}

	maxAuthorizedTime = lowestTime.Add(time.Duration(
		s.config.TimeInStation()) * time.Second)

	for i := range nextEventsTime {
		if nextEventsTime[i].Before(maxAuthorizedTime) {
			output = append(output, s.trains[i])
		}
	}

	return output, maxAuthorizedTime
}

func (s *Simulator) WriteInTimetableReal(train *models.MetroTrain,
	timeArrival time.Time) {

	line := train.GetLine()
	station := train.GetCurrentStation()
	nextStation := train.GetNextStation()
	direction := train.GetDirection()
	var i = station.Id()
	var j = nextStation.Id()
	timeBetweenStation := s.mapObject.GraphTimeBetweenStation()[i][j]
	delay := s.mapObject.GraphDelay()[station.Id()][nextStation.Id()]
	timeInStation := s.config.TimeInStation()
	departure := timeArrival.Add(time.Duration(timeInStation) * time.Second)
	nextArrival := departure.Add(time.Duration(timeBetweenStation+delay) *
		time.Second)
	nextDeparture := nextArrival.Add(time.Duration(timeInStation) * time.Second)
	isRevenue := true
	//TODO is there people on the train ?

	metroStationTab := [2]models.MetroStation{*station, *nextStation}
	timeTab := [4]time.Time{timeArrival, nextArrival, departure, nextDeparture}
	event := models.NewEventTimetableTrain(line, metroStationTab, train,
		direction, timeTab, isRevenue, train.GetTripNumber())
	s.timetableReal.AddEventsTrain(event)
}

/*
GetTrainsPerLine is used get all trains of a given line
*/
func (s *Simulator) GetTrainsPerLine(
	line models.MetroLine) []*models.MetroTrain {
	var listTrains []*models.MetroTrain
	for i := 0; i < len(s.GetTrains()); i++ {
		if s.GetTrains()[i].GetLine().Equals(line) {
			listTrains = append(listTrains, s.GetTrains()[i])
		}
	}
	return listTrains
}

func (s *Simulator) ToCSV() {
	s.timetableReal.ToCSV()
	//TODO CSV output writing should be here
}
