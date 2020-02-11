package models

import (
	"log"
	"math/rand"
	"configs"
	"time"
)

type Trip struct {
	departureTime time.Time
	arrivalTime   time.Time
	path          pathStation
}

//--- constructors

func NewTrip(departure time.Time, path pathStation) Trip {
	var aTrip Trip
	aTrip.departureTime = departure
	aTrip.path = path
	return aTrip
}



//Generate a random hour and minute in the given day
// within the boundaries defined by Business day start and end
func RandomTime(currentDay time.Time) time.Time {
	var currentDayMidnight = time.Date(currentDay.Year(),currentDay.Month(),currentDay.Day(),0,0,0,0,currentDay.Location())
	pickedTime := currentDayMidnight

	bDayStart, error := time.ParseDuration(configs.GetInstance().BusinessDayStart())
	if error != nil{
		log.Fatal(error, "BusinessDayStart - couldn't parse duration")
	}

	aDay, error := time.ParseDuration("24h")
	if error != nil{
		log.Fatal(error, "a Day- couldn't parse duration")
	}

	bDayEnd, error := time.ParseDuration(configs.GetInstance().BusinessDayEnd())
	if error != nil{
		log.Fatal(error, "BusinessDayEnd - couldn't parse duration")
	}

	pickedTime = pickedTime.Add(bDayStart)
	minutesSpan := bDayEnd.Minutes() + 1440 - bDayStart.Minutes()
	minute := rand.Intn(int(minutesSpan))
	pickedTime = pickedTime.Add(time.Duration(minute) * time.Minute)

	if pickedTime.After(currentDayMidnight.Add(aDay)){
		pickedTime = pickedTime.Add(-aDay)
	}

	return pickedTime
}

//Generate a normal distributed time with expected time as mean
//and duration as standard deviation (in seconds)
//return the time limited to the boundaries defined by business day start/end
func normalDistributedTime(duration int64, expectedTime time.Time) time.Time {
	config := configs.GetInstance()
	bDayStart, error := time.ParseDuration(config.BusinessDayStart())
	if error != nil{
		log.Fatal(error, "BusinessDayStart - couldn't parse duration")
	}

	aDay, error := time.ParseDuration("24h")
	if error != nil{
		log.Fatal(error, "a Day- couldn't parse duration")
	}

	bDayEnd, error := time.ParseDuration(config.BusinessDayEnd())
	if error != nil{
		log.Fatal(error, "BusinessDayEnd - couldn't parse duration")
	}

	maxWait := config.GetMaxTimeInStationPassenger()


	unixTime := expectedTime.Unix()
	var currentDayMidnight = time.Date(expectedTime.Year(),expectedTime.Month(),expectedTime.Day(),0,0,0,0,expectedTime.Location())
	unixBuissnesDayStart := currentDayMidnight.Add(bDayStart).Unix()
	unixBuissnesDayEnd := currentDayMidnight.Add(aDay).Add(bDayEnd).Unix()

	tripUnixTime := int64(rand.NormFloat64()*(float64(duration)) + float64(unixTime))
	if tripUnixTime < unixBuissnesDayStart {
		tripUnixTime = unixBuissnesDayStart // The passenger will catch the first train a the begining of the bussiness day
	}
	if tripUnixTime > unixBuissnesDayEnd {
		// the pasenger will catch one of the last trains, we substract some time so he does enter in the station just when it close
		tripUnixTime = unixBuissnesDayEnd - int64(maxWait.Seconds())
	}

	aTime := time.Unix(tripUnixTime, 0)
	return aTime.UTC()
}

//Generate a random path in a map
//return the path.
func randomPath(aMap Map) pathStation {
	stations := aMap.Stations()
	//generating an index to pick a random station
	departureId := rand.Intn(len(stations))
	departureStation := stations[departureId]
	stations = append(stations[:departureId], stations[departureId+1:]...)

	arrivalStation := stations[rand.Intn(len(stations))]
	path, error := aMap.GetPathStation(departureStation, arrivalStation)
	if error != nil {
		log.Fatal(error, "couldn't get path")
	}
	return path
}

//generate a random trip with a random time and a random path
//return the trip
func RandomTrip(currentDay time.Time, aMap Map) Trip {
	departure := RandomTime(currentDay)
	path := randomPath(aMap)
	trip := NewTrip(departure, path)
	return trip
}

func CommuntingTrips(commutePeriodDuration int64, morningCommute time.Duration, eveningCommute time.Duration, currentDay time.Time, aMap Map) (Trip, Trip) {
	morningTime := normalDistributedTime(int64(commutePeriodDuration), currentDay.Add(morningCommute))
	eveningTime := normalDistributedTime(int64(commutePeriodDuration), currentDay.Add(eveningCommute))
	morningPath := randomPath(aMap)

	eveningPath := *aMap.graph[morningPath.StartStation().Id()][morningPath.EndStation().id]
	morningTrip := NewTrip(morningTime, morningPath)
	eveningTrip := NewTrip(eveningTime, eveningPath)
	return morningTrip, eveningTrip
}

//--- constructors

func (t *Trip) DepartureTime() time.Time {
	return t.departureTime
}

func (t *Trip) ArrivalTime() time.Time {
	return t.arrivalTime
}

func (t *Trip) Path() *pathStation {
	return &t.path
}

func (t *Trip) SetPath(path pathStation) {
	t.path = path
}

func (t *Trip) SetDepartureTime(time time.Time) {
	t.departureTime = time
}

func (t *Trip) SetArrivalTime(time time.Time) {
	t.arrivalTime = time
}

func (t *Trip) IsStarted(currentTime time.Time) bool {
	return t.departureTime.Before(currentTime)
}

func (t *Trip) IsCompleted() bool {
	return !t.arrivalTime.IsZero()
}

func (t *Trip) IsValid(start time.Time, end time.Time) bool {
	return t.departureTime.After(start) && t.departureTime.Before(end)
}

// return a string representation of a trip
func (t *Trip) String() string {
	//station_start\tstation_end\ttime_start\ttime_end
	var output string
	if len(t.path.stations) == 0 {
		return "no path in trip"
	}
	output = "start : " + t.path.stations[0].name + "\t" +
		"end : " + t.path.EndStation().name + "\t" +
		"start time : " + t.departureTime.String() + "\t"

	if t.IsCompleted() {
		output += "end time : " + t.arrivalTime.String()
	} else {
		output += "end time : Not Completed"
	}

	return output
}
