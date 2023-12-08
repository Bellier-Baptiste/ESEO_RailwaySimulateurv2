/*
Package simulator

File : simulator.go

Brief : Simulator is the struct that represents the simulator.

Date : 24/01/2019

Author :
  - Team v1
  - Team v2
  - Paul TRÉMOUREUX
  - Alexis BONAMY
  - Benoît VAVASSEUR
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
	"fmt"
	"math"
	"math/rand"
	"network-journey-simulator/src/configs"
	"network-journey-simulator/src/models"
	"strconv"
	"sync"
	"time"
)

/*
Simulator is the struct that represents the simulator.

Attributes :
  - config configs.ConfigurationType : the config object of the simulator
  - adConfig *configs.AdvancedConfig : the advanced config object of the
    simulator
  - mapObject models.Map : the map of the simulator
  - population *models.Population : the population of the simulator
  - trains []*models.MetroTrain : the trains of the simulator
  - currentTime time.Time : the current time of the simulator
  - timetable models.Timetable : the timetable of the simulator
  - timetableReal models.TimetableReal : the real timetable of the simulator
  - eventsStationClosed []models.EventStationClosed : the events of the
    simulator
  - eventsLineDelay []models.EventLineDelay : the events of the simulator
  - eventsLineClosed []models.EventLineClosed : the events of the simulator
  - eventsAttendancePeak []models.EventAttendancePeak : the events of the
    simulator
  - areasDistribution []models.PopulationDistribution : the population
    distribution of an area
  - tripNumberCounter int : the trip number counter of the simulator

Methods :
  - Config() configs.ConfigurationType : get the config object of the
    simulator
  - GetTrains() []*models.MetroTrain : get the trains of the simulator
  - Population() *models.Population : get the population of the simulator
  - GetAllEventsLineClosed() []models.EventLineClosed : get the line closed
    events of the simulator
  - GetAllEventsAttendancePeak() []models.EventAttendancePeak : get the
    attendance peak events of the simulator
  - CreateEventsStationClose() : create "station close" event of the
    simulator
  - CreateEventsLineDelay() : create "line delay" event of the simulator
  - CreateEventsLineClose() : create "line close" event of the simulator
  - CreateEventsAttendancePeak() : create "attendance peak" event of the
    simulator
  - AddTrainLinePeer(line *models.MetroLine, shift, count, aux1, aux2 int)
    (*models.MetroLine, int, int, int, int) : add train to line (peer train
    number) and return it
  - AddTrainLineOdd(line *models.MetroLine, shift, count, aux1, aux2, j int)
    (*models.MetroLine, int, int, int, int) : add train to line (peer train
    number) and return it
  - Init(dayType string) (bool, error) : initialise the simulator
  - Run(n int) : run n loops (one loop == one event)
  - RunOnce() : run one loop (one loop == one event)
  - NextEventsTrain() ([]*models.MetroTrain, time.Time) : get the next event
    of the simulator
  - WriteInTimetableReal(train *models.MetroTrain, aTime time.Time) : write
    in the real timetable of the simulator
  - GetTrainPerLine() map[int][]*models.MetroTrain : get the trains per line
    of the simulator
  - ToCSV() : save the timetable and timetableReal as CSV
*/
type Simulator struct {
	config                   configs.ConfigurationType
	adConfig                 *configs.AdvancedConfig
	mapObject                models.Map
	population               *models.Population
	trains                   []*models.MetroTrain
	currentTime              time.Time
	timetable                models.Timetable
	timetableReal            models.TimetableReal
	eventsStationClosed      []models.EventStationClosed
	eventsLineDelay          []models.EventLineDelay
	eventsLineClosed         []models.EventLineClosed
	eventsAttendancePeak     []models.EventAttendancePeak
	tripNumberCounter        int
	populationsDistributions []models.PopulationDistribution
	destinationDistributions []models.DestinationDistribution
	areas                    []models.Area
}

const (
	strErr      = " error : "
	strEacParse = "EventAttendancePeak : couldn't parse date : "
)

