package models

import (
	"pfe-2018-network-journey-simulator/src/tools"
	"strconv"
)

type TimetableReal struct {
	eventsTrain []*EventTimetableTrain
}

func (timetableReal *TimetableReal) GetEventsTrain() []*EventTimetableTrain {
	return timetableReal.eventsTrain
}

func (timetableReal *TimetableReal) AddEventsTrain(event *EventTimetableTrain) {
	timetableReal.eventsTrain = append(timetableReal.eventsTrain, event)
}

// --- Constructor
func NewTimetableReal() TimetableReal {
	return TimetableReal{}
}

// generate CSV file
func (timetableReal TimetableReal) ToCSV() {
	var aux string

	var row = make([]string, 16)
	row[0] = "line name"
	row[1] = "train number"
	row[2] = "TRS number"
	row[3] = "trip number"
	row[4] = "is revenue"
	row[5] = "direction"
	row[6] = "source station"
	row[7] = "time arrived to the source station"
	row[8] = "time departed from the source station"
	row[9] = "destination station"
	row[10] = "time arrived to the destination station"
	row[11] = "time departed from the destination station"
	row[12] = "depot in/out" //not used in naia
	row[13] = "car number"   //not used in naia
	row[14] = "train trip"   //not used in naia
	row[15] = "train km"     //not used in naia
	csv := tools.NewFile("timetableReal", row)

	var currentEvent *EventTimetableTrain
	//for each timetableStation
	for i := 0; i < len(timetableReal.GetEventsTrain()); i++ {
		currentEvent = timetableReal.GetEventsTrain()[i]
		row[0] = currentEvent.Line().Name()
		row[1] = strconv.Itoa(currentEvent.Train().Id())
		aux = "000" + strconv.Itoa(currentEvent.Train().Id())
		row[2] = aux[len(aux)-3:]
		row[3] = strconv.Itoa(currentEvent.TripNumber())
		row[4] = "" //not ready
		row[5] = currentEvent.Direction()
		row[6] = currentEvent.Station().Name()
		row[7] = currentEvent.Arrival().String()
		row[8] = currentEvent.Departure().String()
		row[9] = currentEvent.NextStation().Name()
		row[10] = currentEvent.NextArrival().String()
		row[11] = currentEvent.NextDeparture().String()
		row[12] = "" //not ready
		row[13] = "" //not ready
		row[14] = "" //not ready
		row[15] = "" //not ready
		csv.Write(row)
	}
}