/*
Config is used to get the config object of the simulator.

Param :
  - s *Simulator : the simulator

Return :
  - configs.ConfigurationType : the config object of the simulator
*/
func (s *Simulator) Config() configs.ConfigurationType {
	return s.config
}

/*
GetTrains is used to get the trains of the simulator.

Param :
  - s *Simulator : the simulator

Return :
  - []*models.MetroTrain : the trains of the simulator
*/
func (s *Simulator) GetTrains() []*models.MetroTrain {
	return s.trains
}

/*
NewSimulator is used to create a new simulator.

Return :
  - *Simulator : the new simulator
*/
func NewSimulator() *Simulator {
	simulator := &Simulator{
		config:                   configs.GetInstance(),
		adConfig:                 nil,
		mapObject:                models.Map{},
		population:               nil,
		trains:                   make([]*models.MetroTrain, 0),
		currentTime:              time.Now(),
		timetable:                models.Timetable{},
		timetableReal:            models.TimetableReal{},
		eventsStationClosed:      make([]models.EventStationClosed, 0),
		eventsLineDelay:          make([]models.EventLineDelay, 0),
		eventsLineClosed:         make([]models.EventLineClosed, 0),
		eventsAttendancePeak:     make([]models.EventAttendancePeak, 0),
		populationsDistributions: make([]models.PopulationDistribution, 0),
		destinationDistributions: make([]models.DestinationDistribution, 0),
		areas:                    make([]models.Area, 0),
		tripNumberCounter:        0,
	}
	return simulator
}

/*
Population is used to get the population of the simulator.

Param :
  - s *Simulator : the simulator

Return :
  - *models.Population : the population of the simulator
*/
func (s *Simulator) Population() *models.Population {
	return s.population
}

/*
GetAllEventsLineClosed is used to get the line closed events of the simulator.

Param :
  - s *Simulator : the simulator

Return :
  - []models.EventLineClosed : the events of the simulator
*/
func (s *Simulator) GetAllEventsLineClosed() []models.EventLineClosed {
	return s.eventsLineClosed
}

/*
GetAllEventsStationClosed is used to get the station closed events of the
simulator.

Param :
  - s *Simulator : the simulator

Return :
  - []models.EventStationClosed : the events of the simulator
*/
func (s *Simulator) GetAllEventsStationClosed() []models.EventStationClosed {
	return s.eventsStationClosed
}

/*
GetAllEventsAttendancePeak is used to get the attendance peak events of the
simulator.

Param :
  - s *Simulator : the simulator

Return :
  - []models.EventAttendancePeak : the events of the simulator
*/
func (s *Simulator) GetAllEventsAttendancePeak() []models.EventAttendancePeak {
	return s.eventsAttendancePeak
}

/*
GetAllPopDistribution is used to get the population distribution of
all the areas.

Param :
  - s *Simulator : the simulator

Return :
  - []models.PopulationDistribution : the population distribution of all the
    areas
*/
func (s *Simulator) GetAllPopDistribution() []models.PopulationDistribution {
	return s.populationsDistributions
}

/*
GetAllDestDistribution is used to get the destination distribution of
all the areas.

Param :
  - s *Simulator : the simulator

Return :
  - []models.DestinationDistribution : the destination distribution of all the
    areas
*/
func (s *Simulator) GetAllDestDistribution() []models.DestinationDistribution {
	return s.destinationDistributions
}

/*
GetPopulationDistributionArea is used to obtain the population distribution
of an area.

Param :
  - s *Simulator : the simulator
  - id int : the id of the area

Return :
  - models.PopulationDistribution : the population distribution of the
    area
*/
func (s *Simulator) GetPopulationDistributionArea(
	id int) models.PopulationDistribution {
	return s.populationsDistributions[id]
}

/*
GetDestinationDistributionArea is used to obtain the destination distribution
of an area.

Param :
  - s *Simulator : the simulator

Return :
  - []models.DestinationDistribution : the destination distribution of the
    areas
*/
func (s *Simulator) GetDestinationDistributionArea(
	id int) models.DestinationDistribution {
	return s.destinationDistributions[id]
}

/*
GetPopulationDistributionStation is used to obtain the population distribution
of a station.
The station has an idArea attribute which is used to obtain the corresponding
population distribution for the area.

Param :
  - s *Simulator : the simulator
  - id int : the id of the station

Return :
  - models.PopulationDistribution : the population distribution of the
    station
*/
func (s *Simulator) GetPopulationDistributionStation(
	id int) models.PopulationDistribution {
	ms := s.adConfig.MapC.Stations[id]
	if ms.IdArea == nil {
		return models.NewPopulationDistribution(14, 14, 14, 15, 14, 14, 15)
	}
	idArea := *ms.IdArea
	return s.populationsDistributions[idArea]
}

/*
GetDestinationDistributionStation is used to obtain the destination distribution
of a station.
The station has an idArea attribute which is used to obtain the corresponding
destination distribution for the area.

Param :
  - s *Simulator : the simulator
  - id int : the id of the station

Return :
  - models.DestinationDistribution : the destination distribution of the
    station
*/
func (s *Simulator) GetDestinationDistributionStation(
	id int) models.DestinationDistribution {
	ms := s.adConfig.MapC.Stations[id]
	if ms.IdArea == nil {
		return models.NewDestinationDistribution(15, 14, 14, 14, 15, 14, 14)
	}
	idArea := *ms.IdArea
	return s.destinationDistributions[idArea]
}

/*
GetIdAreaStation is used to get the areaId of a station.

Param :
  - s *Simulator : the simulator
  - id int : the id of the metroStation

Return :
  - int : the areaId of the station, -1 if the station has no areaId
*/
func (s *Simulator) GetIdAreaStation(id int) int {
	station := s.adConfig.MapC.Stations[id]
	if station.IdArea == nil {
		return -1
	}
	return *station.IdArea
}

/*
GetIdAreaStations is used to get the areaIds of all the stations.

Param :
  - s *Simulator : the simulator

Return :
  - []int : the areaIds of all the stations
*/
func (s *Simulator) GetIdAreaStations() []int {
	var areaIds []int
	for _, ms := range s.adConfig.MapC.Stations {
		if ms.IdArea != nil {
			areaIds = append(areaIds, *ms.IdArea)
		} else {
			areaIds = append(areaIds, -1)
		}
	}
	return areaIds
}

/*
CreateEventsStationClose is used to create "station close" event of the
simulator.

Param :
  - s *Simulator : the simulator
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
CreateEventsLineDelay is used to create "line delay" event of the simulator.

Param :
  - s *Simulator : the simulator
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
CreateEventsLineClose is used to create "line close" event of the simulator.

Param :
  - s *Simulator : the simulator
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
CreateEventsAttendancePeak is used to create "attendance peak" event of the
simulator.

Param :
  - s *Simulator : the simulator
*/
func (s *Simulator) CreateEventsAttendancePeak() {
	s.eventsAttendancePeak = make([]models.EventAttendancePeak,
		len(s.adConfig.MapC.EventsAttendancePeak))
	for i, ev := range s.adConfig.MapC.EventsAttendancePeak {
		startEv, err := time.Parse(time.RFC3339, ev.StartString)
		if err != nil {
			fmt.Print(strEacParse,
				ev.StartString, strErr, err)
			continue
		}
		endEv, err := time.Parse(time.RFC3339, ev.EndString)
		if err != nil {
			fmt.Print(strEacParse,
				ev.EndString, strErr, err)
			continue
		}
		peakEv, err := time.Parse(time.RFC3339, ev.PeakString)
		if err != nil {
			fmt.Print(strEacParse,
				ev.PeakString, strErr, err)
			continue
		}
		s.eventsAttendancePeak[i] = models.NewEventAttendancePeak(startEv,
			endEv, peakEv, ev.StationId, ev.PeakSize, ev.PeakWidth)
	}
}

/*
CreateAreasDistribution is used to create "area distribution"
*/
func (s *Simulator) CreatePopulationsDistribution() {
	s.populationsDistributions = make([]models.PopulationDistribution,
		len(s.adConfig.MapC.Areas))
	for i, pd := range s.adConfig.MapC.Areas {
		s.populationsDistributions[i] =
			models.NewPopulationDistribution(pd.PopulationDistribution.Businessman,
				pd.PopulationDistribution.Child, pd.PopulationDistribution.Retired,
				pd.PopulationDistribution.Student, pd.PopulationDistribution.Tourist,
				pd.PopulationDistribution.Unemployed, pd.PopulationDistribution.Worker)
	}
}

func (s *Simulator) CreateDestinationDistribution() {
	s.destinationDistributions = make([]models.DestinationDistribution,
		len(s.adConfig.MapC.Areas))
	for i, dd := range s.adConfig.MapC.Areas {
		s.destinationDistributions[i] =
			models.NewDestinationDistribution(dd.DestinationDistribution.Commercial,
				dd.DestinationDistribution.Educational,
				dd.DestinationDistribution.Industrial,
				dd.DestinationDistribution.Leisure, dd.DestinationDistribution.Office,
				dd.DestinationDistribution.Residential,
				dd.DestinationDistribution.Touristic)
	}
}

/*
AddTrainLinePeer is used to add train to line (peer train number) and return it.

Param :
  - s *Simulator : the simulator
  - line *models.MetroLine : the line
  - shift int : the shift
  - count int : the count
  - aux1 int : the aux1
  - aux2 int : the aux2

Return :
  - *models.MetroLine : the line
  - int : the shift
  - int : the count
  - int : the aux1
  - int : the aux2
*/
func (s *Simulator) AddTrainLinePeer(line *models.MetroLine,
	shift, count, aux1, aux2 int) (*models.MetroLine, int, int, int, int) {
	for k := 0; k < line.TrainNumber()/2; k++ {
		shift = k * (models.LineTimeLength(line,
			s.MapObject.GraphTimeBetweenStation(),
			s.MapObject.GraphDelay(),
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
					s.MapObject.GraphTimeBetweenStation()[aux1][aux2]+
						s.MapObject.GraphDelay()[aux1][aux2]) * time.Second))
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
					s.MapObject.GraphTimeBetweenStation()[aux1][aux2]+
						s.MapObject.GraphDelay()[aux1][aux2]) * time.Second))
		s.trains[count].SetId(count)
		count++

	}
	return line, shift, count, aux1, aux2
}

/*
AddTrainLineOdd is used to add train to line (peer train number) and return it.

Param :
  - s *Simulator : the simulator
  - line *models.MetroLine : the line
  - shift int : the shift
  - count int : the count
  - aux1 int : the aux1
  - aux2 int : the aux2
  - j int : the j

Return :
  - *models.MetroLine : the line
  - int : the shift
  - int : the count
  - int : the aux1
  - int : the aux2
*/
func (s *Simulator) AddTrainLineOdd(line *models.MetroLine,
	shift, count, aux1, aux2, j int) (*models.MetroLine, int, int, int, int) {
	for l := 0; l < line.TrainNumber(); l++ {
		shift = l * (models.LineTimeLength(s.MapObject.Lines()[j],
			s.MapObject.GraphTimeBetweenStation(),
			s.MapObject.GraphDelay(),
			len(s.MapObject.Lines()[j].Stations()),
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
						s.MapObject.GraphTimeBetweenStation()[aux1][aux2]+
							s.MapObject.GraphDelay()[aux1][aux2]) * time.Second))
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

/*
Init is used to initialise the simulator.

Param :
  - s *Simulator : the simulator
  - dayType string : the day type

Return :
  - bool : true if the initialisation is a success, false otherwise
  - error : an error if the initialisation is a failure
*/
func (s *Simulator) Init(dayType string) (bool, error) {

	// load config
	s.config = configs.GetInstance()
	s.adConfig = configs.GetAdvancedConfigInstance()

	// initialisation of the day type in the config
	fmt.Println("dayType changed in : ", dayType)
	s.config.ChangeParam("day type", dayType)
	fmt.Println("dayType changed in : ", s.config.Get("day type"), "-> ok !")

	//generate events
	s.CreateEventsStationClose()

	s.CreateEventsLineDelay()

	s.CreateEventsLineClose()

	s.CreateEventsAttendancePeak()

	//generate populations distribution
	s.CreatePopulationsDistribution()

	s.CreateDestinationDistribution()

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

	if s.config.PreTimetable() {
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
Run is used to run n loops (one loop == one event). Put n < 0 in order to run
until the end of the period defined in config.json. If n == 0, the function
will do nothing.

Param :
  - s *Simulator : the simulator
  - n int : the number of loops
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

/*
RunOnce is used to run one loop (one loop == one event). It will run until the
next event. If multiple events occur at the same time, they will be done.

Param :
  - s *Simulator : the simulator
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
executeESCStartEventROSetPassengerStart is used to set the path of a passenger
that starts at a closed station (if possible) or remove the trip.

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - nearestStation *models.MetroStation : the nearest station
  - i string : the id of the passenger
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
executeESCStartEventROSetPath is used to set the path of a passenger that starts
at a closed station (if possible) or remove the trip.

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - pass *models.Passenger : the passenger
  - i string : the id of the passenger

Return :
  - *models.Trip : the trip
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
executeESCStartEventRerouteOutside is used to reroute passengers in population
outside.

Param :
  - s *Simulator : the simulator
  - event *models.EventStationClosed : the event
  - stationEvent models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
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
executeESCStartEventRerouteCloseStation is used to reroute passengers in closed
station.

Param :
  - s *Simulator : the simulator
  - event *models.EventStationClosed : the event
  - stationEvent models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
  - currentTime time.Time : the current time
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
executeESCStartEventRerouteWaitingLoop is used to reroute passengers in stations
waiting for train (loop).

Param :
  - s *Simulator : the simulator
  - stationId int : the station id
  - stationEvent models.MetroStation : the station
  - station map[string]*models.Passenger : the station
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
executeESCStartEventRerouteWaiting is used to reroute passengers in stations
waiting for train.

Param :
  - s *Simulator : the simulator
  - event *models.EventStationClosed : the event
  - stationEvent models.MetroStation : the station
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
executeESCStartEventRIEndStationClosed is used to reroute passengers in train
(end station closed).

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - nearestStation *models.MetroStation : the nearest station
  - nextStationTrain *models.MetroStation : the next station
  - nextStationOpenedTrain *models.MetroStation : the next opened station

Return :
  - *models.Trip : the trip
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
executeESCStartEventRIEventStation is used to reroute passengers in train (event
station on path).

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - stationEvent models.MetroStation : the station
  - nextStationTrain *models.MetroStation : the next station
  - nextStationOpenedTrain *models.MetroStation : the next opened station

Return :
  - *models.Trip : the trip
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
executeESCStartEventRerouteInside is used to reroute passengers in train.

Param :
  - s *Simulator : the simulator
  - stationEvent models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
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
executeESCStartEvent is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - stationEvent models.MetroStation : the station
  - event *models.EventStationClosed : the event
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
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
executeESCEndEvent is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - event *models.EventStationClosed : the event
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
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
executeESCReroutePassenger is used to reroute passenger.

Param :
  - s *Simulator : the simulator
  - events []*models.EventStationClosed : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
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

/*
executeEventsStationClosed is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - events []*models.EventStationClosed : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
*/
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
timeArrivalUpdate is used to update time arrival next station for trains on this
part of line.

Param :
  - s *Simulator : the simulator
  - event *models.EventLineDelay
  - lineDelay *models.MetroLine

Return :
  - *models.MetroLine : lineDelay
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
for trains on this part of line.

Param :
  - s *Simulator : the simulator
  - event *models.EventLineDelay : the event
  - lineDelay *models.MetroLine : the line
  - direction string : the direction
  - currentTime time.Time : the current time
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
for trains on this part of line.

Param :
  - s *Simulator : the simulator
  - event *models.EventLineDelay : the event
  - lineDelay *models.MetroLine : the line
  - direction string : the direction
  - currentTime time.Time : the current time
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
checkDirection is used to affect the direction.

Param :
  - s *Simulator : the simulator
  - event *models.EventLineDelay : the event
  - direction string : the direction

Return :
  - string : direction
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

/*
executeEventsLineDelay is used to start the event "line delay".

Param :
  - s *Simulator : the simulator
  - events []*models.EventLineDelay : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time

Return :
  - []*models.EventLineDelay : the events
*/
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
executeELCStartEventROSetPassengerStart is used to set the path of a passenger.

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - nearestStation *models.MetroStation : the nearest station
  - i string : the id of the passenger
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
			if j == k {
				s.population.Outside()[i].RemoveTrip(trip)
				if s.config.PrintDebug() {
					fmt.Println(" - deleted path")
				}
				return
			}
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
executeELCStartEventROSetPath is used to set the path of a passenger.

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - pass *models.Passenger : the passenger
  - i string : the id of the passenger

Return :
  - *models.Trip : the trip
*/
func (s *Simulator) executeELCStartEventROSetPath(trip *models.Trip,
	pass *models.Passenger, i string) *models.Trip {
	//trip = pass.NextTrip()
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
executeELCStartEventRerouteOutside is used to reroute passengers in population
outside.

Param :
  - s *Simulator : the simulator
  - event *models.EventLineClosed : the event
  - stationEvent *models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
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

			if trip != nil {
				trip = s.executeELCStartEventROSetPath(trip, pass, i)
			}
		}
	}
}

/*
executeELCStartEventRerouteCloseLine is used to reroute passengers in closed
line.

Param :
  - s *Simulator : the simulator
  - currentTime time.Time : the current time
  - stationEvent *models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
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
executeELCStartEventRerouteWaitingLoop is used to reroute passengers in stations
waiting for train (loop).

Param :
  - s *Simulator : the simulator
  - id int : the id of the station
  - stationEvent *models.MetroStation : the station
  - station map[string]*models.Passenger : the station
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
executeELCStartEventRerouteWaiting is used to reroute passengers in stations
waiting for train.

Param :
  - s *Simulator : the simulator
  - stationEvent *models.MetroStation : the station
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
executeELCStartEventRIEndStationClosed is used to reroute passengers in train
(end station closed).

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - nearestStation *models.MetroStation : the nearest station
  - nextStationTrain *models.MetroStation : the next station
  - nextStationOpenedTrain *models.MetroStation : the next opened station

Return :
  - *models.Trip : trip
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
executeELCStartEventRIEventStation is used to reroute passengers in train (event
station on path).

Param :
  - s *Simulator : the simulator
  - trip *models.Trip : the trip
  - stationEvent *models.MetroStation : the station
  - nextStationTrain *models.MetroStation : the next station
  - nextStationOpenedTrain *models.MetroStation : the next opened station

Return :
  - *models.Trip : trip
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
executeELCStartEventRerouteInside is used to reroute passengers in train.

Param :
  - s *Simulator : the simulator
  - stationEvent *models.MetroStation : the station
  - nearestStation *models.MetroStation : the nearest station
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
executeELCStartEvent is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - lineEvent []*models.MetroStation : the line
  - event *models.EventLineClosed : the event
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time

Return :
  - []*models.MetroStation : the line
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
executeELCEndEvent is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - lineEvent []*models.MetroStation : the line
  - event *models.EventLineClosed : the event
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
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
executeELCReroutePassenger is used to reroute passenger.

Param :
  - s *Simulator : the simulator
  - events []*models.EventLineClosed : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time

Return :
  - []*models.EventLineClosed : the events
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
executeELCAffectES is used to affect "close" or "open" to station status.

Param :
  - s *Simulator : the simulator
  - eventStations []*models.MetroStation : the stations
  - event *models.EventLineClosed : the event
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
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

/*
executeEventsLineClosed is used to start the event "line close".

Param :
  - s *Simulator : the simulator
  - events []*models.EventLineClosed : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time

Return :
  - []*models.EventLineClosed : the events
*/
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

/*
executeEventsAttendancePeak is used to start the event "attendance peak".

Param :
  - s *Simulator : the simulator
  - events []*models.EventAttendancePeak : the events
  - oldTime time.Time : the old time
  - currentTime time.Time : the current time
*/
func (s *Simulator) executeEventsAttendancePeak(
	events []*models.EventAttendancePeak, oldTime time.Time,
	currentTime time.Time) {
	for _, event := range events {
		// get minutes
		startMinutes := event.GetStart().Hour()*60 + event.GetStart().Minute()
		endMinutes := event.GetEnd().Hour()*60 + event.GetEnd().Minute()
		peakMinutes := event.GetPeak().Hour()*60 + event.GetPeak().Minute()
		oldMinutes := oldTime.Hour()*60 + oldTime.Minute()
		currentMinutes := currentTime.Hour()*60 + currentTime.Minute()

		// get passenger distribution
		passengerDistribution := s.makeEventAttendancePeakDistribution(startMinutes,
			endMinutes, peakMinutes, event)

		// determine the number of passengers for the current minute by taking the
		// difference between the current time and the old time
		currentPassengers := s.pickEventAttendancePeakCurrentPassengers(
			passengerDistribution, oldMinutes, currentMinutes, startMinutes)

		// execute the event
		for i := 0; i < currentPassengers; i++ {
			// get total population
			var totalPopulation int
			totalPopulation += len(s.population.Outside())
			for _, station := range s.population.InStation() {
				totalPopulation += len(station)
			}
			for _, train := range s.population.InTrains() {
				totalPopulation += len(train)
			}

			departureStation := s.mapObject.FindStationById(event.GetIdStation())
			// create arrival station, different from departure station
			arrivalStation := departureStation
			for arrivalStation == departureStation {
				arrivalStation = s.mapObject.FindStationById(rand.Intn(
					len(s.mapObject.Stations())))
			}

			ps, _ := s.mapObject.GetPathStation(*departureStation, *arrivalStation)
			trip := models.NewTrip(currentTime, ps)

			passenger := models.NewPassenger(strconv.Itoa(totalPopulation),
				models.PickAKind())
			passenger.SetTrips([]*models.Trip{&trip})
			passenger.SetCurrentTrip(&trip)

			s.population.Outside()[passenger.Id()] = &passenger
			s.population.TransferFromPopulationToStation(&passenger, departureStation,
				currentTime)
			s.population.InStation()[event.GetIdStation()][passenger.Id()] =
				&passenger
		}
	}
}

/*
makeEventAttendancePeakDistribution is used to create a distribution of
passengers for an event "attendance peak".

Param :
  - s *Simulator : the simulator
  - startMinutes int : the start minutes
  - endMinutes int : the end minutes
  - peakMinutes int : the peak minutes
  - event *models.EventAttendancePeak : the event

Return :
  - []int : the distribution
*/
func (s *Simulator) makeEventAttendancePeakDistribution(startMinutes,
	endMinutes, peakMinutes int, event *models.EventAttendancePeak) []int {
	// create a table to store the number of passengers for each minute
	passengerDistribution := make([]int, endMinutes-startMinutes)

	// calcultate asymetrical gaussian distribution
	peakArea := float64(event.GetPeakSize())
	peakHeight := peakArea / (float64(event.GetPeakWidth()) * math.Sqrt(2*math.Pi))
	for minute := startMinutes; minute < endMinutes; minute++ {
		exponent := -math.Pow(float64(minute-peakMinutes), 2) /
			(2 * math.Pow(float64(event.GetPeakWidth()), 2))
		passengerDistribution[minute-startMinutes] = int(peakHeight *
			math.Exp(exponent))
	}

	// get total number of passengers from passengerDistribution
	totalPassengers := 0
	for _, passengers := range passengerDistribution {
		totalPassengers += passengers
	}

	// add missing passengers to the distribution or remove passengers if
	// there are too many
	value := event.GetPeakSize() > totalPassengers
	print(value)
	if event.GetPeakSize() > totalPassengers {
		missingPassengers := event.GetPeakSize() - totalPassengers
		// add missing passengers
		for missingPassengers > 0 {
			passengerDistribution[rand.Intn(len(passengerDistribution))]++
			missingPassengers--
		}
	}
	if event.GetPeakSize() < totalPassengers {
		missingPassengers := totalPassengers - event.GetPeakSize()
		// remove passengers
		for missingPassengers > 0 {
			passengerDistribution[rand.Intn(len(passengerDistribution))]--
			missingPassengers--
		}
	}

	return passengerDistribution
}

func (s *Simulator) pickEventAttendancePeakCurrentPassengers(
	passengerDistribution []int, oldMinutes, currentMinutes,
	startMinutes int) int {

	currentPassengers := 0
	for minute := oldMinutes; minute < currentMinutes; minute++ {
		if minute < startMinutes {
			continue
		}
		currentPassengers += passengerDistribution[minute-startMinutes]
	}
	return currentPassengers
}

/*
getEventsStationClosed is used to get the events "station close".

Param :
  - s *Simulator : the simulator
  - start time.Time : the start time
  - end time.Time : the end time

Return :
  - []*models.EventStationClosed : the events
*/
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

/*
getEventsLineDelay is used to get the events "line delay".

Param :
  - s *Simulator : the simulator
  - start time.Time : the start time
  - end time.Time : the end time

Return :
  - []*models.EventLineDelay : the events
*/
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

/*
getEventsLineClosed is used to get the events "line close".

Param :
  - s *Simulator : the simulator
  - start time.Time : the start time
  - end time.Time : the end time

Return :
  - []*models.EventLineClosed : the events
*/
func (s *Simulator) getEventsLineClosed(start,
	end time.Time) []*models.EventLineClosed {
	var output []*models.EventLineClosed

	for i := range s.eventsLineClosed {
		if (s.eventsLineClosed[i].Start().Before(end) &&
			s.eventsLineClosed[i].Start().After(start)) ||
			(s.eventsLineClosed[i].End().Before(end) &&
				s.eventsLineClosed[i].End().After(start)) {
			output = append(output, &s.eventsLineClosed[i])
		}
	}

	return output
}

/*
getEventsAttendancePeak is used to get the events "attendance peak".

Param :
  - s *Simulator : the simulator
  - start time.Time : the start time
  - end time.Time : the end time

Return :
  - []*models.EventAttendancePeak : the events
*/
func (s *Simulator) getEventsAttendancePeak(start,
	end time.Time) []*models.EventAttendancePeak {
	var output []*models.EventAttendancePeak

	for i := range s.eventsAttendancePeak {
		if s.eventsAttendancePeak[i].GetStart().Before(end) &&
			s.eventsAttendancePeak[i].GetEnd().After(start) {
			output = append(output, &s.eventsAttendancePeak[i])
		}
	}
	return output
}

/*
NextEventsTrain call the next events.

Param :
  - s *Simulator : the simulator

Return :
  - []*models.MetroTrain : the trains
  - time.Time : the time
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

/*
WriteInTimetableReal is used to write in the timetable real.

Param :
  - s *Simulator : the simulator
  - train *models.MetroTrain : the train
  - timeArrival time.Time : the time arrival
*/
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
	//isRevenue := true
	//TODO is there people on the train ?

	metroStationTab := [2]models.MetroStation{*station, *nextStation}
	timeTab := [4]time.Time{timeArrival, nextArrival, departure, nextDeparture}
	event := models.NewEventTimetableTrain(line, metroStationTab, train,
		direction, timeTab, true /*isRevenue*/, train.GetTripNumber())
	s.timetableReal.AddEventsTrain(event)
}

/*
GetTrainsPerLine is used get all trains of a given line in the simulator.

Param :
  - s *Simulator : the simulator
  - line models.MetroLine : the line

Return :
  - []*models.MetroTrain : the trains
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

/*
ToCSV is used to write in a CSV file.

Param :
  - s *Simulator : the simulator
*/
func (s *Simulator) ToCSV() {
	s.timetableReal.ToCSV()
	//TODO CSV output writing should be here
}
